package com.wly.puzzle15;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * IDA*���15puzzle���� IDA*������IDDFS��A*�㷨������IDDFS�������������е��ڴ濪����A*�㷨��ζ��"����ʽ"������
 * IDA*�������ɵ�����ȵ�A*�㷨�����������"���"ָ��������Ľ��"�ٶ����"��
 * ʹ"�ٶ����"���ϵ������ӣ������Ƿ�����"�ٶ����"��ǰ���ҵ����н⣬������еĻ����ͼ���������
 * �����A*�㷨��ͬ����û�п����б����ڲ�����IDDFS�Ĳ��ԣ�IDA*��������������ģ��ʴ�û�п����б�
 * 
 * @author wly
 * @date 2013-12-20
 *
 */
public class IDAStarAlgorithm {

	// �ֱ�������ϡ��ҡ����ĸ��ƶ�����Ĳ�����
	private int[] up = { -1, 0 };
	private int[] down = { 1, 0 };
	private int[] left = { 0, -1 };
	private int[] right = { 0, 1 };

	/**
	 * ע�⣬����UP��DOWN��LEFT��RIGHT������������Եģ���Ϊ���������ʹ�� ((dPrev != dCurr) && (dPrev%2
	 * == dCurr%2)) ���ж�ǰ�������ƶ������Ƿ��෴
	 */
	private final int UP = 0;
	private final int DOWN = 2;
	private final int LEFT = 1;
	private final int RIGHT = 3;

	private int SIZE;

	// ����Ŀ�������
	private int[][] targetPoints;

	// ���ڼ�¼�ƶ����裬�洢0,1,2,3,��Ӧ�ϣ��£�����
	private int[] moves = new int[100];//ʵ���Ȼ���ã�����4*4��˵

	private static long ans = 0; // ��ǰ������"�������"

