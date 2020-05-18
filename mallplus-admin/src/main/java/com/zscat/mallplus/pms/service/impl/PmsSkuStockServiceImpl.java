package com.zscat.mallplus.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.entity.PmsSkuStock;
import com.zscat.mallplus.pms.mapper.PmsProductMapper;
import com.zscat.mallplus.pms.mapper.PmsSkuStockMapper;
import com.zscat.mallplus.pms.service.IPmsSkuStockService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.vo.Rediskey;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * sku的库存 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class PmsSkuStockServiceImpl extends ServiceImpl<PmsSkuStockMapper, PmsSkuStock> implements IPmsSkuStockService {
    @Resource
    private PmsSkuStockMapper skuStockMapper;
    @Resource
    private PmsProductMapper productMapper;
    @Resource
    private RedisService redisService;
    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        QueryWrapper q = new QueryWrapper();
        q.eq("product_id", pid);

        if (!StringUtils.isEmpty(keyword)) {
            q.like("sku_code", keyword);
        }
        return skuStockMapper.selectList(q);
    }

    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        if (CollectionUtils.isEmpty(skuStockList)) return 0;
        PmsProduct product = productMapper.selectById(pid);
        int stock = 0;
        for (int i = 0; i < skuStockList.size(); i++) {
            PmsSkuStock skuStock = skuStockList.get(i);
            skuStock.setProductName(product.getName());
            if (skuStock.getStock() != null && skuStock.getStock() > 0) {
                stock = stock + skuStock.getStock();
            }
        }
        product.setStock(stock);
        if (productMapper.updateById(product)>0){
            redisService.remove(String.format(Rediskey.GOODSDETAIL, pid + ""));
            return skuStockMapper.replaceList(skuStockList);
        }
        return 0;
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
}
