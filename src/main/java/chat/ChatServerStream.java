package chat;

import common.Pair;
import socket.TcpServer;
import socket.TcpServerAction;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

// Triple:
//  1. E-Mail -> Username
//  2. E-Mail -> Password

public class ChatServerStream extends TcpServerAction<Pair<HashMap<String, String>, HashMap<String, String>>> {
    private static final Logger logger = LogManager.getLogManager().getLogger(ChatServerStream.class.getName());
    private String email;

    public ChatServerStream(TcpServer<Pair<HashMap<String, String>, HashMap<String, String>>> server) {
        super(server);

        if (super.server.sharedState == null) {
            ChatServerStream.logger.log(Level.WARNING, "chat server initialized with empty shared state");
        }
    }

    @Override
    public String processInput(String msg) throws IOException {
        ChatServerStream.logger.log(Level.INFO, msg);

        String[] split = msg.split(" ");

        switch (split[0]) {
            case "reg" -> {
                String email = split[1];
                String username = split[2];

                if (super.server.sharedState.one().containsKey(email)) {
                    return "err reg";
                }

                // email shit hier hin

                super.server.sharedState.one().put(email, username);
            }
            case "an" -> {
                String email = split[1];
                String password = split[2];
                String username = super.server.sharedState.one().get(email);

                String correctPassword = super.server.sharedState.two().get(email);

                if (!password.equals(correctPassword)) {
                    return "err an";
                }

                super.server.broadcast("con " + username);

                StringBuilder returns = new StringBuilder();

                for (String userEmail : super.server.sharedState.two().keySet()) {
                    String userUsername = super.server.sharedState.one().get(userEmail);

                    returns.append("con ").append(userUsername).append('\n');
                }

                this.email = email;

                return returns + "suc an";
            }
            case "chpwd" -> {
                String password = split[1];
                String newPassword = split[2];

                String correctPassword = super.server.sharedState.two().get(this.email);

                if (this.email == null || !password.equals(correctPassword)) {
                    return "err chpwd";
                }

                super.server.sharedState.two().replace(this.email, newPassword);

                return "suc chpwd";
            }
            case "msg" -> {
                String username = super.server.sharedState.one().get(this.email);

                super.server.broadcast("msg " + username + " " + split[1]);
            }
            case "err" -> ChatServerStream.logger.log(Level.WARNING, msg);
            default -> ChatServerStream.logger.log(Level.WARNING, "unknown message received: \"" + msg + "\"");
        }

        return "";
    }

    @Override
    public void clientDisconnect() throws IOException {
        String username = super.server.sharedState.one().get(this.email);

        super.server.broadcast("dis " + username);
    }
}
