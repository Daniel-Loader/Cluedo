package cluedo30_7;

import java.util.ArrayList;
import java.util.stream.Collectors;
import static cluedo30_7.Board.Direction;

/**
 * The Estate interface represents a generic estate in the Hobby Detectives game board.
 * It extends the Cell interface and provides methods to manage occupants (players) and entrances.
 */
public interface Estate extends Cell {

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
        occupants().add(p);
    }

    /**
     * Returns whether a player is located within the estate.
     *
     * @return boolean - whether a player is located within the estate
     */
    default boolean hasPlayer() {
        return occupants().size() > 0;
    }

    /**
     * Returns whether the given player is located within the estate.
     *
     * @return boolean - whether the given player is located within the estate
     */
    default boolean hasPlayer(Player p) {
        return occupants().contains(p);
    }

    /**
     * Removes the player from the estate.
     *
     * @throws IllegalArgumentException As this version shouldn't be called.
     */
    default Player player() {
        throw new IllegalArgumentException("Must Specify which player to remove from an estate (it may have several)");
    }

    /**
     * Removes the player from the estate.
     *
     * @param p The player to be removed from the estate.
     * @throws IllegalArgumentException If the player is not present in the estate.
     */
    default void removePlayer(Player p) throws IllegalArgumentException {
        if (occupants().contains(p)) {
            int i = occupants().indexOf(p);
            this.occupants().remove(i);
        } else {
            throw new IllegalArgumentException(p.name() + " is not present");
        }
    }

    /**
     * Retrieves the list of players (occupants) currently present in the estate.
     *
     * @return The list of players occupying the estate.
     */
    ArrayList<Player> occupants();

    /**
     * Retrieves the player strings (abbreviated names) of the occupants, right-padded to 12 characters.
     *
     * @return The player strings representing the occupants of the estate.
     */
    default String playerStrings() {
        String players = occupants().stream()
                .map(p -> p.name().substring(0, 2))
                .collect(Collectors.joining(" "));
        return String.format("%1$-" + 9 + "s", players);
    }

    /**
     * Adds an Entrance to the list of Entrances in the estate.
     *
     * @param e The Entrance to be added to the estate.
     */
    default void addEntrance(Entrance e) {
        entrances().add(e);
    }

    /**
     * Getter method for entrances
     *
     * @return The list of players occupying the estate.
     */
    ArrayList<Entrance> entrances();

    boolean move(Player p, String d);

    String line(int i);

    String name();
}

/**
 * One of Estate's 5 variants.
 */
class HauntedHouse implements Estate {
    private final int row;
    private final int column;
    private final String name = "Haunted_House";
    private final ArrayList<Player> occupants = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();

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
    public int row() {
        return row;
    }

    /**
     * Getter method for column
     *
     * @return the left most column of the Estate
     */
    @Override
    public int column() {
        return column;
    }

    /**
     * Getter Method for name
     * @return the estates name.
     */
    public String name() { return name; }

    /**
     * Getter method for the estate's occupants;
     *
     * @return occupants - the estate's List of players
     */
    @Override
    public ArrayList<Player> occupants() { return occupants; }

    /**
     * Getter method for the estate's entrances
     *
     * @return entrances - the estate's List of Entrances
     */
    @Override
    public ArrayList<Entrance> entrances() { return entrances; }

    /**
     * Retrieves a specific line of the room representation based on the provided row index.
     *
     * @param i The row index of the line to retrieve.
     * @return The line representation as a String.
     * @throws IndexOutOfBoundsException If the given row index is out of bounds.
     */
    public String line(int i) {
        String wallString = Board.WALLSTRING;
        String right = Board.ENTRANCESTRINGS.get(Direction.RIGHT);
        String down  = Board.ENTRANCESTRINGS.get(Direction.DOWN);

        // Calculate the difference between the provided row index (i) and the current row
        int rowIndexDifference = i - row();

        // Handle different row index cases using the advanced switch expression
        return switch (rowIndexDifference) {
            case 0 -> wallString.repeat(5);
            case 1 -> wallString + " Haunted " + right;
            case 2 -> wallString + "  House  " + wallString;
            case 3 -> wallString + playerStrings() + wallString;
            case 4 -> wallString.repeat(3) + down + wallString;
            default -> throw new IndexOutOfBoundsException("Invalid row index: " + i);
        };
    }


    /**
     * Moves the player in the given direction
     *
     * @param player    The player to try and move
     * @param direction Which way the player should try to move in
     * @return boolean  If the player's chosen direction was valid.
     */
    @Override
    public boolean move(Player player, String direction) {
        switch (direction) {
            case "d" -> {
                if (!player.game().board().cellAt(3, 7).isPassable()) {
                    return false;
                }
                player.setPlayerLocation(this, player.game().board().cellAt(3, 7));
                player.setRow(3);
                player.setColumn(7);
                return true;
            }
            case "s" -> {
                if (!player.game().board().cellAt(5, 5).isPassable()) {
                    return false;
                }
                player.setPlayerLocation(this, player.game().board().cellAt(5, 5));
                player.setRow(5);
                player.setColumn(5);
                return true;
            }
            default -> {
                return false;
            }
        }
    }
    public String toString() {
        return name;
    }
}

