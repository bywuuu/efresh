package com.wly.puzzle15;

/**
 * ��Ϸ���������� ���й�������Ŀɽ������ۣ��ɲο���
 * http://blog.csdn.net/u011638883/article/details/17139739
 * 
 * @author wly
 *
 */
public class PuzzleGenerator {

	/**
	 * �õ�һ���ɽ���������� type==1,���������type==2,���ǹ̶���
	 * 
	 * @return
	 */
	public int[][] getPuzzleData(int type) {

		// -------------------------
		// �����������������ȷ����
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
			// �򵥲�������,������ʾ֮��
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
	 * ��������Ŀɽ���
	 * 
	 * @param state
	 *            ״̬
	 */
	private boolean canSolve(int[][] state) {

		int blank_row = 0; // "�ո�"���ڵ�����

		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				if (state[i][j] == 0) {
					blank_row = i;
				}
			}
		}
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
