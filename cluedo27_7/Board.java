package cluedo27_7;

import cluedo25_7.Estate;
import cluedo27_7.EstateCell.Direction;

import java.util.Map;

public class Board {
    private final int numRows;
    private final int numCols;
    private final Cell[][] cells;
    private final EstateCell[] estates = new EstateCell[5];
    public static final String WALLSTRING = "###";
    public static final Map<Direction, String> ENTRANCESTRINGS = Map.of(
            Direction.UP, "vvv", Direction.DOWN, "^^^",
            Direction.LEFT, ">>>", Direction.RIGHT, "<<<");

    public Board(int rows, int cols) {
        this.numRows = rows;
        this.numCols = cols;
        this.cells = new Cell[rows][cols];
        this.initializeCells();
    }

    public Cell getCellAt(int row, int col) {
        return cells[row][col];
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

    private void initializeCells() {
        for (int row = 0; row < 24; row++) {
            for (int col = 0; col < 24; col++) {
                cells[row][col] = new Path(row, col);
            }
        }
        // Initialize the other cells:
        // ******************************************************
        // Haunted House:
        // ******************************************************
        estates[0] = new HauntedHouse(this, 2, 2);
        fill(2,2,7,7, new Wall());
        fill(3,3,6,6, estates[0]);
        cells[3][6] = new Entrance(3,6, estates[0]);
        estates[0].addEntrance((Entrance) cells[3][6]);
        cells[6][5] = new Entrance(6,5, estates[0]);
        estates[0].addEntrance((Entrance) cells[6][5]);
        // ******************************************************
        // Manic Manor:
        // ******************************************************
        estates[1] = new ManicManor(this, 3,18);
        fill(2,17, 7,22, new Wall());
        fill(3, 18, 5, 21, estates[1]);
        // Entrances:
        // column 17 (x), row 5 (y) [row][column] [y][x] Flip the order.
        cells[5][17] = new Entrance(5, 17, estates[1]); // Need to change the null.
        estates[1].addEntrance((Entrance) cells[5][17]);
        cells[6][20] = new Entrance(6, 20,estates[1]); // Need to change the null.
        estates[1].addEntrance((Entrance) cells[6][20]);
        // ******************************************************
        // Calamity Castle:
        // ******************************************************
        estates[2] = new CalamityCastle(this,18, 3);
        fill(17,2,22,7, new Wall());
        fill(18, 3, 21, 6,  estates[2]);
        // Entrances:
        // column 3 (x), row 17 (y) [row][column] [y][x] Flip the order.
        cells[17][3] = new Entrance(17,13, null); // Need to change the null.
        // column 6 (x), row 18 (y) [row][column] [y][x] Flip the order.
        cells[18][6] = new Entrance(18, 6, null); // Need to change the null.

        // ******************************************************
        // Peril Palace:
        // ******************************************************
        fill(17,17,22,22, new Wall());
        //TODO write Peril Palace:
        // Entrances:
        // column 18 (x), row 17 (y) [row][column] [y][x] Flip the order.
        cells[17][18] = new Entrance(17,18, null); // Need to change the null.
        // column 17 (x), row 20 (y) [row][column] [y][x] Flip the order.
        cells[20][17] = new Entrance(20,17, null); // Need to change the null.

        // ******************************************************
        // Visitation Villa:
        // ******************************************************
        //TODO write Visitation Villa:
        fill(10,9,14,15, new Wall());
        // column 12 (x), row 10 (y) [row][column] [y][x] Flip the order.
        cells[10][12] = new Entrance(10,12, null); // TODO change all the nulls
        cells[12][9] = new Entrance(10,12, null); // Need to change the null.
        // column 11 (x), row 13 (y) [row][column] [y][x] Flip the order.
        cells[13][11] = new Entrance(10,12, null); // Need to change the null.
        // column 14 (x), row 11 (y) [row][column] [y][x] Flip the order.
        cells[11][14] = new Entrance(10,12,  null); // Need to change the null.

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

    public void setCell(int row, int col, EstateCell estateCell) {
        cells[row][col] = estateCell;
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
                    boardPrint.append("   "); // 3 characters.
                    continue;
                }
                // Haunted House:
                else if (row > 1 && row < 7 && col == 2) {
                    // 15 total - 7 word = 8 remaining. So 4 left and 4 right
                    boardPrint.append(estates[0].getLine(row));
                    col = 7;
                }
                // Manic Manor:
                else if (row == 3 && col == 18) {
                    // 15 total - 5 word = 10 remaining. So 5 left and 5 right
                    boardPrint.append("  Manic  ");
                    col = 21;
                } else if (row == 4 && col == 18) {
                    // 15 total - 5 word = 10 remaining. So 5 left and 5 right
                    boardPrint.append(" Manor   ");
                    col = 21;
                } else if (row == 5 && col == 18) {
                    // 15 total
                    boardPrint.append("         ");
                    col = 21;
                }
                // Calamity Castle:
                else if (row == 18 && col == 3) {
                    // 15 total - 5 word = 10 remaining. So 5 left and 5 right
                    boardPrint.append(" Calamit ");
                    col = 6;
                } else if (row == 19 && col == 3) {
                    // 15 total - 5 word = 10 remaining. So 5 left and 5 right
                    boardPrint.append("Castle   ");
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
                boardPrint.append(cells[row][col] + "");

            }
            boardPrint.append("\n");
        }
        System.out.print(boardPrint);

    }
}