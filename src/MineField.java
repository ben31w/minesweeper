import java.awt.Point;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * Class to define MineField for MineSweeper game
 *
 * @author Ben Wright
 * @version 2021.10.08
 */

public class MineField {
    /** Random number generator used to get random cells */
    private static Random rand = new Random();

    /**
     * Helper method to create random point within 2D-array bounds. 
     * A Point object is an easy way to store x, y values in one object.
     * 
     * @param field
     *            the MineField from which a random point should be chosen
     * @return random location within array
     */
    public static Point getRandomCell(int[][] field) {
        int x = rand.nextInt(field.length);
        int y = rand.nextInt(field[0].length);

        return new Point(x, y);
    }


    /**
     * Creates a 2D-array of false values of size rows x cols.
     * This array is used to track which cells in the actual mine field have 
     * been exposed.
     * 
     * @param rows
     *            number of rows in the mine field
     * @param cols
     *            number of columns in the mine field
     * @return a 2D-boolean-array (size rows x cols), all equal to false
     */
    public static boolean[][] setUpExposed(int rows, int cols) {
        boolean[][] array = new boolean[rows][cols];
        for (int r = 0; r < array.length; r++) {
            for (int c = 0; c < array[r].length; c++) {
                array[r][c] = false;
            }
        }

        return array;
    }


    /**
     * Create a 2D-array of size rows x cols with the game setup. 
     * Cells will have the following values: 
     * <ul>
     * 	    <li> -1 if the cell is a mine </li>
     *      <li> 0 if the cell is not a mine, and the cell has no mines as neighbors </li>
     *      <li> 1..8, 1 for each mine that the current cell touches. </li> 
     * </ul>
     * 
     * This method calls getRandomCell to choose random places for the mines, 
     * and setHint to set the value for the non-mine cells.
     * 
     * @param rows
     *            number of rows in the mine field
     * @param cols
     *            number of columns in the mine field
     * @param mines
     *            number of mines in the mine field
     * @return a 2D-integer-array (size rows x cols) representing the 
     *         mine field game board
     */
    public static int[][] createMineField(int rows, int cols, int mines) {
        int[][] field = new int[rows][cols];

        // If mines >= the area of the field, set all cells to mines.
        // NOTE: Because of the way the main is set up, this will never happen.
        if (mines >= rows * cols) {
            for (int r = 0; r < field.length; r++) {
                for (int c = 0; c < field[r].length; c++) {
                    field[r][c] = -1;
                }
            }
        }
        // If mines take up more than half the field, temporarily set all cells
        // to mines, then select random cells to remove mines from.
        else if (mines > 0.5 * rows * cols) {
            for (int r = 0; r < field.length; r++) {
                for (int c = 0; c < field[r].length; c++) {
                    field[r][c] = -1;
                }
            }

            int minesRemoved = 0;
            while (minesRemoved < rows * cols - mines) {
                Point random = getRandomCell(field);
                while (field[random.x][random.y] != -1) {
                    random = getRandomCell(field);
                }

                field[random.x][random.y] = 0;
                minesRemoved++;
            }
        }
        // If mines take up less than half the field, select random cells to
        // have mines.
        else {
            int minesPlaced = 0;
            while (minesPlaced < mines) {
                Point random = getRandomCell(field);
                while (field[random.x][random.y] == -1) {
                    random = getRandomCell(field);
                }

                field[random.x][random.y] = -1;
                minesPlaced++;
            }
        }

        setHint(field);

        return field;
    }


    /**
     * Given a mine field (2D-integer-array with -1 for mines), set the value 
     * of all non-negative cells to the number of mines they border (0-8).
     * 
     * @param field
     *          the mine field that contains the mines; all other values in this 
     *          field will be changed to reflect the number of adjacent mines
     */
    public static void setHint(int[][] field) {
        int lastRow = field.length - 1;
        for (int r = 0; r < field.length; r++) {
            int lastCol = field[r].length - 1;
            for (int c = 0; c < field[r].length; c++) {
                // Don't set a hint if the cell is a mine.
                if (field[r][c] == -1) {
                    continue;
                }

                // Otherwise, count adjacent mines.
                int count = 0;
                if (r != 0 && field[r - 1][c] == -1) {
                    count++;
                }
                if (r != 0 && c != lastCol && field[r - 1][c + 1] == -1) {
                    count++;
                }
                if (c != lastCol && field[r][c + 1] == -1) {
                    count++;
                }
                if (r != lastRow && c != lastCol && field[r + 1][c + 1] == -1) {
                    count++;
                }
                if (r != lastRow && field[r + 1][c] == -1) {
                    count++;
                }
                if (r != lastRow && c != 0 && field[r + 1][c - 1] == -1) {
                    count++;
                }
                if (c != 0 && field[r][c - 1] == -1) {
                    count++;
                }
                if (r != 0 && c != 0 && field[r - 1][c - 1] == -1) {
                    count++;
                }

                // Set the value of the cell to the number of adjacent mines.
                field[r][c] = count;
            }
        }
    }


