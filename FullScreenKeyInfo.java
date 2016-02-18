import java.awt.event.KeyEvent;

/**
 * Objects of this class represent KeyEvent objects.
 * This objects can be used to check which keys are currently pressed.
 * 
 * @author Gevater_Tod4711
 * @version 1.1
 */
public class FullScreenKeyInfo
{
    private KeyEvent keyEvent;
    
    /**
     * Create a new FullScreenKeyInfo object with a KeyEvent object.
     * 
     * @throws IllegalArgumentException
     *      An IllegalArgumentException is thrown when the given KeyEvent object is null.
     */
    public FullScreenKeyInfo(KeyEvent keyEvent) throws IllegalArgumentException {
        if (keyEvent == null) {
            throw new IllegalArgumentException("The given KeyEvent object mussn't be null"); 
        }
        this.keyEvent = keyEvent;
    }
    
    /**
     * Check whether a key is pressed for this object.
     * This method is not static and has not the same funktion as Greenfoot.isKeyDown(String).
     * The equivalent to Greenfoot.isKeyDown(String) is FullScreenWindow.isKeyDown(String).
     */
    public boolean isKeyDown(String keyName) {
        keyName = keyName.toLowerCase();
        if (keyName.length() == 1) {
            return KeyEvent.getKeyText(keyEvent.getKeyCode()).toLowerCase().equals(keyName);// == KeyEvent.getExtendedKeyCodeForChar(keyName.charAt(0));
        }
        else {
            if (keyName.equals("left")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_LEFT;
            }
            else if (keyName.equals("right")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_RIGHT;
            }
            else if (keyName.equals("up")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_UP;
            }
            else if (keyName.equals("down")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_DOWN;
            }
            else if (keyName.equals("enter")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_ENTER;
            }
            else if (keyName.equals("space")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_SPACE;
            }
            else if (keyName.equals("shift")) {
                return keyEvent.isShiftDown();
            }
            else if (keyName.equals("escape")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE;
            }
            else if (keyName.equals("backspace")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_BACK_SPACE;
            }
            else if (keyName.equals("ctrl") || keyName.equals("control") || keyName.equals("strg")) {
                return keyEvent.isControlDown();
            }
            else if (keyName.equals("alt")) {
                return keyEvent.isAltDown();
            }
            else if (keyName.equals("altgr")) {
                return keyEvent.isAltGraphDown();
            }
            else if (keyName.equals("tab") || keyName.equals("tabulator")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_TAB;
            }
            else if (keyName.equals("f1")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_F1;
            }
            else if (keyName.equals("f2")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_F2;
            }
            else if (keyName.equals("f3")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_F3;
            }
            else if (keyName.equals("f4")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_F4;
            }
            else if (keyName.equals("f5")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_F5;
            }
            else if (keyName.equals("f6")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_F6;
            }
            else if (keyName.equals("f7")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_F7;
            }
            else if (keyName.equals("f8")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_F8;
            }
            else if (keyName.equals("f9")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_F9;
            }
            else if (keyName.equals("f10")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_F10;
            }
            else if (keyName.equals("f11")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_F11;
            }
            else if (keyName.equals("f12")) {
                return keyEvent.getKeyCode() == KeyEvent.VK_F12;
            }
        }
        return false;
    }
    
    /**
     * Get the name of the key this KeyEvent represents.
     */
    public String getKey() {
        return KeyEvent.getKeyText(keyEvent.getKeyCode());
    }
}