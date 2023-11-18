package game.search;

import game.Board;

/**
 * Classe para busca pelo método de Busca em Profundidade.
 */
public class DepthSearch extends AbstractSearchMethod implements ISearchMethod {

    @Override
    protected void insertInDeque(Board board) {
        // Adiciona o primeiro tabuleiro na fila de verificações
        this.getSearchQueue().addFirst(board);
    }

}
