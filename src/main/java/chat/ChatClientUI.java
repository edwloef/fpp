package chat;

import javax.swing.*;
import java.awt.*;

public class ChatClientUI extends JFrame {
    private final JList<String> chat = new JList<>();
    private final JList<String> users = new JList<>();

    public ChatClientUI() {
        setTitle("Chat Client UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(newContentPane());
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ChatClientUI();
    }

    private JPanel newContentPane() {
        JScrollPane chatScrollable = new JScrollPane(this.chat);

        JScrollPane usersScrollable = new JScrollPane(this.users);

        JPanel sendButtonPanel = new JPanel();
        sendButtonPanel.setLayout(new BoxLayout(sendButtonPanel, BoxLayout.Y_AXIS));
        sendButtonPanel.add(new JPanel());
        sendButtonPanel.add(new JButton("➤"));

        JTextArea messageInput = new JTextArea();
        messageInput.setLineWrap(true);
        messageInput.setWrapStyleWord(true);

        JPanel userInputSplit = new JPanel();
        userInputSplit.setLayout(new BoxLayout(userInputSplit, BoxLayout.X_AXIS));
        userInputSplit.add(messageInput);
        userInputSplit.add(sendButtonPanel);

        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.add(chatScrollable);
        chatPanel.add(userInputSplit);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatPanel, usersScrollable);
        split.setDividerSize(2);

        JPanel panel = new JPanel(new GridLayout());
        panel.add(split);

        return panel;
    }
}
