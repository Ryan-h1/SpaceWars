import java.util.HashSet;
import java.util.Set;

public class AlienLevelFactory {

  public static Set<Alien> createAlienLevel(int gameLevel) {
    Set<Alien> aliens = new HashSet<>();

    switch (gameLevel) {
      case 1:
        for (int row = 0; row < 3; row++) {
          for (int col = 0; col < 10; col++) {
            BasicAlien basicAlien = new BasicAlien((col + 1) * 110, (row + 1) * 100 - 60, 24);
            aliens.add(basicAlien);
          }
        }
        break;
      case 2:
        // first three rows
        for (int row = 0; row < 3; row++) {
          for (int col = 0; col < 10; col++) {
            if (col == row || col == 10 - row) {
              AdvancedAlien advancedAlien =
                  new AdvancedAlien((col + 1) * 110, (row + 1) * 100 - 60, 24);
              aliens.add(advancedAlien);
            } else {
              BasicAlien basicAlien = new BasicAlien((col + 1) * 110, (row + 1) * 100 - 60, 24);
              aliens.add(basicAlien);
            }
          }
        }
        // fourth row
        for (int col = 0; col < 10; col++) {
          AdvancedAlien advancedAlien = new AdvancedAlien((col + 1) * 110, -60 + 400, 24);
          aliens.add(advancedAlien);
        }
        break;
      case 3:
        // first three row
        for (int row = 0; row < 3; row++) {
          for (int col = 0; col < 10; col++) {
            if ((double) (col) % 2 == 0) {
              FastAlien fastAlien = new FastAlien((col + 1) * 110, (row + 1) * 100 - 60, 24);
              aliens.add(fastAlien);
            } else {
              BasicAlien basicAlien = new BasicAlien((col + 1) * 110, (row + 1) * 100 - 60, 24);
              aliens.add(basicAlien);
            }
          }
        }
        // fourth row
        for (int col = 0; col < 10; col++) {
          if ((double) (col) % 2 == 0) {
            FastAlien fastAlien = new FastAlien((col + 1) * 110, -60 + 400, 24);
            aliens.add(fastAlien);
          } else {
            AdvancedAlien advancedAlien = new AdvancedAlien((col + 1) * 110, -60 + 400, 24);
            aliens.add(advancedAlien);
          }
        }
        break;
      case 4:
        // first two rows
        for (int row = 0; row < 2; row++) {
          for (int col = 0; col < 10; col++) {
            if (col == 3 || col == 6) {
              FastAlien fastAlien = new FastAlien((col + 1) * 110, (row + 1) * 100 - 60, 24);
              aliens.add(fastAlien);
            } else {
              AdvancedAlien advancedAlien =
                  new AdvancedAlien((col + 1) * 110, (row + 1) * 100 - 60, 24);
              aliens.add(advancedAlien);
            }
          }
        }
        // third row
        for (int col = 0; col < 3; col++) {
          TankAlien tankAlien = new TankAlien((col + 1) * 420 - 305, 400, 4);
          aliens.add(tankAlien);
        }
        break;
      default:
        throw new IllegalArgumentException("Unsupported alien level: " + gameLevel);
    }

    return aliens;
  }
}
