import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    public static Scanner scan = new Scanner(System.in);

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_BOLD = "\u001b[1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static int currentGenartion = 0;

    public static void main(String[] args) {
        runGame();
    }

    static int[][] SLCInput (String input) {
        int[][] output = new int[(input.length() - 3)/11][2];
        input = input.substring(2);
        String[] splitUpCordsSt = new String[input.length()/11];

        //(i == 0 ) ? input.substring(0,10):
        for(int i = 0; i < input.length()/11; i++) {
            splitUpCordsSt[i] = input.substring((i * 11), ((i++) * 11));
        }
        for(int i = 0;i < splitUpCordsSt.length; i++){
            output[i][0] = Integer.parseInt(splitUpCordsSt[i].substring(2,4));
            output[i][1] = Integer.parseInt(splitUpCordsSt[i].substring(7,9));
        }
        return output;
    }

    static void CONInput(int x, Cell[][] grid, int secs) {

        if (x > 0) {
            displayASCIIGrid(grid, "Genartion: " + currentGenartion);
            try {
                TimeUnit.MILLISECONDS.sleep(secs);
            } catch (InterruptedException e) {
                System.out.println(":(");
                return;
            }
            CONInput(x - 1, grid, secs);
        } else {
            runInput(grid, secs);
        }
    }

    static void runInput(Cell[][] grid, int defaultItv) {
        System.out.print("Enter Command: ");
        String inputString = scan.nextLine();
        String command = (inputString.length() == 3) ? inputString : inputString.substring(0, 3);
        int intervel = defaultItv;
        boolean shouldStop = false;

         switch (command) {
            case "CON":
                String x = inputString.substring(3);
                CONInput(Integer.parseInt(x), grid, intervel);
                shouldStop = true;
                break;
            case "STP":
                shouldStop = true;
                break;
            case "RES":
                runGame();
                break;
            case "NXT":
                grid = nextGenartion(grid);
                displayASCIIGrid(grid);
                break;
            case "ITV":
                String y = inputString.substring(3);
                intervel = Integer.parseInt(y);
                break;
            case "SCL":
                int[][] liveCellCords = SLCInput(inputString);
                setLiveCells(grid, liveCellCords);
                break;
            case "STS":
                System.out.println("Current Gen: " + currentGenartion + " Current Interval: " + intervel);
                break;
            case "HLP":
                System.out.println("CONx // Continute - Displays the next X intervals at a constant Speed");
                System.out.println("STP // Stop - Stops the game");
                System.out.println("RES // Resart - Restart the game");
                System.out.println("NXT // Next - Displays the next genation");
                System.out.println("ITVx // Intervel - Sets the intervel for Continue to X in millseconds");
                System.out.println("SLC, // Set Live Cells - Must Have Cell Cords be 3 digits & be Formated Like SLC,{001, 011},{002, 012}");
                System.out.println("STS // Stats - Displays curenten Gen and Interval");
                System.out.println("HLP // Help - Displays This List");
                break;
            default:
                System.out.println(ANSI_RED + "Invaild Comand" + ANSI_RESET);
                runInput(grid, intervel);
            break;
        }
        if (shouldStop == true) {
           return;
        } else {
            runInput(grid, intervel);
        }
    }

    static int getYGridSizeInput () {
        int x = 10;
        System.out.print("Y/Virtical: ");
        try {
            x = Integer.parseInt(scan.nextLine());
        } catch (Exception e) {
            System.out.println(ANSI_BOLD + ANSI_RED + "Invalid Input Try Agian" + ANSI_RESET);
            x = getYGridSizeInput();
        }
        
        return x;
    }

    static int getXGridSizeInput () {
        int x = 10;
        System.out.print("X/Horizantel: ");
        try {
            x = Integer.parseInt(scan.nextLine());
        } catch (Exception e) {
            System.out.println(ANSI_BOLD + ANSI_RED + "Invalid Input Try Agian" + ANSI_RESET);
            x = getXGridSizeInput();
        }
        
        return x;
    }

    static void runGame() {
        System.out.println(ANSI_BOLD + "Type HLP for Help" + ANSI_RESET);

        int[] gridSize = {10, 10};
        System.out.println("Enter GridSize");
        gridSize[0] = getXGridSizeInput();
        gridSize[1] = getYGridSizeInput();
        Cell[][] cellGrid = new Cell[gridSize[0]][gridSize[1]];
        cellGrid = initializeGrid(cellGrid);
        
        currentGenartion = 0;

        runInput(cellGrid, 1000);
    }

    //#region Genartion Functions
    static Cell[][] nextGenartion (Cell[][] grid){
        Cell[][] outGrid = new Cell[grid.length][grid[0].length];  
        outGrid = initializeGrid(outGrid);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                outGrid[i][j].setSate(cellLogic(grid, i, j));
            }
        }

        currentGenartion++;
        return outGrid;
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
    //#endregion
    //#region Cell Minuplation Functions
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
    //#endregion

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
        System.out.println("\n");
    }

    static void displayASCIIGrid (Cell[][] grid, String extra) {
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
        System.out.println(extra + "\n");
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