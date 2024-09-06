/**
 * Ryan Hecht
 * Mr. Corea
 * ICS4U1-1B
 * First Created: 2020-11-12
 * Last Edited: 2020-11-18
 * 
 * This class represents an AdvancedAlien mob and is a subclass of Alien. It has 2 lives (2x
 * as many as the BasicAlien), and fires a fast, red laser.
 */

import java.awt.*;
import java.awt.geom.*; 

public class AdvancedAlien extends Alien {
  
  public AdvancedAlien(int x, int y, int imageScaleDivisor) {
    super(x, y, imageScaleDivisor, 2, 1, 0.1, "advancedAlien.png");
  }
  
  /*
   * Instantiates a new projectile and returns it
   */
  public Projectile fireProjectile() {
    return new Projectile(super.getX() + (int)(super.getOuterHitBox().getWidth()/2) - 1,
                                           (int)(super.getY() + super.getOuterHitBox().getHeight()),
                                           3, 30, Color.RED, 5);
  }
  
}
