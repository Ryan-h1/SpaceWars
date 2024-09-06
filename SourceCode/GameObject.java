import java.awt.geom.Rectangle2D;

public interface GameObject {
  Rectangle2D getOuterHitBox();
  boolean isVisible();
}
