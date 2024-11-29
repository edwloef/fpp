package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TcpServer<T> {
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
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    TcpStream client = new TcpStream(socket, this.action);
                    this.clients.add(client);
                    client.start();
                } catch (IOException e) {
                }
            }
        } catch (IOException e) {
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
