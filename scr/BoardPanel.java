package scr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class BoardPanel extends JPanel {
    private GameEngine engine;
    private AI ai;
    private int cellSize = 40;
    private int offsetX = 0, offsetY = 0;
    private Point lastMove = null;
    private List<Point> winningLine = new ArrayList<>();

    public BoardPanel(GameEngine engine, AI ai) {
        this.engine = engine;
        this.ai = ai;
        setPreferredSize(new Dimension(800, 800));

        // Mouse click
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
                requestFocusInWindow(); // Đảm bảo panel nhận bàn phím
            }
        });

        // Key listener chuẩn
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_W:
                        offsetY--;
                        break;
                    case KeyEvent.VK_S:
                        offsetY++;
                        break;
                    case KeyEvent.VK_A:
                        offsetX--;
                        break;
                    case KeyEvent.VK_D:
                        offsetX++;
                        break;
                }
                repaint();
            }
        });
    }

    private void handleClick(int mouseX, int mouseY) {
        int col = mouseX / cellSize + offsetX;
        int row = mouseY / cellSize + offsetY;
        Point p = new Point(col, row);

        if (engine.makeMove(p)) {
            lastMove = p;
            ((GameFrame) SwingUtilities.getWindowAncestor(this)).recordMove(p);

            // Đồng bộ AI
            ai.setCell(row, col, 1); // Người là 1
            repaint();

            if (engine.checkWin(p)) {
                winningLine = engine.getWinningLine(p);
                animateWin();
                return;
            }

            engine.switchTurn();

            // Lượt máy
            if (engine.getCurrentPlayer().isAI() && ai != null) {
                Timer timer = new Timer(300, ev -> {
                    int[] best = ai.getBestMove();
                    if (best != null) {
                        Point aiMove = new Point(best[1], best[0]); // [x,y]
                        engine.makeMove(aiMove);
                        ai.setCell(best[0], best[1], -1); // Máy là -1
                        lastMove = aiMove;
                        ((GameFrame) SwingUtilities.getWindowAncestor(this)).recordMove(aiMove);
                        repaint();

                        if (engine.checkWin(aiMove)) {
                            winningLine = engine.getWinningLine(aiMove);
                            animateWin();
                            return;
                        }
                        engine.switchTurn();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

    private void animateWin() {
        Timer timer = new Timer(300, null);
        final int[] blinkCount = {0};
        timer.addActionListener(e -> {
            blinkCount[0]++;
            repaint();
            if (blinkCount[0] >= 6) {
                timer.stop();
                JOptionPane.showMessageDialog(this,
                        engine.getCurrentPlayer().getName() + " thắng!");
                System.exit(0);
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int viewRows = getHeight() / cellSize;
        int viewCols = getWidth() / cellSize;

        // Vẽ lưới
        for (int i = 0; i <= viewCols; i++)
            g.drawLine(i * cellSize, 0, i * cellSize, viewRows * cellSize);
        for (int i = 0; i <= viewRows; i++)
            g.drawLine(0, i * cellSize, viewCols * cellSize, i * cellSize);

        // Vẽ các ô đã đánh
        for (Point p : engine.getBoard().keySet()) {
            int screenX = (p.x - offsetX) * cellSize;
            int screenY = (p.y - offsetY) * cellSize;
            if (screenX < 0 || screenY < 0 || screenX > getWidth() || screenY > getHeight())
                continue;

            if (engine.getBoard().get(p) == GameEngine.Cell.X) {
                g.drawLine(screenX + 5, screenY + 5, screenX + cellSize - 5, screenY + cellSize - 5);
                g.drawLine(screenX + cellSize - 5, screenY + 5, screenX + 5, screenY + cellSize - 5);
            } else if (engine.getBoard().get(p) == GameEngine.Cell.O) {
                g.drawOval(screenX + 5, screenY + 5, cellSize - 10, cellSize - 10);
            }
        }

        // Highlight nước đi cuối
        if (lastMove != null) {
            g.setColor(Color.GREEN);
            g.drawRect((lastMove.x - offsetX) * cellSize, (lastMove.y - offsetY) * cellSize, cellSize, cellSize);
        }

        // Highlight 5 ô thắng
        g.setColor(Color.RED);
        for (Point p : winningLine) {
            g.drawRect((p.x - offsetX) * cellSize, (p.y - offsetY) * cellSize, cellSize, cellSize);
        }
    }
}
