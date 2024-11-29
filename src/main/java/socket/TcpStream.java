package socket;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class TcpStream extends Thread {
    private final Socket socket;
    private final TcpStreamAction action;
    private final BufferedReader input;
    private final BufferedWriter output;

    public TcpStream(Socket socket, TcpStreamAction action) throws IOException {
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
                System.out.println(msg);

                this.notify(this.action.processInput(msg));
            }
        } catch (SocketException e) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("closing connection...");

        try {
            if (this.action instanceof TcpServerAction<?> serverAction) {
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
