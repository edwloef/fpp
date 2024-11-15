package socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TcpClient extends Thread {
    private static final Logger logger = LogManager.getLogManager().getLogger("TcpClient");
    private final String hostname;
    private final int port;
    private final TcpStreamAction action;

    public TcpClient(String hostname, int port, TcpStreamAction action) {
        this.hostname = hostname;
        this.port = port;
        this.action = action;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(this.hostname, this.port)) {
            TcpClient.logger.log(Level.INFO, "Client " + InetAddress.getLocalHost() + " connected to " + this.hostname + " on port " + this.port);

            new TcpStream(socket, this.action).start();
        } catch (IOException e) {
            TcpClient.logger.log(Level.SEVERE, e.toString());
        }
    }
}
