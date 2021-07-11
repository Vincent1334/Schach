package chess.network;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class contains all information about a connection
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-28
 */
public class Connection {


    private int port;
    private  String ip;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private boolean server;

    /**
     * constructor of connection
     */
    public Connection(){

    }

    /**
     * returns port
     * @return port
     */
    public int getPort() {
        return port;
    }

    /**
     * sets port
     * @param port port, you want to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * returns clientSocket
     * @return clientSocket
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * sets clientSocket
     * @param clientSocket clientSocket, you want to set
     */
    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * returns server socket
     * @return server socket
     */
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * sets server socket
     * @param serverSocket server socket, you want to set
     */
    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * returns the ip
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * sets the ip
     * @param ip ip, you want to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * returns isServer
     * @return isServer
     */
    public boolean isServer() {
        return server;
    }

    /**
     * sets isServer
     * @param isServer the boolean you want to set
     */
    public void setIsServer(boolean isServer) {
        this.server = isServer;
    }

}
