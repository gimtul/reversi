import java.util.List;

public class GreedyAI extends AIPlayer {
    private List<Position> pos;
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        this.pos=gameStatus.ValidMoves();
        Position posboard= new Position(0,0);
        int biggest=0;
        for (int i=0;i< pos.size();i++){
            int posmoves=gameStatus.countFlips(pos.get(i));
            if (posmoves>biggest){
                biggest=posmoves;
                posboard= pos.get(i);
            }
            if (posmoves==biggest){
                int colp= pos.get(i).col();
                int colb=posboard.col();
                if (colp>colb){
                    biggest=posmoves;
                    posboard= pos.get(i);
                }
                if (colp==colb){
                    int rowp= pos.get(i).row();
                    int rowb=posboard.row();
                    if (rowp>rowb){
                        biggest=posmoves;
                        posboard= pos.get(i);
                    }
                }
            }
        }
        Player currentPlayer = isPlayerOne() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();
        SimpleDisc disc=new SimpleDisc(currentPlayer);
        return new Move(posboard,disc);
    }
}
