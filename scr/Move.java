package scr;

import java.awt.Point;

public class Move {
    public Point point;  // Vị trí nước đi
    public boolean isAI; // true nếu là AI, false nếu là người

    public Move(Point point, boolean isAI){
        this.point = point;
        this.isAI = isAI;
    }
}
