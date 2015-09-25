package com.filip.edge.util;

import com.badlogic.gdx.graphics.Color;

public class Constants
{
    public static boolean DEBUG_BUILD = true;

    // Game preferences file
    public static final String PREFERENCES = "theedge.prefs";

    // iOS Leaderboard
    public static final String IOS_LEADERBOARD_ID = "1212";

    // Shader
    public static final String shaderMonochromeVertex = "shaders/monochrome.vs";
    public static final String shaderMonochromeFragment = "shaders/monochrome.fs";

    public static final int DIGIT_WIDTH_CELLS = 5;
    public static final int DIGIT_HEIGHT_CELLS = 9;

    public static final float DIGIT_ASPECT_RATIO = DIGIT_HEIGHT_CELLS / (float)DIGIT_WIDTH_CELLS; // 1.8 = 9(height) / 5(width)

    public static final int BALL_RADIUS = 20;
    public static final int END_CIRCLE_RADIUS = 100;
    public static final int INSIDE_CIRCLE_RADIUS = 27;
    public static final int RECTANGLE_WIDTH = 50;
    public static final float END_CIRCLE_OUTLINE_RADIUS_MULTIPLIER = 0.7f;
    public static final int MAX_LEVELS = 2;
    public static final float[] LEVEL_MULTIPLIERS = {1.5f, 0.75f};

    public static final Color BLUE = new Color(0x00 / 255.0f, 0xa0 / 255.0f, 0xda / 255.0f, 0xff / 255.0f);
    public static final Color GREEN = new Color(20 / 255.0f, 201 / 255.0f, 113 / 255.0f, 0xff / 255.0f);
    public static final Color YELLOW = new Color(244 / 255.0f, 206 / 255.0f, 58 / 255.0f, 0xff / 255.0f);
    public static final Color WHITE = new Color(240 / 255.0f, 244 / 255.0f, 244 / 255.0f, 0xff / 255.0f);
    public static final Color ORANGE = new Color(233 / 255.0f, 127 / 255.0f, 24 / 255.0f, 0xff / 255.0f);
    public static final Color PURPLE = new Color(178 / 255.0f, 127 / 255.0f, 198 / 255.0f, 0xff / 255.0f);
    public static final Color GREY = new Color(150 / 255.0f, 166 / 255.0f, 167 / 255.0f, 0xff / 255.0f);
    public static final Color TURQUOISE = new Color(61 / 255.0f, 200 / 255.0f, 177 / 255.0f, 0xff / 255.0f);
    public static final Color RED = new Color(238 / 255.0f, 115 / 255.0f, 98 / 255.0f, 0xff / 255.0f);
    public static final Color BLACK = new Color(47 / 255.0f, 72 / 255.0f, 94 / 255.0f, 0xff / 255.0f);
    public static final Color TRANSPARENT = new Color(0 / 255.0f, 0 / 255.0f, 0 / 255.0f, 0 / 255.0f);

    public static final Color[] ZONE_COLORS = { BLUE, ORANGE, PURPLE, GREY};

    public static long MAX_SCORE = 1000000000000000000L;

    public static float BOX2D_SCALE = 10;

    public static int WIDTH_IN_PIXELS = 3;
}
