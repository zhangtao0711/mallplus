package com.zscat.mallplus.single;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.common.CommonConstant;
import com.zscat.mallplus.jifen.entity.JifenActivityLuckyDraw;
import com.zscat.mallplus.jifen.entity.JifenLuckDrawProbability;
import com.zscat.mallplus.jifen.entity.JifenLuckDrawRecord;
import com.zscat.mallplus.jifen.mapper.JifenActivityLuckyDrawMapper;
import com.zscat.mallplus.jifen.mapper.JifenLuckDrawProbabilityMapper;
import com.zscat.mallplus.jifen.mapper.JifenLuckDrawRecordMapper;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.set.entity.SetSalesBuy;
import com.zscat.mallplus.set.mapper.SetSalesBuyMapper;
import com.zscat.mallplus.util.TimeUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.vo.jifen.PrizePool;
import com.zscat.mallplus.vo.jifen.PrizePoolBean;
import com.zscat.mallplus.wxminiapp.mapper.AccountWxappMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Api(tags = "积分抽奖")
@Slf4j
@RestController
@RequestMapping("/api/integral/")
public class IntegralDrawController {
    @Resource
    private JifenActivityLuckyDrawMapper jifenActivityLuckyDrawMapper;
    @Resource
    private JifenLuckDrawProbabilityMapper jifenLuckDrawProbabilityMapper;
    @Resource
    private JifenLuckDrawRecordMapper jifenLuckDrawRecordMapper;
    @Resource
    private AccountWxappMapper wxappMapper;
    @Resource
    private SetSalesBuyMapper buyMapper;
    @Resource
    private IPmsProductService productService;

    /**
     * 获取超级大富翁的奖品池
     * zillionaireProductMap 超级大富翁奖品map
     * @param flag true:有现金  false:无现金
     * @return
     */
    private PrizePool getZillionairePrizePool(Long memberId,JifenActivityLuckyDraw jifenActivityLuckyDraw,boolean flag) {

        //设置
         if (jifenActivityLuckyDraw.getEndTime().before(new Date())){
            PrizePool prizePool = new PrizePool();
            prizePool.setTotal(-1);
            return prizePool;
        }
        List<JifenLuckDrawProbability> jifenLuckDrawProbabilities = jifenLuckDrawProbabilityMapper.selectList(new QueryWrapper<JifenLuckDrawProbability>().eq("activity_id",jifenActivityLuckyDraw.getId()));
        Map<Long, JifenLuckDrawProbability> zillionaireProductMap = new HashMap<Long, JifenLuckDrawProbability>();
        for (int i=0;i<jifenLuckDrawProbabilities.size();i++){
            JifenLuckDrawProbability probability = jifenLuckDrawProbabilities.get(i);
            zillionaireProductMap.put(Long.valueOf(i),probability);
        }
        if (zillionaireProductMap==null||zillionaireProductMap.size()==0){
            PrizePool prizePool = new PrizePool();
            prizePool.setTotal(0);
            return prizePool;
        }

        //总的奖品池值
        int total = 0;
        List<PrizePoolBean> poolBeanList = new ArrayList<>();
        for(Map.Entry<Long, JifenLuckDrawProbability> entry : zillionaireProductMap.entrySet()){
            JifenLuckDrawProbability product = entry.getValue();
            //无现金奖品池，过滤掉类型为现金的奖品,和一些设置的奖品限制
            PmsProduct pmsProduct = productService.getById(product.getPointsPrizeId());
            if(!flag && !pmsProduct.getProductType().equals(CommonConstant.productType_2)){
                continue;
            }
            //获取商品的今天的抽奖次数
            List<JifenLuckDrawRecord> jifenLuckDrawRecords = jifenLuckDrawRecordMapper.selectList(new QueryWrapper<JifenLuckDrawRecord>().eq("points_prize_id",product.getPointsPrizeId()).eq("date_format(create_time,'%Y-%m-%d'))", TimeUtil.getStringTodayTime()));
            if (jifenLuckDrawRecords!=null&&jifenLuckDrawRecords.size()==product.getPrizeDayMaxTimes()){
                continue;
            }
            List<JifenLuckDrawRecord> records = jifenLuckDrawRecordMapper.selectList(new QueryWrapper<JifenLuckDrawRecord>().eq("member_id",memberId).eq("points_prize_id",product.getPointsPrizeId()).eq("date_format(create_time,'%Y-%m-%d'))", TimeUtil.getStringTodayTime()));
            if (records!=null&&jifenLuckDrawRecords.size()==product.getUserPrizeMonthMaxTimes()){
                continue;
            }
            //组装奖品池奖品
            PrizePoolBean prizePoolBean = new PrizePoolBean();
            prizePoolBean.setId(product.getPointsPrizeId());
            prizePoolBean.setName(pmsProduct.getName());
            prizePoolBean.setImg(pmsProduct.getPic());
            prizePoolBean.setBegin(total);
//            total = total + product.getEarnings().multiply(new BigDecimal("10000")).intValue();
            total = total + (product.getProbability().multiply(new BigDecimal("10000"))).intValue();
            prizePoolBean.setEnd(total);
            poolBeanList.add(prizePoolBean);
        }
        if (poolBeanList!=null&&poolBeanList.size()<10){
            int plus = 10-poolBeanList.size();
            for (int i=0;i<plus;i++){
                //组装奖品池奖品
                PrizePoolBean prizePoolBean = new PrizePoolBean();
//                prizePoolBean.setId(product.getPointsPrizeId());
                prizePoolBean.setName("谢谢惠顾！");
//                prizePoolBean.setImg(pmsProduct.getPic());
                prizePoolBean.setBegin(total);
//            total = total + product.getEarnings().multiply(new BigDecimal("10000")).intValue();
                total = total + (new BigDecimal("0.8").multiply(new BigDecimal("10000"))).intValue();
                prizePoolBean.setEnd(total);
                poolBeanList.add(prizePoolBean);
            }
        }
        PrizePool prizePool = new PrizePool();
        prizePool.setTotal(total);
        prizePool.setPoolBeanList(poolBeanList);
        return prizePool;
    }

