package com.zscat.mallplus.jifen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.jifen.entity.JifenMemberSignRecord;
import com.zscat.mallplus.jifen.service.IJifenMemberSignRecordService;
import com.zscat.mallplus.util.EasyPoiUtils;
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
import java.util.List;
import java.util.Date;

/**
* @author wang
* @date 2020-06-22
*  签到记录
*/
@Slf4j
@RestController
@RequestMapping("/jifen/jifenMemberSignRecord")
public class JifenMemberSignRecordController {

@Resource
private IJifenMemberSignRecordService IJifenMemberSignRecordService;

@SysLog(MODULE = "jifen", REMARK = "根据条件查询所有签到记录列表")
@ApiOperation("根据条件查询所有签到记录列表")
@GetMapping(value = "/list")
@PreAuthorize("hasAuthority('jifen:jifenMemberSignRecord:read')")
public Object getJifenMemberSignRecordByPage(JifenMemberSignRecord entity,
@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
) {
try {
return new CommonResult().success(IJifenMemberSignRecordService.page(new Page<JifenMemberSignRecord>(pageNum, pageSize), new QueryWrapper<>(entity)));
} catch (Exception e) {
log.error("根据条件查询所有签到记录列表：%s", e.getMessage(), e);
}
return new CommonResult().failed();
}

@SysLog(MODULE = "jifen", REMARK = "保存签到记录")
@ApiOperation("保存签到记录")
@PostMapping(value = "/create")
@PreAuthorize("hasAuthority('jifen:jifenMemberSignRecord:create')")
public Object saveJifenMemberSignRecord(@RequestBody JifenMemberSignRecord entity) {
try {
entity.setCreateTime(new Date());
if (IJifenMemberSignRecordService.save(entity)) {
return new CommonResult().success();
}
} catch (Exception e) {
log.error("保存签到记录：%s", e.getMessage(), e);
return new CommonResult().failed(e.getMessage());
}
return new CommonResult().failed();
}

@SysLog(MODULE = "jifen", REMARK = "更新签到记录")
@ApiOperation("更新签到记录")
@PostMapping(value = "/update/{id}")
@PreAuthorize("hasAuthority('jifen:jifenMemberSignRecord:update')")
public Object updateJifenMemberSignRecord(@RequestBody JifenMemberSignRecord entity) {
try {
if (IJifenMemberSignRecordService.updateById(entity)) {
return new CommonResult().success();
}
} catch (Exception e) {
log.error("更新签到记录：%s", e.getMessage(), e);
return new CommonResult().failed(e.getMessage());
}
return new CommonResult().failed();
}

@SysLog(MODULE = "jifen", REMARK = "删除签到记录")
@ApiOperation("删除签到记录")
@GetMapping(value = "/delete/{id}")
@PreAuthorize("hasAuthority('jifen:jifenMemberSignRecord:delete')")
public Object deleteJifenMemberSignRecord(@ApiParam("签到记录id") @PathVariable Long id) {
try {
if (ValidatorUtils.empty(id)) {
return new CommonResult().paramFailed("签到记录id");
}
if (IJifenMemberSignRecordService.removeById(id)) {
return new CommonResult().success();
}
} catch (Exception e) {
log.error("删除签到记录：%s", e.getMessage(), e);
return new CommonResult().failed(e.getMessage());
}
return new CommonResult().failed();
}

@SysLog(MODULE = "jifen", REMARK = "给签到记录分配签到记录")
@ApiOperation("查询签到记录明细")
@GetMapping(value = "/{id}")
@PreAuthorize("hasAuthority('jifen:jifenMemberSignRecord:read')")
public Object getJifenMemberSignRecordById(@ApiParam("签到记录id") @PathVariable Long id) {
try {
if (ValidatorUtils.empty(id)) {
return new CommonResult().paramFailed("签到记录id");
}
JifenMemberSignRecord coupon = IJifenMemberSignRecordService.getById(id);
return new CommonResult().success(coupon);
} catch (Exception e) {
log.error("查询签到记录明细：%s", e.getMessage(), e);
return new CommonResult().failed();
}

}

@ApiOperation(value = "批量删除签到记录")
@RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
@SysLog(MODULE = "jifen", REMARK = "批量删除签到记录")
@PreAuthorize("hasAuthority('jifen:jifenMemberSignRecord:delete')")
public Object deleteBatch(@RequestParam("ids") List
<Long> ids) {
    boolean count = IJifenMemberSignRecordService.removeByIds(ids);
    if (count) {
    return new CommonResult().success(count);
    } else {
    return new CommonResult().failed();
    }
    }


    @SysLog(MODULE = "jifen", REMARK = "导出社区数据")
    @GetMapping("/exportExcel")
    public void export(HttpServletResponse response, JifenMemberSignRecord entity) {
    // 模拟从数据库获取需要导出的数据
    List<JifenMemberSignRecord> personList = IJifenMemberSignRecordService.list(new QueryWrapper<>(entity));
    // 导出操作
    EasyPoiUtils.exportExcel(personList, "导出社区数据", "社区数据", JifenMemberSignRecord.class, "导出社区数据.xls", response);

    }

    @SysLog(MODULE = "jifen", REMARK = "导入社区数据")
    @PostMapping("/importExcel")
    public void importUsers(@RequestParam MultipartFile file) {
    List<JifenMemberSignRecord> personList = EasyPoiUtils.importExcel(file, JifenMemberSignRecord.class);
    IJifenMemberSignRecordService.saveBatch(personList);
    }
    }


