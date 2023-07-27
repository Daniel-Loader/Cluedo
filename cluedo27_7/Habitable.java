package cluedo27_7;

/**
 * The Habitable interface represents an estate that can be inhabited by players.
 * Any estate that implements this interface must provide its visual appearance as an array of lines.
 */
public interface Habitable {
    /**
     * Retrieves the string representation of a row from the estate.
     * @param i the row on the board to return
     * @return The String representation of the estate on the given row.
     */
    String getLine(int i);

    boolean Move(Player player, String w);
}