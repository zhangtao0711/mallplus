package com.zscat.mallplus.water.service.impl;

import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.water.entity.WtWaterCard;
import com.zscat.mallplus.water.entity.WtWaterCardCreate;
import com.zscat.mallplus.water.entity.WtWaterCardExcel;
import com.zscat.mallplus.water.mapper.WtWaterCardCreateMapper;
import com.zscat.mallplus.water.mapper.WtWaterCardMapper;
import com.zscat.mallplus.water.service.IWtWaterCardCreateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.weixinmp.mapper.AccountWechatsMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import com.zscat.mallplus.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lyn
 * @date 2020-05-29
 */
@Service
public class WtWaterCardCreateServiceImpl extends ServiceImpl
        <WtWaterCardCreateMapper, WtWaterCardCreate> implements IWtWaterCardCreateService {

    @Resource
    private WtWaterCardCreateMapper wtWaterCardCreateMapper;
    @Resource
    private WtWaterCardMapper wtWaterCardMapper;
    @Resource
    private AccountWechatsMapper accountWechatsMapper;

    @Override
    public boolean saveAll(WtWaterCardCreate var,String acKey){
        if(super.save(var)){
            for(Long i =Long.valueOf(var.getStartNo());i<=Long.valueOf(var.getEndNo());i++){
                //左补位到10位
                String num = StringUtils.padRight(i.toString(),10,'0');
                //卡信息保存
                WtWaterCard wtWaterCard = new WtWaterCard();
                wtWaterCard.setCardCreateId(var.getId());//生成批号
                wtWaterCard.setCardNo(num);//卡号
                wtWaterCard.setQrCode(ConstantUtil.weiXin_Subscription+acKey+"?carNo="+num);//二维码
                wtWaterCard.setCreateBy(var.getCreateBy());
                wtWaterCard.setCreateTime(var.getCreateTime());
                wtWaterCard.setDelFlag(ConstantUtil.delFlag);
                wtWaterCard.setState(ConstantUtil.water_code_state_0);//卡状态正常
                wtWaterCard.setStoreId(null);
                wtWaterCardMapper.insert(wtWaterCard);
            }
        }
        return true;
    }
    //卡号是否重复
    public boolean checkNum(Long sta, Long end){
        if(wtWaterCardCreateMapper.getNum(sta,end)!=null){
            return false;
        }
        return true;
    }
    //制卡信息下载
    public List<WtWaterCardExcel> getExport(String delFlag, Long id){
        return wtWaterCardCreateMapper.getExport(delFlag,id);
    }
}
