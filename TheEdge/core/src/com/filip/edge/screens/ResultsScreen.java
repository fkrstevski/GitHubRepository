package com.filip.edge.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.filip.edge.game.WorldController;
import com.filip.edge.game.WorldRenderer;
import com.filip.edge.util.Constants;
import com.filip.edge.util.GamePreferences;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

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
    private Label lblScore;
    private boolean scoreSubmitted;


    private int btnSubmitWidth;
    private int btnSubmitHeight;
    private int txtEmailWidth;
    private int txtEmailHeight;
    private int lblScoreWidth;
    private int lblScoreHeight;

    public ResultsScreen(DirectedGame game) {
        super(game);
        this.game = game;
    }

    @Override
    public void render(float deltaTime)
    {

        // Sets the clear screen color
        Gdx.gl.glClearColor(Constants.ZONE_COLORS[GamePreferences.instance.zone].r,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].g,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].b,
                Constants.ZONE_COLORS[GamePreferences.instance.zone].a);

        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(deltaTime);
        stage.draw();

        if(scoreSubmitted == true) {
            GamePreferences.instance.scoreNeedsToBeSubmitted = false;
            GamePreferences.instance.currentScore = Constants.MAX_SCORE;
            GamePreferences.instance.save();
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void show()
    {
        GamePreferences.instance.load();

        this.stage = new Stage();

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        final TextButton btnSubmit = new TextButton("SUBMIT", skin);
        btnSubmitWidth = (int)(Gdx.graphics.getWidth() * 0.3);
        btnSubmitHeight = (int)(Gdx.graphics.getWidth() * 0.05);

        btnSubmit.setPosition(Gdx.graphics.getWidth() / 2 - btnSubmitWidth / 2, Gdx.graphics.getHeight() / 2);
        btnSubmit.setSize(btnSubmitWidth, btnSubmitHeight);

        btnSubmit.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btnSubmitClicked();
            }
        });

        //txtEmail = new TextField("Enter your email:", skin);
        txtEmail = new TextField("fk@gm.ca", skin);
        txtEmailWidth = (int)(Gdx.graphics.getWidth() * 0.3);
        txtEmailHeight = (int)(Gdx.graphics.getWidth() * 0.05);
        txtEmail.setPosition(Gdx.graphics.getWidth() / 2 - txtEmailWidth / 2, (int)(Gdx.graphics.getHeight() / 2 + txtEmailHeight * 1.5));
        txtEmail.setSize(txtEmailWidth, txtEmailHeight);

        txtEmail.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //txtEmail.setText("");
                return true;
            }
        });

        txtEmail.setTextFieldListener(new TextField.TextFieldListener() {
            public void keyTyped(TextField textField, char c) {
                String email = textField.getText();
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

        lblScore = new Label("THE END - Score: " + GamePreferences.instance.currentScore, skin);
        lblScoreWidth = (int)(Gdx.graphics.getWidth() * 0.3);
        lblScoreHeight = (int)(Gdx.graphics.getWidth() * 0.05);
        lblScore.setPosition(Gdx.graphics.getWidth() / 2 - lblScoreWidth / 2, (int) (Gdx.graphics.getHeight() / 2 + lblScoreHeight * 3));
        lblScore.setSize(lblScoreWidth, lblScoreHeight);


        this.stage.addActor(lblScore);
        this.stage.addActor(txtEmail);
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
            public void cancelled ()
            {

            }
        });
    }

    @Override
    public void hide()
    {
        stage.dispose();
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {
        super.resume();
    }

    @Override
    public InputProcessor getInputProcessor()
    {
        return stage;
    }
}
