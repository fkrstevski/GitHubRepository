package com.filip.edge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.filip.edge.game.Assets;
import com.filip.edge.util.Constants;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public class MainMenuRenderer implements Disposable
{
    private static final String TAG = MainMenuRenderer.class.getName();

    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private MainMenuController worldController;
    public MainMenuRenderer(MainMenuController worldController)
    {
        this.worldController = worldController;
        init();
    }

    private void init()
    {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.setToOrtho(true); // flip y-axis
        camera.update();
        cameraGUI = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();
    }

    public void render(SpriteBatch batch)
    {
        renderWorld(batch);
        renderGui(batch);
    }

    private void renderWorld(SpriteBatch batch)
    {
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.mainMenu.render(batch);
        batch.setShader(null);
        batch.end();
    }

    private void renderGui(SpriteBatch batch)
    {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();

        if (Constants.DEBUG_BUILD)
        {
            renderGuiFpsCounter(batch);
        }
        batch.end();
    }

    private void renderGuiFpsCounter(SpriteBatch batch)
    {
        float x = cameraGUI.viewportWidth - 55;
        float y = cameraGUI.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
        if (fps >= 45)
        {
            // 45 or more FPS show up in green
            fpsFont.setColor(0, 1, 0, 1);
        }
        else if (fps >= 30)
        {
            // 30 or more FPS show up in yellow
            fpsFont.setColor(1, 1, 0, 1);
        }
        else
        {
            // less than 30 FPS show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }

        fpsFont.draw(batch, " " + fps, x, y);
        fpsFont.setColor(1, 1, 1, 1); // white
    }


    public void resize(int width, int height)
    {

        // TODO: Look at viewport width
        /*camera.viewportWidth = (Gdx.graphics.getHeight() / (float)height) * (float)width;
        camera.update();
        cameraGUI.viewportHeight = Gdx.graphics.getHeight();
        cameraGUI.viewportWidth = (Gdx.graphics.getHeight() / (float)height) * (float)width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();*/
    }

    @Override
    public void dispose()
    {

    }
}
