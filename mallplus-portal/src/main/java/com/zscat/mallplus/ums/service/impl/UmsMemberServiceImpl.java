package com.zscat.mallplus.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.zscat.mallplus.common.CommonConstant;
import com.zscat.mallplus.config.MallplusProperties;
import com.zscat.mallplus.enums.AllEnum;
import com.zscat.mallplus.exception.ApiMallPlusException;
import com.zscat.mallplus.jifen.entity.JifenDealerIntegrationChangeHistory;
import com.zscat.mallplus.jifen.mapper.JifenDealerIntegrationChangeHistoryMapper;
import com.zscat.mallplus.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.oms.vo.OrderStstic;
import com.zscat.mallplus.set.entity.SetSalesBuy;
import com.zscat.mallplus.set.mapper.SetSalesBuyMapper;
import com.zscat.mallplus.sms.entity.SmsLabelMember;
import com.zscat.mallplus.sms.mapper.SmsLabelMemberMapper;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.sys.entity.SysUserStaff;
import com.zscat.mallplus.sys.mapper.SysAreaMapper;
import com.zscat.mallplus.sys.mapper.SysUserMapper;
import com.zscat.mallplus.sys.mapper.SysUserStaffMapper;
import com.zscat.mallplus.ums.entity.*;
import com.zscat.mallplus.ums.mapper.SysAppletSetMapper;
import com.zscat.mallplus.ums.mapper.UmsIntegrationConsumeSettingMapper;
import com.zscat.mallplus.ums.mapper.UmsMemberMapper;
import com.zscat.mallplus.ums.mapper.UmsMemberMemberTagRelationMapper;
import com.zscat.mallplus.ums.service.*;
import com.zscat.mallplus.util.*;
import com.zscat.mallplus.util.applet.StringConstantUtil;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.vo.*;
import com.zscat.mallplus.water.entity.WtWaterCard;
import com.zscat.mallplus.water.mapper.WtWaterCardMapper;
import com.zscat.mallplus.weixinmp.entity.AccountWechats;
import com.zscat.mallplus.weixinmp.mapper.AccountWechatsMapper;
import com.zscat.mallplus.wxminiapp.entity.AccountWxapp;
import com.zscat.mallplus.wxminiapp.mapper.AccountWxappMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@Service
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember> implements IUmsMemberService {

    public final static String getPageOpenidUrl = "https://api.weixin.qq.com/sns/jscode2session";
    public final static String GetPageAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    //微信公众号获取用户信息
    public final static String GetPageUserInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    //微信小程序获取用户信息
    public final static String GetPageUserInfoUrl_XCX = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsMemberServiceImpl.class);
    @Resource
    private MallplusProperties mallplusProperties;

    @Autowired
    OssAliyunUtil aliyunOSSUtil;
    Integer regJifen = 100;
    Integer logginJifen = 5;
    @Resource
    UmsIntegrationConsumeSettingMapper integrationConsumeSettingMapper;
    @Resource
    private ISysAppletSetService appletSetService;
    @Resource
    private IUmsMemberBlanceLogService memberBlanceLogService;
    @Resource
    private UmsMemberMapper memberMapper;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    @Resource
    private RedisService redisService;
    /* @Resource
     private AuthenticationManager authenticationManager;*/
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private SmsService smsService;
    @Resource
    private SysAreaMapper areaMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${authCode.expire.seconds}")
    private Long AUTH_CODE_EXPIRE_SECONDS;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${aliyun.sms.expire-minute:1}")
    private Integer expireMinute;
    @Value("${aliyun.sms.day-count:30}")
    private Integer dayCount;
    @Resource
    private UmsMemberMemberTagRelationMapper umsMemberMemberTagRelationMapper;
    @Resource
    private IUmsMemberLevelService memberLevelService;
    @Resource
    private OmsOrderMapper omsOrderMapper;
    @Resource
    private SysUserMapper adminMapper;
    @Resource
    private SysUserStaffMapper staffMapper;
    @Resource
    private IUmsMemberBlanceLogService blanceLogService;
    @Resource
    private IUmsIntegrationChangeHistoryService umsIntegrationChangeHistoryService;
    @Resource
    private JifenDealerIntegrationChangeHistoryMapper jifenDealerIntegrationChangeHistoryMapper;
    @Resource
    private AccountWxappMapper accountWxappMapper;
    @Resource
    private AccountWechatsMapper accountWechatsMapper;
    @Resource
    private WtWaterCardMapper waterCardMapper;
    @Resource
    private SetSalesBuyMapper buyMapper;
    private OkHttpClient okHttpClient = new OkHttpClient();

    /**
     * 微信访问获取结果
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String httpClientSend(String url) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response = client.execute(httpget, responseHandler);
        return response;

    }

    @Override
    public UmsMember getNewCurrentMember() {
        return (UmsMember) this.getCurrentMember();
    }

    @Override
    public String getWxPhone(String openid, String keyStr, String ivStr, String encDataStr) {
        //解析手机号
        WxPhoneInfo wxPhoneInfo = null;
        System.err.println("encDataStr=" + encDataStr + ",keyStr=" + keyStr + ",ivStr=" + ivStr);
        try {
            String result = AES.wxDecrypt(encDataStr, keyStr, ivStr);
            Gson gson = new Gson();
            wxPhoneInfo = gson.fromJson(result, WxPhoneInfo.class);
            if (wxPhoneInfo != null) {
                return wxPhoneInfo.getPhoneNumber();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object webLogin(String wxH5Appid, String wxH5Secret, String code) {
        //H5 微信公众号网页登录
        try {
            log.info("https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                    + wxH5Appid + "&secret=" + wxH5Secret + "&code=" + code + "&grant_type=authorization_code");
            String json = okHttpClient.newCall(
                    new Request.Builder().url("https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                            + wxH5Appid + "&secret=" + wxH5Secret + "&code=" + code + "&grant_type=authorization_code").build()).execute().body().string();
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(json);
            log.info(jsonObject.toJSONString());
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode == null || errcode == 0) {
                String openid = jsonObject.getString("openid");
                Map<String, Object> resultObj = new HashMap<String, Object>();
                UmsMember userVo = this.queryByOpenId(openid,Integer.parseInt(jsonObject.getString("uniacid")));
                String token = null;
                if (null == userVo) {
                    UmsMember umsMember = new UmsMember();
                    umsMember.setUsername("wxapplet" + CharUtil.getRandomString(12));
                    umsMember.setSourceType(2);
                    umsMember.setPassword(passwordEncoder.encode("123456"));
                    umsMember.setCreateTime(new Date());
                    umsMember.setStatus(1);
                    umsMember.setBlance(new BigDecimal(10000));
                    umsMember.setIntegration(0);
                    umsMember.setMemberLevelId(4L);
                    umsMember.setWeixinOpenid(openid);
                    memberMapper.insert(umsMember);
                    token = jwtTokenUtil.generateToken(umsMember.getUsername());
                    resultObj.put("userId", umsMember.getId());
                    resultObj.put("userInfo", umsMember);
                    addIntegration(umsMember.getId(), regJifen, 1, "注册添加积分", AllEnum.ChangeSource.register.code(), umsMember.getUsername(),null);

                } else {
//                    addIntegration(userVo.getId(), logginJifen, 1, "登录添加积分", AllEnum.ChangeSource.login.code(), userVo.getUsername(),null);

                    token = jwtTokenUtil.generateToken(userVo.getUsername());
                    resultObj.put("userId", userVo.getId());
                    resultObj.put("userInfo", userVo);
                }


                if (StringUtils.isEmpty(token)) {
                    throw new ApiMallPlusException("登录失败");

                }
                resultObj.put("tokenHead", tokenHead);
                resultObj.put("token", token);


                return new CommonResult().success(resultObj);
            } else {
                throw new ApiMallPlusException(jsonObject.toJSONString());
            }
        } catch (ApiMallPlusException e) {
            e.printStackTrace();
            throw new ApiMallPlusException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiMallPlusException(e.getMessage());
        }

    }

    @Override
    public Object getCurrentMember() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String requestType = ((HttpServletRequest) request).getMethod();
            if ("OPTIONS".equals(requestType)) {
                return null;
            }
           /* UmsMember member = UserUtils.getCurrentMember();
            if (member != null && member.getId() != null) {
                return member;
            }*/

            String tokenPre = "authorization";
            String authHeader = request.getParameter(tokenPre);
            if (ValidatorUtils.empty(authHeader)) {
                authHeader = request.getHeader(tokenPre);
            }
            if (authHeader != null && authHeader.startsWith("Bearer")) {
                String authToken = authHeader.substring("Bearer".length());
                String username = jwtTokenUtil.getUserNameFromToken(authToken);
                if (ValidatorUtils.notEmpty(username)) {
                    UmsMember member = JsonUtils.jsonToPojo(redisService.get(String.format(Rediskey.MEMBER, username)), UmsMember.class);
                    if (member == null || member.getId() == null) {
                        member = getByUsername(username);
                    }
                    return member;
                }
            } else {
               /* UmsMember member = UserUtils.getCurrentMember();
                if (member != null && member.getId() != null) {
                    return member;
                } else {
                    return new UmsMember();
                }*/
            }
            return new UmsMember();
        } catch (Exception e) {
            e.printStackTrace();
            return new UmsMember();
        }
    }

    @Override
    public void updataMemberOrderInfo() {
        List<OrderStstic> orders = omsOrderMapper.listOrderGroupByMemberId();
        List<UmsMemberLevel> levelList = memberLevelService.list(new QueryWrapper<UmsMemberLevel>().orderByDesc("price"));
        for (OrderStstic o : orders) {
            UmsMember member = new UmsMember();
            member.setId(o.getMemberId());
            member.setBuyMoney(o.getTotalPayAmount());
            for (UmsMemberLevel level : levelList) {
                if (member.getBuyMoney() != null) {
                    if (member.getBuyMoney().compareTo(level.getPrice()) >= 0) {
                        member.setMemberLevelId(level.getId());
                        member.setMemberLevelName(level.getName());
                        break;
                    }
                }
            }
            member.setBuyCount(o.getTotalCount());
            memberMapper.updateById(member);
        }
    }

    /**
     * 添加余额记录 并更新用户余额
     *
     * @param id
     * @param integration
     */
    @Override
    public void addBlance(Long id, Integer integration, int type, String note) {

        UmsMemberBlanceLog blanceLog = new UmsMemberBlanceLog();
        blanceLog.setMemberId(id);
        blanceLog.setPrice(new BigDecimal(integration));
        blanceLog.setCreateTime(new Date());
        blanceLog.setType(type);
        blanceLog.setNote(note);
        blanceLogService.save(blanceLog);
        UmsMember member = memberMapper.selectById(id);
        member.setBlance(member.getBlance().add(blanceLog.getPrice()));
        memberMapper.updateById(member);

    }

    /**
     * 添加积分记录 并更新用户积分
     *
     * @param id
     * @param uniacid 小程序的id
     */
    @Override
    public void addIntegration(Long id, Integer uniacid, int changeType, String note, int sourceType, String operateMan,BigDecimal price) {
        // 这里是每个经销商都不一样，看那个经销商的小程序
        Integer integration = 0;
        BigDecimal waterFee = new BigDecimal("0");
        //1.获取小程序的信息从而获取是那个经销商的store_id和create_by
        AccountWxapp wxapp = accountWxappMapper.selectById(uniacid);
        //2.获取设置的注册赠送积分信息
        UmsIntegrationConsumeSetting consumeSetting = new UmsIntegrationConsumeSetting();
        consumeSetting.setDealerId(wxapp.getCreateBy());
        consumeSetting.setStoreId(wxapp.getStoreId());
        UmsIntegrationConsumeSetting setting = integrationConsumeSettingMapper.selectOne(new QueryWrapper<>());
        if (setting == null) {
            return;
        }
        UmsIntegrationChangeHistory history = new UmsIntegrationChangeHistory();
        JifenDealerIntegrationChangeHistory integrationHistory = new JifenDealerIntegrationChangeHistory();
        history.setMemberId(id);
        if (sourceType == AllEnum.ChangeSource.register.code()) {
            //校验经销商有没有注册赠送的功能
            SetSalesBuy salesBuy = new SetSalesBuy();
            salesBuy.setPerssionId(593L);
            salesBuy.setDealerId(wxapp.getCreateBy());
            SetSalesBuy buy = buyMapper.selectOne(new QueryWrapper<>(salesBuy));
            if (buy==null||buy.getEndTime().before(new Date())){
                return;
            }
            if (setting.getRegister()!=null&&setting.getRegister()!=0){
                integration = setting.getRegister();
                history.setChangeCount(integration);
                integrationHistory.setChangeCount(integration);
            }
            if (setting.getWaterFee()!=null&&setting.getWaterFee().compareTo(waterFee)>0){
                waterFee = setting.getWaterFee().setScale(BigDecimal.ROUND_DOWN,2);
                history.setChangeFee(waterFee);
                integrationHistory.setChangeBalance(waterFee);
            }
        } else if (sourceType == AllEnum.ChangeSource.login.code()) {
            history.setChangeCount(setting.getLogin());
            integration = setting.getLogin();
            integrationHistory.setChangeCount(integration);
        }
        if (sourceType == AllEnum.ChangeSource.order.code()) {
            if (setting.getOrdersStatus()==0 || setting.getDeductionPerAmount()==null){
                return;
            }
            Integer change = price.multiply(new BigDecimal(setting.getDeductionPerAmount())).setScale(BigDecimal.ROUND_DOWN,0).intValue();
            history.setChangeCount(change);
            integration = change;
            integrationHistory.setChangeCount(integration);
        }
        if (sourceType == AllEnum.ChangeSource.sign.code()) {
            history.setChangeCount(setting.getSign());
            integration = setting.getSign();
            integrationHistory.setChangeCount(integration);
        }

        history.setCreateTime(new Date());
        history.setChangeType(changeType);
        history.setOperateNote(note);
        history.setSourceType(sourceType);
        history.setOperateMan(operateMan);

        integrationHistory.setCreateTime(new Date());
        integrationHistory.setChangeType(changeType);
        integrationHistory.setOperateNote(note);
        integrationHistory.setSourceType(sourceType);
        integrationHistory.setOperateMan(operateMan);

        UmsMember member = memberMapper.selectById(id);
        if (member == null) {
            return;
        } else {
            if (member != null && ValidatorUtils.empty(member.getIntegration())) {
                member.setIntegration(0);
            }
            //确定一下是赠送积分还是水费,水费的话
            if (waterFee.compareTo(new BigDecimal("0"))>0){
                //设计还没确定水费谁出----鑫鑫 5-28 11:41:17 我问了甄姐了，甄姐说不用扣,这个就是白送
                //在这里把水费加上，找到卡，一个经销商一个用户一张虚拟卡
                WtWaterCard waterCard=new WtWaterCard();
                //加上卡的type
                waterCard.setCardType(StringConstantUtil.card_type_1);
                waterCard.setDealerId(wxapp.getCreateBy());
                waterCard.setUmsMemberId(id);
                WtWaterCard card = waterCardMapper.selectOne(new QueryWrapper<>(waterCard));
                if (card==null){
                    //TODO 送虚拟卡
                    appletSetService.donateVirtualCard(member,wxapp.getCreateBy());
                }
                if (card.getCardMoney()==null){
                    card.setCardMoney(new BigDecimal("0"));
                }
                if (card.getGiveMoney()==null){
                    card.setGiveMoney(new BigDecimal("0"));
                }
                card.setGiveMoney(card.getGiveMoney().add(waterFee).setScale(BigDecimal.ROUND_DOWN,2));
                card.setCardMoney(card.getCardMoney().add(waterFee).setScale(BigDecimal.ROUND_DOWN,2));
                card.setUpdateBy(1L);
                card.setUpdateTime(new Date());
                waterCardMapper.updateById(card);
                history.setChangeFee(waterFee);
                umsIntegrationChangeHistoryService.save(history);
                redisService.set(String.format(Rediskey.MEMBER, member.getUsername()), JsonUtils.objectToJson(member));
                return;
            }else if (integration==0){
                return;
             }
            //1.首先比较添加的积分是否比经销商的多，多的话，积分为冻结状态
            SysUser dealer = adminMapper.selectById(wxapp.getCreateBy());
            if (dealer==null){
                return;
            }
            integrationHistory.setDealerId(dealer.getId());
            if (dealer.getIntegration()==null){
                dealer.setIntegration(0);
            }
            if (dealer.getIntegration()<integration || dealer.getIntegrationStatus() == null || dealer.getIntegrationStatus()== StringConstantUtil.integrationStatus_0){
                dealer.setIntegrationStatus(StringConstantUtil.integrationStatus_0);
                adminMapper.updateById(dealer);
                return;
            }
            //2.扣除经销商积分，更新数据
            dealer.setIntegration(dealer.getIntegration()-integration);
            adminMapper.updateById(dealer);
            member.setIntegration(member.getIntegration() + integration);
            memberMapper.updateById(member);
            //历史记录(两个：一个用户，一个经销商)
            history.setIntegration(member.getIntegration());
            integrationHistory.setIntegration(dealer.getIntegration());
            umsIntegrationChangeHistoryService.save(history);
            jifenDealerIntegrationChangeHistoryMapper.insert(integrationHistory);
            redisService.set(String.format(Rediskey.MEMBER, member.getUsername()), JsonUtils.objectToJson(member));
        }
    }

    @Override
    public UmsMember getByUsername(String username) {
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(username);

        return memberMapper.selectOne(new QueryWrapper<>(umsMember));
    }

    @Override
    public UmsMember getById(Long id) {
        return memberMapper.selectById(id);
    }

    @Override
    public Object resetPassword(String phone, String password, String confimpassword, String authCode) {
        if (ValidatorUtils.notEmpty(authCode) && !verifyAuthCode(authCode, phone)) {
            return new CommonResult().failed("验证码错误");
        }
        if (!password.equals(confimpassword)) {
            return new CommonResult().failed("密码不一致");
        }
        UmsMember umsMember = new UmsMember();
        umsMember.setPassword(passwordEncoder.encode(password));
        memberMapper.update(umsMember, new QueryWrapper<UmsMember>().eq("phone", phone));
        return true;
    }

    @Override
    public CommonResult register(String phone, String password, String confim, String authCode, String invitecode) {

        //没有该用户进行添加操作
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(phone);
        umsMember.setPhone(phone);
        umsMember.setSourceType(3);
        umsMember.setPassword(password);
        umsMember.setConfimpassword(confim);
        umsMember.setPhonecode(authCode);
        umsMember.setInvitecode(invitecode);
        if (ValidatorUtils.notEmpty(umsMember.getPhonecode()) && !verifyAuthCode(umsMember.getPhonecode(), umsMember.getPhone())) {
            return new CommonResult().failed("验证码错误");
        }
        return this.register(umsMember);
    }

    @Override
    public SmsCode generateCode(String phone) {
        //生成流水号
        String uuid = UUID.randomUUID().toString();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        Map<String, String> map = new HashMap<>(2);
        map.put("code", sb.toString());
        map.put("phone", phone);

        //短信验证码缓存15分钟，
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + phone, sb.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + phone, expireMinute * 60);


        //存储sys_sms
        saveSmsAndSendCode(phone, sb.toString());
        SmsCode smsCode = new SmsCode();
        smsCode.setKey(uuid);
        return smsCode;
    }

    /**
     * 保存短信记录，并发送短信
     *
     * @param phone
     * @param code
     */
    private void saveSmsAndSendCode(String phone, String code) {
        checkTodaySendCount(phone);

        Sms sms = new Sms();
        sms.setPhone(phone);
        sms.setParams(code);
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        smsService.save(sms, params);

        //异步调用阿里短信接口发送短信
        CompletableFuture.runAsync(() -> {
            try {
                smsService.sendSmsMsg(sms);
            } catch (Exception e) {
                params.put("err", e.getMessage());
                smsService.save(sms, params);
                e.printStackTrace();
                LOGGER.error("发送短信失败：{}", e.getMessage());
            }

        });

        // 当天发送验证码次数+1
        String countKey = countKey(phone);
        redisService.increment(countKey, 1L);
        redisService.expire(countKey, 1 * 3600 * 24);
    }

    /**
     * 获取当天发送验证码次数
     * 限制号码当天次数
     *
     * @param phone
     * @return
     */
    private void checkTodaySendCount(String phone) {
        String value = redisService.get(countKey(phone));
        if (value != null) {
            Integer count = Integer.valueOf(value);
            if (count > dayCount) {
                throw new IllegalArgumentException("已超过当天最大次数");
            }
        }

    }

    private String countKey(String phone) {
        return "sms:count:" + LocalDate.now().toString() + ":" + phone;
    }

    @Override
    public CommonResult register(UmsMember user) {
        //验证验证码
        if (ValidatorUtils.notEmpty(user.getPhonecode()) && !verifyAuthCode(user.getPhonecode(), user.getPhone())) {
            return new CommonResult().failed("验证码错误");
        }
        if (!user.getPassword().equals(user.getConfimpassword())) {
            return new CommonResult().failed("密码不一致");
        }
        //查询是否已有该用户
        UmsMember queryM = new UmsMember();
        queryM.setUsername(user.getUsername());
        UmsMember umsMembers = memberMapper.selectOne(new QueryWrapper<>(queryM));
        if (umsMembers != null) {
            return new CommonResult().failed("该用户已经存在");
        }
        SysUser umsAdminList = adminMapper.selectByUserName(user.getUsername());
        if (umsAdminList != null) {
            return new CommonResult().failed("该用户已经存在");
        }
        umsMembers = memberMapper.selectByUsernameStaff(user.getUsername());
        if (umsMembers!=null){
            return new CommonResult().failed("该用户已经存在");
        }
        //没有该用户进行添加操作

        UmsMember umsMember = new UmsMember();
        umsMember.setMemberLevelId(9999L);
        umsMember.setMemberLevelName("未开通会员");
        umsMember.setUniacid(user.getUniacid());
        umsMember.setUsername(user.getUsername());
        umsMember.setNickname(user.getUsername());
        umsMember.setSourceType(user.getSourceType());
        umsMember.setPhone(user.getPhone());
        umsMember.setPassword(passwordEncoder.encode(user.getPassword()));
        umsMember.setCreateTime(new Date());
        umsMember.setStatus(1);
        umsMember.setBuyCount(0);
        umsMember.setBuyMoney(BigDecimal.ZERO);
        umsMember.setBlance(new BigDecimal(0));
        umsMember.setIntegration(0);
        if (ValidatorUtils.notEmpty(user.getInvitecode())) {
            umsMember.setInvitecode(user.getInvitecode());
        }
        String defaultIcon = mallplusProperties.getDefaultIcon();
        umsMember.setIcon(defaultIcon);
        memberMapper.insert(umsMember);
        try {
            //这是要生成二维码的url
            String url = mallplusProperties.getDomain()+"/?invitecode=" + user.getId();
            //要添加到二维码下面的文字
            String words = user.getUsername() + "的二维码";
            //调用刚才的工具类
            ByteArrayResource qrCode = MatrixToImageWriter.createQrCode(url, words);
            InputStream inputStream = new ByteArrayInputStream(qrCode.getByteArray());
            UmsMember member = new UmsMember();
            member.setId(umsMember.getId());
            member.setAvatar(aliyunOSSUtil.upload("png", inputStream));
            memberMapper.updateById(member);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        redisService.set(String.format(Rediskey.MEMBER, umsMember.getUsername()), JsonUtils.objectToJson(umsMember));
        //校验经销商有没有注册赠送的功能
        addIntegration(umsMember.getId(), user.getUniacid(), 1, "注册添加积分", AllEnum.ChangeSource.register.code(), umsMember.getUsername(),null);
        umsMember.setPassword(null);
        return new CommonResult().success("注册成功", null);
    }

    @Override
    public Object simpleReg(String phone, String password, String confimpassword, String invitecode,Integer uniacid) {
        //没有该用户进行添加操作
        UmsMember user = new UmsMember();
        user.setUniacid(uniacid);
        user.setUsername(phone);
        user.setPhone(phone);
        user.setPassword(password);
        user.setConfimpassword(confimpassword);
        user.setInvitecode(invitecode);
        return this.register(user);
    }

    @Override
    public CommonResult generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        //验证码绑定手机号并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, sb.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + telephone, AUTH_CODE_EXPIRE_SECONDS);
        return new CommonResult().success("获取验证码成功", sb.toString());
    }

    @Override
    public CommonResult updatePassword(String telephone, String password, String authCode) {
        UmsMember example = new UmsMember();
        example.setPhone(telephone);
        UmsMember member = memberMapper.selectOne(new QueryWrapper<>(example));
        if (member == null) {
            return new CommonResult().failed("该账号不存在");
        }
        //验证验证码
        if (!verifyAuthCode(authCode, telephone)) {
            return new CommonResult().failed("验证码错误");
        }

        member.setPassword(passwordEncoder.encode(password));
        memberMapper.updateById(member);
        return new CommonResult().success("密码修改成功", null);
    }

    @Override
    public void updateIntegration(Long id, Integer integration) {
        UmsMember record = new UmsMember();
        record.setId(id);
        record.setIntegration(integration);
        memberMapper.updateById(record);
    }

    //对输入的验证码进行校验
    private boolean verifyAuthCode(String authCode, String telephone) {
        if (StringUtils.isEmpty(authCode)) {
            return false;
        }
        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
        return authCode.equals(realAuthCode);
    }

    @Override
    public UmsMember queryByOpenId(String openId,Integer uniacid) {
        UmsMember queryO = new UmsMember();
        queryO.setUniacid(uniacid);
        queryO.setWeixinOpenid(openId);
        return memberMapper.selectOne(new QueryWrapper<>(queryO));
    }

    @Override
    public Map<String, Object> appLogin(String openid, Integer sex, String headimgurl, String unionid, String nickname, String city, Integer source,Integer uniacid) {
        Map<String, Object> resultObj = new HashMap<String, Object>();
        UmsMember userVo = this.queryByOpenId(openid,uniacid);
        String token = null;
        if (null == userVo) {
           /* SysUser sysUser = new SysUser();
            sysUser.setWeixinOpenid(openid);
            sysUser.setUniacid(uniacid);
            SysUser user = adminMapper.selectOne(new QueryWrapper<>(sysUser));
            */
            UmsMember umsMember = new UmsMember();
            umsMember.setUniacid(uniacid);
            umsMember.setUsername("wxapplet" + CharUtil.getRandomString(12));
            umsMember.setSourceType(source);
            umsMember.setPassword(passwordEncoder.encode("123456"));
            umsMember.setCreateTime(new Date());
            umsMember.setStatus(1);
            umsMember.setBlance(new BigDecimal(0));
            umsMember.setIntegration(0);
            umsMember.setMemberLevelId(4L);
            umsMember.setCity(city);
            umsMember.setAvatar(headimgurl);
            umsMember.setGender(sex);
            umsMember.setHistoryIntegration(0);
            umsMember.setWeixinOpenid(openid);
            if (StringUtils.isEmpty(headimgurl)) {
                //会员头像(默认头像)
                umsMember.setIcon("/upload/img/avatar/01.jpg");
            } else {
                umsMember.setIcon(headimgurl);
            }

            umsMember.setNickname(nickname);
            umsMember.setPersonalizedSignature(unionid);
            memberMapper.insert(umsMember);
            token = jwtTokenUtil.generateToken(umsMember.getUsername());
            resultObj.put("userId", umsMember.getId());
            resultObj.put("userInfo", umsMember);
            addIntegration(umsMember.getId(), regJifen, 1, "注册添加积分", AllEnum.ChangeSource.register.code(), umsMember.getUsername(),null);

            //todo  在这里把用户标签添加上
//            addMemberLabel(uniacid,umsMember.getId(),openid);
        } else {
//            addIntegration(userVo.getId(), logginJifen, 1, "登录添加积分", AllEnum.ChangeSource.login.code(), userVo.getUsername(),null);

            token = jwtTokenUtil.generateToken(userVo.getUsername());
            resultObj.put("userId", userVo.getId());
            resultObj.put("userInfo", userVo);
        }
        if (StringUtils.isEmpty(token)) {
            throw new ApiMallPlusException("登录失败");
        }
        resultObj.put("tokenHead", tokenHead);
        resultObj.put("token", token);
        return resultObj;
    }

    @Override
    public Object getAppletOpenId(AppletLoginParam req) {
        JSONObject j = new JSONObject();
        Long dealerId = accountWxappMapper.getDealerIdByUniacid(req.getUniacid());
        String appid = null;
        String secret = null;
        AccountWxapp accountWxapp = new AccountWxapp();
        accountWxapp.setUniacid(req.getUniacid());
        accountWxapp.setCreateBy(dealerId);
        AccountWxapp wxapp = accountWxappMapper.selectOne(new QueryWrapper<>(accountWxapp));
        if (wxapp==null){
            AccountWechats accountWechats = new AccountWechats();
            accountWechats.setUniacid(req.getUniacid());
            accountWechats.setCreateBy(dealerId);
            AccountWechats wechats = accountWechatsMapper.selectOne(new QueryWrapper<>(accountWechats));
            if (wechats==null){
                throw new ApiMallPlusException("信息错误");
            }else {
                appid = wechats.getKey();
                secret = wechats.getSecret();
            }
        }else {
            appid = wxapp.getKey();
            secret = wxapp.getSecret();
        }
        String webAccessTokenhttps = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
        String code = req.getCode();
        if (StringUtils.isEmpty(code)) {
            log.error("code ie empty");
            throw new ApiMallPlusException("code ie empty");
        }
        //获取openid
        String requestUrl = String.format(webAccessTokenhttps,
                appid,
                secret,
                code);


        JSONObject sessionData = CommonUtil.httpsRequest(requestUrl, "GET", null);
//        String userInfos = req.getUserInfo();
        System.out.println(sessionData.toString());


        if (null == sessionData || StringUtils.isEmpty(sessionData.getString("openid"))) {
            throw new ApiMallPlusException("登录失败openid is empty");
        }
        //验证用户信息完整性
        /*String signature = req.getSignature();
        String sha1 = CommonUtil.getSha1(userInfos + sessionData.getString("session_key"));
        if (!signature.equals(sha1)) {
            throw new ApiMallPlusException("登录失败,验证用户信息完整性 签名验证失败" + sha1 + "，" + signature);
        }*/
        //判断数据库里面有没有返回的这个openid，有的话直接登录的首页，没有的话直接就是小程序页面
        j.put("openid",sessionData.getString("openid"));
        Long num = memberMapper.getByOpenid(sessionData.getString("openid"));
        if (num>0){
            j.put("url","pages/login/binPhone");
        }else {
            j.put("url","pages/index/index");
        }
//        UmsMember umsMember = new UmsMember();
//        //这里应该还得加上小程序或者公众号的那个id去筛选
//        //首先判断是经销商小程序还是用户小程序
//        AccountWxapp wxapp = accountWxappMapper.selectById(req.getUniacid());
//        if (wxapp.getStoreId()==1){
//            //检索经销商数据
//            SysUser user = new SysUser();
//            user.setWeixinOpenid(sessionData.getString("openid"));
//            user.setUniacid(req.getUniacid());
//            SysUser member = adminMapper.selectOne(new QueryWrapper<>(user));
//            if (member==null){
//                j.put("url","seller/login");
//            }else {
//                j.put("url","pages/index/index");
//            }
//            return new CommonResult().success(j);
//        }
//        umsMember.setUniacid(req.getUniacid());
//        umsMember.setWeixinOpenid(sessionData.getString("openid"));
//        UmsMember member = memberMapper.selectOne(new QueryWrapper<>(umsMember));
//        if (member==null){
//            j.put("url","seller/login");
//        }else {
//            j.put("url","pages/index/index");
//        }
        return new CommonResult().success(j);
    }

    @Override
    public Object loginByWeixin(AppletLoginParam req) {
        try {
            String userInfos = req.getUserInfo();
            Map<String, Object> me = JsonUtils.readJsonToMap(userInfos);
            if (null == me) {
                throw new ApiMallPlusException("登录失败 userInfos is null");
            }
            Map<String, Object> resultObj = new HashMap<String, Object>();
            UmsMember userVo = new UmsMember();
            // 判断是否绑定了手机号
            if (ValidatorUtils.notEmpty(req.getPhone())) {
                UmsMember queryO = new UmsMember();
                queryO.setPhone(req.getPhone());
                userVo = memberMapper.selectOne(new QueryWrapper<>(queryO));
            } else {
                throw new ApiMallPlusException("登录失败 请先绑定手机号");
            }

            String token = null;
            if (null == userVo) {
                UmsMember umsMember = new UmsMember();
                umsMember.setUsername("wxapplet" + CharUtil.getRandomString(8));
                umsMember.setSourceType(2);
                umsMember.setPassword(passwordEncoder.encode("123456"));
                umsMember.setCreateTime(new Date());
                umsMember.setStatus(1);
                umsMember.setBlance(new BigDecimal(0));
                umsMember.setIntegration(0);
                umsMember.setMemberLevelId(4L);
                umsMember.setAvatar(req.getCloudID());
                umsMember.setCity(me.get("country").toString() + "-" + me.get("province").toString() + "-" + me.get("city").toString());

                umsMember.setGender((Integer) me.get("gender"));
                umsMember.setHistoryIntegration(0);
                umsMember.setWeixinOpenid(req.getOpenid());
                if (StringUtils.isEmpty(me.get("avatarUrl").toString())) {
                    //会员头像(默认头像)
                    umsMember.setIcon("/upload/img/avatar/01.jpg");
                } else {
                    umsMember.setIcon(me.get("avatarUrl").toString());
                }
                // umsMember.setGender(Integer.parseInt(me.get("gender")));
                umsMember.setNickname(me.get("nickName").toString());
                String defaultIcon = mallplusProperties.getDefaultIcon();
                umsMember.setIcon(defaultIcon);
                memberMapper.insert(umsMember);
                try {
                    //这是要生成二维码的url
                    String url = mallplusProperties.getDomain()+"/?invitecode=" + umsMember.getId();
                    //要添加到二维码下面的文字
                    String words = umsMember.getUsername() + "的二维码";
                    //调用刚才的工具类
                    ByteArrayResource qrCode = MatrixToImageWriter.createQrCode(url, words);
                    InputStream inputStream = new ByteArrayInputStream(qrCode.getByteArray());
                    UmsMember member = new UmsMember();
                    member.setId(umsMember.getId());
                    member.setAvatar(aliyunOSSUtil.upload("png", inputStream));
                    memberMapper.updateById(member);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                token = jwtTokenUtil.generateToken(umsMember.getUsername());
                resultObj.put("userId", umsMember.getId());
                resultObj.put("userInfo", umsMember);
                addIntegration(umsMember.getId(), regJifen, 1, "注册添加积分", AllEnum.ChangeSource.register.code(), userVo.getUsername(),null);


            } else {
                //  userVo = this.queryByOpenId(sessionData.getString("openid"));
                if (ValidatorUtils.notEmpty(userVo.getWeixinOpenid())) {
                    addIntegration(userVo.getId(), logginJifen, 1, "登录添加积分", AllEnum.ChangeSource.login.code(), userVo.getUsername(),null);
                    token = jwtTokenUtil.generateToken(userVo.getUsername());
                    resultObj.put("userId", userVo.getId());
                    resultObj.put("userInfo", userVo);
                } else {
                    userVo.setPassword(passwordEncoder.encode("123456"));
                    userVo.setCreateTime(new Date());
                    userVo.setStatus(1);
                    userVo.setBlance(new BigDecimal(0));
                    userVo.setIntegration(0);
                    userVo.setMemberLevelId(4L);
                    userVo.setAvatar(req.getCloudID());
                    userVo.setCity(me.get("country").toString() + "-" + me.get("province").toString() + "-" + me.get("city").toString());

                    userVo.setGender((Integer) me.get("gender"));
                    userVo.setHistoryIntegration(0);
                    userVo.setWeixinOpenid(req.getOpenid());
                    if (StringUtils.isEmpty(me.get("avatarUrl").toString())) {
                        //会员头像(默认头像)
                        userVo.setIcon("/upload/img/avatar/01.jpg");
                    } else {
                        userVo.setIcon(me.get("avatarUrl").toString());
                    }
                    // umsMember.setGender(Integer.parseInt(me.get("gender")));
                    userVo.setNickname(me.get("nickName").toString());

                    memberMapper.updateById(userVo);
                }

            }


            if (StringUtils.isEmpty(token)) {
                throw new ApiMallPlusException("登录失败");
            }
            resultObj.put("tokenHead", tokenHead);
            resultObj.put("token", token);


            return new CommonResult().success(resultObj);
        } catch (ApiMallPlusException e) {
            e.printStackTrace();
            throw new ApiMallPlusException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiMallPlusException(e.getMessage());
        }

    }

    @Override
    public Object loginByWeixin2(AppletLoginnewParam req) {
        try {
            SysAppletSet appletSet = getSysAppletSet(req.getAppIdsource());
            if (null == appletSet) {
                throw new ApiMallPlusException("没有设置支付配置");
            }
            String code = req.getCode();
            if (StringUtils.isEmpty(code)) {
                log.error("code ie empty");
                throw new ApiMallPlusException("code ie empty");
            }
            UserInfo userInfos = req.getUserInfo();

            String signature = req.getSignature();

            if (null == userInfos) {
                throw new ApiMallPlusException("登录失败 userInfos is null");
            }

            Map<String, Object> resultObj = new HashMap<String, Object>();

            String webAccessTokenhttps = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

            //获取openid
            String requestUrl = String.format(webAccessTokenhttps,
                    appletSet.getAppid(),
                    appletSet.getAppsecret(),
                    code);

            JSONObject sessionData = CommonUtil.httpsRequest(requestUrl, "GET", null);

            if (null == sessionData || StringUtils.isEmpty(sessionData.getString("openid"))) {
                throw new ApiMallPlusException("登录失败openid is empty");
            }
            //验证用户信息完整性
            //  String sha1 = CommonUtil.getSha1(JsonUtils.toJsonStr(userInfos) + sessionData.getString("session_key"));
            //  if (!signature.equals(sha1)) {
            //    throw new ApiMallPlusException("登录失败,验证用户信息完整性 签名验证失败" + sha1 + "，" + signature);
            //  }
            UmsMember userVo = this.queryByOpenId(sessionData.getString("openid"),Integer.parseInt(sessionData.getString("uniacid")));
            String token = null;
            if (null == userVo) {
                UmsMember umsMember = new UmsMember();
                umsMember.setUsername("wxapplet" + CharUtil.getRandomString(12));
                umsMember.setSourceType(2);
                umsMember.setPassword(passwordEncoder.encode("123456"));
                umsMember.setCreateTime(new Date());
                umsMember.setStatus(1);
                umsMember.setBlance(new BigDecimal(0));
                umsMember.setIntegration(0);
                umsMember.setMemberLevelId(4L);
                umsMember.setAvatar(req.getCloudID());
                umsMember.setCity(userInfos.getCountry() + "-" + userInfos.getProvince() + "-" +
                        userInfos.getCity());

                umsMember.setGender(Integer.valueOf(userInfos.getGender()));
                umsMember.setHistoryIntegration(0);
                umsMember.setWeixinOpenid(sessionData.getString("openid"));
                if (StringUtils.isEmpty(userInfos.getAvatarUrl())) {
                    //会员头像(默认头像)
                    umsMember.setIcon("/upload/img/avatar/01.jpg");
                } else {
                    umsMember.setIcon(userInfos.getAvatarUrl());
                }
                // umsMember.setGender(Integer.parseInt(me.get("gender")));
                umsMember.setNickname(userInfos.getNickName());
                String defaultIcon = mallplusProperties.getDefaultIcon();
                umsMember.setIcon(defaultIcon);
                memberMapper.insert(umsMember);
                try {
                    //这是要生成二维码的url
                    String url = mallplusProperties.getDomain()+"/?invitecode=" + umsMember.getId();
                    //要添加到二维码下面的文字
                    String words = umsMember.getUsername() + "的二维码";
                    //调用刚才的工具类
                    ByteArrayResource qrCode = MatrixToImageWriter.createQrCode(url, words);
                    InputStream inputStream = new ByteArrayInputStream(qrCode.getByteArray());
                    UmsMember member = new UmsMember();
                    member.setId(umsMember.getId());
                    member.setAvatar(aliyunOSSUtil.upload("png", inputStream));
                    memberMapper.updateById(member);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                token = jwtTokenUtil.generateToken(umsMember.getUsername());
                resultObj.put("userId", umsMember.getId());
                resultObj.put("userInfo", umsMember);
                addIntegration(umsMember.getId(), regJifen, 1, "注册添加积分", AllEnum.ChangeSource.register.code(), umsMember.getUsername(),null);


            } else {
                addIntegration(userVo.getId(), logginJifen, 1, "登录添加积分", AllEnum.ChangeSource.login.code(), userVo.getUsername(),null);
                token = jwtTokenUtil.generateToken(userVo.getUsername());
                resultObj.put("userId", userVo.getId());
                resultObj.put("userInfo", userVo);
            }


            if (StringUtils.isEmpty(token)) {
                throw new ApiMallPlusException("登录失败");
            }
            resultObj.put("tokenHead", tokenHead);
            resultObj.put("token", token);


            return new CommonResult().success(resultObj);
        } catch (ApiMallPlusException e) {
            e.printStackTrace();
            throw new ApiMallPlusException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiMallPlusException(e.getMessage());
        }

    }

    @Override
    public Object loginByWeixin1(AppletLoginParam req) {
        try {
            SysAppletSet appletSet = getSysAppletSet(req.getAppIdsource());

            String code = req.getCode();
            if (StringUtils.isEmpty(code)) {
                log.error("code ie empty");
                throw new ApiMallPlusException("code ie empty");
            }
            String userInfos = req.getUserInfo();
            String signature = req.getSignature();
            Map<String, Object> resultObj = new HashMap<String, Object>();
            String webAccessTokenhttps = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
            //获取openid
            String requestUrl = String.format(webAccessTokenhttps,
                    appletSet.getAppid(),
                    appletSet.getAppsecret(),
                    code);

            JSONObject sessionData = CommonUtil.httpsRequest(requestUrl, "GET", null);
            if (null == sessionData || StringUtils.isEmpty(sessionData.getString("openid"))) {
                throw new ApiMallPlusException("登录失败openid is empty");
            }
            //验证用户信息完整性
            String sha1 = CommonUtil.getSha1(userInfos + sessionData.getString("session_key"));
           /* if (!signature.equals(sha1)) {
                throw new ApiMallPlusException("登录失败,验证用户信息完整性 签名验证失败" + sha1 + "，" + signature);
            }*/
            UmsMember userVo = this.queryByOpenId(sessionData.getString("openid"),Integer.parseInt(sessionData.getString("uniacid")));
            String token = null;
            if (null == userVo) {
                UmsMember umsMember = new UmsMember();
                umsMember.setUsername("wxapplet" + CharUtil.getRandomString(12));
                umsMember.setSourceType(2);
                umsMember.setPassword(passwordEncoder.encode("123456"));
                umsMember.setCreateTime(new Date());
                umsMember.setStatus(1);
                umsMember.setBlance(new BigDecimal(0));
                umsMember.setIntegration(0);
                umsMember.setMemberLevelId(4L);
                umsMember.setAvatar(req.getCloudID());

                if (ValidatorUtils.empty(userInfos)) {
                    //未查询到用户信息，通过微信获取用户tokey信息
                    String requestUrl1 = GetPageAccessTokenUrl.replace("APPID", appletSet.getAppid()).replace("APPSECRET", appletSet.getAppsecret());
                    String openidResponse = httpClientSend(requestUrl1);
                    JSONObject OpenidJSONO = JSONObject.fromObject(openidResponse);
                    String accessToken = String.valueOf(OpenidJSONO.get("access_token"));
                    //获取用户数据信息
                    String requestUserInfoUrl = GetPageUserInfoUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", appletSet.getAppid());
                    String userresponse = httpClientSend(requestUserInfoUrl);
                    JSONObject userJSON = JSONObject.fromObject(userresponse);
                    String nickname = new String(String.valueOf(userJSON.get("nickname")).getBytes("ISO8859-1"), "UTF-8");
                    String avatarUrl = String.valueOf(userJSON.get("avatarUrl"));
                    String city = new String(String.valueOf(userJSON.get("city")).getBytes("ISO8859-1"), "UTF-8");
                    String country = new String(String.valueOf(userJSON.get("country")).getBytes("ISO8859-1"), "UTF-8");
                    String gender = String.valueOf(userJSON.get("gender"));
                    String province = new String(String.valueOf(userJSON.get("province")).getBytes("ISO8859-1"), "UTF-8");
                    String language = String.valueOf(userJSON.get("language"));
                    umsMember.setCity(country + "-" + province + "-" + city);
                    if (StringUtils.isEmpty(avatarUrl)) {
                        //会员头像(默认头像)
                        umsMember.setIcon("/upload/img/avatar/01.jpg");
                    } else {
                        umsMember.setIcon(avatarUrl);
                    }
                    // umsMember.setGender(Integer.parseInt(me.get("gender")));
                    umsMember.setNickname(nickname);
                    if (ValidatorUtils.notEmpty(gender) && !"null".equals(gender)) {
                        umsMember.setGender(Integer.valueOf(gender));
                    }
                } else {
                    Map<String, Object> me = JsonUtils.readJsonToMap(userInfos);
                    umsMember.setCity(me.get("country").toString() + "-" + me.get("province").toString() + "-" + me.get("city").toString());
                    if (StringUtils.isEmpty(me.get("avatarUrl").toString())) {
                        //会员头像(默认头像)
                        umsMember.setIcon("/upload/img/avatar/01.jpg");
                    } else {
                        umsMember.setIcon(me.get("avatarUrl").toString());
                    }
                    // umsMember.setGender(Integer.parseInt(me.get("gender")));
                    umsMember.setNickname(me.get("nickName").toString());
                    umsMember.setGender((Integer) me.get("gender"));
                }
                umsMember.setHistoryIntegration(0);
                umsMember.setWeixinOpenid(sessionData.getString("openid"));
                memberMapper.insert(umsMember);
                token = jwtTokenUtil.generateToken(umsMember.getUsername());
                resultObj.put("userId", umsMember.getId());
                resultObj.put("userInfo", umsMember);
                addIntegration(umsMember.getId(), regJifen, 1, "注册添加积分", AllEnum.ChangeSource.register.code(), umsMember.getUsername(),null);
            } else {
                addIntegration(userVo.getId(), logginJifen, 1, "登录添加积分", AllEnum.ChangeSource.login.code(), userVo.getUsername(),null);
                token = jwtTokenUtil.generateToken(userVo.getUsername());
                resultObj.put("userId", userVo.getId());
                resultObj.put("userInfo", userVo);
            }
            if (StringUtils.isEmpty(token)) {
                throw new ApiMallPlusException("登录失败");
            }
            resultObj.put("tokenHead", tokenHead);
            resultObj.put("token", token);
            return new CommonResult().success(resultObj);
        } catch (ApiMallPlusException e) {
            e.printStackTrace();
            throw new ApiMallPlusException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiMallPlusException(e.getMessage());
        }
    }

    @Override
    public Object loginByWeixin2(AppletLoginParam req) {
        try {
            SysAppletSet appletSet = getSysAppletSet(req.getAppIdsource());
            if (StringUtils.isEmpty(req.getOpenid())) {
                log.error("openid ie empty");
                throw new ApiMallPlusException("openid ie empty");
            }
            String userInfos = req.getUserInfo();
            String signature = req.getSignature();
            Map<String, Object> resultObj = new HashMap<String, Object>();

            UmsMember userVo = this.queryByOpenId(req.getOpenid(),req.getUniacid());
            String token = null;
            if (null == userVo) {
                UmsMember umsMember = new UmsMember();
                umsMember.setUsername("wxapplet" + CharUtil.getRandomString(12));
                umsMember.setSourceType(2);
                umsMember.setPassword(passwordEncoder.encode("123456"));
                umsMember.setCreateTime(new Date());
                umsMember.setStatus(1);
                umsMember.setBlance(new BigDecimal(0));
                umsMember.setIntegration(0);
                umsMember.setMemberLevelId(4L);
                umsMember.setAvatar(req.getCloudID());

                if (ValidatorUtils.empty(userInfos)) {
                    //未查询到用户信息，通过微信获取用户tokey信息
                    String requestUrl1 = GetPageAccessTokenUrl.replace("APPID", appletSet.getAppid()).replace("APPSECRET", appletSet.getAppsecret());
                    String openidResponse = httpClientSend(requestUrl1);
                    JSONObject OpenidJSONO = JSONObject.fromObject(openidResponse);
                    String accessToken = String.valueOf(OpenidJSONO.get("access_token"));
                    //获取用户数据信息
                    String requestUserInfoUrl = GetPageUserInfoUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", appletSet.getAppid());
                    String userresponse = httpClientSend(requestUserInfoUrl);
                    JSONObject userJSON = JSONObject.fromObject(userresponse);
                    String nickname = new String(String.valueOf(userJSON.get("nickname")).getBytes("ISO8859-1"), "UTF-8");
                    String avatarUrl = String.valueOf(userJSON.get("avatarUrl"));
                    String city = new String(String.valueOf(userJSON.get("city")).getBytes("ISO8859-1"), "UTF-8");
                    String country = new String(String.valueOf(userJSON.get("country")).getBytes("ISO8859-1"), "UTF-8");
                    String gender = String.valueOf(userJSON.get("gender"));
                    String province = new String(String.valueOf(userJSON.get("province")).getBytes("ISO8859-1"), "UTF-8");
                    String language = String.valueOf(userJSON.get("language"));
                    umsMember.setCity(country + "-" + province + "-" + city);
                    if (StringUtils.isEmpty(avatarUrl)) {
                        //会员头像(默认头像)
                        umsMember.setIcon("/upload/img/avatar/01.jpg");
                    } else {
                        umsMember.setIcon(avatarUrl);
                    }
                    // umsMember.setGender(Integer.parseInt(me.get("gender")));
                    umsMember.setNickname(nickname);
                    if (ValidatorUtils.notEmpty(gender) && !"null".equals(gender)) {
                        umsMember.setGender(Integer.valueOf(gender));
                    }
                } else {
                    Map<String, Object> me = JsonUtils.readJsonToMap(userInfos);
                    umsMember.setCity(me.get("country").toString() + "-" + me.get("province").toString() + "-" + me.get("city").toString());
                    if (StringUtils.isEmpty(me.get("avatarUrl").toString())) {
                        //会员头像(默认头像)
                        umsMember.setIcon("/upload/img/avatar/01.jpg");
                    } else {
                        umsMember.setIcon(me.get("avatarUrl").toString());
                    }
                    // umsMember.setGender(Integer.parseInt(me.get("gender")));
                    umsMember.setNickname(me.get("nickName").toString());
                    umsMember.setGender((Integer) me.get("gender"));
                }
                umsMember.setHistoryIntegration(0);
                umsMember.setWeixinOpenid(req.getOpenid());
                memberMapper.insert(umsMember);
                token = jwtTokenUtil.generateToken(umsMember.getUsername());
                resultObj.put("userId", umsMember.getId());
                resultObj.put("userInfo", umsMember);
                addIntegration(umsMember.getId(), regJifen, 1, "注册添加积分", AllEnum.ChangeSource.register.code(), umsMember.getUsername(),null);
            } else {
                addIntegration(userVo.getId(), logginJifen, 1, "登录添加积分", AllEnum.ChangeSource.login.code(), userVo.getUsername(),null);
                token = jwtTokenUtil.generateToken(userVo.getUsername());
                resultObj.put("userId", userVo.getId());
                resultObj.put("userInfo", userVo);
            }
            if (StringUtils.isEmpty(token)) {
                throw new ApiMallPlusException("登录失败");
            }
            resultObj.put("tokenHead", tokenHead);
            resultObj.put("token", token);
            return new CommonResult().success(resultObj);
        } catch (ApiMallPlusException e) {
            e.printStackTrace();
            throw new ApiMallPlusException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiMallPlusException(e.getMessage());
        }
    }

    @Override
    public SysAppletSet getSysAppletSet(Integer soruce) {
        SysAppletSet appletSet = new SysAppletSet();
        if (ValidatorUtils.notEmpty(soruce)) {
            if (soruce == 1) { // kaka
                appletSet.setAppid("wx0c1055c6158f7851");
                appletSet.setAppsecret("baa79ed796ec18e932c547fe009d9af0");
            } else if (soruce == 2) { //mallplus商户端
                appletSet.setAppid("wx1d852ffdcbedd005");
                appletSet.setAppsecret("fb5c6eeee8b5d11c9dea0f0273f16c7c");
            } else if (soruce == 3) {
                appletSet.setAppid("");
                appletSet.setAppsecret("");
            } else if (soruce == 4) {
                appletSet.setAppid("");
                appletSet.setAppsecret("");
            } else if (soruce == 5) {
                appletSet.setAppid("");
                appletSet.setAppsecret("");
            }
        } else {
            appletSet = appletSetService.getOne(new QueryWrapper<>());
            if (null == appletSet) {
                throw new ApiMallPlusException("没有设置支付配置");
            }
        }
        return appletSet;
    }


    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        if (jwtTokenUtil.canRefresh(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }


    @Override
    public Map<String, Object> loginByCode(String phone, String authCode) {
        Map<String, Object> tokenMap = new HashMap<>();
        String token = null;
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(phone);

            //验证验证码
            if (!verifyAuthCode(authCode, phone)) {
                throw new ApiMallPlusException("验证码错误");
            }
            UmsMember member = this.getByUsername(phone);
            if (member == null || member.getId() == null) {
                throw new ApiMallPlusException("用户不存在");

            }
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            tokenMap.put("userInfo", member);
            addIntegration(member.getId(), logginJifen, 1, "登录添加积分", AllEnum.ChangeSource.login.code(), member.getUsername(),null);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());

        }
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);

        return tokenMap;
    }

    @Override
    public Map<String, Object> login(String username, String password,String uniacid) {

        Map<String, Object> tokenMap = new HashMap<>();
        String token = null;
        try {
            MemberDetails userDetails = (MemberDetails) userDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
//            UmsMember member = this.getByUsername(username);
            UmsMember member = userDetails.getUmsMember();
            if (!member.getUniacid().equals(uniacid)){
                throw new BadCredentialsException("用户名或者密码不正确");
            }
            //验证验证码
           /* if (!verifyAuthCode(user.getCode(), member.getPhone())) {
                throw  new ApiMallPlusException("验证码错误");
            }*/

            //   Authentication authentication = authenticationManager.authenticate(authenticationToken);
//            addIntegration(member.getId(), logginJifen, 1, "登录添加积分", AllEnum.ChangeSource.login.code(), member.getUsername());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            tokenMap.put("userInfo", member);
        } catch (Exception e) {
            LOGGER.warn("登录异常:{}", e.getMessage());

        }

        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);

        return tokenMap;

    }

    @Override
    public Object initMemberRedis() {
        List<UmsMember> list = memberMapper.selectList(new QueryWrapper<>());
        for (UmsMember member : list) {
            redisService.set(String.format(Rediskey.MEMBER, member.getUsername()), JsonUtils.objectToJson(member));
        }
        return 1;
    }

    @Transactional
    @Override
    public Object withDraw(UmsMemberBlanceLog blanceLog) {
        UmsMember userDO1 = this.getNewCurrentMember();
        UmsMember userDO = this.getById(userDO1.getId());
        if (blanceLog.getMoney().compareTo(userDO.getBlance()) > 0) {
            throw new ApiMallPlusException("余额不足！");
        }

       /* userDO.setBlance(userDO.getBlance().subtract(money));
        this.updateById(userDO);*/

        blanceLog.setMemberId(userDO.getId());
        blanceLog.setCreateTime(new Date());
        blanceLog.setNote("用户提现,余额=" + userDO.getBlance());
        blanceLog.setType(4);
        memberBlanceLogService.save(blanceLog);
        return new CommonResult().success();
    }

    @Override
    public UmsMember selectByUsernameLeader(String username) {
        return memberMapper.selectByUsernameLeader(username);
    }

    @Override
    public UmsMember selectByUsernameStaff(String username) {
        return memberMapper.selectByUsernameStaff(username);
    }

    @Override
    public Long getByOpenid(String openId) {
        return memberMapper.getByOpenid(openId);
    }

    @Override
    public boolean createAppUser(String openid, Integer sex, String headimgurl, String unionid, String nickname, String city, Integer source, Integer uniacid,String phone) {
        if (uniacid==StringConstantUtil.applet_platform){
            SysUser sysUser = new SysUser();
            sysUser.setPhone(phone);
            sysUser.setUsername(phone);
            SysUser user = adminMapper.selectOne(new QueryWrapper<>(sysUser));
            if (user == null){
                SysUserStaff userStaff = new SysUserStaff();
                userStaff.setPhone(phone);
                userStaff.setUsername(phone);
                SysUserStaff staff = staffMapper.selectOne(new QueryWrapper<>(userStaff));
                if (staff!=null){
                    //手机号也没有的话，在那张表里填用户信息呢，经销商里面，员工必须先后台添加
                    staff.setUniacid(uniacid);
                    staff.setWeixinOpenid(openid);
                    staff.setAvatar(headimgurl);
                    staff.setNickname(nickname);
                    staffMapper.updateById(staff);
                    return true;
                }else {
                    return false;
                }
            }else {
                user.setUniacid(uniacid);
                user.setWeixinOpenid(openid);
                user.setAvatar(headimgurl);
                user.setNickName(nickname);
                adminMapper.updateById(user);
                return true;
            }
        }
        UmsMember userVo1 = new UmsMember();
        userVo1.setPhone(phone);
        UmsMember userVo = memberMapper.selectOne(new QueryWrapper<>(userVo1));
        if (null == userVo) {
            UmsMember umsMember = new UmsMember();
            umsMember.setUniacid(uniacid);
            umsMember.setUsername("wxapplet" + CharUtil.getRandomString(12));
            umsMember.setSourceType(source);
            umsMember.setPassword(passwordEncoder.encode("123456"));
            umsMember.setCreateTime(new Date());
            umsMember.setStatus(1);
            umsMember.setBlance(new BigDecimal(0));
            umsMember.setIntegration(0);
            umsMember.setMemberLevelId(4L);
            umsMember.setCity(city);
            umsMember.setAvatar(headimgurl);
            umsMember.setGender(sex);
            umsMember.setHistoryIntegration(0);
            umsMember.setWeixinOpenid(openid);
            if (StringUtils.isEmpty(headimgurl)) {
                //会员头像(默认头像)
                umsMember.setIcon("/upload/img/avatar/01.jpg");
            } else {
                umsMember.setIcon(headimgurl);
            }

            umsMember.setNickname(nickname);
            umsMember.setPersonalizedSignature(unionid);
            memberMapper.insert(umsMember);
            addIntegration(umsMember.getId(), regJifen, 1, "注册添加积分", AllEnum.ChangeSource.register.code(), umsMember.getUsername(), null);
            return true;
        }else {
            userVo.setUniacid(uniacid);
            userVo.setCity(city);
            userVo.setAvatar(headimgurl);
            userVo.setGender(sex);
            userVo.setWeixinOpenid(openid);
            memberMapper.updateById(userVo);
            return true;
        }
    }


}

