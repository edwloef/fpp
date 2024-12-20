package chat;

import socket.TcpServer;
import socket.TcpServerThreadAction;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Properties;

public class ChatServerThreadAction extends TcpServerThreadAction<ChatServerState> {
    private final Session emailSession;
    private String email;

    public ChatServerThreadAction(TcpServer<ChatServerState> server) {
        super(server);

        Properties properties = new Properties();
        properties.put("mail.smtp.host", super.server.sharedState.smtpHost());
        properties.put("mail.smtp.port", 465);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", true);

        Authenticator authenticator = new Authenticator() {
            private final PasswordAuthentication passwordAuthentication = new PasswordAuthentication(ChatServerThreadAction.super.server.sharedState.serverMail(), server.sharedState.srvPassword());

            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return passwordAuthentication;
            }
        };

        this.emailSession = Session.getInstance(properties, authenticator);
    }

    @Override
    public String processInput(String input) throws IOException {
        System.out.println("> " + input);

        String[] split = input.split(" ");

        switch (split[0]) {
            case "reg" -> {
                if (split.length < 3) {
                    return "err reg";
                }

                String email = URLDecoder.decode(split[1], StandardCharsets.UTF_8);

                if (super.server.sharedState.usernames().containsKey(email)) {
                    return "err reg";
                }

                String password = this.sendRegistrationEmail(email);
                if (password == null) {
                    return "err reg";
                }

                String username = URLDecoder.decode(split[2], StandardCharsets.UTF_8);
                super.server.sharedState.usernames().put(email, username);
                password = URLEncoder.encode(password, StandardCharsets.UTF_8);
                super.server.sharedState.passwords().put(email, password);

                return "suc reg";
            }
            case "an" -> {
                if (split.length < 3) {
                    return "err an";
                }

                String email = URLDecoder.decode(split[1], StandardCharsets.UTF_8);

                if (super.server.sharedState.online().contains(email)) {
                    return "err an";
                }

                String password = URLDecoder.decode(split[2], StandardCharsets.UTF_8);

                String correctPassword = super.server.sharedState.passwords().get(email);

                if (!password.equals(correctPassword)) {
                    return "err an";
                }

                this.email = email;
                super.server.sharedState.online().add(this.email);

                String username = URLEncoder.encode(super.server.sharedState.usernames().get(email), StandardCharsets.UTF_8);
                super.server.broadcast("con " + username);

                return "suc an";
            }
            case "chpwd" -> {
                if (split.length < 3) {
                    return "err chpwd";
                }

                String password = URLDecoder.decode(split[1], StandardCharsets.UTF_8);
                String correctPassword = super.server.sharedState.passwords().get(this.email);

                if (this.email == null || !password.equals(correctPassword)) {
                    return "err chpwd";
                }

                String newPassword = URLDecoder.decode(split[2], StandardCharsets.UTF_8);
                super.server.sharedState.passwords().replace(this.email, newPassword);

                return "suc chpwd";
            }
            case "msg" -> {
                if (this.email == null || split.length == 1) {
                    return "";
                }

                String username = URLEncoder.encode(super.server.sharedState.usernames().get(this.email), StandardCharsets.UTF_8);

                super.server.broadcast("msg " + username + " " + split[1]);
            }
            case "con" -> {
                StringBuilder returns = new StringBuilder();

                for (String userEmail : super.server.sharedState.online()) {
                    String userUsername = URLEncoder.encode(super.server.sharedState.usernames().get(userEmail), StandardCharsets.UTF_8);

                    returns.append("con ").append(userUsername).append("\n");
                }

                return returns.toString();
            }
            default -> {
            }
        }

        return "";
    }

    @Override
    public void clientDisconnect() throws IOException {
        super.server.sharedState.online().remove(this.email);

        String username = URLEncoder.encode(super.server.sharedState.usernames().get(this.email), StandardCharsets.UTF_8);
        super.server.broadcast("dis " + username);
    }

    @Override
    public TcpServerThreadAction<ChatServerState> clone() {
        return new ChatServerThreadAction(super.server);
    }

    private String sendRegistrationEmail(String email) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        MimeMessage mimeMessage = new MimeMessage(this.emailSession);
        try {
            mimeMessage.setFrom(new InternetAddress(super.server.sharedState.serverMail()));
            mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            mimeMessage.setSubject("FPP Registrierung");
            mimeMessage.setSentDate(new Date());
            mimeMessage.setText("Passwort: " + password);

            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            return null;
        }

        return password.toString();
    }
}
