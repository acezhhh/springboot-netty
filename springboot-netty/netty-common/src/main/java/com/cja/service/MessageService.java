package com.cja.service;


import com.cja.entity.MessageInfo;
import com.cja.rpc.RPCClient;

import java.util.List;

@RPCClient(name = "message")
public interface MessageService {

    void addMessageInfo(MessageInfo messageInfo);

    List<MessageInfo> findMessageInfoAll();
}
