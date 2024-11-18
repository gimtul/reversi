import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameLogic implements PlayableLogic {

    private boolean isFirstPlayerTurn;
    private Player player1, player2;
    private final int BoardSize = getBoardSize();
    private Disc[][] DiscBoard = new Disc[BoardSize][BoardSize];

    public GameLogic() {
        super();
    }

    @Override
    public boolean locate_disc(Position a, Disc disc) {
        if (this.DiscBoard[a.col()][a.row()] != null)
            return false;
        this.DiscBoard[a.col()][a.row()] = disc;
        this.isFirstPlayerTurn = !this.isFirstPlayerTurn;
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
                if (DiscBoard[col][row] == null && countFlips(new Position(col, row)) >= 0) //very if flips
                    positions.add(new Position(row, col));
            }
        }
        return positions;
    }

    public boolean isBorder(Position a) {
        if (a.row() == 0 || a.row() == 7 || a.col() == 0 || a.col() == 7)
            return true;
        return false;
    }

    @Override
    public int countFlips(Position a) {
//        if () {
//
//        }
        return 0;
    }

    public int countFlipsRec(Position a) {
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
        return isFirstPlayerTurn;
    }

    @Override
    public boolean isGameFinished() {
        return false;
    }

    public void __init__ () {
        DiscBoard[3][4]= new SimpleDisc(player2);
        DiscBoard[4][4]= new SimpleDisc(player1);
        DiscBoard[3][3]= new SimpleDisc(player1);
        DiscBoard[4][3]= new SimpleDisc(player2);

        isFirstPlayerTurn = true;
    }

    @Override
    public void reset() {
        for (int row = 0; row < BoardSize; row++) {
            for (int col = 0; col < BoardSize; col++) {
                DiscBoard[row][col] = null;
            }
        }

        __init__();

    }

    @Override
    public void undoLastMove() {

    }
}
