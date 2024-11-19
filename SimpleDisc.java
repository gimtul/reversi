public class SimpleDisc implements Disc {

    private Player player;
    private boolean wasFlipped = false;

    public SimpleDisc(Player currentPlayer) {
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
        return "⬤";
    }

    public boolean getFlipped() {
        return this.wasFlipped;
    }

    public void setFlipped() {
        this.wasFlipped = true;
    }

}
