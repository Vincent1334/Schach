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
    private boolean killThread = false;

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
            initConnection();
            while(!killThread){
                String input = in.readLine();
                if(input != null){
                    //check if new client is connected
                    if(!isConnected && startCommand(input)){
                        continue;
                    }
                    switch (readyCommand(input)){
                        case 0: break;
                        case 1: continue;
                    }

                    if(input.contains("-")){
                        //get Move message
                        networkMove = Parser.parse(input);
                        if(gui != null) gui.computerOrNetworkIsFinish();
                    }else{
                        //update undoRedo
                        if(gui != null) gui.getController().undoRedoSend(input);
                    }

                    break;
                }
            }

        }catch(Exception x){
            x.printStackTrace();
        }
        if(killThread){
            try {
                in.close();
                out.close();
                serverSocket.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Perform code when ready command arrives
     * @param input equals ready
     * @return 0 or 1 when command was successful
     */
    private int readyCommand(String input){
        if(!isConnected && input.equals("ready")){
            //leave listener if server starts with white
            isConnected = true;
            if(gui != null) gui.computerOrNetworkIsFinish();
            if(!isBlack){
                return 0;
            }
            return 1;
        }
        return 2;
    }

    /**
     * Perform code when start command arrives
     * @param input equals start
     * @return True when command was successful
     */
    private boolean startCommand(String input){
        if(input.equals("start")){
            //send Team color
            out.println(isBlack ? "white" : "black");
            System.out.println("Server: " + (isBlack ? "black" : "white"));
            return true;
        }
        return false;
    }

    /**
     * Initial connection
     * @throws IOException Connection fails
     */
    private void initConnection() throws IOException {
        if(!isConnected){
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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

    public String getIPAddress(){
        try{
            return serverSocket.getInetAddress().getHostAddress();
        }catch (Exception x){
            return "localhost";
        }

    }

    public void sendUndoRedo(int index) {
        out.println(index);
    }

        /**
         * Stops the server and all components
         */
    public void stop() {
        killThread = true;
        thread = new Thread();
        thread.start();
    }
}