import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {
    private Stack<Disc[][]> gameHistory = new Stack<>();
    private Stack<List<Position>> flipPositionsHistory = new Stack<>();
    private boolean isFirstPlayerTurn = true;
    private Player player1, player2;
    private final int BoardSize = getBoardSize();
    private Disc[][] DiscBoard = new Disc[BoardSize][BoardSize];
    private List<Position> flipPositions;

    public GameLogic() {
        super();
    }
    private void saveGameState(Disc[][] discBoard, List<Position> flipPositions) {
        this.gameHistory.push(getDiscBoard(discBoard));
        this.flipPositionsHistory.push(flipPositions);
    }

    @Override
    public boolean locate_disc(Position a, Disc disc) {
        if (this.DiscBoard[a.row()][a.col()] != null || countFlips(a) < 1)
            return false;

        saveGameState(getDiscBoard(this.DiscBoard), this.flipPositions);

        this.DiscBoard[a.row()][a.col()] = disc;

        for (Position pos : this.flipPositions) {
            this.DiscBoard[pos.row()][pos.col()].setOwner(getCurrentPlayer());
        }

        this.isFirstPlayerTurn = !this.isFirstPlayerTurn;
        return true;
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
                if (DiscBoard[row][col] == null && countFlips(new Position(row, col)) > 0)
                    positions.add(new Position(row, col));
            }
        }
        return positions;
    }

    public boolean outOfBound(Position a) {
        if (a.row() == -1 || a.row() == 8 || a.col() == -1 || a.col() == 8)
            return true;
        return false;
    }

    @Override
    public int countFlips(Position a) {
        this.flipPositions = new ArrayList<>();
        int count = 0;

        Player p;
        if (isFirstPlayerTurn())
            p = this.player1;
        else
            p = this.player2;

        if (doesItFlip(a, 0, -1, p))
            Flips(a, 0, -1, p);
        if (doesItFlip(a, -1, -1, p))
            Flips(a, -1, -1, p);
        if (doesItFlip(a, -1, 0, p))
            Flips(a, -1, 0, p);
        if (doesItFlip(a, -1, 1, p))
            Flips(a, -1, 1, p);
        if (doesItFlip(a, 0, 1, p))
            Flips(a, 0, 1, p);
        if (doesItFlip(a, 1, 1, p))
            Flips(a, 1, 1, p);
        if (doesItFlip(a, 1, 0, p))
            Flips(a, 1, 0, p);
        if (doesItFlip(a, 1, -1, p))
            Flips(a, 1, -1, p);

        count += flipPositions.size();

        return count;
    }

    public List<Position> Flips(Position a, int r, int c, Player p) {
        int count = 1;

        while (DiscBoard[a.row() + count*r][a.col() + count*c].getOwner() != p) {
            //if (!getDiscAtPosition(new Position(a.row() + count*r, a.col() + count*c)).getType().equals("⭕"))
            this.flipPositions.add(new Position(a.row() + count*r, a.col() + count*c));
            count++;
        }

        return this.flipPositions;
    }

    public boolean doesItFlip(Position a, int r, int c, Player p) {
        int row = a.row() + r, col = a.col() + c;
        if (outOfBound(new Position(row, col)))
            return false;
        if (DiscBoard[row][col] == null)
            return false;
        if (DiscBoard[row][col].getOwner() == p)
            return true;
        return doesItFlip(new Position(row, col), r, c, p);
    }
    @Override
    public Disc getDiscAtPosition(Position position) {
        return this.DiscBoard[position.row()][position.col()];
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
        List<Position> pos = ValidMoves();
        if (pos.isEmpty()){
            if (isFirstPlayerTurn)
                System.out.println("player 2 has won");
            else
                System.out.println(("player 1 has won"));
            return  true;
        }
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
        if (!this.gameHistory.isEmpty()) {
            this.DiscBoard = this.gameHistory.pop();
            for (Position pos : this.flipPositionsHistory.pop()) {
                this.DiscBoard[pos.row()][pos.col()].setOwner(getCurrentPlayer());
            }
            this.isFirstPlayerTurn = !this.isFirstPlayerTurn;
        } else {
            System.out.println("No moves to undo.");
        }
    }
    public Disc[][] getDiscBoard(Disc[][] discBoard) {
        Disc[][] copyBoard = new Disc[BoardSize][BoardSize];
        for (int row = 0; row <BoardSize; row++){
            for(int col = 0; col < BoardSize; col++){
                copyBoard[row][col] = discBoard[row][col];
            }
        }
        return copyBoard;
    }


}
