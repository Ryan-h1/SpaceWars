/**
 * Ryan Hecht
 * Mr. Corea
 * ICS4U1-1B
 * First Created: 2020-11-12
 * Last Edited: 2020-11-18
 * 
 * This class represents a FastAlien mob and is a subclass of Alien. It has a very fast dy,
 * meaning it descends towards the player much faster than other aliens. It fires a slow
 * green projectile and has 1 life.
 */

import java.awt.*;
import java.awt.geom.*; 

public class FastAlien extends Alien {
  
  public FastAlien(int x, int y, int imageScaleDivisor) {
    super(x, y, imageScaleDivisor, 1, 1, 0.3, "fastAlien.png");
  }
  
  /*
   * Instantiates a new projectile and returns it
   */
  public Projectile fireProjectile() {
    return new Projectile(super.getX() + (int)(super.getOuterHitBox().getWidth()/2) - 1,
                                           (int)(super.getY() + super.getOuterHitBox().getHeight()),
                                           5, 5, Color.GREEN, 3);
  }
  
}
