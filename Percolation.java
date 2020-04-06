/* *****************************************************************************
 *  Name:    Nabil Rahat
 *  NetID:   nrahat
 *  Precept: P01
 *
 *  Description:  Percolation
 *
 **************************************************************************** */

// import java.util.ArrayList;

public class Percolation {
    private static final int CONNECTOR = -20; // gg
    private static final int CONNECTOR_BOTTOM = -30; // gg
    private static final int CUST_CONS = 10; // gg

    private int[] sz;       // ccc
    private final Cell[][] nXnGrid;     // ccc
    private int numberOfOpenSites;     // ccc
    private int rootTreeSize = 0; // cccc
    private boolean percolates = false; // dddd
    private int Row = 9; // ggg

    // private boolean[] tempOpen = new boolean[CUST_CONS]; // xxx
    // private Cell[] tempRoot = new Cell[CUST_CONS]; // ccc
    // private int[] tempRootVal = new int[CUST_CONS]; // nnn
    // hhh
    private final Cell virtualUpperRoot =
            new Cell(CONNECTOR, -1, -1);

    private final Cell virtualBottomRoot =
            new Cell(CONNECTOR_BOTTOM, -2, -2);

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        Row = n;
        sz = new int[((n + 1) * (n + 1))];
        for (int v = 0; v < (n + 1) * (n + 1); v++) {
            sz[v] = 1;
        }
        // virtualUpperRoot = new Cell(CONNECTOR, -1, -1);
        virtualUpperRoot.setOpen(true);