/**
 * One of Estate's 5 variants.
 */
class ManicManor implements Estate {
    private final int row;
    private final int column;
    private final String name = "Manic_Manor";
    private final ArrayList<Player> occupants = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();

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
    public int row() {
        return row;
    }

    /**
     * Getter Method for name
     * @return the estates name.
     */
    public String name() { return name; }

    /**
     * Getter method for the estate's occupants;
     *
     * @return occupants - the estate's List of players
     */
    @Override
    public ArrayList<Player> occupants() { return occupants; }

    /**
     * Getter method for the estate's entrances
     *
     * @return entrances - the estate's List of Entrances
     */
    @Override
    public ArrayList<Entrance> entrances() { return entrances; }

    /**
     * Getter method for column
     *
     * @return the left most column of the Estate
     */
    @Override
    public int column() {
        return column;
    }

    /**
     * Retrieves a specific line of the room representation based on the provided row index.
     *
     * @param i The row index of the line to retrieve.
     * @return The line representation as a String.
     * @throws IndexOutOfBoundsException If the given row index is out of bounds.
     */
    public String line(int i) {
        String wallString = Board.WALLSTRING;
        String left = Board.ENTRANCESTRINGS.get(Direction.LEFT);
        String down = Board.ENTRANCESTRINGS.get(Direction.DOWN);

        // Calculate the difference between the provided row index (i) and the current row
        int rowIndexDifference = i - row();

        // Handle different row index cases using the advanced switch expression
        return switch (rowIndexDifference) {
            case 0 -> wallString.repeat(5);
            case 1 -> wallString + "  Manic  " + wallString;
            case 2 -> wallString + "  Manor  " + wallString;
            case 3 -> left + playerStrings() + wallString;
            case 4 -> wallString.repeat(3) + down + wallString;
            default -> throw new IndexOutOfBoundsException("Invalid row index: " + i);
        };
    }

    public String toString() {
        return name;
    }

