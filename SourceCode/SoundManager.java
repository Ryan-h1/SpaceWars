/**
 * Ryan Hecht
 * Mr. Corea
 * ICS4U1-1B
 * First Created: 2018-04-27
 * Last Edited: 2020-11-18
 * 
 * This class is the SoundManager class. It is simply used for the storage of the AudioPlayer objects
 * and their various sound files
 * 
 * This class belongs to Peter Meijer and is being used under the S.A.M. use policy.
 * 
 */
public class SoundManager 
{
    // audio objects and files
    final AudioPlayer fireLaser = new AudioPlayer("fireLaser.wav");
    final AudioPlayer darudeSandstorm = new AudioPlayer("darudeSandstorm.wav");
    final AudioPlayer alienDeath = new AudioPlayer("alienDeath.wav");
    final AudioPlayer alienFireLaser = new AudioPlayer("alienFireLaser.wav");
    final AudioPlayer alienHitButNotKilled = new AudioPlayer("alienHitButNotKilled.wav");
    final AudioPlayer newWave = new AudioPlayer("newWave.wav");
    final AudioPlayer machineGunPowerUp = new AudioPlayer("machineGunPowerUp.wav");
    final AudioPlayer speedBoostPowerUp = new AudioPlayer("speedBoostPowerUp.wav");
    final AudioPlayer forceFieldPowerUp = new AudioPlayer("forceFieldPowerUp.wav");
    final AudioPlayer machineGunPowerUpDeActivated = new AudioPlayer("machineGunPowerUpDeActivated.wav");
    final AudioPlayer speedBoostPowerUpDeActivated = new AudioPlayer("speedBoostPowerUpDeActivated.wav");
    final AudioPlayer forceFieldPowerUpDeActivated = new AudioPlayer("forceFieldPowerUpDeActivated.wav");
    final AudioPlayer oof = new AudioPlayer("oof.wav");
    final AudioPlayer loudOof = new AudioPlayer("loudOof.wav");
    final AudioPlayer win = new AudioPlayer("win.wav");
}

