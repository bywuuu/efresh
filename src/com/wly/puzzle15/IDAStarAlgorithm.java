package com.wly.puzzle15;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * IDA*求解15puzzle问题 IDA*整合了IDDFS和A*算法。其中IDDFS控制了求解过程中的内存开销，A*算法意味着"启发式"搜索。
 * IDA*可以理解成迭代深度的A*算法，其中这里的"深度"指的是问题的解的"假定损耗"。
 * 使"假定损耗"不断迭代增加，检验是否能在"假定损耗"的前提找到可行解，如果不行的话，就继续迭代。
 * 这里和A*算法不同的是没有开放列表，由于采用了IDDFS的策略，IDA*是深度优先搜索的，故此没有开放列表。
 * 
 * @author wly
 * @date 2013-12-20
 *
 */
public class IDAStarAlgorithm {

	// 分别代表左、上、右、下四个移动方向的操作数
	private int[] up = { -1, 0 };
	private int[] down = { 1, 0 };
	private int[] left = { 0, -1 };
	private int[] right = { 0, 1 };

	/**
	 * 注意，这里UP和DOWN，LEFT和RIGHT必须是两两相对的，因为后面代码中使用 ((dPrev != dCurr) && (dPrev%2
	 * == dCurr%2)) 来判断前后两个移动方向是否相反
	 */
	private final int UP = 0;
	private final int DOWN = 2;
	private final int LEFT = 1;
	private final int RIGHT = 3;

	private int SIZE;

	// 各个目标点坐标
	private int[][] targetPoints;

	// 用于记录移动步骤，存储0,1,2,3,对应上，下，左，右
	private int[] moves = new int[100];//实测居然够用，对于4*4来说

	private static long ans = 0; // 当前迭代的"设想代价"

