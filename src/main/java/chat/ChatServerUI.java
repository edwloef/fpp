package chat;

import javax.swing.*;
import java.awt.*;

public class ChatServerUI extends JFrame {
    private final JList<String> serverLog = new JList<>();
    private final JList<String> users = new JList<>();

    public ChatServerUI() {
        setTitle("Chat Server UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(newContentPane());
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ChatServerUI();
    }

    private JPanel newContentPane() {
        JScrollPane serverLogScrollable = new JScrollPane(this.serverLog);
        JScrollPane usersScrollable = new JScrollPane(this.users);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, serverLogScrollable, usersScrollable);
        split.setDividerSize(2);

        JPanel panel = new JPanel(new GridLayout());
        panel.add(split);

        return panel;
    }
}
