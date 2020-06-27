package com.zscat.mallplus.sms.entity;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zscat.mallplus.utils.BaseEntity;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
* @author zhangtao
* @date 2020-06-26
客户推荐
*/
@Data
@TableName("ums_user_recommend")
public class UmsUserRecommend extends BaseEntity implements Serializable {


            @TableId(value = "id", type = IdType.AUTO)
        private Long id;


            /**
            经销商id
            **/
            @TableField( "dealer_id")
        private Long dealerId;


            /**
            店铺id
            **/
            @TableField( "store_id")
        private   Integer storeId;


            /**
            选择优惠套餐id
            **/
            @TableField( "package_id")
        private Long packageId;


            /**
            是否成为下线0否1是
            **/
            @TableField( "isrecommend_status")
        private Integer isrecommendStatus;


            /**
            邀请码状态 0否1是
            **/
            @TableField( "code_status")
        private Integer codeStatus;


            /**
            奖励状态0->关闭 1->开启
            **/
            @TableField( "award_status")
        private Integer awardStatus;


            /**
            奖励类型
            **/
            @TableField( "reword_type")
        private Integer rewordType;


            /**
            到账类型
            **/
            @TableField( "arrval_type")
        private Integer arrvalType;


            /**
            推荐人获得奖励
            **/
            @TableField( "recommend_award")
        private Long recommendAward;


            /**
            被推荐人获得奖励
            **/
            @TableField( "recommended_award")
        private Long recommendedAward;


            /**
            创建时间
            **/
            @TableField( "create_time")
        private Date createTime;


}
