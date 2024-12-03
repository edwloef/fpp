package chat;

import socket.SocketHandlerThread;
import socket.TcpClient;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ChatClient {
    private static final Scanner sc = new Scanner(System.in);

    private static final String ANSI_CLEAR = "\033[H\033[J"; // move cursor to top left corner of screen, clear screen from cursor to end of screen

    public static void main(String[] args) {
        ChatClientThreadAction action = new ChatClientThreadAction();

        TcpClient client = new TcpClient("localhost", 9876, action);
        try {
            SocketHandlerThread thread = client.setup();

            System.out.println("starting client...");

            thread.start();

            while (!action.isAngemeldet()) {
                System.out.println("┌------------------┐");
                System.out.println("| Registrieren Sie |");
                System.out.println("| sich mit /reg    |");
                System.out.println("├------------------┤");
                System.out.println("| Melden Sie sich  |");
                System.out.println("| an mit /an       |");
                System.out.println("└------------------┘");

                while (true) {
                    try {
                        String in = ChatClient.sc.nextLine();

                        if (in.equals("/reg")) {
                            thread.notify(ChatClient.registrieren());
                            System.out.print(ChatClient.ANSI_CLEAR);
                        } else if (in.equals("/an")) {
                            thread.notify(ChatClient.anmelden());
                            System.out.print(ChatClient.ANSI_CLEAR);
                        } else {
                            continue;
                        }

                        break;
                    } catch (InputMismatchException e) {
                    }

                    System.out.println("falsche Eingabe!");
                }

                action.wait = true;

                while (action.wait) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }

            System.out.println("┌--------------------┐");
            System.out.println("| Versenden Sie eine |");
            System.out.println("| Nachricht mit /msg |");
            System.out.println("├--------------------┤");
            System.out.println("| Ändern Sie Ihr     |");
            System.out.println("| Passwort mit /pwd  |");
            System.out.println("├--------------------┤");
            System.out.println("| Melden Sie sich ab |");
            System.out.println("| mit /ab            |");
            System.out.println("└--------------------┘");

            while (true) {
                String in = ChatClient.sc.nextLine();

                if (in.startsWith("/msg ")) {
                    thread.notify(ChatClient.nachricht(in.substring(5)));
                } else if (in.equals("/pwd")) {
                    action.wait = true;
                    thread.notify(ChatClient.passwortÄndern());
                    while (action.wait) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                } else if (in.equals("/ab")) {
                    thread.close();
                    return;
                } else {
                    System.out.println("falsche Eingabe!");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String registrieren() {
        System.out.print("Bitte geben Sie Ihre E-Mail-Adresse ein: ");
        String email = URLEncoder.encode(ChatClient.sc.nextLine(), StandardCharsets.UTF_8);
        System.out.print("Bitte geben Sie Ihren gewünschten Benutzernamen ein: ");
        String username = URLEncoder.encode(ChatClient.sc.nextLine(), StandardCharsets.UTF_8);

        return "reg " + email + " " + username;
    }

    private static String anmelden() {
        System.out.print("Bitte geben Sie Ihre E-Mail-Adresse ein: ");
        String email = URLEncoder.encode(ChatClient.sc.nextLine(), StandardCharsets.UTF_8);
        System.out.print("Bitte geben Sie Ihr Passwort ein: ");
        String password = URLEncoder.encode(ChatClient.sc.nextLine(), StandardCharsets.UTF_8);

        return "an " + email + " " + password;
    }

    private static String passwortÄndern() {
        System.out.print("Bitte geben Sie Ihr altes Passwort ein: ");
        String password = URLEncoder.encode(ChatClient.sc.nextLine(), StandardCharsets.UTF_8);
        System.out.print("Bitte geben Sie Ihr gewünschtes neues Passwort ein: ");
        String newPassword = URLEncoder.encode(ChatClient.sc.nextLine(), StandardCharsets.UTF_8);

        return "chpwd " + password + " " + newPassword;
    }

    private static String nachricht(String msg) {
        return "msg " + URLEncoder.encode(msg, StandardCharsets.UTF_8);
    }
}
    
