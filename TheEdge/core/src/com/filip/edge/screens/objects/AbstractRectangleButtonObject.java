package com.filip.edge.screens.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.filip.edge.game.Level;
import com.filip.edge.util.Constants;

/**
 * Created by fkrstevski on 2015-02-12.
 */
public abstract class AbstractRectangleButtonObject extends AbstractButtonObject {
    public static final String TAG = AbstractButtonObject.class.getName();
    public Rectangle bounds;

    public AbstractRectangleButtonObject(float width, float height, float x, float y, Color outsideColor, Color insideColor, boolean shared, String region) {
        super(width, height, x, y, outsideColor, insideColor, shared, region);
    }

    @Override
    protected void init(float width, float height, float x, float y, Color outsideColor, Color insideColor, boolean shared, String region) {
        super.init(width, height, x, y, outsideColor, insideColor, shared, region);
        bounds = new Rectangle();
        bounds.set(position.x - origin.x, position.y - origin.y, dimension.x, dimension.y);
    }

    @Override
    public void fillPixmap(float width, float height, Color outsideColor, Color insideColor) {
        buttonPixmap.setColor(outsideColor);
        buttonPixmap.fill();
        buttonPixmap.setColor(insideColor);

        fillInside(width);
    }

    public abstract void fillInside(float size);

    public boolean isTouched(int x, int y) {
        if (this.bounds.contains(x, y)) {
            return true;
        }
        return false;
    }

    public void fillCenterTop(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(width / 2 - digitPartWidth / 2, height / 2, digitPartWidth, height / 2);
    }

    public void fillCenterBottom(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(width / 2 - digitPartWidth / 2, 0, digitPartWidth, height / 2);
    }

    public void fillTopRect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(0, height - digitPartWidth, width, digitPartWidth);
    }

    public void fillTopLeftRect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(0, height / 2, digitPartWidth, height / 2);
    }

    public void fillTopRightRect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(width - digitPartWidth, height / 2, digitPartWidth, height / 2);
    }

    public void fillMiddleRect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(0, height / 2 - digitPartWidth / 2, width, digitPartWidth);
    }

    public void fillTopRight80Rect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(width - digitPartWidth,
                (int) (height * (Constants.DIGIT_WIDTH_CELLS / (float) Constants.DIGIT_HEIGHT_CELLS)),
                digitPartWidth,
                (int) (height * ((Constants.DIGIT_WIDTH_CELLS - 1) / (float) Constants.DIGIT_HEIGHT_CELLS)));
    }

    public void fillTopLeft80Rect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(0,
                (int) (height * (Constants.DIGIT_WIDTH_CELLS / (float) Constants.DIGIT_HEIGHT_CELLS)),
                digitPartWidth,
                (int) (height * ((Constants.DIGIT_WIDTH_CELLS - 1) / (float) Constants.DIGIT_HEIGHT_CELLS)));
    }

    public void fillBottomRight80Rect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(width - digitPartWidth,
                0,
                digitPartWidth,
                (int) (height * ((Constants.DIGIT_WIDTH_CELLS - 1) / (float) Constants.DIGIT_HEIGHT_CELLS)));
    }

    public void fillBottomLeft80Rect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(0,
                0,
                digitPartWidth,
                (int) (height * ((Constants.DIGIT_WIDTH_CELLS - 1) / (float) Constants.DIGIT_HEIGHT_CELLS)));
    }

    public void fillMiddle60Rect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle((int) (width * 0.2),
                height / 2 - digitPartWidth / 2,
                (int) (width * 0.6),
                digitPartWidth);
    }

    public void fillTopMiddle60Rect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle((int) (width * 0.2),
                height - digitPartWidth,
                (int) (width * 0.6),
                digitPartWidth);
    }

    public void fillBottomMiddle60Rect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle((int) (width * 0.2),
                0,
                (int) (width * 0.6),
                digitPartWidth);
    }

    public void fillRight80Rect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(width - digitPartWidth,
                height / Constants.DIGIT_HEIGHT_CELLS,
                digitPartWidth,
                (int) ((float) height / Constants.DIGIT_HEIGHT_CELLS * 7)); // fill 7 out of 9 pixels
    }

    public void fillRightTop90Rect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(width - digitPartWidth,
                digitPartWidth,
                digitPartWidth,
                (int) ((float) height / Constants.DIGIT_HEIGHT_CELLS * 8)); // fill 8 out of 9 pixels
    }

    public void fillLeftTop90Rect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(0,
                digitPartWidth,
                digitPartWidth,
                (int) ((float) height / Constants.DIGIT_HEIGHT_CELLS * 8)); // fill 8 out of 9 pixels
    }

    public void fillBottomCenterThirdRect(int widthInPixels) {
        int width = (int) (dimension.x);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(digitPartWidth,
                0,
                digitPartWidth,
                digitPartWidth);
    }

    public void fillBottomRightSquare(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(width - digitPartWidth * 2,
                0, digitPartWidth * 2, digitPartWidth * 2);
    }

    public void fillBottomRect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(0, 0, width, digitPartWidth);
    }

    public void fillBottomLeftRect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(0, 0, digitPartWidth, height / 2);
    }

    public void fillBottomRightRect(int widthInPixels) {
        int width = (int) (dimension.x);
        int height = (int) (width * Constants.DIGIT_ASPECT_RATIO);
        int digitPartWidth = width / widthInPixels;
        buttonPixmap.fillRectangle(width - digitPartWidth, 0, digitPartWidth, height / 2);
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof AbstractRectangleButtonObject) {
            AbstractRectangleButtonObject point = ((AbstractRectangleButtonObject) object);
            sameSame = position.equals(point.position) &&
                    dimension.equals(point.dimension);
        }

        return sameSame;
    }

    @Override
    public void reset() {

    }

    @Override
    public String toString() {
        return super.toString() + "AbstractRectangleButtonObject{" +
                "bounds=" + bounds +
                '}';
    }
}