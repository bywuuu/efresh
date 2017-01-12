package com.wly.puzzle15;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/**
 * ��Ϸ��Ԫ��һ���Զ�����������װ��������¼�
 * 
 * @author wly
 *
 */
public class GamePanel extends RelativeLayout implements OnClickListener {

	// ��ǰ��Ϸ����
	public static int[][] mData;
	// ��Ϸ�д�ͼƬ���и���СͼƬ����
	private Bitmap[] bitmaps;
	// ��ǰ"�ո�"��λ��
	private int blank_row;
	private int blank_column;

	TileView[][] tiles;

	private int tileWidth;
	private int tileHeight;
	private Context mContext;
	private Handler mHandler;
	private Handler mHandlerUI;
	private Runnable runnableUI;

	// �Զ���
	private AutoRunner autoRunner;

	boolean moving = false; // ��ǵ�ǰ�Ƿ�������"�ƶ���"״̬����ֹ�ظ�������ť���µĴ���

	// //��ǰ��Ϸ״̬
	// private int mState;
	// //��ǰ����
	// private int mCost;
	// //��Ϸ���õ��Ĵ�ͼƬ
	// private Bitmap originalBitmap;
	// //���һ�����ʱ��
	// private long lastPlay;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			move(msg.getData().getInt("direction"),
					msg.getData().getInt("row"), msg.getData().getInt("column"));
		}
	};

	public GamePanel(Context context, Handler handler, Handler handlerUI,
			Runnable runnableUI) {
		super(context);
		this.mContext = context;
		this.mHandler = handler;
		this.mHandlerUI = handlerUI;
		this.runnableUI = runnableUI;
		init(context);
	}

	public Context getMContext() {
		return mContext;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	public void autoRun() {
		if (autoRunner != null) {
			autoRunner.cancel();
		}
		autoRunner = new AutoRunner(this, handler, mHandlerUI, runnableUI,
				new SolvePuzzleListener() {

					@Override
					public void start() {
						// ������Ϣ��MainActivity����ʾ���ؽ�����
						Message msg = new Message();
						msg.what = 0;
						mHandler.sendMessage(msg);
					}

					@Override
					public void finished() {
						// ������Ϣ��MainActivity,���ؼ��ؽ�����
						Message msg = new Message();
						msg.what = 1;
						mHandler.sendMessage(msg);
					}
				});
		autoRunner.start();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(tileWidth * Conf.SIZE, tileHeight * Conf.SIZE);
	}

	/**
	 * �������������������
	 */
	public void init(Context context) {

		this.removeAllViews();

		setData(new PuzzleGenerator().getPuzzleData(1));
		for (int i = 0; i < mData.length; i++) {
			for (int j = 0; j < mData.length; j++) {
				if (mData[i][j] == 0) {
					blank_row = i;
					blank_column = j;
				}
			}
		}
		this.setBackgroundColor(Conf.panelBG);

		bitmaps = new Bitmap[Conf.SIZE * Conf.SIZE];

		tiles = new TileView[Conf.SIZE][Conf.SIZE];

		bitmaps[0] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no0);
		bitmaps[1] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no1);
		bitmaps[2] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no2);
		bitmaps[3] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no3);
		bitmaps[4] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no4);
		bitmaps[5] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no5);
		bitmaps[6] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no6);
		bitmaps[7] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no7);
		bitmaps[8] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no8);
		bitmaps[9] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no9);
		bitmaps[10] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no10);
		bitmaps[11] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no11);
		bitmaps[12] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no12);
		bitmaps[13] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no13);
		bitmaps[14] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no14);
		bitmaps[15] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.no15);

		tileWidth = bitmaps[0].getWidth();
		tileHeight = bitmaps[0].getHeight();

		RelativeLayout.LayoutParams containerParams = new RelativeLayout.LayoutParams(
				Conf.SIZE * tileWidth, Conf.SIZE * tileHeight);
		this.setLayoutParams(containerParams);

		for (int i = 0; i < Conf.SIZE; i++) {
			for (int j = 0; j < Conf.SIZE; j++) {
				tiles[i][j] = new TileView(context, mData[i][j], i, j);
				tiles[i][j].setImageBitmap(bitmaps[mData[i][j]]);
				// tiles[i][j].setId(i*Conf.SIZE + j + 2000);
				// tiles[i][j].setOnTouchListener(this);
				tiles[i][j].setOnClickListener(this);
				RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
						tileWidth, tileHeight);
				layoutParams.leftMargin = tileWidth * j;
				layoutParams.topMargin = tileHeight * i;
				tiles[i][j].setLayoutParams(layoutParams);

				if (mData[i][j] != 0) { // �����"�ո�"
					this.addView(tiles[i][j]);
				}
			}
		}
	}

	public int getBlankRow() {
		return blank_row;
	}

	public int getBlankColumn() {
		return blank_column;
	}

	/**
	 * �õ���ǰ������Ԫ�Ŀ����ƶ��ķ���
	 * 
	 * @return
	 */
	public int getMoveDirection(int row, int column) {
		if (row > 0 && tiles[row - 1][column].getData() == 0) {
			return Conf.UP;
		} else if (row < (mData.length - 1)
				&& tiles[row + 1][column].getData() == 0) {
			return Conf.DOWN;
		} else if (column > 0 && tiles[row][column - 1].getData() == 0) {
			return Conf.LEFT;
		} else if (column < (mData[0].length - 1)
				&& tiles[row][column + 1].getData() == 0) {
			return Conf.RIGHT;
		} else {
			return Conf.UNMOVABLE;
		}
	}

	/**
	 * �ƶ����ݣ���������Ԫ��
	 * 
	 * @param array
	 * @param row
	 * @param column
	 * @param direction
	 */
	private void moveData(int[][] array, TileView[][] tiles, int row,
			int column, int direction) {
		int temp = 0;
		TileView tempView;

		switch (direction) {
		case Conf.UP:
			temp = array[row][column];
			array[row][column] = array[row - 1][column];
			array[row - 1][column] = temp;

			// ����TileView��λ�ñ��
			tempView = tiles[row][column];
			tiles[row][column] = tiles[row - 1][column];
			tiles[row][column].setRow(row + 1);
			tiles[row - 1][column] = tempView;
			tiles[row - 1][column].setRow(row - 1);
			blank_row++;
			break;
		case Conf.DOWN:
			temp = array[row][column];
			array[row][column] = array[row + 1][column];
			array[row + 1][column] = temp;

			// ����TileView��λ�ñ��
			tempView = tiles[row][column];
			tiles[row][column] = tiles[row + 1][column];
			tiles[row][column].setRow(row - 1);
			tiles[row + 1][column] = tempView;
			tiles[row + 1][column].setRow(row + 1);
			blank_row--;
			break;
		case Conf.LEFT:
			temp = array[row][column];
			array[row][column] = array[row][column - 1];
			array[row][column - 1] = temp;

			tempView = tiles[row][column];
			tiles[row][column] = tiles[row][column - 1];
			tiles[row][column].setColumn(column + 1);
			tiles[row][column - 1] = tempView;
			tiles[row][column - 1].setColumn(column - 1);
			blank_column++;
			break;
		case Conf.RIGHT:
			temp = array[row][column];
			array[row][column] = array[row][column + 1];
			array[row][column + 1] = temp;

			// ����TileView��λ�ñ��
			tempView = tiles[row][column];
			tiles[row][column] = tiles[row][column + 1];
			tiles[row][column].setColumn(column - 1);
			tiles[row][column + 1] = tempView;
			tiles[row][column + 1].setColumn(column + 1);
			blank_column--;
			break;
		case Conf.UNMOVABLE:
			break;
		}
	}

	// public void printMatirx(int[][] array) {
	// for(int i=0;i<array.length;i++) {
	// for(int j=0;j<array[0].length;j++) {
	// System.out.print(array[i][j] + " ");
	// }
	// System.out.println();
	// }
	// }

	@Override
	public void onClick(final View v) {
		if (v instanceof TileView) {

			int row = ((TileView) v).getRow();
			int column = ((TileView) v).getColumn();
			int direction = getMoveDirection(row, column);
			move(direction, row, column);
			if (!moving) {
				move(direction, row, column);
			}
		}
	}

	public int[][] getData() {
		return mData;
	}

	public void setData(int[][] mData) {
		this.mData = mData;
	}

	/**
	 * �Զ���������÷���
	 * 
	 * @param direction
	 */
	public void move(int direction, int row, int column) {
		if (!moving) {
			moving = true;

			final TileView v = tiles[row][column];
			TranslateAnimation tAnimation = null;
			final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					tileWidth, tileHeight);
			direction = getMoveDirection(row, column);

			moveData(mData, tiles, row, column, direction);
			// printMatirx(mData);
			switch (direction) {
			case Conf.UP:
				tAnimation = new TranslateAnimation(0, 0, 0, -tileHeight);
				layoutParams.leftMargin = v.getLeft();
				layoutParams.topMargin = v.getTop() - tileHeight;
				break;
			case Conf.DOWN:
				tAnimation = new TranslateAnimation(0, 0, 0, tileHeight);
				layoutParams.leftMargin = v.getLeft();
				layoutParams.topMargin = v.getTop() + tileHeight;
				break;
			case Conf.LEFT:
				tAnimation = new TranslateAnimation(0, -tileWidth, 0, 0);
				layoutParams.leftMargin = v.getLeft() - tileWidth;
				layoutParams.topMargin = v.getTop();
				break;
			case Conf.RIGHT:
				tAnimation = new TranslateAnimation(0, tileWidth, 0, 0);
				layoutParams.leftMargin = v.getLeft() + tileWidth;
				layoutParams.topMargin = v.getTop();
				break;
			case Conf.UNMOVABLE:
				moving = false;
				break;
			}

			if (tAnimation != null) { // ���ܵ����˲����ƶ���λ��
				tAnimation.setDuration(100);
				v.startAnimation(tAnimation);
				tAnimation.setFillAfter(false);
				tAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// ȡ������ϵĶ����������ⷢ����˸
						v.clearAnimation();
						// ����Layout,��Ϊʹ��Animation�ƶ���ͼƬ����û���ƶ�����
						v.setLayoutParams(layoutParams);
						
						moving = false;
					}
				});
			}

			GamePanel.this.invalidate();
		}
	}
}
