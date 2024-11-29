package socket;

import java.io.IOException;
import java.net.Socket;

public class TcpClient {
    private final String hostname;
    private final int port;
    private final TcpThreadAction action;

    public TcpClient(String hostname, int port, TcpThreadAction action) {
        this.hostname = hostname;
        this.port = port;
        this.action = action;
    }

    public SocketHandlerThread setup() throws IOException {
        Socket socket = new Socket(this.hostname, this.port);
        return new SocketHandlerThread(socket, this.action);
    }
}
