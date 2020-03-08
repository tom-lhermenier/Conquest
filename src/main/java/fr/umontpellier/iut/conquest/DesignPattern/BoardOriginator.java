package fr.umontpellier.iut.conquest.DesignPattern;

import fr.umontpellier.iut.conquest.Board;

public class BoardOriginator {

    private Board board;

    public BoardOriginator(Board board)    {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public BoardMemento saveToMemento() {
        BoardMemento boardMemento=new BoardMemento(new Board(board));
        return boardMemento;
    }

    public  void undoFromMemento(BoardMemento memento)
    {
        this.board = memento.getBoard();
    }
    public void printInfo()
    {
        System.out.println(this.board);
    }
}
