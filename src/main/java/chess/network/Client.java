package chess.network;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection() {
        System.out.println("start");
        try {
            clientSocket = new Socket("127.0.0.1", 5555);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String sendMessage(String msg) {
        System.out.println("Sending");
        try {
            out.println(msg);
            String resp = in.readLine();
            return resp;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "error found";
    }

    public void stopConnection() {
        System.out.println("stop");
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String args[]) {
        System.out.println("AWAKE Client");
        Client client = new Client();
        client.startConnection();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Send a message!");
            String text = client.sendMessage(scanner.nextLine());
            System.out.println("Answer of Server: " + text);
            if (text.equals("c ya later")) {
                break;
            }
        }
    }
}