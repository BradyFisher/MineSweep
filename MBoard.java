import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MBoard {
    private int num_rows; //indicates number of rows the Board has
    private int num_columns; //indicates number of columns the Board has
    private int num_mines; //indicates number of Mine objects the Board has
    //private Mine[] mine; //array of all the Mine objects associated with the Board object
    private MTile[][] board; //2-dimensional array of MTile objects required to represent the Board
    private boolean gameOver=false;
    private int num_hidden;
    private int num_minesLeft;
    private int gameOverType = 0;
    private boolean debugMode; //flag indicating if the Board should be printed in debugMode


    // Constructor for the Board class, it assigns the correct number of mines, initializes the board as a 2-dimensional array of MTiles,
    //initializes mine into an array of Mines, and appropriately places the Mines and adds them to the board's mines
    public MBoard(String size, boolean debugMode){

        //determines the correct number of mines for the given rows and columns.

        if (size.toLowerCase().equals("small")|| size.toLowerCase().equals("s")){
            this.num_mines = 14;
            this.num_minesLeft = 14;
            this.num_rows = 10;
            this.num_columns = 10;
        } else if (size.toLowerCase().equals("medium")|| size.toLowerCase().equals("m")){
            this.num_mines = 35;
            this.num_minesLeft = 35;
            this.num_rows = 14;
            this.num_columns = 14;
        } else if (size.toLowerCase().equals("large")|| size.toLowerCase().equals("l")){
            this.num_mines = 75;
            this.num_minesLeft = 75;
            this.num_rows = 19;
            this.num_columns = 19;
        }
        this.num_hidden = (num_rows * num_columns);

        this.debugMode = debugMode;

        //initializing the mine and board arrays
        this.board = new MTile[num_rows][num_columns];
        //Setting the status of every MTile on the board to ' ', to start with a board of no guesses or mines
        for (int row = 0; row < num_rows; row++) {
            for (int col = 0; col < num_columns; col++) {
                board[row][col] = new MTile(row, col, 0, false, ' ');
            }
        }

        //Placing Mines
        int minesPlaced = 0; //Number of mines currently placed
        while (minesPlaced < this.num_mines) {

            boolean openSpace = true;
            int r; //row position
            int c; //column position
            r = (int) Math.floor(Math.random() * num_rows);
            c = (int) Math.floor(Math.random() * num_columns);
            Mine mine1 = new Mine(r, c);//potential mine to place
            // initializes MTile objects with the right row, column, and status for the mine
            //Also checks if the position the mine takes already has a mine
            mine1.set_space(new MTile(r, c, 0,true,' '));
            if (board[r][c].get_mine()) {
                openSpace = false;
            }


            //If the spaces the new mine takes are open, change the status of those positions on the board to show it contains a mine
            if (openSpace) {
                board[r][c].set_mine();
                minesPlaced++;
            }
        }

        //Count adjacent mines for each MTile
        for (int row = 0; row < num_rows; row++) {
            for (int col = 0; col < num_columns; col++) {
                int count = 0;
                if(row != 0){
                    if(board[row-1][col].get_mine()){
                        count++;
                    }

                    if(col !=  0){
                        if(board[row-1][col-1].get_mine()){
                            count++;
                        }
                    }
                    if(col !=  num_columns-1){
                        if(board[row-1][col+1].get_mine()){
                            count++;
                        }
                    }
                }
                if(row != num_rows-1){
                    if(board[row+1][col].get_mine()){
                        count++;
                    }

                    if(col !=  0){
                        if(board[row+1][col-1].get_mine()){
                            count++;
                        }
                    }
                    if(col !=  num_columns-1){
                        if(board[row+1][col+1].get_mine()){
                            count++;
                        }
                    }
                }
                if(col != 0){
                    if(board[row][col-1].get_mine()){
                        count++;
                    }
                }
                if(col != num_columns-1){
                    if(board[row][col+1].get_mine()){
                        count++;
                    }
                }
                board[row][col].set_num(count);
            }
        }


    }

    //Obscures a character if the game is not being played in debug mode
    private char debug(boolean debugMode, char c){
        if(debugMode){
            return c;
        }
        else{
            switch(c){
                case 'X':
                    c = 'X';
                    break;
                case 'M':
                    c = 'M';
                    break;
                case '0':
                    c = '0';
                    break;
                case '1':
                    c = '1';
                    break;
                case '2':
                    c = '2';
                    break;
                case '3':
                    c = '3';
                    break;
                case '4':
                    c = '4';
                    break;
                case '5':
                    c = '5';
                    break;
                case '6':
                    c = '6';
                    break;
                case '7':
                    c = '7';
                    break;
                case '8':
                    c = '8';
                    break;
                default:
                    c = ' ';
                    break;
            }
            return c;
        }
    }
    public int size() {
        return num_rows * num_columns;
    }

    public MTile MTileAt(int k, int l) {
        return board[k][l];
    }

    //Prints a Board object in a way that makes sense to the player
    public String toString(){

        String boardString = "\t";
        for (int j = 0; j < num_columns-1; j++){
            boardString += j + " |" + "\t";
        }

        boardString += num_columns-1;

        for(int i = 0; i < num_rows; i++){
            boardString+= "\n" + i + "\t";
            for (int j = 0; j < num_columns; j++){
                boardString += debug(debugMode, board[i][j].get_status()) + "\t";
            }
        }

        boardString += "\n";
        return boardString;
    }

    //Calculates the number of hidden tiles
    public void noTilesLeft(){
        if(num_mines == num_hidden){
            this.gameOverType = 2;
            this.gameOver = true;
            //System.out.println("Congratulations, you won!!");
        }

    }


    public boolean firstGood(int r, int c){
        if(board[r][c].get_num()==0 && !board[r][c].get_mine()){
            return true;
        }
        else{
            return false;
        }

    }

    public int get_gameOverType(){
        return this.gameOverType;
    }

    public int get_num_minesLeft(){
        return this.num_minesLeft;
    }

    public int get_num_rows(){
        return this.num_rows;
    }

    public int get_num_columns(){
        return this.num_columns;
    }

    //Returns an int based on the guess with 0-> A Guess out of the Bounds of the board
    //1-> a clear space, 2-> a mine hit, 3-> a repeated guess
    //Checks the result of the guess and changes the statuses of the cells if necessary
    public int guess(int r, int c, boolean first){
        char[] numberTiles = {'1', '2', '3', '4', '5', '6', '7', '8'};
        //Checks if the Guess is out of the bounds of the Board
        if ( (r < 0) || (r > (this.num_rows - 1)) || (c < 0) || (c > (this.num_columns - 1))){
            return 0;
            //"Out of Bounds";
        }
        //Checks if the status of the guess position on the board is empty, resulting in a clear space
        //Changes the status of the cell at that position to an adjacent mine count
        else if (!board[r][c].get_mine() && board[r][c].get_hidden()){
            board[r][c].searched();
            if(board[r][c].get_num() == 0){
                guess(r-1,c-1, false);
                guess(r-1,c, false);
                guess(r-1,c+1, false);
                guess(r,c-1, false);
                guess(r,c+1, false);
                guess(r+1,c-1, false);
                guess(r+1,c, false);
                guess(r+1,c+1, false);
            }
            num_hidden--;
            this.noTilesLeft();
            return 1;
            //"Clear Space";
        }

        else if(board[r][c].get_status() == 'X'){
            //System.out.println("Given Coordinate is currently has a flag, need to remove the flag before uncovering the position");
            return 2;
        }
        //Checks if the status of the guess position on the board is a mine
        //Changes the status of the board and mine at that position to a hit
        else if(board[r][c].get_mine() && !first){
            board[r][c].set_status('M');
            this.gameOverType = 1;
            this.gameOver = true;

            return 3;
            //"Hit";
        }
        else {
            return 4;
            //"Penalty: Redundant Guess";
        }
    }
    //Checks if the tile is overturned and a number tile. Then overturns all adjacent tiles.
    public boolean guessUnhidden(int r, int c){
        char[] numberTiles = {'1', '2', '3', '4', '5', '6', '7', '8'};
        if(searchArray(board[r][c].get_status(),numberTiles )){
            if(r >= 1 && c>=1 && board[r-1][c-1].get_status() == ' '){
                guess(r-1,c-1, false);
            }
            if(r >= 1 && board[r-1][c].get_status() == ' '){
                guess(r-1,c, false);
            }
            if(r >= 1 && c < this.num_columns - 1 &&  board[r-1][c+1].get_status() == ' '){
                guess(r-1,c+1, false);
            }
            if(c>=1 && board[r][c-1].get_status() == ' '){
                guess(r,c-1, false);
            }
            if(c < this.num_columns - 1 && board[r][c+1].get_status() == ' '){
                guess(r,c+1, false);
            }
            if(r < this.num_rows - 1 && c>=1 && board[r+1][c-1].get_status() == ' '){
                guess(r+1,c-1, false);
            }
            if(r < this.num_rows - 1 && board[r+1][c].get_status() == ' '){
                guess(r+1,c, false);
            }
            if(r < this.num_rows - 1 && c < this.num_columns - 1 && board[r+1][c+1].get_status() == ' '){
                guess(r+1,c+1, false);
            }
            return true;
        }else{return false;}
    }


    public void flagMine(int r, int c){
        //Checks if the flag is out of the bounds of the Board
        if ( (r < 0) || (r > (this.num_rows - 1)) || (c < 0) || (c > (this.num_columns - 1))){
            //System.out.println("Out of Bounds Flag");
            //"Penalty: Out of Bounds";
        }
        else{
            board[r][c].set_status('X');
            num_minesLeft--;

        }
    }

    public void unflagMine(int r, int c){
        //Checks if the flag is out of the bounds of the Board
        if ( (r < 0) || (r > (this.num_rows - 1)) || (c < 0) || (c > (this.num_columns - 1))){
            //System.out.println("Out of Bounds Flag");
            //"Penalty: Out of Bounds";
        }
        else if(board[r][c].get_status()== 'X'){
            board[r][c].set_status(' ');
            num_minesLeft++;
        }
        else{
            //System.out.println("The given coordinate is not a flag");
        }
    }

    public boolean get_gameOver(){
        return gameOver;
    }

    public void solver(){
        boolean moveHappened = false;
        for (int row = 0; row < num_rows; row++) {
            for (int col = 0; col < num_columns; col++) {
                if(!board[row][col].get_hidden()){



                    int countAdjFlag = 0;
                    int countAdjHidden = 0;
                    if(row != 0){
                        if(board[row-1][col].get_hidden()){
                            countAdjHidden++;
                        }
                        if(board[row-1][col].get_status()=='X'){
                            countAdjFlag++;
                        }

                        if(col !=  0){
                            if(board[row-1][col-1].get_hidden()){
                                countAdjHidden++;
                            }
                            if(board[row-1][col-1].get_status()=='X'){
                                countAdjFlag++;
                            }
                        }
                        if(col !=  num_columns-1){
                            if(board[row-1][col+1].get_hidden()){
                                countAdjHidden++;
                            }
                            if(board[row-1][col+1].get_status()=='X'){
                                countAdjFlag++;
                            }
                        }
                    }
                    if(row != num_rows-1){
                        if(board[row+1][col].get_hidden()){
                            countAdjHidden++;
                        }
                        if(board[row+1][col].get_status()=='X'){
                            countAdjFlag++;
                        }
                        if(col !=  0){
                            if(board[row+1][col-1].get_hidden()){
                                countAdjHidden++;
                            }
                            if(board[row+1][col-1].get_status()=='X'){
                                countAdjFlag++;
                            }
                        }
                        if(col !=  num_columns-1){
                            if(board[row+1][col+1].get_hidden()){
                                countAdjHidden++;
                            }
                            if(board[row+1][col+1].get_status()=='X'){
                                countAdjFlag++;
                            }
                        }
                    }
                    if(col != 0){
                        if(board[row][col-1].get_hidden()){
                            countAdjHidden++;
                        }
                        if(board[row][col-1].get_status()=='X'){
                            countAdjFlag++;
                        }
                    }
                    if(col != num_columns-1){
                        if(board[row][col+1].get_hidden()){
                            countAdjHidden++;
                        }
                        if(board[row][col+1].get_status()=='X'){
                            countAdjFlag++;
                        }
                    }







                    if(board[row][col].get_num() == countAdjHidden && countAdjHidden != countAdjFlag){
                        //System.out.println("solver 1");
                        moveHappened = true;
                        if(row-1 >= 0 && col-1 >= 0 && board[row-1][col-1].get_hidden() && board[row-1][col-1].get_status()!= 'X') {
                            flagMine(row-1,col-1);
                        }
                        if(row-1 >= 0 && board[row-1][col].get_hidden() && board[row-1][col].get_status()!= 'X') {
                            flagMine(row-1,col);
                        }
                        if(row-1 >= 0 && col+1 < num_columns && board[row-1][col+1].get_hidden() && board[row-1][col+1].get_status()!= 'X') {
                            flagMine(row-1,col+1);
                        }
                        if(col-1 >= 0 && board[row][col-1].get_hidden() && board[row][col-1].get_status()!= 'X') {
                            flagMine(row,col-1);
                        }
                        if(col+1 < num_columns && board[row][col+1].get_hidden() && board[row][col+1].get_status()!= 'X') {
                            flagMine(row,col+1);
                        }
                        if(row+1 < num_rows && col-1 >= 0 && board[row+1][col-1].get_hidden() && board[row+1][col-1].get_status()!= 'X') {
                            flagMine(row+1,col-1);
                        }
                        if(row+1 < num_rows && board[row+1][col].get_hidden() && board[row+1][col].get_status()!= 'X') {
                            flagMine(row+1,col);
                        }
                        if(row+1 < num_rows && col+1 < num_columns &&board[row+1][col+1].get_hidden() && board[row+1][col+1].get_status()!= 'X') {
                            flagMine(row+1,col+1);
                        }
                    }







                    if(board[row][col].get_num() == countAdjFlag && countAdjHidden != countAdjFlag){
                        //System.out.println("solver 2");
                        moveHappened = true;
                        if(row-1 >= 0 && col-1 >= 0 && board[row-1][col-1].get_hidden() && board[row-1][col-1].get_status()!= 'X') {
                            guess(row-1,col-1, false);
                        }
                        if(row-1 >= 0 && board[row-1][col].get_hidden() && board[row-1][col].get_status()!= 'X') {
                            guess(row-1,col, false);
                        }
                        if(row-1 >= 0 && col+1 < num_columns && board[row-1][col+1].get_hidden() && board[row-1][col+1].get_status()!= 'X') {
                            guess(row-1,col+1, false);
                        }
                        if(col-1 >= 0 && board[row][col-1].get_hidden() && board[row][col-1].get_status()!= 'X') {
                            guess(row,col-1, false);
                        }
                        if(col+1 < num_columns && board[row][col+1].get_hidden() && board[row][col+1].get_status()!= 'X') {
                            guess(row,col+1, false);
                        }
                        if(row+1 < num_rows && col-1 >= 0 && board[row+1][col-1].get_hidden() && board[row+1][col-1].get_status()!= 'X') {
                            guess(row+1,col-1, false);
                        }
                        if(row+1 < num_rows && board[row+1][col].get_hidden() && board[row+1][col].get_status()!= 'X') {
                            guess(row+1,col, false);
                        }
                        if(row+1 < num_rows && col+1 < num_columns &&board[row+1][col+1].get_hidden() && board[row+1][col+1].get_status()!= 'X') {
                            guess(row+1,col+1, false);
                        }
                    }




                    if(board[row][col].get_num() == countAdjHidden + countAdjFlag && countAdjHidden != countAdjFlag){
                        //System.out.println("solver 3");

                        moveHappened = true;
                        if(row-1 >= 0 && col-1 >= 0 && board[row-1][col-1].get_hidden() && board[row-1][col-1].get_status()!= 'X') {
                            flagMine(row-1,col-1);
                        }
                        if(row-1 >= 0 && board[row-1][col].get_hidden() && board[row-1][col].get_status()!= 'X') {
                            flagMine(row-1,col);
                        }
                        if(row-1 >= 0 && col+1 < num_columns && board[row-1][col+1].get_hidden() && board[row-1][col+1].get_status()!= 'X') {
                            flagMine(row-1,col+1);
                        }
                        if(col-1 >= 0 && board[row][col-1].get_hidden() && board[row][col-1].get_status()!= 'X') {
                            flagMine(row,col-1);
                        }
                        if(col+1 < num_columns && board[row][col+1].get_hidden() && board[row][col+1].get_status()!= 'X') {
                            flagMine(row,col+1);
                        }
                        if(row+1 < num_rows && col-1 >= 0 && board[row+1][col-1].get_hidden() && board[row+1][col-1].get_status()!= 'X') {
                            flagMine(row+1,col-1);
                        }
                        if(row+1 < num_rows && board[row+1][col].get_hidden() && board[row+1][col].get_status()!= 'X') {
                            flagMine(row+1,col);
                        }
                        if(row+1 < num_rows && col+1 < num_columns &&board[row+1][col+1].get_hidden() && board[row+1][col+1].get_status()!= 'X') {
                            flagMine(row+1,col+1);
                        }
                    }
                }
            }
        }
        if(moveHappened){
            solver();
        }

    }

    //Determines if a given char is in a given list of chars. Returns boolean.
    public boolean searchArray(char status, char[] numberTiles){
        boolean found = false;
        for(char x : numberTiles){
            if(x == status){
                found = true;
                break;
            }
        }
        return found;
    }



}