package com.filip.edge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.filip.edge.screens.objects.BackButton;
import com.filip.edge.screens.objects.ScoreUpdateObject;
import com.filip.edge.screens.objects.ScoreUpdateObjectListener;
import com.filip.edge.util.*;

/**
 * Created by fkrstevski on 2015-12-11.
 */
public class LevelResultsScreen extends AbstractGameScreen implements ScoreUpdateObjectListener {
    private DirectedGame game;
    private Stage stage;

    static final float transitionTime = 60.5f;
    float currentAdTime;
    public boolean colorChange;
    public Color clearColor;

    protected ShaderProgram fontShader;

    private OrthographicCamera camera;

    // Used to correctly get the unoptimized framebuffer for iOS
    // It used to give a screenshot of just a white image on iOS
    private FrameBuffer buffer;

    private TextButton.TextButtonStyle textOutsideButtonStyle;
    private TextButton.TextButtonStyle textButtonStyleTransparent;

    // Tweet Button
    private TextButton btnTweet;
    private int btnTweetWidth;
    private int btnTweetHeight;

    // Video Button
    private TextButton btnVideo;
    private int btnVideoWidth;
    private int btnVideoHeight;

    // Back Button
    public BackButton backButton;
    private TextButton btnBack;
    private int btnBackWidth;
    private int btnBackHeight;

    // Next Button
    private TextButton btnNext;
    private int btnNextWidth;
    private int btnNextHeight;

    private Texture levelBGTexture;
    private int bgScreenshotHeight;
    private int bgScreenshotWidth;

    private Texture levelTexture;
    private boolean gottenScreenshot;

    private ScoreUpdateObject scoreUpdateObject;

    // Custom Strings
    public CustomString customString_levelName;
    public CustomString customString_videoReward;
    public CustomString customString_tweetReward;

