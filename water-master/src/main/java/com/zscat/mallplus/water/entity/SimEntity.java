package com.zscat.mallplus.water.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lyn
 * @date 2020-05-22
 * SIM卡第三方appkey
 */
@Data
public class SimEntity {
    /**
     * 查询结果
     **/
    private String msg;
    /**
     * 查询code
     **/
    private String code;

    /**
     * data
     */
    private SimDataEntity data;
}
