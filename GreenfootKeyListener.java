import java.awt.event.KeyEvent;

/**
 * This KeyListener is used to react on KeyEvents. 
 * All incoming KeyEvents are passed on to the fullscreenwindow.
 * 
 * @author Gevater_Tod4711
 * @version 1.1
 */
public class GreenfootKeyListener implements java.awt.event.KeyListener {
    
    private FullScreenWindow window;
    
    /**
     * Create a new GreenfootKeyListener with a reference to the calling FullScreenWindow object.
     */
    public GreenfootKeyListener(FullScreenWindow window) {
        this.window = window;
    }
    
    /**
     * React when a key was pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        window.addKeyEvent(new FullScreenKeyInfo(e));
    }
    
    /**
     * React when a key was released.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        window.deleteKeyEvent(new FullScreenKeyInfo(e));
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
}