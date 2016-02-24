import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class DualTubeShotgun here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DualTubeShotgun extends Weapon
{
    /**
     * Act - do whatever the DualTubeShotgun wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */

	// roundInMag will act as tube 1 and there will be an extra Tube2 variable
	private int Tube2, cdShuck;

	// return bFireMode ? tubeSelect ? roundInMag : Tube2 : roundInMag + Tube2 ;
	// Select for true, Alternate for false
	// bTubeSelect is false when Tube 1 is selected, and true when Tube 2 is selected
	private boolean bFireMode, bTubeSelect, bRoundChambered, bTubeRelTog, bWepFireCoolDown, bTriggerDepressedAlready;
	private GreenfootSound shuckSound;

	public DualTubeShotgun()
	{
		dmg = 20;
		ammo = 100;
		magSize = 7;
		roundInMag = magSize;
		Tube2 = 7;
		
		// tube/s is/are not being reloaded by default
		bReloading = false;
		bFireMode = true;
		bTubeSelect = false;
		bRoundChambered = true;
		bWepFireCoolDown = false;
		bTubeRelTog = false;
		bTriggerDepressedAlready = false;
		cdRate = 3;
		cdFire = 30;
		cdShuck = 30;
		cdReload = 40;
		fireSound = new GreenfootSound ("shotgun_fire.wav");
		emptySound = new GreenfootSound("shotgun_empty.wav");
		reloadSound = new GreenfootSound("shotgun_tube_load.wav");
		shuckSound = new GreenfootSound("shotgun_pump.wav");
	}

    public void act() 
    {
        // Add your action code here.

        coolDownTime = Clamp(coolDownTime - cdRate, 0, cdReload);

    if (FullScreenWindow.isKeyDown("z"))
	    {
		    bFireMode = true;
		    bTubeSelect = false;
	    }

    if (FullScreenWindow.isKeyDown("x"))
	    bFireMode = false;
			    
	if (FullScreenWindow.isKeyDown("c"))
	{
		bFireMode = true;
		bTubeSelect = true;
	}

        bTriggerDepressedAlready = FullScreenWindow.mousePressed(null);

	if (bReloading && coolDownTime == 0)
	{
		if (CurrentTubeAmmo(0) >= 7 && CurrentTubeAmmo(1) >= 7)
		{
			if (!bRoundChambered) Shuck();
			bReloading = false;
		}

		else 
		{
			if (bFireMode)
				LoadTube(bTubeSelect);
		
			else
			{
				LoadTube(bTubeRelTog);
				bTubeRelTog = !bTubeRelTog;
			}

			--ammo;
			playSound(reloadSound);
			coolDownTime = cdReload;
			UpdateInfo();
		}
	}

	if (bWepFireCoolDown && coolDownTime == 0)
	{
		Shuck();
		bWepFireCoolDown = false;
	}
    }    

    public void Shoot(int localRot, int x, int y)
    {
	    if (!bTriggerDepressedAlready)
	    {
		if (bRoundChambered)
		{

			// firemode is alternating, check if current tube is blank
			// thus shucking instead of firing
			// then toggling the currently used tube
			// if (!bFireMode && CurrentTubeAmmo(bTubeSelect ? 1 : 0) == 0)
			// {
				// // DO NOT SUBTRACT ANY VALUE SINCE THE TUBE IS ALREADY EMPTY
				// playSound(shuckSound);
				// bTubeSelect = !bTubeSelect;
				// return;
			// }

			// Tube 1 fires buckshot
			// Tube 2 fires slugs
			if (bTubeSelect) FireSlug(localRot, x, y); else FireBuck(localRot, x, y);

			playFireSound();
			this.WepFireCoolDown();

			// PLEASE BE CAREFUL WITH THE PRECEDENCE OF STATEMENTS
			// the last tube used in chambering MUST be preserved before toggling
			// since Shuck() will use bTubeSelect, toggling early leads to problems
			// bTubeSelect will toggle when bFireMode is on Alternate(false)
			bTubeSelect = (bFireMode ? bTubeSelect : !bTubeSelect);
		}

		else if (bReloading)
		{
			// Pressing the trigger cancels reload
			if (!bRoundChambered) Shuck();
			bReloading = false;
		}

		else
			playSound(emptySound);
	    }
    }

    private int CurrentTubeAmmo(int mode)
    {
	    // mode 0 is Tube1 (roundInMag), mode 1 is Tube2, mode 2 is the sum of both tubes
	int temp = 3;

	switch (mode)
	{
		case 0:
			temp = roundInMag;
			break;

		case 1:
			temp = Tube2;
			break;
		
		case 2:
			temp = roundInMag + Tube2;
			break;
	}

	return temp;
    }

    private void FireBuck(int localRot, int x, int y)
    {
            for (int i = 0; i < 6; ++i)
            {
                int newRot = localRot + (Greenfoot.getRandomNumber(2) == 1 ? 1: -1) * Greenfoot.getRandomNumber(8);
                Projectile p = new Projectile(Greenfoot.getRandomNumber(5) + 20, newRot, dmg, owner);
                int ForwardX = (int)(Math.cos(newRot * Math.PI / 180) * 30), ForwardY = (int)(Math.sin(newRot * Math.PI / 180) * 30);
                int RightX = -ForwardY / 3, RightY = ForwardX / 3;
        
                getWorld().addObject(p, x + ForwardX + RightX, y + ForwardY + RightY);
	    }

    }

    private void FireSlug(int localRot, int x, int y)
    {
		// slugs does 5 times more damage than a single pellet
                Projectile p = new Projectile(20, localRot, dmg * 5, owner); // rotation
                int ForwardX = (int)(Math.cos(localRot * Math.PI / 180) * 30), ForwardY = (int)(Math.sin(localRot * Math.PI / 180) * 30);
                int RightX = -ForwardY / 3, RightY = ForwardX / 3;
            
		// larger image than normal projectile
		GreenfootImage temp = new GreenfootImage(p.getImage());
		temp.scale(20, 20);
		p.setImage(temp);
		getWorld().addObject(p, x + ForwardX + RightX, y + ForwardY + RightY);

    }
    
    public void Reload()
    {
	    // if both of the tubes are not full
	    if (CurrentTubeAmmo(0) < 7 || CurrentTubeAmmo(1) < 7)
	    {
		    
		    bReloading = true;
		    // getting shells for the first time takes half the time yeah
		    coolDownTime = cdReload / 2;
	    }

	    else
	    {
		    bReloading = false;
	    }
    }

    protected void WepFireCoolDown()
    {
	    bRoundChambered = false;
	    bWepFireCoolDown = true;
	    coolDownTime = cdFire;
	    UpdateInfo();
    }

    private void Shuck()
    {
	    if (bTubeSelect ? Tube2 == 0 ? false : true : roundInMag == 0 ? false : true)
	    {
		    coolDownTime = cdFire;
		    playSound(shuckSound);

		    bRoundChambered = true;
		    if (bTubeSelect) --Tube2; else --roundInMag;
	    }

	    else
		    bRoundChambered = false;
		
	    UpdateInfo();
    }

    private void LoadTube(boolean bTube)
    {
	    // don't forget that two or more statements NEED BLOCKS {}
	    if (!bTube)
	    {
		    if (CurrentTubeAmmo(0) < magSize) ++roundInMag; else if (CurrentTubeAmmo(1) < magSize) ++Tube2;
	    }

	    else
	    {
		    if (CurrentTubeAmmo(1) < magSize) ++Tube2; else if (CurrentTubeAmmo(0) < magSize) ++roundInMag;
	    }
    }  

    public void UpdateInfo()
    {
	    String n = " " + (bTubeSelect ? "Tube2" : "Tube1");

	    if (ammo > 0)
	    {
		    GreenfootImage newInfo = new GreenfootImage(ammo + " | " + roundInMag + " + " + Tube2 + (bRoundChambered ? " + 1" : "") + n, 30, Color.red, new Color(0, 0, 0, 0));

		    ((ExampleWorld)(getWorld())).GetInfo().setImage(newInfo);
	    }

	    else
	    {
		    GreenfootImage newInfo = new GreenfootImage("Out of ammo! | "+ roundInMag + " + " + Tube2 + (bRoundChambered ? " + 1" : "" + n), 30, Color.red, new Color(0, 0, 0, 0));

		    ((ExampleWorld)(getWorld())).GetInfo().setImage(newInfo);
	    }
    }

    public int getWepNumber()
    {
	    return 2;
    }
};

