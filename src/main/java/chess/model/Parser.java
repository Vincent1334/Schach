package chess.model;
/**
 * This class contains the parser logic
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-25
 */
public class Parser {

    /**
     * Converts user input into coordinates. e.g. a3 == x: 0 y: 2
     *
     * @param input User input
     * @return Move coordinates
     */
    public static boolean validSyntax(String input) {
        // e.g. "b2-e5Q"

        boolean correct = checkLength(input) && checkHyphen(input) && checkLetters(input) && checkNumbers(input);
        if (input.length() == 6) {
            correct = correct && checkConversionLetter(input);
        }
        return correct;
    }

    private static boolean checkLength(String input) {
        return input.length() == 5 || input.length() == 6;
    }

    private static boolean checkHyphen(String input) {
        return input.charAt(2) == 45;
    }

    private static boolean checkLetters(String input) {
        return input.charAt(0) >= 97 && input.charAt(0) <= 104 && input.charAt(3) >= 97 && input.charAt(3) <= 104;
    }

    private static boolean checkNumbers(String input) {
        return input.charAt(1) >= 49 && input.charAt(1) <= 56 && input.charAt(4) >= 49 && input.charAt(4) <= 56;
    }

    private static boolean checkConversionLetter(String input) {
        return input.charAt(5) == 66 || input.charAt(5) == 78 || input.charAt(5) == 81 || input.charAt(5) == 82;
    }

    /**
     * parse String input into Move objekt
     *
     * @param input String input
     * @return Move object
     */
    public static Move parse(String input) {
        // e.g. "b2-e5Q"
        int posX = (int) input.charAt(0) - 97;
        int posY = (int) input.charAt(1) - 49;
        int newX = (int) input.charAt(3) - 97;
        int newY = (int) input.charAt(4) - 49;
        if (input.length() == 6) {
            int pawnConversionTo;
            switch (input.charAt(5)) {
                // Rook
                case 82:
                    pawnConversionTo = 2;
                    break;
                // Knight
                case 78:
                    pawnConversionTo = 3;
                    break;
                // Bishop
                case 66:
                    pawnConversionTo = 4;
                    break;
                // Queen
                default:
                    pawnConversionTo = 5;
            }
            return new Move(new Position(posX, posY), new Position(newX, newY), pawnConversionTo);
        }
        return new Move(new Position(posX, posY), new Position(newX, newY));
    }
}
