package com.cyx.task;

import com.cyx.pojo.ProgressMsg;
import com.google.gson.Gson;
import javafx.concurrent.Task;

import java.io.*;
import java.net.Socket;

public class ReceiveTask extends Task<ProgressMsg> {


    private final Socket currentSocket;


    public ReceiveTask(Socket currentSocket) {
        this.currentSocket = currentSocket;
    }

    @Override
    protected ProgressMsg call() throws Exception {

        System.out.println(currentSocket.getInetAddress() + ":" + currentSocket.getPort() + "已连接。");

        DataInputStream inputStream = new DataInputStream(currentSocket.getInputStream());
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

        int length;
        byte[] buffer = new byte[1024];
        while ((length = inputStream.read(buffer)) != -1) {
            byteOutputStream.write(buffer, 0, length);
        }

        String receiveStr = byteOutputStream.toString("utf-8");
        System.out.println("receiveStr==>" + receiveStr);
        ProgressMsg progressMsg = new Gson().fromJson(receiveStr, ProgressMsg.class);
        inputStream.close();
        byteOutputStream.close();
        currentSocket.close();
        return progressMsg;
    }

    @Override
    protected void updateValue(ProgressMsg value) {
        super.updateValue(value);
    }
}
