package com.wly.puzzle15;

/**
 * ����������
 * @author wly
 *
 */
public class Conf {
	
	//��Ϸ��ģ����4x4
	public static int SIZE = 4;
	
	public static int panelBG = 0xff7accc8;
	//�����ƶ���ʱ
	public static int MOVE_TIME = 200;
	
	//��ʾ�ĸ��ƶ�����
	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	public static final int UNMOVABLE = -1;
	
	//Handler�е�Message
	public static final int REFRESH_PANEL = 5;
	
	/**
	 * ��ʾ��Ϸ״̬
	 * @author wly
	 *
	 */
	public static class State {
		public static final int INIT = 0; 
		public static final int PAUSE = 1;
		public static final int FINISHED = 2;
	}

}
