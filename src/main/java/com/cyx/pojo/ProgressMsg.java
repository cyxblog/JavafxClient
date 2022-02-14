package com.cyx.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressMsg {
    private String msgType;		// 进度信息类型，这里为“sendProgress”或receiveProgress
    private int identification;	// 一次文件传输事件标识，由前端随机生成
    private int percentage;		// 文件发送进度，为正整数。
    private String senderName;		// 文件发送方代号
    private String receiverName;    // 文件接收方代号
    private String fileName;	 	// 文件名
    private long fileLength; 	// 文件长度，单位为byte
}
