package socket;

import java.io.IOException;

public interface TcpThreadAction {
    String processInput(String input) throws IOException;
}
