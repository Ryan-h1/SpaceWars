import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Quadtree {
  private static final int MAX_OBJECTS = 8;
  private static final int MAX_LEVELS = 6;

  private final List<GameObject> objects = new ArrayList<>();
  private final Quadtree[] nodes = new Quadtree[4];

  private final int level;
  private final Rectangle bounds;

  public Quadtree(int level, Rectangle bounds) {
    this.level = level;
    this.bounds = bounds;
  }

  /** Clears the quadtree */
  public void clear() {
    objects.clear();
    for (int i = 0; i < nodes.length; i++) {
      if (nodes[i] != null) {
        nodes[i].clear();
        nodes[i] = null;
      }
    }
  }

  /** Splits a node into four subnodes */
  private void split() {
    int subWidth = (int) (bounds.getWidth() / 2);
    int subHeight = (int) (bounds.getHeight() / 2);
    int x = (int) bounds.getX();
    int y = (int) bounds.getY();

    nodes[0] = new Quadtree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight)); // Top-right
    nodes[1] = new Quadtree(level + 1, new Rectangle(x, y, subWidth, subHeight));            // Top-left
    nodes[2] = new Quadtree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));// Bottom-left
    nodes[3] = new Quadtree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight)); // Bottom-right
  }

  // Determines which node the object belongs to. -1 means it doesn't fit completely in a node.
  private int getIndex(Rectangle2D outerHitBox) {
    int index = -1;
    double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
    double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

    // Object can fit within the top quadrants
    boolean topQuadrant = outerHitBox.getY() < horizontalMidpoint && outerHitBox.getY() + outerHitBox.getHeight() < horizontalMidpoint;
    // Object can fit within the bottom quadrants
    boolean bottomQuadrant = outerHitBox.getY() > horizontalMidpoint;

    // Object can fit within the left quadrants
    if (outerHitBox.getX() < verticalMidpoint && outerHitBox.getX() + outerHitBox.getWidth() < verticalMidpoint) {
      if (topQuadrant) {
        index = 1; // Top-left
      } else if (bottomQuadrant) {
        index = 2; // Bottom-left
      }
    }
    // Object can fit within the right quadrants
    else if (outerHitBox.getX() > verticalMidpoint) {
      if (topQuadrant) {
        index = 0; // Top-right
      } else if (bottomQuadrant) {
        index = 3; // Bottom-right
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

      Iterator<GameObject> iterator = objects.iterator();
      while (iterator.hasNext()) {
        GameObject gameObject = iterator.next();
        int index = getIndex(gameObject.getOuterHitBox());
        if (index != -1) {
          nodes[index].insert(gameObject);
          iterator.remove();  // Remove from current node
        }
      }
    }
  }

  // Retrieve all objects that could collide with the given object
  public List<GameObject> retrieve(Rectangle2D outerHitBox) {
    List<GameObject> foundObjects = new ArrayList<>(objects);
    int index = getIndex(outerHitBox);

    // Retrieve from all relevant nodes if the object spans multiple quadrants
    if (index == -1 && nodes[0] != null) {
      for (Quadtree node : nodes) {
        foundObjects.addAll(node.retrieve(outerHitBox));
      }
    } else if (index != -1 && nodes[0] != null) {
      foundObjects.addAll(nodes[index].retrieve(outerHitBox));
    }

    return foundObjects;
  }
}
