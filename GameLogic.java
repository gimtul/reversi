import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {
    private Stack<Disc[][]> gameHistory = new Stack<>();
    private Stack<List<Position>> flipPositionsHistory = new Stack<>();
    private Stack<Integer> p1unflipcount=new Stack<>();
    private Stack<Integer> p2unflipcount=new Stack<>();
    private Stack<Integer> p1bombcount=new Stack<>();
    private Stack<Integer> p2bombcount=new Stack<>();
    private boolean isFirstPlayerTurn = true;
    private Player player1, player2;
    private final int BoardSize = getBoardSize();
    private Disc[][] DiscBoard = new Disc[BoardSize][BoardSize];
    private List<Position> flipPositions;
    private List<Position> tempBombPositions;
    private List<Position> bombPositions;
    private int p1discs = 0;
    private int p2discs = 0;
    private boolean counting;


    public GameLogic() {
        super();
    }

    /**saves the current game, including discs on board, bomb and unflippedable count and the flipped discs**/
    private void saveGameState(Disc[][] discBoard, List<Position> flipPositions, int p1bomb, int p2bomb, int p1unflip, int p2unflip) {
        this.gameHistory.push(getDiscBoard(discBoard));
        this.flipPositionsHistory.push(flipPositions);
        this.p1unflipcount.push(p1unflip);
        this.p2unflipcount.push(p2unflip);
        this.p1bombcount.push(p1bomb);
        this.p2bombcount.push(p2bomb);
    }

    @Override
    public boolean locate_disc(Position a, Disc disc) {
        if (this.DiscBoard[a.row()][a.col()] != null || countFlips(a) < 1)
            return false;

        if ((disc.getType().equals("💣"))&&getCurrentPlayer().getNumber_of_bombs()<1)
            return false;

        if ((disc.getType().equals("⭕"))&&getCurrentPlayer().getNumber_of_unflippedable()<1)
            return false;

        saveGameState(getDiscBoard(this.DiscBoard), this.flipPositions, this.player1.number_of_bombs, this.player2.number_of_bombs, this.player1.number_of_unflippedable, this.player2.number_of_unflippedable);

        this.DiscBoard[a.row()][a.col()] = disc;

        if (disc.getType().equals("💣"))
            getCurrentPlayer().reduce_bomb();

        if (disc.getType().equals("⭕"))
            getCurrentPlayer().reduce_unflippedable();

        for (Position pos : this.bombPositions) {
            if (!this.DiscBoard[pos.row()][pos.col()].getOwner().equals(getCurrentPlayer()))
                bombFlips(pos);
        }

        if (getCurrentPlayer()==player1)
            System.out.println("Payer 1 placed a "+disc.getType()+" in ("+a.row()+","+a.col()+")");
        else
            System.out.println("Payer 2 placed a "+disc.getType()+" in ("+a.row()+","+a.col()+")");


        for (Position pos : this.flipPositions) {
            this.DiscBoard[pos.row()][pos.col()].setOwner(getCurrentPlayer());
            if (getCurrentPlayer()==player1)
                System.out.println("Player 1 flipped the "+this.DiscBoard[pos.row()][pos.col()].getType()+"in ("+pos.row()+","+pos.col()+")");
            else
                System.out.println("Player 2 flipped the "+this.DiscBoard[pos.row()][pos.col()].getType()+"in ("+pos.row()+","+pos.col()+")");
        }

        this.isFirstPlayerTurn = !this.isFirstPlayerTurn;
        System.out.println("\n");
        return true;
    }

    /**returns the length and width of the board**/
    @Override
    public int getBoardSize() {
        return 8;
    }

    /**checks and returns a list of valid moves the player can make **/
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

    /**checks if the specified position is inside the board**/
    public boolean outOfBound(Position a) {
        if (a.row() == -1 || a.row() == getBoardSize() || a.col() == -1 || a.col() == getBoardSize())
            return true;
        return false;
    }

    @Override
    public int countFlips(Position a) {
        this.flipPositions = new ArrayList<>();
        this.tempBombPositions = new ArrayList<>();
        this.bombPositions = new ArrayList<>();
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

        this.counting = true;
        initBombPositions(a, p);
        this.counting = false;

        if (!bombPositions.isEmpty()) {
            for (Position pos : this.bombPositions) {
                if (!this.DiscBoard[pos.row()][pos.col()].getOwner().equals(getCurrentPlayer()))
                    bombFlips(pos);
            }
        }

        count += this.flipPositions.size();

        return count;
    }

    public void Flips(Position a, int r, int c, Player p) {

        int count = 1, row, col;

        while (DiscBoard[a.row() + count*r][a.col() + count*c].getOwner() != p) {
            row = a.row() + count*r;
            col = a.col() + count*c;
            Position pos = new Position(row, col);
            if (getDiscAtPosition(new Position(row, col)).getType().equals("⬤"))
                this.flipPositions.add(pos);
            if (getDiscAtPosition(new Position(row, col)).getType().equals("💣")) {
                this.flipPositions.add(pos);
                this.tempBombPositions.add(pos);
                this.bombPositions.add(pos);
            }
            count++;
        }
    }

    public void bombFlips(Position a) {
        int row = a.row(), col = a.col();

        Player p;
        if (isFirstPlayerTurn())
            p = this.player1;
        else
            p = this.player2;

        if (bombDoesItFlip(a, 0, -1, p) && !posContains(new Position(row, col - 1)))
            this.flipPositions.add(new Position(row, col - 1));
        if (bombDoesItFlip(a, -1, -1, p) && !posContains(new Position(row - 1, col - 1)))
            this.flipPositions.add(new Position(row - 1, col - 1));
        if (bombDoesItFlip(a, -1, 0, p) && !posContains(new Position(row - 1, col)))
            this.flipPositions.add(new Position(row - 1, col));
        if (bombDoesItFlip(a, -1, 1, p) && !posContains(new Position(row - 1, col + 1)))
            this.flipPositions.add(new Position(row - 1, col + 1));
        if (bombDoesItFlip(a, 0, 1, p) && !posContains(new Position(row, col + 1)))
            this.flipPositions.add(new Position(row, col + 1));
        if (bombDoesItFlip(a, 1, 1, p) && !posContains(new Position(row + 1, col + 1)))
            this.flipPositions.add(new Position(row + 1, col + 1));
        if (bombDoesItFlip(a, 1, 0, p) && !posContains(new Position(row + 1, col)))
            this.flipPositions.add(new Position(row + 1, col));
        if (bombDoesItFlip(a, 1, -1, p) && !posContains(new Position(row + 1, col - 1)))
            this.flipPositions.add(new Position(row + 1, col - 1));
    }

    public boolean bombDoesItFlip(Position a, int r, int c, Player p) {
        int row = a.row() + r, col = a.col() + c;
        if (outOfBound(new Position(row, col)))
            return false;
        if (this.DiscBoard[row][col] == null)
            return false;
        if (this.DiscBoard[row][col].getOwner() == p)
            return false;
        if (this.DiscBoard[row][col].getType().equals("⭕"))
            return false;
        Position pos = new Position(row, col);
        if (bombPosContains(new Position(row, col)))
            return false;
        if (this.DiscBoard[row][col].getType().equals("💣")) {
            if (counting)
                this.bombPositions.add(pos);
            bombFlips(new Position(row, col));
            return true;
        }
        return true;
    }

    public void initBombPositions(Position a, Player p) {

        if (!tempBombPositions.isEmpty()) {
            for (Position pos : this.tempBombPositions) {
                bombFlips(pos);
            }
        }

    }

    /**checks if there is a bomb that has already been exploded in the specified position**/
    public boolean bombPosContains(Position a) {
        for (Position pos : this.bombPositions) {
            if (pos.row() == a.row() && pos.col() == a.col())
                return true;
        }
        return false;
    }

    public boolean posContains(Position a) {
        for (Position pos : this.flipPositions) {
            if (pos.row() == a.row() && pos.col() == a.col())
                return true;
        }
        return false;
    }

    /**checks if the given disc can be flipped**/
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

    /**returns the disc at the specified position on the board**/
    @Override
    public Disc getDiscAtPosition(Position position) {
        return this.DiscBoard[position.row()][position.col()];
    }

    /**returns the first player**/
    @Override
    public Player getFirstPlayer() {
        return this.player1;
    }

    /**returns the second player**/
    @Override
    public Player getSecondPlayer() {
        return this.player2;
    }

    /**sets the type of players**/
    @Override
    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    /**checks if it is the first player's turn**/
    @Override
    public boolean isFirstPlayerTurn() {
        return isFirstPlayerTurn;
    }

    /**returns the current player playing**/
    public Player getCurrentPlayer() {
        if (isFirstPlayerTurn())
            return getFirstPlayer();
        return getSecondPlayer();
    }

    /**checks if the game is finished by checking if the current player has valid moves to make,
     if not the methode counts the amount of discs each player has and the player with the biggest amount of discs wins. and gets a win point**/
    @Override
    public boolean isGameFinished() {
        List<Position> pos = ValidMoves();
        if (pos.isEmpty()){
            for (int i=0;i<BoardSize;i++){
                for (int j=0;j<BoardSize;j++){
                    if (DiscBoard[i][j] != null) {
                        if (DiscBoard[i][j].getOwner()==player1){
                            p1discs++;
                        }
                        if (DiscBoard[i][j].getOwner()==player2){
                            p2discs++;
                        }
                    }
                }
            }
            if (p1discs>p2discs){
                System.out.println("Player 1 wins with "+ p1discs+" discs! Player 2 " + "had " +p2discs+ " discs.");
                player1.addWin();
            }
            if (p1discs<p2discs){
                System.out.println("Player 2 wins with "+ p2discs+" discs! Player 1 " + "had " +p1discs+ " discs.");
                player2.addWin();
            }
            return  true;
        }
        return false;
    }

    /**the default starting position**/
    public void __init__ () {
        DiscBoard[3][4]= new SimpleDisc(player2);
        DiscBoard[4][4]= new SimpleDisc(player1);
        DiscBoard[3][3]= new SimpleDisc(player1);
        DiscBoard[4][3]= new SimpleDisc(player2);

        isFirstPlayerTurn = true;

        this.p1discs = 0;
        this.p2discs = 0;
    }

    /**resets the game by reseting the bomb and unflippedables count, and calling the default board starting position**/
    @Override
    public void reset() {
        for (int row = 0; row < BoardSize; row++) {
            for (int col = 0; col < BoardSize; col++) {
                DiscBoard[row][col] = null;
            }
        }
        player1.reset_bombs_and_unflippedable();
        player2.reset_bombs_and_unflippedable();
        __init__();

    }

    /**undoes the last move by accessing a save of the board before the last move, while printing the removed and the flipped disks**/
    @Override
    public void undoLastMove() {
        System.out.println("undoing last move:");
        if (!(player1 instanceof AIPlayer) && !(player2 instanceof AIPlayer)) {
            if (!this.gameHistory.isEmpty()) {
                Disc[][] oldboard=getDiscBoard(this.DiscBoard);
                this.DiscBoard = this.gameHistory.pop();
                for (int i=0;i<BoardSize;i++){
                    for(int j=0;j<BoardSize;j++){
                        if (oldboard[i][j]!=null&&this.DiscBoard[i][j]==null){
                            System.out.println("\tUndo: removing "+oldboard[i][j].getType()+" from ("+i+","+j+")");
                        }
                    }
                }
                for (Position pos : this.flipPositionsHistory.pop()) {
                    this.DiscBoard[pos.row()][pos.col()].setOwner(getCurrentPlayer());
                    System.out.println("\tUndo: flipping back "+this.DiscBoard[pos.row()][pos.col()].getType()+" in ("+pos.row()+","+pos.col()+")");
                }
                this.isFirstPlayerTurn = !this.isFirstPlayerTurn;
                player1.setNumber_of_bombs(this.p1bombcount.pop());
                player1.setNumber_of_unflippedable(this.p1unflipcount.pop());
                player2.setNumber_of_bombs(this.p2bombcount.pop());
                player2.setNumber_of_unflippedable(this.p2unflipcount.pop());
            } else {
                System.out.println("\tNo: No previous move available to undo.");
            }
        }
        System.out.println("\n");
    }

    /**copies the current board and returns the copy**/
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
