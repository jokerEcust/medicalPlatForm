package com.ecust.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

/**
 * NIO demo
 * 当客户端连接server时会通过ServerSocketChannel得到SocketChannel（就是之前说的channel通道）
 * 创建好channel后将其注册到selector上，注册形式是放到SelectionKey集合中，selector通过select方法找到有事件发生的selectionKey
 * 之后反向获取实际的SocketChannel，进行相关业务
 *
 */
public class Main {
    public static void main(String[] args) throws Exception{

        //创建出serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //拿到selector对象
        Selector selector = Selector.open();
//        绑定一个端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        serverSocketChannel.configureBlocking(false);//设置为非阻塞
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//设置关心的事件为OP_ACCEPT（客户端连接）

        //等待客户端连接
        while (true){
            if(selector.select(1000)==0){
                //无事件发生
                System.out.println("无连接");
                continue;
            }
            //这里表示有事件发生
//            获取相关的selectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey selectionKey : selectionKeys) {
//                selectionKey关联的通道是什么
                if (selectionKey.isAcceptable()){//连接事件,说明刚开始连接，还没有分配通道
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
//                    将socketChannel注册到selector上，并监听新的事件——————读取
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                //如果是读取事件说明当前的通道已经存在
                if(selectionKey.isReadable()) {
                    //通过key反向获取channel
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
//                    获取buffer
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
//                    将channel的数据放入buffer
                    channel.read(buffer);
                    System.out.println("客户端发送数据："+new String(buffer.array()));

                }
                selectionKeys.remove(selectionKey);
            }
        }

    }
}
