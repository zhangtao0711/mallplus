package com.zscat.mallplus.vo.jifen;

import lombok.Data;

import java.io.Serializable;

@Data
public class PrizePoolBean implements Serializable {
    /**
     * 数据库中真实奖品的ID
     */
    private Long id;
    /**
     * 数据库中真实奖品的名称
     */
    private String name;
    /**
     * 数据库中真实奖品的图片
     */
    private String img;
    /**
     * 奖品的开始池值
     */
    private int begin;
    /**
     * 奖品的结束池值
     */
    private int end;
}
