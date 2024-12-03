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

    public static void main(String[] args) {
        ChatClientThreadAction action = new ChatClientThreadAction();

        TcpClient client = new TcpClient("localhost", 9876, action);
        try {
            SocketHandlerThread stream = client.setup();

            System.out.println("starting client...");

            stream.start();

            while (true) {
                System.out.println("┌-------------------┐");
                System.out.println("| Bitte geben Sie 1 |");
                System.out.println("| oder 2 ein:       |");
                System.out.println("├-------------------┤");
                System.out.println("| 1: Registrierung  |");
                System.out.println("| 2: Anmeldung      |");
                System.out.println("└-------------------┘");

                int in;
                while (true) {
                    try {
                        in = ChatClient.sc.nextInt();

                        if (in == 1 || in == 2) {
                            break;
                        }
                    } catch (InputMismatchException e) {
                    }

                    System.out.println("falsche Eingabe!");
                }

                if (in == 1) {
                    stream.notify(ChatClient.registrieren());
                } else {
                    stream.notify(ChatClient.anmelden());
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String registrieren() {
        System.out.print("Bitte geben Sie Ihre E-Mail-Adresse ein: ");
        String email = URLEncoder.encode(ChatClient.sc.next(), StandardCharsets.UTF_8);
        System.out.print("Bitte geben Sie Ihren gewünschten Benutzernamen ein: ");
        String username = URLEncoder.encode(ChatClient.sc.next(), StandardCharsets.UTF_8);
        return "reg " + email + " " + username;
    }

    private static String anmelden() {
        return "";
    }
}
