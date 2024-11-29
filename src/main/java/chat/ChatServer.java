package chat;

import socket.TcpServer;

import java.util.HashMap;
import java.util.Scanner;

public class ChatServer {
    public static void main(String[] args) {

        TcpServer<ChatServerState> server = new TcpServer<>(9876);

        ChatServerAction action = new ChatServerAction(server);
        server.setAction(action);

        Scanner sc = new Scanner(System.in);

        System.out.print("email-server adresse: ");
        String email = sc.next();

        System.out.print("email-server benutzername: ");
        String username = sc.next();

        System.out.print("email-server passwort: ");
        String password = sc.next();

        ChatServerState state = new ChatServerState(new HashMap<>(), new HashMap<>(), email, username, password);
        server.setSharedState(state);

        server.run();
    }
}
