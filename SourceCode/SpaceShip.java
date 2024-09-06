/**
 * Ryan Hecht
 * Mr. Corea
 * ICS4U1-1B
 * First Created: 2020-11-12
 * Last Edited: 2020-11-18
 * 
 * This is the class for the spaceship that is moved by the player.
 */

import java.awt.*;
import java.awt.geom.*; 
import javax.swing.ImageIcon;
import java.util.*;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class SpaceShip {
  
  private int speed;
  private int x;
  private int y;
  private int w;
  private int h;
  private Image image;
  private Rectangle2D.Double innerCenterHitBox;
  private Rectangle2D.Double innerRearHitBox;
  private int fireDelayMillis;
  private int lives;
  private final ArrayList<PowerUp> equippedPowerUps;
  
  /*
   * Constructor for the SpaceShip. Note that the height and width are calculated through
   * a divisor as to avoid image stretching.
   */
  public SpaceShip(int x, int y, int imageScaleDivisor, int speed, int fireDelayMillis, int lives) { 
    
    this.speed = speed;
    this.fireDelayMillis = fireDelayMillis;
    this.lives = lives;
    this.x = x;
    this.y = y;
    loadImage();
    this.w = image.getWidth(null)/imageScaleDivisor;
    this.h = image.getHeight(null)/imageScaleDivisor;
    this.image = image.getScaledInstance(this.w, this.h, Image.SCALE_DEFAULT);
    this.innerCenterHitBox = new Rectangle2D.Double(this.x + this.w/4 + 8, this.y, this.w/2 - 16, this.h);
    this.innerRearHitBox = new Rectangle2D.Double(this.x + 6, this.y + this.h/2, this.w-12, this.h/2);
    this.equippedPowerUps = new ArrayList<PowerUp>();
  }
  
  /*
   * Loads an image from the ./Graphics folder.
   */
  private void loadImage() {
      try {
          String basePath = System.getProperty("user.dir");
          String imagePath = "";

          if (basePath.endsWith("SourceCode")) {
              imagePath = basePath + "/../Graphics/SpaceShipImage.png";
          } else if (basePath.endsWith("src")) {
              imagePath = basePath + "/../Graphics/SpaceShipImage.png";
          } else {
              imagePath = basePath + "/Graphics/SpaceShipImage.png";
          }

          ImageIcon icon = new ImageIcon(imagePath);
          image = icon.getImage();

          if (image.getWidth(null) == -1 || image.getHeight(null) == -1) {
              throw new IOException("Invalid image dimensions: Width and height must be non-zero.");
          }

      } catch (NullPointerException | IOException e) {
          System.err.println("Error loading the SpaceShip image: " + e.getMessage());
          System.exit(1);
      }
  }

  
  /*
   * Instantiates a new projectile and returns it
   */
  public Projectile fireLaser() {
    Projectile laser = new Projectile(this.x + (int)(this.innerRearHitBox.getWidth()/2.0) + 3, this.y-20,
                                      5, 20, Color.WHITE, -10);
    return laser;
  }
  
  /*
   * Adds a powerUp to the ArrayList of equippedPowerUps and applies it's attributes
   */
  public void activatePowerUp(PowerUp powerUp) {
    // if a powerup of the same type is already in the array, just reset the duration
    for (PowerUp p: equippedPowerUps) {
      if (p.getIdentifier().equals(powerUp.getIdentifier())) {
        p.recordTimeActivated();
        return;
      }
    }
    powerUp.recordTimeActivated();
    powerUp.activate(this);
    equippedPowerUps.add(powerUp);
  }
  
  /*
   * Checks if any of the equipped powerups have worn off and deactivates them if they have
   */
  public void updateEquippedPowerUps(long currentTime, SoundManager sm) {
    for (int i = 0; i < this.equippedPowerUps.size(); i++) {
      if (currentTime - this.equippedPowerUps.get(i).getTimeActivated() > this.equippedPowerUps.get(i).getDuration()) {
        this.equippedPowerUps.get(i).playDeActivationSound(sm);
        this.equippedPowerUps.get(i).deActivate(this);
        this.equippedPowerUps.remove(i);
        i--;
      }
      // Special case for equippable powerups
      else if (this.equippedPowerUps.get(i).getIdentifier().equals("ForceFieldPowerUp")) {
        if (this.lives < this.equippedPowerUps.get(i).getSpecialValue()) {
          this.equippedPowerUps.get(i).playDeActivationSound(sm);
          this.equippedPowerUps.get(i).deActivate(this);
          this.equippedPowerUps.remove(i);
          i--;
        }
      }
    }
  }
  
  /*
   * Equips images for any powerUps
   */
  public void drawEquippedPowerUps(Graphics2D g2d, ImageObserver observer) {
    for (PowerUp powerUp : this.equippedPowerUps) {
      if (powerUp.getIdentifier().equals("ForceFieldPowerUp")) {
        if (this.lives >= powerUp.getSpecialValue()) {
          powerUp.drawThisPowerUp(g2d, observer, this.x, this.y - 40);
        }
      }
    }
  }
  
  // Moves the SpaceShip horizontally
  public void moveLeft() {
    x -= speed;
    this.innerCenterHitBox = new Rectangle2D.Double(this.x + this.w/4 + 8, this.y, this.w/2 - 16, this.h);
    this.innerRearHitBox = new Rectangle2D.Double(this.x + 6, this.y + this.h/2, this.w-12, this.h/2);
  }
  public void moveRight() {
    x += speed;
    this.innerCenterHitBox = new Rectangle2D.Double(this.x + this.w/4 + 8, this.y, this.w/2 - 16, this.h);
    this.innerRearHitBox = new Rectangle2D.Double(this.x + 6, this.y + this.h/2, this.w-12, this.h/2);
  }
  /*
   * Sets the speed of the SpaceShip to the given speed in pixels
   */
  public void setSpeed(int speed) {
    this.speed = speed;
  }
  
  /*
   * Sets the fireDelay in milliseconds
   */
  public void setFireDelayMillis(int fireDelayMillis) {
    this.fireDelayMillis = fireDelayMillis;
  }
  
  /*
   * These seperate methods add or remove the number of lives passed to the method.
   * The addLives and removeLives functions are seperated into two methods to prevent
   * the use of negative values, simplifying the reusability of these methods.
   */
  public void addLives(int lives) {
    this.lives += lives;
  }
  public void removeLives(int lives) {
    this.lives -= lives;
  }
  
  /*
   * Accessor methods:
   */
  public int getX() { 
    return x; 
  }
  public int getY() { 
    return y; 
  }
  public int getW() { 
    return w; 
  }
  public int getH() { 
    return h; 
  }
  public int getSpeed() {
    return speed; 
  }
  public Image getImage() { 
    return image; 
  }
  public Rectangle2D.Double getInnerCenterHitBox() { 
    return innerCenterHitBox; 
  }
  public Rectangle2D.Double getInnerRearHitBox() {
    return innerRearHitBox;
  }
  public int getFireDelayMillis() { 
    return fireDelayMillis; 
  }
  public int getLives() {
    return lives;
  }
  
  
}
