package com.cja.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.concurrent.CountDownLatch;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@ToString
public class ResultSync {

    private ResponseBean responseBean;

    private CountDownLatch countDownLatch;

    public ResultSync(){}
}
