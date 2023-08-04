package cluedo30_7;

import java.util.Map;

/**
 * The Board class handles initialising and printing the Hobby Detectives board.
 */
public class Board {
    private final int numRows;
    private final int numCols;
    private final Cell[][] cells;

    /**
     * 0 = Haunted House
     * 1 = Manic Manor
     * 2 = Calamity Castle
     * 3 = Peril Palace
     * 4 = Visitation Villa
     */
    private final Estate[] estates = new Estate[5];
    public Estate[] estates(){return estates;}
    public static final String WALLSTRING = "##|";
    public enum Direction { UP, DOWN, LEFT, RIGHT }

    /**
     * A map of directions to the string representation of an entrance on that side of an estate
     * (Name Inverse to direction of arrows unfortunately).
     */
    public static final Map<Direction, String> ENTRANCESTRINGS = Map.of(
            Direction.UP, "vv|", Direction.DOWN, "^^|",
            Direction.LEFT, ">>>", Direction.RIGHT, "<<<");

    public Board(int rows, int cols) {
        this.numRows = rows;
        this.numCols = cols;
        this.cells = new Cell[rows][cols];
        this.initializeCells();
    }

    /**
     * Getter method for Cells
     *
     * @param row   the Y coordinate of the desired cell
     * @param col   the X coordinate of the desired cell
     * @return Cell the desired Cell
     */
    public Cell cellAt(int row, int col) {
        return cells[row][col];
    }

