import greenfoot.*;
import java.awt.Color;

/**
 * Write a description of class Weapon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Weapon extends Actor
{
    protected int dmg, ammo, magSize, roundInMag, coolDownTime, cdRate, cdReload, cdFire;
    protected boolean bHidden, bReloading;
    protected GreenfootSound fireSound, reloadSound, emptySound;
    protected Player owner;
    
    /**
     * Act - do whatever the Weapon wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        // Cool down the weapon overtime
        coolDownTime = Clamp(coolDownTime - cdRate, 0, cdReload);
        
        // weapon will cool down already after reload
        // weapon change will not be allowed when reloading
	if (bReloading && coolDownTime == 0) bReloading = false;
    }
    
    public Weapon()
    {bHidden = false;}
    
    protected void WepFireCoolDown()
    {
        // Weapon Cooldown
        --roundInMag;
        coolDownTime = cdFire;
        UpdateInfo();
    }
    
    public void Shoot(int localRot, int x, int y)
    {
        
    }
    
    public void Reload()
    {
        if (roundInMag <= magSize && !bReloading) // so that you can't reload twice
        {
            if (ammo + roundInMag > magSize)
            {
                // Keep the remaining ammo
                // via subtracting magSize whenever reloading
                // Remember, if you replace roundInMag first, mag-Size - roundInMag will just result into 0
                // it won't do anything
                // be careful with the precedence
                
            if (roundInMag == 0)
            {
                playSound(reloadSound);
                bReloading = true;
                ammo -= magSize - roundInMag;
                roundInMag = magSize;
                coolDownTime = cdReload;
                UpdateInfo();
            }

            else
            {
                playSound(reloadSound);
                bReloading = true;
                ammo -= magSize - roundInMag + 1;
                roundInMag = magSize + 1;
                coolDownTime = cdReload;
                UpdateInfo();
            }
         }
            
            else
            {
                // Only set the ammo to zero and the remaining ammo to mag ONCE
                if (ammo != 0)
                {
                    // careful with precedence
                    playSound(reloadSound);
                    bReloading = true;
                    roundInMag = ammo + roundInMag;
                    ammo = 0;
                    coolDownTime = cdReload;
                    UpdateInfo();
                    
                }
                
            }
        }
    }
    
    public void AddAmmo(int inc)
    {
        ammo += inc;
    }
    
    public void UpdateInfo()
    {
       if (ammo > 0 || roundInMag > 0)
       {
           if ( roundInMag > magSize )
           {
           GreenfootImage newInfo = new GreenfootImage(ammo + "|" + magSize + " + 1", 30, Color.red, new Color(0, 0, 0, 0));        
           ((ExampleWorld)(getWorld())).GetInfo().setImage(newInfo);
           }

           else
           {
           GreenfootImage newInfo = new GreenfootImage(ammo + "|" + roundInMag, 30, Color.red, new Color(0, 0, 0, 0));        
           ((ExampleWorld)(getWorld())).GetInfo().setImage(newInfo);
          }
       }
       
       else 
       {
           GreenfootImage newInfo = new GreenfootImage("Out of ammo! | " + roundInMag, 30, Color.red, new Color(0, 0, 0, 0));
           ((ExampleWorld)(getWorld())).GetInfo().setImage(newInfo);
       }
    }
    
    public void setHidden(boolean hid)
    {
        bHidden = hid;
    }
    
    public boolean getHidden()
    {
        return bHidden;
    }
    
    public int getWepNumber()
    {
        // weapon enumeration
        // 0 is pistol
        // 1 is assault
        // 2 is shotgun
        return 0;
    }
    
    public void playFireSound()
    {
        if (fireSound != null)
        {
            playSound(fireSound);
        }
    }
    
    public void playEmptySound()
    {
        if (emptySound != null)
        {
            if (coolDownTime == 0 && roundInMag == 0)
            {
                playSound(emptySound);
                
                // just to space between sounds
                coolDownTime = 300;
            }
        }
    }
    
    private int Clamp(int inp, int min, int max)
    {
        return inp > max ? max : inp < min ? min : inp;
    }
    
    public void SetOwner(Player newOwner)
    {
        owner = newOwner;
    }
    
    public boolean isReloading()
    {
        return bReloading;
    }
    
    protected void playSound(GreenfootSound sound)
    {
        // from start to ".wav" name
        String ToWav = sound.toString().substring(0, sound.toString().indexOf(".wav") + 4);
                
        // filename
        // say for example "foo pistol_reload.wav"
        // just get "pistol_reload.wav via lastIndexOf"
          ToWav = ToWav.substring(ToWav.lastIndexOf(" ") + 1);
                
        (new GreenfootSound(ToWav)).play(); 
    }
}
