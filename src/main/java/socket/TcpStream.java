package socket;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TcpStream extends Thread {
    private static final Logger logger = LogManager.getLogManager().getLogger(TcpStream.class.getName());
    private final Socket socket;
    private final TcpStreamAction action;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;

    public TcpStream(Socket socket, TcpStreamAction action) throws IOException {
        this.socket = socket;
        this.action = action;

        this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
    }

    @Override
    public void run() {
        while (this.isAlive()) {
            try {
                this.notify(this.action.processInput(this.bufferedReader.readLine()));
            } catch (IOException e) {
                TcpStream.logger.log(Level.SEVERE, e.toString(), e);
            }
        }

        try {
            this.socket.close();
        } catch (IOException e) {
            TcpStream.logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void notify(String msg) throws IOException {
        this.bufferedWriter.write(msg);
        this.bufferedWriter.flush();
    }
}
