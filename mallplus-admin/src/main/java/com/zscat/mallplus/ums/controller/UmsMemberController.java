package com.zscat.mallplus.ums.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.enums.AllEnum;
import com.zscat.mallplus.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.sms.entity.SmsLabelMember;
import com.zscat.mallplus.sms.entity.SmsLabelSet;
import com.zscat.mallplus.sms.service.ISmsLabelMemberService;
import com.zscat.mallplus.ums.entity.UmsIntegrationChangeHistory;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.entity.UmsMemberBlanceLog;
import com.zscat.mallplus.ums.entity.UmsMemberLog;
import com.zscat.mallplus.ums.mapper.UmsIntegrationChangeHistoryMapper;
import com.zscat.mallplus.ums.service.IUmsMemberBlanceLogService;
import com.zscat.mallplus.ums.service.IUmsMemberLevelService;
import com.zscat.mallplus.ums.service.IUmsMemberLogService;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.util.ConstantUtil;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.util.StringUtils;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.entity.WtWaterCardLimit;
import com.zscat.mallplus.water.service.IWtWaterCardRechargeService;
import com.zscat.mallplus.water.service.IWtWaterCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "UmsMemberController", description = "会员表管理")
@RequestMapping("/ums/UmsMember")
public class UmsMemberController {
    @Resource
    private IUmsMemberService IUmsMemberService;
    @Resource
    private IUmsMemberLevelService memberLevelService;
    @Resource
    private IUmsMemberBlanceLogService memberBlanceLogService;
    @Resource
    private UmsIntegrationChangeHistoryMapper umsIntegrationChangeHistoryService;
    @Resource
    private OmsOrderMapper omsOrderMapper;
    @Resource
    private IWtWaterCardRechargeService IWtWaterCardRechargeService;
    @Resource
    private IWtWaterCardService IWtWaterCardService;
    @Resource
    private IUmsMemberLogService IUmsMemberLogService;
    @Resource
    private ISmsLabelMemberService ISmsLabelMemberService;

