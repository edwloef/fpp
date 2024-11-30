package chat;

import socket.TcpServer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class ChatServer {
    public static void main(String[] args) {

        TcpServer<ChatServerState> server = new TcpServer<>(9876);

        Scanner sc = new Scanner(System.in);

        System.out.print("email-server passwort: ");
        String password = sc.next();

        ChatServerState state = new ChatServerState(new HashMap<>(), new HashMap<>(), new HashSet<>(), password);
        server.setSharedState(state);

        ChatServerThreadAction action = new ChatServerThreadAction(server);
        server.setAction(action);

        System.out.println("starting server...");

        server.run();
    }
}
