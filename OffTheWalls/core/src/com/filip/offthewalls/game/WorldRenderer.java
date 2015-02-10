package com.filip.offthewalls.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.filip.offthewalls.util.Constants;

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class WorldRenderer implements Disposable
{
    private static final String TAG = WorldRenderer.class.getName();

    private ShapeRenderer shapeRenderer;
    private WorldController worldController;

    public WorldRenderer (WorldController worldController)
    {
        this.worldController = worldController;
        init();
    }
    private void init ()
    {
        shapeRenderer = new ShapeRenderer();
    }

    public void render ()
    {
        renderTestObjects();
    }

    private void renderTestObjects()
    {

        Vector2 first = worldController.getFirstPoint();
        Vector2 last = worldController.getLastPoint();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);

        // Draw only first and last circles
        shapeRenderer.circle(first.x, first.y, Constants.END_CIRCLE_RADIUS * worldController.getLevelMultiplier());
        shapeRenderer.circle(last.x, last.y, Constants.END_CIRCLE_RADIUS * worldController.getLevelMultiplier());

        // Draw inside circles
        for(int i = 1; i < worldController.getNumberOfPoints() - 1; ++i)
        {
            Vector2 p = worldController.getPoint(i);
            shapeRenderer.circle(p.x, p.y, Constants.INSIDE_CIRCLE_RADIUS * worldController.getLevelMultiplier());
        }

        // Draw rectangles
        for(int i = 0; i < worldController.getNumberOfPoints() - 1; ++i)
        {
            Vector2 p1 = worldController.getPoint(i);
            Vector2 p2 = worldController.getPoint(i + 1);
            shapeRenderer.rectLine(p1, p2, Constants.RECTANGLE_WIDTH * worldController.getLevelMultiplier());
        }

        // Draw ball
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.circle(first.x, first.y, Constants.BALL_RADIUS);


        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Draw Start outline
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(first.x, first.y, Constants.END_CIRCLE_OUTLINE_RADIUS * worldController.getLevelMultiplier());


        // Draw End outline
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(last.x, last.y, Constants.END_CIRCLE_OUTLINE_RADIUS * worldController.getLevelMultiplier());

        shapeRenderer.end();
    }

    public void resize (int width, int height)
    {

    }

    @Override public void dispose ()
    {

    }
}