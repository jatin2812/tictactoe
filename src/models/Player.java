package models;
import java.util.Scanner;

public class Player {
    public String name;
    PlayerType playerType;
    Symbol symbol;
    private Scanner scanner;
    Long id;

    public Move makeMove(Board board) {
        System.out.println("Please tell row count where you want to move (Starting from 0)");
        int row = scanner.nextInt();

        System.out.println("Please tell column count where you want to move (Starting from 0)");
        int col = scanner.nextInt();

        return new Move(new Cell(row, col), this);
    }

    public Player(Long id, String name, Symbol symbol, PlayerType type) {
        this.id = id;
        this.playerType = type;
        this.name = name;
        this.symbol = symbol;
        this.scanner = new Scanner(System.in);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }
}
