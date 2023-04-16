/**
 * Ryan Hecht
 * Mr. Corea
 * ICS4U1-1B
 * First Created: 2020-11-12
 * Last Edited: 2020-11-18
 * 
 * This class represents a MachineGunPowerUp that is a subclass of PowerUp. It reduces the
 * fire delay between shots of a SpaceShip's laser gun.
 */

import java.awt.*;
import java.awt.geom.*;

public class SpeedBoostPowerUp extends PowerUp{
  
  private static final int DURATION_MILLIS = 8000;
  private static final String IDENTIFIER = "SpeedBoostPowerUp";
  
  public SpeedBoostPowerUp(int x, int y, int imageScaleDivisor) {
    super(x, y, imageScaleDivisor, 1, "speedBoostPowerUp.png", IDENTIFIER, DURATION_MILLIS);
  }
  
  /*
   * This is the method called when this powerup touches the player to change
   * the properties of the SpaceShip.
   */
  public void activate(SpaceShip spaceShip) {
    spaceShip.setSpeed(8);
  }
  
  /*
   * This method is called when the duration of this powerup is over to reset
   * the SpaceShips's values
   */
  public void deActivate(SpaceShip spaceShip) {
    spaceShip.setSpeed(5);
  }
  
  /*
   * This method plays the activation sound associated with this powerUp.
   */
  public void playActivationSound(SoundManager sm) {
    sm.speedBoostPowerUp.play();
  }
  
  /*
   * This method plays the deactivation sound associated with this powerUp.
   */
  public void playDeActivationSound(SoundManager sm) {
    sm.speedBoostPowerUpDeActivated.play();
  }

}
