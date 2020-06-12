package com.zscat.mallplus.wxminiapp.mapper;


import com.zscat.mallplus.wxminiapp.entity.AccountWxapp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author wang
* @date 2020-05-23
*/
public interface AccountWxappMapper extends BaseMapper<AccountWxapp> {

    public Long getDealerIdByUniacid(@Param("uniacid")Integer uniacid);
}
