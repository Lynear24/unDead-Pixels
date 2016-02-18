import greenfoot.*;

/**
 * Write a description of class Start here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Start extends Actor
{
    /**
     * Act - do whatever the Start wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
         if (Greenfoot.mouseClicked(null)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse.getX() > this.getX() - 150 && mouse.getX() < this.getX() + 150 && mouse.getY() > this.getY() - 150 && mouse.getY() < this.getY() + 150) 
            {
                Greenfoot.setWorld(new ExampleWorld());
                getWorld().removeObject(this);
            }
        }  
    }
}
