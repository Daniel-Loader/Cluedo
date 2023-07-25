package cluedo25_7;

//TODO Write a class description
//TODO Update print, printHand
// and seen cards when a players
// guess is refuted
//TODO prevent players who make a
// failed solution attempt from
// making further guesses
//TODO refactor turn and have
// player move into an Estate
// when they move to an entrance.
import java.util.ArrayList;
import java.util.List;

// Player.java
public class Player{
    private final String name;
    private final Game game;
    private final List<Card> hand;
    private final List<Card> seen;
    private Cell playerLocation;

    //TODO: Use Cell, NOT row and column.
    /**
     * Constructs a new Player object.
     *
     * @param name     The name of the player.
     * @param game     The game the player is a part of
     * @param row      The initial row position of the player on the game board.
     * @param col      The initial column position of the player on the game board.
     */
    public Player(String name, Game game, int row, int col) {
        this.name = name;
        this.game = game;
        this.playerLocation = game.getBoard().getCellAt(row, col);
        this.hand = new ArrayList<>();
        this.seen = new ArrayList<>();
        
    }

    public String toString(){return name.substring(0, 2) + "";}

    //Getter methods for row and column
    public int row()   {return playerLocation.getRow();}
    public int column(){return playerLocation.getColumn();}

    /**
     * Getter method for 'name'.
     */
    public String getName() {
        return name;
    }

    /**
     * Add a card to the player's hand.
     * @param card The card to add.
     */
    public void addToHand(Card card) {
        hand.add(card);
    }

    /**
     * Get the player's hand.
     * @return the player's list of Cards.
     */
    public List<Card> getHand() {
        return List.copyOf(hand);
    }

    /**
     * method to process a players turn.
     * @param roll      how many actions the player can take
     * @return gameOver whether the player has made a successful solution attempt this turn;
     */
    public Boolean turn(int roll) {
        //this is a mess. I will clean it up next time I get a chance.
        Boolean gameOver = false;
        game.getBoard().print();
        print();
        if (playerLocation instanceof EstateCell) {
            gameOver = offerGuess();
        }

        for (int i = 0; i < roll && gameOver.equals(false); i++) {
            game.getBoard().print();
            print();
            move();
            if (playerLocation instanceof EstateCell) {
                gameOver = offerGuess();
            }
        }
        return gameOver;
    }

    public void setPlayerLocation(Cell cell) {
        playerLocation.removePlayer(this);
        playerLocation = cell;
        playerLocation.setPlayer(this);
    }

    /**
     * Method to handle user input for moving using characters
     * 'w', 'a', 's', 'd'
     */
    public void move() {
        System.out.println("Enter a command (w, a, s, d) to Move: ");
        char input = game.getScanner().next().charAt(0);

        switch (input) {
            case 'w' -> {
                if (moveUp()) {
                    System.out.println("You pressed 'w'. Moving up.");
                } else {
                    System.out.println("Up is blocked. Try another direction.");
                }
            }
            case 'a' -> {
                if (moveLeft()) {
                    System.out.println("You pressed 'a'. Moving left.");
                } else {
                    System.out.println("Left is blocked. Try another direction.");
                }
            }
            case 's' -> {
                if (moveDown()) {
                    System.out.println("You pressed 's'. Moving down.");
                } else {
                    System.out.println("Down is blocked. Try another direction.");
                }
            }
            case 'd' -> {
                if (moveRight()) {
                    System.out.println("You pressed 'd'. Moving right.");
                } else {
                    System.out.println("Right is blocked. Try another direction.");
                }
            }
            default -> {
                System.out.println("Invalid input. Try again.");
                move();
            }
        }
    }

    /**
     * Checks that the player can move up before shifting them.
     * Lets them try another direction if they can't move up.
     *
     * @return true if the player moves to a valid square, false if not.
     */
    private boolean moveUp() {
        if (playerLocation.getRow() > 0) {
            Cell newCell = game.getBoard().getCellAt(playerLocation.getRow() - 1, playerLocation.getColumn());
            if (newCell instanceof Path && newCell.isPassable()) {
                setPlayerLocation(newCell);
                return true;
            } else if (newCell instanceof EstateCell) {
                Habitable estate = (Habitable) newCell;
                return estate.Move(this, "w");
            } else if (newCell instanceof Entrance) {
                setPlayerLocation(((Entrance) newCell).getEstate());
                return true;
            } else {
                System.out.println("Up is blocked. Try another direction.");
                return false;
            }
        }
        return false;
    }