    @ApiOperation(value = "用户抽奖页面")
    @PostMapping("luckyDrawPage")
    public Object luckyDrawPage(@RequestParam Integer uniacid){
        JSONObject j = new JSONObject();
        Long dealerId = wxappMapper.getDealerIdByUniacid(uniacid);
        SetSalesBuy salesBuy = new SetSalesBuy();
        salesBuy.setPerssionId(593L);
        salesBuy.setDealerId(dealerId);
        SetSalesBuy buy = buyMapper.selectOne(new QueryWrapper<>(salesBuy));
        if (buy==null||buy.getEndTime().before(new Date())){
            return new CommonResult().failed("对不起，该经销商没有购买此项功能！");
        }
        JifenActivityLuckyDraw jifenActivityLuckyDraw = jifenActivityLuckyDrawMapper.selectOne(new QueryWrapper<>());
        if (jifenActivityLuckyDraw==null){
            return new CommonResult().failed("抽奖活动未开始，敬请期待！");
        }
        j.put("jifenActivityLuckyDraw",jifenActivityLuckyDraw);
        List<JifenLuckDrawProbability> jifenLuckDrawProbabilities = jifenLuckDrawProbabilityMapper.selectList(new QueryWrapper<JifenLuckDrawProbability>().eq("activity_id",jifenActivityLuckyDraw.getId()));
        j.put("jifenLuckDrawProbabilities",jifenLuckDrawProbabilities);
        return new CommonResult().success(j);
    }

    @ApiOperation(value = "用户抽奖功能")
    @PostMapping("luckyDraw")
    public Object luckyDraw(@RequestBody JifenLuckDrawRecord record){
        JifenActivityLuckyDraw jifenActivityLuckyDraw = jifenActivityLuckyDrawMapper.selectOne(new QueryWrapper<>());
        if (jifenActivityLuckyDraw==null){
            return new CommonResult().failed("抽奖活动未开始，敬请期待！");
        }
        List<JifenLuckDrawRecord> jifenLuckDrawRecords = jifenLuckDrawRecordMapper.selectList(new QueryWrapper<JifenLuckDrawRecord>().eq("member_id",record.getMemberId()).eq("date_format(create_time,'%Y-%m-%d'))", TimeUtil.getStringTodayTime()));
        if (jifenLuckDrawRecords!=null&&jifenLuckDrawRecords.size()==jifenActivityLuckyDraw.getTriesLimit()){
            return new CommonResult().failed("您今天的抽奖次数已用完，请明天抽取！");
        }
        PrizePool prizePool = getZillionairePrizePool(record.getMemberId(),jifenActivityLuckyDraw,false);
        if (prizePool.getTotal()==-1){
            return new CommonResult().failed("抽奖活动已结束，敬请期待下一期！");
        }
        if (prizePool.getTotal()==0){
            return new CommonResult().failed("暂时没有抽奖活动，敬请期待！");
        }
        PrizePoolBean prizePoolBean = getPrize(prizePool);
        //添加记录
        if (prizePoolBean==null||prizePoolBean.getId()==null){//没中奖
            record.setPrizeId(0L);
            record.setResult(2);
            prizePoolBean.setName("谢谢参与");
            jifenLuckDrawRecordMapper.insert(record);
        }else {//中奖
            record.setPrizeId(prizePoolBean.getId());
            record.setResult(1);
            record.setMonth(String.valueOf(TimeUtil.getNowMonth()));
            record.setDaily(new Date());
            jifenLuckDrawRecordMapper.insert(record);
        }
        return new CommonResult().success(prizePoolBean);
    }

    public static PrizePoolBean getPrize(PrizePool prizePool){
        //获取总的奖品池值
        int total = prizePool.getTotal();
        //获取随机数
        Random rand=new Random();
        int random=rand.nextInt(total);
        //循环比较奖品池区间
        for(PrizePoolBean prizePoolBean : prizePool.getPoolBeanList()){
            if(random >= prizePoolBean.getBegin() && random < prizePoolBean.getEnd()){
                return prizePoolBean;
            }
        }
        return null;
    }

    @ApiOperation("根据条件查询所有抽奖列表")
    @GetMapping(value = "list")
    public Object list(JifenLuckDrawRecord entity,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(jifenLuckDrawRecordMapper.selectPage(new Page<JifenLuckDrawRecord>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有抽奖列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
}
