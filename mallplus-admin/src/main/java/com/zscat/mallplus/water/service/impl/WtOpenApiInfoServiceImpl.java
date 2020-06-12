package com.zscat.mallplus.water.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.util.SimCodeUtil;
import com.zscat.mallplus.util.WtOpenApiInfoUtils;
import com.zscat.mallplus.water.entity.WtOpenApiInfo;
import com.zscat.mallplus.water.entity.WtSimUrlInfo;
import com.zscat.mallplus.water.mapper.WtOpenApiInfoMapper;
import com.zscat.mallplus.water.service.IWtOpenApiInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-06-10
 */
@Service
public class WtOpenApiInfoServiceImpl extends ServiceImpl
        <WtOpenApiInfoMapper, WtOpenApiInfo> implements IWtOpenApiInfoService {

    @Resource
    private WtOpenApiInfoMapper wtOpenApiInfoMapper;
    //获取设备实例
   public Object deviceInstanceQuery(Integer pageSize){
       try{
           WtOpenApiInfo coupon = new WtOpenApiInfo();
           coupon = wtOpenApiInfoMapper.selectOne(new QueryWrapper<>(coupon));
           Map<String,String> queryParams = new HashMap<>();
           queryParams.put("pageIndex","0");
           queryParams.put("pageSize",pageSize.toString());
           JSONObject data= WtOpenApiInfoUtils.getMapping(coupon,"/api/device",queryParams);
           return null;
       }catch (Exception e){

       }
       return null;
   }

}
