
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {

    private Stack<Disc[][]> gameHistory = new Stack<>();
    private boolean isFirstPlayerTurn;
    private Player player1, player2;
    private final int BoardSize = getBoardSize();
    private Disc[][] DiscBoard = new Disc[BoardSize][BoardSize];

    public GameLogic() {
        super();
    }


    private void saveGameState() {
        gameHistory.push(this.DiscBoard);
    }

    @Override
    public boolean locate_disc(Position a, Disc disc) {
        if (this.DiscBoard[a.col()][a.row()] != null)
            return false;
        this.gameHistory.push(getDiscBoard());
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

    public boolean outOfBound(Position a) {
        if (a.row() == -1 || a.row() == 8 || a.col() == -1 || a.col() == 8)
            return true;
        return false;
    }

    @Override
    public int countFlips(Position a) {
        List<Position> toFlipList = new ArrayList<>();
        int row = a.row(), col = a.col();
        if (!outOfBound(new Position(row+1, col)) && DiscBoard[row][col] != null) {
            if (DiscBoard[row + 1][col].getOwner() != DiscBoard[row][col].getOwner() && DiscBoard[row + 1][col].getOwner() != null) {
                toFlipList.add(new Position(row + 1, col));
                //return countFlipsRec(new Position(row + 1, col), 1, 0, DiscBoard[row][col].getOwner());
            }
        }
        return 0;
    }

//    public int countFlipsRec(Position a, int r, int c, Player p) {
//        int row = a.row(), col = a.col();
//        if (DiscBoard[row+r][col+c].getOwner() != DiscBoard[row][col].getOwner())
//            return countFlipsRec(a, r, c, p);
//        if (doesItFlip(a, r, c, p)) {
//
//        }
//        return 0;
//    }

    public boolean doesItFlip(Position a, int r, int c, Player p) {
        if (DiscBoard[a.row()][a.col()].getOwner() != p)
            return true;
        if (DiscBoard[a.row()][a.col()].getOwner() == p || DiscBoard[a.row()][a.col()] == null)
            return false;
        if (outOfBound(new Position(a.row()+r, a.col()+c)))
            return false;
        return doesItFlip(new Position(a.row()+r, a.col()+c), r, c, p);
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
        if (!gameHistory.isEmpty()) {
            this.DiscBoard = gameHistory.pop();
            this.isFirstPlayerTurn = !this.isFirstPlayerTurn;
        } else {
            System.out.println("No moves to undo.");
        }
    }
    public Disc[][] getDiscBoard() {
        Disc[][] copyBoard=new Disc[BoardSize][BoardSize];
        for (int i=0;i<BoardSize;i++){
            for(int j=0;j<BoardSize;j++){
                copyBoard[i][j]=this.DiscBoard[i][j];
            }
        }
        return copyBoard;
    }

}

