package chat;

import socket.TcpThreadAction;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class ChatClientThreadAction implements TcpThreadAction {
    private static final String ANSI_CYAN = "\033[36m"; // blue output color
    private static final String ANSI_RESET = "\033[0m"; // reset output color
    private boolean angemeldet = false;
    private boolean wait;

    @Override
    public String processInput(String input) {
        String[] split = input.split(" ");

        switch (split[0]) {
            case "suc" -> {
                switch (split[1]) {
                    case "reg" -> System.out.println("Registrierung erfolgreich.");
                    case "an" -> this.angemeldet = true;
                    case "chpwd" -> System.err.println("Passwortänderung erfolgreich.");
                    default -> {
                    }
                }
            }
            case "err" -> {
                switch (split[1]) {
                    case "reg" -> System.out.println("Registrierung fehlgeschlagen. Bitte versuchen sie es erneut.");
                    case "an" -> System.out.println("Anmeldung fehlgeschlagen. Bitte versuchen sie es erneut.");
                    case "chpwd" ->
                        System.err.println("Passwortänderung fehlgeschlagen. Bitte versuchen sie es erneut");
                    default -> {
                    }
                }
            }
            case "con" -> {
                if (!this.angemeldet) {
                    return "";
                }

                String username = URLDecoder.decode(split[1], StandardCharsets.UTF_8);

                System.out.println(ANSI_CYAN + username + ANSI_RESET + " angemeldet");
            }
            case "dis" -> {
                if (!this.angemeldet) {
                    return "";
                }

                String username = URLDecoder.decode(split[1], StandardCharsets.UTF_8);

                System.out.println(ANSI_CYAN + username + ANSI_RESET + " amgemeldet");
            }
            case "msg" -> {
                if (!this.angemeldet) {
                    return "";
                }

                String username = URLDecoder.decode(split[1], StandardCharsets.UTF_8);
                String message = URLDecoder.decode(split[2], StandardCharsets.UTF_8);

                System.out.println(ANSI_CYAN + username + ANSI_RESET + ": " + message);
            }
            default -> {
            }
        }

        if (split[0].equals("suc") || split[0].equals("err")) {
            this.wait = false;
        }

        return "";
    }

    public boolean isAngemeldet() {
        return this.angemeldet;
    }

    public void waitForResponse() {
        this.wait = true;
        while (this.wait) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }
}
