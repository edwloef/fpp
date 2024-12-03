package chat;

import socket.TcpThreadAction;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClientThreadAction implements TcpThreadAction {
    private static final String ANSI_CYAN = "\033[36m"; // blue output color
    private static final String ANSI_RESET = "\033[0m"; // reset output color
    public boolean wait = true;
    private boolean angemeldet = false;

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

        switch (split[0]) {
            case "suc", "err" -> this.wait = false;
            default -> {
            }
        }

        return "";
    }

    public boolean isAngemeldet() {
        return this.angemeldet;
    }

    public String registrieren(Scanner sc) {
        System.out.print("Bitte geben Sie Ihre E-Mail-Adresse ein: ");
        String email = URLEncoder.encode(sc.nextLine(), StandardCharsets.UTF_8);
        System.out.print("Bitte geben Sie Ihren gewünschten Benutzernamen ein: ");
        String username = URLEncoder.encode(sc.nextLine(), StandardCharsets.UTF_8);

        return "reg " + email + " " + username;
    }

    public String anmelden(Scanner sc) {
        System.out.print("Bitte geben Sie Ihre E-Mail-Adresse ein: ");
        String email = URLEncoder.encode(sc.nextLine(), StandardCharsets.UTF_8);
        System.out.print("Bitte geben Sie Ihr Passwort ein: ");
        String password = URLEncoder.encode(sc.nextLine(), StandardCharsets.UTF_8);

        return "an " + email + " " + password;
    }

    public String passwortÄndern(Scanner sc) {
        System.out.print("Bitte geben Sie Ihr altes Passwort ein: ");
        String password = URLEncoder.encode(sc.nextLine(), StandardCharsets.UTF_8);
        System.out.print("Bitte geben Sie Ihr gewünschtes neues Passwort ein: ");
        String newPassword = URLEncoder.encode(sc.nextLine(), StandardCharsets.UTF_8);

        return "chpwd " + password + " " + newPassword;
    }

    public String nachricht(String msg) {
        return "msg " + URLEncoder.encode(msg, StandardCharsets.UTF_8);
    }
}
