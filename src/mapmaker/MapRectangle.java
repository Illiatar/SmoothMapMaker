package mapmaker;

import com.google.gson.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tucker
 */
public class MapRectangle implements MapElement, Saveable {
    private int x;
    
    private int y;
    
    private int width = 0;

    private int height = 0;
    
    private boolean selected = false;
    
    private int xOffset = 0;
    
    private int yOffset = 0;
    
    private Color boxColor = new Color(255, 255, 255, 128);
    
    private Rectangle2D.Double rectShape;
    
    private Rectangle2D.Double[] corners = new Rectangle2D.Double[2];
    
    private double rotation = Math.toRadians(0);
    
    private Area a;
    
    private Area[] cornerAreas = new Area[2];
    
    AffineTransform at;
    
    private Point anchor;
    
    private Rectangle2D.Double anchorRef;
        
    
    public MapRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        rectShape = new Rectangle2D.Double(x, y, width, height);
        anchor = new Point((int)rectShape.getCenterX(), (int)rectShape.getCenterY());
        corners[0] = new Rectangle2D.Double(0,0,0,0);
        corners[1] = new Rectangle2D.Double(0,0,0,0);
        cornerAreas[0] = new Area(corners[0]);
        cornerAreas[1] = new Area(corners[1]);
        anchorRef = rectShape;
        a = new Area(rectShape);
        a = a.createTransformedArea(AffineTransform.getRotateInstance(rotation));
        
    }
    
    @Override
    public void actionPressed(MouseEvent e) {
        UtilityType uType = UtilityTable.selectedButton.getType();
        switch(uType) {
            case Rectangle:
                rectAction(e, true);
                break;
            
            case Select:
                selectAction(e);
                break;
                
            case Delete:
                deleteAction(e);
                break;
                
            case Move:
                moveAction(e);
                break;
                
            case Rotate:
                rotateAction(e);
                break;
        }
    }
    
    @Override
    public void actionReleased(MouseEvent e) {
        UtilityType uType = UtilityTable.selectedButton.getType();
        switch(uType) {
            case Rectangle:
                rectAction(e, false);
                break;
        }
    }
    
    private void rectAction(MouseEvent e, boolean pressed) {
        //DRAGGED
        if(Map.isMouse1Down()) {
            if(!selected) {
                for(MapElement el : Map.getGrounds()) {
                    el.setSelected(false);
                }
                setSelected(true);
            }
            int rectWidth = Math.max(Math.min(e.getX(), Map.getMapWidth()-1), 0) - x;
            width = rectWidth;
            int rectHeight = Math.max(Math.min(e.getY(), Map.getMapHeight()-1), 0) - y;
            height = rectHeight;
            InformationTable.objectText.setText(getInfo());
            normalizeRect();
            setRects();
        }
        
        //MOUSE RELEASED
        if(!pressed) {
            Map.setCurElement(Map.getGrounds().size()-1);
            normalize();
            selectAction(e);
        }
    }
    
    private void selectAction(MouseEvent e) {
        if(!Map.isMouse1Down()) {
            InformationTable.objectText.setText(getInfo());
            for(MapRectangle el : Map.getGrounds()) {
                el.setSelected(false);
            }
            setSelected(true);
        }
    }
    
    private void deleteAction(MouseEvent e) {
        if(contains(e.getX(), e.getY())) {
            Map.getGrounds().remove(Map.getCurElement());
        }
    }
    
    private void moveAction(MouseEvent e) {
        selectAction(e);
            
        if(Map.isMouse1Down()) {
            x = e.getX() - xOffset;
            y = e.getY() - yOffset;
            setRects();
            boolean resetAnchor = true;
            for(Area r : Map.getHotspots()) {
                if(!cornerAreas[0].equals(r) 
                        && r.contains(cornerAreas[0].getBounds2D().getCenterX(), cornerAreas[0].getBounds2D().getCenterY())
                        && !anchorRef.equals(corners[0])) {
                    x = (int)r.getBounds2D().getCenterX();
                    y = (int)r.getBounds2D().getCenterY();
                    anchorRef = corners[0];
                    setRects();
                    resetAnchor = false;
                }
            }
            if(resetAnchor) anchorRef = rectShape;
            InformationTable.objectText.setText(getInfo());
            normalizeRect();
        }
    }
    
    private void rotateAction(MouseEvent e) {
        int originX = e.getX() - (int)anchor.getX();
        int originY = (e.getY() - (int)anchor.getY()) *-1;
        if(originX != 0) rotation = Math.atan((double)originY/(double)originX)*-1;
        rotation = Math.toDegrees(rotation);
        rotation = (int)Math.ceil(rotation/5) * 5;
        rotation = Math.toRadians(rotation);
        normalizeRect();
        InformationTable.objectText.setText(getInfo());
    }

    @Override
    public void render(Graphics2D g2) {
        g2.rotate(rotation, anchor.getX(), anchor.getY());
        g2.setColor(boxColor);
        g2.fill(rectShape);
        if(selected) g2.setColor(Color.blue);
        else g2.setColor(Color.black);
        g2.draw(rectShape);
        g2.rotate(-rotation, anchor.getX(), anchor.getY());
        g2.draw(a);
        for(int i = 0; i < cornerAreas.length; i++) {
            if(UtilityTable.isAnchor()) g2.fill(cornerAreas[i]);
        }
        
    }
    
    @Override
    public void normalize() {
        corners[0] = new Rectangle2D.Double(x - 5, y - 5, 10, 10);
        corners[1] = new Rectangle2D.Double(x + width - 5, y - 5, 10, 10);
        if(width < 0) {
            x += width;
            width *= -1;
        }
        if(height < 0) {
            y += height;
            height *= -1;
        }
    }
    
    private void normalizeRect() {
        int rectX = x;
        int rectY = y;
        int rectWidth = width;
        int rectHeight = height;
        if(width < 0) {
            rectX += width;
            rectWidth *= -1;
        }
        if(height < 0) {
            rectY += height;
            rectHeight *= -1;
        }
        rectShape.setRect(rectX, rectY, rectWidth, rectHeight);
        a = new Area(rectShape);
        cornerAreas[0] = new Area(corners[0]);
        cornerAreas[1] = new Area(corners[1]);
        
        at = new AffineTransform();
        at.rotate(rotation, anchor.getX(), anchor.getY());
        a.transform(at);
        cornerAreas[0].transform(at);
        cornerAreas[1].transform(at);
        corners[0].setRect(x - 5, y - 5, 10, 10);
        corners[1].setRect(x + width - 5, y - 5, 10, 10);
        anchor.setLocation(anchorRef.getCenterX(), anchorRef.getCenterY());
        
    }
    
    @Override
    public boolean contains(int mouseX, int mouseY) {
        //return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
        return a.intersects(mouseX, mouseY, 1, 1);
    }
    
    public boolean cornerContains(int mouseX, int mouseY) {
        boolean corner = false;
        if(corners[0].contains(mouseX, mouseY) || corners[1].contains(mouseX, mouseY)) corner = true;
        return corner;
    }
    
    private void setRects() {
        corners[0].setRect(x - 5, y - 5, 10, 10);
        corners[1].setRect(x + width - 5, y - 5, 10, 10);
        at = new AffineTransform();
        at.rotate(rotation, anchor.getX(), anchor.getY());
        a.transform(at);
        cornerAreas[0].transform(at);
        cornerAreas[1].transform(at);
    }
    
    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }
    
    @Override
    public String getInfo() {
        int rectX = x;
        int rectY = y;
        int rectWidth = width;
        int rectHeight = height;
        if(width < 0) {
            rectX += width;
            rectWidth *= -1;
        }
        if(height < 0) {
            rectY += height;
            rectHeight *= -1;
        }
        return "Rectangle: x[" + rectX + "], y[" + rectY + "], width[" + rectWidth + "], height[" + rectHeight + "], angle[" + Math.round(Math.toDegrees(rotation)) + "]";
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        if(selected) boxColor = new Color(0,0,255,128);
        else boxColor = new Color(255,255,255,128);
        this.selected = selected;
    }

    @Override
    public int getxOffset() {
        return xOffset;
    }

    @Override
    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    @Override
    public int getyOffset() {
        return yOffset;
    }

    @Override
    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public Area[] getCornerAreas() {
        return cornerAreas;
    }

    @Override
    public JsonElement pack() {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("x", x);
        jsonObj.addProperty("y", y);
        jsonObj.addProperty("width", width);
        jsonObj.addProperty("height", height);
        jsonObj.addProperty("angle", rotation);
        return jsonObj;
    }
    
}
