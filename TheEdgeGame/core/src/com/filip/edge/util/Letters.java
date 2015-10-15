package com.filip.edge.util;

import com.badlogic.gdx.graphics.Color;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;

/**
 * Created by fkrstevski on 2015-09-20.
 */
class LetterA extends AbstractRectangleButtonObject {
    public static final String TAG = LetterA.class.getSimpleName();

    public LetterA(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterB extends AbstractRectangleButtonObject {
    public static final String TAG = LetterB.class.getSimpleName();

    public LetterB(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);


        fillTopRight80Rect(Constants.WIDTH_IN_PIXELS);
        fillBottomRight80Rect(Constants.WIDTH_IN_PIXELS);
        fillMiddle60Rect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterC extends AbstractRectangleButtonObject {
    public static final String TAG = LetterC.class.getSimpleName();

    public LetterC(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterD extends AbstractRectangleButtonObject {
    public static final String TAG = LetterD.class.getSimpleName();

    public LetterD(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopMiddle60Rect(Constants.WIDTH_IN_PIXELS);
        fillBottomMiddle60Rect(Constants.WIDTH_IN_PIXELS);
        fillRight80Rect(Constants.WIDTH_IN_PIXELS);


    }
}

class LetterE extends AbstractRectangleButtonObject {
    public static final String TAG = LetterE.class.getSimpleName();

    public LetterE(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterF extends AbstractRectangleButtonObject {
    public static final String TAG = LetterF.class.getSimpleName();

    public LetterF(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);

        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterG extends AbstractRectangleButtonObject {
    public static final String TAG = LetterG.class.getSimpleName();

    public LetterG(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterH extends AbstractRectangleButtonObject {
    public static final String TAG = LetterH.class.getSimpleName();

    public LetterH(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterI extends AbstractRectangleButtonObject {
    public static final String TAG = LetterI.class.getSimpleName();

    public LetterI(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillCenterTop(Constants.WIDTH_IN_PIXELS);
        fillCenterBottom(Constants.WIDTH_IN_PIXELS);
        fillBottomRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterJ extends AbstractRectangleButtonObject {
    public static final String TAG = LetterJ.class.getSimpleName();

    public LetterJ(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterK extends AbstractRectangleButtonObject {
    public static final String TAG = LetterK.class.getSimpleName();

    public LetterK(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRight80Rect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRight80Rect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillMiddle60Rect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterL extends AbstractRectangleButtonObject {
    public static final String TAG = LetterL.class.getSimpleName();

    public LetterL(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterM extends AbstractRectangleButtonObject {
    public static final String TAG = LetterM.class.getSimpleName();

    public LetterM(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS + 1);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS + 1);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS + 1);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS + 1);
        fillCenterTop(Constants.WIDTH_IN_PIXELS + 1);
        fillCenterBottom(Constants.WIDTH_IN_PIXELS + 1);
    }
}

class LetterN extends AbstractRectangleButtonObject {
    public static final String TAG = LetterN.class.getSimpleName();

    public LetterN(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterO extends AbstractRectangleButtonObject {
    public static final String TAG = LetterO.class.getSimpleName();

    public LetterO(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
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

class LetterP extends AbstractRectangleButtonObject {
    public static final String TAG = LetterP.class.getSimpleName();

    public LetterP(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterQ extends AbstractRectangleButtonObject {
    public static final String TAG = LetterQ.class.getSimpleName();

    public LetterQ(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
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
        fillBottomRightSquare(Constants.WIDTH_IN_PIXELS + 1);
    }
}

class LetterR extends AbstractRectangleButtonObject {
    public static final String TAG = LetterR.class.getSimpleName();

    public LetterR(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopRight80Rect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRight80Rect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillMiddle60Rect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterS extends AbstractRectangleButtonObject {
    public static final String TAG = LetterS.class.getSimpleName();

    public LetterS(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
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

class LetterT extends AbstractRectangleButtonObject {
    public static final String TAG = LetterT.class.getSimpleName();

    public LetterT(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillCenterTop(Constants.WIDTH_IN_PIXELS);
        fillCenterBottom(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterU extends AbstractRectangleButtonObject {
    public static final String TAG = LetterU.class.getSimpleName();

    public LetterU(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterV extends AbstractRectangleButtonObject {
    public static final String TAG = LetterV.class.getSimpleName();

    public LetterV(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillBottomCenterThirdRect(Constants.WIDTH_IN_PIXELS);
        fillRightTop90Rect(Constants.WIDTH_IN_PIXELS);
        fillLeftTop90Rect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterW extends AbstractRectangleButtonObject {
    public static final String TAG = LetterW.class.getSimpleName();

    public LetterW(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRightRect(Constants.WIDTH_IN_PIXELS + 1);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS + 1);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS + 1);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS + 1);
        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillCenterTop(Constants.WIDTH_IN_PIXELS + 1);
        fillCenterBottom(Constants.WIDTH_IN_PIXELS + 1);
    }
}

class LetterX extends AbstractRectangleButtonObject {
    public static final String TAG = LetterX.class.getSimpleName();

    public LetterX(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRight80Rect(Constants.WIDTH_IN_PIXELS);
        fillTopLeft80Rect(Constants.WIDTH_IN_PIXELS);
        fillBottomRight80Rect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeft80Rect(Constants.WIDTH_IN_PIXELS);
        fillMiddle60Rect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterY extends AbstractRectangleButtonObject {
    public static final String TAG = LetterY.class.getSimpleName();

    public LetterY(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);
        fillMiddleRect(Constants.WIDTH_IN_PIXELS);
        fillCenterBottom(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterZ extends AbstractRectangleButtonObject {
    public static final String TAG = LetterZ.class.getSimpleName();

    public LetterZ(float width, float height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor, true, TAG);
    }

    @Override
    public void fillInside(float width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopRight80Rect(Constants.WIDTH_IN_PIXELS);

        fillMiddle60Rect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
    }
}
