package mapmaker;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/**
 *
 * @author tucker
 */
public class UtilityButton extends JPanel implements MouseListener {
    //width of button
    private int width = 24;
    
    //height of button
    private int height = 24;
    
    //boolean to determine if button is being hovered over
    private boolean hover = false;
    
    //Color used in bg
    private Color bgCol = new Color(238, 238, 238);
    
    private UtilityType type;
    
    
    //Image for the button
    private BufferedImage img;
    
    public UtilityButton(String ref, UtilityType type) {
        setPreferredSize(new Dimension(width, height));
        addMouseListener(this);
        try {
            this.img = ImageIO.read(new File(ref));
        } catch(IOException e) {}
        this.type = type;
    }
    
    public void rectangleBehavior() {
        
    }
    
    
    /**
     * Paints the button onto the GUI
     * 
     * @param g Graphics context
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if(UtilityTable.selectedButton.equals(this)) {
            g2.setPaint(new GradientPaint(0, 0, bgCol, 0, getHeight(), Color.lightGray));
            g2.fillRoundRect(0, 0, width, height, 4,4);
            g2.setColor(Color.darkGray);
            g2.drawRoundRect(0, 0, width-1, height-1, 4,4);
        } else if(hover) {
            g2.setPaint(new GradientPaint(0, 0, Color.white, 0, getHeight(), bgCol));
            g2.fillRoundRect(0, 0, width, height, 4,4);
            g2.setColor(Color.lightGray);
            g2.drawRoundRect(0, 0, width-1, height-1, 4,4);
        } else {
            g2.setColor(bgCol);
            g2.fillRect(0, 0, width, height);
        }
        g2.drawImage(img, (width-img.getWidth())/2, (height-img.getHeight())/2, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(hover) {
            if(UtilityTable.selectedButton.equals(this)) {
                UtilityTable.selectedButton = UtilityTable.NILBUTTON;
            }
            else {
                UtilityTable.selectedButton = this;
            }
        }
        for(UtilityButton button : UtilityTable.buttons) {
            button.repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        hover = true;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        hover = false;
        repaint();
    }

    public UtilityType getType() {
        return type;
    }
    
    
}
