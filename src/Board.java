import java.util.*;
public class Board {
    @SuppressWarnings("unused")
    private Cell[][] cells;
    private List<Cell> estateCells;

    public Board() {
        this.cells = new Cell[24][24];
        initializeCells();
    }

    // Function to initialize the cells with empty cells
    private void initializeCells() {
        for (int row = 0; row < 24; row++) {
            for (int column = 0; column < 24; column++) {
                cells[row][column] = new Path(row, column);
            }
        }
        // Initialize the other cells:
        // ******************************************************
        // Haunted House:
        // ******************************************************
        for (int row = 2; row <= 6; row++) {
            for (int column = 2; column <= 6; column++) {
                cells[row][column] = new Wall();
            }
        }
        // Entrances:
        // column 6 (x), row 3 (y) [row][column] [y][x] Flip the order.
        cells[3][6] = new Entrance(3, 6, null); // Pos on board validated. //Need to change the null.
        // column 5 (x), row 6 (y) [row][column] [y][x] Flip the order.
        cells[6][5] = new Entrance(6, 5, null); // Need to change the null.

        // ******************************************************
        // Manic Manor:
        // ******************************************************
        for (int row = 2; row <= 6; row++) {
            for (int column = 17; column <= 21; column++) {
                cells[row][column] = new Wall();
            }
        }
        // Entrances:
        // column 17 (x), row 5 (y) [row][column] [y][x] Flip the order.
        cells[5][17] = new Entrance(5, 17, null); // Need to change the null.
        // column 20 (x), row 6 (y) [row][column] [y][x] Flip the order.
        cells[6][20] = new Entrance(6, 20, null); // Need to change the null.

        // ******************************************************
        // Calamity Castle:
        // ******************************************************
        for (int row = 17; row <= 21; row++) {
            for (int column = 2; column <= 6; column++) {
                cells[row][column] = new Wall();
            }
        }
        // Entrances:
        // column 3 (x), row 17 (y) [row][column] [y][x] Flip the order.
        cells[17][3] = new Entrance(17, 3, null); // Need to change the null.
        // column 6 (x), row 18 (y) [row][column] [y][x] Flip the order.
        cells[18][6] = new Entrance(18, 6, null); // Need to change the null.

        // ******************************************************
        // Peril Palace:
        // ******************************************************
        for (int row = 17; row <= 21; row++) {
            for (int column = 17; column <= 21; column++) {
                cells[row][column] = new Wall();
            }
        }
        // Entrances:
        // column 18 (x), row 17 (y) [row][column] [y][x] Flip the order.
        cells[17][18] = new Entrance(17, 18, null); // Need to change the null.
        // column 17 (x), row 20 (y) [row][column] [y][x] Flip the order.
        cells[20][17] = new Entrance(20, 17, null); // Need to change the null.

        // ******************************************************
        // Visitation Villa:
        // ******************************************************
        for (int row = 10; row <= 13; row++) {
            for (int column = 9; column <= 14; column++) {
                cells[row][column] = new Wall();
            }
        }
        // Entrances:
        // column 12 (x), row 10 (y) [row][column] [y][x] Flip the order.
        cells[10][12] = new Entrance(10, 12, null); // Need to change the null.
        // column 9 (x), row 12 (y) [row][column] [y][x] Flip the order.
        cells[12][9] = new Entrance(12, 9, null); // Need to change the null.
        // column 11 (x), row 13 (y) [row][column] [y][x] Flip the order.
        cells[13][11] = new Entrance(13, 11, null); // Need to change the null.
        // column 14 (x), row 11 (y) [row][column] [y][x] Flip the order.
        cells[11][14] = new Entrance(11, 14, null); // Need to change the null.

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

    public Cell getCellAt(int row, int column) {
        return cells[row][column];
    }
    public void setCell(int row, int column, Cell c){cells[row][column] = c;}

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
                    // 15 total - 7 word = 8 remaining. So 4 left and 4 right
                    boardPrint.append(" Haunted ");
                    col = 6;
                } else if (row == 4 && col == 3) {
                    // 15 total - 5 word = 10 remaining. So 5 left and 5 right
                    boardPrint.append("   House ");
                    col = 6;
                } else if (row == 5 && col == 3) {
                    // 15 total
                    boardPrint.append("         ");
                    col = 6;
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