    /**
     * Moves the player in the given direction
     *
     * @param player    The player to try and move
     * @param direction Which way the player should try to move in
     * @return boolean  If the player's chosen direction was valid.
     */
    @Override
    public boolean move(Player player, String direction) {
        switch (direction) {
            case "a" -> {
                player.setPlayerLocation(this, player.game().board().cellAt(5, 16));
                player.setRow(5);
                player.setColumn(16);
                return true;
            }
            case "s" -> {
                player.setPlayerLocation(this, player.game().board().cellAt(7, 20));
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
    private final String name = "Calamity_Castle";
    private final ArrayList<Player> occupants = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();

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
    public int row() { return row; }

    /**
     * Getter method for column
     *
     * @return the left most column of the Estate
     */
    @Override
    public int column() { return column; }

    /**
     * Getter Method for name
     * @return the estates name.
     */
    public String name() { return name; }

    /**
     * Getter method for the estate's occupants;
     *
     * @return occupants - the estate's List of players
     */
    @Override
    public ArrayList<Player> occupants() { return occupants; }

    /**
     * Getter method for the estate's entrances
     *
     * @return entrances - the estate's List of Entrances
     */
    @Override
    public ArrayList<Entrance> entrances() { return entrances; }

    /**
     * Retrieves the visual appearance of the HauntedHouse estate as an array of lines.
     * The lines are generated using the attributes of the enclosing EstateCell.
     *
     * @return The array of strings representing the visual appearance of the HauntedHouse estate.
     */
    public String line(int i) {
        String wallString = Board.WALLSTRING;
        String right = Board.ENTRANCESTRINGS.get(Direction.RIGHT);
        String up    = Board.ENTRANCESTRINGS.get(Direction.UP);

        // Calculate the difference between the provided row index (i) and the current row
        int rowIndexDifference = i - row();

        // Handle different row index cases using the advanced switch expression
        return switch (rowIndexDifference) {
            case 0 -> wallString + up + wallString.repeat(3);
            case 1 -> wallString + " Calamity" + right;
            case 2 -> wallString + "  Castle " + wallString;
            case 3 -> wallString + playerStrings() + wallString;
            case 4 -> wallString.repeat(5);
            default -> throw new IndexOutOfBoundsException("Invalid row index: " + i);
        };
    }

    /**
     * Moves the player in the given direction
     *
     * @param player    The player to try and move
     * @param direction Which way the player should try to move in
     * @return boolean  If the player's chosen direction was valid.
     */
    @Override
    public boolean move(Player player, String direction) {
        switch (direction) {
            case "d" -> {
                player.setPlayerLocation(this, player.game().board().cellAt(18, 7));
                player.setRow(18);
                player.setColumn(7);
                return true;
            }
            case "w" -> {
                player.setPlayerLocation(this, player.game().board().cellAt(16, 3));
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
        return name;
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
    private final String name = "Peril_Palace";
    private final ArrayList<Player> occupants = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();

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
    public int row() { return row; }

    /**
     * Getter method for column
     *
     * @return the left most column of the Estate
     */
    @Override
    public int column() { return column; }

    /**
     * Getter Method for name
     * @return the estates name.
     */
    public String name() { return name; }

    /**
     * Getter method for the estate's occupants;
     *
     * @return occupants - the estate's List of players
     */
    @Override
    public ArrayList<Player> occupants() { return occupants; }

    /**
     * Getter method for the estate's entrances
     *
     * @return entrances - the estate's List of Entrances
     */
    @Override
    public ArrayList<Entrance> entrances() { return entrances; }

    /**
     * Retrieves the visual appearance of the PerilPalace estate as an array of lines.
     * The lines are generated using the attributes of the enclosing EstateCell.
     *
     * @return The array of strings representing the visual appearance of the PerilPalace estate.
     */
    public String line(int i) {
        String wallString = Board.WALLSTRING;
        String up  = Board.ENTRANCESTRINGS.get(Direction.UP);
        String left = Board.ENTRANCESTRINGS.get(Direction.LEFT);

        // Calculate the difference between the provided row index (i) and the current row
        int rowIndexDifference = i - row();

        // Handle different row index cases using the advanced switch expression
        return switch (rowIndexDifference) {
            case 0 -> wallString + up + wallString.repeat(3);
            case 1 -> wallString + "  Peril  " + wallString;
            case 2 -> wallString + "  Palace " + wallString;
            case 3 -> left + playerStrings() + wallString;
            case 4 -> wallString.repeat(5);
            default -> throw new IndexOutOfBoundsException("Invalid row index: " + i);
        };
    }

    public String toString() {
        return name;
    }

    /**
     * Moves the player in the given direction
     *
     * @param player    The player to try and move
     * @param direction Which way the player should try to move in
     * @return boolean  If the player's chosen direction was valid.
     */
    @Override
    public boolean move(Player player, String direction) {
        switch (direction) {
            case "w" -> {
                player.setPlayerLocation(this, player.game().board().cellAt(16, 18));
                player.setRow(16);
                player.setColumn(18); //Cell just above the entrance.
                return true;
            }
            case "a" -> {
                player.setPlayerLocation(this, player.game().board().cellAt(20, 16));
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
    private final String name = "Visitation_Villa";
    private final ArrayList<Player> occupants = new ArrayList<>();
    private final ArrayList<Entrance> entrances = new ArrayList<>();

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
    public int row() { return row; }

    /**
     * Getter method for column
     *
     * @return the left most column of the Estate
     */
    @Override
    public int column() { return column; }

    /**
     * Getter Method for name
     * @return the estates name.
     */
    public String name() { return name; }

    /**
     * Getter method for the estate's occupants;
     *
     * @return occupants - the estate's List of players
     */
    @Override
    public ArrayList<Player> occupants() { return occupants; }

    /**
     * Getter method for the estate's entrances
     *
     * @return entrances - the estate's List of Entrances
     */
    @Override
    public ArrayList<Entrance> entrances() { return entrances; }

    /**
     * Retrieves the visual appearance of the VisitationVilla estate as an array of lines.
     * The lines are generated using the attributes of the enclosing EstateCell.
     *
     * @return The array of strings representing the visual appearance of the VisitationVilla estate.
     */
    public String line(int i) {
        String wallString = Board.WALLSTRING;
        String up     = Board.ENTRANCESTRINGS.get(Direction.UP);
        String down   = Board.ENTRANCESTRINGS.get(Direction.DOWN);
        String left   = Board.ENTRANCESTRINGS.get(Direction.LEFT);
        String right  = Board.ENTRANCESTRINGS.get(Direction.RIGHT);

        // Calculate the difference between the provided row index (i) and the current row
        int rowIndexDifference = i - row();

        // Handle different row index cases using the advanced switch expression
        return switch (rowIndexDifference) {
            case 0 -> wallString.repeat(2) + up    + wallString.repeat(3);
            case 1 -> wallString + " Visit Villa"+ right;
            case 2 -> left + playerStrings()  + "   " + wallString;
            case 3 -> wallString.repeat(3) + down  + wallString.repeat(2);
            default -> throw new IndexOutOfBoundsException("Invalid row index: " + i);
        };
    }

    /**
     * Moves the player in the given direction
     *
     * @param player    The player to try and move
     * @param direction Which way the player should try to move in
     * @return boolean  If the player's chosen direction was valid.
     */
    @Override
    public boolean move(Player player, String direction) {
        switch (direction) {
            case "w" -> {
                player.setPlayerLocation(this, player.game().board().cellAt(row(), column() - 2));
                player.setColumn(column() - 2);
                return true;
            }
            case "a" -> {
                player.setPlayerLocation(this, player.game().board().cellAt(row(), column() - 1));
                player.setColumn(column() - 1);
                return true;
            }
            case "s" -> {
                player.setPlayerLocation(this, player.game().board().cellAt(row(), column() + 1));
                player.setColumn(column() + 1);
                return true;
            }
            case "d" -> {
                player.setPlayerLocation(this, player.game().board().cellAt(row(), column() + 2));
                player.setColumn(column() + 2);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }
}