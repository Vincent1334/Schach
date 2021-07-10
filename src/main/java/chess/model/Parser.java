package chess.model;
/**
 * This class contains the parser logic
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-25
 */
public class Parser {

    /**
     * checks the syntax of the move
     *
     * @param input User input
     * @return if the syntax of the move is valid
     */
    public static boolean validSyntax(String input) {
        // e.g. "b2-e5Q"

        boolean correct = checkLength(input) && checkHyphen(input) && checkLetters(input) && checkNumbers(input);
        if (input.length() == 6) {
            correct = correct && checkConversionLetter(input);
        }
        return correct;
    }

    /**
     * checks if the input has the correct length (5 or 6)
     * @param input User input
     * @return true, if the input has the correct length
     */
    private static boolean checkLength(String input) {
        return input.length() == 5 || input.length() == 6;
    }

    /**
     * checks, if on the index 2 of the input is an hyphen
     * @param input User input
     * @return true, if on the index 2 of the input is an hyphen
     */
    private static boolean checkHyphen(String input) {
        return input.charAt(2) == 45;
    }

    /**
     * checks, if on the index 0 and 3 of the input are figure letters
     * @param input user input
     * @return true, if on the index 0 and 3 of the input are figure letters
     */
    private static boolean checkLetters(String input) {
        return input.charAt(0) >= 97 && input.charAt(0) <= 104 && input.charAt(3) >= 97 && input.charAt(3) <= 104;
    }

    /**
     * checks, if on the index 1 and 4 of the input are numbers between 1 and 8
     * @param input user input
     * @return true, if on the index 1 and 4 of the input are numbers between 1 and 8
     */
    private static boolean checkNumbers(String input) {
        return input.charAt(1) >= 49 && input.charAt(1) <= 56 && input.charAt(4) >= 49 && input.charAt(4) <= 56;
    }

    /**
     * checks, if on the index 5 of the input is a promotion figure letter
     * @param input user input
     * @return true, if on the index 5 of the input is a promotion figure letter
     */
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
