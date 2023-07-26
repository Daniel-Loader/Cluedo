package cluedo25_7;

public interface Cell {

	Cell copy();

	Boolean isPassable();
	void setPlayer(Player player);
	void removePlayer(Player player);
}

final class Path implements Cell {
	private Player player;

	@Override
	public Cell copy() {
		return new Path();
	}

	public void setPlayer(Player p){
		player = p;
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
	
	//public String toString(){return "__|";}
	public String toString(){
		if(player != null){
			return player + "|";
		}
		return "__|";
	}
}

final class Entrance implements Cell {
	private final Estate estate;
	private Player player;

	public Entrance(Estate estate) {
		this.estate = estate;
	}

	public Cell getEstate() {
		return (Cell)estate;
	}

	public Cell copy() {
		return new Entrance(estate);
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
	public Cell copy() { return this; }

	@Override
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