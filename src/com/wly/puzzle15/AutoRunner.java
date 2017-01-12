package com.wly.puzzle15;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * һ���߳������Զ���
 * @author wly
 *
 */
public class AutoRunner extends Thread{
	
	//ÿ���ƶ���Ԫ�ļ��ʱ��
	private long MOVE_TIME = 200;
	
	private GamePanel gamePanel;
	//���ڴ�Ž�Ĳ��裺0,1,2,3
	private int[] moves = new int[100];//ʵ�⹻�ã�����4*4��˵�����Žⲻ�ᳬ��80��
	
	private boolean isAlive = false;
	//��Ϊ���������һ���ȽϺ�ʱ�Ķ�����������������һ���ص��ӿڣ�����ʾ���ؽ�����
	private SolvePuzzleListener mListener;
	private Handler mHandler;
	private Handler mHandlerUI;
	private Runnable runnablUI;
	public AutoRunner(GamePanel gamePanel,Handler handler,Handler handlerUI,Runnable runnableui,SolvePuzzleListener listener) {
		this.gamePanel = gamePanel;
		this.mListener = listener;
		this.mHandler = handler;
		this.mHandlerUI = handlerUI;
		this.runnablUI = runnableui;
	}

	/**
	 * �Ľ��˷����������һ�У�һ��һ����
	 */
	
	@Override
	public void run() {
		super.run();
		mListener.start();
		IDAStarAlgorithm idaStarAlgorithm = new IDAStarAlgorithm(gamePanel.getData());
		moves = idaStarAlgorithm.getSolvePath(gamePanel.getData());
		mListener.finished();
		int i = 0;
		isAlive = true;
		Message msg;
		Bundle bundle;
		while(isAlive && moves[i] != -1) {
			msg = new Message();
//			Intent intent = new Intent();
			bundle = new Bundle();
			switch(moves[i]) {
			case Conf.UP:
				bundle.putInt("row", gamePanel.getBlankRow()-1);
				bundle.putInt("column", gamePanel.getBlankColumn());
				bundle.putInt("direction", Conf.DOWN);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
				break;
			case Conf.DOWN:
				bundle.putInt("row", gamePanel.getBlankRow()+1);
				bundle.putInt("column", gamePanel.getBlankColumn());
				bundle.putInt("direction", Conf.UP);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
				break;
			case Conf.LEFT:
				bundle.putInt("row",gamePanel.getBlankRow());
				bundle.putInt("column", gamePanel.getBlankColumn()-1);
				bundle.putInt("direction", Conf.RIGHT);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
				break;
			case Conf.RIGHT:
				bundle.putInt("row",gamePanel.getBlankRow());
				bundle.putInt("column", gamePanel.getBlankColumn()+1);
				bundle.putInt("direction", Conf.LEFT);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
				break;
			}
			i++;
			try {
				Thread.sleep(MOVE_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		gamePanel.setData(MainActivity.tempData);
		MainActivity.finalStep++;
		mHandlerUI.post(runnablUI);
	}
	

	public void cancel() {
		isAlive = false;
	}
}


/**
 * ��ʾ��ǰ�������Ļص��ӿ�
 * @author wly
 *
 */
interface SolvePuzzleListener {
	public void start();
	public void finished();
}