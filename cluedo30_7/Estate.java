package cluedo30_7;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import static cluedo30_7.Board.Direction;

public interface Estate extends Cell {

    /**
     * Getter method for the estates starting row;
     * @return row - the uppermost row of the estate
     */
    int getRow();

    /**
     * Creates a deep copy of the Estate.
     *
     * @return A new EstateCell object that is a copy of this cell.
     */
    default Cell copy() { return this; }

    /**
     * Checks if a players can move onto this EstateCell.
     *
     * @return true, indicating that the Estate can always be entered.
     */
    @Override
    default Boolean isPassable() { return true; }

    /**
     * Adds a player to the list of occupants in the estate.
     *
     * @param p The player to be added to the estate.
     */
    @Override
    default void setPlayer(Player p) {
        addOccupant(p);
    }

    default Boolean contains(Player p) {
        return getOccupants().contains(p);
    }

    /**
     * Removes the player from the estate.
     *
     * @param p The player to be removed from the estate.
     * @throws IllegalArgumentException If the player is not present in the estate.
     */
    @Override
    default void removePlayer(Player p) {removeOccupant(p);}

    /**
     * Replaces the contents of the estate with the given collection of players
     *
     * @param ps the collection of players
     * @param <T> the subclass of player (or the default one) used by the collection.
     */
    default <T extends Player> void setOccupants(Collection<T> ps) {
        getOccupants().clear();
        getOccupants().addAll(ps);
    }


    /**
     * Adds a player to the list of occupants in the estate.
     *
     * @param p The player to be added to the estate.
     */
    default void addOccupant(Player p) {
        getOccupants().add(p);
    }

    /**
     * Removes a player from the list of occupants in the estate.
     *
     * @param p The player to be removed from the estate.
     * @return The removed player if found.
     * @throws IllegalArgumentException If the player is not present in the estate.
     */
    default Player removeOccupant(Player p) throws IllegalArgumentException {
        if (getOccupants().contains(p)) {
            int i = getOccupants().indexOf(p);
            return this.getOccupants().remove(i);
        } else {
            throw new IllegalArgumentException(p.getName() + " is not present");
        }
    }

    /**
     * Retrieves the list of players (occupants) currently present in the estate.
     *
     * @return The list of players occupying the estate.
     */
    ArrayList<Player> getOccupants();

    /**
     * Retrieves the player strings (abbreviated names) of the occupants, right-padded to 12 characters.
     *
     * @return The player strings representing the occupants of the estate.
     */
    default String getPlayerStrings() {
        String players = getOccupants().stream()
                .map(p -> p.getName().substring(0, 2))
                .collect(Collectors.joining(" "));
        return String.format("%1$-" + 9 + "s", players);
    }

    /**
     * Replaces the contents of the estate with the given collection of entrances
     *
     * @param es the collection of entrances
     * @param <T> the subclass of entrance (or the default one) used by the collection.
     */
    default <T extends Entrance> void setEntrances(Collection<T> es) {
        getEntrances().clear();
        getEntrances().addAll(es);

    }

    /**
     * Adds an Entrance to the list of Entrances in the estate.
     *
     * @param e The Entrance to be added to the estate.
     */
    default void addEntrance(Entrance e) {
        getEntrances().add(e);
    }

    /**
     * Getter method for entrances
     *
     * @return The list of players occupying the estate.
     */
    ArrayList<Entrance> getEntrances();

    boolean Move(Player p, String d);

    String getLine(int i);
}

/**
 * One of Estate's 5 variants.
 */
class HauntedHouse implements Estate {
    private final int row;
    private final int column;
    private final ArrayList<Player> occupants = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();
    public final String adjective = " Haunted ";
    public final String habitat = "  House  ";

    /**
     * Constructs a HauntedHouse estate.
     * @param row The row Haunted House is located on
     * @param col The column Haunted House is located on
     */
    public HauntedHouse(int row, int col) {
        this.row =row;
        this.column = col;
    }

    /**
     * Getter method for the estates starting row;
     *
     * @return row - the uppermost row of the estate
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * Getter method for column
     *
     * @return the left most column of the Estate
     */
    @Override
    public int getColumn() {
        return column;
    }

    /**
     * Getter method for the estate's occupants;
     *
     * @return occupants - the estate's List of players
     */
    @Override
    public ArrayList<Player> getOccupants() { return occupants; }

    /**
     * Getter method for the estate's entrances
     *
     * @return entrances - the estate's List of Entrances
     */
    @Override
    public ArrayList<Entrance> getEntrances() { return entrances; }

