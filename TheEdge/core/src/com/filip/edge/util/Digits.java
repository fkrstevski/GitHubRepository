package com.filip.edge.util;

import com.badlogic.gdx.graphics.Color;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;

/**
 * Created by fkrstevski on 2015-09-21.
 */

class ZeroDigit extends AbstractRectangleButtonObject {
    public static final String TAG = ZeroDigit.class.getSimpleName();

    public ZeroDigit(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
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


class OneDigit extends AbstractRectangleButtonObject {
    public static final String TAG = OneDigit.class.getSimpleName();

    public OneDigit(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}

class TwoDigit extends AbstractRectangleButtonObject {
    public static final String TAG = TwoDigit.class.getSimpleName();

    public TwoDigit(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
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

class ThreeDigit extends AbstractRectangleButtonObject {
    public static final String TAG = ThreeDigit.class.getSimpleName();

    public ThreeDigit(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
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

class FourDigit extends AbstractRectangleButtonObject {
    public static final String TAG = FourDigit.class.getSimpleName();

    public FourDigit(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
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

class FiveDigit extends AbstractRectangleButtonObject {
    public static final String TAG = FiveDigit.class.getSimpleName();

    public FiveDigit(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
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

class SixDigit extends AbstractRectangleButtonObject {
    public static final String TAG = SixDigit.class.getSimpleName();

    public SixDigit(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
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

class SevenDigit extends AbstractRectangleButtonObject {
    public static final String TAG = SevenDigit.class.getSimpleName();

    public SevenDigit(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}


class EightDigit extends AbstractRectangleButtonObject {
    public static final String TAG = EightDigit.class.getSimpleName();

    public EightDigit(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
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

class NineDigit extends AbstractRectangleButtonObject {
    public static final String TAG = NineDigit.class.getSimpleName();

    public NineDigit(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
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