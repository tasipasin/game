package game;

/**
 *
 */
public enum BoardGameMoveEnum {

    UNKNOWN(0, 0),
    UP(-1, 0),
    LEFT(0, -1),
    RIGHT(0, 1),
    DOWN(1, 0);

    private int rowMove;
    private int columnMove;

    private BoardGameMoveEnum(int rowMove, int columnMove) {
        this.rowMove = rowMove;
        this.columnMove = columnMove;
    }

    public int getRowMove() {
        return rowMove;
    }

    public void setRowMove(int rowMove) {
        this.rowMove = rowMove;
    }

    public int getColumnMove() {
        return columnMove;
    }

    public void setColumnMove(int columnMove) {
        this.columnMove = columnMove;
    }

}
