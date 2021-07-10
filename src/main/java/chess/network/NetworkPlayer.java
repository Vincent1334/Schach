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

    private int port;
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
        this.port = port;
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
        this.port = port;
    }

    /**
     * Init a new Client/Server instance
     *
     * @param gui gui for Thread
     */
    public void initNetworkPlayer(Logic gui) {
        if (ipAddress != null) {
            client = new Client(ipAddress, port, gui);
        } else {
            server = new Server(port, isBlack, gui);
        }
    }

    /**
     * Returns move from opponent
     *
     * @return Move
     */
    public Move getMove() {
        if (server != null) {
            return server.getMove();
        }
        return client.getMove();
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

    public int getAndResetUndoRedoIndex(){
        if (server != null) {
           return server.getAndResetUndoRedoIndex();
        } else {
            return client.getAndResetUndoRedoIndex();
        }
    }

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

    public boolean isExit(){
        if (server != null) {
            return server.isExit();
        }
        if (client != null) {
            return client.isExit();
        }
        return false;
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

    public void sendExit(){
        if (server != null) {
            server.sendExit();
        }
        if (client != null) {
            client.sendExit();
        }
    }

    public int getPort(){
        return port;
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
