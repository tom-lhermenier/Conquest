package fr.umontpellier.iut.conquest.DesignPattern;

import java.util.ArrayDeque;
import java.util.Deque;

public class BoardCaretaker {

    final Deque<BoardMemento> mementos = new ArrayDeque<>();
    public BoardMemento getMemento()
    {
        BoardMemento boardMemento= mementos.pop();
        return boardMemento;
    }
    public void addMemento(BoardMemento memento)
    {
        mementos.push(memento);
    }

    public boolean isEmpty(){
        return mementos.isEmpty();
    }
}
