package com.xuecheng.media.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("mq_message")
public class MqMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String messageType;

    private String businessKey1;

    private String businessKey2;

    private String businessKey3;

    private String mqHost;

    private Integer mqPort;

    private String mqVirtualhost;

    private String mqQueue;

    private Integer infoNum;

    private Integer state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnFailureDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnSuccessDate;

    private String returnFailureMsg;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime infoDate;
}
