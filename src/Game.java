import java.util.*;

public class Game {
    private Board board;
    private List<Player> players;

    public Game() {
        this.board = new Board();
        this.players = new ArrayList<>();
    }

    public void initialize() {
        Scanner scanner = new Scanner(System.in);

        // Ask how many players there are
        int numPlayers;
        do {
            System.out.print("Enter the number of players (2 to 4): ");
            numPlayers = scanner.nextInt();
        } while (numPlayers < 2 || numPlayers > 4);

        // Add players based on the number entered
        String[] names = {"Lucilla", "Bert", "Malina", "Percy"};
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(names[i], 0, 0));
        }

        scanner.close();
    }

    public int roll(int turn) {
        Random random = new Random();
        int dice1 = random.nextInt(6) + 1; // Generates a random number between 1 and 6
        int dice2 = random.nextInt(6) + 1; // Generates another random number between 1 and 6
        System.out.printf("Turn %d:   %d rolled\n", turn, dice1 + dice2);
        return dice1 + dice2;
    }

    // Method to cycle through each player in a clockwise manner starting from the given index
    public void clock(int startingindex) {
        Boolean game_over = players.get(startingindex).turn(board, roll(1));
        for (int i = 1; game_over == false; i++) {
            int index = startingindex + i % players.size(); // Loop back to the beginning when reaching the end
            board.print();
            game_over = players.get(index).turn(board, roll(i+1));
            // The current player is handed the board, and the dice roll which tells them how
            // many actions they can take

        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();

        Random random = new Random();
        int startingIndex = random.nextInt(game.players.size()); //picks a random player to start
        game.clock(startingIndex);
    }
}

