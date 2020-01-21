package com.example.demo.repository;

import com.cja.entity.MessageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageInfoRepository extends JpaRepository<MessageInfo, String> {
    List<MessageInfo> findAllByOrderByCreateTimeDesc();
}

