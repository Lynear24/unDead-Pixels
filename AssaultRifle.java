import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MachineGun here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AssaultRifle extends Weapon
{
    /**
     * Act - do whatever the MachineGun wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        super.act();
    }    
    
    public AssaultRifle()
    {
        // default attributes of the Shotgun
        dmg = 15;
        ammo = 550;
        magSize = 60;
        roundInMag = magSize;
        cdRate = 70;
	cdFire = 100;
        cdReload = 3500;
        fireSound = new GreenfootSound("rifle_fire.wav");
        reloadSound = new GreenfootSound("rifle_reload.wav");
        emptySound = new GreenfootSound("rifle_empty.wav");
    }
    
    public void Shoot(int localRot, int x, int y)
    {
        if (coolDownTime == 0 && roundInMag > 0)
        {
            // add a little bit of innacuracy
           int newRot = localRot + (Greenfoot.getRandomNumber(2) == 1 ? 1: -1) * Greenfoot.getRandomNumber(2);
           Projectile p = new Projectile(Greenfoot.getRandomNumber(5) + 20, newRot, dmg, owner); // rotation
           int ForwardX = (int)(Math.cos(newRot * Math.PI / 180) * 30), ForwardY = (int)(Math.sin(newRot * Math.PI / 180) * 30);
           int RightX = -ForwardY / 3, RightY = ForwardX / 3;
        
           getWorld().addObject(p, x + ForwardX + RightX, y + ForwardY + RightY);
        
           // cooldown is max after fire
           this.playFireSound();
           this.WepFireCoolDown();
        }
              
        playEmptySound();
    }
    
    public int getWepNumber()
    {
        return 1;
    }
    
    public void playFireSound()
    {
        if (fireSound != null)
        {
            int randSound = Greenfoot.getRandomNumber(3);
            switch(randSound)
            {
                case 0:
                    playSound(fireSound);
                break;
            
                case 1:
                    playSound(new GreenfootSound("rifle_fire1.wav"));
                break;
                
                case 2:
                    playSound(new GreenfootSound("rifle_fire2.wav"));
                break;
            }
        }
    }
    
    
}
