package com.zscat.mallplus.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.sys.entity.SysUserStaff;
import com.zscat.mallplus.sys.service.ISysUserService;
import com.zscat.mallplus.sys.service.ISysUserStaffService;
import com.zscat.mallplus.ums.entity.UmsMember;
import com.zscat.mallplus.ums.service.IUmsMemberService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.util.UserUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author mallplus
 * @date 2020-05-20
 * 员工账号
 */
@Slf4j
@RestController
@RequestMapping("/staff/sysUserStaff")
public class SysUserStaffController {

    @Resource
    private ISysUserStaffService ISysUserStaffService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private IUmsMemberService memberService;

    @SysLog(MODULE = "staff", REMARK = "根据条件查询所有员工账号列表")
    @ApiOperation("根据条件查询所有员工账号列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('staff:sysUserStaff:read')")
    public Object getSysUserStaffByPage(SysUserStaff entity,
                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISysUserStaffService.page(new Page<SysUserStaff>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有员工账号列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "staff", REMARK = "保存员工账号")
    @ApiOperation("保存员工账号")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('staff:sysUserStaff:create')")
    public Object saveSysUserStaff(@RequestBody SysUserStaff entity) {
        SysUser user = new SysUser();
        user.setUsername(entity.getUsername());
        SysUser user1 =sysUserService.getOne(new QueryWrapper<>(user));
        if (user1!=null){
            return new CommonResult().failed("该用户名已注册!");
        }
        SysUserStaff staff = new SysUserStaff();
        staff.setUsername(entity.getUsername());
        SysUserStaff staff1 =ISysUserStaffService.getOne(new QueryWrapper<>(staff));
        if (staff1!=null){
            return new CommonResult().failed("该用户名已注册!");
        }
        UmsMember member = new UmsMember();
        member.setUsername(entity.getUsername());
        UmsMember member1 =memberService.getOne(new QueryWrapper<>(member));
        if (member1!=null){
            return new CommonResult().failed("该用户名已注册!");
        }
        try {
            entity.setCreateTime(new Date());
            if (ValidatorUtils.empty(entity.getStoreId())) {
                entity.setStoreId(UserUtils.getCurrentMember().getStoreId());
            }
            entity.setStoreName(UserUtils.getCurrentMember().getStoreName());
            if (ISysUserStaffService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存员工账号：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "staff", REMARK = "更新员工账号")
    @ApiOperation("更新员工账号")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('staff:sysUserStaff:update')")
    public Object updateSysUserStaff(@RequestBody SysUserStaff entity) {
        try {
            if (ISysUserStaffService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新员工账号：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "staff", REMARK = "删除员工账号")
    @ApiOperation("删除员工账号")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('staff:sysUserStaff:delete')")
    public Object deleteSysUserStaff(@ApiParam("员工账号id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("员工账号id");
            }
            if (ISysUserStaffService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除员工账号：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "staff", REMARK = "给员工账号分配员工账号")
    @ApiOperation("查询员工账号明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('staff:sysUserStaff:read')")
    public Object getSysUserStaffById(@ApiParam("员工账号id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("员工账号id");
            }
            SysUserStaff coupon = ISysUserStaffService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询员工账号明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除员工账号")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "staff", REMARK = "批量删除员工账号")
    @PreAuthorize("hasAuthority('staff:sysUserStaff:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = ISysUserStaffService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "staff", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, SysUserStaff entity) {
        // 模拟从数据库获取需要导出的数据
        List<SysUserStaff> personList = ISysUserStaffService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", SysUserStaff.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "staff", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<SysUserStaff> personList = EasyPoiUtils.importExcel(file, SysUserStaff.class);
        ISysUserStaffService.saveBatch(personList);
    }

    @SysLog(MODULE = "staff", REMARK = "修改密码")
    @PutMapping("/resetPwd")
    public Object resetPwd(@RequestBody SysUserStaff staff) {
        return ISysUserStaffService.resetPwd(staff);
    }

    @ApiOperation("修改状态-冻结/启用-批量")
    @RequestMapping(value = "/update/updateShowStatus", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "staff", REMARK = "修改状态")
    public Object updateShowStatus(@RequestParam("ids") List<Long> ids,
                                   @RequestParam("showStatus") Integer showStatus) {
        SysUserStaff role = new SysUserStaff();
        for (Long id:ids) {
            role.setId(id);
            role.setStatus(showStatus);
            ISysUserStaffService.updateById(role);
        }
        return new CommonResult().success();
    }

    @ApiOperation("员工绑定微信号，获取openID")
    @RequestMapping(value = "/BindWeChant", method = RequestMethod.GET)
    @ResponseBody
    @SysLog(MODULE = "staff", REMARK = "员工绑定微信号，获取openID")
    public Object staffBindWeChant(@RequestParam("value") String value,
                                   @RequestParam("username") String username) {
        //username是当前登录者的用户名称
        List<Map<String,Object>> list = ISysUserStaffService.bindWeChant(value,username);
        return new CommonResult().success(list);
    }
}


