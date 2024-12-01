public class UnflippableDisc implements Disc {

    private Player player;

    //returns the current player
    public UnflippableDisc(Player currentPlayer) {
        this.player = currentPlayer;
    }

    //returns the owner of the disc
    @Override
    public Player getOwner() {
        return this.player;
    }

    @Override
    public void setOwner(Player player) {
        this.player = player;
    }

    @Override
    public String getType() {
        return "⭕";
    }
}
