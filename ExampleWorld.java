import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * This world is a very simple example for a fullscreen world.
 * 
 * @author Gevater_Tod4711
 * @version 1.1
 */
public class ExampleWorld extends FullScreenWorld
{
    private Billboard info = new Billboard(), wepInfo = new Billboard(), score = new Billboard();
    private Player mainPlayer;
    private int time, playerLives, spawnCounter, playerScore;
    private boolean bSpawning;
    
    public ExampleWorld() {
        super(1300, 750, 1, true, true);
        FullScreenWorld.setFPS(60);
        
        mainPlayer = new Player();
        addObject(mainPlayer, 100, 100);
        addObject(new MouseCoordinate(), 100, 30);
        addObject(new DualTubeShotgun(), 500, 500);
        addObject(new Pistol(), 200, 200);
        addObject(new AssaultRifle(), 200, 300);
        
        playerScore = 0;
        GreenfootImage newInfo = new GreenfootImage("Score: " + playerScore, 30, Color.red, new Color(0, 0, 0, 0));
        score.setImage(newInfo);
        addObject(info, 100, 175);
        addObject(wepInfo, 100, 125);
        addObject(score, 1200, 100);
        
        bSpawning = true;
        
        
        Greenfoot.setSpeed(50);
        Greenfoot.start();
        setCursorDisappearing(true);
        setResolutionKept(true);
        
    }
     
    public void run() {
        
        if (bSpawning)
        {
            spawnCounter++;
        
            if (spawnCounter % (200 - playerScore * 3) == 0)
            {
                boolean bAtX = Greenfoot.getRandomNumber(2) == 1, bAtEdge = Greenfoot.getRandomNumber(2) == 1;
                addObject(new Zombie(2), bAtX ? Greenfoot.getRandomNumber(getWidth()) : bAtEdge ? getWidth(): 0, 
                bAtX ? bAtEdge ? getHeight(): 0 : Greenfoot.getRandomNumber(getHeight()));
            }
        }
        
    }
    
    public Billboard GetInfo()
    {
        return info;
    }
    
    public Billboard GetWepInfo()
    {
        return wepInfo;
    }
    
    public Player GetPlayer()
    {
        return mainPlayer;
    }
    
    public int GetPlayerLives()
    {
        return playerLives;
    }
    
    public void DecPlayerLives()
    {
        playerLives--;
        removeObject(mainPlayer);
        mainPlayer = null;
        bSpawning = false;
        // signal to respawn player again
        RespawnPlayerWithDefaultWeps();
    }
    
    private void RespawnPlayerWithDefaultWeps()
    {
    
    }
    
    public void IncScore()
    {
        playerScore++;
        GreenfootImage newInfo = new GreenfootImage("Score: " + playerScore, 30, Color.red, new Color(0, 0, 0, 0));
        score.setImage(newInfo);
    }
}
