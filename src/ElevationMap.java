import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class ElevationMap extends JFrame {
    static Timer timer;
    private static final int screenHeight = 800, screenWidth = 900;
    static final int numX = 600, numY = 600;
    static final int padding = 100;
    private MouseInput mouseInput;
    private Map map = new Map();
    static HeightPoint [][] heightPoints = new HeightPoint[numX][numY];
    static HeightPoint target = new HeightPoint(0,0, 0);
    static HeightPoint begin = new HeightPoint(0,0,0);
    private static Gradient gradient;
    static int toolSelected = 1;
    private int lowerPaintX, lowerPaintY;
    private Brush brush = new Brush(30);
    boolean fullRepaint = true;
    private TextField waves_X = new TextField("1");
    private TextField waves_Y = new TextField("1");

    public static void main(String[] args) {
        new ElevationMap().makeUI();
    }

    private ElevationMap(){
        super("Terrain map");
        this.frameInit();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(screenWidth, screenHeight);
        setResizable(false);
    }

    private void makeUI(){

        getContentPane().add(BorderLayout.CENTER, map);
        mouseInput = new MouseInput(this);

        JPanel eastPanel = new JPanel();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6,1));

        JPanel input = new JPanel();
        input.setLayout(new GridLayout(2,2));
        input.add(new Label("X waves:"));
        input.add(waves_X);
        input.add(new Label("Y waves:"));
        input.add(waves_Y);
        panel.add(input);

        ResetButton button = new ResetButton(this);
        button.makeButton(panel);

        MakeTerain();

        ToolSelector shift = new ToolSelector(1, "shift", true);
        ToolSelector level = new ToolSelector(2, "level", false);
        ToolSelector soften = new ToolSelector(3, "soften", false);
        ToolSelector slope = new ToolSelector(4, "slope", false);

        ToolSelector[] toolSelectors = new ToolSelector[]{shift, level, soften, slope};

        for (ToolSelector ts : toolSelectors){{
            ts.MakeButton(panel);
        }}

        eastPanel.add(panel);

        getContentPane().add(BorderLayout.EAST, eastPanel);

        timer = new Timer(50, update);
        timer.setRepeats(true);

        addWindowStateListener(arg0 -> frame__windowStateChanged(arg0));
    }

    private void frame__windowStateChanged(WindowEvent arg0) {
        fullRepaint = true;
        repaint();
    }

    void MakeTerain(){
        double waves_x = Double.parseDouble(waves_X.getText());
        double waves_y = Double.parseDouble(waves_Y.getText());
        for (int j = 0; j < numY; j++){
            for (int i = 0; i < numX; i++){
                double x = ((i * waves_x * 2 * Math.PI) / (double) numX);
                double y = ((j * waves_y * 2 * Math.PI) / (double) numY);
                double height = 2 + Math.sin(x) + Math.sin(y);
                heightPoints[i][j] = new HeightPoint(i, j, height);
            }
        }
    }

    private ActionListener update = new ActionListener() {
        public void actionPerformed(ActionEvent evt) { // runs every 50 milliseconds
            int x = mouseInput.getMouseX() - padding;
            int y = mouseInput.getMouseY() - padding;

            if (x > 0 && y > 0 && x < numX && y < numY) {

                brush.paint(x, y, mouseInput.upShift, target.height, begin, gradient, toolSelected);

                lowerPaintX = x - brush.radius + 3;
                lowerPaintY = y - brush.radius + 2;

                fullRepaint = false;
                repaint(lowerPaintX + padding, lowerPaintY + padding + brush.radius, 2 * brush.radius + 1, 2 * brush.radius + 1);
            }
        }
    };

    static void MakeNewGradient(){
        gradient = new Gradient(target, begin);
    }

    private class Map extends JPanel {
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D graphics2d = (Graphics2D) graphics;
            graphics2d.drawRect(padding - 1, padding - 1, numX + 1, numY + 1);

            if (fullRepaint) { // Make the full height map for the first time
                for (int j = 0; j < numY; j++) {
                    for (int i = 0; i < numX; i++) {
                        double height = heightPoints[i][j].height;

                        int red = height > 2.0 ? 180 : (int) (90.0 * height);
                        int green = height < 2.0 ? 180 : (int) (90.0 * (4.0 - height));

                        graphics.setColor(new Color(red, green, 0));
                        graphics.fillRect(padding + i, padding + j, 1, 1);
                    }
                }
            } else { // Make the portion of the height map where the mouse is
                for (int j = 0; j <= 2 * brush.radius; j++) {
                    for (int i = 0; i <= 2 * brush.radius; i++) {
                        int x = lowerPaintX + i - 3;
                        int y = lowerPaintY + j - 2;

                        if (x > - 1 && x <= numX - 1 && y > - 1 && y <= numY - 1) {
                            double height = brush.updatedHeight[i][j];

                            int red = height > 2.0 ? 180 : (int) (90.0 * height);
                            int green = height < 2.0 ? 180 : (int) (90.0 * (4.0 - height));

                            graphics.setColor(new Color(red, green, 0));
                            graphics.fillRect(x + padding, y + padding, 1, 1);
                        }
                    }
                }
            }
        }
    }
}
