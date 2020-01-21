package com.example.demo.service.impl;

import com.cja.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public void test() {
        System.out.println("TestServiceImpl.test");
    }

}
