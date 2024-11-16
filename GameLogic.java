import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameLogic implements PlayableLogic {


    private Player player1, player2;
    private final int BoardSize = getBoardSize();
    private Disc[][] DiscBoard = new Disc[BoardSize][BoardSize];

    public GameLogic() {
        super();
    }

    @Override
    public boolean locate_disc(Position a, Disc disc) {
        this.DiscBoard[a.row()][a.col()] = disc;
        return true;
    }

    @Override
    public Disc getDiscAtPosition(Position position) {
        return this.DiscBoard[position.col()][position.row()];
    }

    @Override
    public int getBoardSize() {
        return 8;
    }

    @Override
    public List<Position> ValidMoves() {
//        List<Position> positions = List.of(new Position(1, 1), new Position(3, 3),
//                new Position(3, 4),new Position(4, 3),new Position(4, 4));
        List<Position> positions = new ArrayList<>();
        for (int row = 0; row < BoardSize; row++) {
            for (int col = 0; col < BoardSize; col++) {
                if (DiscBoard[row][col] == null)
                    positions.add(new Position(row, col));
            }
        }
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
