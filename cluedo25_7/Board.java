package cluedo25_7;

import java.util.Map;

public class Board {
    private final int numRows;
    private final int numCols;
    private final Cell[][] cells;
    private final Estate[] estates = new Estate[5];
    public static final String WALLSTRING = "###";
    public static final Map<Direction, String> ENTRANCESTRINGS = Map.of(
            Direction.UP, "^^^", Direction.DOWN, "vvv",
            Direction.LEFT, ">>>", Direction.RIGHT, "<<<");

    public Board(int rows, int cols) {
        this.numRows = rows;
        this.numCols = cols;
        this.cells = new Cell[rows][cols];
        this.initializeCells();
    }

    public void fill(int startRow, int startCol, int endRow, int endCol, Cell cell) {
        if (startRow > endRow || endRow > numRows || startCol > endCol || endCol > numCols) {
            throw new IllegalArgumentException();
        }
        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                cells[row][col] = cell.copy();
            }
        }
    }

    public Cell getCellAt(int row, int col) {
        return cells[row][col];
    }

    private void initializeCells() {
        fill(0, 0, 24, 24, new Path());
        // Initialize the other cells:
        // ******************************************************
        // Haunted House:
        // ******************************************************
        estates[0] = new HauntedHouse(this, 2);
        fill(2,2,7,7, new Wall());
        fill(3,3,6,6, estates[0]);
        cells[3][6] = new Entrance(estates[0]);
        cells[6][5] = new Entrance(estates[0]);

        // ******************************************************
        // Manic Manor:
        // ******************************************************
        fill(2,17, 7,22, new Wall());
        //TODO write Manic Manor
        // Entrances:
        // column 17 (x), row 5 (y) [row][column] [y][x] Flip the order.
        cells[5][17] = new Entrance( null); // Need to change the null.
        // column 20 (x), row 6 (y) [row][column] [y][x] Flip the order.
        cells[6][20] = new Entrance(null); // Need to change the null.

        // ******************************************************
        // Calamity Castle:
        // ******************************************************
        fill(17,2,22,7, new Wall());
        //TODO write Calamity Castle
        // Entrances:
        // column 3 (x), row 17 (y) [row][column] [y][x] Flip the order.
        cells[17][3] = new Entrance(null); // Need to change the null.
        // column 6 (x), row 18 (y) [row][column] [y][x] Flip the order.
        cells[18][6] = new Entrance(null); // Need to change the null.

        // ******************************************************
        // Peril Palace:
        // ******************************************************
        fill(17,17,22,22, new Wall());
        //TODO write Peril Palace:
        // Entrances:
        // column 18 (x), row 17 (y) [row][column] [y][x] Flip the order.
        cells[17][18] = new Entrance( null); // Need to change the null.
        // column 17 (x), row 20 (y) [row][column] [y][x] Flip the order.
        cells[20][17] = new Entrance(null); // Need to change the null.

        // ******************************************************
        // Visitation Villa:
        // ******************************************************
        //TODO write Visitation Villa:
        fill(10,9,14,15, new Wall());
        // column 12 (x), row 10 (y) [row][column] [y][x] Flip the order.
        cells[10][12] = new Entrance( null); // TODO change all the nulls
        cells[12][9] = new Entrance(null); // Need to change the null.
        // column 11 (x), row 13 (y) [row][column] [y][x] Flip the order.
        cells[13][11] = new Entrance(null); // Need to change the null.
        // column 14 (x), row 11 (y) [row][column] [y][x] Flip the order.
        cells[11][14] = new Entrance( null); // Need to change the null.

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
        // TODO Implement a string representation of board
        System.out.println("Printing Board...");
        StringBuilder boardPrint = new StringBuilder();
        for (int row = 0; row < 24; row++) {

            for (int col = 0; col < 24; col++) {
                if (cells[row][col] == null) {
                    boardPrint.append("None "); // 4 characters + space at the end.
                    continue;
                }
                // Haunted House:
                else if (row == 3 && col == 3) {
                    // 9 total - 7 word = 2 remaining. So 1 left and 1 right
                    boardPrint.append(" Haunted ");
                    col = 6;
                } else if (row == 4 && col == 3) {
                    // 9 total - 5 word = 4 remaining. So 2 left and 2 right
                    boardPrint.append("  House  ");
                    col = 6;
                } else if (row == 5 && col == 3) {
                    // 9 total
                    boardPrint.append(" ".repeat(9));
                    col = 6;
                }
                // Manic Manor:
                else if (row == 3 && col == 18) {
                    // 9 total - 5 word = 4 remaining. So 2 left and 2 right
                    boardPrint.append("  Manic  ");
                    col = 21;
                } else if (row == 4 && col == 18) {
                    // 9 total - 5 word = 4 remaining. So 5 left and 5 right
                    boardPrint.append("  Manor  ");
                    col = 21;
                } else if (row == 5 && col == 18) {
                    // 9 total
                    boardPrint.append(" ".repeat(9));
                    col = 21;
                }
                // Calamity Castle:
                else if (row == 18 && col == 3) {
                    // 9 total - 8 word = 1 remaining. So 1 left and 0 right
                    boardPrint.append(" Calamity");
                    col = 6;
                } else if (row == 19 && col == 3) {
                    // 9 total - 5 word = 4 remaining. So 2 left and 2 right
                    boardPrint.append("  Castle  ");
                    col = 6;
                } else if (row == 20 && col == 3) {
                    // 15 total
                    boardPrint.append("         ");
                    col = 6;
                }

                // Peril Palace:
                else if (row == 18 && col == 18) {
                    // 15 total - 5 word = 10 remaining. So 5 left and 5 right
                    boardPrint.append(" Peril   ");
                    col = 21;
                } else if (row == 19 && col == 18) {
                    // 15 total - 5 word = 10 remaining. So 5 left and 5 right
                    boardPrint.append(" Palace  ");
                    col = 21;
                } else if (row == 20 && col == 18) {
                    // 15 total
                    boardPrint.append("         ");
                    col = 21;
                }

                // Visitation Villa:
                else if (row == 11 && col == 10) {
                    // 15 total - 5 word = 10 remaining. So 5 left and 5 right
                    boardPrint.append(" Visit      ");
                    col = 14;
                } else if (row == 12 && col == 10) {
                    // 15 total - 5 word = 10 remaining. So 5 left and 5 right
                    boardPrint.append(" Villa      ");
                    col = 14;
                }
                boardPrint.append(cells[row][col]);

            }
            boardPrint.append("\n");
        }
        System.out.print(boardPrint);

    }
}