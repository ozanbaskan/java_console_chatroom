import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static final int PORT = 3000;

    private static final ArrayList<ClientHandler> clients = new ArrayList<>();
    private static final ExecutorService pool = Executors.newFixedThreadPool(50);

    //private static final CopyOnWriteArrayList<String> nicks = new CopyOnWriteArrayList<>();

    private static List<String> nicks = Collections.synchronizedList(new ArrayList<String>());

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("==================================================");
        System.out.println("=                                                =");
        System.out.println("=                                                =");
        System.out.println("=   JAVA Konsol Chat Uygulamasına Hoş Geldiniz   =");
        System.out.println("=                                                =");
        System.out.println("=                                                =");
        System.out.println("==================================================");


        while (true)
        {
            Socket socket = serverSocket.accept();
            System.out.println("Bir kullanıcı bağlandı!");
            ClientHandler clientThread = new ClientHandler(socket, clients);
            clients.add(clientThread);
            pool.execute(clientThread);
            if (nicks.size() > 0)
                System.out.println("Sohbetteki Diğer Kullanıcılar: " + nicks);
        }

    }

    public synchronized static List<String> getNicks() {
        return nicks;
    }

    public synchronized static void addNick(String nick){
        nicks.add(nick);
    }

    public synchronized static void removeNick(String nick){
        nicks.remove(nick);
    }


}
