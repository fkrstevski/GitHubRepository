package com.filip.offthewalls.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
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
        if(worldController.collision)
        {
            // Sets the clear screen color to Blue
            Gdx.gl.glClearColor(Constants.RED.r, Constants.RED.g, Constants.RED.b, Constants.RED.a);

        }
        else
        {
            // Sets the clear screen color to Blue
            Gdx.gl.glClearColor(Constants.BLUE.r, Constants.BLUE.g, Constants.BLUE.b, Constants.BLUE.a);

        }
       // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderTestObjects();
    }

    private void renderTestObjects()
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for(int i = 0; i < worldController.shapes.size(); ++i)
        {
            Shape s = worldController.shapes.get(i);
            if(s.getClass().equals(CircleShape.class))
            {
                CircleShape c = (CircleShape)s;

                // Draw Start Circle
                if(c.getType() == CircleShape.CircleType.First)
                {
                    // Draw Outline
                    shapeRenderer.setColor(Constants.GREY);
                    shapeRenderer.circle(c.getPosition().x, c.getPosition().y, c.getRadius());

                    // Draw Inside
                    shapeRenderer.setColor(Constants.WHITE);
                    shapeRenderer.circle(c.getPosition().x, c.getPosition().y, c.getRadius() * 0.7f);

                }
                // Draw End Circle
                else if(c.getType() == CircleShape.CircleType.Last)
                {
                    // Draw Outline
                    shapeRenderer.setColor(Constants.TURQUOISE);
                    shapeRenderer.circle(c.getPosition().x, c.getPosition().y, c.getRadius());

                    // Draw Inside
                    shapeRenderer.setColor(Constants.WHITE);
                    shapeRenderer.circle(c.getPosition().x, c.getPosition().y, c.getRadius() * 0.7f);

                }
                else // Draw Middle Circle
                {
                    shapeRenderer.setColor(Constants.WHITE);
                    shapeRenderer.circle(c.getPosition().x, c.getPosition().y, c.getRadius());
                }
            }
            else if (s.getClass().equals(RectangleShape.class))
            {
                RectangleShape r = (RectangleShape)s;

                ImmediateModeRenderer renderer = shapeRenderer.getRenderer();
                // Triangle 1
                renderer.color(Constants.WHITE);
                renderer.vertex(r.getPoint(0).x, r.getPoint(0).y, 0);
                renderer.color(Constants.WHITE);
                renderer.vertex(r.getPoint(1).x, r.getPoint(1).y, 0);
                renderer.color(Constants.WHITE);
                renderer.vertex(r.getPoint(2).x, r.getPoint(2).y, 0);

                // Triangle 2
                renderer.color(Constants.WHITE);
                renderer.vertex(r.getPoint(3).x, r.getPoint(3).y, 0);
                renderer.color(Constants.WHITE);
                renderer.vertex(r.getPoint(2).x, r.getPoint(2).y, 0);
                renderer.color(Constants.WHITE);
                renderer.vertex(r.getPoint(1).x, r.getPoint(1).y, 0);

            }

        }

        // Draw ball
        shapeRenderer.setColor(Constants.BLACK);
        shapeRenderer.circle(worldController.getBall().getX(), worldController.getBall().getY(), Constants.BALL_RADIUS);

        shapeRenderer.end();


/*        Vector2 first = worldController.getFirstPoint();
        Vector2 last = worldController.getLastPoint();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.identity();

        // Draw only first and last circles
        shapeRenderer.setColor(Constants.GREY);
        shapeRenderer.circle(first.x, first.y, Constants.END_CIRCLE_RADIUS * worldController.getLevelMultiplier());
        shapeRenderer.setColor(Constants.TURQUOISE);
        shapeRenderer.circle(last.x, last.y, Constants.END_CIRCLE_RADIUS * worldController.getLevelMultiplier());

        shapeRenderer.setColor(Constants.WHITE);
        shapeRenderer.circle(first.x, first.y, Constants.END_CIRCLE_OUTLINE_RADIUS * worldController.getLevelMultiplier());
        shapeRenderer.circle(last.x, last.y, Constants.END_CIRCLE_OUTLINE_RADIUS * worldController.getLevelMultiplier());

        Vector2 tmp = new Vector2();

        // Draw rectangles
        for(int i = 0; i < worldController.getNumberOfPoints() - 1; ++i)
        {
            Vector2 p1 = worldController.getPoint(i);
            Vector2 p2 = worldController.getPoint(i + 1);


            // Old way
            //shapeRenderer.rectLine(p1, p2, Constants.RECTANGLE_WIDTH * worldController.getLevelMultiplier());


            float width = Constants.RECTANGLE_WIDTH * worldController.getLevelMultiplier() * 0.5f;
            Vector2 t = tmp.set(p2.y - p1.y, p1.x - p2.x).nor();
            float tx = t.x * width;
            float ty = t.y * width;

            ImmediateModeRenderer renderer = shapeRenderer.getRenderer();
            // Triangle 1
            renderer.color(Constants.WHITE);
            renderer.vertex(p1.x + tx, p1.y + ty, 0);
            renderer.color(Constants.WHITE);
            renderer.vertex(p1.x - tx, p1.y - ty, 0);
            renderer.color(Constants.WHITE);
            renderer.vertex(p2.x + tx, p2.y + ty, 0);

            // Triangle 2
            renderer.color(Constants.WHITE);
            renderer.vertex(p2.x - tx, p2.y - ty, 0);
            renderer.color(Constants.WHITE);
            renderer.vertex(p2.x + tx, p2.y + ty, 0);
            renderer.color(Constants.WHITE);
            renderer.vertex(p1.x - tx, p1.y - ty, 0);


            // Point 1
            //shapeRenderer.setColor(Constants.TURQUOISE);
            //shapeRenderer.circle(p1.x + tx, p1.y + ty, 10);

            // Point 2
            //shapeRenderer.setColor(Constants.BLACK);
            //shapeRenderer.circle(p1.x - tx, p1.y - ty, 10);

            // Point 3
            //shapeRenderer.setColor(Constants.RED);
            //shapeRenderer.circle(p2.x + tx, p2.y + ty, 10);

            // Point 4
            //shapeRenderer.setColor(Constants.PURPLE);
            //shapeRenderer.circle(p2.x - tx, p2.y - ty, 10);



        }

        // Draw inside circles
        for(int i = 1; i < worldController.getNumberOfPoints() - 1; ++i)
        {
            Vector2 p = worldController.getPoint(i);
            shapeRenderer.circle(p.x, p.y, Constants.INSIDE_CIRCLE_RADIUS * worldController.getLevelMultiplier());
        }

        // Draw ball
        shapeRenderer.setColor(Constants.BLACK);
        shapeRenderer.circle(worldController.getBall().getX(), worldController.getBall().getY(), Constants.BALL_RADIUS);

        shapeRenderer.end();

        */
    }

    public void resize (int width, int height)
    {

    }

    @Override public void dispose ()
    {

    }
}