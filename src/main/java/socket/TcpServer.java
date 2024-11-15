package socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TcpServer extends Thread {
    private static final Logger logger = LogManager.getLogManager().getLogger("TcpServer");
    private final int port;
    private final TcpStreamAction action;

    public TcpServer(int port, TcpStreamAction action) {
        this.port = port;
        this.action = action;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            TcpServer.logger.log(Level.INFO, "Server " + InetAddress.getLocalHost() + " listening on port " + this.port);

            while (this.isAlive()) {
                Socket socket = serverSocket.accept();

                TcpServer.logger.log(Level.INFO, "New client connected at " + socket.getInetAddress());

                new TcpStream(socket, this.action).start();
            }
        } catch (IOException e) {
            TcpServer.logger.log(Level.SEVERE, e.toString());
        }
    }
}
