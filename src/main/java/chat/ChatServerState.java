package chat;

import java.util.HashMap;
import java.util.HashSet;

public record ChatServerState(HashMap<String, String> usernames, HashMap<String, String> passwords,
                              HashSet<String> online, String srvPassword, String smtpHost, String serverMail) {
}
