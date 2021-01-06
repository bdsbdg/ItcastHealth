package com.itheima.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {
    private Boolean flag;
    private String msg;
    private Object data;

    public Result(Boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }
}
