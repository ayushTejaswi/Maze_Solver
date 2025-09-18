package Projects.Maze_Solver.Algo;

import Projects.Maze_Solver.Board.MyFrame;

import java.awt.Color;
import java.util.*;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class AStar extends SwingWorker<Void, Void> {
    private ArrayList<ArrayList<Integer>> board;
    private MyFrame frame;
    private boolean[][] vis;
    private int[][] parent;
    private int finRow, finCol, startRow, startCol;
    private int rows, cols;

    static class Node implements Comparable<Node> {
        int row, col, g, f;

        Node(int r, int c, int g, int f) {
            this.row = r;
            this.col = c;
            this.g = g;
            this.f = f;
        }

        public int compareTo(Node o) {
            return Integer.compare(this.f, o.f);
        }
    }

    public AStar(ArrayList<ArrayList<Integer>> board, MyFrame frame) {
        this.board = board;
        this.frame = frame;
        this.rows = board.size();
        this.cols = board.get(0).size();
        this.vis = new boolean[rows][cols];
        this.parent = new int[rows * cols][2];
    }

    @Override
    protected Void doInBackground() throws Exception {
        startRow = -1;
        startCol = -1;
        finRow = -1;
        finCol = -1;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board.get(i).get(j) == 2) {
                    startRow = i;
                    startCol = j;
                }
                if (board.get(i).get(j) == 3) { // red end
                    finRow = i;
                    finCol = j;
                }
            }
        }

        if (startRow == -1 || finRow == -1) {
            System.out.println("Start or End not set.");
            return null;
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(startRow, startCol, 0, heuristic(startRow, startCol)));

        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            if (vis[curr.row][curr.col])
                continue;
            vis[curr.row][curr.col] = true;

            if (!(curr.row == startRow && curr.col == startCol)) {
                final int r = curr.row, c = curr.col;
                SwingUtilities.invokeLater(() -> frame.markCell(r, c, Color.BLUE));
                Thread.sleep(50);
            }

            if (curr.row == finRow && curr.col == finCol) {
                reconstructPath();
                return null;
            }

            int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
            for (int[] d : dirs) {
                int nr = curr.row + d[0], nc = curr.col + d[1];
                if (isValid(nr, nc) && !vis[nr][nc]) {
                    parent[nr * cols + nc][0] = curr.row;
                    parent[nr * cols + nc][1] = curr.col;
                    int gNew = curr.g + 1;
                    int fNew = gNew + heuristic(nr, nc);
                    pq.add(new Node(nr, nc, gNew, fNew));
                }
            }
        }

        System.out.println("No path found with A*.");
        return null;
    }

    private void reconstructPath() throws InterruptedException {
        int r = finRow, c = finCol;
        while (!(r == startRow && c == startCol)) {
            final int rr = r, cc = c;
            SwingUtilities.invokeLater(() -> frame.markPathCell(rr, cc, Color.MAGENTA));
            Thread.sleep(100);
            int pr = parent[r * cols + c][0];
            int pc = parent[r * cols + c][1];
            r = pr;
            c = pc;
        }
    }

    private boolean isValid(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols && board.get(r).get(c) != 1;
    }

    private int heuristic(int r, int c) {
        return Math.abs(r - finRow) + Math.abs(c - finCol);
    }
}
