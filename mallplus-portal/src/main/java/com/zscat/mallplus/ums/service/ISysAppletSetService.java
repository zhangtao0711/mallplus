package com.zscat.mallplus.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.sms.entity.SmsWaterBuyRecord;
import com.zscat.mallplus.ums.entity.SysAppletSet;
import com.zscat.mallplus.ums.entity.UmsMember;

import java.math.BigDecimal;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-06-15
 */
public interface ISysAppletSetService extends IService<SysAppletSet> {

    public boolean getRout(Long dealerId, BigDecimal actualFee, String ip,
                           String transaction_id, Long recordId,Integer type);

    public void donateVirtualCard(UmsMember member, Long dealerId);
}
