package com.wly.puzzle15;

/**
 * 游戏数据生成器 其中关于问题的可解性讨论，可参考：
 * http://blog.csdn.net/u011638883/article/details/17139739
 * 
 * @author wly
 *
 */
public class PuzzleGenerator {

	/**
	 * 得到一个可解的问题数据 type==1,这是随机，type==2,这是固定的
	 * 
	 * @return
	 */
	public int[][] getPuzzleData(int type) {

		// -------------------------
		// 随机生成问题数据正确方法
		// -------------------------
		if (type == 1) {
			int[][] data = new int[Conf.SIZE][Conf.SIZE];
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data.length; j++) {
					data[i][j] = i * data.length + j;
				}
			}
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data.length; j++) {
					int index1 = (int) (Math.random() * Conf.SIZE);
					int index2 = (int) (Math.random() * Conf.SIZE);
					int temp = data[index1][index2];
					data[index1][index2] = data[i][j];
					data[i][j] = temp;
				}
			}
			if (canSolve(data)) {
				return data;
			} else {
				return getPuzzleData(1);
			}
		} else {
			// -------------------------
			// 简单测试数据,仅作演示之用
			// -------------------------

			int[][] data = { { 0, 3, 1, 4 }, { 2, 9, 6, 8 }, { 13, 10, 7, 11 },
					{ 5, 14, 15, 12 } };
			if (canSolve(data)) {
				return data;
			} else {
				return getPuzzleData(1);
			}
		}

	}

	/**
	 * 讨论问题的可解性
	 * 
	 * @param state
	 *            状态
	 */
	private boolean canSolve(int[][] state) {

		int blank_row = 0; // "空格"所在的行数

		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				if (state[i][j] == 0) {
					blank_row = i;
				}
			}
		}
		if (state.length % 2 == 1) { // 问题宽度为奇数
			return (getInversions(state) % 2 == 0);
		} else { // 问题宽度为偶数
			if ((state.length - blank_row) % 2 == 1) { // 从底往上数,空格位于奇数行
				return (getInversions(state) % 2 == 0);
			} else { // 从底往上数,空位位于偶数行
				return (getInversions(state) % 2 == 1);
			}
		}
	}

	/**
	 * 计算问题的"倒置变量和"
	 * 
	 * @param state
	 */
	private int getInversions(int[][] state) {
		int inversion = 0;
		int temp = 0;
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[i].length; j++) {
				int index = i * state.length + j + 1;
				while (index < (state.length * state.length)) {
					if (state[index / state.length][index % state.length] != 0
							&& state[index / state.length][index % state.length] < state[i][j]) {
						temp++;
					}
					index++;
				}
				inversion = temp + inversion;
				temp = 0;
			}
		}
		return inversion;
	}
}