    /**
     * Expose cell exposes specified cell. If a mine, returns false (i.e., we
     * dead); if a hint, returns true; if zero, then exposes all neighbors until
     * hints are exposed.
     *
     * @param row
     *          cell row
     * @param col
     *          cell column
     * @param field
     *          the mine field
     * @param exposed
     *          2D array with whether a cell is exposed. exposed[row][col]
     *          should be set to true in this method.
     * @return false if we hit a mine, true otherwise
     */
    public static
        boolean
        exposeCell(int row, int col, int[][] field, boolean[][] exposed) {

        if (field == null) {
            return false;
        }

        exposed[row][col] = true;
        if (field[row][col] < 0) {
            // Stop recursion if mine
            return false;
        }

        if (field[row][col] > 0) {
            // Stop recursion if neighboring a mine
            return true;
        }

        // Must be zero
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue; // same cell
                }
                // Point cell = new Point(row + i, col + j);
                int curRow = row + i;
                int curCol = col + j;
                if (curRow >= 0 && curRow < field.length) {
                    if (curCol >= 0 && curCol < field[0].length) {
                        // valid cell
                        if (!exposed[curRow][curCol]) {
                            // Make recursive call if and only if
                            // the cell is not already exposed
                            exposeCell(curRow, curCol, field, exposed);
                        }
                    }

                }
            }
        }

        return true;

    }


    /**
     * Returns a single string with the contents of the exposed array
     * 
     * @param exposed
     *          array indicating whether each cell is exposed
     * @return a string expression of the values in Exposed. For example, a 2x3
     *         array with all True values except (1,1) would return "T T T\nT F
     *         T\n"
     */
    public static String exposedToString(boolean[][] exposed) {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < exposed.length; r++) {
            for (int c = 0; c < exposed[r].length; c++) {
                if (exposed[r][c]) {
                    sb.append("T ");
                }
                else {
                    sb.append("F ");
                }
            }
            sb.append( System.lineSeparator() );
        }

        return sb.toString();
    }


    /**
     * Returns a single string with the contents of field. Each number should be 
     * formatted to take up 2 characters, and there should be a space around each
     * value.
     * 
     * @param field
     *          the mine field
     * @return Returns a string representation of the field array. For example,
     *         a 2x3 array with a bomb at 0,1 would return " 1 -1 1\n1 1 1\n"
     */
    public static String fieldToString(int[][] field) {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field[r].length; c++) {
                sb.append( String.format(" %2d ", field[r][c]) );
            }
            sb.append( System.lineSeparator() );
        }

        return sb.toString();
    }


    /**
     * Return a string representation of the mine field given 2D-arrays for the 
     * the mine field (integer-array) and which cells have been exposed in the 
     * field (boolean-array). 
     * 
     * Each row is on a separate line. Each cell is formatted to take up 4 
     * characters, with space around each cell.
     * 
     * The content of each cell will be:
     * <ul>
     *      <li> ' * ' if not exposed </li>
     *      <li> ' -1 ' if mine </li>
     *      <li> ' # ' where # is the number of adjacent mines </li>
     * </ul>
     * @param field
     *           the mine field
     * @param exposed
     *           a 2D-boolean-array indicating whether each cell has been exposed.
     * @return single string with each cell represented as * (if still hidden),
     *         -1 (if a mine), or n (a number indicating how many mines are
     *         around each cell.
     */
    public static String showBoard(int[][] field, boolean[][] exposed) {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field[r].length; c++) {
                // If the cell hasn't been exposed, it should be drawn as a '*'.
                if (!exposed[r][c]) {
                    sb.append( String.format(" %4s ", "*") );
                }
                else {
                    // If the cell equals zero, display a blank space.
                    if (field[r][c] == 0) {
                        sb.append( String.format(" %4s ", " ") );
                    }
                    // Otherwise display the number of the cell.
                    else {
                        sb.append( String.format(" %4d ", field[r][c]) );
                    }
                }
            }
            sb.append( System.lineSeparator() );
        }

        return sb.toString();
    }


    /**
     * Return true if the arrays indicate a win, false otherwise. A player wins
     * if they have exposed all non-mine cells.
     * 
     * @param field
     *            the mine field
     * @param exposed
     *            the cells that are exposed
     * @return true for a win, false otherwise.
     */
    public static boolean won(int[][] field, boolean[][] exposed) {
        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field[r].length; c++) {
                // If there is a cell that isn't a mine hasn't been exposed,
                // the player has not won yet.
                if (field[r][c] != -1 && !exposed[r][c]) {
                    return false;
                }
                // If the player exposes a mine, they lose.
                if (field[r][c] == -1 && exposed[r][c]) {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Helper method prints the mine field every time the player guesses a cell. 
     * If the player is in debug mode, this method also prints a version of the 
     * mine field with all values exposed.
     * 
     * @param field
     *            the mine field to be printed
     * @param exposed
     *            boolean array that indicates which cells have been exposed.
     *            Needed to call showBoard.
     * @param debugOn
     *            if true, print the mine field and all its values
     */
    private static void printBoards(int[][] field, boolean[][] exposed, boolean debugOn) {
        System.out.println("\nBoard:\n" + showBoard(field, exposed));
        if (debugOn) {
            System.out.println(fieldToString(field));
        }
    }


    /**
     * Helper method checks when the user inputs a 'q', and exits the program if 
     * so.
     * 
     * @param input
     *            a string that the user inputs
     */
    private static void checkQuitKey(String input) {
        if (input.contentEquals("q")) {
            System.exit(0);
        }
    }


    /**
     * Used to run the game.
     * 
     * @param args
     *            - not used this time. But debug mode would be an excellent use
     *            for this parameter. We will cover args later in the semester.
     */
    public static void main(String[] args) {
        // Scanner for user input.
        Scanner kbd = new Scanner(System.in);
        // exposed tracks which cells have been exposed.
        // True if the cell is exposed, false otherwise.
        boolean[][] exposed;
        // field is the game board. mines are -1. other values represent
        // the number of (eight connected) neighboring mines
        int[][] field;
        // debugOn indicates whether the player is playing the game normally or
        // in debug mode. In debug mode, the player can see the value of every
        // cell and the exposed array.
        boolean debugOn = false;
        // The number of rows, columns, and mines in the field.
        int rows = 0;
        int cols = 0;
        int mines = 0;

        System.out.println(
            "This program plays MineField. Enter 'q' at any time to quit.");

        // Ask the user if they want to play in debug mode.
        String choice = "a";
        String validChoices = "NnYy";
        while (validChoices.indexOf(choice) == -1) {
            System.out.print("Do you want to play in debug mode? (Y/n) => ");
            choice = kbd.nextLine().toLowerCase();
            checkQuitKey(choice);
            if (choice.contentEquals("y")) {
                debugOn = true;
            }
            else if (choice.contentEquals("n")) {
                debugOn = false;
            }
            else {
                System.out.println("Please enter y or n.");
            }
        }

        // Ask the user how they want the field configured.
        // Ask for rows and columns first.
        while (rows == 0 || cols == 0) {
            System.out.print("Enter number of rows then columns (ex: 4 4)=> ");
            try {
                rows = kbd.nextInt();
                cols = kbd.nextInt();

                if (rows <= 0 || cols <= 0) {
                    System.out
                        .println("Please enter two valid non-zero numbers.");
                }
            }
            catch (InputMismatchException e) {
                String invalid = kbd.nextLine().toLowerCase();
                checkQuitKey(invalid);

                System.out.println("Please enter two valid non-zero numbers.");
            }
        }
        // Ask how many mines the user wants to play with.
        while (mines == 0) {
            System.out.print("Enter number of mines=> ");
            try {
                mines = kbd.nextInt();

                // Can't play with zero or too many mines!
                if (mines <= 0) {
                    System.out.println("Please enter a valid non-zero number.");
                }
                else if (rows * cols > 1 && mines >= rows * cols) {
                    System.out.println(
                        "Too many mines! Must be less than " + (rows * cols)
                            + ".");
                    mines = 0;
                }
            }
            catch (InputMismatchException e) {
                String invalid = kbd.nextLine().toLowerCase();
                checkQuitKey(invalid);

                System.out.println("Please enter a valid non-zero number.");
            }
        }

        // Set up the mine field and exposed.
        exposed = setUpExposed(rows, cols);
        field = createMineField(rows, cols, mines);

        printBoards(field, exposed, debugOn);

        // Play the game until the player wins (or loses)!
        while (!won(field, exposed)) {
            // Ask the user to enter a valid row and column.
            int row = -1;
            int col = -1;
            while (row == -1 || col == -1) {
                System.out.print(
                    "Enter a row and column value "
                        + "(0 0 is top left corner) => ");
                try {
                    row = kbd.nextInt();
                    col = kbd.nextInt();

                    if (row < 0 || col < 0 || row > field.length
                        || col > field[0].length) {
                        System.out.println("Please enter valid coordinates.");
                        row = -1;
                        col = -1;
                    }
                }
                catch (InputMismatchException e) {
                    String invalid = kbd.nextLine().toLowerCase();
                    checkQuitKey(invalid);

                    System.out.println("Please enter valid coordinates.");
                }

            }

            // Check when the player hits a mine.
            if (!exposeCell(row, col, field, exposed)) {
                System.out.println(fieldToString(field) + "You lose. Sorry.");
                // System.exit leave the program immediately. 0 means that the
                // program exited without an error code.
                System.exit(0);
            }
            printBoards(field, exposed, debugOn);
        }
        kbd.close();
        if (won(field, exposed)) {
            System.out.println(fieldToString(field) + "You won!!!");
        }
        System.out.println("Thank you for playing.");
    }

}
