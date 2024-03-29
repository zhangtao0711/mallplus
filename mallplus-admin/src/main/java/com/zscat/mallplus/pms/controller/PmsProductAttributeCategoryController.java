package com.zscat.mallplus.pms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.annotation.SysLog;
import com.zscat.mallplus.pms.entity.EsShopGoodsGroupMap;
import com.zscat.mallplus.pms.entity.PmsProduct;
import com.zscat.mallplus.pms.entity.PmsProductAttributeCategory;
import com.zscat.mallplus.pms.mapper.PmsGoodsGroupMapMapper;
import com.zscat.mallplus.pms.service.IPmsProductAttributeCategoryService;
import com.zscat.mallplus.pms.service.IPmsProductService;
import com.zscat.mallplus.pms.vo.PmsProductAttributeCategoryItem;
import com.zscat.mallplus.utils.CommonResult;
import com.zscat.mallplus.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 产品属性分类表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "PmsProductAttributeCategoryController", description = "产品属性分类表管理")
@RequestMapping("/pms/PmsProductAttributeCategory")
public class PmsProductAttributeCategoryController {
    @Resource
    private IPmsProductAttributeCategoryService IPmsProductAttributeCategoryService;
    @Resource
    private PmsGoodsGroupMapMapper shopGoodsGroupMapMapper;

    @Resource
    private IPmsProductService pmsProductService;

    @SysLog(MODULE = "pms", REMARK = "根据条件查询所有产品属性分类表列表")
    @ApiOperation("根据条件查询所有产品属性分类表列表")
    @GetMapping(value = "/list")
    public Object getPmsProductAttributeCategoryByPage(PmsProductAttributeCategory entity,
                                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IPmsProductAttributeCategoryService.page(new Page<PmsProductAttributeCategory>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有产品属性分类表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "保存产品属性分类表")
    @ApiOperation("保存产品属性分类表")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('pms:PmsProductAttributeCategory:create')")
    public Object create(@RequestParam String name,
                         @RequestParam(value = "pic", required = false) String pic,
                         @RequestParam(value = "showIndex", required = false) Integer showIndex,
                         @RequestParam(value = "style", required = false) Integer style) {
        try {
            PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
            productAttributeCategory.setName(name);
            productAttributeCategory.setShowIndex(showIndex);
            productAttributeCategory.setStyle(style);
            productAttributeCategory.setPic(pic);
            if (IPmsProductAttributeCategoryService.save(productAttributeCategory)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存产品属性分类表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "保存产品属性分类表")
    @ApiOperation("保存产品属性分类表")
    @PostMapping(value = "/createSingle")
    @PreAuthorize("hasAuthority('pms:PmsProductAttributeCategory:create')")
    public Object createSingle(@RequestBody PmsProductAttributeCategory productAttributeCategory) {
        try {
            if (IPmsProductAttributeCategoryService.save(productAttributeCategory)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存产品属性分类表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "更新产品属性分类表")
    @ApiOperation("更新产品属性分类表")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProductAttributeCategory:update')")
    public Object updatePmsProductAttributeCategory(@PathVariable Long id, @RequestParam String name) {
        try {
            PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
            productAttributeCategory.setName(name);
            productAttributeCategory.setId(id);
            if (IPmsProductAttributeCategoryService.updateById(productAttributeCategory)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新产品属性分类表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "删除产品属性分类表")
    @ApiOperation("删除产品属性分类表")
    @GetMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('pms:PmsProductAttributeCategory:delete')")
    public Object deletePmsProductAttributeCategory(@ApiParam("产品属性分类表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("产品属性分类表id");
            }
            if (IPmsProductAttributeCategoryService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除产品属性分类表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "pms", REMARK = "给产品属性分类表分配产品属性分类表")
    @ApiOperation("查询产品属性分类表明细")
    @GetMapping(value = "/{id}")
    public Object getPmsProductAttributeCategoryById(@ApiParam("产品属性分类表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("产品属性分类表id");
            }
            PmsProductAttributeCategory coupon = IPmsProductAttributeCategoryService.getById(id);
            if (coupon != null) {
                List<EsShopGoodsGroupMap> list = shopGoodsGroupMapMapper.selEsShopGoodsGroupMap(coupon.getId());
                List<PmsProduct> lists = new ArrayList<PmsProduct>();
                for (EsShopGoodsGroupMap lis : list) {
                    //根据商品ID查询商品名称、商品图片、商品虚拟库存
                    PmsProduct es = pmsProductService.getById(lis.getGoodsId());
                    if (es != null) {
                        lists.add(es);
                    }
                }
                coupon.setListGoods(lists);
            }
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询产品属性分类表明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除产品属性分类表")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.GET)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除产品属性分类表")
    @PreAuthorize("hasAuthority('pms:PmsProductAttributeCategory:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = IPmsProductAttributeCategoryService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

    @SysLog(MODULE = "pms", REMARK = "获取所有商品属性分类及其下属性")
    @ApiOperation("获取所有商品属性分类及其下属性")
    @RequestMapping(value = "/list/withAttr", method = RequestMethod.GET)
    @ResponseBody
    public Object getListWithAttr() {
        List<PmsProductAttributeCategoryItem> productAttributeCategoryResultList = IPmsProductAttributeCategoryService.getListWithAttr();
        return new CommonResult().success(productAttributeCategoryResultList);
    }
}
