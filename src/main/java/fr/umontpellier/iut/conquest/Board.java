package fr.umontpellier.iut.conquest;

import java.util.ArrayList;
import java.util.List;

/**
 * Modélise un plateau.
 */
public class Board {
    /**
     * Tableau des pions.
     */
    private Pawn[][] field;

    /**
     * Constructeur.
     *
     * @param size : la taille du plateau.
     */
    public Board(int size) {
        field = new Pawn[size][size];
    }

    public Board(Board board) {
        int size = board.field.length;
        field = new Pawn[size][size];
        for(int i = 0; i < size;i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = board.field[i][j];
            }
        }
    }

    /**
     * Constructeur pour Test.
     *
     * @param field : plateau prédéfini.
     */
    public Board(Pawn[][] field) {
        this.field = field;
    }

    /**
     * Les méthodes suivantes sont utilisées pour les tests automatiques. Il ne faut pas les utiliser.
     */
    public Pawn[][] getField() {
        return field;
    }

    /**
     * Retourne la taille du plateau.
     */
    public int getSize() {
        return field.length;
    }

    public void setField(Pawn[][] field) {
        this.field = field;
    }
    /**
     * Affiche le plateau.
     */
    public String toString() {
        int size = field.length;
        StringBuilder b = new StringBuilder();
        for (int r = -1; r < size; r++) {
            for (int c = -1; c < size; c++) {
                if (r == -1 && c == -1) {
                    b.append("_");
                } else if (r == -1) {
                    b.append("_").append(c);
                } else if (c == -1) {
                    b.append(r).append("|");
                } else if (field[r][c] == null) {
                    b.append("_ ");
                } else if (field[r][c].getPlayer().getColor() == 1) {
                    b.append("X ");
                } else {
                    b.append("O ");
                }
            }
            b.append("\n");
        }
        b.append("---");
        return b.toString();
    }

    /**
     * Initialise le plateau avec les pions de départ.
     * Rappel :
     * - player1 commence le jeu avec un pion en haut à gauche (0,0) et un pion en bas à droite.
     * - player2 commence le jeu avec un pion en haut à droite et un pion en bas à gauche.
     */
    public void initField(Player player1, Player player2) {
        Pawn pawn1 = new Pawn(player1);
        Pawn pawn2 = new Pawn(player1);
        Pawn pawn3 = new Pawn(player2);
        Pawn pawn4 = new Pawn(player2);
        this.getField()[0][0] = pawn1;
        this.getField()[getSize()-1][getSize()-1] = pawn2;
        this.getField()[getSize()-1][0] = pawn3;
        this.getField()[0][getSize()-1] = pawn4;

    }


    /**
     * Vérifie si un coup est valide.
     * Rappel :
     * - Les coordonnées du coup doivent être dans le plateau.
     * - Le pion bougé doit appartenir au joueur.
     * - La case d'arrivée doit être libre.
     * - La distance entre la case d'arrivée est au plus 2.
     */
    public boolean isValid(Move move, Player player) {
        if (move.getRow2() < 0 || move.getColumn2() < 0) return false;
        if (move.getRow1() < 0 || move.getColumn1() < 0) return false;
        if (move.getRow2()>= this.getSize()) return false;
        if (move.getColumn2() >= this.getSize()) return false;
        if (move.getRow1()>= this.getSize()) return false;
        if (move.getColumn1() >= this.getSize()) return false;
        if (getField()[move.getRow1()][move.getColumn1()] == null) return false;
        if (!getField()[move.getRow1()][move.getColumn1()].getPlayer().equals(player)) return false;
        if (getField()[move.getRow2()][move.getColumn2()] != null) return false;
        if ((Math.abs(move.getRow2()-move.getRow1()) > 2 ) || (Math.abs(move.getColumn2()-move.getColumn1()) > 2 )) return false;
        return true;
    }

    /**
     * Déplace un pion.
     *
     * @param move : un coup valide.
     *             Rappel :
     *             - Si le pion se déplace à distance 1 alors il se duplique pour remplir la case d'arrivée et la case de départ.
     *             - Si le pion se déplace à distance 2 alors il ne se duplique pas : la case de départ est maintenant vide et la case d'arrivée remplie.
     *             - Dans tous les cas, une fois que le pion est déplacé, tous les pions se trouvant dans les cases adjacentes à sa case d'arrivée prennent sa couleur.
     */
    public void movePawn(Move move) {

        getField()[move.getRow2()][move.getColumn2()] = getField()[move.getRow1()][move.getColumn1()];
        if ((Math.abs(move.getRow2()-move.getRow1()) > 1 ) || (Math.abs(move.getColumn2()-move.getColumn1()) > 1 )){
            getField()[move.getRow1()][move.getColumn1()] = null;
        }
        for (int i = -1 ; i <=1 ; i++) {
            for (int j = -1 ; j<=1; j++){
                if((move.getColumn2()+i < getSize()) && (move.getColumn2()+i >= 0) && (move.getRow2()+j < getSize()) && (move.getRow2()+j >= 0)){
                    if (getField()[move.getRow2()+j][move.getColumn2()+i] != null) getField()[move.getRow2()+j][move.getColumn2()+i] = getField()[move.getRow2()][move.getColumn2()];
                }
            }
        }
    }

    /**
     * Retourne la liste de tous les coups valides de player.
     * S'il n'y a de coup valide, retourne une liste vide.
     */
    public List<Move> getValidMoves(Player player) {
        List <Move> moveValid = new ArrayList<>();
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (getField()[i][j] != null) {
                    for (int x = -2; x <= getSize()-1; x++) {
                        for (int y = -2; y <= getSize()-1; y++) {
                            Move move = new Move(i, j, x, y);
                            if (this.isValid(move, player)) {
                                moveValid.add(move);
                            }
                        }
                    }
                }

            }
        }
        return moveValid;
    }



    /**
     * Retourne le nombre des pions d'un joueur.
     */
    public int getNbPawns(Player player) {
        int nbPawn = 0;
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if(getField()[i][j] != null) {
                    if (getField()[i][j].getPlayer() == player) nbPawn++;
                }
            }
        }
        return nbPawn; 
    }

    public int getNbPawnsOtherPlayer(Player player) {
        int nbPawn = 0;
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if(field[i][j] == null);
                else if (field[i][j].getPlayer() == player);
                else {
                    nbPawn++;
                }
            }
        }
        return nbPawn;
    }
}
