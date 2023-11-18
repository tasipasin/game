package game.search;

import game.Board;
import java.util.List;
import java.util.Map;

/**
 *
 */
public interface ISearchMethod {

    public Map<Integer, Board> executeSearch(Board board);

}
