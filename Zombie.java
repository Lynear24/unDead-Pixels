import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Zombie here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Zombie extends Actor
{
    private int vel, health;
    
    /**
     * Act - do whatever the Zombie wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        if (isTouching(Player.class))
        {
            ((ExampleWorld)(getWorld())).DecPlayerLives();  
        }
        
        else 
            GoTowardsPlayer();
    }    
    
    public Zombie(int newVel)
    {
        vel = newVel;
        health = 100;
    }
    
    public Zombie(int newVel, int newHealth)
    {
        vel = newVel;
        health = newHealth;
    }
    
    private void GoTowardsPlayer()
    {
        Player p = ((ExampleWorld)(getWorld())).GetPlayer();
        
        if (p != null)
        {
            turnTowards(p.getX(), p.getY());
            move(vel);
        }
    }
    
    public void damageHealth(int dmg)
    {
        health -= dmg;
        
        if (health < 0)
        {
            // add score to player
            ((ExampleWorld)(getWorld())).IncScore();
            
            // spawn ammobox randomly
            // 1/10th chance to spawn
            if (Greenfoot.getRandomNumber(10) == 1)
            {
                AmmoBox a = new AmmoBox();
                getWorld().addObject(a, Greenfoot.getRandomNumber(getWorld().getWidth()), Greenfoot.getRandomNumber(getWorld().getHeight()));
            }
            
            getWorld().removeObject(this);
        }
    }
}
