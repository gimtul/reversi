public class BombDisc implements Disc {

    private Player player;


    public BombDisc(Player currentPlayer) {
        this.player = currentPlayer;
    }

    //returns the owner of the disc
    @Override
    public Player getOwner() {
        return this.player;
    }

    //sets the owner of the disc
    @Override
    public void setOwner(Player player) {
        this.player = player;
    }

    //returns the type of the disc
    @Override
    public String getType() {
        return "💣";
    }
}
