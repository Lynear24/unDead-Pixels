import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.core.WorldHandler;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * This abstract world method must be used as the superclass of all fullscreen worldclasses.
 * To create a fullscreen world you have to use the constructor FullScreenWorld(int, int, int, boolean, boolean). All others will not create a fullscreen world.
 * 
 * The act method of this class is declared final because some methods need to be called every act cycle to make the fullscreenwindow work.
 * So you can't use a act method in any subclass of this world but instead of the act method the abstract void run is called every act cycle so you can use public void run() as a kind of new act method.
 * 
 * The FPS rate of the fullscreen window is set to 35 by default. If you want to change it you can use the method setFPS(int) but the higher the FPS rate is the more memory and CPU it'll take.
 * So you shouldn't have a too high FPS rate.
 * 
 * 
 * @author Gevater_Tod4711
 * @version 1.1
 */
public abstract class FullScreenWorld extends World
{
    protected static FullScreenWindow fullScreenWindow;
    
    protected static boolean fullScreenMode;
    protected static boolean cursorDisappearing;
    protected static boolean cursorDisappeared;
    protected static boolean keepResolution;
    
    public static int WORLD_WIDTH;
    public static int WORLD_HEIGHT;
    
    protected static int FPS = 60;
    protected static int drawingRate = 1000 / FPS;
    protected static int cursorDisappearingTime = 5000;
    
    protected int drawingTimer = 0;
    protected int drawingTimerMax = 0;
    
    protected static Thread worldDisplayThread;
    protected static Thread managingKeyThread;
    protected static Thread cursorManagingThread;
    
    /**
     * Create a new world with a given width and hight.
     * 
     * @param width
     *      The width of the created world.
     *  
     * @param height
     *      The height of the created world.
     */
    public FullScreenWorld(int width, int height) {
        this(width, height, 1, true, false);
    }
    /**
     * Create a new world with a given width and hight.
     * 
     * @param width
     *      The width of the created world.
     *  
     * @param height
     *      The height of the created world.
     * 
     * @param cellSize
     *      The cell size of the created world.
     */
    public FullScreenWorld(int width, int height, int cellSize) {
        this(width, height, cellSize, true, false);
    }
    /**
     * Create a new world with a given width and hight.
     * 
     * @param width
     *      The width of the created world.
     *  
     * @param height
     *      The height of the created world.
     * 
     * @param cellSize
     *      The cell size of the created world.
     * 
     * @param bounded
     *      Decide whether the actors should be able to move out of the world.
     */
    public FullScreenWorld(int width, int height, int cellSize, boolean bounded) {
        this(width, height, cellSize, bounded, false);
    }
    /**
     * Create a new world with a given width and hight.
     * 
     * @param width
     *      The width of the created world.
     *  
     * @param height
     *      The height of the created world.
     * 
     * @param cellSize
     *      The cell size of the created world.
     * 
     * @param bounded
     *      Decide whether the actors should be able to move out of the world.
     * 
     * @param fullScreenMode
     *      Decide if the created world should be executed as a fullscreen world or as a normal greenfoot world.
     */
    public FullScreenWorld(int width, int height, int cellSize, boolean bounded, boolean fullScreenMode) {
        super(width, height, cellSize, bounded);
        WORLD_WIDTH = width;
        WORLD_HEIGHT = height;
        this.fullScreenMode = fullScreenMode;
        keepResolution = false;
        if (fullScreenMode) {
            createFullScreenWindow();
            checkForManagingKeys();
            manageCursor();
            displayWorld();
        }
    }
    
    /**
     * The act mehtod is declared final to make shure that the neccessary fullscreen methods are executed every act.
     * Instead of an act method you'll have to use the run method in subclasses of this class.
     */
    public final void act() {
        if (fullScreenMode) {
            fullScreenWindow.setMouseInfo();
        }
        run();
    }
    
    /**
     * The run mehtod is called every act cycle and works like a new act method because the act method in this class is declared final.
     */
    public abstract void run();
    
    /**
     * Create the fullscreenwindow.
     */
    private final void createFullScreenWindow() {
        fullScreenWindow = new FullScreenWindow(this);
    }
    
