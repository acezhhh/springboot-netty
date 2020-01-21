package com.example.demo.service.impl;

import com.cja.entity.MessageInfo;
import com.cja.service.MessageService;
import com.example.demo.repository.MessageInfoRepository;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageInfoRepository messageInfoRepository;

    @Override
    public void addMessageInfo(MessageInfo messageInfo) {
        messageInfo = new MessageInfo().toBuilder()
                .id(UUID.randomUUID().toString())
                .content(messageInfo.getContent())
                .createTime(new Date())
                .build();
        if("qwe".equals(messageInfo.getContent())){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        messageInfoRepository.save(messageInfo);
    }

    @Override
    public List<MessageInfo> findMessageInfoAll() {
        return messageInfoRepository.findAllByOrderByCreateTimeDesc();
    }

}
