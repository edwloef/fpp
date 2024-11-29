package chat;

import socket.TcpClient;
import socket.TcpStream;

import java.io.IOException;

public class ChatClient {
    public static void main(String[] args) {
        ChatClientAction action = new ChatClientAction();

        TcpClient client = new TcpClient("localhost", 9876, action);
        try {
            TcpStream stream = client.setup();

            stream.start();

            stream.notify("msg asdf");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
