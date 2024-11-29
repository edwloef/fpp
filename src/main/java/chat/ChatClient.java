package chat;

import socket.SocketHandlerThread;
import socket.TcpClient;

import java.io.IOException;
import java.util.Scanner;

public class ChatClient {

    public static void main(String[] args) {
        ChatClientThreadAction action = new ChatClientThreadAction();

        TcpClient client = new TcpClient("localhost", 9876, action);
        try {
            SocketHandlerThread stream = client.setup();

            System.out.println("starting client...");

            stream.start();

            Scanner sc = new Scanner(System.in);
            while (true) {
                stream.notify(sc.nextLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