	// 目标状态
	private static int[][] tState = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 },
			{ 9, 10, 11, 12 }, { 13, 14, 15, 0 } };

	private static int[][] sState = { { 0, 1, 3, 4 }, { 2, 10, 6, 8 },
			{ 5, 14, 7, 11 }, { 9, 13, 15, 12 } };

	private static int blank_row, blank_column;

	public IDAStarAlgorithm(int[][] state) {
		SIZE = state.length;
		targetPoints = new int[SIZE * SIZE][2];

		sState = state;
		// 得到空格坐标
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[i].length; j++) {
				if (state[i][j] == 0) {
					blank_row = i;
					blank_column = j;
					i = 5;
					j = 5;
					break;
				}
			}
		}

		// 得到目标点坐标数组
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				targetPoints[tState[i][j]][0] = i; // 行信息

				targetPoints[tState[i][j]][1] = j; // 列信息
			}
		}
		for (int i = 0; i < moves.length; i++) {
			moves[i] = -1;
		}
	}

	/**
	 * 讨论问题的可解性
	 * 
	 * @param state
	 *            状态
	 */
	private boolean canSolve(int[][] state) {
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

	public int[] getSolvePath(int[][] data) {
		for (int i = 0; i < moves.length; i++) {
			moves[i] = -1;
		}
		if (canSolve(data)) {
			// System.out.println("--问题可解，开始求解--");
			// 以曼哈顿距离为初始最小代价数
			int j = getHeuristic(data);
			// System.out.println("初始manhattan距离:" + j);
			int i = -1;// 置空默认移动方向

			// long time = System.currentTimeMillis();
			// 迭代加深"最小代价数"
			for (ans = j;; ans++) {
				if (solve(data, blank_row, blank_column, 0, i, j,MainActivity.methodsType)) {
					break;
				}
			}
			// System.out.println("求解用时:" + (System.currentTimeMillis() -
			// time));

			// printMatrix(data);
			// int[][] matrix = move(data,moves[0]);
			// for(int k=1;k<ans;k++) {
			// matrix = move(matrix, moves[k]);
			// }
			return moves;
		} else {
			// System.out.println("--抱歉！输入的问题无可行解--");
			return null;
		}
	}

	public int[][] move(int[][] state, int direction) {
		int row = 0;
		int column = 0;
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				if (state[i][j] == 0) {
					row = i;
					column = j;
					i = 5;
					break;
				}
			}
		}
		switch (direction) {
		case UP:
			state[row][column] = state[row - 1][column];
			state[row - 1][column] = 0;
			break;
		case DOWN:
			state[row][column] = state[row + 1][column];
			state[row + 1][column] = 0;
			break;
		case LEFT:
			state[row][column] = state[row][column - 1];
			state[row][column - 1] = 0;
			break;
		case RIGHT:
			state[row][column] = state[row][column + 1];
			state[row][column + 1] = 0;
			break;
		}
		// printMatrix(state);
		return state;
	}

	public void printMatrix(int[][] matrix) {
		System.out.println("------------");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * 用于比较两个数组在连续范围内是否相等
	 * @param step
	 * @param state
	 * @param tarState
	 * @return
	 */
	private boolean setStep(int step, int[][] state, int[][] tarState) {
		int pos = 0;
		boolean result = true;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (pos < step) {
					if (state[i][j] != tarState[i][j]) {
						result = false;
					} 
				} else {
					i = SIZE+1;
					break;
				}
				pos++;
			}

		}
		return result;
	}

	/**
	 * 求解方法 JAVA中复制数组元素值的的方法指深拷贝 1
	 * 使用for循环，将数组的每个元素复制（需要将每个对象调用clone方法，才能实现真正的复制） 2 使用clone方法，得到数组的值，而不是引用 3
	 * 使用System.arraycopy方法
	 * 
	 * 注意： 1.上面方法中arraycopy效率较高。 2.
	 * 以上所说的拷贝数组的方法，只是针对一维数组，对于多维数组，要在每一维用以上方法进行复制才能实现复制数组元素的值而不是引用。 3. clone 和
	 * arraycopy对二维数组进行复制时，是浅拷贝， 即
	 * 
	 * System.arraycopy(this.QPN, 0, UQPN, 0, this.QPNNumber);
	 * 
	 * 这样做数组元素只复制了引用。新旧数组指向相同的内存地址，（不论对象数组，还是基本类型数组）。正确的方法为：
	 * 
	 * for(int i = 0; i < this.QPNNumber; i++){ System.arraycopy(this.QPN[i], 0,
	 * UQPN[i], 0, this.QPNNumber); }
	 * 
	 * @param state
	 *            当前状态
	 * @param blank_row
	 *            空位的行坐标
	 * @param blank_column
	 *            空格的列坐标
	 * @param dep
	 *            当前深度
	 * @param d
	 *            上一次移动的方向
	 * @param h
	 *            当前状态估价函数
	 * @return
	 */
	public boolean solve(int[][] state, int blank_row, int blank_column,
			int dep, int d, int h,int type) {

		int h1;

		// 和目标矩阵比较，看是否相同，如果相同则表示问题已解
		boolean isSolved = true;
		if(type==1){//这种方法是先1，2，3，4，再整体
			if (MainActivity.finalStep == 1) {
				
				if (state[0][0] == tState[0][0]) {
					isSolved = true;
					// 这里是因为发现，之前没有删掉冗余的moves，会造成错误的步骤也被记录了，这样就好了
					for (int i = 0; i < moves.length; i++) {
						if (i >= dep) {
							moves[i] = -1;
						}
					}
					MainActivity.tempData = new int[4][4];
					for (int i = 0; i < SIZE; i++) {
						for (int j = 0; j < 4; j++) {
							MainActivity.tempData[i][j] = state[i][j];
						}

					}

					return true;
				} else {
					isSolved = false;
				}
			} else if (MainActivity.finalStep == 2) {
				if ((state[0][0] == tState[0][0]) && (state[0][1] == tState[0][1])) {
					isSolved = true;
					// 这里是因为发现，之前没有删掉冗余的moves，会造成错误的步骤也被记录了，这样就好了
					for (int i = 0; i < moves.length; i++) {
						if (i >= dep) {
							moves[i] = -1;
						}
					}
					MainActivity.tempData = new int[4][4];
					for (int i = 0; i < SIZE; i++) {
						for (int j = 0; j < 4; j++) {
							MainActivity.tempData[i][j] = state[i][j];
						}

					}

					return true;
				} else {
					isSolved = false;
				}
			} else if (MainActivity.finalStep == 3) {
				if ((state[0][0] == tState[0][0]) && (state[0][1] == tState[0][1])
						&& (state[0][2] == tState[0][2])) {
					isSolved = true;
					// 这里是因为发现，之前没有删掉冗余的moves，会造成错误的步骤也被记录了，这样就好了
					for (int i = 0; i < moves.length; i++) {
						if (i >= dep) {
							moves[i] = -1;
						}
					}
					MainActivity.tempData = new int[4][4];
					for (int i = 0; i < SIZE; i++) {
						for (int j = 0; j < 4; j++) {
							MainActivity.tempData[i][j] = state[i][j];
						}

					}

					return true;
				} else {
					isSolved = false;
				}
			} else if (MainActivity.finalStep == 4) {
				if ((state[0][0] == tState[0][0]) && (state[0][1] == tState[0][1])
						&& (state[0][2] == tState[0][2])
						&& (state[0][3] == tState[0][3])) {
					isSolved = true;
					// 这里是因为发现，之前没有删掉冗余的moves，会造成错误的步骤也被记录了，这样就好了
					for (int i = 0; i < moves.length; i++) {
						if (i >= dep) {
							moves[i] = -1;
						}
					}
					MainActivity.tempData = new int[4][4];
					for (int i = 0; i < SIZE; i++) {
						MainActivity.tempData[i] = state[i].clone();
					}

					return true;
				} else {
					isSolved = false;
				}
			} else {
				for (int i = 0; i < SIZE; i++) {
					if (!Arrays.equals(state[i], tState[i])) {
						isSolved = false;
						break;
					}
				}
			}
		}else{//这种方法是一个一个的，所有都是
			if (setStep(MainActivity.finalStep,state,tState)) {
				isSolved = true;
				// 这里是因为发现，之前没有删掉冗余的moves，会造成错误的步骤也被记录了，这样就好了
				for (int i = 0; i < moves.length; i++) {
					if (i >= dep) {
						moves[i] = -1;
					}
				}
				MainActivity.tempData = new int[4][4];
				for (int i = 0; i < SIZE; i++) {
					for (int j = 0; j < 4; j++) {
						MainActivity.tempData[i][j] = state[i][j];
					}

				}

				return true;
			} else {
				isSolved = false;
			}
		}

		

		if (isSolved) {
			for (int i = 0; i < moves.length; i++) {
				if (i >= dep) {
					moves[i] = -1;
				}
			}
			return true;
		}

		if (dep == ans) {
			return false;
		}

		// 用于表示"空格"移动后的坐标位置
		int blank_row1 = blank_row;
		int blank_column1 = blank_column;
		int[][] state2 = new int[SIZE][SIZE];

		for (int direction = 0; direction < 4; direction++) {
			state2 = copyMatrix(state);

			// 本地移动方向和上次移动方向刚好相反，跳过这种情况的讨论
			if (direction != d && (d % 2 == direction % 2)) {
				continue;
			}

			if (direction == UP) {
				blank_row1 = blank_row + up[0];
				blank_column1 = blank_column + up[1];
			} else if (direction == DOWN) {
				blank_row1 = blank_row + down[0];
				blank_column1 = blank_column + down[1];
			} else if (direction == LEFT) {
				blank_row1 = blank_row + left[0];
				blank_column1 = blank_column + left[1];
			} else {
				blank_row1 = blank_row + right[0];
				blank_column1 = blank_column + right[1];
			}

			// 边界检查
			if (blank_column1 < 0 || blank_column1 == SIZE || blank_row1 < 0
					|| blank_row1 == SIZE) {
				continue;
			}

			// 交换空格位置和当前移动位置对应的单元格
			state2[blank_row][blank_column] = state2[blank_row1][blank_column1];
			state2[blank_row1][blank_column1] = 0;

			// 查看当前空格是否正在靠近目标点
			if (direction == DOWN
					&& blank_row1 > targetPoints[state[blank_row1][blank_column1]][0]) {
				h1 = h - 1;
			} else if (direction == UP
					&& blank_row1 < targetPoints[state[blank_row1][blank_column1]][0]) {
				h1 = h - 1;
			} else if (direction == RIGHT
					&& blank_column1 > targetPoints[state[blank_row1][blank_column1]][1]) {
				h1 = h - 1;
			} else if (direction == LEFT
					&& blank_column1 < targetPoints[state[blank_row1][blank_column1]][1]) {
				h1 = h - 1;
			} else {
				// 这种情况发生在任意可能的移动方向都会使得估价函数值变大
				h1 = h + 1;
			}
			if (h1 + dep + 1 > ans) { // 剪枝
				continue;
			}

			moves[dep] = direction;
			// System.out.println("ans:" + ans);
			// printMatrix(state2);
			// 迭代深度求解
			if (solve(state2, blank_row1, blank_column1, dep + 1, direction, h1,MainActivity.methodsType)) {
				return true;
			}
		}
		return false;
	}

	public int[][] copyMatrix(int[][] str) {
		int[][] res = new int[4][4];
		for (int i = 0; i < str.length; i++) {
			res[i] = str[i].clone();
			// for(int j = 0;j<str.length;j++){
			// res[i][j]=str[i][j];
			// }
		}
		return res;
	}

	/**
	 * 得到估价函数值
	 */
	public int getHeuristic(int[][] state) {
		int heuristic = 0;
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[i].length; j++) {
				if (state[i][j] != 0) {
					heuristic = heuristic
							+ Math.abs(targetPoints[state[i][j]][0] - i)
							+ Math.abs(targetPoints[state[i][j]][1] - j);

				}
			}
		}
		return heuristic;
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