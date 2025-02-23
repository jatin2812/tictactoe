package models;

import exceptions.DuplicateSymbolException;
import exceptions.MoreThanOneBotException;
import exceptions.PlayersCountDimensionMismatchException;
import strategies.winningstrategies.WinningStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    Board board;
    List<Player> players;

    List<Move> moves;
    GameStatus gameStatus;

    Player winner;

    public int nextMovePlayerIndex;
    private List<WinningStrategy> winningStrategies;

    public static Builder getBuilder() {
        return new Builder();
    }

    private Game(int dimension, List<Player> players, List<WinningStrategy> winningStrategies) {
        this.nextMovePlayerIndex = 0;
        this.gameStatus = GameStatus.IN_PROGRESS;
        this.moves = new ArrayList<>();
        this.players = players;
        this.winningStrategies = winningStrategies;
        this.board = new Board(dimension);
    }
    public static class Builder {
        private List<Player> players;
        private List<WinningStrategy> winningStrategies;
        private int dimension;

        private Builder() {
            this.players = new ArrayList<>();
            this.winningStrategies = new ArrayList<>();
            this.dimension = 0;
        }

        public Builder setDimension(int dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder setPlayers(List<Player> players) {
            this.players = players;
            return this;
        }

        public Builder addPlayer(Player player) {
            this.players.add(player);
            return this;
        }

        public Builder addWinningStrategy(WinningStrategy winningStrategy) {
            this.winningStrategies.add(winningStrategy);
            return this;
        }

        public Builder setWinningStrategies(List<WinningStrategy> winningStrategies) {
            this.winningStrategies = winningStrategies;
            return this;
        }

        private void validateBotCounts() throws MoreThanOneBotException {
            int botCount = 0;

            for (Player player: players) {
                if (player.getPlayerType().equals(PlayerType.BOT)) {
                    botCount += 1;
                }
            }

            if (botCount > 1) {
                throw new MoreThanOneBotException();
            }
        }

        private void validateDimensionAndPlayersCount() throws PlayersCountDimensionMismatchException {
            if (players.size() != dimension - 1) {
                throw new PlayersCountDimensionMismatchException();
            }
        }

        private void validateUniqueSymbolsForPlayers() throws DuplicateSymbolException {
            Map<Character, Integer> symbolCounts = new HashMap<>();

            for (Player player: players) {
                if (!symbolCounts.containsKey(player.getSymbol().getaChar())) {
                    symbolCounts.put(player.getSymbol().getaChar(), 0);
                }

                symbolCounts.put(
                        player.getSymbol().getaChar(),
                        symbolCounts.get(player.getSymbol().getaChar()) + 1
                );

                if (symbolCounts.get(player.getSymbol().getaChar()) > 1) {
                    throw new DuplicateSymbolException();
                }
            }
        }

        private void validate() throws DuplicateSymbolException, PlayersCountDimensionMismatchException, MoreThanOneBotException {
            validateBotCounts();
            validateDimensionAndPlayersCount();
            validateUniqueSymbolsForPlayers();
        }

        public Game build() throws DuplicateSymbolException, PlayersCountDimensionMismatchException, MoreThanOneBotException {
            validate();

            return new Game(
                    dimension,
                    players,
                    winningStrategies
            );
        }


    }


    private boolean validateMove(Move move) {
        int row = move.getCell().getRow();
        int col = move.getCell().getCol();

        if (row >= board.getSize()) {
            return false;
        }

        if (col >= board.getSize()) {
            return false;
        }

        if (board.getBoard().get(row).get(col).getCellState().equals(CellState.EMPTY)) {
            return true;
        }

        return false;
    }

    private boolean checkWinner(Board board, Move move) {
        for (WinningStrategy winningStrategy: winningStrategies) {
            if (winningStrategy.checkWinner(board, move)) {
                return true;
            }
        }

        return false;
    }

    public void makeMove() {
        Player currentMovePlayer = players.get(nextMovePlayerIndex);

        System.out.println("It is " + currentMovePlayer.getName() + "'s turn. Please make your move.");

        Move move = currentMovePlayer.makeMove(board);

        System.out.println(currentMovePlayer.getName() + " has made a move at row: " + move.getCell().getRow()
                + " column: " + move.getCell().getCol() + ".");

        if (!validateMove(move)) {
            System.out.println("Invalid Move. Please try again.");
            return;
        }

        int row = move.getCell().getRow();
        int col = move.getCell().getCol();

        Cell cellToChange = board.getBoard().get(row).get(col);
        cellToChange.setCellState(CellState.FILLED);
        cellToChange.setPlayer(currentMovePlayer);
        Move finalMoveObject = new Move(cellToChange, currentMovePlayer);
        moves.add(finalMoveObject);

        nextMovePlayerIndex += 1;
        nextMovePlayerIndex %= players.size();

        if (checkWinner(board, finalMoveObject)) {
            gameStatus = GameStatus.WIN;
            winner = currentMovePlayer;
        } else if (moves.size() == this.board.getSize() * this.board.getSize()) {
            gameStatus = GameStatus.DRAW;
        }
    }

    public void undo() {
        if (moves.size() == 0) {
            System.out.println("No move to undo");
            return;
        }

        Move lastMove = moves.get(moves.size() - 1);

        moves.remove(lastMove);

        Cell cell = lastMove.getCell();
        cell.setPlayer(null);
        cell.setCellState(CellState.EMPTY);

        for (WinningStrategy winningStrategy: winningStrategies) {
            winningStrategy.handleUndo(board, lastMove);
        }

        nextMovePlayerIndex -= 1;
        nextMovePlayerIndex = (nextMovePlayerIndex + players.size()) % players.size();
    }

    public void printBoard() {
        board.printBoard();
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public int getNextMovePlayerIndex() {
        return nextMovePlayerIndex;
    }

    public void setNextMovePlayerIndex(int nextMovePlayerIndex) {
        this.nextMovePlayerIndex = nextMovePlayerIndex;
    }
}
