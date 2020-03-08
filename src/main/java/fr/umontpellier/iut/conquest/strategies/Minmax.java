package fr.umontpellier.iut.conquest.strategies;

import fr.umontpellier.iut.conquest.Board;
import fr.umontpellier.iut.conquest.Move;
import fr.umontpellier.iut.conquest.Player;

import java.util.ArrayList;
import java.util.List;

public class Minmax implements Strategy {

    private int iaLevel;

    public Minmax(int iaLevel) {
        this.iaLevel = iaLevel;
    }

    @Override
    public Move getMove(Board board, Player player) {
        int index = getMove2(board, player, iaLevel, true);
        List<Move> moveList = board.getValidMoves(player);
        return moveList.get(index);
    }

    /*@Override
    public Move getMove(Board board, Player player) {

        Move move = new Move(0,0,0,0);
        int value = 0, max = -10, index = 0, i = 0;
        List<Move> moveTestList = board.getValidMoves(player);
        List<Board> boardList = new ArrayList();
        Board boardTest = new Board(board);
        for (int j = 0; j < iaLevel; j++) {
                List<Move> moveList = board.getValidMoves(player);
                int tab[] = new int[moveTestList.size()];

                for (Move m : moveList) {
                    Board board2 = new Board(board);
                    board2.movePawn(m);
                    boardList.add(board2);
                }

                for (Board b : boardList) {
                    value = b.getNbPawns(player) - b.getNbPawnsOtherPlayer(player);
                    tab[i] += value;
                    i++;
                }

                if(j + 1 == iaLevel) {
                    for (int k = 0; k < tab.length; k++) {
                        if(tab[k] > max) {
                            max = tab[k];
                            index = k;
                        }
                    }
                }

                move = moveTestList.get(index);
                index = 0;
                i = 0;
                max = -10;
                if(board.isValid(move,player)) {
                    System.out.println("Move valide");
                }
                else {
                    System.out.println("Move invalide");
                }
                boardTest.movePawn(move);
        }
        return move;
    }*/

    /*public int getMove(Board board, Player player, int profondeur) {
        int i = 0, minmax, index = 0;
        Player player2;
        if (profondeur % 2 == 0) {
            player2 = player.getGame().getOtherPlayer(player);
        }
        else {
            player2 = player;
        }
        List<Move> moveTestList = board.getValidMoves(player2);
        int tab[] = new int[moveTestList.size()];
        List<Board> boardList = new ArrayList();
        if (profondeur < iaLevel) {
            for (Move move : moveTestList) {
                Board board2 = new Board(board);
                board2.movePawn(move);
                boardList.add(board2);
            }
            for (Board b : boardList) {
                tab[i] = getMove(b, player, profondeur + 1);
                i++;
            }

            if (tab.length > 0) {
                if (profondeur % 2 == 0) {
                    minmax = tab[0];
                    for (int j = 1; j < tab.length; j++) {
                        if (tab[j] < minmax) {
                            minmax = tab[j];
                            index = j;
                        }
                    }
                } else {
                    minmax = tab[0];
                    for (int j = 0; j < tab.length; j++) {
                        if (tab[j] > minmax) {
                            minmax = tab[j];
                            index = j;
                        }
                    }
                }
                if (profondeur == 1) {
                    return index;
                }
                return minmax;
            }
            else {
                if(profondeur%2 == 0) {
                    return board.getNbPawns(player2) - board.getNbPawnsOtherPlayer(player2);
                }
                else {
                    return board.getNbPawnsOtherPlayer(player2) - board.getNbPawns(player2);
                }
            }
        }
        else {
            for (Move move : moveTestList) {
                Board board2 = new Board(board);
                board2.movePawn(move);
                boardList.add(board2);
            }
            for (Board b : boardList) {
                tab[i] = b.getNbPawns(player2) - b.getNbPawnsOtherPlayer(player2);
                i++;
            }
            if(tab.length > 0) {
                minmax = tab[0];
                if (profondeur %2 == 0) {
                    for (int j = 1; j < tab.length; j++) {
                        if (tab[j] < minmax) {
                            minmax = tab[j];
                            index = j;
                        }
                    }
                }
                else {
                    for (int j = 1; j < tab.length; j++) {
                        if (tab[j] > minmax) {
                            minmax = tab[j];
                            index = j;
                        }
                    }
                }
                if (this.iaLevel == 1) {
                    return index;
                }
            }
            else {
                if(profondeur%2 == 0) {
                    return board.getNbPawns(player2) - board.getNbPawnsOtherPlayer(player2);
                }
                else {
                    return board.getNbPawnsOtherPlayer(player2) - board.getNbPawns(player2);
                }
            }
            return minmax;
        }
    }*/

    public int getMove2(Board board,Player player, int profondeur, boolean maximizingPlayer) {
        int i = 0, index = 0;
        if(profondeur == 0 || board.getValidMoves(player).isEmpty()) {
            return board.getNbPawns(player) - board.getNbPawnsOtherPlayer(player);
        }
        Player player2;
        if (!maximizingPlayer) {
            player2 = player.getGame().getOtherPlayer(player);
        }
        else {
            player2 = player;
        }
        List<Move> moveList = board.getValidMoves(player2);
        int tab[] = new int[moveList.size()];
        List<Board> boardList = new ArrayList();
        for (Move move : moveList) {
            Board board2 = new Board(board);
            board2.movePawn(move);
            boardList.add(board2);
        }
        for (Board b : boardList) {
            if(maximizingPlayer) {
                tab[i] = getMove2(b, player, profondeur - 1, false);
            }
            else {
                tab[i] = getMove2(b, player,profondeur - 1, true);
            }
            i++;
        }
        if (tab.length > 0) {
            double inf = Double.POSITIVE_INFINITY;
            double inf2 = Double.NEGATIVE_INFINITY;
            if (maximizingPlayer) {
                for (int k = 0; k < tab.length; k++) {
                    if (tab[k] > inf2) {
                        inf2 = tab[k];
                        index = k;
                    }
                }

                if (iaLevel == profondeur) {
                    return index;
                }
                return (int) inf2;
            } else {
                for (int k = 0; k < tab.length; k++) {
                    if (tab[k] < inf) {
                        inf = tab[k];
                        index = k;
                    }
                }
                if (iaLevel == profondeur) {
                    return index;
                }
                return (int) inf;
            }
        }
        else {
            return board.getNbPawns(player) - board.getNbPawnsOtherPlayer(player);
        }
    }

}