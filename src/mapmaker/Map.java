package mapmaker;

import com.google.gson.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;


/**
 * Map that is being edited
 * 
 */
public final class Map extends JPanel implements MouseMotionListener, MouseListener, Saveable {
    //width of map
    private static int width;
    
    //height of map
    private static int height;
    
    //x position of mouse
    private static int mouseX;
    
    //y position of mouse
    private static int mouseY;
    
    private static Stack<MapRectangle> grounds = new Stack<MapRectangle>();
    
    private static boolean mouse1Down = false;
    
    private static int curElement;
    
    private static ArrayList<Area> hotspots = new ArrayList<Area>();
    
    
    /**
     * Creates a new mFFap for editing
     * 
     * @param width Width of the map
     * @param height Height of the map
     */
    public Map(int width, int height, JFrame frame) {
        grounds.clear();
        curElement = 0;
        addMouseMotionListener(this);
        addMouseListener(this);
        Map.width = width;
        Map.height = height;
        setSize(width, height);
        GUI.getSaveItem().setEnabled(true);
    }
    
    /**
     * Paints the map onto the GUI
     * 
     * @param g Graphics context
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.lightGray);
        g2.fillRect(0, 0, width, height);
        for(MapRectangle ground : grounds) {
            ground.render(g2);
        }
    }
    
    /**
     * Prints a String representation of a Map
     * 
     * @return Description of map
     */
    @Override
    public String toString() {
        return "Width: " + width + ", Height: " + height;
    }
    
    /**
     * Returns the width of the map
     * 
     * @return width
     */
    public static int getMapWidth() {
        return width;
    }
    
    /**
     * Returns the height of the map
     * 
     * @return height
     */
    public static int getMapHeight() {
        return height;
    }

    /**
     * Sets the mouse coordinates
     * 
     * @param e MouseEvent context
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(!grounds.isEmpty() && UtilityTable.selectedButton.getType() == UtilityType.Rectangle) grounds.peek().actionPressed(e);
        else if(!grounds.isEmpty()) {
            try {
                grounds.get(curElement).actionPressed(e);
            } catch(ArrayIndexOutOfBoundsException ex) {
                
            }
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressAction(e);
        if(contains()) {
            if(UtilityTable.selectedButton.getType() == UtilityType.Rectangle 
                    && e.getButton() == MouseEvent.BUTTON1 ) {
                grounds.push(new MapRectangle(Map.getMouseX(), Map.getMouseY(), 5, 5));
            }
            if(!grounds.isEmpty()) {
                grounds.get(curElement).setxOffset(mouseX - grounds.get(curElement).getX());
                grounds.get(curElement).setyOffset(mouseY - grounds.get(curElement).getY());
                grounds.get(curElement).actionPressed(e);
            }
            if(e.getButton() == MouseEvent.BUTTON1) {
                mouse1Down = true;
            }
        }
        repaint();
    }
    
    public boolean contains() {
        return mouseX >= getX() && mouseY >= getY() && mouseX <= (getX() + getWidth()) && mouseY <= (getY() + getHeight());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!grounds.isEmpty()) grounds.peek().actionReleased(e);
        if(e.getButton() == MouseEvent.BUTTON1) {
            mouse1Down = false;
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    private void mousePressAction(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        for(int i = 0; i < grounds.size(); i++) {
            if(grounds.get(grounds.size()-1-i).contains(mouseX, mouseY) ) {
                curElement = grounds.size()-1-i;
                i = grounds.size();
            }
        }
    }
    
    public static Stack<MapRectangle> getGrounds() {
        return grounds;
    }

    public static boolean isMouse1Down() {
        return mouse1Down;
    }

    public static int getMouseX() {
        return mouseX;
    }

    public static int getMouseY() {
        return mouseY;
    }

    public static int getCurElement() {
        return curElement;
    }

    public static void setCurElement(int curElement) {
        Map.curElement = curElement;
    }

    public static ArrayList<Area> getHotspots() {
        hotspots.clear();
        for(MapRectangle e : grounds) {
            hotspots.add(e.getCornerAreas()[0]);
            hotspots.add(e.getCornerAreas()[1]);
        }
        return hotspots;
    }

    @Override
    public JsonElement pack() {
        JsonArray jArray = new JsonArray();
        for(MapRectangle g : grounds) {
            jArray.add(g.pack());
        }
        return jArray;
    }
}
