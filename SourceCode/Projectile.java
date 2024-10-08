/**
 * Ryan Hecht Mr. Corea ICS4U1-1B First Created: 2020-11-12 Last Edited: 2020-11-18
 *
 * <p>This class represents a moving projectile. The projectile, at it's most basic form, is a
 * Rectangle2D.Double of a specified color which moves vertically across the screen, according to
 * it's specified velocity. The Rectangle2D.Double is called "outerHitBox", denoting it's use in
 * collision detection.
 */
import java.awt.*;
import java.awt.geom.*;

public class Projectile implements GameObject {
  private static int nextId = 0;

  private final int id = generateUniqueId();

  // Enum for identifying the source of the projectile
  public enum ProjectileSource {
    PLAYER,
    ALIEN
  }

  private final ProjectileSource projectileSource;
  private final Color color;
  private final int x;
  private int y;
  private final int w;
  private final int h;
  private final int velocity;
  private boolean visible;
  private Rectangle2D.Double outerHitBox;

  Projectile(ProjectileSource projectileSource, int x, int y, int w, int h, Color color, int velocity) {
    this.projectileSource = projectileSource;
    this.color = color;
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.velocity = velocity;
    this.visible = true;
  }

  /*
   * This method draws the Rectangle2D.Double of a specified colour to the screen
   */
  public void drawMovingProjectile(Graphics2D g2d) {
    g2d.setColor(color);
    g2d.fill(outerHitBox);
  }

  // This methods increments the y of the Rectangle2D.Double by it's velocity.
  public void moveProjectile() {
    this.y += this.velocity;
    outerHitBox = new Rectangle2D.Double(this.x, this.y, this.w, this.h);
  }

  /*
   * This method sets the visibility of this object; a property used to determine
   * whether an object should be drawn to the screen or removed from the game.
   */
  public void setVisibility(boolean visible) {
    this.visible = visible;
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

  public int getVelocity() {
    return velocity;
  }

  public boolean isVisible() {
    return this.visible;
  }

  public Rectangle2D.Double getOuterHitBox() {
    return this.outerHitBox;
  }

  public ProjectileSource getProjectileSource() {
    return this.projectileSource;
  }

  private int generateUniqueId() {
    return nextId++;
  }

  @Override
  public int hashCode() {
    return 31 * this.id + this.getClass().hashCode();
  }
}
