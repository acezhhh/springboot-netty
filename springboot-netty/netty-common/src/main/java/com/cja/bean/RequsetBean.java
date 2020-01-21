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
public class RequsetBean implements Serializable {

    private Object[] data;

    private String className;

    private String method;

    private String resourceId;

    public RequsetBean() {}

}
