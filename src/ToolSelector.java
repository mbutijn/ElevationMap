import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ToolSelector {
    private int index;
    private String name;
    private boolean checked;
    private static ButtonGroup buttonGroup = new ButtonGroup();

    ToolSelector(int index, String name, boolean checked){
        this.index = index;
        this.name = name;
        this.checked = checked;
    }

    void MakeButton(JPanel panel){
        JRadioButton radioButton = new JRadioButton(name, checked);
        panel.add(radioButton);
        radioButton.addActionListener(new Selector());
        buttonGroup.add(radioButton);
    }

    private class Selector implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ElevationMap.toolSelected = index;
        }
    }
}
