package com.zscat.mallplus.vo.jifen;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PrizePool implements Serializable {
    /**
     * 总池值
     */
    private int total;
    /**
     * 池中的奖品
     */
    private List<PrizePoolBean> poolBeanList;
}
