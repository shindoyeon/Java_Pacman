package pac_man;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Pacman extends JFrame implements Runnable {

	String ImagePath = "" + System.getProperty("user.dir") + "\\";
	ImageIcon lose = new ImageIcon(ImagePath + "src\\loseIcon.jpg");
	ImageIcon smallDot = new ImageIcon(ImagePath + "src\\smallDot.png");
	ImageIcon bigDot = new ImageIcon(ImagePath + "src\\bigDot.png");
	ImageIcon wall = new ImageIcon(ImagePath + "src\\wall.png");
	ImageIcon enemy = new ImageIcon(ImagePath + "src\\enemy.png");
	ImageIcon pacman = new ImageIcon(ImagePath + "src\\pacman.png");
	ImageIcon empty = new ImageIcon(ImagePath + "src\\empty.png");
	ImageIcon success = new ImageIcon(ImagePath + "src\\success.jpg");

	JLabel[][] f = new JLabel[14][14]; // �г����� �ø� �� �迭 ��ü ����
	JButton button = new JButton(lose);
	JButton win = new JButton(success);
	Container contentPane;
	JPanel panel = new JPanel();
	JDialog dialog = new JDialog();
	private int pacmanH = 12;
	private int pacmanW = 7;
	private int enemyH = 7;
	private int enemyW = 7;
	private int numOfDot = 81;
	private static Icon temp1, temp2, temp3, temp4, temp;
	private int start = 2;
	private int where;
	private Random random = new Random();
	
	int   iMap[] = {
	         1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	         1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
	         1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1,
	         1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1,
	         1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
	         1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1,
	         1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1,
	         1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1,
	         1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1,
	         1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1,
	         1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1,
	         1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1,
	         1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1,
	         1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	      };

	Pacman() {
		setTitle("PACMAN");
		contentPane = this.getContentPane();
		panel.setLayout(new GridLayout(14, 14)); // �г��� ���̾ƿ� ����
		setSize(690, 650);
		dialog.setSize(690, 650);
		dialog.setVisible(false);

		temp = empty;

		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 14; j++) {
				f[i][j] = new JLabel(); // �� ����
				panel.add(f[i][j]); // �гο� �� �߰� (�г�-��, ��-������Ʈ)
			}
		}

		paint(f); // ���� �� �׸���

		contentPane.add(panel, BorderLayout.CENTER); // contentpane�� �г� �߰�
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	public void paint(JLabel[][] f) {
		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 14; j++) {
				f[i][j].setIcon(wall);
				panel.add(f[i][j]);
			}
		}
		for (int i = 0; i < 196; i++) {
			if(iMap[i]==0)
				f[i/14][i%14].setIcon(smallDot);
		}
		
		f[12][7].setIcon(pacman); // �ʱ� �÷��̾� ��ġ
		
		f[5][7].setIcon(empty);
		f[6][5].setIcon(empty);
		f[6][6].setIcon(empty);
		f[6][7].setIcon(empty);
		f[6][8].setIcon(empty);
		f[6][9].setIcon(empty);
		f[7][5].setIcon(empty);
		f[7][6].setIcon(empty);
		f[7][7].setIcon(enemy);
		f[7][8].setIcon(empty);
		f[7][9].setIcon(empty);

	}

	public void run() {
		TListener tListener = new TListener(); // timer Ŭ����, �� �̵� Ŭ����
		Timer t = new Timer(300, tListener);
		KListener listener = new KListener(); // Ű���� �Է¿� ���� Ŭ����
		
		if(pacmanH != 100)   
		{
			t.start();
			this.requestFocus();
			this.addKeyListener(new KListener());
		}
		
	}

	class TListener implements ActionListener { // timerŬ����(�� ������) ActionListener
		public void actionPerformed(ActionEvent event) {
			
			int [][] iPath = new int[150][2];
			Node	nodePath;
			AStar	pfAStar;
			
			pfAStar = new AStar(iMap);
			int iCurrentPath = -1;
			
			if( pacmanW >= 0 && pacmanW <= 13 && pacmanH >=0 && pacmanH <=13 && iCurrentPath == -1 &&
					!(pacmanW == enemyW && pacmanH == enemyH ) && iMap[pacmanH*14 + pacmanW] == 0 )
			{
				System.out.println("Enemy:(" +enemyW + "," + enemyH + "), pacman:(" + pacmanW + "," + pacmanH + ")");
				
				nodePath = pfAStar.FindPath(enemyW, enemyH, pacmanW, pacmanH);    
				iCurrentPath = 0;
				
				while( nodePath != null )
				{
					iPath[iCurrentPath][0] = nodePath.x;
					iPath[iCurrentPath][1] = nodePath.y;
					iCurrentPath++;
					nodePath = nodePath.prev;
				}
				//System.out.println("iCurrentPath: "+iCurrentPath);
				iCurrentPath--;
				temp1 = f[iPath[iCurrentPath-1][1]][iPath[iCurrentPath-1][0]].getIcon(); // temp1�� empty or smallDot
				f[enemyH][enemyW].setIcon(temp1); // ����ġ�� �̹����� empty�� ���� (temp �ʱ� empty)
				
				enemyW = iPath[iCurrentPath-1][0];  
				enemyH = iPath[iCurrentPath-1][1];
				f[enemyH][enemyW].setIcon(enemy); // ������ �̹����� enemy���� ����
				iCurrentPath--;
			
			}
			if (numOfDot <= 0) {
				dialog.add(win);
				dialog.setVisible(true);
				numOfDot = 1;
				pacmanH = 100;
			}
			if (enemyH == pacmanH && enemyW == pacmanW) { // �÷��̾� ��ġ�� �� ��ġ�� ���� ��
				f[enemyH][enemyW].setIcon(enemy);
				dialog.add(button);
				dialog.setVisible(true);  
				setVisible(false);   //������ �ݱ�

				pacmanH = 100;
			}
		}

	}

	class KListener extends KeyAdapter { // Ű�����Է¿� ���� KeyListener

		public void keyPressed(KeyEvent e) {
			if (numOfDot <= 0) {
				dialog.add(win);     //�÷��̾� �¸�
				dialog.setVisible(true);
				setVisible(false);   //������ �ݱ�
				numOfDot = 1;
				pacmanH = 100;

			}
			int key = e.getKeyCode();

			switch (key) {
			case KeyEvent.VK_UP:
				if ((f[pacmanH - 1][pacmanW].getIcon()).equals(smallDot)
						|| (f[pacmanH - 1][pacmanW].getIcon()).equals(empty)) {// ���� �̹����� smallDot�� empty�� ������
					if ((f[pacmanH - 1][pacmanW].getIcon()).equals(smallDot)
							&& (f[pacmanH][pacmanW].getIcon()).equals(pacman)) // ���� �̹����� smallDot�̰� ���� �̹����� �Ѹ��̸�
						numOfDot--; // ���� -1
					f[pacmanH - 1][pacmanW].setIcon(pacman); // ���� �̹����� �Ѹ����� �ٲٰ�
					f[pacmanH][pacmanW].setIcon(empty); // ���� �̹����� empty�� �ٲ۴�
					pacmanH--;
				}
				if ((f[pacmanH - 1][pacmanW].getIcon()).equals(enemy)) { // �ʱ⺸�� ��ĭ�� �̹����� ���̸�
					f[enemyH][enemyW].setIcon(enemy); // ���� �̹����� enemy�� �ٲ�
					dialog.add(button);
					dialog.setVisible(true);
					setVisible(false);   //������ �ݱ�
				}
				break;
			case KeyEvent.VK_DOWN:
				if ((f[pacmanH + 1][pacmanW].getIcon()).equals(smallDot)
						|| (f[pacmanH + 1][pacmanW].getIcon()).equals(empty)) {
					if ((f[pacmanH + 1][pacmanW].getIcon()).equals(smallDot)
							&& (f[pacmanH][pacmanW].getIcon()).equals(pacman)) // �ؿ� ����, ���� �̹��� �Ѹ�
						numOfDot--;
					f[pacmanH + 1][pacmanW].setIcon(pacman);
					f[pacmanH][pacmanW].setIcon(empty);
					pacmanH++;
				}
				if ((f[pacmanH + 1][pacmanW].getIcon()).equals(enemy)) { // �� ����ġ�� ���
					f[enemyH][enemyW].setIcon(enemy);
					dialog.add(button);   //lose
					dialog.setVisible(true);
					setVisible(false);   //������ �ݱ�
				}
				break;
			case KeyEvent.VK_LEFT:
				if ((f[pacmanH][pacmanW - 1].getIcon()).equals(smallDot)
						|| (f[pacmanH][pacmanW - 1].getIcon()).equals(empty)) {
					if ((f[pacmanH][pacmanW - 1].getIcon()).equals(smallDot)
							&& (f[pacmanH][pacmanW].getIcon()).equals(pacman))
						numOfDot--;
					f[pacmanH][pacmanW - 1].setIcon(pacman);
					f[pacmanH][pacmanW].setIcon(empty);
					pacmanW--;
				}
				if ((f[pacmanH][pacmanW - 1].getIcon()).equals(enemy)) { // �� ����ġ�� ���
					f[enemyH][enemyW].setIcon(enemy);
					dialog.add(button);    //lose
					dialog.setVisible(true);
					setVisible(false);   //������ �ݱ�
				}
				break;
			case KeyEvent.VK_RIGHT:
				if ((f[pacmanH][pacmanW + 1].getIcon()).equals(smallDot)
						|| (f[pacmanH][pacmanW + 1].getIcon()).equals(empty)) {
					if ((f[pacmanH][pacmanW + 1].getIcon()).equals(smallDot)
							&& (f[pacmanH][pacmanW].getIcon()).equals(pacman))
						numOfDot--;
					f[pacmanH][pacmanW + 1].setIcon(pacman);
					f[pacmanH][pacmanW].setIcon(empty);
					pacmanW++;
				}
				if ((f[pacmanH][pacmanW + 1].getIcon()).equals(enemy)) { // �� ����ġ�� ���
					f[enemyH][enemyW].setIcon(enemy);
					dialog.add(button);   //lose
					dialog.setVisible(true);
					setVisible(false);   //������ �ݱ�
				}
				break;
			}
			System.out.println("smallDot: "+numOfDot); // Ű ���������� �ݺ�
		}

	}
}
