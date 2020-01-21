package com.cja.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "message_info")
@Data
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class MessageInfo implements Serializable {
    @Id
    private String id;

    @Column(name="content")
    private String content;

    @Column(name="create_time")
    private Date createTime;

    public MessageInfo(){}
}
