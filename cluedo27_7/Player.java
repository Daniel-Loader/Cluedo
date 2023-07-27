package cluedo27_7;

import java.util.ArrayList;
import java.util.List;

// Player.java
public class Player{
    private final String name;
    private final Game game;
    private final List<Card> hand;
    private final List<Card> seen;
    private int row;
    private int column;

    public Cell getPlayerLocation() {return game.getBoard().getCellAt(row, column);}

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
        this.row = row;
        this.column = col;
        this.hand = new ArrayList<>();
        this.seen = new ArrayList<>();
        
    }

    public String toString(){return name.substring(0, 2);}

    //Getter and Setter methods for row and column
    public void setRow(int row) { this.row = row;}
    public int row()   {return row;}
    public void setColumn(int col){this.column = col;}
    public int column(){return column;}

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
        if (game.getBoard().getCellAt(row, column) instanceof EstateCell) {
            gameOver = offerGuess();
        }

        for (int i = 0; i < roll && gameOver.equals(false); i++) {
            game.getBoard().print();
            print();
            move();
            if (game.getBoard().getCellAt(row, column) instanceof EstateCell) {
                gameOver = offerGuess();
            }
        }
        return gameOver;
    }

    public void setPlayerLocation(Cell cell) {
        game.getBoard().getCellAt(row, column).removePlayer(this);
        cell.setPlayer(this);
    }

    /**
     * Method to handle user input for moving using characters
     * 'w', 'a', 's', 'd'
     */
    public void move() {
        System.out.println("Enter a command (w, a, s, d) to Move: ");
        char input = Character.toLowerCase(game.getScanner().next().charAt(0));
        
        Cell targetCell = null;
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
    // Other member variables and constructor ...

    /**
     * Checks if a cell is occupied by another player.
     *
     * @param cell The cell to check for occupancy.
     * @return true if the cell is occupied, false otherwise.
     */
    private boolean isCellOccupied(Cell cell) {
        for (Player otherPlayer : game.getPlayers()) {
            if (otherPlayer != this && otherPlayer.getPlayerLocation() == cell) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks that the player can move up before shifting them.
     * Lets them try another direction if they can't move up.
     *
     * @return true if the player moves to a valid square, false if not.
     */
    private boolean moveUp() {
        if (row > 0) {
            Cell playerLocation = game.getBoard().getCellAt(row, column);
            Cell newCell = game.getBoard().getCellAt(row - 1, column);
            if (playerLocation instanceof EstateCell) {
                return ((Habitable) playerLocation).Move(this, "w");
            } else if (newCell instanceof Path && newCell.isPassable()) {
                setPlayerLocation(newCell);
                row--;
                return true;
            } else if (newCell instanceof Entrance) {
                setPlayerLocation(((Entrance) newCell).getEstate());
                row -= 2;
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
        if (row < 23) {
            Cell playerLocation = game.getBoard().getCellAt(row, column);
            Cell newCell = game.getBoard().getCellAt(row + 1, column);
            if (playerLocation instanceof EstateCell) {
                return ((Habitable) playerLocation).Move(this, "s");
            } else if (newCell instanceof Path && newCell.isPassable()) {
                setPlayerLocation(newCell);
                row++;
                return true;
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
        if (column < 23) {
            Cell playerLocation = game.getBoard().getCellAt(row, column);
            Cell newCell = game.getBoard().getCellAt(row, column + 1);
            if (playerLocation instanceof EstateCell) {
                return ((Habitable) playerLocation).Move(this, "d");
            } else if (newCell instanceof Path && newCell.isPassable()) {
                setPlayerLocation(newCell);
                column++;
                return true;
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
        if(row > 0) {
            Cell playerLocation = game.getBoard().getCellAt(row, column);
            Cell newCell = game.getBoard().getCellAt(row, column - 1);
            if (playerLocation instanceof EstateCell) {
                return ((Habitable) playerLocation).Move(this, "a");
            } else if (playerLocation instanceof Path & newCell.isPassable()) {
                setPlayerLocation(newCell);
                column--;
                return true;
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

        List<Card> guess = getGuess();

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

        List<Card> guess = getGuess();

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

    private List<Card> getGuess() {
        System.out.print("Suspect: ");
        String suspectName = game.getScanner().next();
        System.out.print("Weapon: ");
        String weaponName = game.getScanner().next();
        System.out.print("Estate: ");
        String estateName = game.getScanner().next();

        // Create suspect, weapon, and estate cards based on the player's input
        List<Card> guess = new ArrayList<>();
        guess.add(game.getCard(suspectName));
        guess.add(game.getCard(weaponName));
        guess.add(game.getCard(estateName));
        return guess;
    }

    // Implement the logic to check if the guess matches the actual solution
    public Boolean checkSolution(List<Card> guess) {
        // Get the actual solution from the game's globalSolution
        return guess.containsAll(game.getGlobalSolution()) && game.getGlobalSolution().containsAll(guess);
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