        int val = 1;
        nXnGrid = new Cell[n + 1][n + 1];
        Cell tempCell = null;
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                tempCell = new Cell(val, i, j);
                nXnGrid[i][j] = tempCell;
                val++;
                if (i == Row) {
                    nXnGrid[i][j].setCoonectedToBottom(true);
                }
            }
        }
        numberOfOpenSites = 0;
        rootTreeSize = 0;

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        if (row == 1) {
            //
            if (nXnGrid[row][col].getRoot() == null) {
                nXnGrid[row][col].setOpen(true);
                nXnGrid[row][col].setFull(true);
                nXnGrid[row][col].setRoot(virtualUpperRoot);
                incrementNumberOfOpenSite();
                incrementRooTree(1);
            }
            if (isCellOpen(row + 1, col)) {
                // bottom element of first row element
                // open bt doesnt have root
                if (nXnGrid[row + 1][col].getRoot() == null) {
                    nXnGrid[row + 1][col].setRoot(virtualUpperRoot);
                    incrementRooTree(sz[nXnGrid[row + 1][col].getValue()]);
                    if (nXnGrid[row + 1][col].isCoonectedToBottom()) {
                        percolates = true;
                    }
                }
                else {
                    // open and has a root
                    Cell myCll = getRoot(row + 1, col);
                    if (myCll != null && myCll.getValue() != CONNECTOR) {
                        incrementRooTree(sz[myCll.getValue()]);
                        myCll.setRoot(virtualUpperRoot);
                        if (myCll.isCoonectedToBottom()) {
                            percolates = true;
                        }
                    }
                }
            }
        }
        else {
            boolean[] tempOpen = new boolean[CUST_CONS];
            tempOpen[0] = isCellOpen(row + 1, col);
            tempOpen[1] = isCellOpen(row - 1, col);
            tempOpen[2] = isCellOpen(row, col + 1);
            tempOpen[3] = isCellOpen(row, col - 1);
            boolean result = tempOpen[0] || tempOpen[1] || tempOpen[2] || tempOpen[3];
            // no open cells
            if (!result) {
                nXnGrid[row][col].setOpen(true);
                incrementNumberOfOpenSite();
            }
            else {
                Cell[] tempRoot = new Cell[CUST_CONS];
                int[] tempRootVal = new int[CUST_CONS];
                Cell downR = getRoot(row + 1, col);
                Cell upR = getRoot(row - 1, col);
                Cell rghtR = getRoot(row, col + 1);
                Cell lftR = getRoot(row, col - 1);
                if (downR != null) {
                    tempRoot[0] = downR;
                    tempRootVal[0] = downR.getValue();
                }
                if (upR != null) {
                    tempRoot[1] = upR;
                    tempRootVal[1] = upR.getValue();
                }
                if (rghtR != null) {
                    tempRoot[2] = rghtR;
                    tempRootVal[2] = rghtR.getValue();
                }
                if (lftR != null) {
                    tempRoot[3] = lftR;
                    tempRootVal[3] = lftR.getValue();
                }

                boolean fresult = tempRootVal[0] == CONNECTOR ||
                        tempRootVal[1] == CONNECTOR ||
                        tempRootVal[2] == CONNECTOR ||
                        tempRootVal[3] == CONNECTOR;

                if (fresult) {
                    for (int i = 0; i < tempRoot.length; i++) {
                        if (tempRoot[i] != null && tempRoot[i].getValue() != virtualUpperRoot
                                .getValue()) {
                            incrementRooTree(sz[tempRoot[i].getValue()]);
                            tempRoot[i].setRoot(virtualUpperRoot);
                            if (tempRoot[i].isCoonectedToBottom()) {
                                percolates = true;
                            }
                        }
                    }

                    nXnGrid[row][col].setOpen(true);
                    nXnGrid[row][col].setRoot(virtualUpperRoot);
                    incrementNumberOfOpenSite();
                }
                else {
                    int maxTrSz = 0;
                    Cell maxCell = null;
                    int maxInd = 0;
                    for (int h = 0; h < tempRoot.length; h++) {
                        if (tempRoot[h] != null && sz[tempRoot[h].getValue()] > maxTrSz) {
                            maxCell = tempRoot[h];
                            maxTrSz = sz[tempRoot[h].getValue()];
                            maxInd = h;
                        }
                    }
                    tempRoot[maxInd] = null;
                    for (int h = 0; h < tempRoot.length; h++) {
                        if (tempRoot[h] != null && tempRoot[h].getRoot() == null && (maxCell
                                != null) && (
                                tempRoot[h].getValue()
                                        != maxCell.getValue())) {
                            tempRoot[h].setRoot(maxCell);
                            sz[maxCell.getValue()] += sz[tempRoot[h].getValue()];
                            if (row == Row) {
                                tempRoot[h].setCoonectedToBottom(true);
                            }
                        }
                    }
                    nXnGrid[row][col].setOpen(true);
                    nXnGrid[row][col].setRoot(maxCell);
                    incrementNumberOfOpenSite();
                    if (row == Row) {
                        maxCell.setCoonectedToBottom(true);
                    }

                }
            }

        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isValid(row, col)) {
            throwException();
        }
        return nXnGrid[row][col].isOpen();
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isValid(row, col)) {
            throwException();
        }
        Cell root = getRoot(row, col);
        if (root == null) {
            return false;
        }
        return root.getValue() == CONNECTOR;

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // // return percolates;
        // Cell root = null;
        // for (int i = 1; i < nXnGrid.length; i++) {
        //     if (!isOpen(nXnGrid.length - 1, i)) {
        //         continue;
        //     }
        //     root = getRoot(nXnGrid.length - 1, i);
        //     if (root != null && root.getValue() == CONNECTOR) {
        //         return true;
        //     }
        // }
        // return false;
        return percolates;
    }

    // row or column no is valid or not
    private boolean isValid(int row, int col) {
        if (row <= 0 || row >= nXnGrid.length || col <= 0 || col >= nXnGrid.length) {
            return false;
        }
        return true;
    }

    // th
    private void throwException() {
        throw new IllegalArgumentException();
    }

    // to increment the number of open site
    private void incrementNumberOfOpenSite() {
        numberOfOpenSites++;
    }

    // th
    private void incrementRooTree(int val) {
        rootTreeSize += val;
    }


    // is the cell open
    private boolean isCellOpen(int row, int col) {
        if (!isValid(row, col)) {
            return false;
        }
        return nXnGrid[row][col].isOpen();
    }

    // rff
    private Cell getRoot(Cell cell) {
        if (cell == null) {
            return null;
        }

        // Cell[] temp = new Cell[(nXnGrid.length * nXnGrid.length)+1];
        // int h = 0;
        while (cell.getRoot() != null) {
            // temp[h] = cell;
            // System.out.println("d");
            cell = cell.getRoot();
            // h++;
        }
        // int h2 = 0;
        // while (temp[h2] != null) {
        //     temp[h2].setRoot(cell);
        //     h2++;
        // }
        return cell;
    }

    // ff
    private Cell getRoot(int row, int col) {
        Cell cell = getCell(row, col);
        if (!isCellOpen(row, col)) {
            return null;
        }
        return getRoot(cell);
    }

    // gg gg
    private Cell getCell(int row, int col) {

        if (!isValid(row, col)) {
            return null;
        }
        return nXnGrid[row][col];
    }

    // public void print() {
    //     for (int i = 1; i < nXnGrid.length; i++) {
    //         for (int j = 1; j < nXnGrid[i].length; j++) {
    //             if (nXnGrid[i][j].getValue() == CONNECTOR) {
    //                 //System.out.print("A" + "");
    //
    //                 System.out.print("(" + (nXnGrid[i][j].isOpen() == false ? "F" : "T") + ","
    //                                          + nXnGrid[i][j].getValue() + ")");
    //             }
    //             else {
    //                 // System.out.print(nXnGrid[i][j].getValue() + "");
    //                 System.out.print("(" + (nXnGrid[i][j].isOpen() == false ? "F" : "T") + ","
    //                                          + nXnGrid[i][j].getValue() + ")");
    //             }
    //
    //         }
    //         System.out.print("\n");
    //     }
    // }

    // // test client (optional)
    // public static void main(String[] args) {
    // }
    private class Cell {
        private int row;     // ccc
        private int col;    // ccc
        private final int value;    // ccc
        private Cell root; // ccc
        private boolean isOpen;    // ccc
        private boolean isFull;    // ccc
        private int size;    // ccc
        private boolean coonectedToBottom;

        // ccc
        public Cell(int val, int row, int col) {
            this.value = val;
            this.row = row;
            this.col = col;
            isOpen = false;
            isFull = false;
            size = 1;
            root = null;
            coonectedToBottom = false;
            // bottom = null;
        }

        // hhh
        public boolean isCoonectedToBottom() {
            return coonectedToBottom;
        }

        // hhh
        public void setCoonectedToBottom(boolean coonectedToBottom) {
            this.coonectedToBottom = coonectedToBottom;
        }

        // dd
        public int getValue() {
            return value;
        }

        // public void setValue(int value) {
        //     this.value = value;
        // }

        // ccc
        public int getRow() {
            return row;
        }

        // ccc
        public void setRow(int row) {
            this.row = row;
        }

        // ccc
        public int getCol() {
            return col;
        }

        // ccc
        public void setCol(int col) {
            this.col = col;
        }

        // ccc
        public Cell getRoot() {
            return root;
        }

        // ccc
        public void setRoot(Cell root) {
            this.root = root;
        }

        // ccc
        public boolean isOpen() {
            return isOpen;
        }

        // ccc
        public void setOpen(boolean open) {
            isOpen = open;
        }

        // ccc
        public boolean isFull() {
            return isFull;
        }

        // ccc
        public void setFull(boolean full) {
            isFull = full;
        }

        // ccc
        public int getSize() {
            return size;
        }

        // ccc
        public void setSize(int size) {
            this.size = size;
        }

        // vvv
        public void incrementSize() {
            size++;
        }
    }
}