    /**
     * Checks that the player can move down before shifting them.
     * Lets them try another direction if they can't move down.
     *
     * @return true if the player moves to a valid square, false if not.
     */
    private boolean moveDown() {
        if (playerLocation.getRow() < 23) {
            Cell newCell = game.getBoard().getCellAt(playerLocation.getRow() + 1, playerLocation.getColumn());
            if (newCell instanceof Path && newCell.isPassable()) {
                setPlayerLocation(newCell);
                return true;
            } else if (newCell instanceof EstateCell) {
                Habitable estate = (Habitable) newCell;
                return estate.Move(this, "s");
            } else if (newCell instanceof Entrance) {
                setPlayerLocation(((Entrance) newCell).getEstate());
                return true;
            }
        }
        return false;
    }

    /**
     * Checks that the player can move right before shifting them.
     * Lets them try another direction if they can't move right.
     *
     * @return true if the player moves to a valid square, false if not.
     */
    private boolean moveRight() {
        if (playerLocation.getColumn() < 23) {
            Cell newCell = game.getBoard().getCellAt(playerLocation.getRow(), playerLocation.getColumn() + 1);
            if (newCell instanceof Path && newCell.isPassable()) {
                return true;
            } else if (newCell instanceof EstateCell) {
                return ((EstateCell)playerLocation).Move(this, "d");
            } else if (newCell instanceof Entrance) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks that the player can move left before shifting them
     * Lets them try another direction if they can't move left
     * @return true if the player moves to a valid square, false if not.
     */
	private boolean moveLeft() {
        if(playerLocation.getRow() > 0) {
            Cell newCell = game.getBoard().getCellAt(playerLocation.getRow(), playerLocation.getColumn() - 1);
            if (playerLocation instanceof Path & newCell.isPassable()) {
                setPlayerLocation(newCell);
            } else if (playerLocation instanceof EstateCell) {
                Habitable estate = (Habitable) playerLocation;
                return estate.Move(this, "a");
            } else if (playerLocation instanceof Entrance) {
                setPlayerLocation(((Entrance) playerLocation).getEstate());
                return true;
            }
        }
        return false;
    }

    private Boolean offerGuess() {
        System.out.println("Enter 'g' to make a guess or 's' to offer a solution: ");
        System.out.println("Anything else will be treated as a pass");
        char input = game.getScanner().next().charAt(0);

        return switch (input) {
            case 'g' -> makeGuess();
            case 's' -> solutionAttempt();
            default -> false;
        };

	}

    private Boolean makeGuess() {
        System.out.println("Enter your guess:");

        Card[] guess = getGuess();

        // Check if the guess matches the actual solution
        Boolean correctGuess = checkSolution(guess);

        if (correctGuess) {
            System.out.println("Congratulations! Your guess is correct.");
        } else {
            System.out.println("Your guess is incorrect.");
        }

        return true;
    }

    private Boolean solutionAttempt() {
        System.out.println("Enter your solution:");

        Card[] guess = getGuess();

        // Check if the solution attempt matches the actual solution
        Boolean correctSolution = checkSolution(guess);

        if (correctSolution) {
            System.out.println("Congratulations! You successfully solved the mystery.");
            // Implement the logic to reveal the solution and end the game here
        } else {
            System.out.println("Your solution attempt is incorrect.");
        }

        return true;
    }

    private Card[] getGuess() {
        System.out.print("Suspect: ");
        String suspectName = game.getScanner().next();
        System.out.print("Weapon: ");
        String weaponName = game.getScanner().next();
        System.out.print("Estate: ");
        String estateName = game.getScanner().next();

        // Create suspect, weapon, and estate cards based on the player's input
        Card[] guess = new Card[3];
        guess[0] = new Suspect(suspectName, this);
        guess[1] = new Weapon(weaponName, this);
        guess[2] = new Estate(estateName, this);
        return guess;
    }

    // Implement the logic to check if the guess matches the actual solution
    public Boolean checkSolution(Card[] guess) {
        // Get the actual solution from the game's globalSolution
        Suspect actualSuspect = game.getGlobalSolution().getSuspect();
        Weapon actualWeapon = game.getGlobalSolution().getWeapon();
        Estate actualEstate = game.getGlobalSolution().getEstate();

        // Compare the player's guess with the actual solution
        boolean isCorrectSuspect = actualSuspect.equals(guess[0]);
        boolean isCorrectWeapon  =  actualWeapon.equals(guess[1]);
        boolean isCorrectEstate  =  actualEstate.equals(guess[2]);

        // If all three components of the guess match the actual solution, it's correct.
        return isCorrectSuspect && isCorrectWeapon && isCorrectEstate;
    }

    /**
     * Prints out the indexes and names of the cards in the players
     * hand.
     */
    public void printHand() {
        for (int i = 0; i < hand.size(); i++) {
            System.out.printf("%d : %s\n", i, hand.get(i).toString());
        }
        System.out.println("----------");
    }

    /**
     * Prints the players' name and their hand to the console
     */
    public void print() {
        System.out.println("Player: " + this.getName());
        System.out.println("Hand:");
        for (Card card : getHand()) {
            System.out.println(card);
        }
        System.out.println("----------");
    }

    public Game getGame() {
        return game;
    }
}