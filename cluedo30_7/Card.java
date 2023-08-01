package cluedo30_7;

/**
 * The Card class represents a generic card in Cluedo.
 * It contains a string name and can be of Card.Type Suspect, Weapon or Estate.
 */
public record Card(String name, Card.Type cardType) {

    public enum Type {SUSPECT, WEAPON, ESTATE}

    @Override
    public String toString() {
        return String.format("%s<%s>", name, cardType);
    }
}

/*
/**
 * The Card class represents a generic card in the game.
 * It contains information about the card's name, owner, viewers, and the estate it is located in.
 *
public class Card {
	private String name;
	private Player owner;
	private Set<Player> viewers;
	private Estate locatedIn;

	
	/**
     * Creates a new instance of the Card class with the given name and owner.
     *
     * @param name  The name of the card.
     * @param owner The player who owns the card.
     *
    public Card(String name, Player owner) {
    	this.name = name;
    	this.owner = owner;
    	this.viewers = new HashSet<Player>();
    	this.viewers.add(owner);
    }

    /**
     * Gets the owner of the card.
     *
     * @return The player who owns the card.
     *
    public Player getOwner() {
        return owner;
    }

    /**
     * Checks if the given player is a viewer of the card.
     *
     * @param player The player to check.
     * @return true if the player is a viewer of the card, false otherwise.
     *
    public boolean containsViewer(Player player) {
        return this.viewers.contains(player);
    }

    /**
     * Adds a player to the viewers of the card.
     *
     * @param viewer The player to add to the viewers.
     *
    public void addToViewers(Player viewer) {
    	if (viewer != null) {this.viewers.add(viewer);}
    }

    /**
     * Gets the set of players who are viewers of the card.
     *
     * @return The set of viewers of the card.
     *
    public Set<Player> getViewers() {
        return viewers;
    }

    /**
     * Gets the estate where the card is located.
     *
     * @return The estate where the card is located.
     *
    public Estate getLocation() {
		return locatedIn;
	}

    /**
     * Sets the estate where the card is located.
     *
     * @param locatedIn The estate where the card is located.
     *
	public void setLocation(Estate locatedIn) {
		this.locatedIn = locatedIn;
	}

	@Override
    public String toString() {
        return name;
    }
}

/**
 * The Suspect class represents a suspect card in the game.
 * It is a specific type of card with a name and an owner.
 *
final class Suspect extends Card {
	
	/**
     * Creates a new instance of the Suspect class with the given name and owner.
     *
     * @param name  The name of the suspect card.
     * @param owner The player who owns the suspect card.
     *
    public Suspect(String name, Player owner) {
        super(name, owner);
    }
}

/**
 * The Weapon class represents a weapon card in the game.
 * It is a specific type of card with a name and an owner.
 *
class Weapon extends Card {
	
	/**
     * Creates a new instance of the Weapon class with the given name and owner.
     *
     * @param name  The name of the weapon card.
     * @param owner The player who owns the weapon card.
     *
	public Weapon(String name, Player owner) {
		super(name, owner);
	}}

/**
 * The Estate class represents an estate card in the game.
 * It is a specific type of card with a name and an owner.
 *
class Estate extends Card {

	/**
     * Creates a new instance of the Estate class with the given name and owner.
     *
     * @param name  The name of the estate card.
     * @param owner The player who owns the estate card.
     *
	public Estate(String name, Player owner) {
		super(name, owner);
	}
}
*/