    /**
     * Fills a rectangular area with the specified cell type within the given range of rows and columns.
     *
     * @param startRow The starting row index (inclusive) of the area to fill.
     * @param startCol The starting column index (inclusive) of the area to fill.
     * @param endRow The ending row index (exclusive) of the area to fill.
     * @param endCol The ending column index (exclusive) of the area to fill.
     * @param cell The type of cell to fill the area with.
     * @throws IllegalArgumentException if the provided range is invalid (out of bounds).
     */
    private void fill(int startRow, int startCol, int endRow, int endCol, Cell cell) {
        if (startRow > endRow || endRow > numRows || startCol > endCol || endCol > numCols) {
            throw new IllegalArgumentException();
        }
        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                cells[row][col] = cell.copy();
            }
        }
    }

    /**
     * Initializes the cells of the board and sets up the estates and entrances.
     */
    private void initializeCells() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                cells[row][col] = new Path(row, col);
            }
        }
        // Initialize the other cells:
        // ******************************************************
        // Haunted House:
        // ******************************************************
        estates[0] = new HauntedHouse(2, 2);
        fill(2,2,7,7, new Wall());
        fill(3,3,6,6, estates[0]);
        cells[3][6] = new Entrance(3,6, estates[0]);
        estates[0].addEntrance((Entrance) cells[3][6]);
        cells[6][5] = new Entrance(6,5, estates[0]);
        estates[0].addEntrance((Entrance) cells[6][5]);
        // ******************************************************
        // Manic Manor:
        // ******************************************************
        estates[1] = new ManicManor(2,17);
        fill(2,17, 7,22, new Wall());
        fill(3, 18, 6, 21, estates[1]);
        // Entrances:
        // column 17 (x), row 5 (y) [row][column] [y][x] Flip the order.
        cells[5][17] = new Entrance(5, 17, estates[1]);
        estates[1].addEntrance((Entrance) cells[5][17]);
        cells[6][20] = new Entrance(6, 20,estates[1]);
        estates[1].addEntrance((Entrance) cells[6][20]);
        // ******************************************************
        // Calamity Castle:
        // ******************************************************
        estates[2] = new CalamityCastle(17, 2);
        fill(17,2,22,7, new Wall());
        fill(18, 3, 21, 6,  estates[2]);
        // Entrances:
        // column 3 (x), row 17 (y) [row][column] [y][x] Flip the order.
        cells[17][3] = new Entrance(17,13, estates[2]);
        estates[2].addEntrance((Entrance) cells[17][3]);
        // column 6 (x), row 18 (y) [row][column] [y][x] Flip the order.
        cells[18][6] = new Entrance(18, 6, estates[2]);
        estates[2].addEntrance((Entrance) cells[18][6]);

        // ******************************************************
        // Peril Palace:
        // ******************************************************
        estates[3] = new PerilPalace( 17,17);
        fill(17,17,22,22, new Wall());
        fill(18, 18, 21,21, estates[3]);
        cells[17][18] = new Entrance(17,18, estates[3]);
        estates[3].addEntrance((Entrance) cells[17][18]);
        // column 17 (x), row 20 (y) [row][column] [y][x] Flip the order.
        cells[20][17] = new Entrance(20,17, estates[3]);
        estates[3].addEntrance((Entrance) cells[20][17]);

        // ******************************************************
        // Visitation Villa:
        // ******************************************************
        estates[4] = new VisitationVilla( 10,9);
        fill(10,9,14,15, new Wall());
        fill(11, 10, 13, 14, estates[4]);
        // column 12 (x), row 10 (y) [row][column] [y][x] Flip the order.
        cells[10][12] = new Entrance(10,12, estates[4]);
        // column 14 (x), row 11 (y) [row][column] [y][x] Flip the order.
        cells[11][14] = new Entrance(10,12,  estates[4]);
        // column 9 (x), row 12 (Y) [row][column] [y][x] Flip the order.
        cells[12][9] = new Entrance(10,12, estates[4]);
        // column 11 (x), row 13 (y) [row][column] [y][x] Flip the order.
        cells[13][11] = new Entrance(10,12, estates[4]);

        // ******************************************************
        // Top Square:
        // ******************************************************
        for (int row = 5; row <= 6; row++) {
            for (int column = 11; column <= 12; column++) {
                cells[row][column] = new Wall();
            }
        }
        // ******************************************************
        // Right Square:
        // ******************************************************
        for (int row = 11; row <= 12; row++) {
            for (int column = 17; column <= 18; column++) {
                cells[row][column] = new Wall();
            }
        }
        // ******************************************************
        // Bottom Square:
        // ******************************************************
        for (int row = 17; row <= 18; row++) {
            for (int column = 11; column <= 12; column++) {
                cells[row][column] = new Wall();
            }
        }
        // ******************************************************
        // Left Square:
        // ******************************************************
        for (int row = 11; row <= 12; row++) {
            for (int column = 5; column <= 6; column++) {
                cells[row][column] = new Wall();
            }
        }
    }

    /**
     * Prints a string representation of the game board to the console.
     * The representation includes the type of each cell and additional information for specific cells.
     */
    public void print() {
        System.out.println("Printing Board...");
        StringBuilder boardPrint = new StringBuilder();
        for (int row = 0; row < 24; row++) {

            for (int col = 0; col < 24; col++) {
                if (cells[row][col] == null) {
                    //boardPrint.append("   "); // 3 characters.
                    boardPrint.append("nul"); // 3 characters.
                    continue;
                }
                // Haunted House:
                else if (row >= 2 && row < 7 && col == 2) {
                    // 15 total - 7 word = 8 remaining. So 4 left and 4 right
                    boardPrint.append(estates[0].line(row));
                    col = 7;
                }
                // Manic Manor:
                else if (row >= 2 && row < 7 && col == 17) {
                    boardPrint.append(estates[1].line(row));
                    col = 22;
                }
                // Calamity Castle:
                else if (row >= 17 && row < 22 && col == 2) {
                    // 15 total - 7 word = 8 remaining. So 4 left and 4 right
                    boardPrint.append(estates[2].line(row));
                    col = 7;
                }

                // Peril Palace:
                else if (row >= 17 && row < 22 && col == 17) {
                    boardPrint.append(estates[3].line(row));
                    col = 22;
                }

                // Visitation Villa:
                else if (row >= 10 && row < 14 && col == 9) {
                    boardPrint.append(estates[4].line(row));
                    col = 15;
                }
                boardPrint.append(cells[row][col]);

            }
            boardPrint.append("\n");
        }
        System.out.print(boardPrint);

        /*DEBUG ONLY:
        //"Lu Be Ma Pe "

        System.out.print("DEBUG ONLY: estates[0].playerStrings(): = ");
        for(int i = 0; i < estates.length; i++){
            System.out.print(i + " = " + estates[i].playerStrings() + " | ");
        }
        System.out.println();
        */
    }
}