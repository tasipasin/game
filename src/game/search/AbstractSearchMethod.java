package game.search;

import game.Board;
import game.BoardGameMoveEnum;
import game.BoardMoving;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *
 */
public abstract class AbstractSearchMethod implements ISearchMethod {

    /** Resultado esperado. */
    public static final int[][] RESULT = {{1, 4, 7}, {2, 5, 8}, {3, 6, 0}};
    /** Lista de Movimentações possíveis. */
    public final static List<BoardGameMoveEnum> MOVEMENTS = new ArrayList<>();
    /** Definição do tamanho máximo da árvore. */
    public final static int MAX_HEIGHT = 50;

    /** Fila de objetos para realizar a busca. */
    private final Deque<Board> searchQueue = new ConcurrentLinkedDeque<>();
    /** Lista de Resultados pela altura da árvore */
    private final Map<Integer, List<Board>> resultMapping = new HashMap<>();

    static {
        MOVEMENTS.addAll(Arrays.asList(BoardGameMoveEnum.values()));
        MOVEMENTS.remove(BoardGameMoveEnum.UNKNOWN);
    }

    @Override
    public Map<Integer, Board> executeSearch(Board board) {
        Board resultBoard = null;
        Long id = 1L;
        // Inicializa tempo de execução da pesquisa
        Date begin = new Date();
        // Indicador de resposta encontrada
        boolean answerFound = false;
        // Determina o identificador inicial do tabuleiro
        board.setBoardId(id);
        // Adiciona o primeiro tabuleiro na fila de verificações
        this.searchQueue.addFirst(board);
        // Adiciona o primeiro tabuleiro no mapeamento de resultados
        this.putBoardInResultMapping(board);
        // Continua procurando enquanto houver 
        while (!this.searchQueue.isEmpty() && !answerFound) {
            // Recupera o primeiro da fila para realizar movimentação e verificação
            Board currBoard = this.searchQueue.pollFirst();
            Iterator<BoardGameMoveEnum> movementsIt = MOVEMENTS.iterator();
            // Percorre toda a lista de movimentos possíveis
            while (movementsIt.hasNext() && !answerFound) {
                BoardGameMoveEnum movement = movementsIt.next();
                // Tenta realizar a movimentação
                Board newBoard = BoardMoving.moveTo(movement, currBoard);
                // Verifica se houve retorno
                if (null != newBoard) {
                    id++;
                    // Determina o identificador inicial do tabuleiro
                    newBoard.setBoardId(id);
                    // Determina o identificador do tabuleiro que gerou o novo
                    newBoard.setParentBoardId(currBoard.getBoardId());
                    // Adiciona no mapeamento de resultados
                    this.putBoardInResultMapping(newBoard);
                    // Verifica se atingiu o valor máximo de iterações
                    if (newBoard.getMoves() <= MAX_HEIGHT) {
                        // Adiciona na fila para verificação
                        this.insertInDeque(newBoard);
                    }
                    // Verifica se foi encontrada a respostas
                    answerFound = Arrays.deepEquals(newBoard.getBoard(), RESULT);
                    resultBoard = newBoard;
                }
            }
            this.heuristicMoves();
        }
        // Inicializa tempo de finalização da busca para a resposta
        Date end = new Date();
        // Informa o tempo decorrido da operação
        System.out.println(String.format("Resposta %s em %d segundos e %d iterações",
                answerFound ? "encontrada " : "não encontrada",
                ((end.getTime() - begin.getTime()) / 1000),
                id));
        Map<Integer, Board> result = new HashMap<>();
        // Verifica se foi encontrada solução para o problema
        if (answerFound && null != resultBoard) {
            result = this.findSequence(resultBoard);
        }
        // Retorna o mapeamento de resultado
        return result;
    }

    private Map<Integer, Board> findSequence(Board resultBoard) {
        Map<Integer, Board> result = new HashMap<>();
        // Recupera a quantidade de movimentos necessários para encontrar a resposta
        Integer moves = resultBoard.getMoves();
        result.put(moves, resultBoard);
        // Enquanto a quantidade de movimentos for maior ou igual a zero
        while (moves > 0) {
            // Recupera a lista de estados no movimento anterior
            Iterator<Board> boardList = this.resultMapping.get(moves - 1).iterator();
            // Percorre para verificar o estado que originou o próximo
            while (boardList.hasNext() && !result.containsKey(moves - 1)) {
                Board itBoard = boardList.next();
                if (resultBoard.getParentBoardId().equals(itBoard.getBoardId())) {
                    result.put(moves - 1, itBoard);
                    resultBoard = itBoard;
                }
            }
            moves--;
        }
        return result;
    }

    protected abstract void insertInDeque(Board board);

    protected void heuristicMoves() {
        // Nothing to do
    }

    /**
     * Insere o estado atual do tabuleiro no mapeamento de resultados.
     * @param board Objeto de estado do tabuleiro.
     */
    public void putBoardInResultMapping(Board board) {
        // Recupera a quantidade de movimentações que o tabuleiro sofreu
        Integer boardMoves = board.getMoves();
        // Recupera o valor do mapeamento
        List<Board> existingBoardList = this.resultMapping.get(boardMoves);
        // Verifica se a lista já foi inicializada
        if (null == existingBoardList) {
            // Inicializa a lista
            existingBoardList = new ArrayList<>();
        }
        // Insere o estado do tabuleiro na lista
        existingBoardList.add(board);
        // Adiciona a lista atualizada no tabuleiro
        this.resultMapping.put(boardMoves, existingBoardList);
    }

    public static int[][] getResult() {
        return RESULT;
    }

    public static List<BoardGameMoveEnum> getMovements() {
        return MOVEMENTS;
    }

    public static int getMaxHeight() {
        return MAX_HEIGHT;
    }

    public Deque<Board> getSearchQueue() {
        return searchQueue;
    }

    public Map<Integer, List<Board>> getResultMapping() {
        return resultMapping;
    }

}
