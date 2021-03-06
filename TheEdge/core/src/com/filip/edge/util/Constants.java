package com.filip.edge.util;

import com.badlogic.gdx.graphics.Color;

public class Constants {
    // Game preferences file
    public static final String PREFERENCES = "theedge.prefs";

    public static final int GAME_VERSION = 1;
    public static final boolean PRODUCTION = false;
    public static boolean DEBUG_BUILD = false;

    // iOS Leaderboard
    public static final String IOS_LEADERBOARD_ID = "1212";

    public static final float BASE_SCREEN_WIDTH = 1334;
    public static final float BASE_SCREEN_HEIGHT = 750;

    public static final int DIGIT_WIDTH_CELLS = 5;
    public static final int DIGIT_HEIGHT_CELLS = 9;

    public static final float DIGIT_ASPECT_RATIO = DIGIT_HEIGHT_CELLS / (float) DIGIT_WIDTH_CELLS; // 1.8 = 9(height) / 5(width)

    public static final float END_TIME = 2.0f;
    public static final int BALL_RADIUS = 20;
    public static final int END_CIRCLE_RADIUS = 50;
    public static final int INSIDE_CIRCLE_RADIUS = 25;
    public static final int PACER_RADIUS = 40;
    public static final int HOLE_RADIUS = 22;
    public static final int OSCILLATOR_RADIUS = 15;
    public static final int ORBITER_PICKUP_RADIUS = 15;
    public static final int GOLD_PICKUP_RADIUS = 5;
    public static final int GOLD_BODY_RADIUS = 25;
    public static final int ORBITER_PICKUP_BODY_RADIUS = 25;
    public static final int ORBITER_OFFSET = 0;
    public static final int ORBITER_ANGULAR_VELOCITY = 300;
    public static final int ORBITER_RADIUS = 30;
    public static final int RECTANGLE_WIDTH = 50;
    public static final float END_CIRCLE_OUTLINE_RADIUS_MULTIPLIER = 0.75f;
    public static final int MAX_LEVELS = 1;
    public static final int OSCILLATION_DISTANCE = 50;
    public static final float[] LEVEL_MULTIPLIERS = {0.9f, 0.75f};
    public static final float BOX2D_SCALE = 10;

    public static float COLLISION_ACTIVATION_PERCENT = 0.5f;

    public static final float[] DISAPPEARING_OBJECT_STARTTIME = {0.5f, 1, 1.5f, 2, 2.5f, 3, 3.5f, 4, 1.5f, 5};
    public static final float[] DISAPPEARING_OBJECT_TIME = {5, 4.5f, 4, 3.5f, 3, 2.5f, 2, 1.5f, 1, 0.5f};

    public static final float[] FOLLOWER_SPEED = {90 / BOX2D_SCALE, 110 / BOX2D_SCALE, 130 / BOX2D_SCALE, 150 / BOX2D_SCALE, 170 / BOX2D_SCALE, 190 / BOX2D_SCALE, 210 / BOX2D_SCALE, 230 / BOX2D_SCALE, 250 / BOX2D_SCALE, 270 / BOX2D_SCALE};
    public static final float[] FOLLOWER_STARTTIME = {0, 0.5f, 1, 1.5f, 2, 2.5f, 3, 3.5f, 4, 4.5f};

    public static final float[] PACER_SPEED = {90 / BOX2D_SCALE, 110 / BOX2D_SCALE, 130 / BOX2D_SCALE, 150 / BOX2D_SCALE, 170 / BOX2D_SCALE, 190 / BOX2D_SCALE, 210 / BOX2D_SCALE, 230 / BOX2D_SCALE, 250 / BOX2D_SCALE, 270 / BOX2D_SCALE};
    public static final float[] PACER_STARTTIME = {0, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f};

    public static final float[] OSCILLATOR_SPEED = {20 / BOX2D_SCALE, 40 / BOX2D_SCALE, 60 / BOX2D_SCALE, 80 / BOX2D_SCALE, 100 / BOX2D_SCALE, 120 / BOX2D_SCALE, 140 / BOX2D_SCALE, 160 / BOX2D_SCALE, 180 / BOX2D_SCALE, 200 / BOX2D_SCALE};
    public static final float[] OSCILLATOR_STARTTIME = {0, 0.5f, 1, 1.5f, 2, 2.5f, 3, 3.5f, 4, 4.5f};

    public static final float[] DISAPPEARING_STARTTIME = {5.5f, 5, 4.5f, 4, 3.5f, 3, 2.5f, 2, 1.5f, 1};
    public static final float[] DISAPPEARING_TIME = {0.45f, 0.4f, 0.35f, 0.3f, 0.25f, 0.2f, 0.15f, 0.1f, 0.05f, 0.01f};

    public static final float[] HOLE_STARTUP_TIME = {0.5f, 1, 1.5f, 2, 2.5f, 3, 3.5f, 4, 4.5f, 5};
    public static final float[] HOLE_SCALE_TIME = {5, 4.5f, 4, 3.5f, 3, 2.5f, 2, 1.5f, 1, 0.5f};

    public static final float[] ORBITER_STARTUP_TIME = {0.5f, 1, 1.5f, 2, 2.5f, 3, 3.5f, 4, 4.5f, 5};
    public static final float[] ORBITER_DISAPPEAR_TIME = {45, 40, 35, 30, 25, 20, 15, 10, 8, 6};

    public static final int[] BALL_SPEED = {300, 400, 500, 600};

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

    public static final Color[] ZONE_COLORS = {BLUE, TURQUOISE, PURPLE, GREY};

    public static final long MAX_SCORE = 100000000L;
    public static final int SCORE_DECREMENT_FOR_COLLISION = -100000;
    public static final int SCORE_INCREMENT_FOR_SAVED_ORBITER = 10000;
    public static final int EXTRA_ORBITER_WORTH = 10000;
    public static final int GOLD_WORTH = 100;
    public static final int VIDEO_REWARD = 10000;
    public static final int TWEET_REWARD = 1000;

    public static final String SCREENSHOT_LEVEL = "level.png";
    public static final String SCREENSHOT_LEVEL_RESULT = "levelResult.png";

    public static final int WIDTH_IN_PIXELS = 3;

    public static final int NUMBER_OF_CREDITS = 4;

    public static String[][] CREDIT_ARRAY = new String[NUMBER_OF_CREDITS][2];
    
    // POTENTIAL AB DATA
    public static final int LEVEL_FOR_ADS = 4;
    public static final int DEATHS_FOR_ADS = 10;

    static {
        CREDIT_ARRAY[0][0] = "ARTISTIC DIRECTION";  CREDIT_ARRAY[0][1] = "MATTHEW SECRIST";
        CREDIT_ARRAY[1][0] = "EVERYTHINGER";        CREDIT_ARRAY[1][1] = "ALIMA RAHMAN";
        CREDIT_ARRAY[2][0] = "LEAD CUTENESS";       CREDIT_ARRAY[2][1] = "KALI KRSTEVSKA";
        CREDIT_ARRAY[3][0] = "SPECIAL THANKS";      CREDIT_ARRAY[3][1] = "SEBASTIEN RAHMAN";
    }
}
