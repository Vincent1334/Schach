package chess.network;

import chess.gui.Logic;
import chess.model.Move;
import chess.model.Parser;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server implements Runnable{
    
    private boolean isBlack, isReadyToPlay;

    private Thread thread;
    private Logic gui;
    
    private Move networkMove;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private int port;
    private boolean isConnected;

    public Server(int port, boolean isBlack, Logic gui){
        try{       
            this.isBlack = isBlack;
            this.gui = gui;
            this.port = port;
            thread = new Thread(this);
            thread.start();
        }catch(Exception x){
            x.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            if(!isConnected){
                serverSocket = new ServerSocket(port);
                clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                isConnected = true;
            }
        }catch(Exception x){
        }
        System.out.println("Connected");
        //Listener for Server input
        while(true){
            try{
                String input = in.readLine();
                if(input != null){
                    //check if new client is connected
                    if(input.equals("start")){                        
                        //send Team color
                        out.println(isBlack ? "black" : "white");
                        continue;
                    }
                    if(input.equals("ready")){
                        isReadyToPlay = true;
                        //leave listener if server starts with white
                        if(isBlack) break;
                    }
                    //get Move message
                    networkMove = Parser.parse(input);
                    gui.computerOrNetworkIsFinish();
                    break;
                }

            }catch(Exception x){
                x.printStackTrace();
            }
        }
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
    
    public boolean isReadyToPlay(){
        return isReadyToPlay;
    }
}