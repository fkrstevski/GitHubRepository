package com.filip.edge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import com.filip.edge.game.Assets;
import com.filip.edge.util.Constants;
import com.filip.edge.util.ScreenshotFactory;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public class MainMenuRenderer implements Disposable {
    private static final String TAG = MainMenuRenderer.class.getName();

    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private MainMenuController worldController;

    // Used to correctly get the unoptimized framebuffer for iOS
    // It used to give a screenshot of just a white image on iOS
    private FrameBuffer buffer;

    public MainMenuRenderer(MainMenuController worldController) {
        this.worldController = worldController;
        init();
    }

    private void init() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.setToOrtho(true); // flip y-axis
        camera.update();
        cameraGUI = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();

        buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    public void render(SpriteBatch batch) {
        if (ScreenshotFactory.needsToGetScreenshot()) {
            buffer.begin();
        }
        // Sets the clear screen color
        Gdx.gl.glClearColor(worldController.startingColor.r,
                worldController.startingColor.g,
                worldController.startingColor.b,
                worldController.startingColor.a);

        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderWorld(batch);
        batch.setProjectionMatrix(cameraGUI.combined);
        renderGui(batch);
        batch.setShader(null);
        batch.end();

        if (ScreenshotFactory.needsToGetScreenshot()) {
            ScreenshotFactory.saveScreenshot();
            buffer.end();

            batch.begin();
            batch.draw(buffer.getColorBufferTexture(), 0, 0);
            batch.end();
        }
    }

    private void renderWorld(SpriteBatch batch) {
        worldController.mainMenu.render(batch);
    }

    private void renderGui(SpriteBatch batch) {
        if (Constants.DEBUG_BUILD) {
            renderGuiFpsCounter(batch);
        }
    }

    private void renderGuiFpsCounter(SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 55;
        float y = cameraGUI.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
        if (fps >= 45) {
            // 45 or more FPS show up in green
            fpsFont.setColor(0, 1, 0, 1);
        } else if (fps >= 30) {
            // 30 or more FPS show up in yellow
            fpsFont.setColor(1, 1, 0, 1);
        } else {
            // less than 30 FPS show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }

        fpsFont.draw(batch, " " + fps, x, y);
        fpsFont.setColor(1, 1, 1, 1); // white
    }


    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }
}
