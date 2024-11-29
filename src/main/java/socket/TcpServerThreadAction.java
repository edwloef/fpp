package socket;

import java.io.IOException;

public abstract class TcpServerThreadAction<T> implements TcpThreadAction {
    protected final TcpServer<T> server;

    protected TcpServerThreadAction(TcpServer<T> server) {
        this.server = server;
    }

    public abstract void clientDisconnect() throws IOException;

    public abstract TcpServerThreadAction<T> clone();
}
