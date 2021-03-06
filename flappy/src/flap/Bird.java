package flap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Bird {
  BufferedImage birdImage;
  private float x, y;
  private int w, h;
  private float downSpeed = 0;
  private final float THRUST = -12.5f;
  private final float MIN_DOWN_SPEED = -7.5f;
  private final float MAX_DOWN_SPEED = 10;
  private float gravity = 0.25f;
  private boolean prevUp = false;
  private boolean up = false;
  private boolean gameOver = false;

  public Bird() {
    w = 40;
    h = 40;
    birdImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    x = 50;
    y = Screen.HEIGHT / 2;
  }

  public void update(boolean[] keys) {
    up = keys[KeyEvent.VK_SPACE];
    y += downSpeed;
    downSpeed += gravity;
    if (up && !prevUp) downSpeed += THRUST;
    if (downSpeed >= MAX_DOWN_SPEED) downSpeed = MAX_DOWN_SPEED;
    if (downSpeed <= MIN_DOWN_SPEED) downSpeed = MIN_DOWN_SPEED;
    checkCollision();
    prevUp = up;
  }

  private void checkCollision() {
    if (y <= 0) {
      y = 0;
      downSpeed = 0.1f;
    }
    if (y + h >= Screen.HEIGHT) {
      y = Screen.HEIGHT - h;
      downSpeed = 0;
      gameOver = true;
    }
  }

  public BufferedImage getImage() {
    Graphics2D g2d = (Graphics2D) birdImage.getGraphics();
    RenderingHints rh = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHints(rh);
    g2d.setColor(Color.WHITE);
    g2d.fill(new Ellipse2D.Double(0, 0, w, h));
    return birdImage;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public int getWidth() {
    return w;
  }

  public int getHeight() {
    return h;
  }

  public boolean score(Gap gap) {
    if (!gap.scored && gap.getX() - x < 1) { return true; }
    return false;
  }

  public boolean collide(Gap gap) {
    Ellipse2D birdCircle = new Ellipse2D.Double(x, y, w, h);
    Rectangle2D top = gap.getTop();
    Rectangle2D bot = gap.getBot();
    if (birdCircle.intersects(top.getBounds2D()) ||
        birdCircle.intersects(bot.getBounds2D()))
      return true;
    return false;
  }
}
