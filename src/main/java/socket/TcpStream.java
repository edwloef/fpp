package socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TcpStream extends Thread {
    private static final Logger logger = LogManager.getLogManager().getLogger(TcpStream.class.getName());
    private final Socket socket;
    private final TcpStreamAction action;
    private final Scanner input;
    private final OutputStream output;

    public TcpStream(Socket socket, TcpStreamAction action) throws IOException {
        this.socket = socket;
        this.action = action;

        this.input = new Scanner(this.socket.getInputStream());
        this.output = this.socket.getOutputStream();
    }

    @Override
    public void run() {
        while (this.isAlive()) {
            try {
                this.notify(this.action.processInput(this.input.nextLine()));
            } catch (IOException e) {
                TcpStream.logger.log(Level.SEVERE, e.toString(), e);
            } catch (NoSuchElementException e) {
                try {
                    if (this.action instanceof TcpServerAction<?> serverAction) {
                        serverAction.clientDisconnect();
                    }

                    this.socket.close();

                    break;
                } catch (IOException e1) {
                    TcpStream.logger.log(Level.SEVERE, e1.toString(), e1);
                }
            }
        }
    }

    public void notify(String msg) throws IOException {
        this.output.write((msg + "\n").getBytes(StandardCharsets.UTF_8));
    }
}
