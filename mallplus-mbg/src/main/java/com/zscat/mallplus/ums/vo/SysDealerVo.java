package com.zscat.mallplus.ums.vo;

import com.zscat.mallplus.sys.entity.SysUser;
import com.zscat.mallplus.ums.entity.SysAppletSet;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * <p>
 * 经销商的基本信息表
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Data
public class SysDealerVo implements Serializable {

   @Valid
   private SysUser user;

   @Valid
   private SysAppletSet appletSet;

}
