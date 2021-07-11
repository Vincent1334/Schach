package chess.network;

import chess.enums.NetworkFlags;
import chess.gui.Logic;
import chess.managers.LanguageManager;
import chess.model.Move;
import chess.model.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    private final boolean IS_SERVER;
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

    private static final String WHITE = "white";
    private static final String BLACK = "black";
    private static final String READY = "ready";

    /**
     * NetworkPlayer constructor (Server)
     *
     * @param port    Server port
     * @param isBlack client team
     */
    public NetworkPlayer(int port, String ipAddress, boolean IS_SERVER, boolean isBlack) {
        this.PORT = port;
        this.IPADDRESS = ipAddress;
        this.IS_SERVER = IS_SERVER;
        flag = NetworkFlags.Connecting;
        this.isBlack = isBlack;
    }

    /**
     * initializes the network
     * @param gui the gui of the game
     */
    public void initNetworkPlayer(Logic gui){
        this.gui = gui;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * initializes the server
     */
    private void initServer(){
        try {
            serverSocket = new ServerSocket(PORT);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while(!thread.isInterrupted() && flag == NetworkFlags.Connecting){
                String input = in.readLine();
                if(input != null){
                    interpretServerInput(input);
                }
                //Update gui
                updateGUI();
            }
        } catch (IOException e) {
            System.out.println("Can't connect!");
        }
    }

    /**
     * interprets server input
     * @param input ,input which should be interpreted
     */
    private void interpretServerInput(String input){
        //Game without UndoRedo
        if(input.equals("start")){
            gui.disableUndoRedo();
            flag = NetworkFlags.SetupTeams;
            out.println(isBlack ? WHITE : BLACK);
            System.out.println("Server: " + (isBlack ? BLACK : WHITE));
        }
        //Game with undo Redo
        if(input.equals("startUR")){
            flag = NetworkFlags.SetupTeams;
            out.println(isBlack ? "whiteUR" : "blackUR");
            System.out.println("Server: " + (isBlack ? BLACK : WHITE));
        }
        if(input.equals(READY)){
            flag = NetworkFlags.InGame;
        }
    }


    /**
     * initializes the client
     */
    private void initClient(){
        try {
            clientSocket = new Socket(IPADDRESS, PORT);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println("startUR");

            while(!thread.isInterrupted() && flag == NetworkFlags.Connecting){
                String input = in.readLine();
                if(input != null){
                    if (input.equals(WHITE)) {
                        isBlack = false;
                        flag = NetworkFlags.SetupTeams;
                        gui.disableUndoRedo();
                        out.println(READY);
                    }
                    if (input.equals(BLACK)) {
                        isBlack = true;
                        flag = NetworkFlags.SetupTeams;
                        gui.disableUndoRedo();
                        out.println(READY);
                    }
                    if (input.equals("whiteUR")) {
                        isBlack = false;
                        flag = NetworkFlags.SetupTeams;
                        out.println(READY);
                    }
                    if (input.equals("blackUR")) {
                        isBlack = true;
                        flag = NetworkFlags.SetupTeams;
                        out.println(READY);
                    }
                    //UPdate GUI
                    updateGUI();
                }
            }
        } catch (IOException e) {
            System.out.println("Can't connect!");
        }
    }


    /**
     * the game loop of a networkgame
     */
    @Override
    public void run() {
        if(IS_SERVER){
            initServer();
        }else{
            initClient();
        }
        while(!thread.isInterrupted()){
            try{
                String input = in.readLine();
                if(input != null){
                    //Move
                    if(input.length() > 3 && input.charAt(2) == 45){
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
                System.out.println("lost connection!");
            }
        }
    }

    /**
     * updates the gui
     */
    private void updateGUI(){
        if(gui != null) gui.computerOrNetworkIsFinish();
    }

    /**
     * Test if str is a number
     * @param str the string you want to test
     * @return true, if str is a number
     */
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    /**
     * sends a move
     * @param move the move you want to send
     */
    public void sendMove(Move move){
        out.println(move.toString());
    }

    /**
     * send undo/redo
     * @param pointer the index of the move you want to go back
     */
    public void sendUndoRedo(int pointer){
        out.println(pointer);
        if(pointer%2 == 0 && !isBlack || pointer%2 != 0 && isBlack){
            gui.setNotification(true, LanguageManager.getText("network_player_waiting_label"));
        }else{
            gui.setNotification(false, "");
        }
        gui.getController().getScene().updateScene();
    }

    /**
     * the output of the network
     * @return network output
     */
    public Object getNetworkOutput(){
        return networkOutput;
    }

    /**
     * returns the network flag
     * @return flag
     */
    public NetworkFlags getFlag(){
        return flag;
    }

    /**
     * sets the network flag
     * @param flag the flag you want to set
     */
    public void setFlag(NetworkFlags flag){
        this.flag = flag;
    }

    /**
     * returns, if the networkplayer is black
     * @return true, if the networkplayer is black
     */
    public boolean getIsBlack(){
        return isBlack;
    }

    /**
     * terminates the network
     */
    public void killNetwork(){
        try{
            out.println("exit");
            thread.interrupt();
            Thread.sleep(30);
            clientSocket.close();
            if(serverSocket != null) serverSocket.close();
            in.close();
            out.close();
        }catch (Exception x){
            x.printStackTrace();
        }
    }
}


























