package cluedo25_7;

/**
 * The Card class represents a generic card in Cluedo.
 * It contains a string name and can be of Card.Type Suspect, Weapon or Estate.
 */
public record Card(String name, Type cardType) {

    public enum Type {SUSPECT, WEAPON, ESTATE}

    /**
     * Checks if the given player is a viewer of the card.
     *
     * @param player The player to check.
     * @return true if the player is a viewer of the card, false otherwise.
    public boolean containsViewer(Player player) {
        return this.viewers.contains(player);
    }
     */

    /**
     * Adds a player to the viewers of the card.
     *
     * @param viewer The player to add to the viewers.
    public void addToViewers(Player viewer) {
    	if (viewer != null) {this.viewers.add(viewer);}
    }
     */

    /**
     * Gets the set of players who are viewers of the card.
     *
     * @return The set of viewers of the card.
    public Set<Player> getViewers() {
        return viewers;
    }
     */

    /**
     * Gets the estate where the card is located.
     *
     * @return The estate where the card is located.
    public Estate getLocation() {
		return locatedIn;
	}
     */

    /**
     * Sets the estate where the card is located.
     *
     * param locatedIn The estate where the card is located.
	public void setLocation(Estate locatedIn) {
		this.locatedIn = locatedIn;
	}
     */

	@Override
    public String toString() {
        return name;
    }
}