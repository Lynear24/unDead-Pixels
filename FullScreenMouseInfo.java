import greenfoot.Actor;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.util.List;

/**
 * This class works like a greenfoot.MouseInfo object. The coordinates are relative to the world size so you don't have to change the values you already got.
 * This class can be used the same way like greenfoot.MouseInfo objects without any changes.
 * 
 * @author Gevater_Tod4711
 * @version 1.1
 */
public class FullScreenMouseInfo
{
    private MouseEvent mouseEvent;
    
    private Point mousePoint;
    
    private FullScreenWindow callingFrame;
    
    public FullScreenMouseInfo(MouseEvent mouseEvent) throws IllegalArgumentException {
        this(mouseEvent, null);
    }
    public FullScreenMouseInfo(MouseEvent mouseEvent, FullScreenWindow callingFrame) throws IllegalArgumentException {
        if (mouseEvent == null) {
            throw new IllegalArgumentException("The MouseEvent used for the FullScreenMouseInfo mussn't be null.");
        }
        this.mouseEvent = mouseEvent;
        this.callingFrame = callingFrame;
        mousePoint = mouseEvent.getLocationOnScreen();
    }
    
    /**
     * Return the actor (if any) that the current mouse behaviour is related to.
     * 
     * @return
     *      The actor this mouse behaviour is related to or null if there is no such actor.
     */
    public Actor getActor() {
        List<Actor> actors = FullScreenWindow.getDisplayedWorld().getObjectsAt(getX(), getY(), Actor.class);
        if (actors != null) {
            return actors.get(0);
        }
        return null;
    }
    
    /**
     * The number of the pressed or clicked button (if any).
     * 
     * @return
     *      The button number. Usually 1 is the left button, 2 is the middle button and 3 is the right button.
     */
    public int getButton() {
        if (mouseEvent != null) {
            return mouseEvent.getButton();
        }
        return -1;
    }
    
    /**
     * Return the number of mouse clicks associated with this mouse event.
     * 
     * @return
     *      The number of times a button has been clicked.
     */
    public int getClickCount() {
        if (mouseEvent != null) {
            return mouseEvent.getClickCount();
        }
        return -1;
    }
    
    /**
     * Return the current x position of the mouse cursor in the world.
     * The returned value is relative to the worlds size and not to the screen size so this method can be used like in a normal greenfoot scenario.
     * 
     * @return
     *      The x position in the world.
     */
    public int getX() {
        if (callingFrame != null && callingFrame.getDisplayedWorld() != null) {
            if (callingFrame.getDisplayedWorld().isResolutionKept()) {
                int mouseX = (int) (((mousePoint.getX() - FullScreenWindow.MOUSE_OFFSET_X) * FullScreenWorld.WORLD_WIDTH / FullScreenWindow.IMAGE_WIDTH) / callingFrame.getDisplayedWorld().getCellSize());
                if (mouseX < 0) {
                    return 0;
                }
                else if (mouseX >= FullScreenWorld.WORLD_WIDTH) {
                    return FullScreenWorld.WORLD_WIDTH-1;
                }
                else {
                    return mouseX;
                }
            }
            else {
                return (int) ((mousePoint.getX() * FullScreenWorld.WORLD_WIDTH / FullScreenWindow.FRAME_WIDTH) / callingFrame.getDisplayedWorld().getCellSize());
            }
        }
        else {
            return (int) (mousePoint.getX() * FullScreenWorld.WORLD_WIDTH / FullScreenWindow.FRAME_WIDTH);
        }
    }
    
    /**
     * Return the current y position of the mouse cursor in the world.
     * The returned value is relative to the worlds size and not to the screen size so this method can be used like in a normal greenfoot scenario.
     * 
     * @return
     *      The y position in the world.
     */
    public int getY() {
        if (callingFrame != null && callingFrame.getDisplayedWorld() != null) {
            if (callingFrame.getDisplayedWorld().isResolutionKept()) {
                int mouseY = (int) (((mousePoint.getY() - FullScreenWindow.MOUSE_OFFSET_Y) * FullScreenWorld.WORLD_HEIGHT / FullScreenWindow.IMAGE_HEIGHT) / callingFrame.getDisplayedWorld().getCellSize());
                if (mouseY < 0) {
                    return 0;
                }
                else if (mouseY >= FullScreenWorld.WORLD_WIDTH) {
                    return FullScreenWorld.WORLD_WIDTH-1;
                }
                else {
                    return mouseY;
                }
            }
            else {
                return (int) ((mousePoint.getY() * FullScreenWorld.WORLD_HEIGHT / FullScreenWindow.FRAME_HEIGHT) / callingFrame.getDisplayedWorld().getCellSize());
            }
        }
        else {
            return (int) (mousePoint.getY() * FullScreenWorld.WORLD_HEIGHT / FullScreenWindow.FRAME_HEIGHT);
        }
    }
    
    /**
     * Return the current x position of the mouse cursor on the screen.
     * The returned value is relative to the screensize and not to the worlds size.
     * 
     * @return
     *      The x position on the screen.
     */
    public int getXOnScreen() {
        return (int) mousePoint.getX();
    }
    
    /**
     * Return the current y position of the mouse cursor on the screen.
     * The returned value is relative to the screensize and not to the worlds size.
     * 
     * @return
     *      The y position on the screen.
     */
    public int getYOnScreen() {
        return (int) mousePoint.getY();
    }
    
    /**
     * Return the current mouse point consisting of the x and y coordinates of the mouse.
     * The returned value is relative to the worlds size and not to the screen size so this method can be used like in a normal greenfoot scenario.
     * 
     * @return
     *      The current mouse point.
     */
    public Point getMousePoint() {
        return new Point(getX(), getY());
    }
    
    /**
     * The representing String for this FullScreenMouseInfo object.
     */
    public String toString() {
        return "FullScreenMouseInfo: (" + getX() + "|" + getY() + ") \t" + mouseEvent.toString();
    }
}