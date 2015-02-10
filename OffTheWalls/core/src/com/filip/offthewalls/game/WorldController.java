package com.filip.offthewalls.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by fkrstevski on 2015-02-09.
 */
public class WorldController extends InputAdapter
{
    private static final String TAG = WorldController.class.getName();

    private ArrayList<Vector2> points;

    private int level = 1;
    private float timePassed = 0;
    private float levelMultipliers[] = {2, 1.5f, 1};

    public WorldController ()
    {
        init();
    }

    private void init ()
    {
        Gdx.input.setInputProcessor(this);
        initTestObjects();
    }

    private void initTestObjects()
    {
        points = new ArrayList<Vector2>();

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        points.add(new Vector2(width * 0.15f, height * 0.85f));
        points.add(new Vector2(width * 0.85f, height * 0.85f));
        points.add(new Vector2(width * 0.15f, height * 0.15f));
        points.add(new Vector2(width * 0.85f, height * 0.15f));
    }

    public void update (float deltaTime)
    {

    }

    @Override
    public boolean keyUp (int keycode)
    {
        if (keycode == Input.Keys.SPACE)
        {
            updateLevel();
        }
        return false;
    }

    private void updateLevel()
    {
        level++;
        if(level > 3)
            level = 1;

        Gdx.app.debug(TAG, "Level = " + level);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        updateLevel();
        return false;
    }

    public float getLevelMultiplier() {
        return levelMultipliers[level - 1];
    }

    public int getLevel() {
        return level;
    }

    public Vector2 getPoint(int index)
    {
        return points.get(index);
    }

    public Vector2 getFirstPoint()
    {
        return points.get(0);
    }

    public Vector2 getLastPoint()
    {
        return points.get(points.size() - 1);
    }

    public int getNumberOfPoints()
    {
        return points.size();
    }
}
