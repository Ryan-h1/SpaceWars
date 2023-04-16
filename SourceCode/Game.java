/**
 * Ryan Hecht
 * Mr. Corea
 * ICS4U1-1B
 * First Created: 2020-11-12
 * Last Edited: 2020-11-16
 * 
 * This is the main class for Space Wars. Here, the game is loaded, game logic is updated,
 * key presses are handled and the screen is rendered. The game must be run from this file.
 * 
 * Upon running, this program displays a 960 x 1280 menu screen to the user, with three
 * buttons: Play, Instructions and Exit aswell as a "Space Wars" title at the top of the
 * screen. Pressing the exit button will close the program, the instructions button will
 * display the game instructions and controls to the player, and pressing the play button
 * will start the game. 
 * 
 * To learn more about the game, please refer to the Space Wars Game Manual, or run this
 * file to get first hand experience with the game.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.geom.*; 
import java.util.*;
import java.lang.*;

public class Game extends JFrame implements ActionListener, KeyListener {
  
  // Constants
  public static final int CANVAS_WIDTH = 1280;
  public static final int CANVAS_HEIGHT = 960;
  public static final int JPANEL_HEIGHT = 60;
  public static final int REFRESH_MILLI = 8;
  public static final double BASIC_ALIEN_CHANCE_OF_ATTACK = 0.0004;
  public static final Color CANVAS_BACKGROUND = Color.BLACK;
  public static final Color JPANEL_BACKGROUND = Color.WHITE;
  public static final String L1 = "65"; // A
  public static final String L2 = "37"; // LEFT ARROW KEY
  public static final String R1 = "68"; // D
  public static final String R2 = "39"; // RIGHT ARROW KEY
  public static final String FIRE = "32"; // SPACE BAR
  public static final String ENTER = "10"; // ENTER KEY
  
  // The custom drawing canvas (extends JPanel)
  private DrawCanvas canvas;
  
  // Buttons
  JButton instructionsBtn, playBtn, exitBtn, exitBtn2, backBtn, restartBtn, mainMenuBtn;
  
  // Containers for objects
  private ArrayList<String> keyList;
  private ArrayList<Projectile> playerProjectiles;
  private ArrayList<Projectile> alienProjectiles;
  private ArrayList<Alien> aliens;
  private ArrayList<PowerUp> powerUps;
  
  // Utilities
  private SoundManager sm;
  private Timer timer;
  private Random random;
  private Utilities u;
  
  // Game variables
  private SpaceShip spaceShip;
  private String gameState;
  private int spawnLevel;
  private double timeOfShot;
  private int alienHorizontalDirection;
  
  /* 
   * Constructor
   */
  public Game() {
   
    // initiliaze some game variables
    timer = new Timer(REFRESH_MILLI, this);
    random = new Random();
    keyList = new ArrayList<String>();
    sm = new SoundManager();
    u = new Utilities();
    gameState = "MAIN_MENU";
    spawnLevel = 1;
   
     // Add panel to the JFrame
    Container content = getContentPane();
    content.setLayout(null);
   
    // initialize the canvas and dimensions
    canvas = new DrawCanvas();
    // I don't know why the w requires this + 2 to reach original dimensions,
    // but the game is broken without it.
    canvas.setBounds(0,0,CANVAS_WIDTH + 2, CANVAS_HEIGHT);
    content.add(canvas);
    
    // setup Instructions button
    instructionsBtn = new JButton("Instructions");
    instructionsBtn.addActionListener(this);
    instructionsBtn.setBounds(CANVAS_WIDTH/2 - 75, CANVAS_HEIGHT/2, 150, 30);
    content.add(instructionsBtn);
           
    // setup Start button
    playBtn = new JButton("Start");
    playBtn.addActionListener(this);
    playBtn.setBounds(CANVAS_WIDTH/2 - 75, CANVAS_HEIGHT/2 - 100, 150, 30);
    content.add(playBtn);
    
    // setup Exit button
    exitBtn = new JButton("Exit");
    exitBtn.addActionListener(this);
    exitBtn.setBounds(CANVAS_WIDTH/2 - 75, CANVAS_HEIGHT/2 + 100, 150, 30);
    content.add(exitBtn);
    
    // setup Back button to display in the INSTRUCTIONS menu
    backBtn = new JButton("Back");
    backBtn.addActionListener(this);
    backBtn.setBounds(100, CANVAS_HEIGHT - 100, 100, 30);
    content.add(backBtn);
    
    // setup ingame menu restart button
    restartBtn = new JButton("Restart");
    restartBtn.addActionListener(this);
    restartBtn.setBounds(CANVAS_WIDTH/2 - 250, CANVAS_HEIGHT/2, 100, 30);
    content.add(restartBtn);
    
    // setup ingame menu back to main menu button
    mainMenuBtn = new JButton("Main Menu");
    mainMenuBtn.addActionListener(this);
    mainMenuBtn.setBounds(CANVAS_WIDTH/2 - 75, CANVAS_HEIGHT/2 , 150, 30);
    content.add(mainMenuBtn);
    
    // setup ingame menu exit button
    exitBtn2 = new JButton("Exit");
    exitBtn2.addActionListener(this);
    exitBtn2.setBounds(CANVAS_WIDTH/2 + 150, CANVAS_HEIGHT/2, 100, 30);
    content.add(exitBtn2);
     
    // add a keylistener to the frame
    addKeyListener(this);
    
    // make all of the buttons transparent
    playBtn.setOpaque(false);
    playBtn.setContentAreaFilled(false);
    playBtn.setBorderPainted(false);
    instructionsBtn.setOpaque(false);
    instructionsBtn.setContentAreaFilled(false);
    instructionsBtn.setBorderPainted(false);
    exitBtn.setOpaque(false);
    exitBtn.setContentAreaFilled(false);
    exitBtn.setBorderPainted(false);
    backBtn.setOpaque(false);
    backBtn.setContentAreaFilled(false);
    backBtn.setBorderPainted(false);
    restartBtn.setOpaque(false);
    restartBtn.setContentAreaFilled(false);
    restartBtn.setBorderPainted(false);
    mainMenuBtn.setOpaque(false);
    mainMenuBtn.setContentAreaFilled(false);
    mainMenuBtn.setBorderPainted(false);
    exitBtn2.setOpaque(false);
    exitBtn2.setContentAreaFilled(false);
    exitBtn2.setBorderPainted(false);
   
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // I don't know why the w and h need the + 7 and + 30, but setting the layout to null
    // requires it to be put here to keep original dimensions.
    setPreferredSize(new Dimension(CANVAS_WIDTH + 7, CANVAS_HEIGHT + 30));
   
    setResizable(false);
   
    pack();
    setVisible(true);
  }
  
  /*
   * Used to initialize any objects and variables specific to game logic to their default
   * values.
   */
  public void init() {
    
    // player object
    spaceShip = new SpaceShip(CANVAS_WIDTH/2 + -65, CANVAS_HEIGHT - 128, 4, 5, 300, 3);
    
    // values used for calculating some game logic
    alienHorizontalDirection = 1;
    spawnLevel = 1;
    
    // containers for objects
    aliens = new ArrayList<Alien>();
    playerProjectiles = new ArrayList<Projectile>();
    alienProjectiles = new ArrayList<Projectile>();
    powerUps = new ArrayList<PowerUp>();
    
    // loads the aliens of the specified spawnLevel into the array
    loadAliens();
  }
  
  /*
   * Used to update the next frame according to the current controls and previous frame's game
   * logic.
   */
  public void update() {
    
    // horizontal movement of the spaceship
    if (gameState.equals("PLAYING")) {
      if ((keyList.contains(L1) || keyList.contains(L2)) && spaceShip.getX() > 0) {
        if (!keyList.contains(R1) && !keyList.contains(R2)) {
          spaceShip.moveLeft();
        }
      }
      else if ((keyList.contains(R1) || keyList.contains(R2)) && spaceShip.getX() + spaceShip.getW() < CANVAS_WIDTH) {
        if (!keyList.contains(L1) && !keyList.contains(L2)) {
          spaceShip.moveRight();
        }
      }
      // if the FIRE key is pressed and the time of the last shot subtracted from the current time is less
      // than the fire delay in milliseconds, fire another shot and record the time of this shot.
      if (keyList.contains(FIRE) && System.currentTimeMillis() - timeOfShot > spaceShip.getFireDelayMillis()) {
        timeOfShot = System.currentTimeMillis();
        playerProjectiles.add(spaceShip.fireLaser());
        sm.fireLaser.play();
      }
    }
    
    // randomly generate power ups
    randomlySpawnPowerUp();
    
    // update containers of objects
    updateAliens();
    updatePlayerProjectiles();
    updateAlienProjectiles();
    updatePowerUps();
    
    // check the player's equipped power ups
    spaceShip.updateEquippedPowerUps(System.currentTimeMillis(), sm);
    
    // check collisions of objects and update state variables for next call of update()
    checkCollisions();
    
    // check if any updates have resulted in a GAME_OVER or GAME_WON state
    checkWinOrLoss();
    
    canvas.repaint();
  }
  
  /*
   * Handles key pressed events, adding the keys to an array
   */
  public void keyPressed(KeyEvent evt) {
    // check if the key being pressed is in the list, if not add it to the list of currently pressed keys
    if (!keyList.contains(evt.getKeyCode()+"")){
      keyList.add(evt.getKeyCode()+"");
    }       
  }
  
  /*
   * Handles the key released events, removing the keys from an array
   */
  public void keyReleased(KeyEvent evt) {
    // remove the key the from list if it exists
    if (keyList.contains(evt.getKeyCode()+"")){
      int index = keyList.indexOf(evt.getKeyCode()+"");
      keyList.remove(index);
    }
    
    // this needs to be put here so that it does not constantly cycle between paused and playing
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
      if (gameState.equals("PLAYING")) {
        gameState = "PAUSED";
        canvas.repaint();
      }
      else if (gameState.equals("PAUSED")) {
        gameState = "PLAYING";
      }
    }
    
  }
  
  /*
   * Handles key typed events. This method is not currently being used.
   */
  public void keyTyped(KeyEvent evt) {}
  
  /*
   * Handles action events such as menu buttons.
   */
  public void actionPerformed(ActionEvent evt) {
    if (gameState.equals("MAIN_MENU")) {
      // check if the play game button was pressed
      if (evt.getSource() == playBtn)
      {    
        init();     
        gameState = "PLAYING";
      }
      // check if the exit button was pressed
      else if (evt.getSource() == exitBtn)
      { 
        System.exit(0);
      }
      // check if the instructions button was pressed
      else if (evt.getSource() == instructionsBtn) {
        gameState = "INSTRUCTIONS";
        canvas.repaint();
      }
    }
    else if (gameState.equals("GAME_OVER") || gameState.equals("GAME_WON") || gameState.equals("PAUSED")) {
      // check if the restart button was pressed
      if (evt.getSource() == restartBtn) {
        init();     
        gameState = "PLAYING";
      }
      // check if the main menu button was pressed
      else if (evt.getSource() == mainMenuBtn) {
        gameState = "MAIN_MENU";
        canvas.repaint();
      }
      // check if the exit button in the in game menu was pressed
      else if (evt.getSource() == exitBtn2) {
        System.exit(0);
      }
    }
    // check if the back buttonw as pressed
    else if (evt.getSource() == backBtn && gameState.equals("INSTRUCTIONS")) {
      gameState = "MAIN_MENU";
      canvas.repaint();
    }
    // else, if the timer is running and the gameState is set to playing, call update()
    else if (evt.getSource()==timer &&  gameState.equals("PLAYING")) {
      update();   
    }
    requestFocus(); // change the focus to JFrame to receive KeyEvent
  }
  
  
  /**
   * DrawCanvas (inner class) is a JPanel used for custom drawing. Used to render each frame
   */
  class DrawCanvas extends JPanel {
    public void paintComponent(Graphics g) {
      // erase the screen
      super.paintComponent(g);  
      Graphics2D g2d = (Graphics2D)g;
      setBackground(Color.BLACK);
      
      // draw buttons
      drawButtons(g2d);
      
      // --------------------------------------------- MAIN MENU ---------------------------------------------
      if (gameState.equals("MAIN_MENU")) {
        g2d.setColor(Color.WHITE);
        
        // draw title
        u.drawCenteredString(g, "SPACE WARS", 
                             CANVAS_WIDTH, CANVAS_HEIGHT/2, new Font("Algerian", Font.ITALIC, 120));
      }
      // --------------------------------------------- INSTRUCTIONS ---------------------------------------------
      else if (gameState.equals("INSTRUCTIONS")) {
        
        drawInstructions(g2d);
        
      }
      // --------------------------------------------- PAUSED ---------------------------------------------
      else if (gameState.equals("PAUSED")) {
        g2d.setColor(Color.WHITE);
        u.drawCenteredString(g, "PAUSED", 
                            CANVAS_WIDTH, CANVAS_HEIGHT/2, new Font("Algerian", Font.PLAIN, 120));
        u.drawCenteredString(g, "Press ENTER to continue playing", 
                            CANVAS_WIDTH, CANVAS_HEIGHT/4*3, new Font("Courier", Font.PLAIN, 40));
      }
      // --------------------------------------------- GAME OVER ---------------------------------------------
      else if (gameState.equals("GAME_OVER")) {
        g2d.setColor(Color.WHITE);
        u.drawCenteredString(g, "GAME OVER", 
                             CANVAS_WIDTH, CANVAS_HEIGHT/2, new Font("Algerian", Font.PLAIN, 120));
      }
      // --------------------------------------------- GAME WON ---------------------------------------------
      else if (gameState.equals("GAME_WON")) {
        g2d.setColor(Color.WHITE);
        u.drawCenteredString(g, "YOU WON",
                              CANVAS_WIDTH, CANVAS_HEIGHT/2, new Font("Algerian", Font.PLAIN, 120));
      }
      // --------------------------------------------- PLAYING ---------------------------------------------
      else if (gameState.equals("PLAYING")) {
        
        // draw the space ship and any equipped powerups
        g2d.drawImage(spaceShip.getImage(), spaceShip.getX(), spaceShip.getY(), this);
        spaceShip.drawEquippedPowerUps(g2d, this);
        
        // draw the aliens
        for (Alien alien : aliens) {
          if (alien.isVisible()) {
            alien.drawThisAlien(g2d, this);
          }
        }
        
        // draw the player projectiles
        for (Projectile projectile : playerProjectiles) {
          if (projectile.isVisible()) {
            projectile.drawMovingProjectile(g2d);
          }
        }
        
        // draw the alien projectiles
        for (Projectile projectile : alienProjectiles) {
          if (projectile.isVisible()) {
            projectile.drawMovingProjectile(g2d);
          }
        }
        
        // draw PowerUps
        for (PowerUp powerUp : powerUps) {
          if (powerUp.isVisible()) {
            powerUp.drawThisPowerUp(g2d, this);
          }
        }
        
        // draw player lives
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Courier", Font.BOLD, 18));
        g2d.drawString("LIVES: " + u.toTicks(spaceShip.getLives()), 20, CANVAS_HEIGHT - 20);
        
      }
    }
  }
  
  // -------------------------------- FUNCTIONS  ---------------------------------
  
  /*
   * Calculates visibility for projectile in playerProjectiles, updating their positions if they are
   * visible and removing them from the ArrayList if they aren't.
   */
  public void updatePlayerProjectiles() {
    
    for (int i = 0; i < playerProjectiles.size(); i++) {
      // set visibility
      if (playerProjectiles.get(i).getY() <= 0 || 
          playerProjectiles.get(i).getY() + playerProjectiles.get(i).getH() >= CANVAS_HEIGHT) {
        playerProjectiles.get(i).setVisibility(false);
      }
      
      // move or remove the projectile from the ArrayList if visibile is true or false respectively
      if (playerProjectiles.get(i).isVisible()) {
        playerProjectiles.get(i).moveProjectile();
      }
      else {
        playerProjectiles.remove(i);
        i--;
      }
    }
    
  }
  
  /*
   * Calculates visibility for projectile in alienProjectiles, updating their positions if they are
   * visible and removing them from the ArrayList if they aren't.
   */
  public void updateAlienProjectiles() {
    
    // iterate through projectile objects in alienProjectiles
    for (int i = 0; i < alienProjectiles.size(); i++) {
      // set visibility
      if (alienProjectiles.get(i).getY() <= 0 || 
          alienProjectiles.get(i).getY() + alienProjectiles.get(i).getH() >= CANVAS_HEIGHT) {
        alienProjectiles.get(i).setVisibility(false);
      }
      
      // move or remove the projectile from the ArrayList if visibile is true or false respectively
      if (alienProjectiles.get(i).isVisible()) {
        alienProjectiles.get(i).moveProjectile();
      }
      else {
        alienProjectiles.remove(i);
        i--;
      }
    }
    
  }
  
  /*
   * Calculates visibility for alien in aliens, updating their positions if they are
   * visible and removing them from the ArrayList if they aren't.
   */
  public void updateAliens() {
    
    // iterate through alien objects in aliens
    for (int i = 0; i < aliens.size(); i++) {
      if (aliens.get(i).isVisible()) {
        // if any aliens have reached the location of the player, the game is over
        if (aliens.get(i).getY() + aliens.get(i).getH() >= CANVAS_HEIGHT - 110) {
          spaceShip.removeLives(spaceShip.getLives());
        }
        // if the aliens have reached the side edges, change the direction of all the aliens
        else if (aliens.get(i).getX() + aliens.get(i).getW() >= CANVAS_WIDTH) {
          alienHorizontalDirection = -1;
        }
        else if (aliens.get(i).getX() <= 0) {
          alienHorizontalDirection = 1;
        }
        // change the x and y coordinates of the alien accordingly
        aliens.get(i).moveThisAlien(alienHorizontalDirection);
        
        // random chance that the alien will fire a projectile
        double chance = random.nextDouble();
        if (chance < BASIC_ALIEN_CHANCE_OF_ATTACK) {
          sm.alienFireLaser.play();
          alienProjectiles.add(aliens.get(i).fireProjectile());
        }
      }
      // play death sound effect and remove this alien from the ArrayList
      else {
        sm.alienDeath.play();
        aliens.remove(i);
        i--;
      }
    }
  }
  
  /*
   * Calculates visibility for PowerUp in PowerUps, updating their positions if they are
   * visible and removing them from the ArrayList if they aren't.
   */
  public void updatePowerUps() {
    
    // iterate through powerUp objects in powerUps
    for (int i = 0; i < powerUps.size(); i++) {
      // set visibility to false if the powerup has fallen off the screen
      if (powerUps.get(i).getY() + powerUps.get(i).getH() >= CANVAS_HEIGHT) {
        powerUps.get(i).setVisibility(false);
      }
      
      // change the x and y coordinates of the powerUp if it is still visible, else, remove it
      if (powerUps.get(i).isVisible()) {
        powerUps.get(i).movePowerUp();
      }
      else {
        powerUps.remove(i);
        i--;
      }
    }
  }
  
  /*
   * Calculates collisions and sets the visibilities of objects accordingly
   */
  public void checkCollisions() {
    
    // check collison of playerProjectiles with aliens
    for (Projectile projectile : playerProjectiles) {
      for (Alien alien : aliens) {
        if (u.detectCollision(projectile.getOuterHitBox(), alien.getOuterHitBox())) {
          projectile.setVisibility(false);
          alien.decrementLives(1);
          if (alien.getLives() <= 0) {
            alien.setVisibility(false);
          }
          else {
            sm.alienHitButNotKilled.play();
          }
        }
      }
    }
    
    // check collision of alienProjectiles with player
    for (Projectile projectile : alienProjectiles) {
      if (u.detectCollision(spaceShip.getInnerCenterHitBox(), projectile.getOuterHitBox()) ||
          u.detectCollision(spaceShip.getInnerRearHitBox(), projectile.getOuterHitBox())) {
        if (spaceShip.getLives() > 1) {
          sm.oof.play();
        }
        projectile.setVisibility(false);
        spaceShip.removeLives(1);
      }
    }
    
    // check collisions of powerUps with player
    for (PowerUp powerUp : powerUps) {
      if (u.detectCollision(spaceShip.getInnerCenterHitBox(), powerUp.getOuterHitBox()) ||
          u.detectCollision(spaceShip.getInnerRearHitBox(), powerUp.getOuterHitBox())) {
        spaceShip.activatePowerUp(powerUp);
        powerUp.playActivationSound(sm);
        powerUp.setVisibility(false);
      }
    }
  }
  
  /*
   * Uses a randomly generated double to create the chance to spawn a powerup.
   */
  public void randomlySpawnPowerUp() {
    double chance = random.nextDouble();
    if (chance < 0.00035) {
      MachineGunPowerUp machineGunPowerUp = new MachineGunPowerUp((int)(Math.random()*CANVAS_WIDTH-150 + 1)
                                                                                               +100, 0, 10);
      powerUps.add(machineGunPowerUp);
    }
    else if (chance < 0.0006) {
      SpeedBoostPowerUp speedBoostPowerUp = new SpeedBoostPowerUp((int)(Math.random()*CANVAS_WIDTH-150 + 1)
                                                                                               +100, 0, 16);
      powerUps.add(speedBoostPowerUp);
    }
    else if (chance < 0.001) {
      ForceFieldPowerUp forceFieldPowerUp = new ForceFieldPowerUp((int)(Math.random()*CANVAS_WIDTH-150 + 1)
                                                                                               +100, 0, 12);
      powerUps.add(forceFieldPowerUp);
    }
  }
  
  /*
   * Updates the game state
   * 
   * Note that the game state is updated in various other places in the Game class, however,
   * the conditions to do so are placed in this method whenever possible.
   */
  public void checkWinOrLoss() {
    if (gameState.equals("PLAYING")) {
      if (aliens.isEmpty()) {
        if (spawnLevel < 4) {
          spawnLevel++;
          sm.newWave.play();
          loadAliens();
        }
        else {
          sm.win.play();
          gameState = "GAME_WON";
        }
      // the array should still have aliens in it when the player loses all their lives  
      }
      else if (spaceShip.getLives() <= 0) {
        sm.loudOof.play();
        gameState = "GAME_OVER";
      }
    }
  }
  
  /*
   * Changes the visiblity of the ingame menu buttons and their associated button holders
   */
  public void drawButtons(Graphics2D g2d) {
    // MAIN_MENU
    if (gameState.equals("MAIN_MENU")) {
      
      g2d.setColor(Color.WHITE);
      g2d.fill(new Rectangle2D.Double(CANVAS_WIDTH/2 - 75, CANVAS_HEIGHT/2, 150, 30));
      g2d.fill(new Rectangle2D.Double(CANVAS_WIDTH/2 - 75, CANVAS_HEIGHT/2 - 100, 150, 30));
      g2d.fill(new Rectangle2D.Double(CANVAS_WIDTH/2 - 75, CANVAS_HEIGHT/2 + 100, 150, 30));
      
      g2d.setColor(Color.BLACK);
      g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
      g2d.drawString("PLAY", CANVAS_WIDTH/2 - 22, CANVAS_HEIGHT/2 - 79);
      g2d.drawString("INSTRUCTIONS", CANVAS_WIDTH/2 - 64, CANVAS_HEIGHT/2 + 21);
      g2d.drawString("EXIT", CANVAS_WIDTH/2 - 22, CANVAS_HEIGHT/2 + 121);
      
      playBtn.setVisible(true);
      instructionsBtn.setVisible(true);
      exitBtn.setVisible(true);
    }
    else {
      playBtn.setVisible(false);
      instructionsBtn.setVisible(false);
      exitBtn.setVisible(false);
    }
    
    // INSTRUCTIONS
    if (gameState.equals("INSTRUCTIONS")) {
      
      g2d.setColor(Color.WHITE);
      g2d.fill(new Rectangle2D.Double(100, CANVAS_HEIGHT - 100, 100, 30));
      
      g2d.setColor(Color.BLACK);
      g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
      g2d.drawString("BACK", 127, CANVAS_HEIGHT - 79);
      
      backBtn.setVisible(true);
    }
    else {
      backBtn.setVisible(false);
    }
    
    // GAME_WON, GAME_OVER, PLAYING
    if (gameState.equals("GAME_WON") || gameState.equals("GAME_OVER") || gameState.equals("PAUSED")) {
      
      g2d.setColor(Color.WHITE);
      g2d.fill(new Rectangle2D.Double(CANVAS_WIDTH/2 - 250, CANVAS_HEIGHT/2, 100, 30));
      g2d.fill(new Rectangle2D.Double(CANVAS_WIDTH/2 - 75, CANVAS_HEIGHT/2, 150, 30));
      g2d.fill(new Rectangle2D.Double(CANVAS_WIDTH/2 + 150, CANVAS_HEIGHT/2, 100, 30));
      
      g2d.setColor(Color.BLACK);
      g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
      g2d.drawString("RESTART", CANVAS_WIDTH/2 - 239, CANVAS_HEIGHT/2 + 21);
      g2d.drawString("MAIN MENU", CANVAS_WIDTH/2 - 50, CANVAS_HEIGHT/2 + 21);
      g2d.drawString("EXIT", CANVAS_WIDTH/2 + 178, CANVAS_HEIGHT/2 + 21);
      
      restartBtn.setVisible(true);
      mainMenuBtn.setVisible(true);
      exitBtn2.setVisible(true);
    }
    else {
      restartBtn.setVisible(false);
      mainMenuBtn.setVisible(false);
      exitBtn2.setVisible(false);
    }
  }
  
  /*
   * Draws the instructions to the screen. This method has been seperated from the DrawCanvas
   * to increase readability and allow this text to be better used elsewhere.
   */
  public void drawInstructions(Graphics2D g2d) {
    
    g2d.setColor(Color.WHITE);
    
    // INTRODUCTION
    g2d.setFont(new Font("Courier", Font.BOLD, 24));
    g2d.drawString("INTRODUCTION:", 100, 100);
    g2d.setFont(new Font("Courier", Font.BOLD, 18));
    g2d.drawString("The year is 2033. Earth is at war with the Kyartyglotes, an ancient alien race", 295, 101);
    g2d.drawString("from the planet Kyzaar. As a result of a scouting mission gone wrong, you've", 295, 121);
    g2d.drawString("found yourself stranded 10,000 miles North of Jupiter's equatorial plate - right", 295, 141);
    g2d.drawString("in the heart of Kyartyglote territory. With just a single laser cannon working,", 295, 161);
    g2d.drawString("you're about to face off against the strongest unit of the Kyatryglote army; the", 295, 181);
    g2d.drawString("Kyelshevics.", 295, 201);
    
    // OBJECTIVE
    g2d.setFont(new Font("Courier", Font.BOLD, 24));
    g2d.drawString("OBJECTIVE:", 100, 250);
    g2d.setFont(new Font("Courier", Font.BOLD, 18));
    g2d.drawString("Use your laser cannon to shoot all four platoons of the Kyelshevics. But be", 295, 251);
    g2d.drawString("careful! The Kyelshevics will fire their deadly red, blue, yellow and green", 295, 271);
    g2d.drawString("lasers back at you. If hit, you'll sustain damage, losing one of your three lives.", 295, 291);
    g2d.drawString("If you lose all of your lives, or if the Kyelshevics get close enough to your", 295, 311);
    g2d.drawString("spaceship to touch it, it's game over. Kill all four platoons, and you've won!", 295, 331);
    
    // POWERUPS
    g2d.setFont(new Font("Courier", Font.BOLD, 24));
    g2d.drawString("POWERUPS:", 100, 380);
    g2d.setFont(new Font("Courier", Font.BOLD, 18));
    g2d.drawString("Being Kyartyglote territory, valuable space debris called powerups will frequently", 295, 381);
    g2d.drawString("fly towards you. Touch these objects to gain a temporary speed boost, increased", 295, 401);
    g2d.drawString("rate of fire, or force field.", 295, 421);
    
    // CONTROLS
    g2d.setFont(new Font("Courier", Font.BOLD, 24));
    g2d.drawString("CONTROLS:", 100, 470);
    g2d.setFont(new Font("Courier", Font.BOLD, 18));
    g2d.drawString("You're not ready for action just yet. Let's go over the controls first. Use the A", 295, 471);
    g2d.drawString("and D keys or the LEFT and RIGHT arrow keys to move your spaceship. Press and hold", 295, 491);
    g2d.drawString("SPACEBAR to fire your laser cannon, and at any point in the game, press ENTER to", 295, 511);
    g2d.drawString("to initiate a pause screen with a restart, main menu and exit button.", 295, 531);
    
    // CLOSING
    g2d.setFont(new Font("Courier", Font.BOLD, 24));
    g2d.drawString("CLOSING:", 100, 580);
    g2d.setFont(new Font("Courier", Font.BOLD, 18));
    g2d.drawString("Remember, don't be afraid to pause the game and read over the full game manual", 295, 581);
    g2d.drawString("to refresh your knowledge or to gain any further information that is not covered", 295, 601);
    g2d.drawString("here. That's all soldier, good luck on your mission.", 295, 621);
  }
  
  /**
   * Loads aliens into the ArrayList for the current spawnLevel
   */
  public void loadAliens() {
    // SPAWN 1
    if (spawnLevel == 1) {
      for (int row = 0; row < 3; row++) {
        for (int col = 0; col < 10; col++) {
          BasicAlien basicAlien = new BasicAlien((col+1)*110, (row+1)*100 - 60, 24);
          aliens.add(basicAlien);
        }
      }
    }
    // SPAWN 2
    else if (spawnLevel == 2) {
      // first three rows
      for (int row = 0; row < 3; row++) {
        for (int col = 0; col < 10; col++) {
          if (col == row || col == 10 - row) {
            AdvancedAlien advancedAlien = new AdvancedAlien((col+1)*110, (row+1)*100 - 60, 24);
            aliens.add(advancedAlien);
          }
          else {
            BasicAlien basicAlien = new BasicAlien((col+1)*110, (row+1)*100 - 60, 24);
            aliens.add(basicAlien);
          }
        }
      }
      // fourth row
      for (int col = 0; col < 10; col++) {
        AdvancedAlien advancedAlien = new AdvancedAlien((col+1)*110, -60 + 400, 24);
        aliens.add(advancedAlien);
      }
    }
    // SPAWN 3
    else if (spawnLevel == 3) {
      // first three row
      for (int row = 0; row < 3; row++) {
        for (int col = 0; col < 10; col++) {
          if ((double)(col) % 2 == 0) {
            FastAlien fastAlien = new FastAlien((col+1)*110, (row+1)*100 - 60, 24);
            aliens.add(fastAlien);
          }
          else {
            BasicAlien basicAlien = new BasicAlien((col+1)*110, (row+1)*100 - 60, 24);
            aliens.add(basicAlien);
          }
        }
      }
      // fourth row
      for (int col = 0; col < 10; col++) {
        if ((double)(col) % 2 == 0) {
          FastAlien fastAlien = new FastAlien((col+1)*110, -60 + 400, 24);
          aliens.add(fastAlien);
        }
        else {
          AdvancedAlien advancedAlien = new AdvancedAlien((col+1)*110, -60 + 400, 24);
          aliens.add(advancedAlien);
        }
      }
    }
    // SPAWN 4
    else if (spawnLevel == 4) {
      // first two rows
      for (int row = 0; row < 2; row++) {
        for (int col = 0; col < 10; col++) {
          if (col == 3 || col == 6) {
            FastAlien fastAlien = new FastAlien((col+1)*110, (row+1)*100 - 60, 24);
            aliens.add(fastAlien);
          }
          else {
            AdvancedAlien advancedAlien = new AdvancedAlien((col+1)*110, (row+1)*100 - 60, 24);
            aliens.add(advancedAlien);
          }
        }
      }
      // third row
      for (int col = 0; col < 3; col++) {
        TankAlien tankAlien = new TankAlien((col+1)*420 - 305, 400, 4);
        aliens.add(tankAlien);
      }
    }
    
  }
  
  // -----------------------------------------------------------------------------
  
  public static void main(String[] args) {
    
    Game game = new Game();
    game.timer.start();
    
  }
  
}
