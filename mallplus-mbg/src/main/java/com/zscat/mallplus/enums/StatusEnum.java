package com.zscat.mallplus.enums;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 交易API Constant
 *
 * @author dp
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusEnum {


    /**
     * 审核类型：
     *
     * @author mallplus
     */
    public enum AuditType implements BaseEnum<Integer> {

        /**
         * 初始状态
         */
        INIT(2, "init"),
        /**
         * 审核成功
         */
        FAIL(3, "fail"),
        /**
         * 审核失败
         */
        SUCESS(1, "sucess"),
        ;

        private int code;
        private String value;

        AuditType(int code, String value) {
            this.code = code;
            this.value = value;
        }

        @Override
        public Integer code() {
            return code;
        }

        @Override
        public String desc() {
            return value;
        }
    }

    /**
     * 审核类型：
     *
     * @author mallplus
     */
    public enum YesNoType implements BaseEnum<Integer> {

        /**
         * 启用
         */
        YES(1, "yes"),

        /**
         * 禁用
         */
        NO(0, "no"),
        ;

        private int code;
        private String value;

        YesNoType(int code, String value) {
            this.code = code;
            this.value = value;
        }

        @Override
        public Integer code() {
            return code;
        }

        @Override
        public String desc() {
            return value;
        }
    }
}
