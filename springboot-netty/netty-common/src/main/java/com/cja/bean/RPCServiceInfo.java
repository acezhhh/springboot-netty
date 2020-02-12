package com.cja.bean;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class RPCServiceInfo {

    private String ip;

    private Integer port;

    private String name;

}
