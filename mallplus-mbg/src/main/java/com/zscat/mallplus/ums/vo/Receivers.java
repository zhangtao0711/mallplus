package com.zscat.mallplus.ums.vo;

import lombok.Data;

@Data
public class Receivers {

    private String type;
    private String account;
    private String relationType;

    @Override
    public String toString() {
        return "{" +
                "\"type\"=\"" + type + '\"' +
                ", \"account\"=\"" + account + '\"' +
                ", \"relationType\"=\"" + relationType + '\"' +
                '}';
    }
}
