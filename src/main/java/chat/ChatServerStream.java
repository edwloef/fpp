package chat;

import socket.TcpServer;
import socket.TcpServerAction;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ChatServerStream extends TcpServerAction<ChatServerState> {
    private static final Logger logger = LogManager.getLogManager().getLogger(ChatServerStream.class.getName());
    private String email;

    public ChatServerStream(TcpServer<ChatServerState> server) {
        super(server);

        if (super.server.sharedState == null) {
            ChatServerStream.logger.log(Level.WARNING, "chat server initialized with empty shared state");
        }
    }

    private static String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }

    @Override
    public String processInput(String msg) throws IOException {
        ChatServerStream.logger.log(Level.INFO, msg);

        String[] split = msg.split(" ");

        switch (split[0]) {
            case "reg" -> {
                String email = split[1];
                String username = split[2];

                if (super.server.sharedState.usernames().containsKey(email)) {
                    return "err reg";
                }

                String password = this.sendRegistrationEmail(email, super.server.sharedState);
                if (password == null) {
                    return "err reg";
                }

                password = URLEncoder.encode(password, StandardCharsets.UTF_8);
                super.server.sharedState.usernames().put(email, username);
                super.server.sharedState.passwords().put(email, password);

                return "suc reg";
            }
            case "an" -> {
                String email = split[1];
                String password = split[2];
                String username = super.server.sharedState.usernames().get(email);

                String correctPassword = super.server.sharedState.passwords().get(email);

                if (!password.equals(correctPassword)) {
                    return "err an";
                }

                super.server.broadcast("con " + username);

                StringBuilder returns = new StringBuilder();

                for (String userEmail : super.server.sharedState.passwords().keySet()) {
                    String userUsername = super.server.sharedState.usernames().get(userEmail);

                    returns.append("con ").append(userUsername).append('\n');
                }

                this.email = email;

                return returns + "suc an";
            }
            case "chpwd" -> {
                String password = split[1];
                String newPassword = split[2];

                String correctPassword = super.server.sharedState.passwords().get(this.email);

                if (this.email == null || !password.equals(correctPassword)) {
                    return "err chpwd";
                }

                super.server.sharedState.passwords().replace(this.email, newPassword);

                return "suc chpwd";
            }
            case "msg" -> {
                String username = super.server.sharedState.usernames().get(this.email);

                super.server.broadcast("msg " + username + " " + split[1]);
            }
            case "err" -> ChatServerStream.logger.log(Level.WARNING, msg);
            default -> ChatServerStream.logger.log(Level.WARNING, "unknown message received: \"" + msg + "\"");
        }

        return "";
    }

    @Override
    public void clientDisconnect() throws IOException {
        String username = super.server.sharedState.usernames().get(this.email);

        super.server.broadcast("dis " + username);
    }

    private String sendRegistrationEmail(String email, ChatServerState sharedState) {
        String password = ChatServerStream.generateRandomPassword();

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.uni-jena.de");
        properties.put("mail.smtp.port", 465);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.auth", true);

        Authenticator authenticator = new Authenticator() {
            private final PasswordAuthentication passwordAuthentication = new PasswordAuthentication(sharedState.srvUsername(), sharedState.srvPassword());

            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return passwordAuthentication;
            }
        };

        Session session = Session.getInstance(properties, authenticator);

        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(sharedState.srvEmail()));
            mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            mimeMessage.setSubject("FPP Registrierung");
            mimeMessage.setSentDate(new Date());
            mimeMessage.setText("Passwort: " + password);

            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            ChatServerStream.logger.log(Level.WARNING, e.toString(), e);
            return null;
        }

        return password;
    }
}