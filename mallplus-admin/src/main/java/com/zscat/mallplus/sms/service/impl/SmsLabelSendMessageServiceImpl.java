package com.zscat.mallplus.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.sms.entity.SmsLabelSendMessage;
import com.zscat.mallplus.sms.entity.SmsLabelSet;
import com.zscat.mallplus.sms.mapper.SmsLabelSendMessageMapper;
import com.zscat.mallplus.sms.mapper.SmsLabelSetMapper;
import com.zscat.mallplus.sms.service.ISmsLabelSendMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.sys.mapper.SysUserMapper;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.mapper.UmsMemberMapper;
import com.zscat.mallplus.weixinmp.entity.AccountWechats;
import com.zscat.mallplus.weixinmp.mapper.AccountWechatsMapper;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpMassTagMessage;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
* @author wang
* @date 2020-05-30
*/
@Service
public class SmsLabelSendMessageServiceImpl extends ServiceImpl
<SmsLabelSendMessageMapper, SmsLabelSendMessage> implements ISmsLabelSendMessageService {

@Resource
private  SmsLabelSendMessageMapper smsLabelSendMessageMapper;
@Resource
private SmsLabelSetMapper labelSetMapper;
@Resource
private SysUserMapper userMapper;
@Resource
private UmsMemberMapper memberMapper;
@Resource
private AccountWechatsMapper wechatsMapper;
@Resource
private WxMpService wxMpService;


    @Override
    public String sendWxMessage(SmsLabelSendMessage message) throws WxErrorException {
        //区分一下是全部还是标签发送
        if (message.getSendType()==1){
            //指定标签发送人
            List<UmsMember> list = memberMapper.getMemberByLabelId(message.getLabelId());
            //获取了数据之后选择发送 公众号发送
            Integer uniacid = list.get(0).getUniacid();
            AccountWechats accountWechats = new AccountWechats();
            accountWechats.setUniacid(uniacid);
            AccountWechats wxapp = wechatsMapper.selectOne(new QueryWrapper<>(accountWechats));
            if (wxapp==null){
                //公众号没有绑定
                return null;
            }else {
                //公众号发送信息 群发
                SmsLabelSet set = labelSetMapper.selectById(message.getLabelId());
                return testTextMassMessageSend(wxapp.getKey(),message.getMessage(),set.getTagId());
            }

        }else if (message.getSendType()==2){
            //全部发送
            //获取一下经销商的公众号的id
            AccountWechats wxapp = this.getAppId(message.getDealerId());
            if (wxapp==null){
                //公众号没有绑定
                return null;
            }else {
                //公众号发送信息 群发
                return testTextMassGroupMessageSend(wxapp.getKey(),message.getMessage());
            }
        }
        return null;
    }

    @Override
    public AccountWechats getAppId(Long dealerId) {
        AccountWechats accountWechats = new AccountWechats();
        accountWechats.setCreateBy(dealerId);
        AccountWechats wxapp = wechatsMapper.selectOne(new QueryWrapper<>(accountWechats));
        if (wxapp==null){
            SysUser user = userMapper.selectById(dealerId);
            if (user.getPid()==0){
                //平台的
                AccountWechats accountWechats1 = new AccountWechats();
                accountWechats1.setUniacid(1);
                accountWechats1.setStoreId(1);
                wxapp = wechatsMapper.selectOne(new QueryWrapper<>(accountWechats1));
            }else {
                //用上级的
                AccountWechats accountWechats2 = new AccountWechats();
                accountWechats2.setCreateBy(user.getPid());
                wxapp = wechatsMapper.selectOne(new QueryWrapper<>(accountWechats));
                if (wxapp == null){
                    //上级没有怎么办，找上上级
                    if (user.getGid()==0){
                        //平台的
                        AccountWechats accountWechats1 = new AccountWechats();
                        accountWechats1.setUniacid(1);
                        accountWechats1.setStoreId(1);
                        wxapp = wechatsMapper.selectOne(new QueryWrapper<>(accountWechats1));
                    }else {
                        AccountWechats accountWechats3 = new AccountWechats();
                        accountWechats3.setCreateBy(user.getGid());
                        wxapp = wechatsMapper.selectOne(new QueryWrapper<>(accountWechats));
                        if (wxapp==null){
                            //平台的
                            AccountWechats accountWechats1 = new AccountWechats();
                            accountWechats1.setUniacid(1);
                            accountWechats1.setStoreId(1);
                            wxapp = wechatsMapper.selectOne(new QueryWrapper<>(accountWechats1));
                        }
                    }
                }
            }
        }
        return wxapp;
    }

    private String testTextMassMessageSend(String appid,String content,Long tagId) throws WxErrorException {
        // 发送群发消息
        WxMpMassTagMessage massMessage = new WxMpMassTagMessage();
        massMessage.setMsgType(WxConsts.MassMsgType.TEXT);
        massMessage.setContent(content);
        massMessage.setTagId(tagId);

        WxMpMassSendResult massResult = this.wxMpService.switchoverTo(appid).getMassMessageService().massGroupMessageSend(massMessage);
        if (massResult==null||massResult.getMsgId()==null){
            return null;
        }else {
            return massResult.getMsgId();
        }
    }

    public String testTextMassGroupMessageSend(String appid,String content) throws WxErrorException {
        WxMpMassTagMessage massMessage = new WxMpMassTagMessage();
        massMessage.setMsgType(WxConsts.MassMsgType.TEXT);
        massMessage.setContent(content);
        massMessage.setSendAll(true);

        WxMpMassSendResult massResult = this.wxMpService.switchoverTo(appid).getMassMessageService().massGroupMessageSend(massMessage);
        if (massResult==null||massResult.getMsgId()==null){
            return null;
        }else {
            return massResult.getMsgId();
        }
    }
}
