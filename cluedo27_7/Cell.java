package cluedo27_7;

public interface Cell {

	public Cell copy();

	public int getRow();
	
	public int getColumn();

	public Boolean contains(Player p);

	public Boolean isPassable();
	void setPlayer(Player player);
	void removePlayer(Player player);
}

final class Path implements Cell {
	private int row;
	private int column;
	private Player player;


	public Path(int row, int column) {
		this.row = row;
		this.column = column;
	}

	@Override
	public Cell copy() {
		return new Path(row, column);
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getColumn() {
		return column;
	}

	public void setPlayer(Player p){
		if (player == null) {
			player = p;
		} else {
			throw new IllegalStateException("Adding a player to a filled Cell");
		}
	}

	public void removePlayer(Player p){
		if (p == player) {
			player = null;
		} else {
			throw new IllegalArgumentException("that Player is not in this cell");
		}
	}

	public Boolean isPassable() {

		return player == null;
	}
	
	public Boolean contains(Player p){return player.equals(p);}
	
	//public String toString(){return "__|";}
	public String toString(){
		if(player != null){
			return player.toString() + "|";
		}
		return "__|";
	}
}

final class Entrance implements Cell {
	@SuppressWarnings("unused")
	private int row;
	private int column;
	private EstateCell estate;
	private Player player;

	public Entrance(int row, int column, EstateCell estate) {
		this.row    = row;
		this.column = column;
		this.estate = estate;
	}

	public Cell getEstate() {
		return (Cell)estate;
	}

	public Cell copy() {
		return new Entrance(row, column, estate);
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getColumn() {
		return column;
	}

	public Boolean contains(Player p) {
		return player.equals(p);
	}

	public Boolean isPassable() {
		return player == null;
	}

	@Override
	public void setPlayer(Player p) {
		if (this.player == null) {
			player = p;
		} else {
			throw new IllegalArgumentException("Overloading Entrance of" + estate.toString());
		}
	}

	@Override
	public void removePlayer(Player p) {
		if (p == player) {
			player = null;
		} else {
			throw new IllegalArgumentException(player.getName() + "is not in this cell");
		}
	}

	@Override
	public String toString() {
		return "   ";
	}
}

final class Wall implements Cell {

	@Override
	public Cell copy() {
		return this;
	}

	@Override
	public int getRow() {
		return 0;
	}

	@Override
	public int getColumn() {
		return 0;
	}

	public Boolean contains(Player p) {
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