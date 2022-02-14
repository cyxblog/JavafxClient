package com.cyx.task;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SendTask{

    public static void send(int port, String info){
        try {
            Socket socket = new Socket("localhost", port);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.write(info.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
