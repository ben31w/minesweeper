This project begins creation of the ```MineSweeper``` game found on some computers.  Here is one version online: https://cardgames.io/minesweeper/

In this project we will write code that defines creation of the MineField, and handles updating the game.  Later projects will add user interaction.




## Setup
* Fork this project into your private repo from your course group.
* Clone the fork of your repo to your computer
* Create an Eclipse project that points to this repo
  * Be sure to add JUnit4 library

The project defines the ```MineField.java``` class.  Please read the
JavaDoc carefully for descriptions. The Javadoc is in the source code,
and can be viewed in [API format here](https://www.pcs.cnu.edu/~lambert/Classes/CPSC255/P1/MineField.html).

You will be creating a MineField game. By default, the game plays
normally. Use [these sample runs](https://www.pcs.cnu.edu/~lambert/Classes/CPSC255/P1/sample_runs.txt) as your guide for how the program
should play. **It is important to play the game, not just write the
methods.** Therefore, make sure that you complete each method in time
to play the game.

Most of the description for each method is in the javadoc. Here are
additional hints for various methods:
## ```public static int[][] createMineField(int rows, int cols, int mines)```

This method takes three arguments: ```rows, columns```, and
```mines```, and returns a new array that is the MineField. If mines
is <= 0, there will be no mines. If mines is >= rows*columns, all
cells will be mines.

To create this array, first place the mines randomly in the array. -1
indicates that the cell is a mine.

if ```mines > rows*columns``` then set the entire mineField array values to -1 to indicate full of mines.

There can only be one mine per location, so if you randomly choose the same location twice, then you need to re-sample a location.   As this chance of collision increases as the number of mines go up, and may significantly increase runtime, I suggest that if ```mines > 0.5*rows*columns```, you take the opposite approach and initialize all cells as having a mine and randomly choose the appropriate number of cells to set to clear.

After setting the mines,
you need to calculate the hint values for cells that are not
mines. Call setHint to do this.


## ```public Point getRandomCell(int[][] field)```
* A Point is just an easy way of storing 2 values (x and y).
* Look at
Zybooks section 1.6  to determine how to use Random. You should use
the `rand` object created with the line:
``` private static Random rand = new Random();```
This method is the only place that the variable `rand` is used.

## ```boolean exposeCell(int row, int column)```
This method is written for you. Do not change it, but do read the code to figure out how and why it works. You do not need to understand recursion completely, but this is a good example of it.

## ```main```
main is written for you, but you should modify it in the following
way: ask the user if they want to run in debug mode. If the user
enters any value other than a 'Y' or a 'y', the program runs as it
does not. If, however, the user enters 'Y' or a 'y', then  each time the user is asked to enter an x
and a y value, the contents of the field and exposed array are
displayed by calling exposedToString and fieldToString. This will
allow you to play the game knowing the "answers," and it will help you
debug your program. There will be no junit tests for this part. I will
grade it for Phase 3.

### Directions

* Do not change the instance variable names or given method signatures
* You may create additional helper methods if you want
* I've started you with good JavaDoc, but you must add proper JavaDoc for any methods you add.
* Submit to GitLab frequently; at least one commit per method you write.

### This project is due in 3 phases
* Phase 1 (30 pts)- Due 6-October at 11:59PM
	* You get credit for the fraction of JUnit tests passed up to 30 points (e.g. if > 30%, then get 30 points)
* Phase 2 (40 pts) Due 13-October at 11:59PM
	* This is 65% JUnit correctness and 15% proper style and 20% regular contributions to gitlab
* Phase 3 (30 points) - Due 16-October at 11:59
You do not need to turn in any additional code for this. You should
make sure that your program runs as the sample runs do, with the
additional question of "Do you want to run in debug mode" added. There
are no junit tests for this. Just run the program to make sure it
looks like the sample runs. I recommend that you run the program
outside of Eclipse on the command line to see the output better.


## Honor Policy

 Unlike weekly assignments, this project has strict Honor Guidelines

* You may work in collaboration with one partner (or individually at your discretion).
* If you want to work with a partner, choose one yourself
   * Email me and cc your partner prior to Tuesday 22-Sept @ 11:59PM
* You must both submit to your personal GitLab repos.  Either:
   * Work together in one shared repo, and then do a final fork to individual repos
   * Work collaboratively, but making individual code commits to separate repos
* You may freely discuss and share code with your partner
* You may NOT view anyone else's code other than your partner!

* Any discussions with others, must follow the *Empty Hands Rule*
  * You may discuss concepts in abstract, including on paper or white board
  * You must destroy notes/erase prior to coding
  * You must document all discussions with other people
	  * This includes both giving and receiving help
* Code will be checked for plagarism and unauthorized sharing.

## Notes:

* Read the JavaDoc for specific directions for each methods
* Look at the unit tests carefully when debugging for details.
* You do NOT need to write JUnit tests for this.
* Web-cat is picky on style and logic.  You can have correct code that will still lose points in web-cat.  Look at the reports.
  * Web-cat wants braces around single line statements.
