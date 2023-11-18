package game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Classe de controle do jogo.
 */
public final class Board {

    /** Chave para campo que armazena o valor da coluna do bloco branco. */
    private static final String BLANK_COLUMN = "blankColumn";
    /** Chave para campo que armazena o valor da linha do bloco branco. */
    private static final String BLANK_ROW = "blankRow";
    /** Resultado esperado. */
    public static final int[][] RESULT = {{1, 4, 7}, {2, 5, 8}, {3, 6, 0}};

    /** Identificador único do Tabuleiro. */
    private Long boardId;
    /** Identificador do Tabuleiro que gerou esse. */
    private Long parentBoardId = null;
    /** Estado do board. */
    private final int[][] board = new int[3][3];
    /** Posição da posição do quadrado branco. */
    private final Map<String, Integer> blankPosition = new HashMap<>();
    /** Quantidade de movimentações que o quadro recebeu. */
    private Integer moves = 0;
    /** Armazena o último movimento feito no tabuleiro. */
    private BoardGameMoveEnum lastMove = BoardGameMoveEnum.UNKNOWN;
    /** Pontuação do tabuleiro de acordo com o posicionamento das peças. */
    private Double score = 0D;

    /**
     * Classe de controle do jogo.
     */
    public Board() {
        this.initializeBoard();
    }

    /**
     * Classe de controle do jogo.
     * @param board Estado inicial do jogo.
     */
    public Board(int[][] board) {
        // Realiza processo de cópia sem referência para o jogo
        for (int i = 0; i < board.length; i++) {
            this.board[i] = board[i].clone();
        }
        // Busca onde está o campo vazio
        this.findEmpty();
    }

    /**
     * Classe de controle do jogo.
     * @param newBoard Objeto para cópia.
     */
    public Board(Board newBoard) {
        // Realiza processo de cópia sem referência para o jogo
        for (int i = 0; i < newBoard.getBoard().length; i++) {
            this.board[i] = newBoard.getBoard()[i].clone();
        }
        this.blankPosition.putAll(newBoard.getBlankPosition());
        this.moves = Integer.valueOf(newBoard.getMoves().intValue());
    }

    /**
     * Inicializa o jogo de forma aleatória.
     */
    private void initializeBoard() {
        // Inicializa lista de valores que ja foram inseridos no jogo
        Set<Integer> valuesGone = new HashSet<>();
        // Inicializa iterador sobre as linhas do jogo
        for (int row = 0; row < board.length; row++) {
            // Inicializa iterador sobre as colunas do jogo
            for (int column = 0; column < board.length; column++) {
                // Recupera um valor aleatório entre 0 e 8, incluídos
                int randomValue = ThreadLocalRandom.current().nextInt(0, 8);
                // Verifica se o valor recuperado já foi sorteado
                while (valuesGone.contains(randomValue)) {
                    // Recupera novo valor aleatório
                    randomValue = ThreadLocalRandom.current().nextInt(0, 8 + 1);
                }
                // Adiciona o valor na lista de valores que ja foram escolhidos
                valuesGone.add(randomValue);
                // Adiciona o valor na coluna e linha da vez
                this.board[column][row] = randomValue;
                // Verifica se o valor selecionado é o quadrado vazio, ou zero.
                if (randomValue == 0) {
                    // Armazena as informações no objeto de localização do quadrado vazio
                    blankPosition.put(BLANK_ROW, row);
                    blankPosition.put(BLANK_COLUMN, column);
                }
            }
        }
    }

    /**
     * Busca a posição do quadrado vazio.
     */
    protected void findEmpty() {
        // Inicializa iterador sobre as linhas do jogo
        for (int row = 0; row < board.length; row++) {
            // Inicializa iterador sobre as colunas do jogo
            for (int column = 0; column < board.length; column++) {
                // Adiciona o valor na coluna e linha da vez
                int value = this.board[column][row];
                // Verifica se o valor selecionado é o quadrado vazio, ou zero.
                if (value == 0) {
                    // Armazena as informações no objeto de localização do quadrado vazio
                    blankPosition.put(BLANK_ROW, row);
                    blankPosition.put(BLANK_COLUMN, column);
                }
            }
        }
    }

    /**
     * Avalia o estado do tabuleiro e retorna a pontuação dele.
     * @return o estado do tabuleiro e retorna a pontuação dele.
     */
    public Double evaluateBoard() {
        Double result = 0D;
        // Inicializa iterador sobre as linhas do jogo
        for (int row = 0; row < board.length; row++) {
            // Inicializa iterador sobre as colunas do jogo
            for (int column = 0; column < board.length; column++) {
                // Verifica se valor está na posição
                if (this.board[column][row] == RESULT[column][row]) {
                    result += 1D;
                    // Senão verifica se está dos lados
                } else if ((row > 0 && this.board[column][row - 1] == RESULT[column][row])
                        || (row < (board.length - 1) && this.board[column][row + 1] == RESULT[column][row])
                        || (column > 0 && this.board[column - 1][row] == RESULT[column][row])
                        || (column < (board.length - 1) && this.board[column + 1][row] == RESULT[column][row])) {
                    result += 0.5D;
                    // Senão verifica se está nas diagonais
                } else if ((column > 0 && row > 0 && this.board[column - 1][row - 1] == RESULT[column][row])
                        || (column > 0 && row < (board.length - 1) && this.board[column - 1][row + 1] == RESULT[column][row])
                        || (column < (board.length - 1) && row > 0 && this.board[column + 1][row - 1] == RESULT[column][row])
                        || (column < (board.length - 1) && row < (board.length - 1) && this.board[column + 1][row + 1] == RESULT[column][row])) {
                    result += 0.25D;
                }
            }
        }
        this.score = result;
        this.toString();
        return result;
    }