	// Ŀ��״̬
	private static int[][] tState = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 },
			{ 9, 10, 11, 12 }, { 13, 14, 15, 0 } };

	private static int[][] sState = { { 0, 1, 3, 4 }, { 2, 10, 6, 8 },
			{ 5, 14, 7, 11 }, { 9, 13, 15, 12 } };

	private static int blank_row, blank_column;

	public IDAStarAlgorithm(int[][] state) {
		SIZE = state.length;
		targetPoints = new int[SIZE * SIZE][2];

		sState = state;
		// �õ��ո�����
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

		// �õ�Ŀ�����������
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				targetPoints[tState[i][j]][0] = i; // ����Ϣ

				targetPoints[tState[i][j]][1] = j; // ����Ϣ
			}
		}
		for (int i = 0; i < moves.length; i++) {
			moves[i] = -1;
		}
	}

	/**
	 * ��������Ŀɽ���
	 * 
	 * @param state
	 *            ״̬
	 */
	private boolean canSolve(int[][] state) {
		if (state.length % 2 == 1) { // ������Ϊ����
			return (getInversions(state) % 2 == 0);
		} else { // ������Ϊż��
			if ((state.length - blank_row) % 2 == 1) { // �ӵ�������,�ո�λ��������
				return (getInversions(state) % 2 == 0);
			} else { // �ӵ�������,��λλ��ż����
				return (getInversions(state) % 2 == 1);
			}
		}
	}

	public int[] getSolvePath(int[][] data) {
		for (int i = 0; i < moves.length; i++) {
			moves[i] = -1;
		}
		if (canSolve(data)) {
			// System.out.println("--����ɽ⣬��ʼ���--");
			// �������پ���Ϊ��ʼ��С������
			int j = getHeuristic(data);
			// System.out.println("��ʼmanhattan����:" + j);
			int i = -1;// �ÿ�Ĭ���ƶ�����

			// long time = System.currentTimeMillis();
			// ��������"��С������"
			for (ans = j;; ans++) {
				if (solve(data, blank_row, blank_column, 0, i, j,MainActivity.methodsType)) {
					break;
				}
			}
			// System.out.println("�����ʱ:" + (System.currentTimeMillis() -
			// time));

			// printMatrix(data);
			// int[][] matrix = move(data,moves[0]);
			// for(int k=1;k<ans;k++) {
			// matrix = move(matrix, moves[k]);
			// }
			return moves;
		} else {
			// System.out.println("--��Ǹ������������޿��н�--");
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
	 * ���ڱȽ�����������������Χ���Ƿ����
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
	 * ��ⷽ�� JAVA�и�������Ԫ��ֵ�ĵķ���ָ��� 1
	 * ʹ��forѭ�����������ÿ��Ԫ�ظ��ƣ���Ҫ��ÿ���������clone����������ʵ�������ĸ��ƣ� 2 ʹ��clone�������õ������ֵ������������ 3
	 * ʹ��System.arraycopy����
	 * 
	 * ע�⣺ 1.���淽����arraycopyЧ�ʽϸߡ� 2.
	 * ������˵�Ŀ�������ķ�����ֻ�����һά���飬���ڶ�ά���飬Ҫ��ÿһά�����Ϸ������и��Ʋ���ʵ�ָ�������Ԫ�ص�ֵ���������á� 3. clone ��
	 * arraycopy�Զ�ά������и���ʱ����ǳ������ ��
	 * 
	 * System.arraycopy(this.QPN, 0, UQPN, 0, this.QPNNumber);
	 * 
	 * ����������Ԫ��ֻ���������á��¾�����ָ����ͬ���ڴ��ַ�������۶������飬���ǻ����������飩����ȷ�ķ���Ϊ��
	 * 
	 * for(int i = 0; i < this.QPNNumber; i++){ System.arraycopy(this.QPN[i], 0,
	 * UQPN[i], 0, this.QPNNumber); }
	 * 
	 * @param state
	 *            ��ǰ״̬
	 * @param blank_row
	 *            ��λ��������
	 * @param blank_column
	 *            �ո��������
	 * @param dep
	 *            ��ǰ���
	 * @param d
	 *            ��һ���ƶ��ķ���
	 * @param h
	 *            ��ǰ״̬���ۺ���
	 * @return
	 */
	public boolean solve(int[][] state, int blank_row, int blank_column,
			int dep, int d, int h,int type) {

		int h1;

		// ��Ŀ�����Ƚϣ����Ƿ���ͬ�������ͬ���ʾ�����ѽ�
		boolean isSolved = true;
		if(type==1){//���ַ�������1��2��3��4��������
			if (MainActivity.finalStep == 1) {
				
				if (state[0][0] == tState[0][0]) {
					isSolved = true;
					// ��������Ϊ���֣�֮ǰû��ɾ�������moves������ɴ���Ĳ���Ҳ����¼�ˣ������ͺ���
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
					// ��������Ϊ���֣�֮ǰû��ɾ�������moves������ɴ���Ĳ���Ҳ����¼�ˣ������ͺ���
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
					// ��������Ϊ���֣�֮ǰû��ɾ�������moves������ɴ���Ĳ���Ҳ����¼�ˣ������ͺ���
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
					// ��������Ϊ���֣�֮ǰû��ɾ�������moves������ɴ���Ĳ���Ҳ����¼�ˣ������ͺ���
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
		}else{//���ַ�����һ��һ���ģ����ж���
			if (setStep(MainActivity.finalStep,state,tState)) {
				isSolved = true;
				// ��������Ϊ���֣�֮ǰû��ɾ�������moves������ɴ���Ĳ���Ҳ����¼�ˣ������ͺ���
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

		// ���ڱ�ʾ"�ո�"�ƶ��������λ��
		int blank_row1 = blank_row;
		int blank_column1 = blank_column;
		int[][] state2 = new int[SIZE][SIZE];

		for (int direction = 0; direction < 4; direction++) {
			state2 = copyMatrix(state);

			// �����ƶ�������ϴ��ƶ�����պ��෴�������������������
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

			// �߽���
			if (blank_column1 < 0 || blank_column1 == SIZE || blank_row1 < 0
					|| blank_row1 == SIZE) {
				continue;
			}

			// �����ո�λ�ú͵�ǰ�ƶ�λ�ö�Ӧ�ĵ�Ԫ��
			state2[blank_row][blank_column] = state2[blank_row1][blank_column1];
			state2[blank_row1][blank_column1] = 0;

			// �鿴��ǰ�ո��Ƿ����ڿ���Ŀ���
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
				// �������������������ܵ��ƶ����򶼻�ʹ�ù��ۺ���ֵ���
				h1 = h + 1;
			}
			if (h1 + dep + 1 > ans) { // ��֦
				continue;
			}

			moves[dep] = direction;
			// System.out.println("ans:" + ans);
			// printMatrix(state2);
			// ����������
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
	 * �õ����ۺ���ֵ
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
	 * ���������"���ñ�����"
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