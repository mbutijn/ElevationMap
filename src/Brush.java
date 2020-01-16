class Brush {
    int radius;
    private double [][] strength;
    double [][] updatedHeight = new double[61][61];

    Brush(int radius){
        this.radius = radius;
        strength = new double[2*radius+1][2*radius+1];
        for(int i = -radius; i < radius+1; i++) {
            for (int j = -radius; j < radius+1; j++) {
                if (i == 0 && j == 0){
                    strength[radius][radius] = 0.5;
                } else {
                    double distance = Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2));
                    if (distance < radius) {
                        strength[i+radius][j+radius] = 0.5 * (1.0 - distance / radius);
                    } else {
                        strength[i+radius][j+radius] = 0;
                    }
                }
            }
        }
    }

    void paint(int x, int y, boolean up, double endHeight, HeightPoint beginPoint, Gradient gradient, int toolSelector){
        for(int i = -radius; i < radius + 1; i++) {
            for (int j = -radius; j < radius + 1; j++) {
                int paintX = x + i;
                int paintY = y + j;

                if (paintX >= 0 && paintX <= ElevationMap.numX - 1 && paintY >= 0 && paintY <= ElevationMap.numY - 1) {
                    double startHeight = ElevationMap.heightPoints[x + i][y + j].height;

                    switch (toolSelector){
                        case 1: // shift tool
                            endHeight = up ? startHeight + 0.2 : startHeight - 0.2;
                            break;
                        case 2: // level tool
                            break;
                        case 3: // soften tool
                            if (x + i + 1 < ElevationMap.numX && y + j + 1 < ElevationMap.numY && x + i - 1 > 0 && y + j - 1 > 0) {
                                double underHeight = ElevationMap.heightPoints[x + i][y + j + 1].height;
                                double upperHeight = ElevationMap.heightPoints[x + i][y + j - 1].height;
                                double leftHeight = ElevationMap.heightPoints[x + i - 1][y + j].height;
                                double rightHeight = ElevationMap.heightPoints[x + i + 1][y + j].height;
                                double surrounding_average = (underHeight + upperHeight + leftHeight + rightHeight) / 4.0;
                                endHeight = startHeight + 4 * (surrounding_average - startHeight);
                            }
                            break;
                        case 4: // slope tool
                            double xDiff = paintX - beginPoint.x;
                            double yDiff = paintY - beginPoint.y;
                            double ratio = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2)) * Math.cos(gradient.direction - Math.atan2(yDiff, xDiff)) / gradient.distance;
                            ratio = ratio < 0 ? 0 : ratio > 1 ? 1 : ratio;
                            endHeight = beginPoint.height + ratio * gradient.heightDifference;
                            break;
                    }
                    updateHeightValue(startHeight, endHeight, i, j, x, y);
                }
            }
        }
    }

    private void updateHeightValue(double startHeight, double endHeight, int i, int j, int x, int y){
        double height = startHeight + strength[i+ radius][j+ radius]*(endHeight - startHeight);
        height = height < 0 ? 0 : height > 4.0 ? 4.0 : height;
        ElevationMap.heightPoints[x + i][y + j].height = height;

        updatedHeight[i + 30][j + 30] = height;
    }

}
