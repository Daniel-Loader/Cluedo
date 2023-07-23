import java.util.Set;

public interface Cell {

	public Cell clone();

	//TODO finalize a list of generic cell methods;

	public Boolean contains(Player p);

	public Boolean isPassable();


}

final class Path implements Cell {
	private int row;
	private int column;
	//private Set<Player> players;
	private Player player;

	public void setPlayer(Player p){
		player = p;
	}

	public void removePlayer(Player p){
		player = null;
	}

	public Path(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public Cell clone() {
		return new Path(row, column);
	}

	//TODO finalize a list of generic cell methods;
	public Boolean contains(Player p){return player.equals(p);}
	//public Boolean contains(Player p) {return players.contains(p);}

	public Boolean isPassable() {
		return true;
	}
	//public String toString(){return "Path";} //4 characters
	public String toString(){return "' '";} //4 characters
}

final class Entrance implements Cell {
	@SuppressWarnings("unused")
	private int row;
	private int column;
	private Estate estate;
	private Set<Player> players;

	public Entrance(int row, int column, Estate estate) {
		this.row    = row;
		this.column = column;
		this.estate = estate;
	}

	public Cell clone() {
		return new Entrance(row, column, estate);
	}

	//TODO finalize a list of generic cell methods;

	public Boolean contains(Player p) {
		return players.contains(p);
	}

	public Boolean isPassable() {
		return true;
	}

	public String toString(){return "Etr";} //4 characters
}

final class Wall implements Cell {
	public Cell clone() {
		//TODO implement Clone
		return null;
	}

	//TODO finalize a list of generic cell methods;

	public Boolean contains(Player p) {
		return false;
	}

	public Boolean isPassable() {
		return false;
	}

	public String toString(){return "[ ]";} //4 characters
}

//Skeleton for each estate as seperate cell.

final class EstateCell implements Cell{

	private int row;
	private int column;

	public EstateCell(int row, int column){
		this.row = row;
		this.column = column;
	}

	@Override
	public Cell clone() {
		//TODO implement Clone
		return new EstateCell(row, column);
	}

	@Override
	public Boolean contains(Player p) {
		return null;
	}

	@Override
	public Boolean isPassable() {
		return false;
	}
}