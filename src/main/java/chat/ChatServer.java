package chat;

import socket.TcpServer;
import java.util.HashMap;

public class ChatServer {
    public static void main(String[] args) {
        TcpServer<ChatServerState> server = new TcpServer<>(9876);

        ChatServerAction action = new ChatServerAction(server);
        server.setAction(action);

        ChatServerState state = new ChatServerState(new HashMap<>(), new HashMap<>(), "email", "username", "password");
        server.setSharedState(state);

        server.run();
    }
}
