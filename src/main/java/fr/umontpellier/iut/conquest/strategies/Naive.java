package fr.umontpellier.iut.conquest.strategies;

import fr.umontpellier.iut.conquest.Board;
import fr.umontpellier.iut.conquest.Move;
import fr.umontpellier.iut.conquest.Player;

import java.util.List;

public class Naive implements Strategy {

    @Override
    public Move getMove(Board board, Player player) {
        Move move;
        boolean moveIsInvalid;
        do {
            List<Move> moveList = board.getValidMoves(player);
            int randomNumber = (int)(Math.random() * (moveList.size()));
            move = moveList.get(randomNumber);
            moveIsInvalid = !board.isValid(move, player);
        } while (moveIsInvalid);
        return move;
    }
}
