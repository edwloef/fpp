package socket;

import java.io.IOException;

public abstract class TcpServerAction<T> implements TcpStreamAction {
    protected final TcpServer<T> server;

    protected TcpServerAction(TcpServer<T> server) {
        this.server = server;
    }

    public abstract void clientDisconnect() throws IOException;
}
