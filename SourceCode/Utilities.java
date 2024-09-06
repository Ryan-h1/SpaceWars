/**
 * Ryan Hecht
 * Mr. Corea
 * ICS4U1-1B
 * First Created: 2020-11-12
 * Last Edited: 2020-11-18
 * 
 * This is a general utilities class. It includes the following static methods:
 * void drawCenteredString(Graphics g, String text, int w, int h, Font font),
 * boolean detectCollision(Shape shapeA, Shape shapeB),
 * String toTicks(int n),
 */


import java.awt.*;
import java.awt.geom.*; 

public final class Utilities {
  
  public Utilities() {}
  
  /*
   * This method draws String text in Font font centered to the passed int w and int h with Graphics g.
   */
  public static void drawCenteredString(Graphics g, String text, int w, int h, Font font) {
    FontMetrics metrics = g.getFontMetrics(font);
    // calculate x coordinate for the string
    int x = (w - metrics.stringWidth(text)) / 2;
    // calculate y coordinate for the string
    int y = (metrics.getAscent() + (h - (metrics.getAscent() + metrics.getDescent())) / 2);
    // draw the string
    g.setFont(font);
    g.drawString(text, x, y);
  }
  
  /* 
   * This method checks if the objects shapeA and shapeB have collided.
   */
  public static boolean detectCollision(Shape shapeA, Shape shapeB) {
    Area areaA = new Area(shapeA);
    areaA.intersect(new Area(shapeB));
    return !areaA.isEmpty();
  }
  
  /*
   * This method accepts an int and returns a String representation in tick marks.
   */
  public static String toTicks(int n) {
    return "|".repeat(Math.max(0, n));
  }
  
}
