import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * This world is only used to initialise the scenario.
 * 
 * @author Gevater_Tod4711
 * @version 1.1
 */
public class InitialisingWorld extends World
{
    
    public InitialisingWorld() {
        super(700, 750, 1);
       
        GreenfootImage title = new GreenfootImage("Undead\n PixelZ\n Beta 3", 55, Color.red, new Color(0, 0, 0, 0));
        GreenfootImage text = new GreenfootImage("Start the Slaughter!", 30, Color.green, Color.black);
            
        Start butt = new Start();
        butt.setImage(text);
        addObject(butt, getWidth()/2, getHeight() / 2);
        
        
        getBackground().drawImage(title, getWidth()/2 - title.getWidth()/2, 40);
        Greenfoot.start();
    }
    
    public void act() {
        /*here I used Greenfoot.mouseClicked (...) because this world is no fullscreen world.
        In your scenario you should use FullScreenWindow.mouseClicked (except in non-fullscreen starting worlds like this one)*/
       
            // System.out.println(mouse.getX() + " " + mouse.getY());
    }
}