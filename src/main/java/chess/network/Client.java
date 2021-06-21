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

    private boolean isConnected;

    public Client(String ipAddress, int port, Logic gui){
        try {
            this.gui = gui;
            clientSocket = new Socket(ipAddress, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        while(true){
            try{
                if(!isConnected){
                    out.println("start");
                    Thread.sleep(500);
                    String input = in.readLine();
                    if(input != null){
                        if(input.equals("white")){
                            isBlack = false;
                            isConnected = true;
                            break;
                        }
                        else{
                            isBlack = true;
                            isConnected = true;
                            break;
                        }
                    }
                }else{
                    String input = in.readLine();
                    if(input != null){
                        networkMove = Parser.parse(input);
                        gui.computerOrNetworkIsFinish();
                        break;
                    }

                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
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
}