package game;

/**
 * Classe de movimentação do jogo (jogador).
 */
public class BoardMoving {

    /**
     * Realiza o movimento no tabuleiro.
     * @param movement Indicador de movimento requisitado.
     * @param oldBoard Objeto do estado atual do tabuleiro.
     * @return Tabuleiro atualizado após movimentação.
     */
    public static Board moveTo(BoardGameMoveEnum movement, Board oldBoard) {
        Board result = null;
        if (BoardMoving.canMove(oldBoard, movement)) {
            // Realiza cópia do quadro
            result = new Board(oldBoard);
            // Recupera o valor da antiga posição do 0
            int oldRow = result.getBlankRowPosition();
            int oldColumn = result.getBlankColumnPosition();
            // Indica posição futura do 0
            int newRow = oldRow + movement.getRowMove();
            int newColumn = oldColumn + movement.getColumnMove();
            // Recupera o valor que deve ser substituido pelo 0
            int valueToMove = result.getBoard()[newColumn][newRow];
            // Adiciona o 0 na nova posição
            result.getBoard()[newColumn][newRow] = 0;
            // Insere o valor na posição antiga do 0
            result.getBoard()[oldColumn][oldRow] = valueToMove;
            // Atualiza os valores da posição do quadrado branco
            result.setBlankColumnPosition(newColumn);
            result.setBlankRowPosition(newRow);
            // Incrementa a quantidade de movimentações
            result.setMoves(oldBoard.getMoves() + 1);
            // Determina o último movimento 
            result.setLastMove(movement);
        }
        return result;
    }

    /**
     * Verifica se a movimentação pode ser executada no tabuleiro. Verifica se o
     * movimento não é contrário ao movimento anterior, para nao desfazer, e se
     * não está no limite.
     * @param board    Objeto de tabuleiro.
     * @param movement Movimentação desejada.
     * @return Indicador de possibilidade de movimentação.
     */
    private static boolean canMove(Board board, BoardGameMoveEnum movement) {
        // Inicializa a variável de resultado
        boolean result = false;
        // Recupera o último movimento que o tabuleiro sofreu
        BoardGameMoveEnum oldState = board.getLastMove();
        switch (movement) {
            case UP:
                result = !board.isBlankMostTop() && !oldState.equals(BoardGameMoveEnum.DOWN);
                break;
            case DOWN:
                result = !board.isBlankMostBottom() && !oldState.equals(BoardGameMoveEnum.UP);
                break;
            case LEFT:
                result = !board.isBlankMostLeft() && !oldState.equals(BoardGameMoveEnum.RIGHT);
                break;
            case RIGHT:
                result = !board.isBlankMostRight() && !oldState.equals(BoardGameMoveEnum.LEFT);
                break;
            default:
                break;
        }
        return result;
    }

}
