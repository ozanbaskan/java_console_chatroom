import com.sun.tools.jconsole.JConsoleContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

public class MessageHandler implements Runnable{

    private Socket socket;
    private BufferedReader in;
    private String nick;

    public MessageHandler(Socket socket, String nick) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.nick = nick;
    }

    @Override
    public void run() {

        try {
                while (true){
                    String message = in.readLine();
                    if (message == null) break;
                    System.out.println("\r" + message);
                    System.out.print(nick + ": ");
                }

            } catch (IOException ignored) {

            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }
}
