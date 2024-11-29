public class Move {
    private Position pos;
    private Disc disc;
    public Move(Position p,Disc d){
        this.disc=d;
        this.pos=p;
    }
    public Position position() {
        return pos;
    }

    public Disc disc() {
        return disc;
    }
}