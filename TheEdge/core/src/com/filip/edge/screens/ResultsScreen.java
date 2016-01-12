package com.filip.edge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.filip.edge.util.Constants;
import com.filip.edge.util.DigitRenderer;
import com.filip.edge.util.GamePreferences;
import com.filip.edge.util.ScreenshotFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fkrstevski on 2015-09-24.
 */
public class ResultsScreen extends AbstractGameScreen {
    private DirectedGame game;
    private Stage stage;
    private TextField txtEmail;
    private String score;
    private boolean scoreSubmitted;
    private String email;
    private boolean displayError;
    private String error;
    private static final String ENTER_EMAIL = "ENTER YOUR EMAIL:";

    private TextButton btnSubmitOutside;
    private TextButton btnSubmit;
    private TextButton.TextButtonStyle textOutsideButtonStyle;
    private TextButton.TextButtonStyle textOutsideButtonStyleRed;
    private TextButton.TextButtonStyle textInsideButtonStyle;

    private OrthographicCamera camera;

    // Used to correctly get the unoptimized framebuffer for iOS
    // It used to give a screenshot of just a white image on iOS
    private FrameBuffer buffer;

    private int btnSubmitWidth;
    private int btnSubmitHeight;
    private int txtEmailWidth;
    private int txtEmailHeight;

    public ResultsScreen(DirectedGame game) {
        super(game);
        this.game = game;
        this.email = ENTER_EMAIL;
        this.displayError = false;
        buffer = new FrameBuffer(Pixmap.Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    @Override
    public void render(float deltaTime) {
        if (ScreenshotFactory.needsToGetScreenshot()) {
            buffer.begin();
        }

        // Sets the clear screen color
        Gdx.gl.glClearColor(Constants.ZONE_COLORS[GamePreferences.instance.zone].r,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].g,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].b,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].a);

        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(deltaTime);
        stage.draw();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        DigitRenderer.instance.renderStringCentered("THE END", (int) (Gdx.graphics.getHeight() * 0.1), game.batch, 1);
        DigitRenderer.instance.renderStringCentered("SUBMIT", (int) (Gdx.graphics.getHeight() / 1.5), game.batch, 1);

        score = "" + GamePreferences.instance.currentScore;
        int scoreLength = score.length() * DigitRenderer.instance.digitWidth;
        DigitRenderer.instance.renderNumber(GamePreferences.instance.currentScore, (int) (Gdx.graphics.getWidth() / 2 + scoreLength / 2), (int) (Gdx.graphics.getHeight() * 0.2), game.batch);

        if (this.displayError) {
            DigitRenderer.instance.renderStringCentered(error, Gdx.graphics.getHeight() -
                    DigitRenderer.instance.digitHeight / 2 -
                    DigitRenderer.instance.digitWidth / Constants.WIDTH_IN_PIXELS, game.batch, 1);
        }

        game.batch.setShader(null);
        game.batch.end();

        if (ScreenshotFactory.needsToGetScreenshot()) {
            ScreenshotFactory.saveScreenshot();
            buffer.end();

            game.batch.begin();
            game.batch.draw(buffer.getColorBufferTexture(), 0, 0);
            game.batch.end();
        }

