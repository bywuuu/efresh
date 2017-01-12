package com.wly.puzzle15;

/**
 * 参数配置类
 * @author wly
 *
 */
public class Conf {
	
	//游戏规模，如4x4
	public static int SIZE = 4;
	
	public static int panelBG = 0xff7accc8;
	//方块移动耗时
	public static int MOVE_TIME = 200;
	
	//表示四个移动方向
	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	public static final int UNMOVABLE = -1;
	
	//Handler中的Message
	public static final int REFRESH_PANEL = 5;
	
	/**
	 * 表示游戏状态
	 * @author wly
	 *
	 */
	public static class State {
		public static final int INIT = 0; 
		public static final int PAUSE = 1;
		public static final int FINISHED = 2;
	}

}
