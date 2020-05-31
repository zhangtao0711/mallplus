package com.zscat.mallplus.wxminiapp.service.impl;

import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.wxminiapp.entity.AccountWxapp;
import com.zscat.mallplus.wxminiapp.mapper.AccountWxappMapper;
import com.zscat.mallplus.wxminiapp.service.IAccountWxappService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author wang
 * @date 2020-05-23
 */
@Service
public class AccountWxappServiceImpl extends ServiceImpl
        <AccountWxappMapper, AccountWxapp> implements IAccountWxappService {

    @Resource
    private AccountWxappMapper accountWxappMapper;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private RedisService redisService;


    @Override
    public Integer getCount() {
        Map<String,Object> map = jdbcTemplate.queryForMap("select sum(sum1) as total from ( select count(*) as sum1 from account_wxapp union all select count(*) as sum1 from  account_wechats ) as total");
        Integer count = Integer.parseInt(map.get("total").toString());
        if (count==0){
            redisService.set("uniacid","0");
            count=Math.toIntExact(redisService.increment("uniacid", 1));
        }else {
            if (redisService.exists("uniacid")){
                count = Math.toIntExact(redisService.increment("uniacid", 1));
            }else {
                redisService.set("uniacid",count.toString());
                count = Math.toIntExact(redisService.increment("uniacid", 1));
            }
        }
        return count;
    }
}
