/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.search;

import game.Board;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class AStarSearch extends AbstractSearchMethod implements ISearchMethod {
    
    private final List<Board> boardList = new ArrayList<>();
    
    @Override
    protected void insertInDeque(Board board) {
        board.evaluateBoard();
        boardList.add(board);
    }
    
    @Override
    protected void heuristicMoves() {
        Collections.sort(boardList, (Board u1, Board u2) -> u1.getScore().compareTo(u2.getScore()));
        boardList.forEach(currBoard -> {
            this.getSearchQueue().addFirst(currBoard);
        });
        boardList.clear();
    }
    
}
