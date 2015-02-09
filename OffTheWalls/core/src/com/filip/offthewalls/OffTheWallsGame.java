package com.filip.offthewalls;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class OffTheWallsGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
    ShapeRenderer shapeRenderer;

    ArrayList<Vector2> points;

    int level = 1;
    float levelTime = 2.0f;
    float timePassed = 0;
    float levelMultipliers[] = {2, 1.5f, 1};

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
        shapeRenderer = new ShapeRenderer();

        points = new ArrayList<Vector2>();

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        points.add(new Vector2(width * 0.15f, height * 0.85f));
        points.add(new Vector2(width * 0.85f, height * 0.85f));
        points.add(new Vector2(width * 0.15f, height * 0.15f));
        points.add(new Vector2(width * 0.85f, height * 0.15f));
    }

	@Override
	public void render () {

        timePassed += Gdx.graphics.getDeltaTime();
        if(timePassed > levelTime)
        {
            level++;
            if(level > 3)
                level = 1;

            timePassed = 0;
        }

        Vector2 first = points.get(0);
        Vector2 last = points.get(points.size() - 1);


		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);

        // Draw only first and last circles
        shapeRenderer.circle(first.x, first.y, 50 * levelMultipliers[level - 1]);
        shapeRenderer.circle(last.x, last.y, 50 * levelMultipliers[level - 1]);

        // Draw inside circles
        for(int i = 1; i < points.size() - 1; ++i)
        {
            Vector2 p = points.get(i);
            shapeRenderer.circle(p.x, p.y, 25 * levelMultipliers[level - 1]);
        }

        // Draw rectangles
        for(int i = 0; i < points.size() - 1; ++i)
        {
            Vector2 p1 = points.get(i);
            Vector2 p2 = points.get(i+1);
            shapeRenderer.rectLine(p1, p2, 50 * levelMultipliers[level - 1]);
        }

        // Draw ball
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.circle(first.x, first.y, 20);


        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Draw Start outline
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(first.x, first.y, 35 * levelMultipliers[level - 1]);


        // Draw End outline
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(last.x, last.y, 35 * levelMultipliers[level - 1]);

        shapeRenderer.end();
    }
}
