import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ObjectOutputStream usersOut;

    private ArrayList<ClientHandler> clients;

    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients) throws IOException {
        this.socket = clientSocket;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        usersOut = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        String nick = null;
        try {
            usersOut.writeObject(Server.getNicks());
            nick = in.readLine();
            Server.addNick(nick);
            sendMessage(">>> " + nick + " sohbete katıldı!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (true)
            {
                String message = in.readLine();
                if (message == null) break;
                sendMessage(message);
            }
        } catch (IOException ignored){

        } finally {

            Server.removeNick(nick);
            sendMessage(">>> " + nick + " sohbetten ayrıldı!");

            System.out.println(nick + " kullanısı ayrıldı.");
            System.out.println("Kullanıcılar: " + Server.getNicks());
            System.out.println("==================================================");
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(String message)
    {
        for (ClientHandler client : clients)
        {
            if (client.equals(this)) continue;
            client.out.println(message);
        }
    }

}
