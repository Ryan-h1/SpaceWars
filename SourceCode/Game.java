/**
 * Ryan Hecht Mr. Corea ICS4U1-1B First Created: 2020-11-12 Last Edited: 2020-11-16
 *
 * <p>This is the main class for Space Wars. Here, the game is loaded, game logic is updated, key
 * presses are handled and the screen is rendered. The game must be run from this file.
 *
 * <p>Upon running, this program displays a 960 x 1280 menu screen to the user, with three buttons:
 * Play, Instructions and Exit aswell as a "Space Wars" title at the top of the screen. Pressing the
 * exit button will close the program, the instructions button will display the game instructions
 * and controls to the player, and pressing the play button will start the game.
 *
 * <p>To learn more about the game, please refer to the Space Wars Game Manual, or run this file to
 * get first hand experience with the game.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.geom.*;
import java.util.*;
import java.lang.*;
import java.util.List;

public class Game extends JFrame implements ActionListener, KeyListener, Constants {
  private final Quadtree quadtree;

  // The custom drawing canvas (extends JPanel)
  private final DrawCanvas canvas = new DrawCanvas();

  // Buttons
  JButton instructionsBtn, playBtn, exitBtn, exitBtn2, backBtn, restartBtn, mainMenuBtn;

  // Set of all the keys that are currently pressed
  private final Set<String> keysPressed = new HashSet<>();

  // Containers for objects
  private Set<Projectile> playerProjectiles;
  private Set<Projectile> alienProjectiles;
  private Set<Alien> aliens;
  private Set<PowerUp> powerUps;

  // Utilities
  private final SoundManager sm = new SoundManager();
  private final Timer timer;
  private final Random random = new Random();

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
    quadtree = new Quadtree(0, new Rectangle(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT));
    timer = new Timer(REFRESH_MILLI, this);
    gameState = "MAIN_MENU";
    spawnLevel = 1;

    // Configure JFrame
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(CANVAS_WIDTH + 7, CANVAS_HEIGHT + 30));
    setResizable(false);
    addKeyListener(this);

    // Initialize the canvas and dimensions
    canvas.setBounds(0, 0, CANVAS_WIDTH + 2, CANVAS_HEIGHT);

    // Initialize and configure buttons
    instructionsBtn = createButton("Instructions", CANVAS_WIDTH / 2 - 75, CANVAS_HEIGHT / 2);
    playBtn = createButton("Start", CANVAS_WIDTH / 2 - 75, CANVAS_HEIGHT / 2 - 100);
    exitBtn = createButton("Exit", CANVAS_WIDTH / 2 - 75, CANVAS_HEIGHT / 2 + 100);
    backBtn = createButton("Back", 100, CANVAS_HEIGHT - 100);
    restartBtn = createButton("Restart", CANVAS_WIDTH / 2 - 250, CANVAS_HEIGHT / 2);
    mainMenuBtn = createButton("Main Menu", CANVAS_WIDTH / 2 - 75, CANVAS_HEIGHT / 2);
    exitBtn2 = createButton("Exit", CANVAS_WIDTH / 2 + 150, CANVAS_HEIGHT / 2);

    // Add components to the JFrame
    Container content = getContentPane();
    content.setLayout(null);
    content.add(canvas);
    content.add(instructionsBtn);
    content.add(playBtn);
    content.add(exitBtn);
    content.add(backBtn);
    content.add(restartBtn);
    content.add(mainMenuBtn);
    content.add(exitBtn2);

    pack();
    setVisible(true);
  }

  /*
   * Creates a button with the specified text, x and y coordinates and adds it to the JFrame.
   */
  private JButton createButton(String text, int x, int y) {
    JButton button = new JButton(text);
    button.addActionListener(this);
    button.setBounds(x, y, 150, 30);
    button.setOpaque(false);
    button.setContentAreaFilled(false);
    button.setBorderPainted(false);
    return button;
  }

  /*
   * Used to initialize any objects and variables specific to game logic to their default
   * values.
   */
  public void init() {

    // player object
    spaceShip = new SpaceShip(CANVAS_WIDTH / 2 + -65, CANVAS_HEIGHT - 128, 4, 5, 300, 3);

    // values used for calculating some game logic
    alienHorizontalDirection = 1;
    spawnLevel = 1;

    // containers for objects
    aliens = new HashSet<>();
    playerProjectiles = new HashSet<>();
    alienProjectiles = new HashSet<>();
    powerUps = new HashSet<>();

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
      if ((keysPressed.contains(L1) || keysPressed.contains(L2)) && spaceShip.getX() > 0) {
        if (!keysPressed.contains(R1) && !keysPressed.contains(R2)) {
          spaceShip.moveLeft();
        }
      } else if ((keysPressed.contains(R1) || keysPressed.contains(R2))
          && spaceShip.getX() + spaceShip.getW() < CANVAS_WIDTH) {
        if (!keysPressed.contains(L1) && !keysPressed.contains(L2)) {
          spaceShip.moveRight();
        }
      }
      // if the FIRE key is pressed and the time of the last shot subtracted from the current time
      // is less
      // than the fire delay in milliseconds, fire another shot and record the time of this shot.
      if (keysPressed.contains(FIRE)
          && System.currentTimeMillis() - timeOfShot > spaceShip.getFireDelayMillis()) {
        timeOfShot = System.currentTimeMillis();
        playerProjectiles.add(spaceShip.fireLaser());
        sm.fireLaser.play();
      }
    }

    // randomly generate power ups
    randomlySpawnPowerUp();

    // update containers of objects
    updateGameObjects();

    // recalculate the quadtree with the newly changed objects
    updateQuadTree();

    // check the player's equipped power ups
    spaceShip.updateEquippedPowerUps(System.currentTimeMillis(), sm);

    // check collisions of objects and update state variables for next call of update()
    checkCollisions();

    // check if any updates have resulted in a GAME_OVER or GAME_WON state
    checkWinOrLoss();

    canvas.repaint();
  }

  /*
   * Handles key pressed events, adding the keys to a set
   */
  public void keyPressed(KeyEvent evt) {
    keysPressed.add(evt.getKeyCode() + "");
  }

  /*
   * Handles the key released events, removing the keys from an array
   */
  public void keyReleased(KeyEvent evt) {
    // remove the key from the set if it exists
    keysPressed.remove(evt.getKeyCode() + "");

    // this needs to be put here so that it does not constantly cycle between paused and playing
    if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
      if (gameState.equals("PLAYING")) {
        gameState = "PAUSED";
        canvas.repaint();
      } else if (gameState.equals("PAUSED")) {
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
      if (evt.getSource() == playBtn) {
        init();
        gameState = "PLAYING";
      }
      // check if the exit button was pressed
      else if (evt.getSource() == exitBtn) {
        System.exit(0);
      }
      // check if the instructions button was pressed
      else if (evt.getSource() == instructionsBtn) {
        gameState = "INSTRUCTIONS";
        canvas.repaint();
      }
    } else if (gameState.equals("GAME_OVER")
        || gameState.equals("GAME_WON")
        || gameState.equals("PAUSED")) {
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
    else if (evt.getSource() == timer && gameState.equals("PLAYING")) {
      update();
    }
    requestFocus(); // change the focus to JFrame to receive KeyEvent
  }

  /** DrawCanvas (inner class) is a JPanel used for custom drawing. Used to render each frame */
  class DrawCanvas extends JPanel {
    public void paintComponent(Graphics g) {
      // erase the screen
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;
      setBackground(Color.BLACK);

      // draw buttons
      drawButtons(g2d);

      // --------------------------------------------- MAIN MENU
      // ---------------------------------------------
      switch (gameState) {
        case "MAIN_MENU" -> {
          g2d.setColor(Color.WHITE);

          // draw title
          Utilities.drawCenteredString(
              g,
              "SPACE WARS",
              CANVAS_WIDTH,
              CANVAS_HEIGHT / 2,
              new Font("Algerian", Font.ITALIC, 120));
        }
        // --------------------------------------------- INSTRUCTIONS
        // ---------------------------------------------
        case "INSTRUCTIONS" -> drawInstructions(g2d);

        // --------------------------------------------- PAUSED
        // ---------------------------------------------
        case "PAUSED" -> {
          g2d.setColor(Color.WHITE);
          Utilities.drawCenteredString(
              g, "PAUSED", CANVAS_WIDTH, CANVAS_HEIGHT / 2, new Font("Algerian", Font.PLAIN, 120));
          Utilities.drawCenteredString(
              g,
              "Press ENTER to continue playing",
              CANVAS_WIDTH,
              CANVAS_HEIGHT / 4 * 3,
              new Font("Courier", Font.PLAIN, 40));
        }
        // --------------------------------------------- GAME OVER
        // ---------------------------------------------
        case "GAME_OVER" -> {
          g2d.setColor(Color.WHITE);
          Utilities.drawCenteredString(
              g,
              "GAME OVER",
              CANVAS_WIDTH,
              CANVAS_HEIGHT / 2,
              new Font("Algerian", Font.PLAIN, 120));
        }
        // --------------------------------------------- GAME WON
        // ---------------------------------------------
        case "GAME_WON" -> {
          g2d.setColor(Color.WHITE);
          Utilities.drawCenteredString(
              g, "YOU WON", CANVAS_WIDTH, CANVAS_HEIGHT / 2, new Font("Algerian", Font.PLAIN, 120));
        }
        // --------------------------------------------- PLAYING
        // ---------------------------------------------
        case "PLAYING" -> {
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
          g2d.drawString(
              "LIVES: " + Utilities.toTicks(spaceShip.getLives()), 20, CANVAS_HEIGHT - 20);
        }
      }
    }
  }

  // -------------------------------- FUNCTIONS  ---------------------------------

  private void updateGameObjects() {
    updateAliens();
    updatePlayerProjectiles();
    updateAlienProjectiles();
    updatePowerUps();
  }

  private void updateQuadTree() {
    quadtree.clear();

    for (Alien alien : aliens) {
      if (alien.isVisible()) {
        quadtree.insert(alien);
      }
    }

    for (Projectile projectile : playerProjectiles) {
      if (projectile.isVisible()) {
        quadtree.insert(projectile);
      }
    }

    for (Projectile projectile : alienProjectiles) {
      if (projectile.isVisible()) {
        quadtree.insert(projectile);
      }
    }

    for (PowerUp powerUp : powerUps) {
      if (powerUp.isVisible()) {
        quadtree.insert(powerUp);
      }
    }

    quadtree.insert(spaceShip);
  }

  /*
   * Calculates visibility for projectile in playerProjectiles, updating their positions if they are
   * visible and removing them from the ArrayList if they aren't.
   */
  public void updatePlayerProjectiles() {
    Iterator<Projectile> iterator = playerProjectiles.iterator();
    while (iterator.hasNext()) {
      Projectile playerProjectile = iterator.next();

      // set visibility
      if (playerProjectile.getY() <= 0
          || playerProjectile.getY() + playerProjectile.getH() >= CANVAS_HEIGHT) {
        playerProjectile.setVisibility(false);
      }

      // remove the projectile from the set if it's no longer visible
      if (!playerProjectile.isVisible()) {
        iterator.remove();
        continue;
      }

      // move the projectile
      playerProjectile.moveProjectile();
    }
  }

  /*
   * Calculates visibility for projectile in alienProjectiles, updating their positions if they are
   * visible and removing them from the ArrayList if they aren't.
   */
  public void updateAlienProjectiles() {
    Iterator<Projectile> iterator = alienProjectiles.iterator();
    while (iterator.hasNext()) {
      Projectile alienProjectile = iterator.next();

      // set visibility
      if (alienProjectile.getY() <= 0
          || alienProjectile.getY() + alienProjectile.getH() >= CANVAS_HEIGHT) {
        alienProjectile.setVisibility(false);
      }

      if (!alienProjectile.isVisible()) {
        iterator.remove();
        continue;
      }

      alienProjectile.moveProjectile();
    }
  }

  /*
   * Calculates visibility for alien in aliens, updating their positions if they are
   * visible and removing them from the ArrayList if they aren't.
   */
  public void updateAliens() {

    // iterate through alien objects in aliens
    Iterator<Alien> iterator = aliens.iterator();
    while (iterator.hasNext()) {
      Alien alien = iterator.next();

      // kill the alien
      if (!alien.isVisible()) {
        sm.alienDeath.play();
        iterator.remove();
        continue;
      }

      // if any aliens have reached the location of the player, the game is over
      if (alien.getY() + alien.getH() >= CANVAS_HEIGHT - 110) {
        spaceShip.removeLives(spaceShip.getLives());
      }
      // if the aliens have reached the side edges, change the direction of all the aliens
      else if (alien.getX() + alien.getW() >= CANVAS_WIDTH) {
        alienHorizontalDirection = -1;
      } else if (alien.getX() <= 0) {
        alienHorizontalDirection = 1;
      }

      // change the x and y coordinates of the alien accordingly
      alien.moveThisAlien(alienHorizontalDirection);

      // random chance that the alien will fire a projectile
      if (random.nextDouble() < BASIC_ALIEN_CHANCE_OF_ATTACK) {
        sm.alienFireLaser.play();
        alienProjectiles.add(alien.fireProjectile());
      }
    }
  }

  /*
   * Calculates visibility for PowerUp in PowerUps, updating their positions if they are
   * visible and removing them from the ArrayList if they aren't.
   */
  public void updatePowerUps() {

    // iterate through powerUp objects in powerUps
    Iterator<PowerUp> iterator = powerUps.iterator();
    while (iterator.hasNext()) {
      PowerUp powerUp = iterator.next();

      if (powerUp.getY() + powerUp.getH() >= CANVAS_HEIGHT) {
        powerUp.setVisibility(false);
      }

      if (!powerUp.isVisible()) {
        iterator.remove();
        continue;
      }

      powerUp.movePowerUp();
    }
  }

  /*
   * Calculates collisions and sets the visibilities of objects accordingly
   */
  public void checkCollisions() {
    checkAlienCollisions();
    checkPlayerCollisions();
  }

  private void checkAlienCollisions() {
    for (Alien alien : aliens) {
      List<GameObject> possibleCollisions = quadtree.retrieve(alien.getOuterHitBox());
      for (GameObject possibleCollision : possibleCollisions) {
        // If the object is not a projectile, then exit early
        if (!(possibleCollision instanceof Projectile projectile)) {
          continue;
        }

        // If the projectile came from an alien, then exit early
        if (projectile.getProjectileSource().equals(Projectile.ProjectileSource.ALIEN)) {
          continue;
        }

        // If the project did not collide with the alien, then exit early
        if (!Utilities.detectCollision(projectile.getOuterHitBox(), alien.getOuterHitBox())) {
          continue;
        }

        projectile.setVisibility(false);
        alien.decrementLives(1);

        if (alien.getLives() <= 0) {
          alien.setVisibility(false);
        } else {
          sm.alienHitButNotKilled.play();
        }
      }
    }
  }

  private void checkPlayerCollisions() {
    List<GameObject> possibleCollisions = quadtree.retrieve(spaceShip.getOuterHitBox());
    for (GameObject possibleCollision : possibleCollisions) {
      // If the GameObject is not a projectile, then don't worry about collisions
      if (possibleCollision instanceof Projectile projectile) {

        // If the projectile came from the player, then don't worry about collisions
        if (projectile.getProjectileSource().equals(Projectile.ProjectileSource.PLAYER)) {
          continue;
        }

        if (Utilities.detectCollision(spaceShip.getInnerCenterHitBox(), projectile.getOuterHitBox())
            || Utilities.detectCollision(
                spaceShip.getInnerRearHitBox(), projectile.getOuterHitBox())) {
          if (spaceShip.getLives() > 1) {
            sm.oof.play();
          }
          projectile.setVisibility(false);
          spaceShip.removeLives(1);
        }
      } else if (possibleCollision instanceof PowerUp powerUp) {
        if (Utilities.detectCollision(spaceShip.getInnerCenterHitBox(), powerUp.getOuterHitBox())
            || Utilities.detectCollision(
                spaceShip.getInnerRearHitBox(), powerUp.getOuterHitBox())) {
          spaceShip.activatePowerUp(powerUp);
          powerUp.playActivationSound(sm);
          powerUp.setVisibility(false);
        }
      }
    }
  }

  /*
   * Uses a randomly generated double to create the chance to spawn a powerup.
   */
  public void randomlySpawnPowerUp() {
    double chance = random.nextDouble();
    double spawnModifier = Math.random();
    if (chance < 0.00035) {
      powerUps.add(
          new MachineGunPowerUp((int) (spawnModifier * CANVAS_WIDTH - 150 + 1) + 100, 0, 10));
    } else if (chance < 0.0006) {
      powerUps.add(
          new SpeedBoostPowerUp((int) (spawnModifier * CANVAS_WIDTH - 150 + 1) + 100, 0, 16));
    } else if (chance < 0.001) {
      powerUps.add(
          new ForceFieldPowerUp((int) (spawnModifier * CANVAS_WIDTH - 150 + 1) + 100, 0, 12));
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
        } else {
          sm.win.play();
          gameState = "GAME_WON";
        }
        // the array should still have aliens in it when the player loses all their lives
      } else if (spaceShip.getLives() <= 0) {
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
      g2d.fill(
          new Rectangle2D.Double(
              (double) CANVAS_WIDTH / 2 - 75, (double) CANVAS_HEIGHT / 2, 150, 30));
      g2d.fill(
          new Rectangle2D.Double(
              (double) CANVAS_WIDTH / 2 - 75, (double) CANVAS_HEIGHT / 2 - 100, 150, 30));
      g2d.fill(
          new Rectangle2D.Double(
              (double) CANVAS_WIDTH / 2 - 75, (double) CANVAS_HEIGHT / 2 + 100, 150, 30));

      g2d.setColor(Color.BLACK);
      g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
      g2d.drawString("PLAY", CANVAS_WIDTH / 2 - 22, CANVAS_HEIGHT / 2 - 79);
      g2d.drawString("INSTRUCTIONS", CANVAS_WIDTH / 2 - 64, CANVAS_HEIGHT / 2 + 21);
      g2d.drawString("EXIT", CANVAS_WIDTH / 2 - 22, CANVAS_HEIGHT / 2 + 121);

      playBtn.setVisible(true);
      instructionsBtn.setVisible(true);
      exitBtn.setVisible(true);
    } else {
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
    } else {
      backBtn.setVisible(false);
    }

    // GAME_WON, GAME_OVER, PLAYING
    if (gameState.equals("GAME_WON")
        || gameState.equals("GAME_OVER")
        || gameState.equals("PAUSED")) {

      g2d.setColor(Color.WHITE);
      g2d.fill(
          new Rectangle2D.Double(
              (double) CANVAS_WIDTH / 2 - 250, (double) CANVAS_HEIGHT / 2, 100, 30));
      g2d.fill(
          new Rectangle2D.Double(
              (double) CANVAS_WIDTH / 2 - 75, (double) CANVAS_HEIGHT / 2, 150, 30));
      g2d.fill(
          new Rectangle2D.Double(
              (double) CANVAS_WIDTH / 2 + 150, (double) CANVAS_HEIGHT / 2, 100, 30));

      g2d.setColor(Color.BLACK);
      g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
      g2d.drawString("RESTART", CANVAS_WIDTH / 2 - 239, CANVAS_HEIGHT / 2 + 21);
      g2d.drawString("MAIN MENU", CANVAS_WIDTH / 2 - 50, CANVAS_HEIGHT / 2 + 21);
      g2d.drawString("EXIT", CANVAS_WIDTH / 2 + 178, CANVAS_HEIGHT / 2 + 21);

      restartBtn.setVisible(true);
      mainMenuBtn.setVisible(true);
      exitBtn2.setVisible(true);
    } else {
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
    g2d.drawString(
        "The year is 2033. Earth is at war with the Kyartyglotes, an ancient alien race", 295, 101);
    g2d.drawString(
        "from the planet Kyzaar. As a result of a scouting mission gone wrong, you've", 295, 121);
    g2d.drawString(
        "found yourself stranded 10,000 miles North of Jupiter's equatorial plate - right",
        295,
        141);
    g2d.drawString(
        "in the heart of Kyartyglote territory. With just a single laser cannon working,",
        295,
        161);
    g2d.drawString(
        "you're about to face off against the strongest unit of the Kyatryglote army; the",
        295,
        181);
    g2d.drawString("Kyelshevics.", 295, 201);

    // OBJECTIVE
    g2d.setFont(new Font("Courier", Font.BOLD, 24));
    g2d.drawString("OBJECTIVE:", 100, 250);
    g2d.setFont(new Font("Courier", Font.BOLD, 18));
    g2d.drawString(
        "Use your laser cannon to shoot all four platoons of the Kyelshevics. But be", 295, 251);
    g2d.drawString(
        "careful! The Kyelshevics will fire their deadly red, blue, yellow and green", 295, 271);
    g2d.drawString(
        "lasers back at you. If hit, you'll sustain damage, losing one of your three lives.",
        295,
        291);
    g2d.drawString(
        "If you lose all of your lives, or if the Kyelshevics get close enough to your", 295, 311);
    g2d.drawString(
        "spaceship to touch it, it's game over. Kill all four platoons, and you've won!", 295, 331);

    // POWERUPS
    g2d.setFont(new Font("Courier", Font.BOLD, 24));
    g2d.drawString("POWERUPS:", 100, 380);
    g2d.setFont(new Font("Courier", Font.BOLD, 18));
    g2d.drawString(
        "Being Kyartyglote territory, valuable space debris called powerups will frequently",
        295,
        381);
    g2d.drawString(
        "fly towards you. Touch these objects to gain a temporary speed boost, increased",
        295,
        401);
    g2d.drawString("rate of fire, or force field.", 295, 421);

    // CONTROLS
    g2d.setFont(new Font("Courier", Font.BOLD, 24));
    g2d.drawString("CONTROLS:", 100, 470);
    g2d.setFont(new Font("Courier", Font.BOLD, 18));
    g2d.drawString(
        "You're not ready for action just yet. Let's go over the controls first. Use the A",
        295,
        471);
    g2d.drawString(
        "and D keys or the LEFT and RIGHT arrow keys to move your spaceship. Press and hold",
        295,
        491);
    g2d.drawString(
        "SPACEBAR to fire your laser cannon, and at any point in the game, press ENTER to",
        295,
        511);
    g2d.drawString(
        "to initiate a pause screen with a restart, main menu and exit button.", 295, 531);

    // CLOSING
    g2d.setFont(new Font("Courier", Font.BOLD, 24));
    g2d.drawString("CLOSING:", 100, 580);
    g2d.setFont(new Font("Courier", Font.BOLD, 18));
    g2d.drawString(
        "Remember, don't be afraid to pause the game and read over the full game manual", 295, 581);
    g2d.drawString(
        "to refresh your knowledge or to gain any further information that is not covered",
        295,
        601);
    g2d.drawString("here. That's all soldier, good luck on your mission.", 295, 621);
  }

  /** Loads aliens into the Set for the current spawnLevel */
  private void loadAliens() {
    this.aliens = new HashSet<>(AlienLevelFactory.createAlienLevel(this.spawnLevel));
  }

  // -------------------------MAIN METHOD-----------------------------------------

  public static void main(String[] args) {

    Game game = new Game();
    game.timer.start();
  }
}
