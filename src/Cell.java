import java.util.Set;

public interface Cell {

	public Cell clone();

	//TODO finalize a list of generic cell methods;

	public Boolean contains(Player p);

	public Boolean isPassable();
}

final class Entrance implements Cell {
	@SuppressWarnings("unused")
	private Estate estate;
	private Set<Player> players;

	public Cell clone() {
		//TODO implement Clone
		return null;
	}

	//TODO finalize a list of generic cell methods;

	public Boolean contains(Player p) {
		return players.contains(p);
	}

	public Boolean isPassable() {
		return true;
	}
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
}