import greenfoot.*;

/**
 * Write a description of class Pistol here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Pistol extends Weapon
{
    /**
     * Act - do whatever the Pistol wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    private boolean bTriggerDepressedAlready; // Semi auto fire check  
    
    public void act() 
    {
        // Add your action code here.
        super.act();

        bTriggerDepressedAlready = FullScreenWindow.mousePressed(null);
    }    
    
    public Pistol()
    {
        // default attributes of the Shotgun
        dmg = 25;
        ammo = 100;
        magSize = 36;
        roundInMag = magSize;
        cdRate = 15;
        cdFire = 50;
        cdReload = 600;
        fireSound = new GreenfootSound("pistol_fire.wav");
        reloadSound = new GreenfootSound("pistol_reload.wav");
        emptySound = new GreenfootSound("pistol_empty.wav");
        
    }
    
    public void Shoot(int localRot, int x, int y)
    {
        if (!bTriggerDepressedAlready)
        {
            if (coolDownTime == 0 && roundInMag > 0)
            {
                Projectile p = new Projectile(20, localRot, dmg, owner); // rotation
                int ForwardX = (int)(Math.cos(localRot * Math.PI / 180) * 30), ForwardY = (int)(Math.sin(localRot * Math.PI / 180) * 30);
                int RightX = -ForwardY / 3, RightY = ForwardX / 3;
            
               getWorld().addObject(p, x + ForwardX + RightX, y + ForwardY + RightY);
               
               
                // cooldown is max
               playFireSound();
               this.WepFireCoolDown();
            }
            
            else 
                this.playEmptySound();
        } 
        
    }
            
    public int getWepNumber()
    
    {
        return 0;
    }
}