        if (scoreSubmitted == true) {
            GamePreferences.instance.reset();
            GamePreferences.instance.save();
            game.setScreen(new MenuScreen(game, false));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.setToOrtho(true); // flip y-axis
        camera.update();

        //GamePreferences.instance.load();

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

        textOutsideButtonStyleRed = new TextButton.TextButtonStyle();
        textOutsideButtonStyleRed.up = skin.newDrawable("white", Constants.RED);
        textOutsideButtonStyleRed.down = skin.newDrawable("white", Constants.RED);
        textOutsideButtonStyleRed.checked = skin.newDrawable("white", Constants.RED);
        textOutsideButtonStyleRed.over = skin.newDrawable("white", Constants.RED);
        textOutsideButtonStyleRed.font = skin.getFont("default");
        skin.add("defaultOutsideRed", textOutsideButtonStyleRed);

        textInsideButtonStyle = new TextButton.TextButtonStyle();
        Color insideColor = new Color(Constants.ZONE_COLORS[GamePreferences.instance.zone].r,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].g,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].b,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].a);
        textInsideButtonStyle.up = skin.newDrawable("white", insideColor);
        textInsideButtonStyle.down = skin.newDrawable("white", insideColor);
        textInsideButtonStyle.checked = skin.newDrawable("white", insideColor);
        textInsideButtonStyle.over = skin.newDrawable("white", insideColor);
        textInsideButtonStyle.font = skin.getFont("default");
        skin.add("defaultInside", textInsideButtonStyle);

        TextField.TextFieldStyle tStyle = new TextField.TextFieldStyle();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/UnscreenMK.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Gdx.graphics.getWidth() / 20;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        tStyle.background = skin.newDrawable("white", Constants.WHITE);
        tStyle.cursor = skin.newDrawable("white", insideColor);
        tStyle.cursor.setMinWidth(2f);
        tStyle.selection = skin.newDrawable("white", 0.5f, 0.5f, 0.5f, 0.5f);
        tStyle.font = font;
        tStyle.fontColor = insideColor;
        skin.add("default", tStyle);

        btnSubmitOutside = new TextButton("", skin, "defaultOutside");
        btnSubmitWidth = (int) (Gdx.graphics.getWidth() * 0.31);
        btnSubmitHeight = (int) (Gdx.graphics.getHeight() * 0.16);
        btnSubmitOutside.setPosition(Gdx.graphics.getWidth() / 2 - btnSubmitWidth / 2, Gdx.graphics.getHeight() / 3 - btnSubmitHeight / 2);
        btnSubmitOutside.setSize(btnSubmitWidth, btnSubmitHeight);

        btnSubmit = new TextButton("", skin, "defaultInside");
        btnSubmitWidth = (int) (Gdx.graphics.getWidth() * 0.3);
        btnSubmitHeight = (int) (Gdx.graphics.getHeight() * 0.15);
        btnSubmit.setPosition(Gdx.graphics.getWidth() / 2 - btnSubmitWidth / 2, Gdx.graphics.getHeight() / 3 - btnSubmitHeight / 2);
        btnSubmit.setSize(btnSubmitWidth, btnSubmitHeight);
        btnSubmit.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btnSubmitClicked();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        if (GamePreferences.instance.email.isEmpty()) {
            txtEmail = new TextField(ENTER_EMAIL, skin);
        } else {
            txtEmail = new TextField(GamePreferences.instance.email, skin);
        }
        //txtEmail = new TextField(this.email, skin);
        txtEmailWidth = (int) (Gdx.graphics.getWidth() * 0.95);
        txtEmailHeight = (int) (Gdx.graphics.getWidth() * 0.08);
        txtEmail.setPosition(Gdx.graphics.getWidth() / 2 - txtEmailWidth / 2, (int) (Gdx.graphics.getHeight() / 2.4));
        txtEmail.setSize(txtEmailWidth, txtEmailHeight);
        txtEmail.setCursorPosition(5);

        txtEmail.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (txtEmail.getText().compareToIgnoreCase(ENTER_EMAIL) == 0) {
                    txtEmail.setText("");
                    email = "";
                }
                return true;
            }
        });

        txtEmail.setTextFieldListener(new TextField.TextFieldListener() {
            public void keyTyped(TextField textField, char c) {
                email = textField.getText();
                Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
                Matcher m = p.matcher(email);
                boolean matchFound = m.matches();
                if (matchFound) {
                    btnSubmit.setTouchable(Touchable.enabled);
                    displayError = false;
                    btnSubmitOutside.setStyle(textOutsideButtonStyle);
                    btnSubmit.setStyle(textInsideButtonStyle);
                } else {
                    btnSubmit.setTouchable(Touchable.disabled);
                    displayError = true;
                    btnSubmitOutside.setStyle(textOutsideButtonStyleRed);
                    btnSubmit.setStyle(textOutsideButtonStyleRed);
                    error = "INVALID EMAIL";
                }
            }
        });

        this.stage.addActor(txtEmail);
        this.stage.addActor(btnSubmitOutside);
        this.stage.addActor(btnSubmit);
    }

    public void btnSubmitClicked() {
        if (txtEmail.getText().compareToIgnoreCase(ENTER_EMAIL) == 0) {
            btnSubmit.setTouchable(Touchable.disabled);
            displayError = true;
            btnSubmitOutside.setStyle(textOutsideButtonStyleRed);
            btnSubmit.setStyle(textOutsideButtonStyleRed);
            error = "PLEASE ENTER YOUR EMAIL";
        } else {
            btnSubmit.setTouchable(Touchable.disabled);
            displayError = false;
            btnSubmitOutside.setStyle(textOutsideButtonStyle);
            btnSubmit.setStyle(textInsideButtonStyle);
            GamePreferences.instance.getUserID();
            if (!GamePreferences.instance.userID.isEmpty()) {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("userID", "" + GamePreferences.instance.userID);
                parameters.put("email", txtEmail.getText());
                parameters.put("score", "" + GamePreferences.instance.currentScore);
                parameters.put("tries", GamePreferences.instance.tries);
                parameters.put("times", GamePreferences.instance.times);
                parameters.put("tweets", "" + GamePreferences.instance.tweetsMade);
                parameters.put("ads", "" + GamePreferences.instance.adsSuccessfullyWatched);
                parameters.put("extraData", "data from game");
                parameters.put("version", "" + Constants.GAME_VERSION);
                parameters.put("isProduction", "" + Constants.PRODUCTION);
                Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
                request.setUrl("https://secure.bluehost.com/~alimalim/absolutegames/TheEdgeSubmitScore.php");

                request.setContent(HttpParametersUtils.convertHttpParameters(parameters));
                request.setHeader("Content-Type", "application/x-www-form-urlencoded");

                Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        Gdx.app.log("Status code ", "" + httpResponse.getStatus().getStatusCode());
                        Gdx.app.log("Result ", httpResponse.getResultAsString());

                        btnSubmit.setTouchable(Touchable.enabled);

                        if (httpResponse.getStatus().getStatusCode() == 200) {
                            scoreSubmitted = true;
                            GamePreferences.instance.email = txtEmail.getText();
                            GamePreferences.instance.save();
                        } else {
                            displayError = true;
                            btnSubmitOutside.setStyle(textOutsideButtonStyleRed);
                            btnSubmit.setStyle(textOutsideButtonStyleRed);
                            error = "SUBMISSION ERROR";
                        }
                    }

                    @Override
                    public void failed(Throwable t) {
                        Gdx.app.error("Failed ", t.getMessage());
                        btnSubmit.setTouchable(Touchable.enabled);
                        displayError = true;
                        btnSubmitOutside.setStyle(textOutsideButtonStyleRed);
                        btnSubmit.setStyle(textOutsideButtonStyleRed);
                        error = "NO CONNECTION";
                    }

                    @Override
                    public void cancelled() {
                        btnSubmit.setTouchable(Touchable.enabled);
                        displayError = true;
                        btnSubmitOutside.setStyle(textOutsideButtonStyleRed);
                        btnSubmit.setStyle(textOutsideButtonStyleRed);
                        error = "NO CONNECTION";
                    }
                });
            } else {
                btnSubmit.setTouchable(Touchable.enabled);
                displayError = true;
                btnSubmitOutside.setStyle(textOutsideButtonStyleRed);
                btnSubmit.setStyle(textOutsideButtonStyleRed);
                error = "NO CONNECTION";
            }
        }
    }

    @Override
    public void hide() {
        stage.dispose();
        buffer.dispose();
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
