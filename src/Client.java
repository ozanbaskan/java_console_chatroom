import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private static final String SERVER_IP = "127.0.0.1";

    public static void  main(String[] args) throws IOException {

        Socket socket = new Socket(SERVER_IP, Server.PORT);

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ObjectInputStream usersIn = new ObjectInputStream(socket.getInputStream());

        System.out.println("          ==================================================");
        System.out.println("          =                                                =");
        System.out.println("          =                                                =");
        System.out.println("          =       JAVA Konsol Chat Odasına Hoş Geldin      =");
        System.out.println("          =                                                =");
        System.out.println("          =                                                =");
        System.out.println("          ==================================================");
        System.out.println();


        List<String> users = null;
        try {
            users = (List<String>) usersIn.readObject();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }


        int i = 1;
        if (users.size() == 0) System.out.println("Şu an sohbette kimse yok. :(");
        else
        {
            System.out.println("Şu an sohbet kanalında " + users.size() + " kişi bulunmaktadır:");
            for (String nick : users)
            {
                System.out.println(i++ + " - " + nick);
            }
        }

        System.out.println("==========================================");

        String nick = getNick();
        while (users.contains(nick))
        {
            System.out.print("Bu kullanıcı bulunuyor.");
            nick = getNick();
        }
        out.println(nick);


        MessageHandler messageHandler = new MessageHandler(socket, nick);
        Thread thread = new Thread(messageHandler);
        thread.start();

        while (true)
        {
            System.out.print(nick + ": ");
            String typed = keyboard.readLine();
            while (typed.isBlank())
            {
                typed = keyboard.readLine();
            }

            if (typed.toLowerCase().equals("quit")) break;

            out.println(nick + ": " + typed);

        }

        out.close();
        keyboard.close();
        socket.close();

    }

    private static String getNick() throws IOException {

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Sohbete bağlanmak için bir rumuz giriniz: ");
        String nick = keyboard.readLine();

        while (nick == null || nick.length() == 0 || nick.length() > 16)
        {
            System.out.println("Rumuz boş veya 16 karakterden uzun olamaz.");
            System.out.print("Sohbete bağlanmak için bir rumuz giriniz: ");
            nick = keyboard.readLine();
        }

        return nick.trim();
    }

}