    /**
     * Retrieves a specific line of the room representation based on the provided row index.
     *
     * @param i The row index of the line to retrieve.
     * @return The line representation as a String.
     * @throws IndexOutOfBoundsException If the given row index is out of bounds.
     */
    public String getLine(int i) {
        String wallString = Board.WALLSTRING;
        String right = Board.ENTRANCESTRINGS.get(Direction.RIGHT);
        String down  = Board.ENTRANCESTRINGS.get(Direction.DOWN);

        // Calculate the difference between the provided row index (i) and the current row
        int rowIndexDifference = i - getRow();

        // Handle different row index cases using the advanced switch expression
        return switch (rowIndexDifference) {
            case 0 -> wallString.repeat(5);
            case 1 -> wallString + adjective + right;
            case 2 -> wallString + habitat + wallString;
            case 3 -> wallString + getPlayerStrings() + wallString;
            case 4 -> wallString.repeat(3) + down + wallString;
            default -> throw new IndexOutOfBoundsException("Invalid row index: " + i);
        };
    }

    @Override
    public boolean Move(Player player, String direction) {
        switch (direction) {
            case "d" -> {
                //player.setPlayerLocation(player.getGame().getBoard().getCellAt(3, 7));
                player.setPlayerLocation(this, player.getGame().getBoard().getCellAt(3, 7));
                player.setRow(3);
                player.setColumn(7);
                return true;
            }
            case "s" -> {
                player.setPlayerLocation(this, player.getGame().getBoard().getCellAt(7, 5));
                player.setRow(7);
                player.setColumn(5);
                return true;
            }
            default -> {
                return false;
            }
        }
    }
    public String toString() {
        return adjective + " " + habitat;
    }
}

/**
 * One of Estate's 5 variants.
 */
class ManicManor implements Estate {
    private final int row;
    private final int column;
    private final ArrayList<Player> occupants = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();
    private final String adjective = "  Manic  ";
    private final String habitat   = "  Manor  ";

    /**
     * Constructs a HauntedHouse estate with the given enclosing Estate.
     *
     * @param row The row Haunted House is located on
     * @param col The column Haunted House is located on
     */
    public ManicManor(int row, int col) {
       this.row = row;
       this.column = col;
    }

    /**
     * Getter method for the estates starting row;
     *
     * @return row - the uppermost row of the estate
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * Getter method for the estate's occupants;
     *
     * @return occupants - the estate's List of players
     */
    @Override
    public ArrayList<Player> getOccupants() { return occupants; }

    /**
     * Getter method for the estate's entrances
     *
     * @return entrances - the estate's List of Entrances
     */
    @Override
    public ArrayList<Entrance> getEntrances() { return entrances; }

    /**
     * Getter method for column
     *
     * @return the left most column of the Estate
     */
    @Override
    public int getColumn() {
        return column;
    }

    /**
     * Retrieves a specific line of the room representation based on the provided row index.
     *
     * @param i The row index of the line to retrieve.
     * @return The line representation as a String.
     * @throws IndexOutOfBoundsException If the given row index is out of bounds.
     */
    public String getLine(int i) {
        String wallString = Board.WALLSTRING;
        String left = Board.ENTRANCESTRINGS.get(Direction.LEFT);
        String down = Board.ENTRANCESTRINGS.get(Direction.DOWN);

        // Calculate the difference between the provided row index (i) and the current row
        int rowIndexDifference = i - getRow();

        // Handle different row index cases using the advanced switch expression
        return switch (rowIndexDifference) {
            case 0 -> wallString.repeat(5);
            case 1 -> wallString + adjective + wallString;
            case 2 -> wallString + habitat + wallString;
            case 3 -> left + getPlayerStrings() + wallString;
            case 4 -> wallString.repeat(3) + down + wallString;
            default -> throw new IndexOutOfBoundsException("Invalid row index: " + i);
        };
    }

    public String toString() {
        return adjective + " " + habitat;
    }

