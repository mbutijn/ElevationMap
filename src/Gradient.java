import static java.lang.Math.atan2;

public class Gradient {
    HeightPoint target, begin;
    double heightDifference, distance, direction;
    private int xDiff, yDiff;

    Gradient(HeightPoint target, HeightPoint begin){
        this.target = target;
        this.begin = begin;
        this.heightDifference = target.height - begin.height;
        this.xDiff = target.x - begin.x;
        this.yDiff = target.y - begin.y;
        this.distance = Math.sqrt(Math.pow(xDiff,2) + Math.pow(yDiff,2));
        this.direction = atan2(yDiff, xDiff);
    }

    static void SetGradientPoint(HeightPoint point, int x, int y) {
        point.x = x;
        point.y = y;
        point.height = ElevationMap.heightPoints[x][y].height;
    }
}
