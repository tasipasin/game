package game.search;

/**
 * Classe para seleção do Métodod e Busca.
 */
public class SearchMethodFactory {

    public static ISearchMethod selectMethod(int value) {
        ISearchMethod result = null;
        switch (value) {
            case 1:
                result = new BreadthFirstSearch();
                break;
            case 2:
                result = new DepthSearch();
                break;
            case 3:
                result = new AStarSearch();
                break;
            default:
                break;
        }
        return result;
    }

}
