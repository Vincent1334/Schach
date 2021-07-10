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
public class Client implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Thread thread;
    private boolean isBlack;
    private Logic gui;
    private Object networkOutput;

    private String ipAddress;
    private int port;
    private boolean isConnected;

    /**
     * Client constructor
     *
     * @param ipAddress Server address
     * @param port      Server port
     * @param gui       gui for Thread
     */
    public Client(String ipAddress, int port, Logic gui) {
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
        initConnection();
        while (!thread.isInterrupted()) {
            try {
                String input = in.readLine();
                if (input != null) {
                    //Game traffic
                    if (isConnected) {
                        //chess move input
                        if(input.contains("-")){
                            //get Move message
                            networkOutput = Parser.parse(input);
                        }
                        //undo redo input
                        if(isNumeric(input)){
                            //update undoRedo
                            networkOutput = Integer.parseInt(input);
                        }
                        //exit input
                        if(input.equals("exit")){
                            networkOutput = true;
                        }
                        if(gui != null) gui.computerOrNetworkIsFinish();
                        System.out.println("Client: " + input);
                    }else{
                        //Init traffic
                        System.out.println("Client: " + input);
                        if (input.equals("white")) {
                            isBlack = false;
                            isConnected = true;
                            out.println("ready");
                        }
                        if (input.equals("black")) {
                            isBlack = true;
                            isConnected = true;
                            out.println("ready");
                        }
                        if(gui != null) gui.computerOrNetworkIsFinish();
                    }
                }
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    }

    private void initConnection(){
        if (!isConnected) {
            try {
                clientSocket = new Socket(ipAddress, port);
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out.println("start");
            } catch (Exception x) {
                System.out.println("Cant connect");
            }
        }
    }

    /**
     * get move from Server
     *
     * @return move
     */
    public Object getOutput() {
        return networkOutput;
    }

    /**
     * Send move to Server
     *
     * @param move Move
     */
    public void sendMove(Move move) {
        out.println(move.toString());
        System.out.println("Client: send Move " + move.toString());
    }

    public void sendExit(){
        out.println("exit");
    }

    public void sendUndoRedoIndex(int index) {
        out.println(index);
    }



    /**
     * Returns Client team
     *
     * @return isBlack
     */
    public boolean clientTeam() {
        return isBlack;
    }

    public String getIpAddress(){
        return ipAddress;
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    /**
     * Stop client and all components
     */
    public void stop(){
        try{
            thread.interrupt();
            Thread.sleep(20);
            in.close();
            out.close();
            clientSocket.close();
        }catch(Exception x){

        }



    }
}