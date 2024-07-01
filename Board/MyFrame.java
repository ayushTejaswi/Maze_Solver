package Projects.Maze_Solver.Board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MyFrame extends JFrame implements ActionListener {
    private Cell[][] grid;
    private Cell currGreenCell = null;
    private Cell currRedCell = null;
    private final int rows = 20;
    private final int cols = 20;

    ImageIcon img;
    JMenu open;
    JMenu algo;
    JMenuItem newB;
    JMenuItem bfs;
    JMenuItem dfs;
    JMenuItem a_;
    JMenuItem exit;

    public void markCell(int row, int col, Color color) {
        if (grid[row][col].getValue() != 2 && grid[row][col].getValue() != 3) {
            grid[row][col].setBackground(color);
        }
    }

    public void markPathCell(int row, int col, Color color) {
        if (grid[row][col].getValue() == 0) {
            grid[row][col].setBackground(color);
        }
    }

    MyFrame() {
        img = new ImageIcon("tattoo.png");

        this.setVisible(true);
        this.setTitle("Maze Solver");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        JMenuBar menu = new JMenuBar();

        open = new JMenu("Open");
        algo = new JMenu("Algorithm");
        bfs = new JMenuItem("BFS");
        dfs = new JMenuItem("DFS");
        a_ = new JMenuItem("A* Algo");
        exit = new JMenuItem("Exit");
        newB = new JMenuItem("New");

        menu.add(open);
        menu.add(algo);
        open.add(newB);
        open.add(exit);
        algo.add(dfs);
        algo.add(bfs);
        algo.add(a_);

        newB.addActionListener(this);
        exit.addActionListener(this);
        bfs.addActionListener(this);
        dfs.addActionListener(this);
        a_.addActionListener(this);

        JPanel gridPanel = createGridPanel(rows, cols); // 20x20 grid
        this.add(gridPanel, BorderLayout.CENTER);

        this.setIconImage(img.getImage());
        this.setResizable(false);
        this.setJMenuBar(menu);
        this.pack();
        this.setLocationRelativeTo(null);

    }

    private JPanel createGridPanel(int rows, int cols) {
        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(rows, cols));
        grid = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                Cell cell = new Cell();
                grid[i][j] = cell;
                panel.add(cell);
            }
        }
        return panel;
    }

    private class Cell extends JPanel {
        private int value = 0;

        public Cell() {
            this.setPreferredSize(new Dimension(30, 30));
            this.setBackground(Color.WHITE);
            this.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK));
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        toggle1();
                    } else if (e.getButton() == MouseEvent.BUTTON1) {
                        toggle2();
                    }
                }
            });

        }

        private void toggle1() {
            if (value != 1) {
                value = 1;
                setBackground(Color.BLACK);
            } else {
                value = 0;
                setBackground(Color.WHITE);
            }
        }

        private void toggle2() {
            if (currGreenCell != null && currRedCell != null) {
                currRedCell.reset();
                currGreenCell.reset();
                currGreenCell = null;
                currRedCell = null;

            } else {
                if (currGreenCell == null) {
                    currGreenCell = this;
                    this.value = 2;
                    this.setBackground(Color.green);
                } else {
                    currRedCell = this;
                    this.value = 3;
                    this.setBackground(Color.RED);
                }
            }
        }

        public void reset() {
            value = 0;
            setBackground(Color.WHITE);
        }

        public int getValue() {
            return value;
        }

    }

    public ArrayList<ArrayList<Integer>> board() {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            ArrayList<Integer> tmp = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                tmp.add(grid[i][j].getValue());
            }
            res.add(tmp);
        }
        return res;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == bfs) {
            System.out.println("BFS called");
            ArrayList<ArrayList<Integer>> board = board();
            Main.bfs(board, this);

        } else if (e.getSource() == dfs) {
            System.out.println("DFS Called");
        }
        if (e.getSource() == a_) {
            System.out.println("A* Called");
        }
        if (e.getSource() == newB) {
            this.dispose();
            new MyFrame();
        }
        if (e.getSource() == exit) {

            System.exit(0);
        }
    }

}
