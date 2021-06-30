package chess.network;

import chess.gui.Logic;
import chess.model.Move;
import chess.model.Parser;
import java.net.*;
import java.io.*;

/**
 * This class contains the server logic
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-28
 */
public class Server implements Runnable{
    
    private boolean isBlack;

    private Thread thread;
    private Logic gui;
    
    private Move networkMove;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private int port;
    private boolean isConnected;

    /**
     * Server constructor
     * @param port server port
     * @param isBlack server team
     * @param gui gui for thread
     */
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

    /**
     * Server loop
     */
    @Override
    public void run() {
        try{
            if(!isConnected){
                serverSocket = new ServerSocket(port);
                clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            }
            while(true){
                String input = in.readLine();
                if(input != null){
                    System.out.println(input);
                    //check if new client is connected
                    if(!isConnected){
                        if(input.equals("start")){
                            //send Team color
                            out.println(isBlack ? "white" : "black");
                            System.out.println("Server: " + (isBlack ? "black" : "white"));
                            continue;
                        }
                        if(input.equals("ready")){
                            //leave listener if server starts with white
                            isConnected = true;
                            gui.computerOrNetworkIsFinish();
                            if(!isBlack) break;
                            else continue;
                        }
                    }

                    //get Move message
                    networkMove = Parser.parse(input);
                    gui.computerOrNetworkIsFinish();
                    break;
                }
            }

        }catch(Exception x){
        }
    }

    /**
     * Return move
     * @return move
     */
    public Move getMove(){
        return networkMove;
    }

    /**
     * Send move to client
     * @param move the move
     */
    public void sendMove(Move move){
        out.println(move.toString());
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Stops the server and all components
     */
    public void stop() {
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