package mapmaker;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

/**
 *
 * @author tucker
 */
public final class InformationTable extends JPanel{
    public static JTextArea objectText = new JTextArea("Nothing Selected");
    
    public InformationTable() {
        setLayout(new GridLayout(1,1));
        objectText.setEditable(false);
        objectText.setBackground(new Color(225,225,225));
        add(objectText);
    }
    
    
    /**
     * Paints the table onto the GUI
     * 
     * @param g Graphics context
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //g2.setColor(Color.blue);
        //g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
