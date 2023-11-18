package game.search;

import game.Board;

/**
 * Classe para busca pelo método de Busca em Largura.
 */
public class BreadthFirstSearch extends AbstractSearchMethod implements ISearchMethod {

    @Override
    protected void insertInDeque(Board board) {
        // Adiciona o primeiro tabuleiro na fila de verificações
        this.getSearchQueue().addLast(board);
    }

}
