package cluedo27_7;

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
    private final ArrayList<Card> allCards;
    private final List<Card> solution;
	

    /*
     * Constructs a new Game instance with an initialized board, 3-4 players,
     * and a randomly generated solution that will be different every time
     * main is run.
     */
    public Game() {
        this.board = new Board(24, 24);
        this.scanner = new Scanner(System.in);
        this.players = new ArrayList<>();
        addPlayers();
        this.allCards = new ArrayList<>();
        this.solution = getGlobalSolution();
    }
    public Board getBoard() {
        return board;
    }
    
	public Scanner getScanner() {
		return scanner;
	}

    /**
     * Asks the user how many players there are repeatedly until it gets
     * a valid answer. Then adds that many players to Game's list.
     */
	private void addPlayers() {
	    int numPlayers = 0;
	    boolean isValidInput = false;

	    do {
	        System.out.print("Enter the number of players (3 to 4): ");
	        try {
	            numPlayers = Integer.parseInt(getScanner().nextLine());

	            if (numPlayers >= 3 && numPlayers <= 4) {
	                isValidInput = true;
	            } else {
	                System.out.println("Invalid number of players. The number of players must be between 3 and 4.");
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid input. Please enter a valid number.");
	        }
	    } while (!isValidInput);

	    players.add(new Player("Lucilla", this, 1, 11));
	    players.add(new Player("Bert", this, 9, 1));
	    players.add(new Player("Malina", this, 22, 9));
	    if (numPlayers == 4) {
	        players.add(new Player("Percy", this, 14, 22));
	    }

	    for (int i = 0; i < numPlayers; i++) {
	        Player currentPlayer = getPlayer(i);
	        // WARNING: this will cause an error if the player's initial position is not on a Path cell.
	        Path currentPath = (Path) board.getCellAt(currentPlayer.row(), currentPlayer.column());
	        currentPath.setPlayer(currentPlayer); //Set the player on the board cell.
	    }
	}
    
    public List<Player> getPlayers() {
    	return List.copyOf(players);
    }
    
    public Player getPlayer(int i) {
    	return players.get(i);
    }
    public Card getCard(String name) {
        for (Card c : allCards) {
            if (c.name().equals(name)) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.format("%s isn't a valid Card name", name));
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
            int index = (startingIndex + i) % players.size(); // Loop back to the beginning when reaching the end
            gameOver = players.get(index).turn(roll(i+1));

        }
    }

    /**
     * Returns the next card of the specified type from the given list of cards.
     * The method removes the card from the list before returning it.
     *
     * @param cardType  The type of the card to retrieve.
     * @return The next card of the specified type, or null if no such card is
     * found.
     */
    private Card getNextCardOfType(List<Card> cards, Card.Type cardType) {
        for (Card card : cards) {
            if (card.cardType() == cardType) {
                boolean remove = cards.remove(card);
                return card;
            }
        }
        throw new IllegalStateException("No card of specified type found");
    }

    /**
     * Returns the global solution for the game, which contains the actual suspect,
     * weapon, and estate.
     *
     * @return The global solution for the game.
     */
    public List<Card> getGlobalSolution() {
        allCards.add(new Card("Lucilla",          Card.Type.SUSPECT));
        allCards.add(new Card("Bert",             Card.Type.SUSPECT));
        allCards.add(new Card("Malina",           Card.Type.SUSPECT));
        allCards.add(new Card("Percy",            Card.Type.SUSPECT));
        allCards.add(new Card("Broom",            Card.Type.WEAPON));
        allCards.add(new Card("Scissors",         Card.Type.WEAPON));
        allCards.add(new Card("Knife",            Card.Type.WEAPON));
        allCards.add(new Card("Shovel",           Card.Type.WEAPON));
        allCards.add(new Card("Ipad",             Card.Type.WEAPON));
        allCards.add(new Card("Haunted House",    Card.Type.ESTATE));
        allCards.add(new Card("Manic Manor",      Card.Type.ESTATE));
        allCards.add(new Card("Calamity Castle",  Card.Type.ESTATE));
        allCards.add(new Card("Peril Palace",     Card.Type.ESTATE));
        allCards.add(new Card("Visitation Villa", Card.Type.ESTATE));
        Collections.shuffle(allCards);

        ArrayList<Card> solution = new ArrayList<>(allCards);

        // Distribute the cards among the players
        for (int i = 0; i < players.size(); i++) {
            getPlayer(i).addToHand(getNextCardOfType(solution, Card.Type.WEAPON));
            getPlayer(i).addToHand(getNextCardOfType(solution, Card.Type.ESTATE));
            if (i < 4) {
                getPlayer(i).addToHand(getNextCardOfType(solution, Card.Type.SUSPECT));
            }
        }
        if (players.size() == 3) {
            players.get(0).addToHand(getNextCardOfType(solution, Card.Type.WEAPON));
            players.get(1).addToHand(getNextCardOfType(solution, Card.Type.ESTATE));
        }
        return solution;
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

}