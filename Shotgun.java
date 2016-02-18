import greenfoot.*;

/**
 * Write a description of class Shotgun here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Shotgun extends Weapon
{
    /**
     * Act - do whatever the Shotgun wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        // Parent method cools down the weapon
        super.act();
    }
    
    public Shotgun()
    {
        // default attributes of the Shotgun
        dmg = 20;
        ammo = 100;
        magSize = 16;
        roundInMag = magSize;
        cdRate = 7;
        cdFire = 100;
        cdReload = 250;
        reloadSound = new GreenfootSound("shotgun_reload.wav");
        fireSound = new GreenfootSound("shotgun_fire.wav");
        emptySound = new GreenfootSound("shotgun_empty.wav");
    }
    
    public void Shoot(int localRot, int x, int y)
    {
        // only fire when weapon already cooled down
        // and when rounds in magazine is not zero
        if (coolDownTime == 0 && roundInMag > 0)
        {
            for (int i = 0; i < 6; ++i)
            {
                int newRot = localRot + (Greenfoot.getRandomNumber(2) == 1 ? 1: -1) * Greenfoot.getRandomNumber(8);
                Projectile p = new Projectile(Greenfoot.getRandomNumber(5) + 20, newRot, dmg, owner);
                int ForwardX = (int)(Math.cos(newRot * Math.PI / 180) * 30), ForwardY = (int)(Math.sin(newRot * Math.PI / 180) * 30);
                int RightX = -ForwardY / 3, RightY = ForwardX / 3;
        
                getWorld().addObject(p, x + ForwardX + RightX, y + ForwardY + RightY);
            }
            
        // cooldown only happens after firing the wep duh
        // method will already do the cooldown and update ammo info
        
            playFireSound();
            this.WepFireCoolDown();
        // 
        }
        
        playEmptySound();
    }
    
    public int getWepNumber()
    {
        return 2;
    }
}
