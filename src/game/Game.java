package game;

import game.search.ISearchMethod;
import game.search.SearchMethodFactory;
import java.util.Map;
import java.util.Scanner;

public class Game {

    /** Determina o estado inicial do tabuleiro. */
    private static final int[][] DEFAULT_VALUE = {{0, 3, 6}, {1, 4, 7}, {2, 5, 8}};
//    private static final int[][] DEFAULT_VALUE = {{1, 4, 7}, {2, 5, 8}, {3, 0, 6}};

    public static void main(String[] args) {
        try {
            System.out.println("Selecione a opcao desejada para fazer a busca do resultado:");
            System.out.println("1. Busca Cega em Largura");
            System.out.println("2. Busca Cega em Profundidade");
            System.out.println("3. Busca Heurística A*");
            ISearchMethod searchMethod = null;
            do {
                // Inicializa objeto de leitura do console
                Scanner sc = new Scanner(System.in);
                // Recupera a entrada do usuário
                int selectedOption = sc.nextInt();
                // Recupera modo de busca de acordo com a entrada do usuário
                searchMethod = SearchMethodFactory.selectMethod(selectedOption);
            } while (null == searchMethod);
            Map<Integer, Board> result = searchMethod.executeSearch(new Board(DEFAULT_VALUE));
            if (null != result && !result.isEmpty()) {
                System.out.println("Sequência de Ações para encontrar a solução:");
                for (int i = 0; i < result.size(); i++) {
                    Board board = result.get(i);
                    System.out.println(String.format("Nível: %d", i));
                    System.out.println(String.format("Movimento: %s", board.getLastMove().toString()));
                    System.out.println(board.toString());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            System.exit(0);
        }
    }

}
