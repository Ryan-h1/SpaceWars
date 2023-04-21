/**
 * Ryan Hecht
 * Mr. Corea
 * ICS4U1-1B
 * First Created: 2020-11-12
 * Last Edited: 2020-11-18
 * 
 * This class represents a PowerUp that is applied to the player.
 */

import java.awt.*;
import java.awt.geom.*; 
import javax.swing.ImageIcon;
import java.awt.image.ImageObserver;
import java.lang.*;
import java.io.IOException;

public class PowerUp {
  
  private int x;
  private int y;
  private int w;
  private int h;
  private int dy;
  private long timeActivated;
  private Image image;
  private boolean visible;
  private Rectangle2D.Double outerHitBox;
  private String identifier;
  private int duration;
  
  /*
   * Constructor for the PowerUp class. Note that the height and width are calculated through
   * a divisor as to avoid image stretching.
   */
  public PowerUp(int x, int y, int imageScaleDivisor, int dy, String imageName, String identifier, int duration) {
    this.x = x;
    this.y = y;
    this.dy = dy;
    loadImage(imageName);
    this.w = image.getWidth(null)/imageScaleDivisor;
    this.h = image.getHeight(null)/imageScaleDivisor;
    this.image = image.getScaledInstance(this.w, this.h, Image.SCALE_DEFAULT);
    this.outerHitBox = new Rectangle2D.Double(this.x, (int)this.y, this.w, this.h);
    this.visible = true;
    this.identifier = identifier;
    this.duration = duration;
  }
  
  /*
   * Loads an image from the ./Graphics folder.
   */
  private void loadImage(String imageName) {
      try {
          String basePath = System.getProperty("user.dir");
          String imagePath = "";

          if (basePath.endsWith("SourceCode")) {
              imagePath = basePath + "/../Graphics/" + imageName;
          } else if (basePath.endsWith("src")) {
              imagePath = basePath + "/../Graphics/" + imageName;
          } else {
              imagePath = basePath + "/Graphics/" + imageName;
          }

          ImageIcon icon = new ImageIcon(imagePath);
          image = icon.getImage();

          if (image.getWidth(null) == -1 || image.getHeight(null) == -1) {
              throw new IOException("Invalid image dimensions: Width and height must be non-zero.");
          }

      } catch (NullPointerException | IOException e) {
          System.err.println("Error loading the Alien image: " + e.getMessage());
          System.exit(1);
      }
  }
  
  /*
   * This method draws an image of this PowerUp to the screen
   */
  public void drawThisPowerUp(Graphics2D g2d, ImageObserver observer) {
    g2d.drawImage(this.image, this.x, this.y, observer);
  }
  
  /*
   * This method also draws a powerup, but allows an optional specified x and y
   */
  public void drawThisPowerUp(Graphics2D g2d, ImageObserver observer, int x, int y) {
    g2d.drawImage(this.image, x, y, observer);
  }
  
  /*
   * This method moves the PowerUp
   */
  public void movePowerUp() {
    this.y += this.dy;
    this.outerHitBox = new Rectangle2D.Double(this.x, this.y, this.w, this.h);
  }
  
  /*
   * This is the method called when this powerup touches the player to change
   * the properties of the SpaceShip.
   */
  public void activate(SpaceShip spaceShip) {
    // Overridden by the subclass
  }
  
  /*
   * This method is called when the duration of this powerup is over to reset
   * the SpaceShips's values
   */
  public void deActivate(SpaceShip spaceShip) {
    // Overriden by the subclass
  }
  
  /*
   * This is the method called to record the system that at which the powerup
   * was activated the properties of the SpaceShip.
   */
  public void recordTimeActivated() {
    this.timeActivated = System.currentTimeMillis();
  }
  
  /*
   * This method sets the visibility of this object; a property used to determine
   * whether an object should be drawn to the screen or removed from the game.
   */
  public void setVisibility(boolean visible) {
    this.visible = visible;
  }
  
  /*
   * This method plays the activation sound associated with this powerUp.
   * It should be overridden in the subclass, else it will play darude sandstorm.
   */
  public void playActivationSound(SoundManager sm) {
    sm.darudeSandstorm.play();
  }
  
  /*
   * This method plays the deactivation sound associated with this powerUp.
   * It should be overridden in the subclass, else it will play darude sandstorm.
   */
  public void playDeActivationSound(SoundManager sm) {
    sm.darudeSandstorm.play();
  }
  
  /*
   * Accessor methods:
   */
  public int getX() { 
    return this.x; 
  }
  public int getY() { 
    return this.y; 
  }
  public int getW() { 
    return this.w; 
  }
  public int getH() { 
    return this.h; 
  }
  public int getDY() {
    return this.dy;
  }
  public Image getImage() { 
    return this.image; 
  }
  public boolean isVisible() {
    return this.visible;
  }
  public Rectangle2D.Double getOuterHitBox() { 
    return this.outerHitBox; 
  }
  public int getDuration() {
    return this.duration;
  }
  public String getIdentifier() {
    return this.identifier;
  }
  public long getTimeActivated() {
    return timeActivated;
  }
  
  /*
   * Special case for when an int value is needed that is not associated with all powerups.
   * It will return -9999 if it is called on a powerUp that does not have this method
   * overridden.
   */
  public int getSpecialValue() {
    return -9999;
  }
  
}
