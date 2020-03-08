package fr.umontpellier.iut.conquest;

import fr.umontpellier.iut.conquest.DesignPattern.BoardCaretaker;
import fr.umontpellier.iut.conquest.DesignPattern.BoardMemento;
import fr.umontpellier.iut.conquest.DesignPattern.BoardOriginator;
import fr.umontpellier.iut.conquest.strategies.Strategy;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Modélise une partie de Conquest.
 */

public class Game {
    /**
     * Scanner permettant de lire les entrées.
     */
    private static Scanner scan;
    /**
     * Le plateau de jeu.
     */
    private Board board;
    /**
     * Les joueurs.
     */
    private Player[] players = new Player[2];

    /**
     * Deque de sauvegarde de board
     */
    private BoardCaretaker boardSaved;

    private BoardOriginator boardOriginator;

    /**
     * Constructeur.
     * Crée un plateau à partir de sa taille (impaire).
     * Crée les joueurs à partir de leur stratégie et leur nom.
     */
    public Game(int size, Strategy strategy1, String name1, Strategy strategy2, String name2) {
        board = new Board(size);
        players[0] = new Player(strategy1, this, name1, 1);
        players[1] = new Player(strategy2, this, name2, 2);
        boardSaved = new BoardCaretaker();
    }

    /**
     * Constructeur pour Test.
     * Prend un plateau prédéfini.
     * Crée les joueurs à partir de leur stratégie et leur nom.
     */
    public Game(Board board, Strategy strategy1, String name1, Strategy strategy2, String name2) {
        this.board = board;
        players[0] = new Player(strategy1, this, name1, 1);
        players[1] = new Player(strategy2, this, name2, 2);
    }

    /**
     * Les méthodes suivantes sont utilisées pour les tests automatiques. Il ne faut pas les utiliser.
     */
    public static Scanner getScan() {
        return scan;
    }

    public static void initInput(InputStream inputStream) {
        scan = new Scanner(inputStream);
    }

    public Player[] getPlayers() {
        return players;
    }

    /**
     * Getter.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Fait tourner une partie.
     *
     * @param hardcore : Si hardcore = 1, les joueurs ne peuvent pas annuler les coups joués.
     *                 Sinon harcdore = 0 et les joueurs ont le droit d'annuler un ou plusieurs coups à chaque fois qu'un coup est joué.
     */
    public void run(int hardcore) {
        // Le joueur qui commence est le premier joueur.
        Player player = players[0];

        // Initialise le jeu.
        initGame();

        // Fait tourner le jeu.
        while (!isFinished()) {

            // Affiche le plateau.
            System.out.println(board);

            // Demande au joueur courant de joueur.
            if (!board.getValidMoves(player).isEmpty()) {
                board.movePawn(player.play());
            }

            // En mode non-hardcore, demande au joueur s'il veut annuler un ou plusieurs coups.
            if (hardcore == 0) {
                player = confirmOrUndoMove(player);
            }
            // Change de joueur.
            player = getOtherPlayer(player);
        }

        // Affiche le plateau final.
        System.out.println(board);

        // Affiche le nom du vainqueur.
        System.out.println("Le vainqueur est " + getWinner().getName() + " !");
    }

    /**
     * Initialise le jeu.
     */
    private void initGame() {
        board.initField(players[0], players[1]);
        boardOriginator = new BoardOriginator(board);
        BoardMemento boardMemento = boardOriginator.saveToMemento();
        boardSaved.addMemento(boardMemento);
    }

    /**
     * Prends un joueur en entrée et retourne l'autre joueur.
     */
    public Player getOtherPlayer(Player player) {
        if(player.equals(players[0])) return players[1];
        else return players[0];
    }

    /**
     * Teste si la partie est finie.
     * Rappel :
     * - La partie est finie quand il n'y a plus de case libre.
     * - La partie est finie quand l'un des deux joueurs n'a plus de pions.
     */
    public boolean isFinished() {
        if(getBoard().getNbPawns(players[0])+getBoard().getNbPawns(players[1]) == Math.pow(getBoard().getSize(), 2)) return true;
        else if(getBoard().getNbPawns(players[0]) == 0 || getBoard().getNbPawns(players[1]) == 0) return true;
        else return false;
    }

    /**
     * Retourne le joueur qui a gagné la partie.
     * Rappel : Le joueur qui gagne est celui qui possède le plus de pions.
     */
    public Player getWinner() {
        if(getBoard().getNbPawns(players[0])>getBoard().getNbPawns(players[1])) return players[0];
        else return players[1];
    }

    /**
     * Demande au joueur s'il veut annuler (ou pas) un ou plusieurs coups.
     * <p>
     * Tant que le joueur player le désire et que l'on n'est pas revenu au début de la partie en cours,
     * propose à player de reculer d'un coup en faisant saisir 1, ou 0 sinon.
     * Cette méthode doit donc modifier l'état de l'attribut board.
     *
     * @param player : le joueur courant.
     * @return Player : le joueur dont il est le tour de jouer.
     */
    private Player confirmOrUndoMove(Player player) {
        int playerTest = 0; // variable servant à vérifier si un move a été undo afin de choisir le bon joueur en sortie
        do {
            System.out.println("Saisissez 1 pour reculer d'un coup, 0 sinon");
            if(scan.nextInt() == 1){
                playerTest += 1;
                BoardMemento boardMemento = boardSaved.getMemento();
                boardOriginator.undoFromMemento(boardMemento);
                board = boardOriginator.getBoard();
            }else{
                BoardMemento boardMemento = boardOriginator.saveToMemento();
                boardSaved.addMemento(boardMemento);
                if(playerTest%2 == 1) {
                    return getOtherPlayer(player);
                }
                return player;
            }
        }while (!boardSaved.isEmpty());
        BoardOriginator boardOriginator = new BoardOriginator(board);
        BoardMemento boardMemento = boardOriginator.saveToMemento();
        boardSaved.addMemento(boardMemento);
        if(playerTest%2 == 1) return getOtherPlayer(player);
        else return player;

    }
}

