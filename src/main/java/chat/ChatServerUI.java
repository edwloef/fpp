package chat;


import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class ChatServerUI extends JFrame {
    private final JList<String> serverLog = new JList<>();
    private final JList<String> users = new JList<>();
    private final ImageIcon icon = new ImageIcon("ChatImage.jpg");

    public ChatServerUI() {
        setTitle("Chat Server UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setIconImage(icon.getImage());
        
        

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

        Button startButton = new Button("Server Start!");
        startButton.setMinimumSize(new Dimension(startButton.getMaximumSize().width, 20));
        startButton.setMaximumSize( new Dimension(startButton.getMaximumSize().width, startButton.getMaximumSize().height/40));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(usersScrollable);
        rightPanel.add(buttonPanel);
        
        rightPanel.setMinimumSize(new Dimension(100, 100));
        serverLogScrollable.setMinimumSize(new Dimension(100, 100)); 

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, serverLogScrollable, rightPanel);
        split.setDividerSize(2);

        JPanel panel = new JPanel(new GridLayout());
        panel.add(split);
        return panel;
    }
}
