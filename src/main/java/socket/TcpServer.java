package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TcpServer<T> {
    private final int port;
    private final ArrayList<SocketHandlerThread> clients;
    public T sharedState;
    private TcpServerThreadAction<T> action;

    public TcpServer(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
    }

    public void setAction(TcpServerThreadAction<T> action) {
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
                    SocketHandlerThread client = new SocketHandlerThread(socket, this.action.clone());
                    client.start();
                    synchronized (this.clients) {
                        this.clients.add(client);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void broadcast(String msg) throws IOException {
        ArrayList<SocketHandlerThread> clients = new ArrayList<>();

        synchronized (this.clients) {
            for (SocketHandlerThread client : this.clients) {
                if (!client.isClosed()) {
                    clients.add(client);
                }
            }

            this.clients.clear();
            this.clients.addAll(clients);

            for (SocketHandlerThread client : this.clients) {
                client.notify(msg);
            }
        }

    }
}
