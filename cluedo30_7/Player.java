package cluedo30_7;

import java.util.ArrayList;
import java.util.List;

/**
 * The Player class handles all the details of moving the players across the
 * board except in the case of a player within an estate, which is handled by
 * the estate, as well as making guesses and solve attempts.
 */
public class Player{
    private final String name;
    private final Game game;
    private final List<String> hand;
    private final List<String> seen;
    private final List<Cell> used;
    private boolean canSolve = true;
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
        this.used = new ArrayList<>();

    }

    public String toString(){return name.substring(0, 2);}

    //Getter and Setter methods for row and column
    public boolean canSolve()   {return canSolve;}
    public void setRow(int row) { this.row = row;}
    public int row()   {return row;}
    public void setColumn(int col){this.column = col;}
    public int column(){return column;}

    /**
     * Getter method for 'name'.
     */
    public String name() {
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
    public List<String> hand() {
        return List.copyOf(hand);
    }

    /**
     * setter method for the cell containing this player.
     *
     * @param fromCell the cell the player came from.
     * @param   toCell the cell to move the player into.
     * @throws IllegalArgumentException if the player isn't located in fromCell or toCell is full.
     */
    public void setPlayerLocation(Cell fromCell, Cell toCell) {
        fromCell.removePlayer(this);
        toCell.setPlayer(this);
    }

    /*
     * getter method for the cell containing this player
     *
     * @return playerLocation - the cell located on the players row and column.
     */
    public Cell playerLocation() {return game.board().cellAt(row, column);}

    /**
     * method to process a players turn.
     * @param roll      how many actions the player can take
     * @return gameOver whether the player has made a successful solution attempt this turn;
     */
    public boolean turn(int roll) {
        char exitStatus = 'c';
        this.used.clear();
        this.used.add(game.board().cellAt(row, column));
        for (int i = 0; i < roll; i++) {
            game.board().print();
            System.out.printf("%s's Turn: %d moves left\n", this.name(), roll-i);
            if (playerLocation() instanceof Estate) {
                print();
                exitStatus = offerGuess();
            }
            if (exitStatus == 't' || exitStatus == 'g') {
                break;
            } else {
                move();
            }
        }
        return (exitStatus == 'g');
    }

    /**
     * Method to handle user input for moving using characters
     * 'w', 'a', 's', 'd'
     */
    public void move() {
        System.out.println("Enter a command (w, a, s, d) to move: ");
        char direction = Character.toLowerCase(game.scanner().next().charAt(0));

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
            Cell playerLocation = game.board().cellAt(row, column);
            Cell newCell = game.board().cellAt(row - 1, column);
            if (used.contains(newCell)) {
                return false;
            } else {
                used.add(newCell);
            }
            if (playerLocation instanceof Estate) {
                return ((Estate) playerLocation).move(this, "w");
            } else if (newCell instanceof Path && newCell.isPassable()) {
                setPlayerLocation(playerLocation, newCell);
                row--;
                return true;
            } else if (newCell instanceof Entrance) {
                setPlayerLocation(playerLocation, ((Entrance) newCell).estate());
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
            Cell playerLocation = game.board().cellAt(row, column);
            Cell newCell = game.board().cellAt(row + 1, column);
            if (used.contains(newCell)) {
                return false;
            } else {
                used.add(newCell);
            }
            if (playerLocation instanceof Estate) {
                return ((Estate) playerLocation).move(this, "s");
            } else if (newCell instanceof Path && newCell.isPassable()) {
                setPlayerLocation(playerLocation, newCell);
                row++;
                return true;
            } else if (newCell instanceof Entrance) {
                System.out.println("DEBUG ONLY: newCell = " + newCell);
                System.out.println("DEBUG ONLY: (Entrance)newCell = " + newCell);
                System.out.println("DEBUG ONLY: estate = " + ((Entrance)newCell).estate());
                //Error could be in cellAt()???
                setPlayerLocation(playerLocation, ((Entrance) newCell).estate());
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
            Cell playerLocation = game.board().cellAt(row, column);
            Cell newCell = game.board().cellAt(row, column + 1);
            if (used.contains(newCell)) {
                return false;
            } else {
                used.add(newCell);
            }
            if (playerLocation instanceof Estate) {
                return ((Estate) playerLocation).move(this, "d");
            } else if (newCell instanceof Path && newCell.isPassable()) {
                setPlayerLocation(playerLocation, newCell);
                column++;
                return true;
            } else if (newCell instanceof Entrance) {
                setPlayerLocation(playerLocation, ((Entrance) newCell).estate());
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
            Cell playerLocation = game.board().cellAt(row, column);
            Cell newCell = game.board().cellAt(row, column - 1);
            if (used.contains(newCell)) {
                return false;
            } else {
                used.add(newCell);
            }
            if (playerLocation instanceof Estate) {
                return ((Estate) playerLocation).move(this, "a");
            } else if (newCell instanceof Path & newCell.isPassable()) {
                setPlayerLocation(playerLocation, newCell);
                column--;
                return true;
            } else if (newCell instanceof Entrance) {
                Estate estate = ((Entrance) newCell).estate();
                if (!used.contains(estate)) {
                    used.add(estate);
                    setPlayerLocation(playerLocation, estate);
                    column -= 2;
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * asks the player if they would like to make a guess or solution attempt.
     *
     * @return char - 'g' for game over, 't' for turn  over, or 'c' for continue.
     */
    private char offerGuess() {
        System.out.println("Enter 'g' to make a guess or 's' to offer a solution: ");
        System.out.println("Anything else will be treated as a pass");
        char input = game.scanner().next().charAt(0);

        return switch (input) {
            case 'g' -> makeGuess();
            case 's' -> solutionAttempt();
            default -> 'c';
        };
    }

    /**
     * asks the player for valid suspect, weapon and estate which it compares
     * to each other player's hand to see if anyone can refute the guess.
     *
     * @return char - 't' for turn over
     */
    private char makeGuess() {
        System.out.println("Enter your guess:");

        ArrayList<String> guess = guess();

        // Check if the guess can be refuted.
        String alibi = game.refute(this, guess);

        if (alibi == null) {
            System.out.println("No one could refute your guess");
        } else {
            this.seen.add(alibi);
            System.out.printf("%s was not part of the crime\n", alibi);
        }

        return 't';
    }

    /**
     * asks the player for valid suspect, weapon and estate which it compares
     * to the true murderer, weapon and crime scene.
     *
     * @return char - 'g' for game over or 't' for turn over.
     */
    private char solutionAttempt() {
        if (canSolve) {
            System.out.println("Enter your solution:");
            ArrayList<String> guess = guess();
            // Check if the solution attempt matches the actual solution
            canSolve = game.checkSolution(guess);
        } else {
            System.out.println("You already made a solve attempt");
            return offerGuess();
        }
        if (canSolve) {
            System.out.println("Congratulations " + this.name + "! You successfully solved the mystery.");
            return 'g';
        } else {
            System.out.println("Your solution attempt is incorrect.");
            return 't';
        }
    }

    /**
     * asks the player for valid suspect, weapon and estate which it adds to a list
     *
     * @return guess - the list of suspect, weapon and estate
     */
    private ArrayList<String> guess() {

        // add suspect, weapon, and estate cards based on the player's input
        ArrayList<String> guess = new ArrayList<>();
        guess.add(game.suspectCard());
        guess.add(game.weaponCard());
        guess.add(((Estate) playerLocation()).name());
        return guess;
    }

    public String refute(ArrayList<String> guess) {
        boolean containsAny = hand.stream()
                .map(guess::contains)
                .toList().contains(true);
        String alibi = null;
        if (containsAny) {
            while (!guess.contains(alibi)) {
                System.out.printf("Choose a card from %s that is in %s to share\n",
                        hand, guess);
                alibi = game.scanner().nextLine();
            }
        }
        System.out.println(this.name + " has refuted. Pass Tablet to next player.");
        return alibi;
    }

    /**
     * Prints the players' name and their hand to the console
     */
    public void print() {
        System.out.println("\nHand:");
        System.out.println("----------");
        for (String card : hand()) {
            System.out.println(card);
        }
        System.out.println("\nSeen:");
        System.out.println("----------");
        for (String card : seen) {
            System.out.println(card);
        }
        System.out.println("----------");
    }

    public Game game() {
        return game;
    }
}