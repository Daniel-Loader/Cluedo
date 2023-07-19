import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

// Player.java
public class Player implements Cell {
    private String name;
	private int row;
	private int column;
    private Set<Card> hand;

    public Player(String name, int row, int column) {
    	this.name = name;
    	this.row = row;
    	this.column = column;
        this.hand = new HashSet<Card>();
    }

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
        Scanner scanner = new Scanner(System.in);

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

        scanner.close();
	}

    //Checks that the player can move upwards before shifting them
    //Lets them try another direction if they can't move upwards
	private void moveUp(Board board) {
    	if (row > 0 && board.getCellAt(row-1, column).isPassable()) {
    		//board.getCell(this).removePlayer(this);
    		System.out.println("You pressed 'w'. Moving up.");
        	row--;
        	//board.getCell(this).addPlayer(this);
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

    private Boolean offerGuess() {
    	Scanner scanner = new Scanner(System.in);

        System.out.println("Enter 'g' to make a guess or 's' to offer a solution: ");
        System.out.println("Anything else will be treated as a pass");
        char input = scanner.next().charAt(0);

        switch (input) {
            case 'g':
            	scanner.close();
                return makeGuess();
            case 's':
            	scanner.close();
                return solutionAttempt();
            default:
            	scanner.close();
            	return false;
        }

	}

    private Boolean solutionAttempt() {
    	//TODO implement this
    	return false;
	}

	private Boolean makeGuess() {
		//TODO implement this
    	return false;
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
        Player player = new Player("John", 0, 0);

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
