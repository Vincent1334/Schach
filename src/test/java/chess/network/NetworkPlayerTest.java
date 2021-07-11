package chess.network;

import chess.model.Move;
import chess.model.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class NetworkPlayerTest {

    @Test
    public void testNetwork() throws InterruptedException {
        //Setup Network: client is black and server is white
        NetworkPlayer client = new NetworkPlayer(5555,"localhost", false, false);
        NetworkPlayer server = new NetworkPlayer(5555, "localhost", true, false);

        server.initNetworkPlayer(null);
        client.initNetworkPlayer(null);

        //Waiting for connection
        Thread.sleep(500);

        //Check if the teams were distributed correctly
        assertFalse(server.getIsBlack());
        assertTrue(client.getIsBlack());

        //Check move
        Move testMove = new Move(new Position(1, 1), new Position(3, 3));
        server.sendMove(testMove);

        //Waiting for connection
        Thread.sleep(500);


        assertEquals(testMove.toString(), ((Move) client.getNetworkOutput()).toString());

        //kill Network
        client.killNetwork();
        server.killNetwork();

        //Setup Network: client is white and server is black
        client = new NetworkPlayer(5555,"localhost", false, false);
        server = new NetworkPlayer(5555, "localhost", true, true);

        server.initNetworkPlayer(null);
        client.initNetworkPlayer(null);

        //Waiting for connection
        Thread.sleep(500);

        //Check if the teams were distributed correctly
        assertFalse(client.getIsBlack());
        assertTrue(server.getIsBlack());

        //Check move
        client.sendMove(testMove);

        Thread.sleep(500);

        assertEquals(testMove.toString(), ((Move) server.getNetworkOutput()).toString());
    }
}
