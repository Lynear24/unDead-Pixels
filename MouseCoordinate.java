import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class MouseCoordinate here.
 * 
 * @author Gevater_Tod4711
 * @version 1.1
 */
public class MouseCoordinate extends Actor
{
    
    public void act() {
        FullScreenMouseInfo mouse = FullScreenWindow.getMouseInfo();
        if (mouse != null) {
            setImage(new GreenfootImage("Mouse: (" + mouse.getX() + "|" + mouse.getY() + ")", 30, Color.red, new Color(0, 0, 0, 0)));
        }
    }    
}