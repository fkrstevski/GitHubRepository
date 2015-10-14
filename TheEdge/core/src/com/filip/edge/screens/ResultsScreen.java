package com.filip.edge.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.filip.edge.util.Constants;
import com.filip.edge.util.DigitRenderer;
import com.filip.edge.util.GamePreferences;

import java.util.Date;
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
    private boolean scoreSubmitted;
    private String email;

    private OrthographicCamera camera;

    private int btnSubmitWidth;
    private int btnSubmitHeight;
    private int txtEmailWidth;
    private int txtEmailHeight;

    public ResultsScreen(DirectedGame game) {
        super(game);
        this.game = game;
        this.email = "ENTER YOUR EMAIL";
    }

    @Override
    public void render(float deltaTime) {
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
        //Gdx.app.log(TAG, "Sprite Batch begin");
        game.batch.begin();

        DigitRenderer.instance.renderStringCentered("THE END", (int) (Gdx.graphics.getHeight() * 0.1), game.batch);
        DigitRenderer.instance.renderStringCentered("SUBMIT", (int) (Gdx.graphics.getHeight() / 1.5), game.batch);

        //DigitRenderer.instance.renderStringCentered(email, (int) (Gdx.graphics.getHeight() * 0.35 ), game.batch);

        String score = "" + GamePreferences.instance.currentScore;
        int scoreLength = score.length() * DigitRenderer.instance.digitWidth;
        DigitRenderer.instance.renderNumber(GamePreferences.instance.currentScore, (int) (Gdx.graphics.getWidth() / 2 + scoreLength / 2), (int) (Gdx.graphics.getHeight() * 0.2), game.batch);

        if (scoreSubmitted == true) {
            GamePreferences.instance.scoreNeedsToBeSubmitted = false;
            GamePreferences.instance.currentScore = Constants.MAX_SCORE;
            GamePreferences.instance.save();
            game.setScreen(new MenuScreen(game));
        }

        game.batch.setShader(null);
        game.batch.end();
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

        GamePreferences.instance.load();

        this.stage = new Stage();
        Skin skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA4444);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        skin.add("default", new BitmapFont());

        // Outside button style
        TextButton.TextButtonStyle textOutsideButtonStyle = new TextButton.TextButtonStyle();
        textOutsideButtonStyle.up = skin.newDrawable("white", Constants.WHITE);
        textOutsideButtonStyle.down = skin.newDrawable("white", Constants.WHITE);
        textOutsideButtonStyle.checked = skin.newDrawable("white", Constants.WHITE);
        textOutsideButtonStyle.over = skin.newDrawable("white", Constants.WHITE);
        textOutsideButtonStyle.font = skin.getFont("default");
        skin.add("defaultOutside", textOutsideButtonStyle);

        TextButton.TextButtonStyle textInsideButtonStyle = new TextButton.TextButtonStyle();
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

        final TextButton btnSubmitOutside = new TextButton("", skin, "defaultOutside");
        btnSubmitWidth = (int) (Gdx.graphics.getWidth() * 0.31);
        btnSubmitHeight = (int) (Gdx.graphics.getHeight() * 0.16);
        btnSubmitOutside.setPosition(Gdx.graphics.getWidth() / 2 - btnSubmitWidth / 2, Gdx.graphics.getHeight() / 3 - btnSubmitHeight / 2);
        btnSubmitOutside.setSize(btnSubmitWidth, btnSubmitHeight);

        final TextButton btnSubmit = new TextButton("", skin, "defaultInside");
        btnSubmitWidth = (int) (Gdx.graphics.getWidth() * 0.3);
        btnSubmitHeight = (int) (Gdx.graphics.getHeight() * 0.15);
        btnSubmit.setPosition(Gdx.graphics.getWidth() / 2 - btnSubmitWidth / 2, Gdx.graphics.getHeight() / 3 - btnSubmitHeight / 2);
        btnSubmit.setSize(btnSubmitWidth, btnSubmitHeight);
        btnSubmit.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btnSubmitClicked();
            }
        });


        txtEmail = new TextField("Enter your email:", skin);
        //txtEmail = new TextField(this.email, skin);
        txtEmailWidth = (int) (Gdx.graphics.getWidth() * 0.95);
        txtEmailHeight = (int) (Gdx.graphics.getWidth() * 0.08);
        txtEmail.setPosition(Gdx.graphics.getWidth() / 2 - txtEmailWidth / 2, (int) (Gdx.graphics.getHeight() / 2.4));
        txtEmail.setSize(txtEmailWidth, txtEmailHeight);
        txtEmail.setCursorPosition(5);

        txtEmail.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                txtEmail.setText("");
                email = "";
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
                } else {
                    btnSubmit.setTouchable(Touchable.disabled);
                }
            }

        });

        this.stage.addActor(txtEmail);
        this.stage.addActor(btnSubmitOutside);
        this.stage.addActor(btnSubmit);

        Gdx.input.setCatchBackKey(true);
    }

    public void btnSubmitClicked() {
        Map<String, String> parameters = new HashMap<String, String>();
        Date date = new Date(TimeUtils.millis());
        parameters.put("name", txtEmail.getText());
        parameters.put("score", "" + GamePreferences.instance.currentScore);
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
        request.setUrl("http://www.absolutegames.ca/httptest.php");

        request.setContent(HttpParametersUtils.convertHttpParameters(parameters));
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("Status code ", "" + httpResponse.getStatus().getStatusCode());
                Gdx.app.log("Result ", httpResponse.getResultAsString());
                scoreSubmitted = true;
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("Failed ", t.getMessage());
            }

            @Override
            public void cancelled() {

            }
        });
    }

    @Override
    public void hide() {
        stage.dispose();
        Gdx.input.setCatchBackKey(false);
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
