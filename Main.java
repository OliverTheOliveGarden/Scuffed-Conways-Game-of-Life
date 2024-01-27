public class Main {

    enum inputs {
        CON, // Continute
        STP, // Stop
        RES, // Reset
        NXT, // Next
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static void main(String[] args) {
        Cell[][] cellGrid = new Cell[7][7];
        int[][] seedLive = {{2, 3},{2, 2},{3, 3},{2,4}};

        cellGrid = initializeGrid(cellGrid);
        cellGrid = setLiveCells(cellGrid, seedLive);

        displayASCIIGrid(cellGrid);
        displayASCIIGrid(nextGenartion(cellGrid));
    }


    static Cell[][] nextGenartion (Cell[][] grid){
        Cell[][] outGrid = new Cell[grid.length][grid[0].length];  
        outGrid = initializeGrid(outGrid);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                outGrid[i][j].setSate(cellLogic(grid, i, j));
            }
        }

        return outGrid;
    }

    static Boolean cellLogic (Cell[][] grid, int x, int y) {
        int numOfLiveNeighbours = getNumOfLiveCells(getNeighbours(grid, x, y));
        Cell outCell = new Cell(Cell.DEAD);
        boolean cellState = grid[x][y].getState();

        if (numOfLiveNeighbours < 2 && cellState == Cell.LIVE) { 
            outCell.setSate(Cell.DEAD);
            return outCell.getState();
        } else if (numOfLiveNeighbours > 3 && cellState == Cell.LIVE) {
            outCell.setSate(Cell.DEAD);
            return outCell.getState();
        } else if (numOfLiveNeighbours == 2 && cellState == Cell.LIVE) {
            outCell.setSate(Cell.LIVE);
            return outCell.getState();
        } else if (numOfLiveNeighbours == 3 && cellState == Cell.LIVE) {
            outCell.setSate(Cell.LIVE);
            return outCell.getState();
        } else if (numOfLiveNeighbours == 3 && cellState == Cell.DEAD) {
            outCell.setSate(Cell.LIVE);
            return outCell.getState();
        }

        return outCell.getState();
    }   

    static int getNumOfLiveCells(Cell[] cells) {
        int x = 0; 
        for (int i = 0; i < cells.length; i++) {
            x += (cells[i].getState() == Cell.LIVE) ? 1 : 0;
        }
        return x;
    }

    static Cell[] getNeighbours(Cell[][] grid, int x, int y) {
        Cell[] neighbours = new Cell[8];

        for (int i = 0; i < neighbours.length; i++) {
            neighbours[i] = new Cell(Cell.DEAD);
        }  

        try {
            neighbours[0].setSate(grid[x-1][y-1].getState()); 
        } catch (Exception e) {
            neighbours[0].setSate(Cell.DEAD);;
        }

        try {
            neighbours[1].setSate(grid[x-1][y].getState());
        } catch (Exception e) {
            neighbours[1].setSate(Cell.DEAD);;
        }

        try {
            neighbours[2].setSate(grid[x-1][y+1].getState());
        } catch (Exception e) {
            neighbours[2].setSate(Cell.DEAD);;
        }

        try {
            neighbours[3].setSate(grid[x][y-1].getState());
        } catch (Exception e) {
            neighbours[3].setSate(Cell.DEAD);;
        }

        try {
            neighbours[4].setSate(grid[x][y+1].getState());
        } catch (Exception e) {
            neighbours[4].setSate(Cell.DEAD);;
        }

        try {
            neighbours[5].setSate(grid[x+1][y-1].getState());
        } catch (Exception e) {
            neighbours[5].setSate(Cell.DEAD);;
        }

        try {
            neighbours[6].setSate(grid[x+1][y].getState());
        } catch (Exception e) {
            neighbours[6].setSate(Cell.DEAD);;
        }

        try {
            neighbours[7].setSate(grid[x+1][y+1].getState());
        } catch (Exception e) {
            neighbours[7].setSate(Cell.DEAD);;
        }

        return neighbours;
    }

    static Cell[][] setLiveCells (Cell[][] grid, int[][] coordinates) {
        for (int i = 0; i < coordinates.length; i++) {
            grid[coordinates[i][0]][coordinates[i][1]].setSate(true);
        }
        return grid;
    }

    static Cell[][] initializeGrid(Cell[][] grid){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell(Cell.LIVE);
            }
        }

        return grid;
    }

    static void displayASCIIGrid (Cell[][] grid) {
        String[][] outGrid = new String[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                outGrid[i][j] = (grid[i][j].getState() == Cell.LIVE) ? ANSI_WHITE_BACKGROUND + ANSI_WHITE + "+" + ANSI_RESET: ANSI_BLACK_BACKGROUND + ANSI_BLACK + "-" + ANSI_RESET;
            }
        }

        for (int i = 0; i < outGrid.length; i++) {
            for (int j = 0; j < outGrid[i].length; j++) {
                System.err.print(outGrid[i][j]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

}

class Cell {
    boolean state;
    public static final boolean LIVE = true;
    public static final boolean DEAD = false;

    public Cell (boolean state) {
        state = this.state;
    }

    public boolean getState() {
        return state;
    }

    public void setSate(boolean x) {
        state = x;
    }
}