package com.ecust.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel=SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress=new InetSocketAddress("127.0.0.1",6666);
        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)){//未连接进入
            while (!socketChannel.finishConnect()){//当还没有建立连接时循环于此
                System.out.println("做客户端自己的事");
            }
        }
//        连接建立
        String str="hello world";
        ByteBuffer byteBuffer=ByteBuffer.wrap(str.getBytes());
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
