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
        test01.put("convertPawnTo", 0);
        assertEquals(Cli.parse("a1-a2"), test01, "");

        //Testcase 02 (correct PawnConversion input)
        Map<String, Integer> test02 = new HashMap<String, Integer>();
        test02.put("posX", 3);
        test02.put("posY", 6);
        test02.put("newX", 3);
        test02.put("newY", 7);
        test02.put("convertPawnTo", 1);
        assertEquals(Cli.parse("d7-d8K"), test02, "");

        //Testcase 03 (invalid default input)
        Map<String, Integer> test03 = new HashMap<String, Integer>();
        assertEquals(Cli.parse("f1-a2"), test03, "f1-a2 fail");
        assertEquals(Cli.parse("d1-i5"), test03, "d1-i5 fail");
        assertEquals(Cli.parse("b3-a9"), test03, "b3-a9 fail");
        assertEquals(Cli.parse("a1-a22"), test03, "a1-a22 fail");
        assertEquals(Cli.parse("d1-a2-d6"), test03, "d1-a2-d6 fail");
        assertEquals(Cli.parse("d-e"), test03, "d-e fail");
        assertEquals(Cli.parse("2-4"), test03, "2-4 fail");
        assertEquals(Cli.parse("-"), test03, "- fail");

        //Testcase 04 (invalid PawnConversion input)
        Map<String, Integer> test04 = new HashMap<String, Integer>();
        assertEquals(Cli.parse("a1-a2V"), test04, "a1-a2V fail");
        assertEquals(Cli.parse("b6-b7F"), test04, "b6-b7F fail");
        assertEquals(Cli.parse("i6-i7K"), test04, "i6-i7K fail");
        assertEquals(Cli.parse("d6-d7-Q"), test04, "d6-d7-Q fail");
        assertEquals(Cli.parse("d6-d7QQ"), test04, "d6-d7QQ fail");
    }
}
