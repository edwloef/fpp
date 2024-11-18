package socket;

import java.io.IOException;

public interface TcpStreamAction {
    String processInput(String input) throws IOException;
}
