package chat;

import socket.TcpStreamAction;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ChatClientStream implements TcpStreamAction {
    private static final Logger logger = LogManager.getLogManager().getLogger(ChatClientStream.class.getName());

    @Override
    public String processInput(String msg) {
        String[] split = msg.split(" ");

        switch (split[0]) {
            case "suc" -> ChatClientStream.logger.log(Level.FINE, msg);
            case "err" -> ChatClientStream.logger.log(Level.WARNING, msg);
            case "con" -> {
                String username = URLDecoder.decode(split[1], StandardCharsets.UTF_8);

                System.out.println("User " + username + " connected");
            }
            case "dis" -> {
                String username = URLDecoder.decode(split[1], StandardCharsets.UTF_8);

                System.out.println("User " + username + " disconnected");
            }
            case "msg" -> {
                String username = URLDecoder.decode(split[1], StandardCharsets.UTF_8);
                String message = URLDecoder.decode(split[2], StandardCharsets.UTF_8);

                System.out.println("User " + username + " says \"" + message + "\"");
            }
            default -> ChatClientStream.logger.log(Level.WARNING, "unknown message received: \"" + msg + "\"");
        }

        return "";
    }
}
