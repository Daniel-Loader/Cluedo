package cluedo27_7;

/**
 * The Habitable interface represents an estate that can be inhabited by players.
 * Any estate that implements this interface must provide its visual appearance as an array of lines.
 */
public interface Habitable {
    /**
     * Retrieves the visual appearance of the estate as an array of lines.
     *
     * @return The array of strings representing the estate's visual appearance.
     */
    String[] getLines();

    boolean Move(Player player, String w);
}
