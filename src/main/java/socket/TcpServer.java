package socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TcpServer<T> {
    private static final Logger logger = LogManager.getLogManager().getLogger(TcpServer.class.getName());
    private final int port;
    private final ArrayList<TcpStream> clients;
    public T sharedState;
    private TcpServerAction<T> action;

    public TcpServer(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
    }

    public void setAction(TcpServerAction<T> action) {
        this.action = action;
    }

    public void setSharedState(T sharedState) {
        this.sharedState = sharedState;
    }

    public void run() {
        if (this.action == null) {
            TcpServer.logger.log(Level.WARNING, "Tried to run server without setting action");
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            TcpServer.logger.log(Level.INFO, "Server " + InetAddress.getLocalHost() + " listening on port " + this.port);

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    TcpServer.logger.log(Level.INFO, "New client connected at " + socket.getInetAddress());

                    TcpStream client = new TcpStream(socket, this.action);
                    this.clients.add(client);
                    client.start();
                } catch (IOException e) {
                    TcpServer.logger.log(Level.SEVERE, e.toString(), e);
                }
            }
        } catch (IOException e) {
            TcpServer.logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void broadcast(String msg) throws IOException {
        ArrayList<TcpStream> clients = new ArrayList<>();

        for (TcpStream client : this.clients) {
            if (client.isAlive()) {
                clients.add(client);
            }
        }

        this.clients.clear();
        this.clients.addAll(clients);

        for (TcpStream client : this.clients) {
            client.notify(msg);
        }
    }
}
