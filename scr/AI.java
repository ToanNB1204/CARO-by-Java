package scr;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class AI {
    private int Size;
    private int countToWin;
    private int[][] board; // 0 = trống, 1 = người, -1 = AI

    public AI(int size, int countToWin) {
        this.Size = size;
        this.countToWin = countToWin;
        board = new int[size][size];
    }

    public void setCell(int i, int j, int val) {
        board[i][j] = val;
    }

    // Kiểm tra có quân lân cận
    private boolean hasNeighbors(int i, int j) {
        for (int dx = -1; dx <= 1; dx++)
            for (int dy = -1; dy <= 1; dy++)
                if (dx != 0 || dy != 0) {
                    int ni = i + dx, nj = j + dy;
                    if (ni >= 0 && nj >= 0 && ni < Size && nj < Size && board[ni][nj] != 0) return true;
                }
        return false;
    }

    // Tính điểm cho một ô trống
    private int evaluateCell(int i, int j) {
        if (board[i][j] != 0) return -1; // không phải ô trống
        if (!hasNeighbors(i, j)) return -1; // quá xa không cần xét

        int score = 0;
        int[][] dirs = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

        for (int[] d : dirs) {
            int aCount = countInDirection(i, j, d[0], d[1], -1) + countInDirection(i, j, -d[0], -d[1], -1) + 1; // AI
            int uCount = countInDirection(i, j, d[0], d[1], 1) + countInDirection(i, j, -d[0], -d[1], 1) + 1; // Người

            // Điểm tấn công
            if (aCount >= countToWin) return 100000; // thắng ngay
            if (aCount == 4) score += 10000;
            else if (aCount == 3) score += 1000;
            else if (aCount == 2) score += 100;

            // Điểm phòng thủ
            if (uCount >= countToWin) return 90000; // chặn ngay
            if (uCount == 4) score += 9000;
            else if (uCount == 3) score += 900;
            else if (uCount == 2) score += 90;
        }
        return score;
    }

    // Đếm số quân liên tiếp theo hướng dx,dy
    private int countInDirection(int x, int y, int dx, int dy, int player) {
        int count = 0;
        int nx = x + dx, ny = y + dy;
        while (nx >= 0 && ny >= 0 && nx < Size && ny < Size && board[nx][ny] == player) {
            count++;
            nx += dx;
            ny += dy;
        }
        return count;
    }

    // Lấy nước đi tốt nhất
    public int[] getBestMove() {
        int maxScore = -1;
        Point bestMove = null;
        for (int i = 0; i < Size; i++) {
            for (int j = 0; j < Size; j++) {
                int score = evaluateCell(i, j);
                if (score > maxScore) {
                    maxScore = score;
                    bestMove = new Point(i, j);
                }
            }
        }

        if (bestMove == null) return new int[]{Size / 2, Size / 2}; // fallback
        return new int[]{bestMove.x, bestMove.y};
    }

    public void printBoard() {
        for (int i = 0; i < Size; i++) {
            for (int j = 0; j < Size; j++) {
                System.out.printf("%2d ", board[i][j]);
            }
            System.out.println();
        }
    }
}
