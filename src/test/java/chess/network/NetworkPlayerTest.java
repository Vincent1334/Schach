package chess.network;

import chess.model.Move;
import chess.model.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests to check all network features
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-07-11
 */
public class NetworkPlayerTest {

    private final String IP = "localhost";

    /**
     * Test default network features
     * @throws InterruptedException network issue
     */
    @Test
    public void testNetworkServerWhite() throws InterruptedException {
        //Setup Network: client is black and server is white
        NetworkPlayer client = new NetworkPlayer(5555,IP, false, false);
        NetworkPlayer server = new NetworkPlayer(5555, IP, true, false);

        server.initNetworkPlayer(null);
        client.initNetworkPlayer(null);

        //Waiting for connection
        Thread.sleep(500);

        //Check if the teams were distributed correctly
        assertFalse(server.isNetworkPlayerBlack());
        assertTrue(client.isNetworkPlayerBlack());

        //Check move
        Move testMove = new Move(new Position(1, 1), new Position(3, 3));
        server.sendMove(testMove);

        //Waiting for connection
        Thread.sleep(500);


        assertEquals(testMove.toString(), ((Move) client.getNetworkOutput()).toString());

        //kill Network
        client.killNetwork();
        server.killNetwork();


    }

    /**
     * Test default network features
     * @throws InterruptedException network issue
     */
    @Test
    public void testNetworkServerBlack() throws InterruptedException {
        //Setup Network: client is white and server is black
        NetworkPlayer client = new NetworkPlayer(5555,IP, false, false);
        NetworkPlayer server = new NetworkPlayer(5555, IP, true, true);

        server.initNetworkPlayer(null);
        client.initNetworkPlayer(null);

        //Waiting for connection
        Thread.sleep(500);

        //Check if the teams were distributed correctly
        assertFalse(client.isNetworkPlayerBlack());
        assertTrue(server.isNetworkPlayerBlack());

        //Check move
        Move testMove = new Move(new Position(1, 1), new Position(3, 3));
        client.sendMove(testMove);

        Thread.sleep(500);

        assertEquals(testMove.toString(), ((Move) server.getNetworkOutput()).toString());

        //Check UndoRedo
        client.sendUndoRedo(2);

        Thread.sleep(500);

        assertEquals(2, (Integer) server.getNetworkOutput());

        //kill Network
        client.killNetwork();
        server.killNetwork();
    }
}
