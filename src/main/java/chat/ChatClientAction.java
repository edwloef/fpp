package chat;

import socket.TcpStreamAction;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ChatClientAction implements TcpStreamAction {
    private static final Logger logger = LogManager.getLogManager().getLogger(ChatClientAction.class.getName());

    @Override
    public String processInput(String input) {
        String[] split = input.split(" ");

        switch (split[0]) {
            case "suc" -> ChatClientAction.logger.log(Level.FINE, input);
            case "err" -> ChatClientAction.logger.log(Level.WARNING, input);
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
            default -> ChatClientAction.logger.log(Level.WARNING, "unknown message received: \"" + input + "\"");
        }

        return "";
    }
}
