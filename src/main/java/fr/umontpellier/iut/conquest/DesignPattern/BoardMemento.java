package fr.umontpellier.iut.conquest.DesignPattern;

import fr.umontpellier.iut.conquest.Board;

public class BoardMemento {

    private Board board;

    public BoardMemento(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public String toString(){
        String str="Current Memento State" + this.board ;
        return str;
    }
}
