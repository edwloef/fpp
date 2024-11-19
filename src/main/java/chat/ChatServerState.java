package chat;

import java.util.HashMap;

public record ChatServerState(HashMap<String, String> usernames, HashMap<String, String> passwords, String srvEmail,
                              String srvUsername, String srvPassword) {
}