    @SysLog(MODULE = "ums", REMARK = "根据条件查询所有会员表列表")
    @ApiOperation("根据条件查询所有会员表列表")
    @GetMapping(value = "/list")
    public Object getUmsMemberByPage(UmsMember entity,
                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize
    ) {
        try {
//            if (ValidatorUtils.empty(entity.getBuyCountss())) {
//                return new CommonResult().success(IUmsMemberService.page(new Page<UmsMember>(pageNum, pageSize), new QueryWrapper<>(entity).orderByDesc("create_time")));

                return new CommonResult().success(IUmsMemberService.selectMember(new Page<Map<String, Object>>(pageNum, pageSize),
                        entity));
//            }
//            return new CommonResult().success(IUmsMemberService.page(new Page<UmsMember>(pageNum, pageSize), new QueryWrapper<>(entity).ge("buy_count", entity.getBuyCountss()).orderByDesc("create_time")));

        } catch (Exception e) {
            log.error("根据条件查询所有会员表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "保存会员表")
    @ApiOperation("保存会员表")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('ums:UmsMember:create')")
    @Transactional
    public Object saveUmsMember(@RequestBody UmsMember entity) {
        try {
            //判断用户标签是否可用
            if(entity.getLabelList()!=null && entity.getLabelList().size()>0){
                //判断有没有购买用户标签功能
                if(!IWtWaterCardRechargeService.getSalesInfo(entity.getDealerId(),ConstantUtil.ums_label_perssion_id)){
                    return new CommonResult().failed("您尚未购买此功能，或已到期。请开通此功能后再次使用！");
                }
            }
            if (IUmsMemberService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存会员表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "更新会员表")
    @ApiOperation("更新会员表")
    @PostMapping(value = "/update")
    @PreAuthorize("hasAuthority('ums:UmsMember:update')")
    @Transactional
    public Object updateUmsMember(@RequestBody UmsMember entity) {
        try {
            //判断用户标签是否可用
            if(entity.getLabelList()!=null && entity.getLabelList().size()>0){
                //判断有没有购买用户标签功能
                if(!IWtWaterCardRechargeService.getSalesInfo(entity.getDealerId(),ConstantUtil.ums_label_perssion_id)){
                    return new CommonResult().failed("您尚未购买此功能，或已到期。请开通此功能后再次使用！");
                }
            }
            if (IUmsMemberService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新会员表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "删除用户标签")
    @ApiOperation("删除用户标签")
    @PostMapping(value = "/removeLabel")
    @PreAuthorize("hasAuthority('ums:UmsMember:update')")
    @Transactional
    public Object removeLabel(@RequestBody SmsLabelSet entity,@RequestParam("umsMemberId") Long umsMemberId) {
        try {
            //判断用户标签是否可以删除
            if(entity.getIsSystem().equals(ConstantUtil.is)){
                return new CommonResult().failed("系统标签不能删除！");
            }
            if (IUmsMemberService.removeLabel(entity.getId(),umsMemberId)) {
                //添加日志
                UmsMemberLog umsMemberLog= new UmsMemberLog();
                umsMemberLog.setMemberId(umsMemberId);//会员id
                umsMemberLog.setFalg(ConstantUtil.member_log_falg_0);//日志
                umsMemberLog.setContent("删除用户标签："+entity.getLabelName());//内容
                umsMemberLog.setCreateTime(new Date());
                umsMemberLog.setCreateBy(UserUtils.getCurrentMember().getId());
                if (IUmsMemberLogService.save(umsMemberLog)) {
                    return new CommonResult().success();
                }
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新会员表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "添加用户标签")
    @ApiOperation("添加用户标签")
    @PostMapping(value = "/addLabel")
    @PreAuthorize("hasAuthority('ums:UmsMember:update')")
    @Transactional
    public Object addLabel(@RequestBody SmsLabelSet entity,@RequestParam("umsMemberId") Long umsMemberId) {
        try {
            //判断用户标签是否可以添加
            if(entity.getIsSystem().equals(ConstantUtil.is)){
                return new CommonResult().failed("系统标签不能手动添加！");
            }
            SmsLabelMember smsLabelMember = new SmsLabelMember();
            smsLabelMember.setMemberId(umsMemberId);
            smsLabelMember.setLabelId(entity.getId());

            if (ISmsLabelMemberService.save(smsLabelMember)) {
                //添加日志
                UmsMemberLog umsMemberLog= new UmsMemberLog();
                umsMemberLog.setMemberId(umsMemberId);//会员id
                umsMemberLog.setFalg(ConstantUtil.member_log_falg_0);//日志
                umsMemberLog.setContent("添加用户标签："+entity.getLabelName());//内容
                umsMemberLog.setCreateTime(new Date());
                umsMemberLog.setCreateBy(UserUtils.getCurrentMember().getId());
                if (IUmsMemberLogService.save(umsMemberLog)) {
                    return new CommonResult().success();
                }
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新会员表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "添加备注")
    @ApiOperation("添加备注")
    @PostMapping(value = "/addRemarks")
    @PreAuthorize("hasAuthority('ums:UmsMember:update')")
    @Transactional
    public Object addRemarks(@RequestBody UmsMemberLog entity) {
        try {
            //判断用户标签是否可以添加
            if(entity.getContent()==null || entity.getContent().isEmpty()){
                return new CommonResult().failed("备注不能为空！");
            }
            entity.setFalg(ConstantUtil.member_log_falg_1);//备注
            entity.setCreateTime(new Date());
            entity.setCreateBy(UserUtils.getCurrentMember().getId());
            if (IUmsMemberLogService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新会员表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "挂失")
    @ApiOperation("挂失")
    @PostMapping(value = "/updateWaterCard")
    @PreAuthorize("hasAuthority('ums:UmsMember:update')")
    @Transactional
    public Object updateWaterCard(@RequestBody UmsMember entity) {
        try {
            if(entity.getCardNo()!=null && !entity.getCardNo().isEmpty()){
                return new CommonResult().failed("没有绑定实体卡，不能挂失！");
            }
            if (IWtWaterCardService.updateStateByCard(entity.getCardNo(),ConstantUtil.water_code_state_2
                    ,UserUtils.getCurrentMember().getId())) {
                //添加日志
                UmsMemberLog umsMemberLog= new UmsMemberLog();
                umsMemberLog.setMemberId(entity.getId());//会员id
                umsMemberLog.setFalg(ConstantUtil.member_log_falg_0);//日志
                umsMemberLog.setContent("会员卡挂失："+entity.getCardNo());//内容
                umsMemberLog.setCreateTime(new Date());
                umsMemberLog.setCreateBy(UserUtils.getCurrentMember().getId());
                if (IUmsMemberLogService.save(umsMemberLog)) {
                    return new CommonResult().success();
                }
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("会员实体卡挂失：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "积分管理")
    @ApiOperation("积分管理")
    @PostMapping(value = "/updateIntegration")
    @PreAuthorize("hasAuthority('ums:UmsMember:update')")
    @Transactional
    public Object updateIntegration(@RequestBody UmsMember entity) {
        try {
            UmsMember coupon = IUmsMemberService.getById(entity.getId());
            if(coupon.getIntegration()!=entity.getIntegration()){
                Integer integration = entity.getIntegration()-coupon.getIntegration();
                coupon.setIntegration(entity.getIntegration());
                IUmsMemberService.updateById(coupon);
                //添加日志
                UmsMemberLog umsMemberLog= new UmsMemberLog();
                umsMemberLog.setMemberId(entity.getId());//会员id
                umsMemberLog.setFalg(ConstantUtil.member_log_falg_0);//日志
                if(integration>0){
                    umsMemberLog.setContent("会员积分增加"+integration+"积分");//内容
                }else{
                    umsMemberLog.setContent("会员积分减少"+(integration*-1)+"积分");//内容
                }
                umsMemberLog.setCreateTime(new Date());
                umsMemberLog.setCreateBy(UserUtils.getCurrentMember().getId());
                if (IUmsMemberLogService.save(umsMemberLog)) {
                    return new CommonResult().success();
                }
            }
        } catch (Exception e) {
            log.error("会员实体卡挂失：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "加入黑名单/取消黑名单")
    @ApiOperation("加入黑名单/取消黑名单")
    @PostMapping(value = "/updateStatus")
    @PreAuthorize("hasAuthority('ums:UmsMember:update')")
    @Transactional
    public Object updateStatus(@RequestBody UmsMember entity) {
        try {
            UmsMember coupon = IUmsMemberService.getById(entity.getId());
            //添加日志
            UmsMemberLog umsMemberLog= new UmsMemberLog();
            umsMemberLog.setMemberId(entity.getId());//会员id
            umsMemberLog.setFalg(ConstantUtil.member_log_falg_0);//日志
            //0->禁用；1->启用
            if(coupon.getStatus().equals(ConstantUtil.not)){
                coupon.setStatus(Integer.parseInt(ConstantUtil.is));//启用
                umsMemberLog.setContent("加入黑名单会员:"+entity.getUsername());//内容
            }else{
                coupon.setStatus(Integer.parseInt(ConstantUtil.not));//禁用
                umsMemberLog.setContent("取消黑名单会员:"+entity.getUsername());//内容
            }
            IUmsMemberService.updateById(coupon);
            coupon.setIntegration(entity.getIntegration());
            umsMemberLog.setCreateTime(new Date());
            umsMemberLog.setCreateBy(UserUtils.getCurrentMember().getId());
            if (IUmsMemberLogService.save(umsMemberLog)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("会员实体卡挂失：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    // TODO LI 添加发送通知

    @SysLog(MODULE = "ums", REMARK = "后台余额充值")
    @ApiOperation("后台余额充值")
    @PostMapping(value = "/handleEditBlance")
    //  @PreAuthorize("hasAuthority('ums:UmsMember:update')")
    @Transactional
    public Object handleEditBlance(@RequestParam("id") Long id,
                                   @RequestParam("blance") BigDecimal blance) {
        try {
            UmsMember entity = new UmsMember();
            entity.setId(id);
            entity.setBlance(blance);
            UmsMember member = IUmsMemberService.getById(entity.getId());

            UmsMemberBlanceLog blog = new UmsMemberBlanceLog();
            blog.setMemberId(entity.getId());
            blog.setCreateTime(new Date());
            blog.setNote("后台余额充值：" + blance);
            blog.setPrice(entity.getBlance());
            blog.setType(AllEnum.BlanceType.ADD.code());
            memberBlanceLogService.save(blog);
            entity.setBlance(entity.getBlance().add(member.getBlance()));
            if (IUmsMemberService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("后台余额充值：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "后台积分充值")
    @ApiOperation("后台积分充值")
    @PostMapping(value = "/handleEditIntegration")
    //  @PreAuthorize("hasAuthority('ums:UmsMember:update')")
    @Transactional
    public Object handleEditIntegration(@RequestParam("id") Long id,
                                        @RequestParam("integration") Integer integration) {
        try {

            UmsMember entity = new UmsMember();
            entity.setId(id);
            entity.setIntegration(integration);
            UmsMember member = IUmsMemberService.getById(entity.getId());

            entity.setIntegration(entity.getIntegration() + member.getIntegration());
            UmsIntegrationChangeHistory history = new UmsIntegrationChangeHistory();
            history.setMemberId(entity.getId());
            history.setChangeCount(entity.getIntegration());
            history.setCreateTime(new Date());
            history.setChangeType(AllEnum.ChangeType.Add.code());
            history.setOperateNote("后台积分充值:" + integration);
            history.setSourceType(AllEnum.ChangeSource.admin.code());
            history.setOperateMan(UserUtils.getCurrentMember().getId() + "");
            umsIntegrationChangeHistoryService.insert(history);

            if (IUmsMemberService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("后台积分充值：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "删除会员表")
    @ApiOperation("删除会员表")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('ums:UmsMember:delete')")
    public Object deleteUmsMember(@ApiParam("会员表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("会员表id");
            }
            if (IUmsMemberService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除会员表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "给会员表分配会员表")
    @ApiOperation("查询会员表明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ums:UmsMember:read')")
    public Object getUmsMemberById(@ApiParam("会员表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("会员表id");
            }
            UmsMember entity = new UmsMember();
            entity.setId(id);
            return new CommonResult().success(IUmsMemberService.selectMember(new Page<Map<String, Object>>(0, 10),
                    entity));

//            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询会员表明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除会员表")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除会员表")
    @PreAuthorize("hasAuthority('ums:UmsMember:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = IUmsMemberService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @ApiOperation("更新会员的订单信息")
    @PostMapping(value = "/updateMemberOrderInfo")
    public Object updateMemberOrderInfo() {
        try {
            IUmsMemberService.updataMemberOrderInfo();
            return new CommonResult().success();
        } catch (Exception e) {
            log.error("更新会员的订单信息：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
    }


}
