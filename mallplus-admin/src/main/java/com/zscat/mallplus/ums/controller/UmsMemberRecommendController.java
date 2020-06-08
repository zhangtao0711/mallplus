package com.zscat.mallplus.ums.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.ums.entity.UmsMemberRecommend;
import com.zscat.mallplus.ums.service.IUmsMemberRecommendService;
import com.zscat.mallplus.util.EasyPoiUtils;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Date;
import java.util.Map;

/**
 * @author lyn
 * @date 2020-06-06
 * 用户推荐列表
 */
@Slf4j
@RestController
@Api(tags = "UmsMemberRecommendController", description = "会员用户推荐列表")
@RequestMapping("/ums/umsMemberRecommend")
public class UmsMemberRecommendController {

    @Resource
    private IUmsMemberRecommendService IUmsMemberRecommendService;

    @SysLog(MODULE = "ums", REMARK = "根据条件查询所有用户推荐列表列表")
    @ApiOperation("根据条件查询所有用户推荐列表列表")
    @GetMapping(value = "/list")
//    @PreAuthorize("hasAuthority('ums:umsMemberRecommend:read')")
    public Object getUmsMemberRecommendByPage(UmsMemberRecommend entity,
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
//            return new CommonResult().success(IUmsMemberRecommendService.page(new Page<UmsMemberRecommend>(pageNum, pageSize), new QueryWrapper<>(entity)));
            return new CommonResult().success(IUmsMemberRecommendService.selectMemberRecommend(new Page<Map<String, Object>>(pageNum, pageSize),
                    entity));
        } catch (Exception e) {
            log.error("根据条件查询所有用户推荐列表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "保存用户推荐列表")
    @ApiOperation("保存用户推荐列表")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('ums:umsMemberRecommend:create')")
    public Object saveUmsMemberRecommend(@RequestBody UmsMemberRecommend entity) {
        try {

            if (IUmsMemberRecommendService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存用户推荐列表：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "更新用户推荐列表")
    @ApiOperation("更新用户推荐列表")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('ums:umsMemberRecommend:update')")
    public Object updateUmsMemberRecommend(@RequestBody UmsMemberRecommend entity) {
        try {
            if (IUmsMemberRecommendService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新用户推荐列表：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "删除用户推荐列表")
    @ApiOperation("删除用户推荐列表")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('ums:umsMemberRecommend:delete')")
    public Object deleteUmsMemberRecommend(@ApiParam("用户推荐列表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("用户推荐列表id");
            }
            if (IUmsMemberRecommendService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除用户推荐列表：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage());
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "ums", REMARK = "给用户推荐列表分配用户推荐列表")
    @ApiOperation("查询用户推荐列表明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ums:umsMemberRecommend:read')")
    public Object getUmsMemberRecommendById(@ApiParam("用户推荐列表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("用户推荐列表id");
            }
            UmsMemberRecommend coupon = IUmsMemberRecommendService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询用户推荐列表明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除用户推荐列表")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @SysLog(MODULE = "ums", REMARK = "批量删除用户推荐列表")
    @PreAuthorize("hasAuthority('ums:umsMemberRecommend:delete')")
    public Object deleteBatch(@RequestParam("ids") List
            <Long> ids) {
        boolean count = IUmsMemberRecommendService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    @SysLog(MODULE = "ums", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, UmsMemberRecommend entity) {
        // 模拟从数据库获取需要导出的数据
        List<UmsMemberRecommend> personList = IUmsMemberRecommendService.list(new QueryWrapper<>(entity));
        // 导出操作
        EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", UmsMemberRecommend.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "ums", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
        List<UmsMemberRecommend> personList = EasyPoiUtils.importExcel(file, UmsMemberRecommend.class);
        IUmsMemberRecommendService.saveBatch(personList);
    }
}


