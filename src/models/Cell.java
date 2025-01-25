package models;

public class Cell {
    public int row;
    public int col;

    Player player;
    CellState cellState;

    public Cell(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public void display() {
        if (player == null) {
            System.out.print("| - |");
        } else {
            System.out.printf("| " + player.getSymbol().getaChar() + " |");
        }
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public CellState getCellState() {
        return cellState;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }
}
