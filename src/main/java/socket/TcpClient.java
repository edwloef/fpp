package socket;

import java.io.IOException;
import java.net.Socket;

public class TcpClient {
    private final String hostname;
    private final int port;
    private final TcpStreamAction action;

    public TcpClient(String hostname, int port, TcpStreamAction action) {
        this.hostname = hostname;
        this.port = port;
        this.action = action;
    }

    public TcpStream setup() throws IOException {
        Socket socket = new Socket(this.hostname, this.port);
        return new TcpStream(socket, this.action);
    }
}
