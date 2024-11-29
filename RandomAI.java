import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer {
    private List<Position> pos;
    private int bombcount;
    private int unflipcount;
    private Player p;
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        this.pos= gameStatus.ValidMoves();
        this.bombcount=getNumber_of_bombs();
        this.unflipcount=getNumber_of_unflippedable();
        Random random = new Random();
        int discrandom= random.nextInt(3);
        int posrandom= random.nextInt(pos.size());
        Player currentPlayer = isPlayerOne() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();
        Position randpos=pos.get(posrandom);
        Disc disc=new SimpleDisc(currentPlayer);
        if (discrandom==1){
            if (bombcount>0) {
                disc = new BombDisc(currentPlayer);
            }
        }
        if (discrandom==2){
            if(unflipcount>0) {
                disc = new UnflippableDisc(currentPlayer);
            }
        }
        return new Move(randpos,disc);
    }
}