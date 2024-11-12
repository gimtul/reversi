import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameLogic implements PlayableLogic {

    private Player player1;

    private Player player2;

    @Override
    public boolean locate_disc(Position a, Disc disc) {
        return true;
    }

    @Override
    public Disc getDiscAtPosition(Position position) {
        return null;
    }

    @Override
    public int getBoardSize() {
        return 8;
    }

    @Override
    public List<Position> ValidMoves() {
        List<Position> positions = List.of(new Position(1, 1), new Position(3, 3),
                new Position(3, 4),new Position(4, 3),new Position(4, 4));
        return positions;
    }

    @Override
    public int countFlips(Position a) {
        return 0;
    }

    @Override
    public Player getFirstPlayer() {
        return this.player1;
    }

    @Override
    public Player getSecondPlayer() {
        return this.player2;
    }

    @Override
    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public boolean isFirstPlayerTurn() {
        return true;
    }

    @Override
    public boolean isGameFinished() {
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public void undoLastMove() {

    }
}
