
public class Board {
	@SuppressWarnings("unused")
	private Cell[][] cells;

	public Board() {
        this.cells = new Cell[24][24];
        initializeCells();
    }

    // Function to initialize the cells with empty cells
    private void initializeCells() {
        for (int row = 0; row < 24; row++) {
            for (int column = 0; column < 24; column++) {
                cells[row][column] = new Cell(row, column);
            }
        }
    }

	public Cell getCellAt(int row, int column) {
		return cells[row][column];
	}

	public void print() {
		// TODO Implement a string representation of board
		System.out.println("Printing Board...");

	}
}
