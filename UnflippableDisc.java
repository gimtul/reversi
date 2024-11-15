public class UnflippableDisc implements Disc {

    private Player player;

    public UnflippableDisc(Player currentPlayer) {
        this.player = currentPlayer;
    }

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
        return "Unflippedable Disc";
    }
}
