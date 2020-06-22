package com.zscat.mallplus.weixinmp.mapper;


import com.zscat.mallplus.weixinmp.entity.AccountWechats;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author lyn
* @date 2020-05-22
*/
public interface AccountWechatsMapper extends BaseMapper<AccountWechats> {

    Long getLastNum(@Param("dealerId") Long dealerId);
}
