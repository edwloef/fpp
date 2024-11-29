package chat;

import socket.TcpStreamAction;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class ChatClientAction implements TcpStreamAction {
    @Override
    public String processInput(String input) {
        String[] split = input.split(" ");

        switch (split[0]) {
            case "suc", "err" -> {
            }
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
            default -> {
            }
        }

        return "";
    }
}
