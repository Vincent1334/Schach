package chess.network;

import chess.gui.Logic;
import chess.model.Move;
import chess.model.Parser;
import java.net.*;
import java.io.*;
import java.util.*;

public class Client implements Runnable{
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread thread;
    private boolean isBlack;
    private Logic gui;
    private Move networkMove;

    private String ipAddress;
    private int port;

    private boolean isConnected;

    public Client(String ipAddress, int port, Logic gui){
        try {
            this.ipAddress = ipAddress;
            this.port = port;
            this.gui = gui;
            thread = new Thread(this);
            thread.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        if(!isConnected){
            try{
                clientSocket = new Socket(ipAddress, port);
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out.println("start");
            }catch (Exception x){
                System.out.println("Cant connect");
            }
        }
        while(true){
            try{
                String input = in.readLine();
                if(input != null){
                    if(isConnected){
                        networkMove = Parser.parse(input);
                        gui.computerOrNetworkIsFinish();
                        break;
                    }
                    if(input.equals("white")){
                        isBlack = false;
                        isConnected = true;
                        break;
                    }
                    if(input.equals("black")){
                        isBlack = true;
                        isConnected = true;
                    }
                }
            }catch (Exception x){
            }
        }
    }

    public Thread getThread(){
        return thread;
    }

    public Move getMove(){
        return networkMove;
    }

    public void sendMove(Move move){
        out.println(move.toString());
        thread = new Thread(this);
        thread.start();
    }

    public void sendMessage(String message){
        out.println(message);
        thread = new Thread(this);
        thread.start();
    }

    public boolean isBlack(){
        return isBlack;
    }

    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}