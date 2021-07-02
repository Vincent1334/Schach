package chess.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * This class contains tests to check the methods in the class "Parser"
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-07-02
 */
public class ParserTest {

    @Test
    /**
     * Tests basic parser functionality
     */
    public void testParser() {
        // Testcase 1 (correct default input)
        Move testMove1 = new Move(new Position(0, 0), new Position(0, 1), 5);
        assertEquals(testMove1, Parser.parse("a1-a2"), "Test1 fail");

        // Testcase 2 (correct PawnConversion input)
        Move testMove2 = new Move(new Position(3, 6), new Position(3, 7), 4);
        assertEquals(testMove2, Parser.parse("d7-d8B"), "Test2 fail");

        // Testcase 3 (correct PawnConversion input)
        Move testMove3 = new Move(new Position(3, 6), new Position(3, 7));
        assertEquals(testMove3, Parser.parse("d7-d8"), "Test3 fail");
    }

    /**
     * Tests some random inputs outside of the boards boundaries
     */
    @Test
    public void testSyntaxOutOfBounds() {
        assertFalse(Parser.validSyntax("d1-i5"), "d1-i5 not detected");
        assertFalse(Parser.validSyntax("b3-a9"), "b3-a9 not detected");
        assertFalse(Parser.validSyntax("a1-a22"), "a1-a22 not detected");
    }

    /**
     * Tests some random invalid inputs
     */
    @Test
    public void testSyntaxInvalidInput() {
        assertFalse(Parser.validSyntax("d1-a2-d6"), "d1-a2-d6 not detected");
        assertFalse(Parser.validSyntax("d-e"), "d-e not detected");
        assertFalse(Parser.validSyntax("2-4"), "2-4 not detected");
        assertFalse(Parser.validSyntax("-"), "- not detected");
    }

    /**
     * Tests some random inputs for Pawn Conversion
     */
    @Test
    public void testSyntaxInvalidPawnConversion() {
        assertFalse(Parser.validSyntax("a1-a2V"), "a1-a2V not detected");
        assertFalse(Parser.validSyntax("b6-b7F"), "b6-b7F not detected");
        assertFalse(Parser.validSyntax("i6-i7K"), "i6-i7K not detected");
        assertFalse(Parser.validSyntax("d6-d7-Q"), "d6-d7-Q not detected");
        assertFalse(Parser.validSyntax("d6-d7QQ"), "d6-d7QQ not detected");
    }
}