    public LevelResultsScreen(DirectedGame game, boolean colorChange) {
        super(game);
        this.game = game;
        this.colorChange = colorChange;
        clearColor = new Color();
        if (this.colorChange) {
            clearColor.set(Constants.ZONE_COLORS[GamePreferences.instance.zone - 1]);
        } else {
            clearColor.set(Constants.ZONE_COLORS[GamePreferences.instance.zone]);
        }

        String vertexShader = Gdx.files.internal("shaders/vertex.glsl").readString();

        String fontFragmentShader = Gdx.files.internal("shaders/fontPixelShader.glsl").readString();
        fontShader = new ShaderProgram(vertexShader, fontFragmentShader);

        buffer = new FrameBuffer(Pixmap.Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        FileHandle file = new FileHandle(Gdx.files.getLocalStoragePath() + Constants.SCREENSHOT_LEVEL);

        levelTexture = new Texture(file);
        gottenScreenshot = false;

        scoreUpdateObject = new ScoreUpdateObject();
        scoreUpdateObject.setListener(this);

        float scale = 1;
        String level = "LEVEL " + GamePreferences.instance.getCurrentLevel();
        this.customString_levelName = new CustomString(level,
                Gdx.graphics.getWidth() * 0.25f - DigitRenderer.instance.digitWidth * 0.5f * scale + level.length() * DigitRenderer.instance.digitWidth * DigitRenderer.instance.EXTRA_SPACING * 0.5f * scale,
                Gdx.graphics.getHeight() * 0.29f,
                1);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.setToOrtho(true); // flip y-axis
        camera.update();

        this.stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    game.setScreen(new MenuScreen(game, false));
                    return true;
                }
                return super.keyDown(keyCode);
            }
        };

        Skin skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        // Store the default libgdx font under the name "default".
        skin.add("default", new BitmapFont());

        // Outside button style
        textOutsideButtonStyle = new TextButton.TextButtonStyle();
        textOutsideButtonStyle.up = skin.newDrawable("white", Constants.WHITE);
        textOutsideButtonStyle.down = skin.newDrawable("white", Constants.WHITE);
        textOutsideButtonStyle.checked = skin.newDrawable("white", Constants.WHITE);
        textOutsideButtonStyle.over = skin.newDrawable("white", Constants.WHITE);
        textOutsideButtonStyle.font = skin.getFont("default");
        skin.add("defaultOutside", textOutsideButtonStyle);

        textButtonStyleTransparent = new TextButton.TextButtonStyle();
        textButtonStyleTransparent.up = skin.newDrawable("white", Constants.TRANSPARENT);
        textButtonStyleTransparent.down = skin.newDrawable("white", Constants.TRANSPARENT);
        textButtonStyleTransparent.checked = skin.newDrawable("white", Constants.TRANSPARENT);
        textButtonStyleTransparent.over = skin.newDrawable("white", Constants.TRANSPARENT);
        textButtonStyleTransparent.font = skin.getFont("default");
        skin.add("transparent", textButtonStyleTransparent);

        btnTweet = new TextButton("", skin, "defaultOutside");
        btnTweetWidth = (int) (Gdx.graphics.getWidth() * 0.32);
        btnTweetHeight = (int) (Gdx.graphics.getHeight() * 0.16);
        btnTweet.setPosition(DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS, DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS);
        btnTweet.setSize(btnTweetWidth, btnTweetHeight);
        btnTweet.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btnTweetClicked();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        btnVideo = new TextButton("", skin, "defaultOutside");
        btnVideoWidth = (int) (Gdx.graphics.getWidth() * 0.32);
        btnVideoHeight = (int) (Gdx.graphics.getHeight() * 0.16);
        btnVideo.setPosition(Gdx.graphics.getWidth() * 0.5f - btnVideoWidth / 2, DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS);
        btnVideo.setSize(btnVideoWidth, btnVideoHeight);
        btnVideo.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btnVideoClicked();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        btnBack = new TextButton("", skin, "transparent");
        btnBackWidth = (int) (Gdx.graphics.getWidth() * 0.1);
        btnBackHeight = (int) (Gdx.graphics.getWidth() * 0.1);
        btnBack.setPosition(Gdx.graphics.getWidth() * 0f, Gdx.graphics.getHeight() * 0.9f);
        btnBack.setSize(btnBackWidth, btnBackHeight);
        btnBack.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MenuScreen(game, false));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        btnNext = new TextButton("", skin, "defaultOutside");
        btnNextWidth = (int) (Gdx.graphics.getWidth() * 0.32);
        btnNextHeight = (int) (Gdx.graphics.getHeight() * 0.16);
        btnNext.setPosition(Gdx.graphics.getWidth() - btnNextWidth - DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS, DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS);
        btnNext.setSize(btnNextWidth, btnNextHeight);
        btnNext.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                GamePreferences.instance.completedLevelTweet = false;
                GamePreferences.instance.completedLevelVideoReward = false;
                GamePreferences.instance.showingLevelResults = false;
                GamePreferences.instance.save();
                game.setScreen(new GameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        bgScreenshotWidth = (int) (Gdx.graphics.getWidth() * 0.5);
        bgScreenshotHeight = (int) (bgScreenshotWidth * Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth());

        Pixmap p = new Pixmap(
                bgScreenshotWidth,
                bgScreenshotHeight,
                Pixmap.Format.RGB888);
        p.setColor(Constants.WHITE);
        p.fill();
        levelBGTexture = new Texture(p);
        p.dispose();

        this.stage.addActor(btnNext);
        this.stage.addActor(btnBack);
        if(!GamePreferences.instance.completedLevelTweet) {
            this.stage.addActor(btnTweet);
        }
        if(!GamePreferences.instance.completedLevelVideoReward && GamePreferences.instance.getCurrentLevel() % 3 == 0) {
            this.stage.addActor(btnVideo);
        }

        // Backbutton
        backButton = new BackButton(Gdx.graphics.getWidth() * 0.05f,   // size
                (int) (Gdx.graphics.getWidth() * 0.03),    // x
                (int) (Gdx.graphics.getWidth() * 0.03),     // y
                Constants.TRANSPARENT,         // outside color
                Constants.WHITE,
                false, "CircleBack" + GamePreferences.instance.zone);      // inside color

        float scale = 1;
        String videoReward = "+" + Constants.VIDEO_REWARD;
        this.customString_videoReward = new CustomString(videoReward,
                btnVideo.getX() + btnVideoWidth / 2 - DigitRenderer.instance.digitWidth * 0.5f * scale + videoReward.length() * DigitRenderer.instance.digitWidth * DigitRenderer.instance.EXTRA_SPACING * 0.5f * scale,
                Gdx.graphics.getHeight() - (int) (btnVideo.getY() + btnVideoHeight * 1.3),
                1);

        String tweetReward = "+" + Constants.TWEET_REWARD;
        this.customString_tweetReward = new CustomString(tweetReward,
                btnTweet.getX() + btnTweetWidth / 2 - DigitRenderer.instance.digitWidth * 0.5f * scale + tweetReward.length() * DigitRenderer.instance.digitWidth * DigitRenderer.instance.EXTRA_SPACING * 0.5f * scale,
                Gdx.graphics.getHeight() - (int) (btnTweet.getY() + btnTweetHeight * 1.3),
                1);
    }

    public void btnTweetClicked() {
        String tweet = "Just beat level "  + GamePreferences.instance.getCurrentLevel() + " #theedgegameALPHA";
        game.showTweetSheet(tweet, Constants.SCREENSHOT_LEVEL_RESULT);
    }

    public void btnVideoClicked() {
        game.showRewardVideoAd();
    }

    public void giveVideoReward(){
        addScoreUpdate(Constants.VIDEO_REWARD);
        GamePreferences.instance.adsSuccessfullyWatched++;
        GamePreferences.instance.completedLevelVideoReward = true;
        GamePreferences.instance.currentScore += Constants.VIDEO_REWARD;
        GamePreferences.instance.save();
        GamePreferences.instance.currentScore -= Constants.VIDEO_REWARD;

        for(Actor actor : stage.getActors()) {
            if(actor.equals(btnVideo)) {
                actor.addAction(Actions.removeActor());
                break;
            }
        }
    }

    public void giveTweetReward(){
        addScoreUpdate(Constants.TWEET_REWARD);
        GamePreferences.instance.tweetsMade++;
        GamePreferences.instance.completedLevelTweet = true;
        GamePreferences.instance.currentScore += Constants.TWEET_REWARD;
        GamePreferences.instance.save();
        GamePreferences.instance.currentScore -= Constants.TWEET_REWARD;

        for(Actor actor : stage.getActors()) {
            if(actor.equals(btnTweet)) {
                actor.addAction(Actions.removeActor());
                break;
            }
        }
    }

    public void addScoreUpdate(int score) {
        int y = (int) (DigitRenderer.instance.digitHeight / 2) +
                DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS;
        int x = (int) (Gdx.graphics.getWidth() - DigitRenderer.instance.digitWidth / 2 - DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS);
        scoreUpdateObject.init(score, x, y + 50, x, y);
    }

    @Override
    public void scoreUpdateObjectFinished(int score) {
        GamePreferences.instance.currentScore += score;
        GamePreferences.instance.save();
    }

    @Override
    public void render(float deltaTime) {

        if(this.scoreUpdateObject.isAlive) {
            scoreUpdateObject.update(deltaTime);
        }

        if (!gottenScreenshot) {
            ScreenshotFactory.getScreenShot(true, Constants.SCREENSHOT_LEVEL_RESULT);
            gottenScreenshot = true;
        }

        if (colorChange) {
            if (GamePreferences.instance.zone >= 0) {
                currentAdTime += deltaTime;
                if (currentAdTime > transitionTime) {
                    currentAdTime = 0;
                    colorChange = false;
                }
                clearColor.lerp(Constants.ZONE_COLORS[GamePreferences.instance.zone], currentAdTime / transitionTime);
            }
        }

        if (ScreenshotFactory.needsToGetScreenshot()) {
            buffer.begin();
        }

        // Sets the clear screen color
        Gdx.gl.glClearColor(clearColor.r,
                clearColor.g,
                clearColor.b,
                clearColor.a);

        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(deltaTime);
        if (!ScreenshotFactory.needsToGetScreenshot()) {
            stage.draw();
        }

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        if (scoreUpdateObject.isAlive == true) {
            game.batch.setShader(fontShader);
            if (scoreUpdateObject.score < 0) {
                fontShader.setUniformf("u_alpha", (1 - scoreUpdateObject.alpha));
                fontShader.setUniformf("u_red", Constants.RED.r);
                fontShader.setUniformf("u_green", Constants.RED.g);
                fontShader.setUniformf("u_blue", Constants.RED.b);
            } else {
                fontShader.setUniformf("u_alpha", (1 - scoreUpdateObject.alpha));
                fontShader.setUniformf("u_red", Constants.GREEN.r);
                fontShader.setUniformf("u_green", Constants.GREEN.g);
                fontShader.setUniformf("u_blue", Constants.GREEN.b);
            }
            DigitRenderer.instance.renderNumber(Math.abs((long) scoreUpdateObject.score), (int) scoreUpdateObject.currentPosition.x, (int) scoreUpdateObject.currentPosition.y, game.batch);
            game.batch.setShader(null);
        }

        int y = (int) (DigitRenderer.instance.digitHeight / 2) +
                DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS;
        int x = (int) (camera.viewportWidth - DigitRenderer.instance.digitWidth / 2 - DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS);

        DigitRenderer.instance.renderNumber(GamePreferences.instance.currentScore, x, y, game.batch);

        this.customString_levelName.render(game.batch);

        DigitRenderer.instance.renderStringAtCenterXPoint("TRIES",
                (int) (btnTweet.getX() + btnTweetWidth / 2),
                (int) (Gdx.graphics.getHeight() * 0.45f),
                game.batch, 1);

        int currentLevel = GamePreferences.instance.getCurrentLevel();
        long tries = 1;

        if (currentLevel == GamePreferences.instance.levelTries.size()) {
            tries = GamePreferences.instance.levelTries.get(currentLevel - 1);
        }

        DigitRenderer.instance.renderNumber(tries,
                (int) (btnTweet.getX() + btnTweetWidth / 2),
                (int) (Gdx.graphics.getHeight() * 0.55f),
                game.batch);

        DigitRenderer.instance.renderStringAtCenterXPoint("TIME",
                (int) (Gdx.graphics.getWidth() * 0.35f),
                (int) (Gdx.graphics.getHeight() * 0.45f),
                game.batch, 1);

        long time = 1;

        if (currentLevel == GamePreferences.instance.levelTimes.size()) {
            time = GamePreferences.instance.levelTimes.get(currentLevel - 1);
        }

        DigitRenderer.instance.renderTime(time,
                (int) (Gdx.graphics.getWidth() * 0.35f),
                (int) (Gdx.graphics.getHeight() * 0.55f),
                game.batch);

        if (!ScreenshotFactory.needsToGetScreenshot()) {
            backButton.render(game.batch);

            game.batch.setShader(fontShader);
            fontShader.setUniformf("u_alpha", 1);
            fontShader.setUniformf("u_red", clearColor.r);
            fontShader.setUniformf("u_green", clearColor.g);
            fontShader.setUniformf("u_blue", clearColor.b);
            DigitRenderer.instance.renderStringAtCenterXPoint("TWEET", (int) (btnTweet.getX() + btnTweetWidth / 2), Gdx.graphics.getHeight() - (int) (btnTweet.getY() + btnTweetHeight / 2), game.batch, 1);
            DigitRenderer.instance.renderStringAtCenterXPoint("NEXT LEVEL", (int) (btnNext.getX() + btnNextWidth / 2), Gdx.graphics.getHeight() - (int) (btnNext.getY() + btnNextHeight / 2), game.batch, 1);
            game.batch.setShader(null);

            if(!GamePreferences.instance.completedLevelVideoReward && GamePreferences.instance.getCurrentLevel() % 3 == 0) {
                game.batch.setShader(fontShader);
                fontShader.setUniformf("u_alpha", 1);
                fontShader.setUniformf("u_red", Constants.RED.r);
                fontShader.setUniformf("u_green", Constants.RED.g);
                fontShader.setUniformf("u_blue", Constants.RED.b);
                DigitRenderer.instance.renderStringAtCenterXPoint("VIDEO", (int) (btnVideo.getX() + btnVideoWidth / 2), Gdx.graphics.getHeight() - (int) (btnVideo.getY() + btnVideoHeight / 2), game.batch, 1);
                game.batch.setShader(null);
            }

            if(!GamePreferences.instance.completedLevelVideoReward && GamePreferences.instance.getCurrentLevel() % 3 == 0) {
                customString_videoReward.render(game.batch);
            }

            if(!GamePreferences.instance.completedLevelTweet) {
                customString_tweetReward.render(game.batch);
            }
        }

        game.batch.draw(levelBGTexture,
                Gdx.graphics.getWidth() - bgScreenshotWidth - DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS,
                Gdx.graphics.getHeight() * 0.5f - bgScreenshotHeight * 0.65f,
                bgScreenshotWidth - (DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS) * 2,
                bgScreenshotHeight - (DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS) * 2);

        game.batch.draw(levelTexture,
                Gdx.graphics.getWidth() - bgScreenshotWidth - DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS + DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS,
                Gdx.graphics.getHeight() * 0.5f - bgScreenshotHeight * 0.65f + DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS,
                bgScreenshotWidth - (DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS) * 4,
                bgScreenshotHeight - (DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS) * 4);

        game.batch.setShader(null);
        game.batch.end();

        if (ScreenshotFactory.needsToGetScreenshot()) {
            ScreenshotFactory.saveScreenshot();
            buffer.end();

            game.batch.begin();
            game.batch.draw(buffer.getColorBufferTexture(), 0, 0);
            backButton.render(game.batch);
            if(!GamePreferences.instance.completedLevelVideoReward && GamePreferences.instance.getCurrentLevel() % 3 == 0) {
                customString_videoReward.render(game.batch);
            }

            if(!GamePreferences.instance.completedLevelTweet) {
                customString_tweetReward.render(game.batch);
            }
            game.batch.end();
        }
    }

    @Override
    public void hide() {
        stage.dispose();
        buffer.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }
}
