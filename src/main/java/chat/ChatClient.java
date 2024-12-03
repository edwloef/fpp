package chat;

import socket.SocketHandlerThread;
import socket.TcpClient;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ChatClient implements Runnable {
    private static final String ANSI_CLEAR = "\033[H\033[J"; // move cursor to top left corner of screen, clear screen from cursor to end of screen

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.run();
    }

    @Override
    public void run() {
        try {
            ChatClientThreadAction action = new ChatClientThreadAction();
            TcpClient client = new TcpClient("localhost", 9876, action);
            SocketHandlerThread thread = client.setup();

            System.out.println("starting client...");
            thread.start();

            Scanner sc = new Scanner(System.in);

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
                        String in = sc.nextLine().strip();

                        if (in.equals("/reg")) {
                            thread.notify(action.registrieren(sc));
                            System.out.print(ChatClient.ANSI_CLEAR);
                        } else if (in.equals("/an")) {
                            thread.notify(action.anmelden(sc));
                            System.out.print(ChatClient.ANSI_CLEAR);
                        } else {
                            System.out.println("falsche Eingabe!");
                            continue;
                        }

                        break;
                    } catch (InputMismatchException e) {
                    }

                    System.out.println("falsche Eingabe!");
                }

                action.waitForResponse();
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
                String in = sc.nextLine().strip();

                if (in.startsWith("/msg ")) {
                    thread.notify(action.nachricht(in.substring(5)));
                } else if (in.equals("/pwd")) {
                    thread.notify(action.passwortÄndern(sc));
                    action.waitForResponse();
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
}
    
