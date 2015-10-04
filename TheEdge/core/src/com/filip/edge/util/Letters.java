package com.filip.edge.util;

import com.badlogic.gdx.graphics.Color;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;

/**
 * Created by fkrstevski on 2015-09-20.
 */
class LetterA extends AbstractRectangleButtonObject {
    public static final String TAG = LetterA.class.getName();

    public LetterA(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterB extends AbstractRectangleButtonObject {
    public static final String TAG = LetterB.class.getName();

    public LetterB(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
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
    public static final String TAG = LetterC.class.getName();

    public LetterC(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterD extends AbstractRectangleButtonObject {
    public static final String TAG = LetterD.class.getName();

    public LetterD(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopMiddle60Rect(Constants.WIDTH_IN_PIXELS);
        fillBottomMiddle60Rect(Constants.WIDTH_IN_PIXELS);
        fillRight80Rect(Constants.WIDTH_IN_PIXELS);


    }
}

class LetterE extends AbstractRectangleButtonObject {
    public static final String TAG = LetterE.class.getName();

    public LetterE(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterF extends AbstractRectangleButtonObject {
    public static final String TAG = LetterF.class.getName();

    public LetterF(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);

        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterG extends AbstractRectangleButtonObject {
    public static final String TAG = LetterG.class.getName();

    public LetterG(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterH extends AbstractRectangleButtonObject {
    public static final String TAG = LetterH.class.getName();

    public LetterH(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterI extends AbstractRectangleButtonObject {
    public static final String TAG = LetterI.class.getName();

    public LetterI(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillCenterTop(Constants.WIDTH_IN_PIXELS);
        fillCenterBottom(Constants.WIDTH_IN_PIXELS);
        fillBottomRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterJ extends AbstractRectangleButtonObject {
    public static final String TAG = LetterJ.class.getName();

    public LetterJ(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterK extends AbstractRectangleButtonObject {
    public static final String TAG = LetterK.class.getName();

    public LetterK(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRight80Rect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRight80Rect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillMiddle60Rect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterL extends AbstractRectangleButtonObject {
    public static final String TAG = LetterL.class.getName();

    public LetterL(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterM extends AbstractRectangleButtonObject {
    public static final String TAG = LetterM.class.getName();

    public LetterM(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
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
    public static final String TAG = LetterN.class.getName();

    public LetterN(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterO extends AbstractRectangleButtonObject {
    public static final String TAG = LetterO.class.getName();

    public LetterO(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterP extends AbstractRectangleButtonObject {
    public static final String TAG = LetterP.class.getName();

    public LetterP(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterQ extends AbstractRectangleButtonObject {
    public static final String TAG = LetterQ.class.getName();

    public LetterQ(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
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
    public static final String TAG = LetterR.class.getName();

    public LetterR(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopRight80Rect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRight80Rect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
        fillMiddle60Rect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterS extends AbstractRectangleButtonObject {
    public static final String TAG = LetterS.class.getName();

    public LetterS(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);

        fillMiddleRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterT extends AbstractRectangleButtonObject {
    public static final String TAG = LetterT.class.getName();

    public LetterT(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillCenterTop(Constants.WIDTH_IN_PIXELS);
        fillCenterBottom(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterU extends AbstractRectangleButtonObject {
    public static final String TAG = LetterU.class.getName();

    public LetterU(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomRightRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterV extends AbstractRectangleButtonObject {
    public static final String TAG = LetterV.class.getName();

    public LetterV(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillBottomCenterThirdRect(Constants.WIDTH_IN_PIXELS);
        fillRightTop90Rect(Constants.WIDTH_IN_PIXELS);
        fillLeftTop90Rect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterW extends AbstractRectangleButtonObject {
    public static final String TAG = LetterW.class.getName();

    public LetterW(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
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
    public static final String TAG = LetterX.class.getName();

    public LetterX(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRight80Rect(Constants.WIDTH_IN_PIXELS);
        fillTopLeft80Rect(Constants.WIDTH_IN_PIXELS);
        fillBottomRight80Rect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeft80Rect(Constants.WIDTH_IN_PIXELS);
        fillMiddle60Rect(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterY extends AbstractRectangleButtonObject {
    public static final String TAG = LetterY.class.getName();

    public LetterY(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopLeftRect(Constants.WIDTH_IN_PIXELS);
        fillTopRightRect(Constants.WIDTH_IN_PIXELS);
        fillMiddleRect(Constants.WIDTH_IN_PIXELS);
        fillCenterBottom(Constants.WIDTH_IN_PIXELS);
    }
}

class LetterZ extends AbstractRectangleButtonObject {
    public static final String TAG = LetterZ.class.getName();

    public LetterZ(int width, int height, float x, float y, Color outsideColor, Color insideColor) {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRect(Constants.WIDTH_IN_PIXELS);
        fillTopRight80Rect(Constants.WIDTH_IN_PIXELS);

        fillMiddle60Rect(Constants.WIDTH_IN_PIXELS);

        fillBottomRect(Constants.WIDTH_IN_PIXELS);
        fillBottomLeftRect(Constants.WIDTH_IN_PIXELS);
    }
}
