package cluedo25_7;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The Estate Cell class represents a generic estate for our Cluedo game.
 * Each EstateCell instance has an array of lines representing the visual appearance of the estate,
 * a list of occupants (players) currently present in the estate, and entrance strings for each direction.
 * The estate can be a HauntedHouse or any other custom estate that implements the Habitable interface.
 */
public class Estate implements Cell {
	@SuppressWarnings("unused")
    private final Map<Direction, String> entranceStrings = Board.ENTRANCESTRINGS;
	public	final String wallString = Board.WALLSTRING;
    private final int row;
    private ArrayList<Player> occupants = new ArrayList<Player>();
    private ArrayList<Entrance> entrances;
    private String[] lines = new String[5];
    
    Estate(int row) {
    	this.row = row;
    }
    
    /**
     * Creates a deep copy of the EstateCell.
     *
     * @return A new EstateCell object that is a copy of this cell.
     */
    @Override
	public Cell copy() {
    	throw new IllegalStateException("Estates shouldn't be duplicated");
	}

    /**
     * Getter method for the estates starting row;
     * @return row - the uppermost row of the estate
     */
    private int getRow() { return row; }
	
	@SuppressWarnings("unused")
	private void setOccupants(ArrayList<Player> ps) {
		this.occupants = ps;
	}
	
	/**
     * Retrieves the list of players (occupants) currently present in the estate.
    *
    * @return The list of players occupying the estate.
    */
   public ArrayList<Player> getOccupants() {
       return occupants;
   }

   /**
    * Adds a player to the list of occupants in the estate.
    *
    * @param p The player to be added to the estate.
    */
   public void addOccupant(Player p) {
       this.occupants.add(p);
   }

   /**
    * Removes a player from the list of occupants in the estate.
    *
    * @param p The player to be removed from the estate.
    * @return The removed player if found.
    * @throws IllegalArgumentException If the player is not present in the estate.
    */
   public Player removeOccupant(Player p) throws IllegalArgumentException {
       if (occupants.contains(p)) {
           int i = occupants.indexOf(p);
           return this.occupants.remove(i);
       } else {
           throw new IllegalArgumentException(p.getName() + " is not present");
       }
   }
   
   /**
    * Retrieves the player strings (abbreviated names) of the occupants, right-padded to 12 characters.
    *
    * @return The player strings representing the occupants of the estate.
    */
   public String getPlayerStrings() {
       String players = getOccupants().stream()
               .map(p -> p.getName().substring(0, 2))
               .collect(Collectors.joining(" "));
       return String.format("%1$-" + 12 + "s", players);
   }
	   
	public boolean Move(Player p, String d) {throw new Error("Generic Estates shouldn't have people in them");}
	
	public void setLines(String[] ls) {
		this.lines = ls;
	}
	
	/**
     * Retrieves the lines representing the visual appearance of the estate.
     *
     * @return The array of strings representing the estate's visual appearance.
     */
    public String[] getLines() { return lines; }
    


	public String getLineString(int i) {
		return lines[i-getRow()];
	}
    
    private void setEntrances(ArrayList<Entrance> es) {
		this.entrances = es;
		
	}
	
	@SuppressWarnings("unused")
	private void addEntrance(Entrance e) {
		this.entrances.add(e);
	}

    /**
     * Checks if a players can move onto this EstateCell.
     *
     * @return true, indicating that the EstateCell is always able to be entered.
     */
    @Override
    public Boolean isPassable() {
        return false;
    }

    @Override
    public void setPlayer(Player player) {
        addOccupant(player);
    }

    @Override
    public void removePlayer(Player player) {
        removeOccupant(player);
    }

    /**
     * Retrieves the entrance string corresponding to the given direction.
     *
     * @param key The direction (UP, DOWN, LEFT, RIGHT) for which the entrance string is to be retrieved.
     * @return The entrance string for the specified direction.
     */
    public String getEntranceString(Direction key) {
        return entranceStrings.get(key);
    }

    
}

/**
 * One of EstateCell's 5 variants.
 */
class HauntedHouse extends Estate implements Habitable {
    private final String adjective;
    private final String habitat;

    /**
     * Constructs a HauntedHouse estate with the given enclosing EstateCell.
     * @param b   The board Haunted House belongs to
     */
    public HauntedHouse(Board b, int row) {
        super(row);
        this.adjective = "Haunted";
        this.habitat = "House";
        super.setLines(initializeLines());
    }

	/**
     * Retrieves the adjective describing the HauntedHouse estate.
     *
     * @return The adjective for the HauntedHouse estate.
     */
    public String getAdjective() {
        return adjective;
    }

    /**
     * Retrieves the visual appearance of the HauntedHouse estate as an array of lines.
     * The lines are generated using the attributes of the enclosing EstateCell.
     *
     * @return The array of strings representing the visual appearance of the HauntedHouse estate.
     */
    public String[] initializeLines() {
        String wallString = super.wallString;
        String right = super.getEntranceString(Direction.RIGHT);
        String down  = super.getEntranceString(Direction.DOWN);

        // Generate the lines using the attributes of the enclosing Estate
        String[] hauntedHouseLines = {
                wallString.repeat(5),
                wallString + String.format("%" + 12 + "s", adjective) + right,
                wallString + String.format("%" + 12 + "s", habitat) + wallString,
                wallString + super.getPlayerStrings() + wallString,
                wallString.repeat(3) + down + wallString
        };
        return hauntedHouseLines;
    }
    
    public String toString() {
    	return adjective + " " + habitat;
    }

    @Override
    public boolean Move(Player player, String direction) {
        if (direction.equals("d")) {
            player.setPlayerLocation(player.getGame().getBoard().getCellAt(6, 3));
            return true;
        } else if (direction.equals("s")) {
            player.setPlayerLocation(player.getGame().getBoard().getCellAt(6, 3));
            return true;
        } else {
            return false;
        }
    }
}

/**
 * An enumeration that specifies the direction (UP, DOWN, LEFT, RIGHT) used for entrances in the estate.
 */
enum Direction { UP, DOWN, LEFT, RIGHT }

/**
 * The Habitable interface represents an estate that can be inhabited by players.
 * Any estate that implements this interface must provide its visual appearance as an array of lines.
 */
interface Habitable {
    /**
     * Retrieves the visual appearance of the estate as an array of lines.
     *
     * @return The array of strings representing the estate's visual appearance.
     */
    String[] getLines();

    boolean Move(Player player, String w);
}