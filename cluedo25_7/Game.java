package cluedo25_7;



import java.util.*;
/**
 * The Game class initializes the game and tells players when it is their turn,
 * as well as storing the board, the scanner used for reading player input,
 * all instances of the player class and the Game's Solution.
 */
public class Game {

    private final Board board;
    private final Scanner scanner;
    private final List<Player> players;
    @SuppressWarnings("unused")
	private final Solution globalSolution;

    /**
     * Constructs a new Game instance with an initialized board, 3-4 players,
     * and a randomly generated solution that will be different every time
     * main is run.
     */
    public Game() {
        this.board = new Board(24, 24);
        this.scanner = new Scanner(System.in);
        this.players = new ArrayList<>(); addPlayers();
        this.globalSolution = getGlobalSolution();
    }
    
	public Scanner getScanner() {
		return scanner;
	}

    /**
     * Asks the user how many players there are repeatedly until it gets
     * a valid answer. Then adds that many players to Game's list.
     */
    private void addPlayers() {
        int numPlayers;
        do {
            System.out.print("Enter the number of players (3 to 4): ");
            numPlayers = getScanner().nextInt();
        } while (numPlayers < 3 || numPlayers > 4);

        players.add(new Player("Lucilla", this,1, 11));
        players.add(new Player("Bert"   , this,9,  1));
        players.add(new Player("Malina" , this,22, 9));
        if (numPlayers == 4) {players.add(new Player("Percy", this,14, 22));}

        for (int i = 0; i < numPlayers; i++) {
            Player currentPlayer = getPlayer(i);
            //WARNING: this will cause an error if the player's initial position is not on a Path cell.
            Path currentPath = (Path)board.getCellAt(currentPlayer.row(), currentPlayer.column());
            currentPath.setPlayer(currentPlayer); //Set the player on the board cell.
        }
    }
    
    public List<Player> getPlayers() {
    	return List.copyOf(players);
    }
    
    public Player getPlayer(int i) {
    	return players.get(i);
    }

    /**
     * Simulates a pair of die rolls by generating two random numbers between 1 and 6.
     *
     * @param turn The current turn number.
     * @return The sum of two 'die rolls'.
     */
    public int roll(int turn) {
        Random random = new Random();
        int die1 = random.nextInt(6) + 1;
        int die2 = random.nextInt(6) + 1;
        System.out.printf("Turn %d:   %d rolled\n", turn, die1 + die2);
        return die1 + die2;
    }

    /**
     * Cycles through each player in a clockwise manner, starting from the given
     * index. Until turn() returns true, indicating the game is over.
     *
     * @param startingIndex The index of the player to start the game from.
     */
    public void clock(int startingIndex) {
        Boolean gameOver = players.get(startingIndex).turn(roll(1));
        for (int i = 1; !gameOver; i++) {
            int index = startingIndex + i % players.size(); // Loop back to the beginning when reaching the end
            gameOver = players.get(index).turn(roll(i+1));

        }
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
        for (int i = 0; i < players.size(); i++) {
            getPlayer(i).addToHand(getNextCardOfType(allCards, Weapon.class));
            getPlayer(i).addToHand(getNextCardOfType(allCards, Estate.class));
            if (i < 4) {
                getPlayer(i).addToHand(getNextCardOfType(allCards, Suspect.class));
            }
        }
        if (players.size() == 3) {
            players.get(0).addToHand(getNextCardOfType(allCards, Weapon.class));
            players.get(1).addToHand(getNextCardOfType(allCards, Weapon.class));
        }
        return new Solution(getNextCardOfType(allCards, Suspect.class),
        		getNextCardOfType(allCards, Weapon.class),
        		getNextCardOfType(allCards, Estate.class));
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

    public Board getBoard() {
        return board;
    }

    /**
     * The Solution class represents a set of cards that together form a solution to the game.
     * It includes a suspect, weapon, and estate cards.
     */
    public static class Solution {
        private final Suspect suspect;
        private final Weapon weapon;
        private final Estate estate;

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