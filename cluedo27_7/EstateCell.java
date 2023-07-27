package cluedo27_7;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The EstateCell class represents a generic estate for our Cluedo game.
 * Each EstateCell instance has an array of lines representing the visual appearance of the estate,
 * a list of occupants (players) currently present in the estate, and entrance strings for each direction.
 * The estate can be a HauntedHouse or any other custom estate that implements the Habitable interface.
 */
public class EstateCell implements Cell {
    public String getLine(int i) {
        return lines[i-row];
    }

    static enum Direction { UP, DOWN, LEFT, RIGHT }
	@SuppressWarnings("unused")
    private final Map<Direction, String> entranceStrings = Board.ENTRANCESTRINGS;
	public	final String wallString = Board.WALLSTRING;
    private final int row;
    private final int column;
    private List<Player> occupants = new ArrayList<Player>();
    private List<Entrance> entrances = new ArrayList<Entrance>();
    private String[] lines = new String[5];
    final String adjective;
    final String habitat;

    /**
     * Retrieves the adjective describing the HauntedHouse estate.
     *
     * @return The adjective for the HauntedHouse estate.
     */
    public String getAdjective() {
        return adjective;
    }

    /**
     * Retrieves the adjective describing the HauntedHouse estate.
     *
     * @return The adjective for the HauntedHouse estate.
     */
    public String getHabitat() {
        return habitat;
    }
    
    EstateCell(int row, int col, String adj, String habitat) {
    	this.row = row;
    	this.column = col;
        this.adjective = adj;
        this.habitat = habitat;
    }
    
    /**
     * Creates a deep copy of the EstateCell.
     *
     * @return A new EstateCell object that is a copy of this cell.
     */
    @Override
	public Cell copy() {
    	EstateCell copy = new EstateCell(row, column, adjective, habitat);
    	copy.setEntrances(entrances);
    	copy.setLines(lines);
    	return copy;
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getColumn() {
		return column;
	}
	
	@SuppressWarnings("unused")
	private void setOccupants(List<Player> ps) {
		this.occupants = ps;
	}
	
	/**
     * Retrieves the list of players (occupants) currently present in the estate.
    *
    * @return The list of players occupying the estate.
    */
   public List<Player> getOccupants() {
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
       return String.format("%1$-" + 9 + "s", players);
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

    /**
     * Checks if the given player is currently occupying this EstateCell.
     *
     * @param p The player to check for occupancy.
     * @return true if the player is occupying this EstateCell, false otherwise.
     */
    @Override
    public Boolean contains(Player p) {
        return occupants.contains(p);
    }
    
    private void setEntrances(List<Entrance> es) {
		this.entrances = es;
		
	}
	
	@SuppressWarnings("unused")
    public void addEntrance(Entrance e) {
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
class HauntedHouse extends EstateCell implements Habitable {

    /**
     * Constructs a HauntedHouse estate.
     * @param b   The board Haunted House belongs to
     * @param row The row Haunted House is located on
     * @param col The column Haunted House is located on
     */
    public HauntedHouse(Board b, int row, int col) {
        super(row, col, " Haunted ", "  House  ");
        super.setLines(initializeLines());
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
                wallString + adjective + right,
                wallString + habitat + wallString,
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
            player.setPlayerLocation(player.getGame().getBoard().getCellAt(3, 7));
            player.setRow(3); player.setColumn(7);
            return true;
        } else if (direction.equals("s")) {
            player.setPlayerLocation(player.getGame().getBoard().getCellAt(7, 5));
            return true;
        } else {
            return false;
        }
    }
}

/**
 * One of EstateCell's 5 variants.
 */
class ManicManor extends EstateCell implements Habitable {

    /**
     * Constructs a HauntedHouse estate with the given enclosing EstateCell.
     *
     * @param b   The board Haunted House belongs to
     * @param row The row Haunted House is located on
     * @param col The column Haunted House is located on
     */
    public ManicManor(Board b, int row, int col) {
        super(row, col, "  Manic  ","  Manor  ");
        super.setLines(initializeLines());
    }

    /**
     * Retrieves the visual appearance of the HauntedHouse estate as an array of lines.
     * The lines are generated using the attributes of the enclosing EstateCell.
     *
     * @return The array of strings representing the visual appearance of the HauntedHouse estate.
     */
    public String[] initializeLines() {
        String wallString = super.wallString;
        String left = super.getEntranceString(Direction.LEFT);
        String down = super.getEntranceString(Direction.DOWN);

        // Generate the lines using the attributes of the enclosing Estate
        String[] hauntedHouseLines = {
                wallString.repeat(5),
                wallString + adjective + wallString,
                wallString + habitat + wallString,
                left + super.getPlayerStrings() + wallString,
                wallString.repeat(3) + down + wallString
        };
        return hauntedHouseLines;
    }

    public String toString() {
        return adjective + " " + habitat;
    }

    @Override
    public boolean Move(Player player, String direction) {
        if (direction.equals("a")) {
            player.setPlayerLocation(player.getGame().getBoard().getCellAt(5,17));
            player.setRow(5);
            player.setColumn(17);
            return true;
        } else if (direction.equals("s")) {
            player.setPlayerLocation(player.getGame().getBoard().getCellAt(6,20));
            player.setRow(6);
            player.setColumn(20);
            return true;
        } else {
            return false;
        }
    }
}

/**
 * One of EstateCell's 5 variants.
 */
class CalamityCastle extends EstateCell implements Habitable {

    /**
     * Constructs a CalamityCastle estate.
     * @param b   The board Calamity Castle belongs to
     * @param row The row Calamity Castle is located on
     * @param col The column Calamity Castle is located on
     */
    public CalamityCastle(Board b, int row, int col) {
        super(row, col, " Calamity", "  Castle ");
        super.setLines(initializeLines());
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
        String up  = super.getEntranceString(Direction.DOWN);

        // Generate the lines using the attributes of the enclosing Estate
        String[] hauntedHouseLines = {
                wallString + up + wallString.repeat(3),
                wallString + adjective + right,
                wallString + habitat + wallString,
                wallString + super.getPlayerStrings() + wallString,
                wallString.repeat(5)
        };
        return hauntedHouseLines;
    }

    public String toString() {
        return adjective + " " + habitat;
    }

    @Override
    public boolean Move(Player player, String direction) {
        if (direction.equals("d")) {
            player.setPlayerLocation(player.getGame().getBoard().getCellAt(3, 7));
            player.setRow(3); player.setColumn(7);
            return true;
        } else if (direction.equals("s")) {
            player.setPlayerLocation(player.getGame().getBoard().getCellAt(7, 5));
            return true;
        } else {
            return false;
        }
    }
}