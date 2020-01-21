package com.example.demo.controller;

import com.cja.bean.ResponseBean;
import com.cja.entity.MessageInfo;
import com.cja.service.MessageService;
import com.cja.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TestService testService;

    @GetMapping("/testSend/{ttt}")
    public ResponseBean testSend(@PathVariable("ttt") String content) {
        MessageInfo messageInfo = new MessageInfo().toBuilder()
                .content(content)
                .build();
        messageService.addMessageInfo(messageInfo);
        return new ResponseBean();
    }

    @GetMapping("/sendRPC")
    public List<MessageInfo> sendRPC() {
        return messageService.findMessageInfoAll();
    }

    @GetMapping("/test")
    public Object test() {
        testService.test();
        return new ResponseBean();
    }

}
