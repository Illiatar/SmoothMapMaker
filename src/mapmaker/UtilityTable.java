package mapmaker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author tucker
 */
public final class UtilityTable extends JPanel implements ActionListener {
    public final static UtilityButton NILBUTTON = new UtilityButton("", UtilityType.Nil);
    
    public static UtilityButton selectedButton = NILBUTTON;
    
    public static ArrayList<UtilityButton> buttons = new ArrayList<UtilityButton>();
    
    private static boolean anchor = true;
    
    private static JCheckBox anchorBox = new JCheckBox("Anchors", true);
    
    private Color bgColor = new Color(238,238,238);
    
    public UtilityTable() {
        anchorBox.addActionListener(this);
        setLayout(new WrapLayout(FlowLayout.LEFT));
        buttons.add(new UtilityButton("res/rect.png", UtilityType.Rectangle));
        selectedButton = buttons.get(buttons.size()-1);
        buttons.add(new UtilityButton("res/move.png", UtilityType.Move));
        buttons.add(new UtilityButton("res/resize.png", UtilityType.Resize));
        buttons.add(new UtilityButton("res/rotate.png", UtilityType.Rotate));
        buttons.add(new UtilityButton("res/info.png", UtilityType.Select));
        buttons.add(new UtilityButton("res/trash.png", UtilityType.Delete));
        
        for(UtilityButton button : buttons) {
            add(button);
        }
        add(anchorBox);
    }
    
    
    /**
     * Paints the table onto the GUI
     * 
     * @param g Graphics context
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public static boolean isAnchor() {
        return anchor;
    }

    public static void setAnchor(boolean anchor) {
        UtilityTable.anchor = anchor;
        if(anchor) anchorBox.setSelected(true);
        else anchorBox.setSelected(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(anchorBox)) {
            if(anchorBox.isSelected()) anchor = true;
            else anchor = false;
            GUI.getCurMap().repaint();
        }
    }
}
