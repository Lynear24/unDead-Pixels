import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class AmmoBox here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AmmoBox extends Actor
{
    
    /**
     * Act - do whatever the AmmoBox wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (isTouching(Player.class))
        {
            Player temp = (Player)(getOneIntersectingObject(Player.class));
            temp.AddAmmo(150, 250, 75);
            getWorld().removeObject(this);
            (new GreenfootSound("ammo_pickup.wav")).play();
        }
    }
    
}