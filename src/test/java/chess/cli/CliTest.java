package chess.cli;

import chess.model.Move;
import chess.model.Position;
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

    /*@Test
    public void parserTest(){
        //Testcase 01 (correct default input)
        Map<String, Integer> test01 = new HashMap<String, Integer>();
        test01.put("posX", 0);
        test01.put("posY", 0);
        test01.put("newX", 0);
        test01.put("newY", 1);
        test01.put("convertPawnTo", 5);
        assertEquals(test01, Cli.parse("a1-a2"),  "Test01 fail");

        //Testcase 02 (correct PawnConversion input)
        Map<String, Integer> test02 = new HashMap<String, Integer>();
        test02.put("posX", 3);
        test02.put("posY", 6);
        test02.put("newX", 3);
        test02.put("newY", 7);
        test02.put("convertPawnTo", 4);
        assertEquals(test02, Cli.parse("d7-d8B"),  "Test02 fail");

        //Testcase 03 (invalid default input)
        Map<String, Integer> test03 = new HashMap<String, Integer>();
        assertEquals(test03, Cli.parse("d1-i5"),  "d1-i5 fail");
        assertEquals(test03, Cli.parse("b3-a9"),  "b3-a9 fail");
        assertEquals(test03, Cli.parse("a1-a22"),  "a1-a22 fail");
        assertEquals(test03, Cli.parse("d1-a2-d6"),  "d1-a2-d6 fail");
        assertEquals(test03, Cli.parse("d-e"),  "d-e fail");
        assertEquals(test03, Cli.parse("2-4"),  "2-4 fail");
        assertEquals(test03, Cli.parse("-"),  "- fail");

        //Testcase 04 (invalid PawnConversion input)
        Map<String, Integer> test04 = new HashMap<String, Integer>();
        assertEquals(test04, Cli.parse("a1-a2V"),  "a1-a2V fail");
        assertEquals(test04, Cli.parse("b6-b7F"),  "b6-b7F fail");
        assertEquals(test04, Cli.parse("i6-i7K"),  "i6-i7K fail");
        assertEquals(test04, Cli.parse("d6-d7-Q"),  "d6-d7-Q fail");
        assertEquals(test04, Cli.parse("d6-d7QQ"),  "d6-d7QQ fail");
    }*/

    /**
     * Tests whether the parser works correctly
     */
    @Test
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

 /*    @Test
    public void testValidSyntax(){
        assertFalse(Cli.validSyntax("d1-i5"),  "d1-i5 not detected");
        assertFalse(Cli.validSyntax("b3-a9"),  "b3-a9 not detected");
        assertFalse(Cli.validSyntax("a1-a22"),  "a1-a22 not detected");
        assertFalse(Cli.validSyntax("d1-a2-d6"),  "d1-a2-d6 not detected");
        assertFalse(Cli.validSyntax("d-e"),  "d-e not detected");
        assertFalse(Cli.validSyntax("2-4"),  "2-4 not detected");
        assertFalse(Cli.validSyntax("-"),  "- not detected");
        assertFalse(Cli.validSyntax("a1-a2V"),  "a1-a2V not detected");
        assertFalse(Cli.validSyntax("b6-b7F"),  "b6-b7F not detected");
        assertFalse(Cli.validSyntax("i6-i7K"),  "i6-i7K not detected");
        assertFalse(Cli.validSyntax("d6-d7-Q"),  "d6-d7-Q not detected");
        assertFalse(Cli.validSyntax("d6-d7QQ"),  "d6-d7QQ not detected");
    }*/

    /**
     * Tests some random inputs outside of the boards boundaries
     */
    @Test
    public void testSyntaxOutOfBounds(){
        assertFalse(Cli.validSyntax("d1-i5"),  "d1-i5 not detected");
        assertFalse(Cli.validSyntax("b3-a9"),  "b3-a9 not detected");
        assertFalse(Cli.validSyntax("a1-a22"),  "a1-a22 not detected");
    }

    /**
     * Tests some random invalid inputs
     */
    @Test
    public void testSyntaxInvalidInput(){
        assertFalse(Cli.validSyntax("d1-a2-d6"),  "d1-a2-d6 not detected");
        assertFalse(Cli.validSyntax("d-e"),  "d-e not detected");
        assertFalse(Cli.validSyntax("2-4"),  "2-4 not detected");
        assertFalse(Cli.validSyntax("-"),  "- not detected");
    }

    /**
     * Tests some random inputs for Pawn Conversion
     */
    @Test
    public void testSyntaxInvalidPawnConversion(){
        assertFalse(Cli.validSyntax("a1-a2V"),  "a1-a2V not detected");
        assertFalse(Cli.validSyntax("b6-b7F"),  "b6-b7F not detected");
        assertFalse(Cli.validSyntax("i6-i7K"),  "i6-i7K not detected");
        assertFalse(Cli.validSyntax("d6-d7-Q"),  "d6-d7-Q not detected");
        assertFalse(Cli.validSyntax("d6-d7QQ"),  "d6-d7QQ not detected");
    }

}
