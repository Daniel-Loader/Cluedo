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
    private final ArrayList<Player> players;
    private ArrayList<Card> allCards;
    private HashSet<Card> solution;

    /**
     * Constructs a new Game instance with an initialized board, 3-4 players,
     * and a randomly generated solution that will be different every time
     * main is run.
     */
    public Game() {
        this.board = new Board(24, 24);
        this.scanner = new Scanner(System.in);
        this.players = new ArrayList<>(); addPlayers();
        initializeCards();
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
            getPlayer(i).getPlayerLocation().setPlayer(getPlayer(i)); //Set the player on the board cell.
        }
    }
    
    public List<Player> getPlayers() {
    	return List.copyOf(players);
    }
    
    public Player getPlayer(int i) {
    	return players.get(i);
    }
    public Card getCard(String suspectName) {
        for (Card c : allCards) {
            if (c.name().equals(suspectName)) {
                return c;
            }
        }
        throw new IllegalArgumentException(String.format("%s isn't a valid Card name", suspectName));
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
    private Card getNextCardOfType(Card.Type cardType) {
        Iterator<Card> iterator = allCards.iterator();
        while (iterator.hasNext()) {
            Card card = iterator.next();
            if (card.cardType() == cardType) {
                iterator.remove(); // Remove the card from the list
                return card;
            }
        }
        throw new IllegalStateException("No card of specified type found");
    }

    /**
     * Initializes the global solution for the game, which contains the suspect,
     * weapon, and estate and the players' hands.
     */
    public void initializeCards() {
        this.allCards = new ArrayList<>();
        allCards.add(new Card("Lucilla",          Card.Type.SUSPECT));
        allCards.add(new Card("Bert",             Card.Type.SUSPECT));
        allCards.add(new Card("Malina",           Card.Type.SUSPECT));
        allCards.add(new Card("Percy",            Card.Type.SUSPECT));
        allCards.add(new Card("Revolver"   ,      Card.Type.WEAPON));
        allCards.add(new Card("Candlestick",      Card.Type.WEAPON));
        allCards.add(new Card("Knife",            Card.Type.WEAPON));
        allCards.add(new Card("Rope",             Card.Type.WEAPON));
        allCards.add(new Card("Haunted House",    Card.Type.ESTATE));
        allCards.add(new Card("Manic Manor",      Card.Type.ESTATE));
        allCards.add(new Card("Calamity Castle",  Card.Type.ESTATE));
        allCards.add(new Card("Peril Palace",     Card.Type.ESTATE));
        allCards.add(new Card("Visitation Villa", Card.Type.ESTATE));
        Collections.shuffle(allCards);

        this.solution = new HashSet<>();
        solution.add(getNextCardOfType(Card.Type.SUSPECT));
        solution.add(getNextCardOfType(Card.Type.WEAPON));
        solution.add(getNextCardOfType(Card.Type.ESTATE));

        for (int i = 0; i < 3; i++) {
            players.get(i).getHand().addAll(allCards.subList(3*i, 3*(i+1)));
        }
        if (players.size() == 3) {
            players.get(0).getHand().add(allCards.get(9));
            players.get(1).getHand().add(allCards.get(10));
        } else {//There are 4 players
            players.get(3).getHand().addAll(allCards.subList(9, 11));
        }

        allCards.addAll(solution);
    }

    public Boolean makeGuess() {
        System.out.println("Enter your guess:");

        HashSet<Card> guess = getGuess();

        // Check if the guess matches the actual solution
        Boolean correctGuess = checkSolution(guess);

        if (correctGuess) {
            System.out.println("Congratulations! Your guess is correct.");
        } else {
            System.out.println("Your guess is incorrect.");
        }

        return true;
    }

    public Boolean solutionAttempt() {
        System.out.println("Enter your solution:");

        HashSet<Card> guess = getGuess();

        // Check if the solution attempt matches the actual solution
        return checkSolution(guess);
    }

    private HashSet<Card> getGuess() {
        System.out.print("Suspect: ");
        String suspectName = scanner.nextLine();
        System.out.print("Weapon: ");
        String weaponName = scanner.nextLine();
        System.out.print("Estate: ");
        String estateName = scanner.nextLine();

        // Create suspect, weapon, and estate cards based on the player's input
        HashSet<Card> guess = new HashSet<>();
        guess.add(getCard(suspectName));
        guess.add(getCard(weaponName));
        guess.add(getCard(estateName));
        return guess;
    }

    /**
     * Compares the players guess to the solution
     * @param guess the players guess
     * @return gameOver; whether the solution is the same as the players guess.
     */
    public Boolean checkSolution(HashSet<Card> guess) {
        // Get the actual solution from the game's globalSolution
        return guess.containsAll(this.solution) && this.solution.containsAll(guess);
    }

    /**
     * Closes all open resources, such as the Scanner, at the end of the game.
     */
    public void terminate() {
        System.out.println("Congratulations! You successfully solved the mystery.");
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