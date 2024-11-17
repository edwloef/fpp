package socket;

public abstract class TcpServerAction implements TcpStreamAction {
    protected final TcpServer server;

    protected TcpServerAction(TcpServer server) {
        this.server = server;
    }
}
