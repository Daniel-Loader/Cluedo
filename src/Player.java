import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

// Player.java
public class Player implements Cell {
	Scanner scanner;
    private String name;
	private int row;
	private int column;
    private Set<Card> hand;
    private Game game;

    //TODO: Use Cell, NOT row and column.
    public Player(String name, int row, int column, Scanner scanner) {
    	this.name = name;
    	this.row = row;
    	this.column = column;
        this.hand = new HashSet<Card>();
        this.scanner = scanner;
        
    }
    public void setGame(Game game) {
        this.game = game;
    }

    public String toString(){return "'" + name.toCharArray()[0] + "'";}

    //Getter methods for row and column
    public int row(){return row;}
    public int column(){return column;}

    // Getter and Setter methods for 'name'
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Add a card to the player's hand
    public void addToHand(Card card) {
        hand.add(card);
    }

    // Remove a card from the player's hand
    public void removeFromHand(Card card) {
        hand.remove(card);
    }

    // Get the player's hand
    public Set<Card> getHand() {
        return hand;
    }

    // Print players' names and their hands to the console
    public void print() {
        System.out.println("Player: " + this.getName());
        System.out.println("Hand:");
        for (Card card : this.getHand()) {
            System.out.println(card);
        }
        System.out.println("----------");
    }

    public void printHand() {
    	System.out.println("Hand:");
    	for (Card card : this.getHand()) {
            System.out.println(card);
        }
        System.out.println("----------");
	}

    // Method to handle user input for moving using characters
    // 'w', 'a', 's', 'd', and 'g'
    public void move(Board board) {
        System.out.println("Enter a command (w, a, s, d) to Move: ");
        char input = scanner.next().charAt(0);

        switch (input) {
            case 'w':
                // Perform action for 'w'
                moveUp(board);
                break;
            case 'a':
                // Perform action for 'a'
                moveLeft(board);
                break;
            case 's':
                // Perform action for 's'
                moveDown(board);
                break;
            case 'd':
                // Perform action for 'd'
            	moveRight(board);
                break;
            default:
                // Handle any other input
                System.out.println("Invalid input. Try again.");
                move(board);
        }
	}

    //Checks that the player can move upwards before shifting them
    //Lets them try another direction if they can't move upwards
	private void moveUp(Board board) {
        Cell newCell = board.getCellAt(row-1, column);
    	if (row > 0 && newCell instanceof Path) {
            Path newPath = (Path)newCell;
    		newPath.removePlayer(this);
    		System.out.println("You pressed 'w'. Moving up.");
        	row--;
        	newPath.setPlayer(this);
        } else if(newCell instanceof EstateCell){
            moveEstate("up");
        } else {
        	// Don't leave the board
            System.out.println("Up is blocked. Try another diection.");
            move(board);
        }

	}

	//Checks that the player can move left before shifting them
    //Lets them try another direction if they can't move left
	private void moveLeft(Board board) {
    	if (column > 0 && board.getCellAt(row, column-1).isPassable()) {
        	//board.getCellAt(row, column).removePlayer(this);
        	System.out.println("You pressed 'a'. Moving left.");
        	column--;
        	//board.getCellAt(row, column).addPlayer(this);
    	} else {
        	// Don't leave the board
    		System.out.println("Left is blocked. Try another diection.");
            move(board);
    	}

    }

	//Checks that the player can move downwards before shifting them
    //Lets them try another direction if they can't move downwards
	private void moveDown(Board board) {
		if (this.row < 23) {
			//board.getCellAt(row, column).removePlayer(this);
    		System.out.println("You pressed 's'. Moving down.");
        	row++;
        	//board.getCellAt(row, column).addPlayer(this);
        } else {
        	// Don't leave the board
            System.out.println("Down is blocked. Try another diection.");
            move(board);
        }

	}

	//Checks that the player can move right before shifting them
    //Lets them try another direction if they can't move right
	private void moveRight(Board board) {
    	if (column < 23 && board.getCellAt(row, column+1).isPassable()) {
    		//board.getCellAt(row, column).removePlayer(this);
        	System.out.println("You pressed 'd'. Moving right.");
        	column++;
        	//board.getCellAt(row, column).removePlayer(this);
    	} else {
    		System.out.println("Right is blocked. Try another diection.");
            move(board);
    	}

    }

    /**
     * Move out of the estate.
     * Direction can either be "left", "right", "up" or "down".
     */
    private void moveEstate(String direction){

    }

