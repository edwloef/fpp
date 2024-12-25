package chat;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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

        JPanel sendButtonPanel = new JPanel();
        sendButtonPanel.setLayout(new BoxLayout(sendButtonPanel, BoxLayout.Y_AXIS));
        sendButtonPanel.add(new JPanel());
        sendButtonPanel.add(new JButton("➤"));

        this.messageInput.setLineWrap(true);
        this.messageInput.setWrapStyleWord(true);
        this.messageInput.setMaximumSize(new Dimension(this.messageInput.getMaximumSize().width,
                this.messageInput.getFontMetrics(this.messageInput.getFont()).getHeight()));
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

        JPanel userInputPanel = new JPanel();
        userInputPanel.setLayout(new BoxLayout(userInputPanel, BoxLayout.X_AXIS));
        userInputPanel.add(messageInput);
        userInputPanel.add(sendButtonPanel);
        // TODO: this isn't perfect
        userInputPanel.setMaximumSize(
                new Dimension(userInputPanel.getMaximumSize().width, this.messageInput.getMaximumSize().height));

        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setMaximumSize(new Dimension(chatPanel.getMaximumSize().width, chatPanel.getMaximumSize().height));
        chatPanel.add(chatScrollable);
        chatPanel.add(userInputPanel);

        // Right side;

        JScrollPane usersScrollable = new JScrollPane(this.users);

        Button anButton = new Button("Anmelden");
        this.setButtonDimension(anButton);
        Button regButton = new Button("Registrieren");
        this.setButtonDimension(regButton);
        Button chpewButton = new Button("Change Pasword");
        this.setButtonDimension(chpewButton);

        JPanel buttomPanel = new JPanel();
        buttomPanel.setLayout(new BoxLayout(buttomPanel, BoxLayout.Y_AXIS));
        if (/** PolicyQualifierInfo.status == true || */
        false) {
            buttomPanel.add(anButton);
            buttomPanel.add(regButton);
        } else {
            buttomPanel.add(chpewButton);
        }

        JPanel logPanel = new JPanel();
        logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.Y_AXIS));
        logPanel.add(usersScrollable);
        logPanel.add(buttomPanel);

        chatPanel.setMinimumSize(new Dimension(100, 100));
        logPanel.setMinimumSize(new Dimension(100, 100));
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatPanel, logPanel);
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

    private void setButtonDimension(Button button) {
        button.setMinimumSize(new Dimension(button.getMaximumSize().width, 20));
        button.setMaximumSize(new Dimension(button.getMaximumSize().width, button.getMaximumSize().height / 40));
    }
}
