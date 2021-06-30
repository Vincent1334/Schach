package chess.network;

import chess.gui.Logic;
import chess.model.Move;
import chess.model.Parser;
import java.net.*;
import java.io.*;

/**
 * This class contains the client logic
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-28
 */
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

    /**
     * Client constructor
     * @param ipAddress Server address
     * @param port Server port
     * @param gui gui for Thread
     */
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

    /**
     * Client loop
     */
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
                    System.out.println("Client: " + input);
                    if(input.equals("white")){
                        isBlack = false;
                        isConnected = true;
                        out.println("ready");
                        gui.computerOrNetworkIsFinish();
                        break;
                    }
                    if(input.equals("black")){
                        isBlack = true;
                        isConnected = true;
                        out.println("ready");
                        gui.computerOrNetworkIsFinish();
                    }
                }
            }catch (Exception x){
                x.printStackTrace();
            }
        }
    }

    /**
     * Get client thread
     * @return Thread
     */
    public Thread getThread(){
        return thread;
    }

    /**
     * get move from Server
     * @return move
     */
    public Move getMove(){
        return networkMove;
    }

    /**
     * Send move to Server
     * @param move Move
     */
    public void sendMove(Move move){
        out.println(move.toString());
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Returns Client team
     * @return isBlack
     */
    public boolean isBlack(){
        return isBlack;
    }

    /**
     * Stop client and all components
     */
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