package chess.network;

import chess.gui.Logic;
import chess.model.Move;

public class NetworkPlayer {

    private Server server;
    private Client client;

    private int port;
    private String ipAddress;
    private boolean isBlack;

    public NetworkPlayer(int port, boolean isBlack){
        this.port = port;
        this.isBlack = isBlack;

    }

    public NetworkPlayer(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;

    }

    public void initNetworkPlayer(Logic gui){
        if(ipAddress != null){
            client = new Client(ipAddress, port, gui);
        }else{
            server = new Server(port, isBlack, gui);
        }
    }

    public Move getMove(){
        if(server != null) return server.getMove();
        else return client.getMove();
    }

    public void sendMove(Move move){
        if(server != null) server.sendMove(move);
        else client.sendMove(move);
    }

    public boolean isReadyToPlay(){
        return server.isReadyToPlay();
    }

    public Thread getClientThread(){
        return client.getThread();
    }

    public void sendMessage(String message){
        if(server != null) server.sendMessage(message);
        else client.sendMessage(message);
    }
}
