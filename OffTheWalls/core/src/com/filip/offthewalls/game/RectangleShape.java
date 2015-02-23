package com.filip.offthewalls.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by fkrstevski on 2015-02-10.
 */
public class RectangleShape extends Shape
{
    private static final String TAG = RectangleShape.class.getName();

    private Vector2 points[];
    private float width;

    Vector2 tmp = new Vector2();

    public RectangleShape(Vector2 p1, Vector2 p2, float w)
    {
        this.width = w * 0.5f;
        Vector2 t = tmp.set(p2.y - p1.y, p1.x - p2.x).nor();
        float tx = t.x * width;
        float ty = t.y * width;

        points = new Vector2[4];

        points[0] = new Vector2(p1.x + tx, p1.y + ty);
        points[1] = new Vector2(p1.x - tx, p1.y - ty);
        points[2] = new Vector2(p2.x + tx, p2.y + ty);
        points[3] = new Vector2(p2.x - tx, p2.y - ty);
    }

    @Override
    public boolean containsBall(Ball ball, float levelMult)
    {
        Rectangle r = new Rectangle();
        r.setPosition(points[0]);

        float w = Math.abs(points[0].x - points[1].x);
        float h = Math.abs(points[0].y - points[2].y);
        r.setSize(w, h);
        //r.set(points[0].x, points[0].y, w, h);

        Gdx.app.debug("FILIP", "w = " + w + " h = " + h);

        return r.contains(ball.getX(), ball.getY());
    }

    public Vector2 getPoint(int index)
    {
        return points[index];
    }

}