    @Override
    public boolean Move(Player player, String direction) {
        switch (direction) {
            case "a" -> {
                player.setPlayerLocation(this, player.getGame().getBoard().getCellAt(5, 16));
                player.setRow(5);
                player.setColumn(16);
                return true;
            }
            case "s" -> {
                player.setPlayerLocation(this, player.getGame().getBoard().getCellAt(7, 20));
                player.setRow(7);
                player.setColumn(20);
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}

/**
 * One of EstateCell's 5 variants.
 */
class CalamityCastle implements Estate {
    private final int row;
    private final int column;
    private final ArrayList<Player> occupants = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();
    private final String adjective = " Calamity";
    private final String habitat   = "  Castle ";

    /**
     * Constructs a CalamityCastle estate.
     * @param row The row Calamity Castle is located on
     * @param col The column Calamity Castle is located on
     */
    public CalamityCastle(int row, int col) {
        this.row = row;
        this.column = col;
    }

    /**
     * Getter method for the estates starting row;
     *
     * @return row - the uppermost row of the estate
     */
    @Override
    public int getRow() { return row; }

    /**
     * Getter method for column
     *
     * @return the left most column of the Estate
     */
    @Override
    public int getColumn() { return column; }

    /**
     * Getter method for the estate's occupants;
     *
     * @return occupants - the estate's List of players
     */
    @Override
    public ArrayList<Player> getOccupants() { return occupants; }

    /**
     * Getter method for the estate's entrances
     *
     * @return entrances - the estate's List of Entrances
     */
    @Override
    public ArrayList<Entrance> getEntrances() { return entrances; }

    /**
     * Retrieves the visual appearance of the HauntedHouse estate as an array of lines.
     * The lines are generated using the attributes of the enclosing EstateCell.
     *
     * @return The array of strings representing the visual appearance of the HauntedHouse estate.
     */
    public String getLine(int i) {
        String wallString = Board.WALLSTRING;
        String right = Board.ENTRANCESTRINGS.get(Direction.RIGHT);
        String up    = Board.ENTRANCESTRINGS.get(Direction.UP);

        // Calculate the difference between the provided row index (i) and the current row
        int rowIndexDifference = i - getRow();

        // Handle different row index cases using the advanced switch expression
        return switch (rowIndexDifference) {
            case 0 -> wallString + up + wallString.repeat(3);
            case 1 -> wallString + adjective + right;
            case 2 -> wallString + habitat + wallString;
            case 3 -> wallString + getPlayerStrings() + wallString;
            case 4 -> wallString.repeat(5);
            default -> throw new IndexOutOfBoundsException("Invalid row index: " + i);
        };
    }

    @Override
    public boolean Move(Player player, String direction) {
        switch (direction) {
            case "d" -> {
                player.setPlayerLocation(this, player.getGame().getBoard().getCellAt(18, 7));
                player.setRow(18);
                player.setColumn(7);
                return true;
            }
            case "w" -> {
                player.setPlayerLocation(this, player.getGame().getBoard().getCellAt(16, 3));
                player.setRow(16);
                player.setColumn(3);
                return true;
            }
            default -> {
                return false;
            }
        }
    }



    public String toString() {
        return adjective + " " + habitat;
    }
}

//--------------------------------------------------------------------------------------
//Unimplemented:
//--------------------------------------------------------------------------------------
/**
 * One of EstateCell's 5 variants.
 */
class PerilPalace implements Estate {
    private final int row;
    private final int column;
    private final ArrayList<Player> occupants = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();
    private final String adjective = "  Peril  ";
    private final String habitat   = "  Palace ";

    /**
     * Constructs a Peril Palace estate.
     * @param row The row Peril Palace is located on
     * @param col The column Peril Palace is located on
     */
    public PerilPalace(int row, int col) {
        this.row = row;
        this.column = col;
    }

    /**
     * Getter method for the estates starting row;
     *
     * @return row - the uppermost row of the estate
     */
    @Override
    public int getRow() { return row; }

    /**
     * Getter method for column
     *
     * @return the left most column of the Estate
     */
    @Override
    public int getColumn() { return column; }

    /**
     * Getter method for the estate's occupants;
     *
     * @return occupants - the estate's List of players
     */
    @Override
    public ArrayList<Player> getOccupants() { return occupants; }

    /**
     * Getter method for the estate's entrances
     *
     * @return entrances - the estate's List of Entrances
     */
    @Override
    public ArrayList<Entrance> getEntrances() { return entrances; }

    /**
     * Retrieves the visual appearance of the PerilPalace estate as an array of lines.
     * The lines are generated using the attributes of the enclosing EstateCell.
     *
     * @return The array of strings representing the visual appearance of the PerilPalace estate.
     */
    public String getLine(int i) {
        String wallString = Board.WALLSTRING;
        String up  = Board.ENTRANCESTRINGS.get(Direction.UP);
        String left = Board.ENTRANCESTRINGS.get(Direction.LEFT);

        // Calculate the difference between the provided row index (i) and the current row
        int rowIndexDifference = i - getRow();

        // Handle different row index cases using the advanced switch expression
        return switch (rowIndexDifference) {
            case 0 -> wallString + up + wallString.repeat(3);
            case 1 -> wallString + adjective + wallString;
            case 2 -> wallString + habitat + wallString;
            case 3 -> left + getPlayerStrings() + wallString;
            case 4 -> wallString.repeat(5);
            default -> throw new IndexOutOfBoundsException("Invalid row index: " + i);
        };
    }

    public String toString() {
        return adjective + " " + habitat;
    }

    @Override
    public boolean Move(Player player, String direction) {
        switch (direction) {
            case "w" -> {
                player.setPlayerLocation(this, player.getGame().getBoard().getCellAt(16, 18));
                player.setRow(16);
                player.setColumn(18); //Cell just above the entrance.
                return true;
            }
            case "a" -> {
                player.setPlayerLocation(this, player.getGame().getBoard().getCellAt(20, 16));
                player.setRow(20);
                player.setColumn(16); //Cell to the left of the entrance.
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}

/**
 * One of Estate's 5 variants.
 */
class VisitationVilla implements Estate {
    private final int row;
    private final int column;
    private final ArrayList<Player> occupants = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();
    private final String adjective = "Visit ";
    private final String habitat   = "Villa ";

    /**
     * Constructs a Peril Palace estate.
     * @param row The row Peril Palace is located on
     * @param col The column Peril Palace is located on
     */
    public VisitationVilla(int row, int col) {
        this.row = row;
        this.column = col;
    }

    /**
     * Getter method for the estates starting row;
     *
     * @return row - the uppermost row of the estate
     */
    @Override
    public int getRow() { return row; }

    /**
     * Getter method for column
     *
     * @return the left most column of the Estate
     */
    @Override
    public int getColumn() { return column; }

    /**
     * Getter method for the estate's occupants;
     *
     * @return occupants - the estate's List of players
     */
    @Override
    public ArrayList<Player> getOccupants() { return occupants; }

    /**
     * Getter method for the estate's entrances
     *
     * @return entrances - the estate's List of Entrances
     */
    @Override
    public ArrayList<Entrance> getEntrances() { return entrances; }

    /**
     * Retrieves the visual appearance of the VisitationVilla estate as an array of lines.
     * The lines are generated using the attributes of the enclosing EstateCell.
     *
     * @return The array of strings representing the visual appearance of the VisitationVilla estate.
     */
    public String getLine(int i) {
        String wallString = Board.WALLSTRING;
        String up     = Board.ENTRANCESTRINGS.get(Direction.UP);
        String down   = Board.ENTRANCESTRINGS.get(Direction.DOWN);
        String left   = Board.ENTRANCESTRINGS.get(Direction.LEFT);
        String right  = Board.ENTRANCESTRINGS.get(Direction.RIGHT);

        // Calculate the difference between the provided row index (i) and the current row
        int rowIndexDifference = i - getRow();

        // Handle different row index cases using the advanced switch expression
        return switch (rowIndexDifference) {
            case 0 -> wallString.repeat(2) + up    + wallString.repeat(3);
            case 1 -> wallString + adjective     +habitat+ right;
            case 2 -> left + getPlayerStrings()  + "   " + wallString;
            case 3 -> wallString.repeat(3) + down  + wallString.repeat(2);
            default -> throw new IndexOutOfBoundsException("Invalid row index: " + i);
        };
    }

    @Override
    public boolean Move(Player player, String direction) {
        switch (direction) {
            case "w" -> {
                player.setPlayerLocation(this, player.getGame().getBoard().getCellAt(9, 12));
                player.setRow(9);
                player.setColumn(12); //Cell just above the entrance.
                return true;
            }
            case "a" -> {
                player.setPlayerLocation(this, player.getGame().getBoard().getCellAt(12, 8));
                player.setRow(12);
                player.setColumn(8); //Cell to the left of the entrance.
                return true;
            }
            case "s" -> {
                player.setPlayerLocation(this, player.getGame().getBoard().getCellAt(14, 11));
                player.setRow(14);
                player.setColumn(11); //Cell just below the entrance.
                return true;
            }
            case "d" -> {
                player.setPlayerLocation(this, player.getGame().getBoard().getCellAt(11, 15));
                player.setRow(11);
                player.setColumn(15); //Cell to the right of the entrance.
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public String toString() {
        return adjective + " " + habitat;
    }
}