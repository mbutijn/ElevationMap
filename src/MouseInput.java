import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MouseInput {
    private JFrame frame;
    private boolean beginToSet = true;
    boolean upShift = true;

    MouseInput(JFrame frame){
        this.frame = frame;

        frame.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent event) {
                if (event.getButton() == 3){ // right mouse click
                    if (ElevationMap.toolSelected == 1){ // When Shift tool
                        upShift = false;
                        ElevationMap.timer.start();
                    } else if (ElevationMap.toolSelected == 2 || ElevationMap.toolSelected == 4) { // Only when level tool or slope tool
                        Gradient.SetGradientPoint(ElevationMap.target, getMouseX()- ElevationMap.padding, getMouseY() - ElevationMap.padding);
                    }
                } else if (event.getButton() == 1){ // left mouse click
                    if (ElevationMap.toolSelected == 1) { // When Shift tool
                        upShift = true;
                        ElevationMap.timer.start();
                    } else if (ElevationMap.toolSelected == 3){
                        ElevationMap.timer.start();
                    } else if (ElevationMap.toolSelected == 2 || ElevationMap.toolSelected == 4) { // Only when level tool or slope tool
                        if (beginToSet) {
                            Gradient.SetGradientPoint(ElevationMap.begin, getMouseX() - ElevationMap.padding, getMouseY() - ElevationMap.padding);
                            beginToSet = false;
                            ElevationMap.MakeNewGradient();
                            ElevationMap.timer.start();
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                ElevationMap.timer.stop();
                beginToSet = true;
            }
        });
    }

    int getMouseX(){
        return (int) getPoint().getX() - frame.getX();
    }

    int getMouseY(){
        return (int) getPoint().getY() - frame.getY() - 32;
    }

    private Point getPoint(){
        return MouseInfo.getPointerInfo().getLocation();
    }
}
