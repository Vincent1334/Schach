package chess.network;

import chess.gui.Logic;
import chess.model.Move;
import chess.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class NetworkPlayerTest {

    @Test
    public void testNetwork() throws InterruptedException {
        //Setup Network: client is black and server is white
        NetworkPlayer client = new NetworkPlayer("localhost", 5555);
        NetworkPlayer server = new NetworkPlayer(5555, false);

        server.initNetworkPlayer(null);
        client.initNetworkPlayer(null);

        //Waiting for connection
        Thread.sleep(500);

        //Check if the teams were distributed correctly
        assertFalse(server.team());
        assertTrue(client.team());

        //Check move
        Move testMove = new Move(new Position(1, 1), new Position(3, 3));
        server.sendMove(testMove);

        Thread.sleep(500);

        //assertEquals(testMove.toString(), client.getMove().toString());

        //kill Network
        client.killNetwork();
        server.killNetwork();

        //Setup Network: client is white and server is black
        client = new NetworkPlayer("localhost", 5555);
        server = new NetworkPlayer(5555, true);

        server.initNetworkPlayer(null);
        client.initNetworkPlayer(null);

        //Waiting for connection
        Thread.sleep(500);

        //Check if the teams were distributed correctly
        assertFalse(client.team());
        assertTrue(server.team());

        //Check move
        client.sendMove(testMove);

        Thread.sleep(500);

       // assertEquals(testMove.toString(), server.getMove().toString());
    }

    @Test
    public void testNetworkFeatures() throws InterruptedException {
        //Setup Network: client is black and server is white
        NetworkPlayer client = new NetworkPlayer("localhost", 5555);
        NetworkPlayer server = new NetworkPlayer(5555, false);

        server.initNetworkPlayer(null);
        client.initNetworkPlayer(null);

        //Waiting for connection
        Thread.sleep(500);

        //Check isReadyToPlay
        assertFalse(server.isReadyToPlay());
        assertFalse(client.isReadyToPlay());
        server.setReadyToPlay(true);
        client.setReadyToPlay(true);
        assertTrue(server.isReadyToPlay());
        assertTrue(client.isReadyToPlay());

        //Check IP address
        assertEquals("0.0.0.0", server.getIpAddress());
        assertEquals("localhost", client.getIpAddress());
    }
}
