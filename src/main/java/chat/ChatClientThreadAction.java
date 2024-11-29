package chat;

import socket.TcpThreadAction;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class ChatClientThreadAction implements TcpThreadAction {
    @Override
    public String processInput(String input) {
        String[] split = input.split(" ");

        switch (split[0]) {
            case "suc" -> {
                switch (split[1]) {
                    case "reg" -> System.out.println("Registrierung erfolgreich.");
                    case "an" -> System.out.println("Anmeldung erfolgreich.");
                    case "chpwd" -> System.err.println("Passwortänderung erfolgreich.");
                    default -> {
                    }
                }
            }
            case "err" -> {
                switch (split[1]) {
                    case "reg" ->
                        System.out.println("Ihre Eingaben für die Registrierung sind ungültig. Bitte versuchen sie es erneut.");
                    case "an" ->
                        System.out.println("Ihre Eingaben für die Anmeldung sind ungültig. Bitte versuchen sie es erneut.");
                    case "chpwd" ->
                        System.err.println("Ihre Eingaben für die Passwortänderung sind ungültig. Bitte versuchen sie es erneut");
                    default -> {
                    }
                }
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
