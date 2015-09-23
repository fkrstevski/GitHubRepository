package com.filip.edge.util;

import com.badlogic.gdx.graphics.Color;
import com.filip.edge.screens.objects.AbstractRectangleButtonObject;

/**
 * Created by fkrstevski on 2015-09-20.
 */
class LetterA extends AbstractRectangleButtonObject
{
    public static final String TAG = LetterA.class.getName();

    public LetterA(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopLeftRect();
        fillTopRightRect();

        fillMiddleRect();
        fillBottomLeftRect();
        fillBottomRightRect();
    }
}

class LetterB extends AbstractRectangleButtonObject{
    public static final String TAG = LetterB.class.getName();

    public LetterB(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopLeftRect();
        fillBottomRect();
        fillBottomLeftRect();


        fillTopRight80Rect();
        fillBottomRight80Rect();
        fillMiddle60Rect();
    }
}

class LetterC extends AbstractRectangleButtonObject{
    public static final String TAG = LetterC.class.getName();

    public LetterC(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopLeftRect();

        fillBottomRect();
        fillBottomLeftRect();
    }
}

class LetterD extends AbstractRectangleButtonObject{
    public static final String TAG = LetterD.class.getName();

    public LetterD(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        //fillTopCenter
    }
}

class LetterE extends AbstractRectangleButtonObject{
    public static final String TAG = LetterE.class.getName();

    public LetterE(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopLeftRect();

        fillMiddleRect();

        fillBottomRect();
        fillBottomLeftRect();
    }
}

class LetterF extends AbstractRectangleButtonObject{
    public static final String TAG = LetterF.class.getName();

    public LetterF(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopLeftRect();

        fillMiddleRect();

        fillBottomLeftRect();
    }
}

class LetterG extends AbstractRectangleButtonObject{
    public static final String TAG = LetterG.class.getName();

    public LetterG(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopLeftRect();

        fillBottomRect();
        fillBottomLeftRect();
        fillBottomRightRect();
    }
}

class LetterH extends AbstractRectangleButtonObject{
    public static final String TAG = LetterH.class.getName();

    public LetterH(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        //fillTopRect();
        fillTopLeftRect();
        fillTopRightRect();

        fillMiddleRect();
        fillBottomLeftRect();
        fillBottomRightRect();
    }
}

class LetterI extends AbstractRectangleButtonObject{
    public static final String TAG = LetterI.class.getName();

    public LetterI(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillCenterTop();
        fillCenterBottom();
        fillBottomRect();
    }
}

class LetterJ extends AbstractRectangleButtonObject{
    public static final String TAG = LetterJ.class.getName();

    public LetterJ(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRightRect();
        fillBottomRightRect();
        fillBottomRect();
    }
}

class LetterK extends AbstractRectangleButtonObject{
    public static final String TAG = LetterK.class.getName();

    public LetterK(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRight80Rect();
        fillTopLeftRect();
        fillBottomRight80Rect();
        fillBottomLeftRect();
        fillMiddle60Rect();
    }
}

class LetterL extends AbstractRectangleButtonObject{
    public static final String TAG = LetterL.class.getName();

    public LetterL(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopLeftRect();
        fillBottomLeftRect();
        fillBottomRect();
    }
}

class LetterM extends AbstractRectangleButtonObject{
    public static final String TAG = LetterM.class.getName();

    public LetterM(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopRightRect();
        fillTopLeftRect();
        fillBottomLeftRect();
        fillBottomRightRect();
        fillCenterTop();
        fillCenterBottom();
    }
}

class LetterN extends AbstractRectangleButtonObject{
    public static final String TAG = LetterN.class.getName();

    public LetterN(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopRightRect();
        fillTopLeftRect();
        fillBottomLeftRect();
        fillBottomRightRect();
    }
}

class LetterO extends AbstractRectangleButtonObject{
    public static final String TAG = LetterO.class.getName();

    public LetterO(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopLeftRect();
        fillTopRightRect();

        fillBottomRect();
        fillBottomLeftRect();
        fillBottomRightRect();
    }
}

class LetterP extends AbstractRectangleButtonObject{
    public static final String TAG = LetterP.class.getName();

    public LetterP(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width) {
        fillTopRect();
        fillTopLeftRect();
        fillTopRightRect();

        fillMiddleRect();
        fillBottomLeftRect();
    }
}

class LetterQ extends AbstractRectangleButtonObject{
    public static final String TAG = LetterQ.class.getName();

    public LetterQ(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopLeftRect();
        fillTopRightRect();

        fillBottomRect();
        fillBottomLeftRect();
        fillBottomRightRect();
    }
}

class LetterR extends AbstractRectangleButtonObject{
    public static final String TAG = LetterR.class.getName();

    public LetterR(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopRight80Rect();
        fillTopLeftRect();
        fillBottomRight80Rect();
        fillBottomLeftRect();
        fillMiddle60Rect();
    }
}

class LetterS extends AbstractRectangleButtonObject{
    public static final String TAG = LetterS.class.getName();

    public LetterS(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopLeftRect();

        fillMiddleRect();

        fillBottomRect();
        fillBottomRightRect();
    }
}

class LetterT extends AbstractRectangleButtonObject{
    public static final String TAG = LetterT.class.getName();

    public LetterT(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillCenterTop();
        fillCenterBottom();
    }
}

class LetterU extends AbstractRectangleButtonObject{
    public static final String TAG = LetterU.class.getName();

    public LetterU(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopLeftRect();
        fillTopRightRect();

        fillBottomRect();
        fillBottomRightRect();
        fillBottomLeftRect();
    }
}

class LetterV extends AbstractRectangleButtonObject{
    public static final String TAG = LetterV.class.getName();

    public LetterV(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {

    }
}

class LetterW extends AbstractRectangleButtonObject{
    public static final String TAG = LetterW.class.getName();

    public LetterW(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRightRect();
        fillTopLeftRect();
        fillBottomRightRect();
        fillBottomLeftRect();
        fillBottomRect();
        fillCenterTop();
        fillCenterBottom();
    }
}

class LetterX extends AbstractRectangleButtonObject{
    public static final String TAG = LetterX.class.getName();

    public LetterX(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRight80Rect();
        fillTopLeft80Rect();
        fillBottomRight80Rect();
        fillBottomLeft80Rect();
        fillMiddle60Rect();
    }
}

class LetterY extends AbstractRectangleButtonObject{
    public static final String TAG = LetterY.class.getName();

    public LetterY(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopLeftRect();
        fillTopRightRect();
        fillMiddleRect();
        fillCenterBottom();
    }
}

class LetterZ extends AbstractRectangleButtonObject{
    public static final String TAG = LetterZ.class.getName();

    public LetterZ(int width, int height, float x, float y, Color outsideColor, Color insideColor)
    {
        super(width, height, x, y, outsideColor, insideColor);
    }

    @Override
    public void fillInside(int width)
    {
        fillTopRect();
        fillTopRightRect();

        fillMiddleRect();

        fillBottomRect();
        fillBottomLeftRect();
    }
}
