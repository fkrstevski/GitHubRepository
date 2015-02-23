package come.filip.templategame.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import come.filip.templategame.screens.transitions.ScreenTransition;

public abstract class DirectedGame implements ApplicationListener
{

    private boolean init;
    private AbstractGameScreen currScreen;
    private SpriteBatch batch;

    public void setScreen(AbstractGameScreen screen)
    {
        setScreen(screen, null);
    }

    public void setScreen(AbstractGameScreen screen, ScreenTransition screenTransition)
    {
        if (!init)
        {

            batch = new SpriteBatch();
            init = true;
        }
        if (this.currScreen != null) this.currScreen.hide();
        this.currScreen = screen;
        if (this.currScreen != null) {
            this.currScreen.show();
            this.currScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        Gdx.input.setInputProcessor(currScreen.getInputProcessor());
    }

    @Override
    public void render()
    {
        // get delta time and ensure an upper limit of one 60th second
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f / 60.0f);

            // no ongoing transition
            if (currScreen != null)
            {
                currScreen.render(deltaTime);
            }
    }

    @Override
    public void resize(int width, int height)
    {
        if (currScreen != null)
        {
            currScreen.resize(width, height);
        }

    }

    @Override
    public void pause()
    {
        if (currScreen != null)
        {
            currScreen.pause();
        }
    }

    @Override
    public void resume()
    {
        if (currScreen != null)
        {
            currScreen.resume();
        }
    }

    @Override
    public void dispose()
    {
        if (currScreen != null)
        {
            currScreen.hide();
        }
        if (init)
        {
            currScreen = null;
            batch.dispose();
            init = false;
        }
    }

}
