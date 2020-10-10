import java.awt.Point;
import java.util.Random;
import java.util.Scanner;

/**
 * Class to define MineField for MineSweeper game
 *
 * @author Ben Wright
 * @version 2020.10.02
 */

public class MineField {

    // private means cannot be viewed outside class, but any method in class can
    // use this.
    /** Private instance of random number generator */
    private static Random rand = new Random();

    /**
     * Helper method to create random point within array bounds. A Point object 
     * is an easy way to store x, y values in one object.
     * 
     * Use the rand object declared above for this method. (see
     * https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/java/awt/Point.html)
     * To declare a Point, and make the x value be 3 and the y value be 4 (3, 4),
     * and then return that value, use:
     * 
     * @formatter:off
     * 
     *                <pre>
     * Point location = new Point()
     * location.x = 3;
     * location.y = 4;
     * return location;
     *                </pre>
     * 
     * @formatter:on
     * @param field
     *            the MineField from which a random point should be chosen
     * @return random location within array
     */
    public static Point getRandomCell(int[][] field) {
        double dx = Math.random() * field.length;
        int x = (int)dx;
        double dy = Math.random() * field[0].length;
        int y = (int)dy;

        return new Point(x, y);
    }


    /**
     * setUpExposed creates a 2d array of false values of size rows x cols
     * 
     * @param rows
     *            number of rows in the mine field
     * @param cols
     *            number of columns in the mine field
     * @return a 2d boolean array (size rows * cols), all equal to false
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
     * createMineField creates a 2d array of size rows x cols with the game
     * setup. Cells will have the following values: (a) -1 if the cell is a mine
     * (b) 0 if the cell is not a mine, and the cell has no mines as neighbors
     * (c) 1..8, 1 for each mine that the current cell touches. This method
     * should call getRandomCell to choose random places for the mines, and
     * setHint to set up the non-mine cells
     * 
     * @param rows
     *            number of rows in the mine field
     * @param cols
     *            number of columns in the mine field
     * @param mines
     *            number of mines in the mine field return a new 2d boolean
     *            array (size rows * cols), all equal to false
     * @return a 2d array of size rows x cols that is the MineField
     */
    // TODO Refactor this!
    public static int[][] createMineField(int rows, int cols, int mines) {
        int[][] field = new int[rows][cols];

        // If mines >= the area of the field, set all cells to mines.
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
     * Given a minefield (-1 for mines) set all non negative cells to count the
     * number of adjacent cells that contain mines (8-connected including
     * diagonal corners) If a cell has a mine (-1 value), then leave it -1. If a
     * cell does not have a mine (0 initially), then calculate the number of
     * mines in its eight neighbors, and write that value. Look at the sample
     * run to see an example of these numbers.
     * 
     * @param field
     *            - the MineField that contains the mines, and will be changed
     *            to contain hints
     */
    // TODO Refactor this!
    public static void setHint(int[][] field) {
        int lastRow = field.length - 1;
        for (int r = 0; r < field.length; r++) {
            int lastCol = field[r].length - 1;
            for (int c = 0; c < field[r].length; c++) {
                // Don't set a hint if the cell is a mine (=-1).
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
     * Expose cell exposes specified cell. If a mine, returns false (i.e. we
     * dead); if a hint, returns true; if zero, then exposes all neighbors until
     * hints are exposed. This method is written for you. **Do not modify it!**
     * It solves the problem of exposing neighbor cells by calling exposeCell
     * recursively. We will cover recursion later this semester.
     *
     * @param row
     *            - cell row
     * @param col
     *            - cell column
     * @param field
     *            - the mine field
     * @param exposed
     *            - 2D array with whether a cell is exposed. exposed[row][col]
     *            should be set to true in this method.
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
            // stop recursion if neighboring a mine
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
     *            - array of T/F indicating whether each cell is exposed
     * @return a string expression of the values in Exposed. For example, a 2x3
     *         array with all True values except (1,1) would return "T T T\nT F
     *         T\n"
     */
    public static String exposedToString(boolean[][] exposed) {
        String result = "";

        for (int r = 0; r < exposed.length; r++) {
            for (int c = 0; c < exposed[r].length; c++) {
                if (exposed[r][c]) {
                    result += "T ";
                }
                else {
                    result += "F ";
                }
            }
            result += System.lineSeparator();
        }

        return result;
    }


    /**
     * Returns a single string with the contents of field. Each number should be
     * formatted to take up 2 spaces, and there should be a space around each
     * value.
     * 
     * @param field
     *            the mine field
     * @return Returns a string representation of the field array. For example,
     *         a 2x3 array with a bomb at 0,1 would return " 1 -1 1\n1 1 1\n"
     */
    public static String fieldToString(int[][] field) {
        String result = "";

        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field[r].length; c++) {
                result += String.format(" %2d ", field[r][c]);
            }
            result += System.lineSeparator();
        }

        return result;
    }


    /**
     * showBoard with each row in separate line, newline between. Use 4
     * characters per cell ' * ' if not exposed ' -1 ' if mine ' # ' where # is
     * hint - the number of adjacent mines
     * 
     * @param field
     *            - the minefield
     * @param exposed
     *            - a 2d array indicating whether each cell has been exposed.
     * @return single string with each cell represented as * (if still hidden),
     *         -1 (if a mine), or n (a number indicating how many mines are
     *         around each cell.
     */
    public static String showBoard(int[][] field, boolean[][] exposed) {
        String board = "";

        for (int r = 0; r < field.length; r++) {
            for (int c = 0; c < field[r].length; c++) {
                // If the cell hasn't been exposed, it should be drawn as a '*'.
                if (!exposed[r][c]) {
                    board += String.format(" %4s ", "*");
                }
                else {
                    // If the cell equals zero, display a blank space.
                    if (field[r][c] == 0) {
                        board += String.format(" %4s ", " ");
                    }
                    // Otherwise display the number of the cell.
                    else {
                        board += String.format(" %4d ", field[r][c]);
                    }
                }
            }
            board += System.lineSeparator();
        }

        return board;
    }


    /**
     * Return true if the arrays indicate a win, false otherwise. A player wins
     * if they have exposed all and only values that are not bombs.
     * 
     * @param field
     *            the minefield
     * @param exposed
     *            the cells that are exposed
     * @return true for a win, false otherwise.
     */
    public static boolean won(int[][] field, boolean[][] exposed) {
        // @TODO - fix this
        return false;
    }


    /**
     * main is correct as written, but does not have a debug option. You should
     * add that. That will require adding code in main. Do not change anything
     * else. Do not use nextLine() here or anywhere in the program. If you need
     * to read a String, use next().
     * 
     * @param args
     *            - not used this time. But debug mode would be an excellent use
     *            for this parameter. We will cover args later in the semester.
     */
    public static void main(String[] args) {
        Scanner kbd = new Scanner(System.in);
        // exposed is T if the cell is exposed. False otherwise.
        boolean[][] exposed;
        // field is the game board. mines are -1. other values represent
        // the number of (eight connected) neighboring mines
        int[][] field;
        System.out.println("This program plays MineField. To start,");
        System.out.print("Enter number of rows then columns=> ");
        int rows = kbd.nextInt();
        int cols = kbd.nextInt();
        System.out.print("Enter number of mines=> ");
        int mines = kbd.nextInt();
        exposed = setUpExposed(rows, cols);
        field = createMineField(rows, cols, mines);
        System.out.println(showBoard(field, exposed));
        while (!won(field, exposed)) {
            System.out.print(
                "Enter a row and column value"
                    + "(0 0 is top left corner) => ");
            int row = kbd.nextInt();
            int col = kbd.nextInt();
            if (!exposeCell(row, col, field, exposed)) {
                System.out.println(fieldToString(field) + "You lose. sorry.");
                // System.exit leave the program immediately. 0 means that the
                // program exited without an error code.
                System.exit(0);
            }
            System.out.println("\nBoard:\n" + showBoard(field, exposed));
        }
        kbd.close();
        if (won(field, exposed)) {
            System.out.println(fieldToString(field) + "You won!!!");
        }
        System.out.println("Thank you for playing.");
    }

}
