package socket;

import java.io.*;
import java.net.Socket;

public class SocketHandlerThread extends Thread {
    private final Socket socket;
    private final TcpThreadAction action;
    private final BufferedReader input;
    private final BufferedWriter output;

    public SocketHandlerThread(Socket socket, TcpThreadAction action) throws IOException {
        this.socket = socket;
        this.action = action;

        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.output = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
    }

    @Override
    public void run() {
        try {
            String msg;

            while ((msg = this.input.readLine()) != null) {
                this.notify(this.action.processInput(msg));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("closing connection...");

        try {
            if (this.action instanceof TcpServerThreadAction<?> serverAction) {
                serverAction.clientDisconnect();
            }

            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isClosed() {
        return this.socket.isClosed();
    }

    public void notify(String msg) throws IOException {
        this.output.write(msg);
        this.output.newLine();
        this.output.flush();
    }
}