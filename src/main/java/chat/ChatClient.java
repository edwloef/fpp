package chat;

import socket.TcpClient;
import socket.TcpStream;

import java.io.IOException;

import java.util.Scanner;

public class ChatClient {

    private static final Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        ChatClientAction action = new ChatClientAction();

        

        TcpClient client = new TcpClient("localhost", 9876, action);
        try {
            TcpStream stream = client.setup();

            stream.start();

            while(true){
                String msg = sc.next();
                stream.notify("msg" + msg);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
