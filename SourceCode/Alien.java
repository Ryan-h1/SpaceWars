/**
 * Ryan Hecht
 * Mr. Corea
 * ICS4U1-1B
 * First Created: 2020-11-12
 * Last Edited: 2020-11-18
 * 
 * This class represents an Alien mob. It is the parent of all Alien mobs, including the
 * BasicAlien, AdvancedAlien, FastAlien, and TankAlien. The most important methods and 
 * properties of the Alien object include the lives property, representing the
 * number of lives this alien has, an image property, representing the png image of the
 * alien, an outerHitBox equal to the width and height of the scaled image, and an ability
 * to fire projectiles towards the bottom of the screen through it's fireProjectile() method,
 * which returns a projectile.
 */

import java.awt.*;
import java.awt.geom.*; 
import javax.swing.ImageIcon;
import java.awt.image.ImageObserver;

public class Alien {
  
  private int x;
  private double y;
  private int w;
  private int h;
  private int dx;
  private double dy;
  private Image image;
  private boolean visible;
  private Rectangle2D.Double outerHitBox;
  private int lives;
  
  /*
   * Constructor for the SpaceShip. Note that the height and width are calculated through
   * a divisor as to avoid image stretching.
   */
  public Alien(int x, int y, int imageScaleDivisor, int lives, int dx, double dy, String imageName) { 
    
    this.x = x;
    this.y = y;
    loadImage(imageName);
    this.w = image.getWidth(null)/imageScaleDivisor;
    this.h = image.getHeight(null)/imageScaleDivisor;
    this.dx = dx;
    this.dy = dy;
    this.image = image.getScaledInstance(this.w, this.h, Image.SCALE_DEFAULT);
    this.outerHitBox = new Rectangle2D.Double(this.x, (int)this.y, this.w, this.h);
    this.visible = true;
    this.lives = lives;
  }
  
  /*
   * Loads an image from the ./Graphics folder.
   */
  private void loadImage(String imageName) {
    ImageIcon icon = new ImageIcon("./Graphics/" + imageName);
    image = icon.getImage();
  }
  
  /*
   * This method draws an image of this alien to the screen
   */
  public void drawThisAlien(Graphics2D g2d, ImageObserver observer) {
    g2d.drawImage(this.image, this.x, (int)this.y, observer);
  }
  
  /*
   * This method moves the alien
   */
  public void moveThisAlien(int direction) {
    this.x += this.dx*direction;
    this.y += this.dy;
    this.outerHitBox = new Rectangle2D.Double(this.x, (int)this.y, this.w, this.h);
  }
  
  /*
   * This method sets the visibility of this object; a property used to determine
   * whether an object should be drawn to the screen or removed from the game.
   */
  public void setVisibility(boolean visible) {
    this.visible = visible;
  }
  
  /*
   * Instantiates a new projectile and returns it. To be overridden by the subclass.
   */
  public Projectile fireProjectile() {
    Projectile alienLaser = new Projectile(this.x + (int)(this.outerHitBox.getWidth()/2.0) - 2, (int)this.y-20,
                                      5, 20, Color.WHITE, -10);
    return alienLaser;
  }
  
  /*
   * This method decrements the Alien's lives by the passed positive int
   */
  public void decrementLives(int decrementer) {
    this.lives -= decrementer;
  }
  
  /*
   * Accessor methods:
   */
  public int getX() { 
    return this.x; 
  }
  public double getY() { 
    return this.y; 
  }
  public int getW() { 
    return this.w; 
  }
  public int getH() { 
    return this.h; 
  }
  public int getHorizontalSpeed() {
    return this.dx;
  }
  public Image getImage() { 
    return this.image; 
  }
  public boolean isVisible() {
    return this.visible;
  }
  public int getLives() {
    return this.lives;
  }
  public Rectangle2D.Double getOuterHitBox() { 
    return this.outerHitBox; 
  }
  
}
