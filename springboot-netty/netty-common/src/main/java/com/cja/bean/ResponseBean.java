package com.cja.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@ToString
public class ResponseBean<T> implements Serializable {

    private T data;

    private String code = "200";

    private String msg = "ok";

    private String resourceId;

    public ResponseBean() {}


}
