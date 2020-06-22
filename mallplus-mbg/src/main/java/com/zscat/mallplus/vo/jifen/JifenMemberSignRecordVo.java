package com.zscat.mallplus.vo.jifen;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.jifen.entity.JifenDealerIntegrationChangeHistory;
import com.zscat.mallplus.jifen.entity.JifenMemberSignRecord;
import com.zscat.mallplus.ums.entity.UmsIntegrationChangeHistory;
import com.zscat.mallplus.utils.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.Year;
import java.util.Date;

/**
 * @author wang
 * @date 2020-06-22
 * 签到记录
 */
@Data
public class JifenMemberSignRecordVo {

    private JifenMemberSignRecord record;

    private JifenDealerIntegrationChangeHistory dealerIntegrationChangeHistory;

    private UmsIntegrationChangeHistory umsIntegrationChangeHistory;

}
