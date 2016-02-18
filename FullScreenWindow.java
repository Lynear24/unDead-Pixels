import greenfoot.Actor;
import greenfoot.World;
import greenfoot.Greenfoot;
import greenfoot.core.WorldHandler;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

/**
 * This class is used to create the fullscreen frame which displays the world image.
 * The image that is send to this frame is passed on to the FullScreenBufferPanel where the image is printed.
 * 
 * Mouse and key listeners are included into this frame.
 * Because the greenfoot frame is not the active frame when you playing in fullscreen mode the methods Greenfoot.isKeyDown(String), Greenfoot.getMouseInfo(), (...) will not work.
 * Instead you will have to use FullScreenWindow.isKeyDown(String) or FullScreenWindow.getMouseInfo() which work the same way like the greenfoot methods.
 * 
 * All changed methods are:
 *  - Greenfoot.isKeyDown(String)
 *  - Greenfoot.getKey()
 *  - Greenfoot.getMouseInfo()
 *  - Greenfoot.mouseClicked(Object obj)
 *  - Greenfoot.mouseDragged(Object obj)
 *  - Greenfoot.mouseDragEnded(Object obj)
 *  - Greenfoot.mouseMoved(Object obj)
 *  - Greenfoot.mousePressed(Object obj)
 *  
 * All these methods need to be changed to FullScreenWindow.methodname to make the fullscreen mode work.
 * These changes can be done by my Greenfoot Full Screen Rewriter scenario which does all the changes with only a fiew mouse clicks.
 * 
 * 
 * @author Gevater_Tod4711
 * @version 1.1
 */ 
public class FullScreenWindow extends JFrame {
    
    public static int FRAME_WIDTH;
    public static int FRAME_HEIGHT;
    
    public static int MOUSE_OFFSET_X;
    public static int MOUSE_OFFSET_Y;
    public static int IMAGE_WIDTH;
    public static int IMAGE_HEIGHT;
    
    private int mouseClickCounter = 0;
    
    private static boolean isMouseClicked;
    private static boolean isMouseDragged;
    private static boolean isMouseDragEnded;
    private static boolean isMousePressed;
    
    private static FullScreenWorld displayedWorld;
    
    private static FullScreenWindow selfReference;
    
    private static List<FullScreenKeyInfo> keyInfo;
    
    private static JButton minimizButton = new JButton("Minimize");
    private static JButton closeButton = new JButton("Close");
    
    private static MouseEvent lastMouseEvent;
    
    private static FullScreenMouseInfo lastMouseInfo;
    private static FullScreenMouseInfo mouseInfo;
    
    private static FullScreenBufferPanel panel;
    
    private static Thread windowControllThread;
    