    /**
     * Retorna a linha posição do bloco branco.
     * @return a linha posição do bloco branco.
     */
    public int getBlankRowPosition() {
        return this.blankPosition.get(BLANK_ROW);
    }

    /**
     * Determina a linha posição do bloco branco.
     * @param value a linha posição do bloco branco.
     */
    public void setBlankRowPosition(int value) {
        this.blankPosition.put(BLANK_ROW, value);
    }

    /**
     * Retorna a coluna posição do bloco branco.
     * @return a coluna posição do bloco branco.
     */
    public int getBlankColumnPosition() {
        return this.blankPosition.get(BLANK_COLUMN);
    }

    /**
     * Retorna a coluna posição do bloco branco.
     * @param value a coluna posição do bloco branco.
     */
    public void setBlankColumnPosition(int value) {
        this.blankPosition.put(BLANK_COLUMN, value);
    }

    /**
     * Verifica se o bloco branco está o mais à direita possível.
     * @return Indicador do bloco branco está o mais à direita possível.
     */
    public boolean isBlankMostRight() {
        return this.getBlankColumnPosition() == this.board.length - 1;
    }

    /**
     * Verifica se o bloco branco está o mais à esquerda possível.
     * @return Indicador do bloco branco está o mais à esquerda possível.
     */
    public boolean isBlankMostLeft() {
        return this.getBlankColumnPosition() == 0;
    }

    /**
     * Verifica se o bloco branco está o mais em cima possível.
     * @return Indicador do bloco branco está o mais em cima possível.
     */
    public boolean isBlankMostTop() {
        return this.getBlankRowPosition() == 0;
    }

    /**
     * Verifica se o bloco branco está o mais em baixo possível.
     * @return Indicador do bloco branco está o mais em baixo possível.
     */
    public boolean isBlankMostBottom() {
        return this.getBlankRowPosition() == this.board.length - 1;
    }

    /**
     * Retorna o Identificador do Tabuleiro.
     * @return o Identificador do Tabuleiro.
     */
    public Long getBoardId() {
        return boardId;
    }

    /**
     * Determina o Identificador do Tabuleiro.
     * @param boardId o Identificador do Tabuleiro.
     */
    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    /**
     * Retorna o identificador do tabuleiro que o gerou.
     * @return o identificador do tabuleiro que o gerou.
     */
    public Long getParentBoardId() {
        return parentBoardId;
    }

    /**
     * Determina o identificador do tabuleiro que o gerou.
     * @param parentBoardId o identificador do tabuleiro que o gerou.
     */
    public void setParentBoardId(Long parentBoardId) {
        this.parentBoardId = parentBoardId;
    }

    /**
     * Retorna o tabuleiro.
     * @return o tabuleiro.
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Retorna a posição do quadrado branco.
     * @return a posição do quadrado branco.
     */
    public Map<String, Integer> getBlankPosition() {
        return blankPosition;
    }

    /**
     * Retorna a quantidade de movimentos executadas no quadro.
     * @return a quantidade de movimentos executadas no quadro.
     */
    public Integer getMoves() {
        return moves;
    }

    /**
     * Determina a quantidade de movimentos executadas no quadro.
     * @param moves a quantidade de movimentos executadas no quadro.
     */
    public void setMoves(Integer moves) {
        this.moves = moves;
    }

    /**
     * Retorna o último movimento executado no quadro.
     * @return o último movimento executado no quadro.
     */
    public BoardGameMoveEnum getLastMove() {
        return lastMove;
    }

    /**
     * Determina o último movimento executado no quadro.
     * @param lastMove o último movimento executado no quadro.
     */
    public void setLastMove(BoardGameMoveEnum lastMove) {
        this.lastMove = lastMove;
    }

    /**
     * Retorna a pontuação do tabuleiro.
     * @return a pontuação do tabuleiro.
     */
    public Double getScore() {
        return score;
    }

    /**
     * Determina a pontuação do tabuleiro.
     * @param score a pontuação do tabuleiro.
     */
    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column <= board.length; column++) {
                if (column == board.length) {
                    sb.append("\n");
                } else if (this.board[column][row] == 0) {
                    sb.append("\u00AD");
                } else {
                    sb.append(board[column][row]);
                }
            }
        }
        sb.append("Espaço Branco em: ").append(String.format("{blankRow=%d, blankColumn=%d}",
                this.blankPosition.get(BLANK_ROW) + 1, this.blankPosition.get(BLANK_COLUMN) + 1))
                .append("\n");
        return sb.toString();
    }

}
