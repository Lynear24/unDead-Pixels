import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * A simple actor to show the funktion of the full screen mode.
 * 
 * @author Gevater_Tod4711
 * @version 1.1
 */
public class Player extends Actor
{
    int vel, localRot = 0, currentWep;
    private String[] keys = new String[4];
    private Weapon[] wep = new Weapon[3];
    
    public void act() {
       

        FullScreenMouseInfo mouse = FullScreenWindow.getMouseInfo();
        if (mouse != null) {
            // turnTowards(mouse.getX(), mouse.getY());
            localRot = (int)(Math.atan2((double)(mouse.getY() - getY()), (double)(mouse.getX() - getX())) * 180 / Math.PI);
        }
        
        if (isTouching(Weapon.class))
        {
            Weapon temp = (Weapon)(getOneIntersectingObject(Weapon.class));
            
            if (!temp.getHidden())
            {
                (new GreenfootSound("weapon_pickup.wav")).play();
                wep[temp.getWepNumber()] = temp;
                temp.setHidden(true);
                temp.setImage(new GreenfootImage(" ", 0, Color.white, Color.white));
            }
            
        }
        
        move();
        ChangeWep();
        ReloadWep();
        FireWep();
    }
    
    public Player()
    {
        vel = 7;
        keys[0] = "w";
        keys[1] = "s";
        keys[2] = "a";
        keys[3] = "d"; 
    }
    
    public Player(String up, String down, String left, String right, int velocity)
    {
        vel = velocity;
        keys[0] = up;
        keys[1] = down;
        keys[2] = left;
        keys[3] = right;
    }
    
    public void move() throws NullPointerException {
        
        if (FullScreenWindow.isKeyDown(keys[0]))
        {
            setRotation(-90);
            move(vel);
        }
        
        if (FullScreenWindow.isKeyDown(keys[1]))
        {
            setRotation(90);
            move(vel);
        }
        
        if (FullScreenWindow.isKeyDown(keys[2]))
        {
            setRotation(180);
            move(vel);
        }
        
        if (FullScreenWindow.isKeyDown(keys[3]))
        {
            setRotation(0);
            move(vel);
        }
        
        setRotation(localRot);
    }
    
    private void FireWep()
    {
        if (FullScreenWindow.mousePressed(null))
        {  
            // Shoot the weapon
            // if no weapon is available, don't shoot
            if (wep[currentWep] != null)
            {
                wep[currentWep].Shoot(localRot, getX(), getY());
            }
        }
 
    }
    
    private void ChangeWep()
    {
        
     // weapon change will not be allowed when reloading
     if (wep[currentWep] != null && !wep[currentWep].isReloading())
     {
        if (FullScreenWindow.isKeyDown("1"))
        {
            if (wep[0] != null)
            {
                currentWep = 0;
                setImage("player_pistol_sprite.png");
            }
                
        }
        
        else if (FullScreenWindow.isKeyDown("2"))
        {
            if (wep[1] != null)
            {
                currentWep = 1;
                setImage("player_rifle_sprite.png");
            }
        }
        
        else if (FullScreenWindow.isKeyDown("3"))
        {
            if (wep[2] != null)
            {
                currentWep = 2;
                setImage("player_shotgun_sprite.png");
            }
        }
        
        // only update the hud when you have weapon in hand
        if (wep[currentWep] != null)
            wep[currentWep].UpdateInfo();
            
        String WepEquipped = "";
        switch(currentWep)
        {
            case 0:
            WepEquipped = "Pistol";
            break;
            
            case 1:
            WepEquipped = "Rifle";
            break;
            
            case 2:
            WepEquipped = "UTS 15";
            break;
        }
        
        GreenfootImage newInfo = new GreenfootImage(WepEquipped, 30, Color.red, new Color(0, 0, 0, 0));
        ((ExampleWorld)(getWorld())).GetWepInfo().setImage(newInfo);
     }

    }
    
    private void ReloadWep()
    {
        if (FullScreenWindow.isKeyDown("r"))
        {  
            // Shoot the weapon
            // if no weapon is available, don't reload
            if (wep[currentWep] != null)
            {
                wep[currentWep].Reload();
            }
        }
    }
    
    public void AddAmmo(int pist, int ar, int shot)
    {
        if (wep[0] != null) wep[0].AddAmmo(pist);
        if (wep[1] != null) wep[1].AddAmmo(ar);
        if (wep[2] != null) wep[2].AddAmmo(shot);
    }
}
