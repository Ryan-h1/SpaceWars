/**
 * Ryan Hecht
 * Mr. Corea
 * ICS4U1-1B
 * First Created: 2020-11-12
 * Last Edited: 2020-11-18
 * 
 * This class represents an ForceFieldPowerUp that is a subclass of PowerUp. It gives the player an
 * extra life for it's duration, and if the player has not lost a live while it was equipped,
 * removes the extra life upon deActivation.
 */

import java.awt.*;
import java.awt.geom.*;

public class ForceFieldPowerUp extends PowerUp{
  
  // Constants
  public static final String IDENTIFIER = "ForceFieldPowerUp";

  private static final int DURATION_MILLIS = 10000;
  private int livesAtActivation;
  
  public ForceFieldPowerUp(int x, int y, int imageScaleDivisor) {
    super(x, y, imageScaleDivisor, 1, "forceFieldPowerUp.png", IDENTIFIER, DURATION_MILLIS);
  }
  
  /*
   * This is the method called when this powerup touches the player to change
   * the properties of the SpaceShip.
   */
  public void activate(SpaceShip spaceShip) {
    spaceShip.addLives(1);
    this.livesAtActivation = spaceShip.getLives();
  }
  
  /*
   * This method is called when the duration of this powerup is over to reset
   * the SpaceShips's values
   */
  public void deActivate(SpaceShip spaceShip) {
    if (spaceShip.getLives() >= livesAtActivation) {
      spaceShip.removeLives(1);
    }
  }
  
  /*
   * This method plays the activation sound associated with this powerUp.
   */
  public void playActivationSound(SoundManager sm) {
    sm.forceFieldPowerUp.play();
  }
  
  /*
   * This method plays the deactivation sound associated with this powerUp.
   */
  public void playDeActivationSound(SoundManager sm) {
    sm.forceFieldPowerUpDeActivated.play();
  }
  
  /*
   * Returns the number of lives the spaceship had when the powerup was equipepd
   */
  public int getSpecialValue() {
    return this.livesAtActivation;
  }
}
