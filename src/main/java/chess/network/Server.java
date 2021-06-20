package chess.network;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

    private static int port = 5555;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private static Scanner scanner;

    //On start
    public static void main(String args[]) {
        scanner = new Scanner(System.in);
        System.out.println("AWAKE Server");
        //create this server
        Server server = new Server();
        //start this server
        server.start(port);

    }

    public void start(int port) {
        System.out.println("Start server");
        try {
            //create server socket on given port
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String hello;
            while ((hello = in.readLine()) != null) {

                if ("end".equals(hello)) {
                    out.println("c ya later");
                    break;
                } else {
                    System.out.println("Got a message!: " + hello + " Please Answer!");
                    String answer = scanner.nextLine();
                    out.println(answer);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void stop() {
        System.out.println("stop");
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}