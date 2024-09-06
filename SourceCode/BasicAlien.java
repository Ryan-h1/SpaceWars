/**
 * Ryan Hecht
 * Mr. Corea
 * ICS4U1-1B
 * First Created: 2020-11-12
 * Last Edited: 2020-11-18
 *
 * This class represents a basic alien mob and is a subclass of Alien. It fires a blue laser.
 */

import java.awt.*;

public class BasicAlien extends Alien {

  public BasicAlien(int x, int y, int imageScaleDivisor) {
    super(x, y, imageScaleDivisor, 1, 1, 0.1, "basicAlien.png");
  }

  /*
   * Instantiates a new projectile and returns it
   */
  public Projectile fireProjectile() {
    return new Projectile(super.getX() + (int)(super.getOuterHitBox().getWidth()/2) - 1,
                                           (int)(super.getY() + super.getOuterHitBox().getHeight()),
                                           3, 20, Color.BLUE, 3);
  }

}
