package com.filip.edge.util;

import com.badlogic.gdx.graphics.Color;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;

/**
 * Created by fkrstevski on 2015-09-21.
 */

class Digit0 extends AbstractRectangleButtonObject {
    public static final String TAG = Digit0.class.getSimpleName();

    public Digit0(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);

    }
}


class Digit1 extends AbstractRectangleButtonObject {
    public static final String TAG = Digit1.class.getSimpleName();

    public Digit1(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillCenterTop(Constants.WIDTH_IN_PIXELS);
        fillCenterBottom(Constants.WIDTH_IN_PIXELS);
    }
}

class Digit2 extends AbstractRectangleButtonObject {
    public static final String TAG = Digit2.class.getSimpleName();

    public Digit2(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);

    }
}

class Digit3 extends AbstractRectangleButtonObject {
    public static final String TAG = Digit3.class.getSimpleName();

    public Digit3(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);

    }
}

class Digit4 extends AbstractRectangleButtonObject {
    public static final String TAG = Digit4.class.getSimpleName();

    public Digit4(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}

class Digit5 extends AbstractRectangleButtonObject {
    public static final String TAG = Digit5.class.getSimpleName();

    public Digit5(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);

    }
}

class Digit6 extends AbstractRectangleButtonObject {
    public static final String TAG = Digit6.class.getSimpleName();

    public Digit6(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);

    }
}

class Digit7 extends AbstractRectangleButtonObject {
    public static final String TAG = Digit7.class.getSimpleName();

    public Digit7(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}


class Digit8 extends AbstractRectangleButtonObject {
    public static final String TAG = Digit8.class.getSimpleName();

    public Digit8(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);

    }
}

class Digit9 extends AbstractRectangleButtonObject {
    public static final String TAG = Digit9.class.getSimpleName();

    public Digit9(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}