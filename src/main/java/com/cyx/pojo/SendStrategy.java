package com.cyx.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendStrategy {
    private String msgType;			// 消息类型，此处为“send_strategy”
    private int identification;		// 一次文件传输标识，由前端随机生成
    private String senderName;		// 发送方代号
    private String receiverName;		// 接收方代号
    private String srcFilePath;			// 文件本地路径
    private long fileDataLength;		// 文件长度，单位为byte
    private int divideMethod;		// 文件分片方式（2/4/8片）
    private int groupNum;			// 文件分组数量（1/2/3）
    private int timer;				// 接收方等待最长时间
}
