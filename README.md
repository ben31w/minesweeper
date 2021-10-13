This project is a command-line minesweeper game.

## Basic rules
In this game, the user creates a mine field with a specified number of rows and columns. 
Random cells in this field will have mines. All other cells are hint cells that reveal the number of adjacent mines.
The user must guess cells until all hint cells are exposed. When this happens, the user wins!
But if the user guesses a cell and it's revealed to be a mine, they explode.

## How to play this version
To play, run the file ```MineField.java``` (I prefer doing this from the command-line).

Before the game begins, the user must specify three things:
<ol>
	<li>Whether to play in debug mode (Y/n)</li>
	<li>The size of the mine field in (row column) format (e.g., "4 4" would specify a 4x4 grid)</li>
	<li>The number of mines that will be placed in the mine field (positive integer)</li>
</ol>

In debug mode, the user can see a copy of the mine field with all the cells revealed (including mines) whenever they guess a cell.
To play the game traditionally, enter "n" when the program asks the debug question.

Once the user has specified the layout of the mine field, it's time to guess cells. The user guesses cells by giving (row column) coordinates. The row and column values are separated by a space, and "0 0" is the upper left corner of the grid.
Once the user guesses a cell, it is revealed on the mine field. If it's a mine (represented by a value of -1), then the user loses.
The user keeps guessing until they hit a mine or they win by revealing all non-mine cells.
