package com.zscat.mallplus.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.ums.entity.SysAppletSet;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-06-15
 */
public interface SysAppletSetMapper extends BaseMapper<SysAppletSet> {

    List<Map<String,Object>> getMonitorFirm(@Param("level")Integer level, @Param("value") String value,
                                            @Param("storeId")Integer storeId);

}
