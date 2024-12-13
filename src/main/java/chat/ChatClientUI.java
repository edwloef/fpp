package chat;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ChatClientUI extends JFrame {
    private final JList<String> chat = new JList<>();
    private final JList<String> users = new JList<>();
    private final JTextArea messageInput = new JTextArea();

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

        this.messageInput.setLineWrap(true);
        this.messageInput.setWrapStyleWord(true);
        this.messageInput.setMaximumSize(new Dimension(this.messageInput.getMaximumSize().width, this.messageInput.getFontMetrics(this.messageInput.getFont()).getHeight()));
        this.messageInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                adjustMessageInputHeight();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                adjustMessageInputHeight();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                adjustMessageInputHeight();
            }
        });

        JPanel userInputSplit = new JPanel();
        userInputSplit.setLayout(new BoxLayout(userInputSplit, BoxLayout.X_AXIS));
        userInputSplit.add(messageInput);
        userInputSplit.add(sendButtonPanel);
        // TODO: this isn't perfect
        userInputSplit.setMaximumSize(new Dimension( userInputSplit.getMaximumSize().width, this.messageInput.getMaximumSize().height));

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

    private void adjustMessageInputHeight() {
        int width = this.messageInput.getWidth();
        if (width == 0) {
            return;
        }

        FontMetrics fm = this.messageInput.getFontMetrics(this.messageInput.getFont());
        int lineHeight = fm.getHeight();

        String text = this.messageInput.getText();
        int lines = text.lines().mapToInt(line -> (fm.stringWidth(line) / width) + 1).sum();

        if (text.endsWith("\n") || text.isEmpty()) {
            lines += 1;
        }

        this.messageInput.setMaximumSize(new Dimension(this.messageInput.getWidth(), lineHeight * lines));
        this.messageInput.revalidate();
    }
}
