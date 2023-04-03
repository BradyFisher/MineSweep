public class Mine {

    private  MTile space; //array of MTile objects associated with the Mine

    // Constructor for the Mine class, with an array of MTiles
    public Mine(int r, int c){
        this.space = new MTile(r,c, 0, true,' '); //declares the MTile objects associated with the mine
    }

    // Getter method for the space attribute, returns an array of the MTile objects representing the space
    public MTile get_space(){
        return this.space;
    }

    // Setter method for the space attribute, sets space to
    public void set_space(MTile t){
        this.space = t;
    }
}
