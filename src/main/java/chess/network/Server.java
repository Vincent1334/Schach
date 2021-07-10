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
    private int undoRedoIndex = -1;
    private boolean exit = false;

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
            while(!thread.isInterrupted()){
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
                    }
                    if(isNumeric(input)){
                        //update undoRedo
                        undoRedoIndex = Integer.parseInt(input);
                        if(gui != null) gui.computerOrNetworkIsFinish();
                        //Let the thread alive
                        if(undoRedoIndex%2 == 0 && isBlack || undoRedoIndex%2 != 0 && !isBlack){
                            continue;
                        }
                    }
                    if(input.equals("exit")){
                        exit = true;
                    }
                    System.out.println("Server: " + input);
                    if(gui != null) gui.computerOrNetworkIsFinish();
                    break;
                }
            }

        }catch(Exception x){
            x.printStackTrace();
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

    /**
     * send exit to Client
     */
    public void sendExit(){
        out.println("exit");
    }

    /**
     * returns the IP address of the server
     * @return the IP address of the server
     */
    public String getIPAddress(){
        try{
            return serverSocket.getInetAddress().getHostAddress();
        }catch (Exception x){
            return "localhost";
        }

    }

    /**
     * send undoRedoIndex to Client and manage thread
     * @param index of the movehistory you want to go back
     */
    public void sendUndoRedoIndex(int index) {
        out.println(index);
        undoRedoIndex = index;
        gui.computerOrNetworkIsFinish();
        thread.interrupt();
        if(index%2 == 0 && isBlack || index%2 != 0 && !isBlack){
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * resets undoRedoIndex
     * @return the movehistory index to which should be jumped back
     */
    public int getAndResetUndoRedoIndex(){
        int index = undoRedoIndex;
        undoRedoIndex = -1;
        return index;
    }

    /**
     * returns, if the server exited
     * @return true, if the server exited
     */
    public boolean isExit(){
        return exit;
    }


    /**
         * Stops the server and all components
         */
    public void stop() {
       try{
           thread.interrupt();
           in.close();
           out.close();
           serverSocket.close();
           clientSocket.close();
       }catch (Exception x){

       }
    }

    /**
     * checks if str is a number
     * @param str the String you want to check
     * @return true, if the str is a number
     */
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}