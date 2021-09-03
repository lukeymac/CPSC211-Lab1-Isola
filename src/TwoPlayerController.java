import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TwoPlayerController {
    private IsolaBoard board;
    private BoardSpace currentPlayer;

    public static void main(String[] args) {
        TwoPlayerController controller = new TwoPlayerController();
        controller.go();
    }

    public void go() {
        // Create board
        this.board = new IsolaBoard();
        // Create a view attached to that board
        GameView view = new GameView(board);
        this.currentPlayer = BoardSpace.Player1; // maybe refactor

        // Create output.dat
        File saveGame = new File("output.dat");
        PrintWriter writer;
        try {
            writer = new PrintWriter(saveGame);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open output.dat");
            return;
        }

        // while game is not over
        //      display board
        //
        //      ask current player for their move
        //      write move to output.dat
        //      make that move on the board
        //      switch to next player
        while (board.checkWinner() == BoardSpace.Available) {
            view.clearScreen();
            view.displayBoard();

            view.askForMove();
            Scanner scan = new Scanner(System.in);
            String moveAsDirection = scan.nextLine();
            writer.println(moveAsDirection);
            BoardPosition move = this.convertToPosition(moveAsDirection);
            board.movePlayer(currentPlayer, move);
            if (currentPlayer == BoardSpace.Player1) {
                currentPlayer = BoardSpace.Player2;
            } else {
                currentPlayer = BoardSpace.Player1;
            }
        }

        // display board
        view.clearScreen();
        view.displayBoard();

        // close output.dat
        writer.close();
    }

    private BoardPosition convertToPosition(String cardinalDirection) {
        // Convert the cardinal direction given by the player to a move that can be used by the board.movePlayer method.
        BoardPosition currentPosition = board.findPosition(currentPlayer);
        int x = 0;
        int y = 0;
        switch (cardinalDirection) {
            case "N" -> y = -1;
            case "NE" -> {
                y = -1;
                x = 1;
            }
            case "E" -> x = 1;
            case "SE" -> {
                y = 1;
                x = 1;
            }
            case "S" -> y = 1;
            case "SW" -> {
                y = 1;
                x = -1;
            }
            case "W" -> x = -1;
            case "NW" -> {
                y = -1;
                x = -1;
            }
        }
        return new BoardPosition(currentPosition.getRow()+y, currentPosition.getColumn()+x);
        // Having this method in both the TwoPlayerController and the ReplayController is redundant.
        // Would like to make a 'helper' methods class but unsure of best practices in Java for this.
    }
}