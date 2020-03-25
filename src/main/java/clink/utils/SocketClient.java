package clink.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public class SocketClient {
    // 搭建客户端
    public static void main(String[] args) throws IOException {
        try {

            Socket socket = new Socket("127.0.0.1", 9087);
            System.out.println("客户端启动成功");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            PrintWriter write = new PrintWriter(socket.getOutputStream());


            for (int a = 0; a < 100; a++) {
                write.println(String.valueOf(Math.random()));
                Thread.sleep(100);
                System.out.println(a);
                write.flush();
            }


            write.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("can not listen to:" + e);// 出错，打印出错信息
        }
    }

}