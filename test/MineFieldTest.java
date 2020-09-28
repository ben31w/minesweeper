import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.awt.Point;
import org.junit.Test;

public class MineFieldTest
{
    @Test
    public void getRandomCell()
    {
        Point expected = new Point(0, 0);
        Point actual = MineField.getRandomCell(new int[][] { { 0 } });
        assertEquals(expected, actual);
        int[][] param = { { 0, 0 }, { 0, 0 }, { 0, 0 } };
        actual = MineField.getRandomCell(param);
        assertTrue(actual.x == 0 || actual.x == 1 || actual.x == 2);
        assertTrue(actual.y == 0 || actual.y == 1 || actual.y == 2);
        boolean different = false;
        for (int i = 0; i < 50; i++)
        { // points should be different when randomly chosen
            Point actual2 = MineField.getRandomCell(param);
            if (actual != actual2)
            {
                different = true;
                break;
            }
        }
        assertTrue(
            "getRandomCell should return different values if called repeatedly",
            different);

    }


    private int count(int[][] array, int value)
    {
        int count = 0;
        for (int i = 0; i < array.length; i++)
        {
            for (int j = 0; j < array[0].length; j++)
            {
                if (array[i][j] == value)
                {
                    count++;
                }
            }
        }
        return count;
    }


    @Test
    public void testCreateMineField()
    {
        int[][] field1Actual = MineField.createMineField(1, 1, 1);
        assertArrayEquals(
            "Should be array with one -1",
            new int[][] { { -1 } },
            field1Actual);
        int[][] field2Actual = MineField.createMineField(1, 1, 0);
        assertArrayEquals(
            "Should be array with one 0",
            new int[][] { { 0 } },
            field2Actual);
        int[][] field3Actual = MineField.createMineField(5, 4, 0);
        int[][] field3Expected = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
            { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
        assertArrayEquals(
            "Should be array with all 0s",
            field3Expected,
            field3Actual);
        int[][] field4Actual = MineField.createMineField(3, 43, -1);
        int[][] field4Expected = new int[3][43];
        for (int i = 0; i < field4Expected.length; i++)
        {
            for (int j = 0; j < field4Expected[0].length; j++)
            {
                field4Expected[i][j] = 0;
            }
        }
        assertArrayEquals(
            "Should be array with all 0s",
            field4Expected,
            field4Actual);
        int[][] field6Actual = MineField.createMineField(3, 43, 300);
        int[][] field6Expected = new int[3][43];
        for (int i = 0; i < field4Expected.length; i++)
        {
            for (int j = 0; j < field6Expected[0].length; j++)
            {
                field6Expected[i][j] = -1;
            }
        }
        assertArrayEquals(
            "Should be array with all bombs(-1s)",
            field6Expected,
            field6Actual);

        int[][] field5Actual = MineField.createMineField(5, 6, 5);
        int countNegOnes = count(field5Actual, -1);
        assertEquals(
            "You should have 5 -1s in a 5x6 field with 5 bombs",
            5,
            countNegOnes);
        int[][] field7Actual = MineField.createMineField(5, 6, 28);
        countNegOnes = count(field7Actual, -1);
        assertEquals(
            "You should have 28 -1s in a 5x6 field with 28 bombs",
            28,
            countNegOnes);

    }


    @Test
    public void testShowBoard()
    {
        int[][] field1 = { { 0 } };
        boolean[][] exposed1 = { { false } };
        String str = MineField.showBoard(field1, exposed1);
        assertTrue(str.contains("*"));
        assertFalse(
            "cells not exposed shoudl have a *",
            str.contains("0") || str.contains("#") || str.contains("-1"));
        int[][] field2 = { { 0, 0, 0, -1 }, { 0, 0, 1, 1 }, { 0, 0, 0, 0 } };
        boolean[][] exposed2 = { { true, true, false, true },
            { false, false, true, false }, { true, false, true, false } };
        str = MineField.showBoard(field2, exposed2);
        assertFalse("0s should show as blanks if exposed", str.contains("0"));
        assertTrue(
            "exposed[1][2] is true and field[1][2] is 1",
            str.contains("1"));
        assertTrue("-1 is exposed", str.contains("-1"));
        long countStars = str.chars().filter(ch -> ch == '*').count();
        assertEquals("6 false in exposed should have 6 stars", 6, countStars);
    }


    @Test
    public void testWon()
    {
        int[][] field1 = { {} };
        boolean[][] exposed1 = { {} };
        boolean expectedTrue = true;
        boolean expectedFalse = false;
        boolean actual1 = MineField.won(field1, exposed1);
        assertEquals("Empty array is a win", expectedTrue, actual1);
        int[][] field2 = { { 0 } };
        boolean[][] exposed2 = { { true } };
        boolean actual2 = MineField.won(field2, exposed2);
        assertEquals(
            "All exposed with no bombs is a win",
            expectedTrue,
            actual2);
        int[][] field3 = { { -1 } };
        boolean[][] exposed3 = { { true } };
        boolean actual3 = MineField.won(field3, exposed3);
        assertEquals(
            "All exposed with all bombs is a loss",
            expectedFalse,
            actual3);
        int[][] field4 = { { 0 } };
        boolean[][] exposed4 = { { false } };
        boolean actual4 = MineField.won(field4, exposed4);
        assertEquals("Not exposed is not a win", expectedFalse, actual4);
        int[][] field5 = { { -1 } };
        boolean[][] exposed5 = { { false } };
        boolean actual5 = MineField.won(field5, exposed5);
        assertEquals(
            "All exposed except bombs is a win",
            expectedTrue,
            actual5);

        int[][] field6 = { { 1, 1, 1, 0 }, { 1, -1, 1, 0 } };
        boolean[][] exposed6 =
            { { true, true, true, false }, { true, false, true, false } };
        boolean actual6 = MineField.won(field6, exposed6);
        assertEquals("Not exposed is not a win", expectedFalse, actual6);
        int[][] field7 = { { -1, -1, -1 }, { -1, -1, 3 } };
        boolean[][] exposed7 =
            { { false, false, false }, { false, false, true } };
        boolean actual7 = MineField.won(field7, exposed7);
        assertEquals(
            "All exposed except bombs is a win",
            expectedTrue,
            actual7);
    }


    public void testFieldToString()
    {
        int[][] field = { { 0, 1, 1 }, { 0, 1, -1 }, { 1, 2, 1 }, { -1, 2, 1 },
            { 2, -1, 1 } };
        String str = MineField.fieldToString(field);
        assertTrue("All 0s should be displayed as 0", str.contains("0"));
        assertTrue("All bombs should be displayed as -1", str.contains("-1"));
        assertTrue("All numbers should be displayed", str.contains("2"));
        long countOnes = str.chars().filter(ch -> ch == '1').count();
        long countNegs = str.chars().filter(ch -> ch == '-').count();
        long countTwos = str.chars().filter(ch -> ch == '2').count();
        long countZeros = str.chars().filter(ch -> ch == '0').count();
        assertEquals("There should be 3 -1s", 3, countNegs);
        assertEquals("All 1 and bombs (-1) should be displayed", 10, countOnes);
        assertEquals("There should be 2 0s", 2, countZeros);
        assertEquals("There should be three 2s", 3, countTwos);
    }


    @Test
    public void testExposedToString()
    {
        boolean[][] exposed = { { false, true, false }, { false, false, false },
            { true, false, true }, { true, true, true },
            { false, false, false } };
        String str = MineField.exposedToString(exposed);
        assertTrue("true should be displayed as T", str.contains("T"));
        assertTrue("false should be displayed as F", str.contains("F"));
        long countTs = str.chars().filter(ch -> ch == 'T').count();
        long countFs = str.chars().filter(ch -> ch == 'F').count();
        assertEquals("There should be 9 Fs displayed", 9, countFs);
        assertEquals("There should be 6 Ts displayed", 6, countTs);

    }


    @Test
    public void setHint()
    {
        int[][] field1Actual = { { -1 } };
        int[][] field1Expected = { { -1 } };
        MineField.setHint(field1Actual);
        assertArrayEquals(
            "All bombs field should be unchanged with hints",
            field1Expected,
            field1Actual);
        int[][] field2Actual = { { 0, 0 }, { 0, 0 } };
        int[][] field2Expected = { { 0, 0 }, { 0, 0 } };
        MineField.setHint(field2Actual);
        assertArrayEquals(
            "All 0s field should be unchanged with hints",
            field2Expected,
            field2Actual);
        int[][] field3Actual = { { 0, -1 }, { 0, 0 } };
        int[][] field3Expected = { { 1, -1 }, { 1, 1 } };
        MineField.setHint(field3Actual);
        assertArrayEquals(
            "All surrounding neighbors of bombs should be set to 1",
            field3Expected,
            field3Actual);
        int[][] field4Actual = { { 0, 0, 0 }, { 0, 0, -1 }, { 0, 0, 0 },
            { -1, 0, 0 }, { 0, -1, 0 } };
        int[][] field4Expected = { { 0, 1, 1 }, { 0, 1, -1 }, { 1, 2, 1 },
            { -1, 2, 1 }, { 2, -1, 1 } };
        MineField.setHint(field4Actual);
        assertArrayEquals(
            "If a cell has >1 neighbor with a bomb, it should count all neighbors",
            field4Expected,
            field4Actual);
        int[][] field5Actual =
            { { -1, -1, -1 }, { -1, 0, -1 }, { -1, -1, -1 }, { 0, 0, 0 } };
        int[][] field5Expected =
            { { -1, -1, -1 }, { -1, 8, -1 }, { -1, -1, -1 }, { 2, 3, 2 } };
        MineField.setHint(field5Actual);
        assertArrayEquals(
            "If a cell has >1 neighbor with a bomb, it should count all neighbors",
            field5Expected,
            field5Actual);
    }


    @Test
    public void testExposeCell()
    {
        assertFalse(
            "a null array should return false",
            MineField.exposeCell(0, 0, null, null));
        int[][] field = { { 0, 1, 1 }, { 0, 1, -1 }, { 1, 2, 1 }, { -1, 2, 1 },
            { 2, -1, 1 } };
        int[][] fieldCopy = { { 0, 1, 1 }, { 0, 1, -1 }, { 1, 2, 1 },
            { -1, 2, 1 }, { 2, -1, 1 } };

        boolean[][] exposed = { { false, false, false },
            { false, false, false }, { false, false, false },
            { false, false, false }, { false, false, false } };
        boolean[][] exposedCopy = { { false, false, false },
            { false, false, false }, { false, false, false },
            { false, false, false }, { false, false, false } };
        boolean actual = MineField.exposeCell(0, 1, field, exposed);
        assertTrue(
            "(row,col) should be exposed after this method",
            exposed[0][1]);
        assertArrayEquals(
            "field should not change in exposeCell",
            field,
            fieldCopy);
        exposed[0][1] = false; // change back to that exposed is the same as
                               // before
        assertArrayEquals(
            "Only the exposed cell should change",
            exposed,
            exposedCopy);
        assertTrue("return true for non-bombs", actual);
        actual = MineField.exposeCell(1, 2, field, exposed);
        assertArrayEquals(
            "field should not change in exposeCell",
            field,
            fieldCopy);
        assertTrue(
            "(row,col) should be exposed after this method",
            exposed[1][2]);
        exposed[1][2] = false; // change back
        assertArrayEquals(
            "Exposed should only change where the cell where the bombs is",
            exposed,
            exposedCopy);
        assertFalse("Return false for bombs", actual);
        actual = MineField.exposeCell(2, 2, field, exposed);
        assertTrue(
            "(row,col) should be exposed after this method",
            exposed[2][2]);

        assertArrayEquals(
            "field should not change in exposeCell",
            field,
            fieldCopy);
        assertEquals(exposed[2][2], true);
        exposed[2][2] = false; // change back
        /*
         * for (int i = 0; i < exposed.length; i++) { for (int j = 0; j <
         * exposed[0].length; j++) { System.out.print(exposed[i][j] + " "); }
         * System.out.println("\n"); } System.out.println("copy"); /* for (int i
         * = 0; i < exposedCopy.length; i++) { for (int j = 0; j <
         * exposedCopy[0].length; j++) { System.out.print(exposedCopy[i][j] +
         * " "); } System.out.println("\n"); }
         */
        assertArrayEquals(
            "Exposed should only change where the cell is",
            exposed,
            exposedCopy);
        assertTrue("return true for non-bombs", actual);
        actual = MineField.exposeCell(0, 0, field, exposed);
        assertArrayEquals(
            "field should not change in exposeCell",
            field,
            fieldCopy);
        assertTrue("return true for non-bombs", actual);
        assertEquals(exposed[0][0], true);
        assertEquals(
            "non-bomb neighbors should be exposed",
            exposed[1][0],
            true);
        assertEquals(
            "non-bomb neighbors should be exposed",
            exposed[0][1],
            true);
        assertEquals(
            "non-bomb neighbors should be exposed",
            exposed[1][1],
            true);
        assertEquals(
            "non-bomb neighbors should be exposed",
            exposed[2][0],
            true);
        assertEquals(
            "non-bomb neighbors should be exposed",
            exposed[2][1],
            true);
        exposed[2][1] = false;
        exposed[2][0] = false;
        exposed[1][1] = false;
        exposed[1][0] = false;
        exposed[0][1] = false;
        exposed[0][0] = false;
        assertArrayEquals(
            "Exposed should only change where the cell is",
            exposedCopy,
            exposed);
    }


    @Test
    public void testSetExposed()
    {
        boolean[][] expected1 = {};
        boolean[][] expected5 = { { false } };
        boolean[][] expected2 = { { false, false, false, false, false } };
        boolean[][] expected3 = {};
        boolean[][] expected4 =
            { { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },
                { false, false, false, false, false, false, false, false },

                { false, false, false, false, false, false, false, false } };
        int n1 = 0;
        int n2 = 1;
        int n3 = 5;
        int n4 = 8;
        int n5 = 17;
        boolean[][] actual1 = MineField.setUpExposed(n1, n1);
        boolean[][] actual5 = MineField.setUpExposed(n2, n2);
        boolean[][] actual2 = MineField.setUpExposed(n2, n3);
        boolean[][] actual3 = MineField.setUpExposed(n1, n4);
        boolean[][] actual4 = MineField.setUpExposed(n5, n4);
        assertArrayEquals("First arrays unequal", expected1, actual1);
        assertArrayEquals("fifth arrays unequal", expected5, actual5);
        assertArrayEquals("Second arrays unequal", expected2, actual2);
        assertArrayEquals("Third arrays unequal", expected3, actual3);
        assertArrayEquals("fourth arrays unequal", expected4, actual4);
    }

}
