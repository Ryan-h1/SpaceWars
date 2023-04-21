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
    AudioPlayer fireLaser = new AudioPlayer("fireLaser.wav");
    AudioPlayer darudeSandstorm = new AudioPlayer("darudeSandstorm.wav");
    AudioPlayer alienDeath = new AudioPlayer("alienDeath.wav");
    AudioPlayer alienFireLaser = new AudioPlayer("alienFireLaser.wav");
    AudioPlayer alienHitButNotKilled = new AudioPlayer("alienHitButNotKilled.wav");
    AudioPlayer newWave = new AudioPlayer("newWave.wav");
    AudioPlayer machineGunPowerUp = new AudioPlayer("machineGunPowerUp.wav");
    AudioPlayer speedBoostPowerUp = new AudioPlayer("speedBoostPowerUp.wav");
    AudioPlayer forceFieldPowerUp = new AudioPlayer("forceFieldPowerUp.wav");
    AudioPlayer machineGunPowerUpDeActivated = new AudioPlayer("machineGunPowerUpDeActivated.wav");
    AudioPlayer speedBoostPowerUpDeActivated = new AudioPlayer("speedBoostPowerUpDeActivated.wav");
    AudioPlayer forceFieldPowerUpDeActivated = new AudioPlayer("forceFieldPowerUpDeActivated.wav");
    AudioPlayer oof = new AudioPlayer("oof.wav");
    AudioPlayer loudOof = new AudioPlayer("loudOof.wav");
    AudioPlayer win = new AudioPlayer("win.wav");
}

