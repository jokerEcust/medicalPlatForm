package com.ecust.demo.MQ;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BrokerServer implements Runnable {
    public static int SERVICE_PORT=9999;
    private final Socket socket;

    public BrokerServer(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        try(
                BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out=new PrintWriter(socket.getOutputStream());
                ){
            while (true){
                String s = in.readLine();
                if (s==null){
                    continue;
                }
                System.out.println("收到数据:"+s);
                if (s.equals("CONSUME")){
                    String msg = Broker.consume();
                    out.println(msg);
                    out.flush();
                }else {
                    Broker.produce(s);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(SERVICE_PORT);
        while (true){
            BrokerServer brokerServer = new BrokerServer(serverSocket.accept());
            new Thread(brokerServer).start();
        }
    }
}
