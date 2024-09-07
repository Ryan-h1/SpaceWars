/**
 * Ryan Hecht Mr. Corea ICS4U1-1B First Created: 2020-11-12 Last Edited: 2020-11-18
 *
 * <p>This class represents a TankAlien mob and is a subclass of Alien. It is a yellow alien that
 * has 10 lives (10x that of a BasicAlien) and shoots slow moving yellow bullets.
 */
import java.awt.*;
import java.awt.geom.*;

public class TankAlien extends Alien {

  public TankAlien(int x, int y, int imageScaleDivisor) {
    super(x, y, imageScaleDivisor, 10, 1, 0.1, "tankAlien.png");
  }

  /*
   * Instantiates a new projectile and returns it
   */
  public Projectile fireProjectile() {
    return new Projectile(
        Projectile.ProjectileSource.ALIEN,
        super.getX() + (int) (super.getOuterHitBox().getWidth() / 2) - 1,
        (int) (super.getY() + super.getOuterHitBox().getHeight()),
        10,
        10,
        Color.YELLOW,
        1);
  }
}
