/**
 * Ryan Hecht
 * Mr. Corea
 * ICS4U1-1B
 * First Created: 2020-11-12
 * Last Edited: 2020-11-18
 * 
 * This class represents a MachineGunPowerUp that is a subclass of PowerUp. It reduces the
 * fire delay between the shots of a SpaceShip's laser gun.
 */

import java.awt.*;
import java.awt.geom.*;

public class MachineGunPowerUp extends PowerUp{
  
  // Constants
  private static final int DURATION_MILLIS = 5000;
  private static final String IDENTIFIER = "MachineGunPowerUp";
  
  public MachineGunPowerUp(int x, int y, int imageScaleDivisor) {
    super(x, y, imageScaleDivisor, 1, "machineGunPowerUp.png", IDENTIFIER, DURATION_MILLIS);
  }
  
  /*
   * This is the method called when this powerup touches the player to change
   * the properties of the SpaceShip.
   */
  public void activate(SpaceShip spaceShip) {
    spaceShip.setFireDelayMillis(150);
  }
  
  /*
   * This method is called when the duration of this powerup is over to reset
   * the SpaceShips's values
   */
  public void deActivate(SpaceShip spaceShip) {
    spaceShip.setFireDelayMillis(300);
  }
  
  /*
   * This method plays the activation sound associated with this powerUp.
   */
  public void playActivationSound(SoundManager sm) {
    sm.machineGunPowerUp.play();
  }
  
  /*
   * This method plays the deactivation sound associated with this powerUp.
   */
  public void playDeActivationSound(SoundManager sm) {
    sm.machineGunPowerUpDeActivated.play();
  }

}
