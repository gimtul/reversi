public class HumanPlayer extends Player {
    public HumanPlayer(boolean b) {
        super(b);
    }

    @Override
    boolean isHuman() {//is player 1
        return true;
    }

    public Move makeMove(PlayableLogic gameStatus) {
        return null;
    }
}
