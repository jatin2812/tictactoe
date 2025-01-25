
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


import controllers.GameController;

import models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import strategies.winningstrategies.ColWinningStrategy;
import strategies.winningstrategies.WinningStrategy;


public class Main {
    public static void main(String[] args) {
        GameController gameController = new GameController();
        Scanner scanner = new Scanner(System.in);

        try {
            int dimensionOfGame = 3;

            List<Player> players = new ArrayList<>();
            players.add(
                    new Player(1L, "Jatin", new Symbol('X'), PlayerType.HUMAN)
            );

            players.add(
                    new Bot(2L, "GPT", new Symbol('O'), BotDifficultyLevel.HARD)
            );

            List<WinningStrategy> winningStrategies = List.of(
                    new ColWinningStrategy()
            );

            Game game = gameController.startGame(
                    dimensionOfGame,
                    players,
                    winningStrategies
            );

            while(gameController.checkState(game).equals(GameStatus.IN_PROGRESS)) {
                // 1. printBoard
                // 2. x's turn
                // 3. ask to makeMove

                gameController.printBoard(game);

                System.out.println("Does anyone want to undo? (y/n)");
                String undoAnswer = scanner.next();

                if (undoAnswer.equalsIgnoreCase("y")) {
                    gameController.undo(game);
                    continue;
                }

                gameController.makeMove(game);
            }

            System.out.println("Game is finished");
            GameStatus state = gameController.checkState(game);

            if (state.equals(GameStatus.DRAW)) {
                System.out.println("It is a draw");
            } else {
                System.out.println("Winner is " + gameController.getWinner(game).getName());
            }

        } catch (Exception e) {
            System.out.println("exception");
        }

    }
}