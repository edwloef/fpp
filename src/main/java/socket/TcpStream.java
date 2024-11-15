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

    public TcpStream(Socket socket, TcpStreamAction action) {
        this.socket = socket;
        this.action = action;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

            while (this.isAlive()) {
                bufferedWriter.write(this.action.processInput(bufferedReader.readLine()));
                bufferedWriter.flush();
            }

            this.socket.close();
        } catch (IOException e) {
            TcpStream.logger.log(Level.SEVERE, e.toString(), e);
        }
    }
}
