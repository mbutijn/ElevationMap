import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResetButton {
    ElevationMap map;

    ResetButton(ElevationMap map){
        this.map = map;
    }

    public void makeButton(JPanel panel){
        JButton button = new JButton("Reset terrain");
        button.addActionListener(new Reset());

        panel.add(button);
    }

    private class Reset implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionListener){
            map.MakeTerain();
            map.fullRepaint = true;
            map.repaint();
        }
    }

}
