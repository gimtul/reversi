import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameLogic implements PlayableLogic {

    private boolean isFirstPlayerTurn = true;
    private Player player1, player2;
    private final int BoardSize = getBoardSize();
    private Disc[][] DiscBoard = new Disc[BoardSize][BoardSize];

    public GameLogic() {
        super();
    }

    @Override
    public boolean locate_disc(Position a, Disc disc) {
        if (this.DiscBoard[a.row()][a.col()] != null || countFlips(a) < 1)
            return false;
        this.DiscBoard[a.row()][a.col()] = disc;
        this.isFirstPlayerTurn = !this.isFirstPlayerTurn;
        return true;
    }

    @Override
    public Disc getDiscAtPosition(Position position) {
        return this.DiscBoard[position.row()][position.col()];
    }

    @Override
    public int getBoardSize() {
        return 8;
    }

    @Override
    public List<Position> ValidMoves() {
        List<Position> positions = new ArrayList<>();
        for (int row = 0; row < BoardSize; row++) {
            for (int col = 0; col < BoardSize; col++) {
                System.out.println(row + ", " + col);
                if (DiscBoard[row][col] == null && countFlips(new Position(row, col)) > 0)
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

    public boolean outOfBound(Position a) {
        if (a.row() == -1 || a.row() == 8 || a.col() == -1 || a.col() == 8)
            return true;
        return false;
    }

    @Override
    public int countFlips(Position a) {
        //boolean[][] toFlip = new
        Player p;
        if (isFirstPlayerTurn())
            p = getFirstPlayer();
        else
            p = getSecondPlayer();

        if (a.row() == 4 && a.col() == 3)
            System.out.println("this");

        if (doesItFlip(a, 0, -1, p))
            return Flips(a, 0, -1, p).size();
        if (doesItFlip(a, -1, -1, p))
            return Flips(a, -1, -1, p).size();
        if (doesItFlip(a, -1, 0, p))
            return Flips(a, -1, 0, p).size();
        if (doesItFlip(a, -1, 1, p))
            return Flips(a, -1, 1, p).size();
        if (doesItFlip(a, 0, 1, p)) //////////
            return Flips(a, 0, 1, p).size();
        if (doesItFlip(a, 1, 1, p))
            return Flips(a, 1, 1, p).size();
        if (doesItFlip(a, 1, 0, p))
            return Flips(a, 1, 0, p).size();
        if (doesItFlip(a, 1, -1, p))
            return Flips(a, 1, -1, p).size();

        return 0;
    }

    public List<Position> Flips(Position a, int r, int c, Player p) {
        List<Position> flipPositions = new ArrayList<>();
        int count = 1;

        while (DiscBoard[a.row() + count*r][a.col() + count*c].getOwner() != p) {
            flipPositions.add(new Position(a.row() + count*r, a.col() + count*c));
            count++;
        }
        return flipPositions;
    }

    public boolean doesItFlip(Position a, int r, int c, Player p) {
        if (DiscBoard[a.row()][a.col()] != null)
            System.out.println(DiscBoard[a.row()][a.col()] + " here!");
        int row = a.row() + r, col = a.col() + c;
        if (outOfBound(new Position(row, col)))
            return false;
//        System.out.println(DiscBoard[3][4] + "here!");
//        System.out.println(DiscBoard[4][4] + "here!");
//        System.out.println(DiscBoard[3][3] + "here!");
//        System.out.println(DiscBoard[4][3] + "here!");
        if (DiscBoard[row][col] == null)
            return false;
        if (DiscBoard[row][col].getOwner() == p)
            return true;
        return doesItFlip(new Position(row, col), r, c, p);
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

    public Player getCurrentPlayer() {
        if (isFirstPlayerTurn())
            return getFirstPlayer();
        return getSecondPlayer();
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
