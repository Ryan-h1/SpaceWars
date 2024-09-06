import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Quadtree {
  private static final int MAX_OBJECTS = 4; // Max objects before splitting (4 gives us QUADrants)
  private static final int MAX_LEVELS = 5; // Max depth of the three

  private final List<GameObject> objects = new ArrayList<>();
  private final Quadtree[] nodes = new Quadtree[MAX_OBJECTS];

  private int level;
  private Rectangle bounds;

  public Quadtree(int level, Rectangle bounds) {
    this.level = level;
    this.bounds = bounds;
  }

  /** Clears the quadtree */
  public void clear() {
    objects.clear();
    for (int i = 0; i < MAX_OBJECTS; i++) {
      if (nodes[i] != null) {
        nodes[i].clear();
        nodes[i] = null;
      }
    }
  }

  /** Splits a node into four sub nodes */
  private void split() {
    int subWidth = (int) (bounds.getWidth() / 2);
    int subHeight = (int) (bounds.getHeight() / 2);
    int x = (int) bounds.getX();
    int y = (int) bounds.getY();

    nodes[0] = new Quadtree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight)); // Top right
    nodes[1] = new Quadtree(level + 1, new Rectangle(x, y, subWidth, subHeight));            // Top left
    nodes[2] = new Quadtree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));// Bottom left
    nodes[3] = new Quadtree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight)); // Bottom right
  }

  private int getIndex(Rectangle2D outerHitBox) {
    int index = -1;
    double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
    double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

    boolean topQuadrant = outerHitBox.getY() < horizontalMidpoint && outerHitBox.getY() + outerHitBox.getHeight() < horizontalMidpoint;
    boolean bottomQuadrant = outerHitBox.getY() > horizontalMidpoint;

    if (outerHitBox.getX() < verticalMidpoint && outerHitBox.getX() + outerHitBox.getWidth() < verticalMidpoint) {
      if (topQuadrant) {
        index = 1; // Top left
      } else if (bottomQuadrant) {
        index = 2; // Bottom left
      }
    } else if (outerHitBox.getX() > verticalMidpoint) {
      if (topQuadrant) {
        index = 0; // Top right
      } else if (bottomQuadrant) {
        index = 3; // Bottom right
      }
    }

    return index;
  }

  // Insert object into the quadtree
  public void insert(GameObject obj) {
    if (nodes[0] != null) {
      int index = getIndex(obj.getOuterHitBox());

      if (index != -1) {
        nodes[index].insert(obj);
        return;
      }
    }

    objects.add(obj);

    if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
      if (nodes[0] == null) {
        split();
      }

      int i = 0;
      while (i < objects.size()) {
        int index = getIndex(objects.get(i).getOuterHitBox());
        if (index != -1) {
          nodes[index].insert(objects.remove(i));
        } else {
          i++;
        }
      }
    }
  }

  // Retrieve all objects that could collide with the given object
  public List<GameObject> retrieve(Rectangle2D outerHitBox) {
    int index = getIndex(outerHitBox);
    List<GameObject> foundObjects = new ArrayList<>(objects);

    // If there are child nodes, retrieve from the appropriate one
    if (index != -1 && nodes[0] != null) {
      foundObjects.addAll(nodes[index].retrieve(outerHitBox));
    }

    return foundObjects;
  }
}
