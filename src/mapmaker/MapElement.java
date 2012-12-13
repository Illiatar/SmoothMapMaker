package mapmaker;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 *
 * @author tucker
 */
public interface MapElement {
    public void render(Graphics2D g2);
    
    
    public int getHeight();

    public void setHeight(int height);

    public int getWidth();

    public void setWidth(int width);

    public int getX();

    public void setX(int x);

    public int getY();

    public void setY(int y);
    
    public boolean isSelected();
    
    public void setSelected(boolean selected);
    
    public boolean contains(int mouseX, int mouseY);
    
    public void normalize();
    
    public void actionPressed(MouseEvent e);
    
    public void actionReleased(MouseEvent e);
    
    public String getInfo();
    
    public int getxOffset();

    public void setxOffset(int offsetX);

    public int getyOffset();

    public void setyOffset(int offsetY);
}
