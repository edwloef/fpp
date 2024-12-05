package chat;

import socket.TcpServer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class ChatServer implements Runnable {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.run();
    }

    @Override
    public void run() {
        TcpServer<ChatServerState> server = new TcpServer<>(9876);

        Scanner sc = new Scanner(System.in);

        System.out.print("email-server passwort: ");
        String password = sc.next();

        ChatServerState state = new ChatServerState(new HashMap<>(), new HashMap<>(), new HashSet<>(), password, "mail.gmx.net", "loeffler.steiner.fpp@gmx.de");
        server.setSharedState(state);

        ChatServerThreadAction action = new ChatServerThreadAction(server);
        server.setAction(action);

        System.out.println("starting server...");

        server.run();
    }
}
