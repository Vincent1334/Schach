package chess.cli;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class CliTest {

    @Test
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
        test02.put("convertPawnTo", 1);
        assertEquals(test02, Cli.parse("d7-d8P"),  "Test02 fail");

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
    }
}