    /**
     * Create a new FullScreenWindow with reference to the calling World object.
     */
    public FullScreenWindow(FullScreenWorld world) {
        this.displayedWorld = world;
        selfReference = this;
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        FRAME_WIDTH = (int) screenSize.getWidth();
        FRAME_HEIGHT = (int) screenSize.getHeight();
        double scaleFactor = Math.min(screenSize.getWidth()/FullScreenWorld.WORLD_WIDTH, screenSize.getHeight()/FullScreenWorld.WORLD_HEIGHT);
        MOUSE_OFFSET_X = (int) ((screenSize.getWidth() - (scaleFactor * FullScreenWorld.WORLD_WIDTH)) / 2);
        MOUSE_OFFSET_Y = (int) ((screenSize.getHeight() - (scaleFactor * FullScreenWorld.WORLD_HEIGHT)) / 2);
        IMAGE_WIDTH = (int) (scaleFactor * FullScreenWorld.WORLD_WIDTH);
        IMAGE_HEIGHT = (int) (scaleFactor * FullScreenWorld.WORLD_HEIGHT);
        keyInfo = new java.util.ArrayList<FullScreenKeyInfo>();
        setUndecorated(true);
        setVisible(true);
        minimizButton.setBounds(FRAME_WIDTH - 170, 0, 91, 25);
        minimizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                setState(ICONIFIED);
            }
        });
        closeButton.setBounds(FRAME_WIDTH - 79, 0, 79, 25);
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        startWindowControllThread();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        addKeyListener(new GreenfootKeyListener(this));
        addMouseListener(new GreenfootMouseListener(this));
        addMouseMotionListener(new GreenfootMouseListener(this));
        panel = new FullScreenBufferPanel();
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel.setLayout(null);
        setContentPane(panel);
        if (getGraphicsConfiguration().getDevice().isFullScreenSupported()) {
            getGraphicsConfiguration().getDevice().setFullScreenWindow(this);
        }
        if (!GreenfootToolkit.getToolkit().playingOnline()) {
            minimizButton.setIcon(new ImageIcon(FullScreenWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/minimize.gif")));
            closeButton.setIcon(new ImageIcon(FullScreenWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/close.gif")));
            for (Window window : getWindows()) {
                if (!(window instanceof greenfoot.gui.GreenfootFrame) && !window.equals(this)) {
                    window.dispose();
                }
            }
        }
    }
    
    private void startWindowControllThread() {
        class WindowControllThread implements Runnable {
            
            private Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            
            private FullScreenMouseInfo mouse;
            
            public void run() {
                while (true) {
                    mouse = FullScreenWindow.getMouseInfo();
                    if (mouse != null) {
                        if (mouse.getXOnScreen() > screenSize.getWidth() - 175 && mouse.getYOnScreen() < 25) {
                            panel.add(minimizButton);
                            panel.add(closeButton);
                            try {
                                Thread.currentThread().sleep(1000);
                            }
                            catch (InterruptedException ie) {
                                //ie.printStackTrace();
                            }
                        }
                        else {
                            panel.remove(minimizButton);
                            panel.remove(closeButton);
                        }
                    }
                    try {
                        Thread.currentThread().sleep(100);
                    }
                    catch (InterruptedException ie) {
                        //ie.printStackTrace();
                    }
                }
            }
        }
        windowControllThread = new Thread(new WindowControllThread(), "Window Controll Thread");
        windowControllThread.start();
    }
    
    /**
     * Check whether a given key is currently pressed down.
     * 
     * @param keyName 
     *      The name of the key to check.
     * 
     * @return
     *      Returns true if the key is currently pressed down.
     */
    public static boolean isKeyDown(String keyName) {
        if (displayedWorld != null) {
            if (keyInfo != null && !keyInfo.isEmpty()) {
                try {
                    for (FullScreenKeyInfo info : keyInfo) {
                        if (info.isKeyDown(keyName)) {
                            return true;
                        }
                    }
                }
                catch (java.util.ConcurrentModificationException cme) {
                    //cme.printStackTrace();
                    return false;
                }
                catch (java.util.NoSuchElementException nsee) {
                    //nsee.printStackTrace();
                    return false;
                }
                catch (NullPointerException npe) {
                    //npe.printStackTrace();
                    return false;
                }
            }
            return false;
        }
        else {
            return Greenfoot.isKeyDown(keyName);
        }
    }
    /**
     * Get the most recently pressed key, since the last time this method was called.
     * If no key was pressed since this method was last called, it will return null.
     * If more than one key was pressed, this returns only the most recently pressed key.
     *   
     * @return
     *      The name of the most recently pressed key.
     */
    public static String getKey() {
        if (displayedWorld != null) {
            if (keyInfo != null && !keyInfo.isEmpty()) {
                String key = keyInfo.get(keyInfo.size()-1).getKey();
                keyInfo.remove(keyInfo.size()-1);
                return key;
            }
            return null;
        }
        else {
            return Greenfoot.getKey();
        }
    }
    
    /**
     * Return a FullScreenMouseInfo object with information about the mouse state.
     * 
     * @return
     *      The info about the current state of the mouse, or null if the mouse cursor is outside the world boundary (unless being dragged).
     */
    public static FullScreenMouseInfo getMouseInfo() {
        if (displayedWorld != null && lastMouseEvent != null) {
            return new FullScreenMouseInfo(lastMouseEvent, selfReference);
        }
        else {
            return null;//Greenfoot.getMouseInfo();
        }
    }
    
    /**
     * Get the reference to the world that is currently displayed.
     * 
     * @return
     *      The reference to the world that is displayed.
     */
    public static FullScreenWorld getDisplayedWorld() {
        return displayedWorld;
    }
    
    /**
     * Hide the greenfoot frame so that the fullscreen window is the only visible frame.
     */
    public static final void hideGreenfootFrame() {
        if (displayedWorld != null && displayedWorld instanceof FullScreenWorld) {
            ((FullScreenWorld) displayedWorld).hideGreenfootFrame();
        }
    }
    /**
     * Show the greenfoot frame so that it's visible again.
     */
    public static final void showGreenfootFrame() {
        if (displayedWorld != null && displayedWorld instanceof FullScreenWorld) {
            ((FullScreenWorld) displayedWorld).showGreenfootFrame();
        }
    }
    
    /**
     * Set the mouse cursor for this frame.
     * 
     * @param image
     *      The image of the new mouse cursor.
     * 
     * @param cursorPoint
     *      The click point of the new cursor.
     */
    public void setMouseCursor(BufferedImage image, Point cursorPoint) {
        panel.setMouseCursor(Toolkit.getDefaultToolkit().createCustomCursor(image, cursorPoint, "cursor"));
    }
    /**
     * Set the mouse cursor for this frame.
     * 
     * @param cursor
     *      The new cursor.
     */
    public void setMouseCursor(Cursor cursor) {
        panel.setCursor(cursor);
    }
    
    /**
     * Tell the fullscreenwindow to create a new FullScreenMouseInfo object.
     * This object is stored for two acts to check whether the mouse is moved, clicked, ...
     */
    public void setMouseInfo() {
        lastMouseInfo = mouseInfo;
        if (lastMouseEvent != null) {
            mouseInfo = new FullScreenMouseInfo(lastMouseEvent, this);
        }
        else {
            mouseInfo = null;
        }
        if (isMouseClicked) {
            mouseClickCounter++;
            if (mouseClickCounter >= 2) {
                isMouseClicked = false;
            }
        }
    }
    
    /**
     * Set the last occouring mouse event.
     * 
     * @param mouseEvent
     *      The latest occouring MouseEvent object passed on by the mouse listener.
     */
    public void setLastMouseEvent(MouseEvent mouseEvent) {
        this.lastMouseEvent = mouseEvent;
    }
    
    /**
     * Set the currently displayed image.
     * 
     * @param img
     *      The next displayed image.
     */
    public void setImage(BufferedImage img) {
        FullScreenBufferPanel panel = (FullScreenBufferPanel) getContentPane();
        panel.setImage(img);
        panel.repaint();
    }
    
    /**
     * Add a new FullScreenKeyInfo object to the key info queue when a key was pressed.
     */
    public void addKeyEvent(FullScreenKeyInfo keyInfo) {
        this.keyInfo.add(keyInfo);
    }
    /**
     * Delete a FullScreenKeyInfo object from the key info queue when the key was released.
     */
    public void deleteKeyEvent(FullScreenKeyInfo keyInfo) {
        String deletingKey = keyInfo.getKey();
        for (int i = 0; i < this.keyInfo.size(); i++) {
            if (this.keyInfo.get(i).getKey().equals(deletingKey)) {
                this.keyInfo.remove(i);
                i--;
            }
        }
    }
    /**
     * Clear the whole key info queue.
     */
    public void deleteAllKeyEvents() {
        keyInfo = new java.util.ArrayList<FullScreenKeyInfo>();
    }
    
    /**
     * True if the mouse has been clicked (pressed and released) on the given object.
     * If the parameter is an Actor the method will only return true if the mouse has been clicked on the given actor.
     * If there are several actors at the same place, only the top most actor will receive the click.
     * If the parameter is a World then true will be returned if the mouse was clicked on the world background.
     * If the parameter is null, then true will be returned for any click, independent of the target clicked on.
     * 
     * @param obj
     *      Typically one of Actor, World or null.
     * 
     * @return
     *      Returns true if the mouse has been clicked as explained above.
     */
    public static boolean mouseClicked(java.lang.Object obj) {
        if (displayedWorld != null) {
            if (isMouseClicked) {
                if (obj != null) {
                    if ((obj instanceof Actor && displayedWorld.getObjectsAt(mouseInfo.getX(), mouseInfo.getY(), obj.getClass()).contains(obj)) || (obj instanceof World && obj.equals(displayedWorld))) {
                        return true;
                    }
                    return false;
                }
                else {
                    return true;
                }
            }
            return false;
        }
        else {
            return Greenfoot.mouseClicked(obj);
        }
    }
    /**
     * Set the current mouseClicked state.
     * This method is called from the GreenfootKeyListener object when the mouse has been clicked.
     * 
     * @param isMouseClicked
     *      The new mouse state.
     */
    public void setMouseClicked(boolean isMouseClicked) {
        this.isMouseClicked = isMouseClicked;
        mouseClickCounter = 0;
    }
    
    /**
     * True if the mouse is currently being dragged on the given object.
     * The mouse is considered to be dragged on an object if the drag started on that object - even if the mouse has since been moved outside of that object.
     * If the parameter is an Actor the method will only return true if the drag started on the given actor.
     * If there are several actors at the same place, only the top most actor will receive the drag.
     * If the parameter is a World then true will be returned if the drag action was started on the world background.
     * If the parameter is null, then true will be returned for any drag action, independent of the target clicked on.
     * 
     * @param obj
     *      Typically one of Actor, World or null.
     * 
     * @return
     *      Returns true if the mouse has been dragged as explained above.
     */
    public static boolean mouseDragged(java.lang.Object obj) {
        if (displayedWorld != null) {
            if (isMouseDragged) {
                if (obj != null) {
                    if ((obj instanceof Actor && displayedWorld.getObjectsAt(mouseInfo.getX(), mouseInfo.getY(), obj.getClass()).contains(obj)) || (obj instanceof World && obj.equals(displayedWorld))) {
                        return true;
                    }
                    return false;
                }
                return true;
            }
            return false;
        }
        else {
            return Greenfoot.mouseDragged(obj);
        }
    }
    /**
     * Set the current mouseDragged state.
     * This method is called from the GreenfootKeyListener object when the mouse has been dragged.
     * 
     * @param isMouseDragged
     *      The new mouse state.
     */
    public void setMouseDragged(boolean isMouseDragged) {
        this.isMouseDragged = isMouseDragged;
    }
    
    /**
     * True if a mouse drag has ended.
     * This happens when the mouse has been dragged and the mouse button released.
     * If the parameter is an Actor the method will only return true if the drag started on the given actor.
     * If there are several actors at the same place, only the top most actor will receive the drag.
     * If the parameter is a World then true will be returned if the drag action was started on the world background.
     * If the parameter is null, then true will be returned for any drag action, independent of the target clicked on. 
     * 
     * @param obj
     *      Typically one of Actor, World or null.
     * 
     * @return
     *      Returns true if the mouse has been dragged as explained above.
     */
    public static boolean mouseDragEnded(java.lang.Object obj) {
        if (displayedWorld != null) {
            if (isMouseDragEnded) {
                if (obj != null) {
                    if ((obj instanceof Actor && displayedWorld.getObjectsAt(mouseInfo.getX(), mouseInfo.getY(), obj.getClass()).contains(obj)) || (obj instanceof World && obj.equals(displayedWorld))) {
                        return true;
                    }
                    return false;
                }
                return true;
            }
            return false;
        }
        else {
            return Greenfoot.mouseDragEnded(obj);
        }
    }
    /**
     * Set the current mouseDragEnded state.
     * This method is called from the GreenfootKeyListener object when the mouse dragg has ended.
     * 
     * @param isMouseDragEnded
     *      The new mouse state.
     */
    public void setMouseDragEnded(boolean isMouseDragEnded) {
        this.isMouseDragEnded = isMouseDragEnded;
    }
    
    /**
     * True if the mouse has been moved on the given object.
     * The mouse is considered to be moved on an object if the mouse pointer is above that object.
     * If the parameter is an Actor the method will only return true if the move is on the given actor.
     * If there are several actors at the same place, only the top most actor will receive the move.
     * If the parameter is a World then true will be returned if the move was on the world background.
     * If the parameter is null, then true will be returned for any move, independent of the target under the move location.
     * 
     * @param obj
     *      Typically one of Actor, World or null.
     * 
     * @return
     *      True if the mouse has been moved as explained above.
     */
    public static boolean mouseMoved(java.lang.Object obj) {
        if (displayedWorld != null) {
            if (lastMouseInfo != null && mouseInfo != null && !lastMouseInfo.getMousePoint().equals(mouseInfo.getMousePoint())) {
                if (obj != null) {
                    if ((obj instanceof Actor && displayedWorld.getObjectsAt(mouseInfo.getX(), mouseInfo.getY(), obj.getClass()).contains(obj)) || (obj instanceof World && obj.equals(displayedWorld))) {
                        return true;
                    }
                    return false;
                }
                return true;
            }
            return false;
        }
        else {
            return Greenfoot.mouseMoved(obj);
        }
    }
    
    /**
     * True if the mouse has been pressed (changed from a non-pressed state to being pressed) on the given object.
     * If the parameter is an Actor the method will only return true if the mouse has been pressed on the given actor.
     * If there are several actors at the same place, only the top most actor will receive the press.
     * If the parameter is a World then true will be returned if the mouse was pressed on the world background.
     * If the parameter is null, then true will be returned for any mouse press, independent of the target pressed on.
     * 
     * @param obj
     *      Typically one of Actor, World or null.
     * 
     * @return
     *      True if the mouse has been pressed as explained above.
     */
    public static boolean mousePressed(java.lang.Object obj) {
        if (displayedWorld != null) {
            if (isMousePressed) {
                if (obj != null) {
                    if ((obj instanceof Actor && displayedWorld.getObjectsAt(mouseInfo.getX(), mouseInfo.getY(), obj.getClass()).contains(obj)) || (obj instanceof World && obj.equals(displayedWorld))) {
                        return true;
                    }
                    return false;
                }
                return true;
            }
            return false;
        }
        else {
            return Greenfoot.mousePressed(obj);
        }
    }
    /**
     * Set the current mousePressed state.
     * This method is called from the GreenfootKeyListener object when the mouse has been pressed.
     * 
     * @param isMousePressed
     *      The new mouse state.
     */
    public void setMousePressed(boolean isMousePressed) {
        this.isMousePressed = isMousePressed;
    }
}