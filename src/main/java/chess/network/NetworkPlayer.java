package chess.network;

import chess.enums.NetworkFlags;
import chess.gui.Logic;
import chess.model.Move;
import chess.model.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class contains the network logic
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-28
 */
public class NetworkPlayer implements Runnable{

    private Logic gui;

    private final int PORT;
    private final String IPADDRESS;
    private boolean isBlack;
    private boolean isServer;
    private Thread thread;

    //Server objects
    private ServerSocket serverSocket;
    //Client objects
    private Socket clientSocket;
    //Network objects
    private PrintWriter out;
    private BufferedReader in;
    private Object networkOutput;
    //Network FLags
    NetworkFlags flag;

    /**
     * NetworkPlayer constructor (Server)
     *
     * @param port    Server port
     * @param isBlack client team
     */
    public NetworkPlayer(int port, String ipAddress, boolean isServer, boolean isBlack) {
        this.PORT = port;
        this.IPADDRESS = ipAddress;
        this.gui = gui;
        this.isServer = isServer;
        flag = NetworkFlags.Connecting;
        this.isBlack = isBlack;
    }

    public void initNetworkPlayer(Logic gui){
        this.gui = gui;
        thread = new Thread(this);
        thread.start();
    }

    private void initServer(){
        try {
            serverSocket = new ServerSocket(PORT);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while(!thread.isInterrupted() && flag == NetworkFlags.Connecting){
                String input = in.readLine();
                if(input != null){
                    //Game without UndoRedo
                    if(input.equals("start")){
                        //TODO: disableUndoRedo()
                        flag = NetworkFlags.SetupTeams;
                        out.println(isBlack ? "white" : "black");
                        System.out.println("Server: " + (isBlack ? "black" : "white"));
                    }
                    //Game with undo Redo
                    if(input.equals("startUR")){
                        flag = NetworkFlags.SetupTeams;
                        out.println(isBlack ? "whiteUR" : "blackUR");
                        System.out.println("Server: " + (isBlack ? "black" : "white"));
                    }
                    if(input.equals("ready")){
                        flag = NetworkFlags.InGame;
                    }
                }
                //Update gui
                updateGUI();
            }
        } catch (IOException e) {
            System.out.println("Can't connect!");
        }
    }

    private void initClient(){
        try {
            clientSocket = new Socket(IPADDRESS, PORT);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println("startUR");

            while(!thread.isInterrupted() && flag == NetworkFlags.Connecting){
                String input = in.readLine();
                if(input != null){
                    if (input.equals("white")) {
                        isBlack = false;
                        flag = NetworkFlags.SetupTeams;
                        //TODO: disableUndoRedo()
                        out.println("ready");
                    }
                    if (input.equals("black")) {
                        isBlack = true;
                        flag = NetworkFlags.SetupTeams;
                        //TODO: disableUndoRedo()
                        out.println("ready");
                    }
                    if (input.equals("whiteUR")) {
                        isBlack = false;
                        flag = NetworkFlags.SetupTeams;
                        out.println("ready");
                    }
                    if (input.equals("blackUR")) {
                        isBlack = true;
                        flag = NetworkFlags.SetupTeams;
                        out.println("ready");
                    }
                    //UPdate GUI
                    updateGUI();
                }
            }
        } catch (IOException e) {
            System.out.println("Can't connect!");
        }
    }


    @Override
    public void run() {
        if(isServer){
            initServer();
        }else{
            initClient();
        }
        while(!thread.isInterrupted()){
            try{
                String input = in.readLine();
                if(input != null){
                    //Move
                    if(input.contains("-")){
                        //get Move message
                        networkOutput = Parser.parse(input);
                        flag = NetworkFlags.Move;
                        updateGUI();
                    }
                    //UndoRedo
                    if(isNumeric(input)){
                        //update undoRedo
                        networkOutput = Integer.parseInt(input);
                        flag = NetworkFlags.UndoRedo;
                        updateGUI();
                    }
                    //Exit
                    if(input.equals("exit")){
                        flag = NetworkFlags.Exit;
                        updateGUI();
                    }
                }
            }catch (Exception x){
                System.out.println("Network Error!");
            }
        }
    }

    private void updateGUI(){
        if(gui != null) gui.computerOrNetworkIsFinish();
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public void sendMove(Move move){
        out.println(move.toString());
    }

    public void sendUndoRedo(int pointer){
        out.println(pointer);
    }

    public Object getNetworkOutput(){
        return networkOutput;
    }

    public NetworkFlags getFlag(){
        return flag;
    }

    public void setFlag(NetworkFlags flag){
        this.flag = flag;
    }

    public boolean getIsBlack(){
        return isBlack;
    }

    public void killNetwork(){
        try{
            out.println("exit");
            thread.interrupt();
            in.close();
            out.close();
            if(serverSocket != null) serverSocket.close();
            clientSocket.close();
        }catch (Exception x){
            x.printStackTrace();
        }
    }
}


























