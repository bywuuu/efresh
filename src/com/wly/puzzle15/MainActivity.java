package com.wly.puzzle15;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	GamePanel gamePanel;
	ProgressDialog pd;
	public Button random;
	public static Button autoRun;
	public static int[][] tempData = new int[4][4];// 这个是中间存储当前谜题的变量，因为改成了一个一个来，所以要这样
	public static int finalStep = 1;
	public static int methodsType = 2;// 采用哪种方法求解，1的话就是前四个然后后面所有；2的话就是一个一个来

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				pd.show();
			} else if (msg.what == 1) {
				pd.cancel();
			}
		}
	};
	Handler handlerUI = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RelativeLayout gameContainer = (RelativeLayout) this
				.findViewById(R.id.gamePanelContainer);
		this.handlerUI = new Handler(this.getMainLooper()) {
			@Override
			public void handleMessage(android.os.Message msg) {
				super.handleMessage(msg);
				Bundle bundle = msg.getData();
			};
		};
		gamePanel = new GamePanel(this, handler, handlerUI, runnable);
		gameContainer.addView(gamePanel);

		random = (Button) this.findViewById(R.id.random);
		random.setOnClickListener(autoRadomL);
		autoRun = (Button) this.findViewById(R.id.autoRun);
		autoRun.setOnClickListener(autoClickL);
		// pd = new ProgressDialog(MainActivity.this);
		// pd.setMessage("求解问题中，请稍加等待");

	}

	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.random:
	// finalStep = 1;
	// gamePanel.init(MainActivity.this);
	// break;
	// case R.id.autoRun:
	// if (finalStep == 1) {
	//
	// } else if (finalStep == 2) {
	// pd = new ProgressDialog(MainActivity.this);
	// pd.setMessage("求解问题中，请稍加等待");
	// gamePanel.setData(MainActivity.tempData);
	// } else if (finalStep == 3) {
	// pd = new ProgressDialog(MainActivity.this);
	// pd.setMessage("求解问题中，请稍加等待");
	// gamePanel.setData(MainActivity.tempData);
	// } else if (finalStep == 4) {
	// pd = new ProgressDialog(MainActivity.this);
	// pd.setMessage("求解问题中，请稍加等待");
	// gamePanel.setData(MainActivity.tempData);
	// } else {
	// pd = new ProgressDialog(MainActivity.this);
	// pd.setMessage("求解问题中，请稍加等待");
	// gamePanel.setData(MainActivity.tempData);
	// }
	// gamePanel.autoRun();
	// break;
	// }
	// }

	OnClickListener autoClickL = new Button.OnClickListener() {
		public void onClick(View v) {
			if (finalStep == 1) {
				pd = new ProgressDialog(MainActivity.this);
				pd.setMessage("求解1的移动");
			} else if (finalStep == 2) {
				pd = new ProgressDialog(MainActivity.this);
				pd.setMessage("求解2的移动");
				gamePanel.setData(MainActivity.tempData);
			} else if (finalStep == 3) {
				pd = new ProgressDialog(MainActivity.this);
				pd.setMessage("求解3的移动");
				gamePanel.setData(MainActivity.tempData);
			} else if (finalStep == 4) {
				pd = new ProgressDialog(MainActivity.this);
				pd.setMessage("求解4的移动");
				gamePanel.setData(MainActivity.tempData);
			} else {
				pd = new ProgressDialog(MainActivity.this);
				pd.setMessage("求解5~15的最优解，请等待（有时可长达1分钟，短则数秒。。。）");
				gamePanel.setData(MainActivity.tempData);
			}
			gamePanel.autoRun();

		}
	};
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (methodsType == 1) {
				if (finalStep <= 5) {
					autoClick();
				}
			} else {
				if (finalStep <= 15) {
					autoClick();
				}
			}

		}
	};

	OnClickListener autoRadomL = new Button.OnClickListener() {
		public void onClick(View v) {
			finalStep = 1;
			gamePanel.init(MainActivity.this);
		}
	};

	public static void autoClick() {
		autoRun.performClick();
	}

	public void autoRadom() {
		random.performClick();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Dialog dialog = new AlertDialog.Builder(this)
					.setTitle("单选框")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setSingleChoiceItems(new String[] { "Item1", "Item2" },
							MainActivity.methodsType == 1 ? 0 : 1,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									if (which == 0) {
										MainActivity.methodsType = 1;
									} else if (which == 1) {
										MainActivity.methodsType = 2;
									}
									dialog.dismiss();
								}
							}).setNegativeButton("取消", null).show();
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MainActivity.this.finish();
		}
		return true;
	}
}
