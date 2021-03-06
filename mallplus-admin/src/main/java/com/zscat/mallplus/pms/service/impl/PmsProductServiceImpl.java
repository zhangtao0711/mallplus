package com.zscat.mallplus.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.cms.service.ICmsPrefrenceAreaProductRelationService;
import com.zscat.mallplus.cms.service.ICmsSubjectProductRelationService;
import com.zscat.mallplus.pms.entity.*;
import com.zscat.mallplus.pms.mapper.*;
import com.zscat.mallplus.pms.service.*;
import com.zscat.mallplus.pms.vo.PmsProductParam;
import com.zscat.mallplus.pms.vo.PmsProductResult;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.ums.entity.UmsMemberTag;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.util.DateUtils;
import com.zscat.mallplus.util.JsonUtil;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.utils.IdWorker;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.Rediskey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements IPmsProductService {

    @Resource
    private PmsProductMapper productMapper;
    @Resource
    private IPmsMemberPriceService memberPriceDao;
    @Resource
    private PmsMemberPriceMapper memberPriceMapper;
    @Resource
    private IPmsProductLadderService productLadderDao;
    @Resource
    private PmsProductLadderMapper productLadderMapper;
    @Resource
    private IPmsProductFullReductionService productFullReductionDao;
    @Resource
    private PmsProductFullReductionMapper productFullReductionMapper;
    @Resource
    private IPmsSkuStockService skuStockDao;
    @Resource
    private PmsSkuStockMapper skuStockMapper;
    @Resource
    private IPmsProductAttributeValueService productAttributeValueDao;
    @Resource
    private PmsProductAttributeValueMapper productAttributeValueMapper;
    @Resource
    private ICmsSubjectProductRelationService subjectProductRelationDao;
    @Resource
    private CmsSubjectProductRelationMapper subjectProductRelationMapper;
    @Resource
    private ICmsPrefrenceAreaProductRelationService prefrenceAreaProductRelationDao;
    @Resource
    private CmsPrefrenceAreaProductRelationMapper prefrenceAreaProductRelationMapper;

    @Resource
    private PmsProductVertifyRecordMapper productVertifyRecordDao;

    @Resource
    private PmsProductVertifyRecordMapper productVertifyRecordMapper;
    @Resource
    private RedisService redisService;

    @Override
    public int create(PmsProductParam productParam) {
        int count;
        //创建商品
        PmsProduct product = productParam;
        product.setCreateTime(new Date());
        product.setId(null);
        //处理sku的编码
        handleSkuStockCode(productParam.getSkuStockList(), product);
        if (ValidatorUtils.isEmpty(product.getProductSn())) {
            product.setProductSn(IdWorker.getId() + "");
        }
        if (ValidatorUtils.empty(product.getExpireTime())) {
            product.setExpireTime(DateUtils.strToDate(DateUtils.addDay(new Date(), 5)));
        }
        if (ValidatorUtils.empty(product.getOriginalPrice())) {
            product.setOriginalPrice(product.getPrice());
        }
        if (ValidatorUtils.empty(product.getUnit())) {
            product.setUnit("件");
        }
        SysUser user = UserUtils.getCurrentMember();
        product.setStoreName(user.getStoreName());
        //product.setStoreId(user.getStoreId());
        if (ValidatorUtils.empty(product.getAlbumPics())) {
            product.setAlbumPics(product.getPic());
        }
       /* Long[] tagList = productParam.getTagLists();
        String tags=null;
        if(tagList!=null && tagList.length>0){
            for (Long tag:tagList){
                tags=tags+tag+",";
            }
            tags.substring(0,tags.length()-1);
        }
        product.setTags(tags);*/
        productMapper.insert(product);
        //根据促销类型设置价格：、阶梯价格、满减价格
        Long productId = product.getId();
        //会员价格
        //   relateAndInsertList(memberPriceDao, productParam.getMemberPriceList(), productId);
        //阶梯价格
        // relateAndInsertList(productLadderDao, productParam.getProductLadderList(), productId);
        //满减价格
        // relateAndInsertList(productFullReductionDao, productParam.getProductFullReductionList(), productId);

        //添加sku库存信息
        relateAndInsertList(skuStockDao, productParam.getSkuStockList(), productId);
        //添加商品参数,添加自定义商品规格
        relateAndInsertList(productAttributeValueDao, productParam.getProductAttributeValueList(), productId);
        //关联专题
        //  relateAndInsertList(subjectProductRelationDao, productParam.getSubjectProductRelationList(), productId);
        //关联优选
        //   relateAndInsertList(prefrenceAreaProductRelationDao, productParam.getPrefrenceAreaProductRelationList(), productId);
        //关联专题

        if (!CollectionUtils.isEmpty(productParam.getSubjectProductRelationList())) {
            for (CmsSubjectProductRelation relation : productParam.getSubjectProductRelationList()) {
                relation.setProductId(productId);
                subjectProductRelationDao.save(relation);
            }
        }
        //关联优选
        if (!CollectionUtils.isEmpty(productParam.getPrefrenceAreaProductRelationList())) {
            for (CmsPrefrenceAreaProductRelation relation : productParam.getPrefrenceAreaProductRelationList()) {
                relation.setProductId(productId);
                prefrenceAreaProductRelationDao.save(relation);
            }
        }
        count = 1;
        redisService.set(String.format(Rediskey.GOODSDETAIL, product.getId()), JsonUtil.objectToJson(productParam));
        return count;
    }

    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, PmsProduct product) {
        if (CollectionUtils.isEmpty(skuStockList)) return;
        int stock = 0;
        for (int i = 0; i < skuStockList.size(); i++) {
            PmsSkuStock skuStock = skuStockList.get(i);
            skuStock.setProductName(product.getName());
            if (StringUtils.isEmpty(skuStock.getSkuCode())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                StringBuilder sb = new StringBuilder();
                //日期
                sb.append(sdf.format(new Date()));
                //四位商品id
                sb.append(String.format("%04d", product.getProductCategoryId()));
                //三位索引id
                sb.append(String.format("%03d", i + 1));
                skuStock.setSkuCode(sb.toString());
            }
            if (skuStock.getStock() != null && skuStock.getStock() > 0) {
                stock = stock + skuStock.getStock();

            }

        }
        product.setStock(stock);
    }

    @Override
    public PmsProductResult getUpdateInfo(Long id) {
        return productMapper.getUpdateInfo(id);
    }

    @Override
    public int update(Long id, PmsProductParam productParam) {

        int count;
        //更新商品信息
        PmsProduct product = productParam;
        product.setId(id);
        if (ValidatorUtils.isEmpty(product.getProductSn())) {
            product.setProductSn(IdWorker.getId() + "");
        }
        handleSkuStockCode(productParam.getSkuStockList(), product);
        /* Long[] tagList = productParam.getTagLists();
        String tags=null;
        if(tagList!=null && tagList.length>0){
            for (Long tag:tagList){
                tags=tags+tag+",";
            }
            tags.substring(0,tags.length()-1);
        }
        product.setTags(tags);*/
        productMapper.updateById(product);
        redisService.remove(String.format(Rediskey.GOODSDETAIL, product.getId()));
        //会员价格
        //  memberPriceMapper.delete(new QueryWrapper<>(new PmsMemberPrice()).eq("product_id", id));
        //  relateAndInsertList(memberPriceDao, productParam.getMemberPriceList(), id);
        //阶梯价格

        productLadderMapper.delete(new QueryWrapper<>(new PmsProductLadder()).eq("product_id", id));
        relateAndInsertList(productLadderDao, productParam.getProductLadderList(), id);
        //满减价格

        productFullReductionMapper.delete(new QueryWrapper<>(new PmsProductFullReduction()).eq("product_id", id));
        relateAndInsertList(productFullReductionDao, productParam.getProductFullReductionList(), id);
        //修改sku库存信息
        skuStockMapper.delete(new QueryWrapper<>(new PmsSkuStock()).eq("product_id", id));

        relateAndInsertList(skuStockDao, productParam.getSkuStockList(), id);
        //修改商品参数,添加自定义商品规格

        productAttributeValueMapper.delete(new QueryWrapper<>(new PmsProductAttributeValue()).eq("product_id", id));
        relateAndInsertList(productAttributeValueDao, productParam.getProductAttributeValueList(), id);

        //关联专题
        subjectProductRelationMapper.delete(new QueryWrapper<>(new CmsSubjectProductRelation()).eq("product_id", id));
        //  relateAndInsertList(subjectProductRelationDao, productParam.getSubjectProductRelationList(), id);
        if (!CollectionUtils.isEmpty(productParam.getSubjectProductRelationList())) {
            for (CmsSubjectProductRelation relation : productParam.getSubjectProductRelationList()) {
                relation.setProductId(id);
                subjectProductRelationDao.save(relation);
            }
        }
        //关联优选

        prefrenceAreaProductRelationMapper.delete(new QueryWrapper<>(new CmsPrefrenceAreaProductRelation()).eq("product_id", id));
        //  relateAndInsertList(prefrenceAreaProductRelationDao, productParam.getPrefrenceAreaProductRelationList(), id);
        if (!CollectionUtils.isEmpty(productParam.getPrefrenceAreaProductRelationList())) {
            for (CmsPrefrenceAreaProductRelation relation : productParam.getPrefrenceAreaProductRelationList()) {
                relation.setProductId(id);
                prefrenceAreaProductRelationDao.save(relation);
            }
        }
        count = 1;

        redisService.set(String.format(Rediskey.GOODSDETAIL, product.getId()), JsonUtil.objectToJson(productParam));
        return count;
    }


    @Override
    public int updateVerifyStatus(Long ids, Integer verifyStatus, String detail) {
        PmsProduct product = new PmsProduct();
        product.setVerifyStatus(verifyStatus);
        int count = productMapper.update(product, new QueryWrapper<PmsProduct>().eq("id", ids));
        //修改完审核状态后插入审核记录

        PmsProductVertifyRecord record = new PmsProductVertifyRecord();
        record.setProductId(ids);
        record.setCreateTime(new Date());
        record.setDetail(detail);
        record.setStatus(verifyStatus);
        record.setVertifyMan(UserUtils.getCurrentMember().getUsername());
        productVertifyRecordMapper.insert(record);
        redisService.remove(String.format(Rediskey.GOODSDETAIL, product.getId()));
        return count;
    }

    @Override
    public int updateisFenxiao(List<Long> ids, Integer newStatus) {
        PmsProduct record = new PmsProduct();
        record.setIsFenxiao(newStatus);
        clerGoodsRedis(ids);
        return productMapper.update(record, new QueryWrapper<PmsProduct>().in("id", ids));
    }

    @Override
    public int updateisVip(List<Long> ids, Integer newStatus) {
        PmsProduct record = new PmsProduct();
        record.setIsVip(newStatus);
        clerGoodsRedis(ids);
        return productMapper.update(record, new QueryWrapper<PmsProduct>().in("id", ids));
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        PmsProduct record = new PmsProduct();
        record.setPublishStatus(publishStatus);
        clerGoodsRedis(ids);
        return productMapper.update(record, new QueryWrapper<PmsProduct>().in("id", ids));
    }

    public void clerGoodsRedis(List<Long> ids) {
        for (Long id : ids) {
            redisService.remove(String.format(Rediskey.GOODSDETAIL, id));
        }
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        PmsProduct record = new PmsProduct();
        record.setRecommandStatus(recommendStatus);
        clerGoodsRedis(ids);
        return productMapper.update(record, new QueryWrapper<PmsProduct>().in("id", ids));
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        PmsProduct record = new PmsProduct();
        record.setNewStatus(newStatus);
        clerGoodsRedis(ids);
        return productMapper.update(record, new QueryWrapper<PmsProduct>().in("id", ids));
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        PmsProduct record = new PmsProduct();
        record.setDeleteStatus(deleteStatus);
        clerGoodsRedis(ids);
        return productMapper.update(record, new QueryWrapper<PmsProduct>().in("id", ids));
    }

    @Override
    public List<PmsProduct> list(String keyword) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("delete_status", 0);

        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like("name", keyword);

        }
        return productMapper.selectList(queryWrapper);
    }

    @Override
    public List<PmsProductVertifyRecord> getProductVertifyRecord(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id", id);

        return productVertifyRecordMapper.selectList(queryWrapper);
    }


    /**
     * 建立和插入关系表操作
     *
     * @param dao       可以操作的dao
     * @param dataList  要插入的数据
     * @param productId 建立关系的id
     */
    private void relateAndInsertList(Object dao, List dataList, Long productId) {
        try {
            if (CollectionUtils.isEmpty(dataList)) return;
            for (Object item : dataList) {
                Method setId = item.getClass().getMethod("setId", Long.class);
                setId.invoke(item, (Long) null);
                Method setProductId = item.getClass().getMethod("setProductId", Long.class);
                setProductId.invoke(item, productId);
            }
            Method insertList = dao.getClass().getMethod("saveBatch", Collection.class);
            insertList.invoke(dao, dataList);
        } catch (Exception e) {

            log.warn("创建产品出错:{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}


