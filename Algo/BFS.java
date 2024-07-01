package Projects.MazeSolver.Algo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import Projects.MazeSolver.Board.MyFrame;
import generics.Pair;

public class BFS extends SwingWorker<Void, Void> {
    private ArrayList<ArrayList<Integer>> board;
    private MyFrame frame;
    private boolean[][] vis;
    private Queue<Pair<Pair<Integer, Integer>, String>> q;
    private int finRow, finCol;

    public BFS(ArrayList<ArrayList<Integer>> board, MyFrame frame) {
        this.board = board;
        this.frame = frame;
        int m = board.size();
        int n = board.get(0).size();
        vis = new boolean[m][n];
        q = new LinkedList<>();
    }

    @Override
    protected Void doInBackground() throws Exception {
        int m = board.size();
        int n = board.get(0).size();

        finRow = -1;
        finCol = -1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board.get(i).get(j) == 2) {
                    q.add(new Pair<>(new Pair<>(i, j), ""));
                    vis[i][j] = true;
                }
                if (board.get(i).get(j) == 3) {
                    finRow = i;
                    finCol = j;
                }
            }
        }

        int drow[] = { -1, 0, 1, 0 };
        int dcol[] = { 0, 1, 0, -1 };
        String directions[] = { "U", "R", "D", "L" };

        while (!q.isEmpty()) {
            Pair<Pair<Integer, Integer>, String> p = q.poll();
            int row = p.getFirst().getFirst();
            int col = p.getFirst().getSecond();
            String path = p.getSecond();

            if (row == finRow && col == finCol) {
                System.out.println("Path is: " + path);
                markPath(board, path, finRow, finCol);
                return null;
            }

            for (int i = 0; i < 4; i++) {
                int nrow = row + drow[i];
                int ncol = col + dcol[i];
                if (nrow >= 0 && nrow < m && ncol >= 0 && ncol < n && !vis[nrow][ncol]
                        && board.get(nrow).get(ncol) != 1) {
                    vis[nrow][ncol] = true;
                    q.add(new Pair<>(new Pair<>(nrow, ncol), path + directions[i]));
                    final int r = nrow, c = ncol;
                    SwingUtilities.invokeLater(() -> frame.markCell(r, c, Color.BLUE));
                    Thread.sleep(100);
                }
            }
        }
        System.out.println("No path found.");
        return null;
    }

    private void markPath(ArrayList<ArrayList<Integer>> board, String path, int finRow, int finCol) {
        int row = finRow;
        int col = finCol;
        for (int i = path.length() - 1; i >= 0; i--) {
            char direction = path.charAt(i);
            switch (direction) {
                case 'U':
                    row++;
                    break;
                case 'R':
                    col--;
                    break;
                case 'D':
                    row--;
                    break;
                case 'L':
                    col++;
                    break;
            }
            final int r = row, c = col;
            SwingUtilities.invokeLater(() -> frame.markPathCell(r, c, Color.MAGENTA));
        }
    }

}