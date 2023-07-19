import java.util.HashSet;
import java.util.Set;

public class Card {
	private String name;
	private Player owner;
	private Set<Player> viewers;
	private Estate locatedIn;

    public Card(String name, Player owner) {
    	this.name = name;
    	this.owner = owner;
    	this.viewers = new HashSet<Player>();
    	this.viewers.add(owner);
    }

    public Player getOwner() {
        return owner;
    }

    public boolean containsViewer(Player player) {
        return this.viewers.contains(player);
    }

    // Add a card to the player's hand
    public void addToViewers(Player viewer) {
    	if (viewer != null) {this.viewers.add(viewer);}
    }

    // Get the card's owner
    public Set<Player> getViewers() {
        return viewers;
    }

    public Estate getLocation() {
		return locatedIn;
	}

	public void setLocation(Estate locatedIn) {
		this.locatedIn = locatedIn;
	}

	@Override
    public String toString() {
        return name;
    }
}


final class Suspect extends Card {
    public Suspect(String name, Player owner) {
        super(name, owner);
    }
}

class Weapon extends Card {

	public Weapon(String name, Player owner) {
		super(name, owner);
		// TODO Auto-generated constructor stub
	}}

class Estate extends Card {

	public Estate(String name, Player owner) {
		super(name, owner);
		// TODO Auto-generated constructor stub
	}}
