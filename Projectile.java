import greenfoot.*;

/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Projectile extends Actor
{
    private int vel, dmg;
    private Player owner;
    
    /**
     * Act - do whatever the Projectile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        move(vel);
        
        if (isTouching(Zombie.class))
        {
             Zombie temp = (Zombie)(getOneIntersectingObject(Zombie.class));

             // pushes back the zombie
             int ForwardX = (int)(Math.cos(getRotation() * Math.PI / 180) * 65), ForwardY = (int)(Math.sin(getRotation() * Math.PI / 180) * 65);
             temp.setLocation(temp.getX() + ForwardX, temp.getY() + ForwardY);
             
             temp.damageHealth(dmg);
             
             
             
             getWorld().removeObject(this);
        }
        
        else if (isAtEdge())
            getWorld().removeObject(this);
            
        // rule of parallelism in if else statements
        // In two if statements: 
        // if the object is already deleted, it cannot evaluate the second function, which is isAtEdge()
        // However, if else is used, isAtEdge() will not be evaluated anymore thus not causing an error
        // use else if whenever the events that are going to happen are going to depend on only one thing
        // otherwise, the if statements are parallel to each other

    }    
    
    public Projectile(int newVel, int initialRot, int wepDmg, Player wepOwner)
    {
        vel = newVel / 3 * 5;
        dmg = wepDmg;
        owner = wepOwner;
        setRotation(initialRot);
    }
}
