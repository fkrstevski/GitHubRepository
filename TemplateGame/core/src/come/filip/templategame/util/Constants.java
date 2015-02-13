/*******************************************************************************
 * Copyright 2013 Andreas Oehlke
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package come.filip.templategame.util;

import com.badlogic.gdx.graphics.Color;

public class Constants {

	// Location of description file for texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS = "images/canyonbunny.pack";
	public static final String TEXTURE_ATLAS_UI = "images/canyonbunny-ui.pack";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";

	// Location of description file for skins
	public static final String SKIN_CANYONBUNNY_UI = "images/canyonbunny-ui.json";

	// Location of image file for level 01
	public static final String LEVEL_01 = "levels/level-01.png";


	// Duration of feather power-up in seconds
	public static final float ITEM_FEATHER_POWERUP_DURATION = 9;

	// Delay after game over
	public static final float TIME_DELAY_GAME_OVER = 3;

	// Delay after game finished
	public static final float TIME_DELAY_GAME_FINISHED = 6;

	// Game preferences file
	public static final String PREFERENCES = "canyonbunny.prefs";

	// Shader
	public static final String shaderMonochromeVertex = "shaders/monochrome.vs";
	public static final String shaderMonochromeFragment = "shaders/monochrome.fs";

	// Angle of rotation for dead zone (no movement)
	public static final float ACCEL_ANGLE_DEAD_ZONE = 5.0f;

	// Max angle of rotation needed to gain maximum movement velocity
	public static final float ACCEL_MAX_ANGLE_MAX_MOVEMENT = 20.0f;

    public static final int BALL_RADIUS = 20;
    public static final int END_CIRCLE_RADIUS = 50;
    public static final int INSIDE_CIRCLE_RADIUS = 25;
    public static final int RECTANGLE_WIDTH = 50;
    public static final int END_CIRCLE_OUTLINE_RADIUS = 35;
    public static final int MAX_LEVELS = 3;
    public static final float[] LEVEL_MULTIPLIERS = {2, 1.5f, 1};

    public static final Color BLUE = new Color(0x00/255.0f, 0xa0/255.0f, 0xda/255.0f, 0xff/255.0f);
    public static final Color GREEN = new Color(20/255.0f, 201/255.0f, 113/255.0f, 0xff/255.0f);
    public static final Color YELLOW = new Color(244/255.0f, 206/255.0f, 58/255.0f, 0xff/255.0f);
    public static final Color WHITE = new Color(240/255.0f, 244/255.0f, 244/255.0f, 0xff/255.0f);
    public static final Color ORANGE = new Color(233/255.0f, 127/255.0f, 24/255.0f, 0xff/255.0f);
    public static final Color PURPLE = new Color(178/255.0f, 127/255.0f, 198/255.0f, 0xff/255.0f);
    public static final Color GREY = new Color(150/255.0f, 166/255.0f, 167/255.0f, 0xff/255.0f);
    public static final Color TURQUOISE = new Color(61/255.0f, 200/255.0f, 177/255.0f, 0xff/255.0f);
    public static final Color RED = new Color(238/255.0f, 115/255.0f, 98/255.0f, 0xff/255.0f);
    public static final Color BLACK = new Color(47/255.0f, 72/255.0f, 94/255.0f, 0xff/255.0f);


}
