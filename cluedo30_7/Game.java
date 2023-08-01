package cluedo30_7;

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
    private final ArrayList<String> allCards;
    private final ArrayList<String> solution;
	

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

	    players.add(new Player("Lucilla", this, 3, 7));
	    players.add(new Player("Bert", this, 5, 16));
	    players.add(new Player("Malina", this, 18, 7));
	    if (numPlayers == 4) {
	        players.add(new Player("Percy", this, 20, 16));
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
    public String getCard(String name) {
        for (String c : allCards) {
            if (c.equals(name)) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.format("%s isn't a valid Card name", name));
    }

    public String getSuspectCard() {
        System.out.println("Enter a Suspect:");
        for (String c : allCards.subList(0, 4)) {
            System.out.println(c);
        }
        String suspectName = getScanner().next();
        for (String c : allCards.subList(0, 4)) {
            if (c.equals(suspectName)) {
                return c;
            }
        }
        System.out.printf("%s isn't a valid suspect Card name\n", suspectName);
        return getSuspectCard();
    }

    public String getWeaponCard() {
        System.out.println("Enter a Weapon:");
        for (String c : allCards.subList(4, 9)) {
            System.out.println(c);
        }
        String weaponName = getScanner().next();
        for (String c : allCards.subList(4, 9)) {
            if (c.equals(weaponName)) {
                return c;
            }
        }
        System.out.printf("%s isn't a valid weapon Card name\n", weaponName);
        return getWeaponCard();
    }

    public String getEstateCard() {
        System.out.println("Enter an Estate:");
        for (String c : allCards.subList(9, 14)) {
            System.out.println(c);
        }
        String estateName = getScanner().next();
        for (String c : allCards.subList(9, 14)) {
            if (c.equals(estateName)) {
                return c;
            }
        }
        System.out.printf("%s isn't a valid estate Card name\n", estateName);
        return getEstateCard();
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

    /*
     * Returns the next card of the specified type from the given list of cards.
     * The method removes the card from the list before returning it.
     *
     * @param cardType  The type of the card to retrieve.
     * @return The next card of the specified type, or null if no such card is
     * found.
     *
    private Card getNextCardOfType(List<Card> cards, Card.Type cardType) {
        for (Card card : cards) {
            if (card.cardType() == cardType) {
                if (cards.remove(card)) return card;
                else throw new IllegalArgumentException("Removing" + card + " not present in " + cards);

            }
        }
        throw new IllegalStateException("No card of specified type found");
    }
    */
 
 
    /**
     * Returns the global solution for the game, which contains the actual suspect,
     * weapon, and estate.
     *
     * @return The global solution for the game.
     */
    public ArrayList<String> getGlobalSolution() {
        allCards.add("Lucilla");         // 0
        allCards.add("Bert"   );         // 1
        allCards.add("Malina" );         // 2
        allCards.add("Percy"  );         // 3
        allCards.add("Broom"  );         // 4
        allCards.add("Scissors");        // 5
        allCards.add("Knife"   );        // 6
        allCards.add("Shovel"  );        // 7
        allCards.add("Ipad"    );        // 8
        allCards.add("Haunted_House"   );// 9
        allCards.add("Manic_Manor"     );//10
        allCards.add("Calamity_Castle" );//11
        allCards.add("Peril_Palace"    );//12
        allCards.add("Visitation_Villa");//13
        
        Random random = new Random();
        int culpritIndex = random.nextInt(4);
        int weaponIndex  = random.nextInt(5)+4;
        int houseIndex   = random.nextInt(5)+9;
        ArrayList<String> solution = new ArrayList<>();
        solution.add(allCards.get(culpritIndex));
        solution.add(allCards.get(weaponIndex ));
        solution.add(allCards.get(houseIndex  ));
        
        ArrayList<String> indices = new ArrayList<>();
        for(int i =0;i<14;i++){
            indices.add(String.valueOf(i));
            
        }
        indices.remove(houseIndex  );
        indices.remove(weaponIndex );
        indices.remove(culpritIndex);
        Collections.shuffle(indices);
        int offset = random.nextInt(players.size());
        for(int i = 0;i<11;i++){
        int indexOfCard = Integer.parseInt(indices.get(i));   
        getPlayer((i+offset) % players.size()).addToHand(allCards.get(indexOfCard));

        }
        // Distribute the cards among the players

        return solution;
    }

    /**
     * Checks if each player has a string matching any of the three in player's guess.
     *              Lets players with more than one matching string choose which to share.
     * @param guess A suspect, weapon and state the player believes committed the
     *              crime with and within respectively.
     * @return alibi - The first string matching an element in the players guess,
     *              or null if no matching cards are found.
     */
    public String refute(ArrayList<String> guess) {
        String alibi = null;
        //TODO check if any player can refute the player's guess
        return alibi;
    }

    public Boolean checkSolution(ArrayList<String> guess) {
        // Get the actual solution from the game's globalSolution
        return guess.containsAll(this.solution) && this.solution.containsAll(guess);
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