    /**
     * Create a WorldDisplayThread and start it to draw the images on the fullscreenwindow.
     */
    private final void displayWorld() {
        class WorldDisplayer implements Runnable {
            
            public void run() {
                while (true) {
                    drawWorldImage();
                    try {
                        Thread.currentThread().sleep(drawingRate);
                    }
                    catch (InterruptedException ie) {
                        //ie.printStackTrace();
                    }
                }
            }
            
            public void drawWorldImage() {
                try {
                    if (drawingTimer >= drawingTimerMax) {
                        drawingTimer = 0;
                        BufferedImage bgImage = WorldHandler.getInstance().getSnapShot();
                        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                        if (bgImage != null && screenSize != null) {
                            if (keepResolution) {
                                double scaleFactor = Math.min(screenSize.getWidth()/WORLD_WIDTH, screenSize.getHeight()/WORLD_HEIGHT);
                                fullScreenWindow.setImage(resizeImage(bgImage, (int) (WORLD_WIDTH * scaleFactor), (int) (WORLD_HEIGHT * scaleFactor), bgImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : bgImage.getType()));
                            }
                            else {
                                fullScreenWindow.setImage(resizeImage(bgImage, (int) screenSize.getWidth(), (int) screenSize.getHeight(), bgImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : bgImage.getType()));
                            }
                        }
                    }
                    drawingTimer++;
                }
                catch (NullPointerException npe) {
                    //npe.printStackTrace();
                }
            }
        }
        worldDisplayThread = new Thread(new WorldDisplayer(), "World Display Thread");
        worldDisplayThread.start();
    }
    
    /**
     * Create a ManagingKeyThread and run it.
     * The ManagingKeyThread is used to start and stop the execution of the programm.
     * 
     * The managing keys are:
     *  - ctrl + r (run the scenario)
     *  - ctrl + p (pause the scenario)
     *  - ctrl + h (hide the greenfoot frame)
     *  - ctrl + g (show the greenfoot frame)
     *  - alt + tab (switch to the greenfoot frame)
     */
    private final void checkForManagingKeys() {
        class ManagingKeyListener implements Runnable {
            
            public void run() {
                while (true) {
                    checkForKeys();
                    try {
                        Thread.currentThread().sleep(50);
                    }
                    catch (InterruptedException ie) {
                        //ie.printStackTrace();
                    }
                }
            }
            
            public void checkForKeys() {
                if (FullScreenWindow.isKeyDown("ctrl")) {
                    if (FullScreenWindow.isKeyDown("r")) {
                        Greenfoot.start();
                    }
                    else if (FullScreenWindow.isKeyDown("p")) {
                        Greenfoot.stop();
                    }
                    if (FullScreenWindow.isKeyDown("h")) {
                        hideGreenfootFrame();
                    }
                    else if (FullScreenWindow.isKeyDown("g")) {
                        showGreenfootFrame();
                    }
                }
                if (FullScreenWindow.isKeyDown("alt") && FullScreenWindow.isKeyDown("tab")) {
                    fullScreenWindow.deleteAllKeyEvents();
                }
            }
        }
        managingKeyThread = new Thread(new ManagingKeyListener(), "Managing Key Thread");
        managingKeyThread.start();
    }
    
    /**
     * Manage the cursor funktion.
     * When the cursor is not moved for some time the cursor is made invisible.
     * The time till the cursor is made invisible can be set using the setCursurDisappearingTime(int) method and the setCursorDisappearing(boolean) method.
     */
    private final void manageCursor() {
        class CursorDisappear implements Runnable {
            
            private FullScreenMouseInfo mouse = FullScreenWindow.getMouseInfo();
            
            public void run() {
                while (true) {
                    if (!cursorDisappeared) {
                        FullScreenMouseInfo mouseInfo = FullScreenWindow.getMouseInfo();
                        try {
                            Thread.currentThread().sleep(cursorDisappearingTime);
                        }
                        catch (InterruptedException ie) {
                            //ie.printStackTrace();
                            continue;
                        }
                        if (fullScreenWindow != null && mouseInfo.getMousePoint().equals(FullScreenWindow.getMouseInfo().getMousePoint())) {
                            cursorDisappeared = true;
                            fullScreenWindow.setMouseCursor(new GreenfootImage(1, 1).getAwtImage(), new Point(0, 0));
                        }
                    }
                    else {
                        if (fullScreenWindow != null && !mouse.getMousePoint().equals(FullScreenWindow.getMouseInfo().getMousePoint())) {
                            cursorDisappeared = false;
                            fullScreenWindow.setMouseCursor(Cursor.getDefaultCursor());
                        }
                    }
                }
            }
        }
        cursorManagingThread = new Thread(new CursorDisappear(), "Cursor Managing Thread");
        if (cursorDisappearing) {
            cursorManagingThread.start();
        }
    }
    
