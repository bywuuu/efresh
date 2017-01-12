package com.wly.puzzle15;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 一个线程做的自动机
 * @author wly
 *
 */
public class AutoRunner extends Thread{
	
	//每次移动单元的间隔时间
	private long MOVE_TIME = 200;
	
	private GamePanel gamePanel;
	//用于存放解的步骤：0,1,2,3
	private int[] moves = new int[100];//实测够用，对于4*4来说，最优解不会超过80步
	
	private boolean isAlive = false;
	//因为求解问题是一个比较耗时的动作，所以这里做了一个回调接口，来显示加载进度条
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
	 * 改进了方法，打算第一行，一步一步来
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
 * 表示当前求解问题的回调接口
 * @author wly
 *
 */
interface SolvePuzzleListener {
	public void start();
	public void finished();
}