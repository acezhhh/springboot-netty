package com.cja.service;


import com.cja.rpc.RPCClient;

@RPCClient(name = "message")
public interface TestService {
    void test();
}
