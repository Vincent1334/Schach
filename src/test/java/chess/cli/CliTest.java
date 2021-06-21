package chess.cli;

import chess.model.Move;
import chess.model.Position;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests to check the methods in CLI
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-11
 *
 */

public class CliTest {

    /**
     * Tests whether the parser works correctly
     */
       /*
    @Test
    @Disabled
    public void testParser(){
        //Testcase 1 (correct default input)
        Move testMove1 = new Move(new Position(0,0), new Position(0,1),5);
        assertEquals(testMove1, Cli.parse("a1-a2"),  "Test1 fail");

        //Testcase 2 (correct PawnConversion input)
        Move testMove2 = new Move(new Position(3,6), new Position(3,7),4);
        assertEquals(testMove2, Cli.parse("d7-d8B"),  "Test2 fail");

        //Testcase 3 (correct PawnConversion input)
        Move testMove3 = new Move(new Position(3,6), new Position(3,7));
        assertEquals(testMove3, Cli.parse("d7-d8"),  "Test3 fail");
    }

       /**
        * Tests some random inputs outside of the boards boundaries
        */
    /*
    @Test
    @Disabled
    public void testSyntaxOutOfBounds(){
        assertFalse(Cli.validSyntax("d1-i5"),  "d1-i5 not detected");
        assertFalse(Cli.validSyntax("b3-a9"),  "b3-a9 not detected");
        assertFalse(Cli.validSyntax("a1-a22"),  "a1-a22 not detected");
    }

       /**
        * Tests some random invalid inputs
        */
       /*
    @Test
    @Disabled
    public void testSyntaxInvalidInput(){
        assertFalse(Cli.validSyntax("d1-a2-d6"),  "d1-a2-d6 not detected");
        assertFalse(Cli.validSyntax("d-e"),  "d-e not detected");
        assertFalse(Cli.validSyntax("2-4"),  "2-4 not detected");
        assertFalse(Cli.validSyntax("-"),  "- not detected");
    }

    /**
     * Tests some random inputs for Pawn Conversion
     */
 /*   @Test
    @Disabled
    public void testSyntaxInvalidPawnConversion(){
        assertFalse(Cli.validSyntax("a1-a2V"),  "a1-a2V not detected");
        assertFalse(Cli.validSyntax("b6-b7F"),  "b6-b7F not detected");
        assertFalse(Cli.validSyntax("i6-i7K"),  "i6-i7K not detected");
        assertFalse(Cli.validSyntax("d6-d7-Q"),  "d6-d7-Q not detected");
        assertFalse(Cli.validSyntax("d6-d7QQ"),  "d6-d7QQ not detected");
    }
*/
}
