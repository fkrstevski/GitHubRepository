package com.filip.theedge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;

public class BaseScene extends ScreenAdapter implements InputProcessor{
	protected TheEdge game;
	private boolean keyHandled;
	public BaseScene(TheEdge thrustCopter) {
		game=thrustCopter;
		keyHandled=false;
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
	}
	@Override
	public void render(float delta) {
		super.render(delta);
		if(Gdx.input.isKeyPressed(Keys.BACK)){
			if(keyHandled){
				return;
			}
			handleBackPress();
			keyHandled=true;
		}else{
			keyHandled=false;
		}
	}
	protected void handleBackPress() {
		System.out.println("back");
	}

    @Override
    public boolean keyDown (int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp (int keycode)
    {
        return false;
    }

    @Override
    public boolean keyTyped (char character)
    {
        return false;
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved (int screenX, int screenY)
    {
        return false;
    }


    public boolean scrolled (int amount)
    {
        return false;
    }
}
