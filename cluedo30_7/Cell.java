package cluedo30_7;

/**
 * The cell interface lists the common methods of all cell implementations.
 */
public interface Cell {
	/**
	 * Creates a copy of the Cell.
	 *
	 * @return A new instance of the Cell with the same properties as the original.
	 */
	Cell copy();

	/**
	 * Gets the row index of the Cell on the game board.
	 *
	 * @return The row index of the Cell.
	 */
	int row();

	/**
	 * Gets the column index of the Cell on the game board.
	 *
	 * @return The column index of the Cell.
	 */
	int column();

	/**
	 * Checks if the Cell is passable, i.e., if a player can move to this Cell.
	 *
	 * @return true if the Cell is passable (empty), false otherwise.
	 */
	Boolean isPassable();

	/**
	 * Checks if a player is in the Cell.
	 *
	 * @param player The Player to check for.
	 * @return has   whether the player is present.
	 */
	boolean hasPlayer(Player player);

	/**
	 * Sets a player in the Cell.
	 *
	 * @param player The Player to set in the Cell.
	 */
	void setPlayer(Player player);

	/**
	 * Removes a player from the Path cell.
	 *
	 * @param p the player to remove.
	 * @throws IllegalArgumentException if the specified Player is not in this cell.
	 */
	void removePlayer(Player p);
}

/**
 * The Path class represents a passable cell on the game board big enough for a single player.
 */
final class Path implements Cell {
	private final int row;
	private final int column;
	private Player player;

	/**
	 * Creates a new Path cell with the specified row and column indices.
	 *
	 * @param row    The row index of the Path cell.
	 * @param column The column index of the Path cell.
	 */
	public Path(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * Returns a deep copy of the Cell.
	 *
	 * @return copy - An identical (empty) path.
	 */
	@Override
	public Cell copy() {
		return new Path(row, column);
	}

	/**
	 * Getter method for Row
	 *
	 * @return the Cell's Y coordinate.
	 */
	@Override
	public int row() {
		return row;
	}

	/**
	 * Getter method for Column
	 *
	 * @return the Cell's X coordinate.
	 */
	@Override
	public int column() {return column;}

	/**
	 * Returns if the player can step into the cell.
	 *
	 * @return if there is room for a player
	 */
	public Boolean isPassable() {

		return player == null;
	}

	/**
	 * Returns if the specified player is in the Cell
	 *
	 * @param p The Player to check for presence in the Cell.
	 * @return true if the player is present, false otherwise
	 */
	public boolean hasPlayer(Player p){return player.equals(p);}

	/**
	 * Adds a player if there is room
	 *
	 * @param p The Player to set in the Cell.
	 * @throws IllegalStateException if the Path cell is already occupied by another player.
	 */
	public void setPlayer(Player p){
		if (player == null) {
			player = p;
		} else {
			throw new IllegalStateException("Adding a player to a filled Cell");
		}
	}

	/**
	 * Removes a player from the Path cell.
	 *
	 * @param p the player to remove.
	 * @throws IllegalArgumentException if the specified Player is not in this cell.
	 */
	public void removePlayer(Player p){
		if (player != p) {
			throw new IllegalArgumentException(p.name() + " is not in this cell");
		} else {
			player = null;
		}
	}


	/**
	 * String representation of a path
	 *
	 * @return The players name if one is present __| if empty.
	 */
	public String toString(){
		if(player != null){
			return player + "|";
		}
		return "__|";
	}
}

/**
 * The Entrance class allows access to an estate.
 */
final class Entrance implements Cell {
	private final int row;
	private final int column;
	private final Estate estate;
	private Player player;

	public Entrance(int row, int column, Estate estate) {
		this.row    = row;
		this.column = column;
		this.estate = estate;
	}

	/**
	 * Getter method for Row
	 *
	 * @return the Cell's Y coordinate.
	 */
	@Override
	public int row() { return row; }

	/**
	 * Getter method for Column
	 *
	 * @return the Cell's X coordinate.
	 */
	@Override
	public int column() { return column; }


	/**
	 * Getter Method for estate
	 *
	 * @return the estate this entrance belongs to
	 */
	public Estate estate() { return estate; }

	/**
	 * Returns a deep copy of the Cell.
	 *
	 * @return copy - An identical entrance to the Cells estate.
	 */
	@Override
	public Cell copy() {
		return new Entrance(row, column, estate);
	}

	/**
	 * Returns if the player can step into the Cell.
	 *
	 * @return if there is room for a player
	 */
	public Boolean isPassable() {return player == null; }

	/**
	 * Returns if the specified player is in the Cell
	 *
	 * @param p The Player to check for presence in the Cell.
	 * @return true if the player is present, false otherwise
	 */
	public boolean hasPlayer(Player p){return player.equals(p);}

	/**
	 * Method to add player to the entrance's estate.
	 *
	 * @param p The Player to set in the Estate.
	 * @throws IllegalArgumentException if the user tries to use a blocked entrance.
	 */
	@Override
	public void setPlayer(Player p) {
		if (this.player == null) {
			estate.setPlayer(p);
		} else {
			throw new IllegalArgumentException("Overloading Entrance of" + estate.toString());
		}
	}

	/**
	 * Removes the given player if present.
	 *
	 * @param p The Player to remove from the Entrance or Estate.
	 * @throws IllegalArgumentException if the player is not located in the entrance or estate.
	 */
	@Override
	public void removePlayer(Player p) {
		if (p == player) {
			player = null;
		} else if (estate.hasPlayer(p)) {
			estate.removePlayer(p);
		} else {
			throw new IllegalArgumentException(player.name() + "is not in this estate");
		}
	}

	/**
	 * String representation of an entrance.
	 * @return 3 spaces if not overridden by estate's line() method.
	 */
	@Override
	public String toString() {
		return "   ";
	}
}

/**
 * Walls represent impassible cells on the board.
 */
final class Wall implements Cell {
	/**
	 * Since none of walls values change, all walls can be pointers to the same instance.
	 * @return pointer to this wall instance.
	 */
	@Override
	public Cell copy() {
		return this;
	}

	/**
	 * Autogenerated method to satisfy Cell's requirements
	 * @throws IllegalArgumentException if called.
	 */
	@Override
	public int row() {
		throw new IllegalArgumentException("Shouldn't be checking a Wall's location");
	}

	/**
	 * Autogenerated method to satisfy Cell's requirements
	 * @throws IllegalArgumentException if called.
	 */
	@Override
	public int column() {
		throw new IllegalArgumentException("Shouldn't be checking a Wall's location");
	}

	/**
	 * Checks if there is room for a player.
	 *
	 * @param p The Player to check for presence in the Cell.
	 * @return false - Walls can't contain players.
	 */

	public boolean hasPlayer(Player p) {
		return false;
	}

	public Boolean isPassable() {
		return false;
	}

	@Override
	public void setPlayer(Player player) {
		throw new IllegalCallerException("Walls can't store players");
	}

	@Override
	public void removePlayer(Player player) {
		throw new IllegalCallerException("Walls can't store players");
	}

	public String toString(){return "###";}
}