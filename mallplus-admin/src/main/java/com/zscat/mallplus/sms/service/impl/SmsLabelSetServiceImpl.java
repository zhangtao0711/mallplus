package com.zscat.mallplus.sms.service.impl;

import com.zscat.mallplus.sms.entity.SmsLabelSet;
import com.zscat.mallplus.sms.mapper.SmsLabelSetMapper;
import com.zscat.mallplus.sms.service.ISmsLabelSetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Map;

/**
* @author wang
* @date 2020-05-30
*/
@Service
public class SmsLabelSetServiceImpl extends ServiceImpl
<SmsLabelSetMapper, SmsLabelSet> implements ISmsLabelSetService {

@Resource
private  SmsLabelSetMapper smsLabelSetMapper;


    @Override
    public Integer getCount(String labelName) {
        Integer count = 0;
        if (labelName.equals("会员余额")){
            Map<String,Object> map = smsLabelSetMapper.selectBalanceCount(labelName);
            count = Integer.parseInt(map.get("total").toString());
        }else if (labelName.equals("用水频次")){

        }else if (labelName.equals("推荐客户频次")){

        }else if (labelName.equals("用户社区")){

        }else if (labelName.equals("会员等级")){
            count=0;
        }

        return count;
    }
}
