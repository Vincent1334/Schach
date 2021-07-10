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
    private Object networkOutput;
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
                    //Is Client ready?
                    if(!isConnected && readyCommand(input)){
                       continue;
                    }
                    //send move
                    if(input.contains("-")){
                        //get Move message
                        networkOutput = Parser.parse(input);
                    }
                    //send undo redo
                    if(isNumeric(input)){
                        //update undoRedo
                        networkOutput = Integer.parseInt(input);
                    }
                    //send exit
                    if(input.equals("exit")){
                        exit = true;
                    }
                    System.out.println("Server: " + input);
                    if(gui != null) gui.computerOrNetworkIsFinish();
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
    private boolean readyCommand(String input){
        if(input.equals("ready")) {
            return false;
        }
        return true;
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
    public Object getOutput(){
        return networkOutput;
    }

    /**
     * Send move to client
     * @param move the move
     */
    public void sendMove(Move move){
        out.println(move.toString());
        System.out.println("Server: send Move " + move.toString());
    }

    public void sendExit(){
        out.println("exit");
    }

    public String getIPAddress(){
        try{
            return serverSocket.getInetAddress().getHostAddress();
        }catch (Exception x){
            return "localhost";
        }

    }

    public void sendUndoRedoIndex(int index) {
        out.println(index);
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

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}