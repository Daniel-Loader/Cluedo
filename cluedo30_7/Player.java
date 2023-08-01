package cluedo30_7;

import java.util.ArrayList;
import java.util.List;


public class Player{
    private final String name;
    private final Game game;
    private final List<String> hand;
    private final List<String> seen;
    private int row;
    private int column;

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
    public void addToHand(String card) {
        hand.add(card);
    }

    /**
     * Get the player's hand.
     * @return the player's list of Cards.
     */
    public List<String> getHand() {
        return List.copyOf(hand);
    }

    public void setPlayerLocation(Cell fromCell, Cell toCell) {
        //game.getBoard().getCellAt(row, column).removePlayer(this);
        fromCell.removePlayer(this);
        toCell.setPlayer(this);
    }

    public Cell getPlayerLocation() {return game.getBoard().getCellAt(row, column);}

    /**
     * method to process a players turn.
     * @param roll      how many actions the player can take
     * @return gameOver whether the player has made a successful solution attempt this turn;
     */
    public Boolean turn(int roll) {
        //this is a mess. I will clean it up next time I get a chance.
        char exitStatus = 'c';
        if (getPlayerLocation() instanceof Estate) {
            exitStatus = offerGuess();
        }

        for (int i = 0; i < roll && exitStatus != 't' && exitStatus != 'g'; i++) {
            game.getBoard().print();
            print(roll-i);
            move();
            if (getPlayerLocation() instanceof Estate) {
                exitStatus = offerGuess();
            }
        }
        return (exitStatus == 'g');
    }

    private char offerGuess() {
        System.out.println("Enter 'g' to make a guess or 's' to offer a solution: ");
        System.out.println("Anything else will be treated as a pass");
        char input = game.getScanner().next().charAt(0);

        return switch (input) {
            case 'g' -> makeGuess();
            case 's' -> solutionAttempt();
            default -> 'c';
        };

    }

    private char makeGuess() {
        System.out.println("Enter your guess:");

        ArrayList<String> guess = getGuess();

        // Check if the guess matches the actual solution
        String alibi = game.refute(guess);

        if (alibi == null) {
            System.out.println("No one could refute your guess");
        } else {
            this.seen.add(alibi);
            System.out.printf("%s was not part of the crime\n", alibi);
        }

        return 't';
    }

    /**
     * Method to handle user input for moving using characters
     * 'w', 'a', 's', 'd'
     */
    public void move() {
        System.out.println("Enter a command (w, a, s, d) to Move: ");
        char direction = Character.toLowerCase(game.getScanner().next().charAt(0));

        switch (direction) {
            case 'w' -> {
                if (moveUp()) {
                    System.out.println("You pressed 'w'. Moving up.");
                } else {
                    System.out.println("Up is blocked. Try another direction.");
                    move();
                }
            }
            case 'a' -> {
                if (moveLeft()) {
                    System.out.println("You pressed 'a'. Moving left.");
                } else {
                    System.out.println("Left is blocked. Try another direction.");
                    move();
                }
            }
            case 's' -> {
                if (moveDown()) {
                    System.out.println("You pressed 's'. Moving down.");
                } else {
                    System.out.println("Down is blocked. Try another direction.");
                    move();
                }
            }
            case 'd' -> {
                if (moveRight()) {
                    System.out.println("You pressed 'd'. Moving right.");
                } else {
                    System.out.println("Right is blocked. Try another direction.");
                    move();
                }
            }
            default -> {
                System.out.println("Invalid direction. Try again.");
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
        if (row > 0) {
            Cell playerLocation = game.getBoard().getCellAt(row, column);
            Cell newCell = game.getBoard().getCellAt(row - 1, column);
            if (playerLocation instanceof Estate) {
                return ((Estate) playerLocation).Move(this, "w");
            } else if (newCell instanceof Path && newCell.isPassable()) {
                setPlayerLocation(playerLocation, newCell);
                row--;
                return true;
            } else if (newCell instanceof Entrance) {
                setPlayerLocation(playerLocation, ((Entrance) newCell).getEstate());
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
            if (playerLocation instanceof Estate) {
                return ((Estate) playerLocation).Move(this, "s");
            } else if (newCell instanceof Path && newCell.isPassable()) {
                setPlayerLocation(playerLocation, newCell);
                row++;
                return true;
            } else if (newCell instanceof Entrance) {
                System.out.println("DEBUG ONLY: newCell = " + newCell);
                System.out.println("DEBUG ONLY: (Entrance)newCell = " + newCell);
                System.out.println("DEBUG ONLY: estate = " + ((Entrance)newCell).getEstate());
                //Error could be in getCellAt()???
                setPlayerLocation(playerLocation, ((Entrance) newCell).getEstate());
                row += 2;
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
            if (playerLocation instanceof Estate) {
                return ((Estate) playerLocation).Move(this, "d");
            } else if (newCell instanceof Path && newCell.isPassable()) {
                setPlayerLocation(playerLocation, newCell);
                column++;
                return true;
            } else if (newCell instanceof Entrance) {
                setPlayerLocation(playerLocation, ((Entrance) newCell).getEstate());
                column += 2;
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
        if(column > 0) {
            Cell playerLocation = game.getBoard().getCellAt(row, column);
            Cell newCell = game.getBoard().getCellAt(row, column - 1);
            if (playerLocation instanceof Estate) {
                return ((Estate) playerLocation).Move(this, "a");
            } else if (playerLocation instanceof Path & newCell.isPassable()) {
                setPlayerLocation(playerLocation, newCell);
                column--;
                return true;
            } else if (playerLocation instanceof Entrance) {
                setPlayerLocation(playerLocation, ((Entrance) playerLocation).getEstate());
                column -= 2;
                return true;
            }
        }
        return false;
    }

    private char solutionAttempt() {
        System.out.println("Enter your solution:");

        ArrayList<String> guess = getGuess();

        // Check if the solution attempt matches the actual solution
        Boolean correctSolution = game.checkSolution(guess);

        if (correctSolution) {
            System.out.println("Congratulations " + this.name + "! You successfully solved the mystery.");
            return 'g';
            // Implement the logic to reveal the solution and end the game here
        } else {
            System.out.println("Your solution attempt is incorrect.");
            return 't';
        }
    }

    private ArrayList<String> getGuess() {

        // add suspect, weapon, and estate cards based on the player's input
        ArrayList<String> guess = new ArrayList<>();
        guess.add(game.getSuspectCard());
        guess.add(game.getWeaponCard());
        guess.add(game.getEstateCard());
        return guess;
    }

    /**
     * Prints out the indexes and names of the cards in the players
     * hand.
     */
    public void printHand() {
        for (int i = 0; i < hand.size(); i++) {
            System.out.printf("%d : %s\n", i, hand.get(i));
        }
        System.out.println("----------");
    }

    /**
     * Prints the players' name and their hand to the console
     */
    public void print(int movesLeft) {
        System.out.printf("%s's Turn: %d moves left\n", this.getName(), movesLeft);
        System.out.println("Hand:");
        for (String card : getHand()) {
            System.out.println(card);
        }
        System.out.println("Seen:");
        for (String card : seen) {
            System.out.println(card);
        }
        System.out.println("----------");
    }

    public Game getGame() {
        return game;
    }
}