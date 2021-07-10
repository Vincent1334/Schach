package chess.network;

import chess.gui.Logic;
import chess.model.Move;

/**
 * This class contains the network logic
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-28
 */
public class NetworkPlayer {

    private Server server;
    private Client client;

    private final int PORT;
    private boolean connected = false;
    private String ipAddress;
    private boolean isBlack;

    /**
     * NetworkPlayer constructor (Server)
     *
     * @param port    Server port
     * @param isBlack client team
     */
    public NetworkPlayer(int port, boolean isBlack) {
        this.PORT = port;
        this.isBlack = isBlack;
    }

    /**
     * NetworkPlayer constructor (Client)
     *
     * @param port      Server port
     * @param ipAddress Server ip
     */
    public NetworkPlayer(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.PORT = port;
    }

    /**
     * Init a new Client/Server instance
     *
     * @param gui gui for Thread
     */
    public void initNetworkPlayer(Logic gui) {
        if (ipAddress != null) {
            client = new Client(ipAddress, PORT, gui);
        } else {
            server = new Server(PORT, isBlack, gui);
        }
    }

    /**
     * Returns Output
     *
     * @return output
     */
    public Object getOutput() {
        if (server != null) {
            return server.getOutput();
        }
        return client.getOutput();
    }

    /**
     * send Move to opponent
     *
     * @param move Move
     */
    public void sendMove(Move move) {
        if (server != null) {
            server.sendMove(move);
        } else {
            client.sendMove(move);
        }
    }

    /**
     * sends the undoRedoIndex
     * @param index the index of the movehistory you want to jump back
     */
    public void sendUndoRedo(int index){
        if (server != null) {
            server.sendUndoRedoIndex(index);
        } else {
            client.sendUndoRedoIndex(index);
        }
    }

    /**
     * Returns team
     *
     * @return isBlack
     */
    public boolean team() {
        if (ipAddress == null) {
            return isBlack;
        }
        return client.clientTeam();
    }

    /**
     * Set ready to play
     *
     * @param isReady True when network is ready to play
     */
    public void setReadyToPlay(boolean isReady) {
        this.connected = isReady;
    }

    /**
     * Returns ready to play
     *
     * @return True when network is ready to play
     */
    public boolean isReadyToPlay() {
        return connected;
    }

    public String getIpAddress(){
        if (server != null) {
            return server.getIPAddress();
        }
        if (client != null) {
            return client.getIpAddress();
        }
        return "localhost";
    }

    /**
     * sends exit
     */
    public void sendExit(){
        if (server != null) {
            server.sendExit();
        }
        if (client != null) {
            client.sendExit();
        }
    }

    /**
     * returns the port of the game
     * @return the port of the game
     */
    public int getPORT(){
        return PORT;
    }

    /**
     * Stop network and all components
     */
    public void killNetwork() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }
}