    private Boolean offerGuess() {

        System.out.println("Enter 'g' to make a guess or 's' to offer a solution: ");
        System.out.println("Anything else will be treated as a pass");
        char input = scanner.next().charAt(0);

        switch (input) {
            case 'g':
                return makeGuess();
            case 's':
                return solutionAttempt();
            default:
            	return false;
        }

	}

    private Boolean makeGuess() {
        System.out.println("Enter your guess:");
        System.out.print("Suspect: ");
        String suspectName = scanner.next();
        System.out.print("Weapon: ");
        String weaponName = scanner.next();
        System.out.print("Estate: ");
        String estateName = scanner.next();

        // Create suspect, weapon, and estate cards based on the player's input
        Suspect suspectCard = new Suspect(suspectName, this);
        Weapon weaponCard = new Weapon(weaponName, this);
        Estate estateCard = new Estate(estateName, this);

        // Check if the guess matches the actual solution
        Boolean correctGuess = checkSolution(suspectCard, weaponCard, estateCard);

        if (correctGuess) {
            System.out.println("Congratulations! Your guess is correct.");
        } else {
            System.out.println("Your guess is incorrect.");
        }

        return true;
    }

    // Implement the logic to check if the guess matches the actual solution
    public Boolean checkSolution(Suspect suspect, Weapon weapon, Estate estate) {
        // Get the actual solution from the game's globalSolution
        Suspect actualSuspect = game.getGlobalSolution().getSuspect();
        Weapon actualWeapon = game.getGlobalSolution().getWeapon();
        Estate actualEstate = game.getGlobalSolution().getEstate();

        // Compare the player's guess with the actual solution
        boolean isCorrectSuspect = suspect.equals(actualSuspect);
        boolean isCorrectWeapon = weapon.equals(actualWeapon);
        boolean isCorrectEstate = estate.equals(actualEstate);

        // If all three components of the guess match the actual solution, it's correct.
        return isCorrectSuspect && isCorrectWeapon && isCorrectEstate;
    }



    private Boolean solutionAttempt() {
        System.out.println("Enter your solution:");
        System.out.print("Suspect: ");
        String suspectName = scanner.next();
        System.out.print("Weapon: ");
        String weaponName = scanner.next();
        System.out.print("Estate: ");
        String estateName = scanner.next();

        // Create suspect, weapon, and estate cards based on the player's input
        Suspect suspectCard = new Suspect(suspectName, this);
        Weapon weaponCard = new Weapon(weaponName, this);
        Estate estateCard = new Estate(estateName, this);

        // Check if the solution attempt matches the actual solution
        Boolean correctSolution = checkSolution(suspectCard, weaponCard, estateCard);

        if (correctSolution) {
            System.out.println("Congratulations! You successfully solved the mystery.");
            // Implement the logic to reveal the solution and end the game here
        } else {
            System.out.println("Your solution attempt is incorrect.");
        }

        return true;
    }

    

	public Boolean turn(Board board, int roll) {
		//this is a mess. I will clean it up next time I get a chance.
    	Boolean turnOver = false;
    	board.print();
		print();
    	if (board.getCellAt(row, column) instanceof Entrance) {
			turnOver = offerGuess();
		}

		for (int i = 0; i < roll && turnOver.equals(false); i++) {
			board.print();
			print();
			move(board);
			if (board.getCellAt(row, column) instanceof Entrance) {
				turnOver = offerGuess();
				break;
			}
		}
		return false;
	}

	public Cell clone() {
		//TODO why does player need this?
		return null;
	}

	public static void main(String[] args) {
        // Creating a Player
        Player player = new Player("John", 0, 0, null);

        // Creating a Suspect card and assigning it to the player
        Suspect suspectCard = new Suspect("Colonel Mustard", player);

        // Adding the Suspect card to the player's hand
        player.addToHand(suspectCard);

        // Creating a Weapon card and assigning it to the player
        Weapon weaponCard = new Weapon("Revolver", player);

        // Adding the Weapon card to the player's hand
        player.addToHand(weaponCard);

        // Creating an Estate card and assigning it to the player
        Estate estateCard = new Estate("Library", player);

        // Adding the Estate card to the player's hand
        player.addToHand(estateCard);

        // Printing the player's name and their hand
        System.out.println("Player: " + player.getName());
        System.out.println("Hand:");
        for (Card card : player.getHand()) {
            System.out.println(card);
        }
    }
	

	@Override
	public Boolean contains(Player p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isPassable() {
		// TODO Auto-generated method stub
		return null;
	}
}