    /**
     * Hide the greenfoot frame.
     */
    @SuppressWarnings("This method may cause problems")
    public static final void hideGreenfootFrame() {
        Component panel = WorldHandler.getInstance().getWorldCanvas();
        while (panel.getParent() != null) {
            panel = panel.getParent();
        }
        panel.setVisible(false);
    }
    /**
     * Show the greenfoot frame.
     */
    @SuppressWarnings("This method may cause problems")
    public final void showGreenfootFrame() {
        Component panel = WorldHandler.getInstance().getWorldCanvas();
        while (panel.getParent() != null) {
            panel = panel.getParent();
        }
        panel.setVisible(true);
    }
    
    /**
     * Resize the world image to a new size (screensize).
     * 
     * @param originalImage
     *      The image that is resized.
     * 
     * @param width
     *      The new width of the resized image.
     * 
     * @param height
     *      The new height of the resized image.
     * 
     * @param type
     *      The imagetype of the resizing image.
     * 
     * @return
     *      The given originalImage resized to the given width and height.
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height, int type){
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }
    
    /**
     * Get the reference to the current used FullScreenWindow object.
     * 
     * @return 
     *      The currend used FullScreenWindow object.
     */
    public final FullScreenWindow getFullScreenWindow() {
        return fullScreenWindow;
    }
    
    /**
     * Check whether the current world is a fullscreenworld or not.
     * 
     * @return
     *      Returns true if the current world is a fullscreen world; false if not.
     */
    public static boolean isFullScreenMode() {
        return fullScreenMode;
    }
    
    /**
     * Check whether the cursor is disappearing.
     * 
     * @return
     *      Return whether the cursor is disappearing or not.
     */
    public static boolean isCursorDisappearing() {
        return cursorDisappearing;
    }
    /**
     * Set the disappearing of the cursor.
     * 
     * @param disappearing
     *      Set the cursor disappearing or not disappearing.
     */
    public static void setCursorDisappearing(boolean disappearing) {
        cursorDisappearing = disappearing;
        if (disappearing) {
            if (cursorManagingThread.isInterrupted()) {
                cursorManagingThread.start();
            }
        }
        else {
            if (!cursorManagingThread.isInterrupted()) {
                cursorManagingThread.interrupt();
                if (fullScreenWindow != null && cursorDisappeared) {
                    cursorDisappeared = false;
                    fullScreenWindow.setMouseCursor(Cursor.getDefaultCursor());
                }
            }
        }
    }
    
    /**
     * Get the current used FPS rate.
     * 
     * @return
     *      The current FPS rate.
     */
    public static int getFPS() {
        return FPS;
    }
    /**
     * Set the FPS rate of the fullscreenwindow
     * 
     * @param fps
     *      The new FPS rate.
     * 
     * @throws IllegalArgumentException
     *      An IllegalArgumentException is thrown if the given FPS number is less or equal 0.
     */
    public static void setFPS(int fps) throws IllegalArgumentException {
        if (fps <= 0) {
            throw new IllegalArgumentException("fps(" + fps + ") must be greater than 0 (and should be less than 75).");
        }
        FPS = fps;
    }
    
    /**
     * Check whether the resolution is kept or changed in the fullscreen mode.
     * 
     * @return
     *      Returns true if the resolution is kept.
     */
    public static boolean isResolutionKept() {
        return keepResolution;
    }
    /**
     * Change the resolution of the fullscreen mode.
     * 
     * @param keep
     *      If the parameter is true the resolution of the worlds image is not changed.
     */
    public static void setResolutionKept(boolean keep) {
        keepResolution = keep;
    }
    
    /**
     * Get the current set time till the cursor disappears.
     * The cursor may not disappear after the set time if the cursor disappearing is set to false.
     * 
     * @return
     *      The current time till the cursor disappears.
     */
    public static int getCursorDisappearingTime() {
        return cursorDisappearingTime;
    }
    /**
     * Set the time till the cursor disappears (in milliseconds).
     * The cursor may not disappear after the set time if the cursor disappearing is set to false.
     * 
     * @param disappearingTime
     *      The time till the cursor disappears.
     */
    public static void setCursorDisappearingTime(int disappearingTime) {
        cursorDisappearingTime = disappearingTime;
    }
}