public class MTile {

    private int row; //Expresses the value of the row of the Tile
    private int col; //Expresses the value of the column of the Tile
    private int num; //Expresses the value of the number of adjacent Tiles with mines.
    private boolean hidden; //Expresses whether the tile is hidden.
    private boolean mine; //Expresses whether the tile is a mine.
    private char status; //  'X': Expected Mine, 'M': Mine hit; ' ': unknown; '0'- '8' number of mines on surrounding

    // Getter method for the status attribute, returns a char representing the status
    public char get_status(){
        return this.status;
    }

    // Getter method for the row attribute, returns a int representing the row
    public int get_row(){
        return this.row;
    }

    // Getter method for the column attribute, returns a int representing the column
    public int get_column(){
        return this.col;
    }
    // Setter method for the status attribute, sets status to a given char
    public void set_status(char c){
        this.status = c;
    }

    // Getter method for the num attribute, returns a int representing the number of adjacent mines.
    public int get_num(){
        return this.num;
    }

    // Getter method for the mine attribute, returns a boolean representing whether the tile has a mine.
    public boolean get_mine(){
        return this.mine;
    }

    //Setter method for the mine attribute
    public void set_mine(){
        mine = true;
    }

    public void set_num(int n){
        this.num = n;
    }

    // Getter method for the hidden attribute, returns a boolean stating whether the tile is still hidden.
    public boolean get_hidden(){
        return this.hidden;
    }

    // Setter method for the hidden attribute, changes hidden to false
    public void searched(){
        this.hidden = false;
        if(this.num == 0){
            this.status = '0';
        }
        else if(this.num == 1){
            this.status = '1';
        }
        else if(this.num == 2){
            this.status = '2';
        }
        else if(this.num == 3){
            this.status = '3';
        }
        else if(this.num == 4){
            this.status = '4';
        }
        else if(this.num == 5){
            this.status = '5';
        }
        else if(this.num == 6){
            this.status = '6';
        }
        else if(this.num == 7){
            this.status = '7';
        }
        else if(this.num == 8){
            this.status = '8';
        }
    }



    // Constructor for the MTile class
    public MTile(int row, int col, int n, boolean mine, char status){
        this.row = row;
        this.col = col;
        this.status = status;
        this.num = n;
        this.mine = mine;
        this.hidden = true;
    }
}
