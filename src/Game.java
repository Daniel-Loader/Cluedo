import java.util.*;
/**
 * The Game class initialises the game and tells players when it is their turn,
 * as well as storing the board, the scanner used for reading player input,
 * all instances of the player class and the Game's Solution.
 */
public class Game {

    private final Board board;
    private final Scanner scanner;
    private final List<Player> players;
    private final Solution globalSolution;

    /**
     * Constructs a new Game instance with an initialized board, 3-4 players,
     * and a randomly generated solution that will be different every time
     * main is run.
     */
    public Game() {
        this.board = new Board();
        this.scanner = new Scanner(System.in);
        this.players = new ArrayList<>(); addPlayers();
        this.globalSolution = getGlobalSolution();
    }

    /**
     * Asks the user how many players there are repeatedly until it gets
     * a valid answer. Then adds that many players to Game's list.
     */
    private void addPlayers() {
        do {
            System.out.print("Enter the number of players (3 to 4): ");
            numPlayers = scanner.nextInt();
        } while (numPlayers < 3 || numPlayers > 4);

        Player lucilla = new Player("Lucilla", 1, 11, scanner);
        Player bert = new Player("Bert", 9, 1, scanner);
        Player malina = new Player("Malina", 22, 9, scanner);
        if (numPlayers = 4) {percy = new Player("Percy", 14, 22, scanner);}
    }

    /**
     * Returns the next card of the specified type from the given list of cards.
     * The method removes the card from the list before returning it.
     *
     * @param cards     The list of cards to search for the specified type.
     * @param cardType  The class type of the card to retrieve.
     * @param <T>       The type of card to retrieve, extending the Card class.
     * @return The next card of the specified type, or null if no such card is
     * found.
     */
    private <T extends Card> T getNextCardOfType(List<Card> cards, Class<T> cardType) {
        for (Card card : cards) {
            if (cardType.isInstance(card)) {
                cards.remove(card);
                return cardType.cast(card);
            }
        }
        return null;
    }

    /**
     * Returns the global solution for the game, which contains the actual suspect,
     * weapon, and estate.
     *
     * @return The global solution for the game.
     */
    public Solution getGlobalSolution() {
        List<Card> allCards = new ArrayList<>();
        allCards.add(new Suspect("Lucilla", null));
        allCards.add(new Suspect("Bert", null));
        allCards.add(new Suspect("Malina", null));
        allCards.add(new Suspect("Percy", null));
        allCards.add(new Weapon("Revolver", null));
        allCards.add(new Weapon("Candlestick", null));
        allCards.add(new Weapon("Knife", null));
        allCards.add(new Weapon("Rope", null));
        allCards.add(new Estate("Haunted House", null));
        allCards.add(new Estate("Manic Manor", null));
        allCards.add(new Estate("Calamity Castle", null));
        allCards.add(new Estate("Peril Palace", null));
        allCards.add(new Estate("Visitation Villa", null));
        Collections.shuffle(allCards);

        // Distribute the cards among the players
        for (int i = 0; i < Players.size(); i++) {
            Players.get(i).addToHand(getNextCardOfType(allCards, Weapon.class));
            Players.get(i).addToHand(getNextCardOfType(allCards, Estate.class));
            if (i < 4) {
                currentPlayer.addToHand(getNextCardOfType(allCards, Suspect.class));
            }
        }
        if (i == 3) {
            players.get(0).addToHand(getNextCardOfType(allCards, Weapon.class));
            players.get(1).addToHand(getNextCardOfType(allCards, Weapon.class));
        }
        return allCards;
    }

    /**
     * Simulates a dice roll by generating two random numbers between 1 and 6 and
     * returns their sum.
     *
     * @param turn The current turn number.
     * @return The sum of two dice rolls.
     */
    public int roll(int turn) {
        Random random = new Random();
        int dice1 = random.nextInt(6) + 1;
        int dice2 = random.nextInt(6) + 1;
        System.out.printf("Turn %d:   %d rolled\n", turn, dice1 + dice2);
        return dice1 + dice2;
    }

    /**
     * Cycles through each player in a clockwise manner, starting from the given
     * index. Until turn() returns true, indicating the game is over.
     *
     * @param startingIndex The index of the player to start the game from.
     */
    public void clock(int startingIndex) {
        Boolean game_over = players.get(startingindex).turn(board, roll(1));
        for (int i = 1; game_over == false; i++) {
            int index = startingindex + i % players.size(); // Loop back to the beginning when reaching the end
            board.print();
            /**
             * The current player is handed the board, and the dice roll which
             * tells them how many actions they can take
             */
            game_over = players.get(index).turn(board, roll(i+1));

        }
    }

    /**
     * Closes all open resources, such as the Scanner, at the end of the game.
     */
    public void terminate() {
        scanner.close();
    }

    /**
     * The main method to start and run the game.
     * It initializes the game, selects a random player to start, and starts the game loop.
     *
     * @param args The command-line arguments (not used in this implementation).
     */
    public static void main(String[] args) {
        Game game = new Game();

        Random random = new Random();
        int startingIndex = random.nextInt(game.players.size()); //picks a random player to start
        game.clock(startingIndex);

        game.terminate();
    }

    /**
     * The Solution class represents a set of cards that together form a solution to the game.
     * It includes a suspect, weapon, and estate cards.
     */
    public class Solution {
        private Suspect suspect;
        private Weapon weapon;
        private Estate estate;

        /**
         * Constructs a new Solution instance with the specified suspect, weapon, and estate.
         *
         * @param suspect The suspect card.
         * @param weapon  The weapon card.
         * @param estate  The estate card.
         */
        public Solution(Suspect suspect, Weapon weapon, Estate estate) {
            this.suspect = suspect;
            this.weapon = weapon;
            this.estate = estate;
        }

        /**
         * Gets the suspect card from the solution.
         *
         * @return The suspect card.
         */
        public Suspect getSuspect() {
            return suspect;
        }

        /**
         * Gets the weapon card from the solution.
         *
         * @return The weapon card.
         */
        public Weapon getWeapon() {
            return weapon;
        }

        /**
         * Gets the estate card from the solution.
         *
         * @return The estate card.
         */
        public Estate getEstate() {
            return estate;
        }
    }
}