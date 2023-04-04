# MineSweep
A java project for a classic **Mine Sweep Game**.

## Project Files
This Project contains MTile class, Mine class, MBoard class, GameGUI class, and MGame class.<br />
The **Mtile** class represents a tile on the board.<br />
The **Mine** class represents a mine on the board.<br />
The **MBoard** class represents a Full Mine Sweep board.<br />
The **Game GUI** class builds the Graphical User Interface for the game.<br />
The **MGame** class runs the game.<br />
I have also included 3 screenshots of the GUI of the game. A screenshot of the game when it is started (MineSweepStart). A screenshot of the game when you loss the game (MineSweepLoss). A screenshot of the game when you win (MineSweepWin). 

## Mine Sweep Game Definition
Mine Sweep is a game where the player has to clear a minefield without detonating any hidden mines. 
The game board is divided into a grid of tiles, and some of these tiles contain mines that must be avoided.

The player starts by designating a tile to overturn and the number revealed indicates the number of mines in the adjacent tiles. If a tile is safe, the player can keep clicking on adjacent tiles until all non-mine tiles are revealed. However, if a tile containing a mine is clicked, the game is lost.

To help the player, flags can be placed on tiles believed to contain mines to prevent accidentally clicking on them. The game is won when all non-mine tiles have been cleared.

The game can be run with 3 board sizes: small(10 x 10 grid; 14 mines), medium(14 x 14 grid; 35 mines) and large(19 x 19 grid; 75 mines).
