import java.net.*;
import java.io.*;

public class Client {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {
            System.out.println("Sending request to server...");
            socket = new Socket("127.0.0.1", 7777);

            System.out.println("Connected to server");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true); // auto-flush

            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader started...");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equalsIgnoreCase("Exit")) {
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server: " + msg);
                }
            } catch (Exception e) {
                System.out.println("Connection closed.");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writer started...");
            try {
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    String content = br1.readLine();
                    out.println(content);
                    if (content.equalsIgnoreCase("Exit")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Connection closed.");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is Client. Starting client...");
        new Client();
    }
}
