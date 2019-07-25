package pac_man;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class gamestart extends JFrame {
   String ImagePath = "" + System.getProperty("user.dir") + "\\";
   ImageIcon lose = new ImageIcon(ImagePath + "src\\loseIcon.jpg");
   ImageIcon smallDot = new ImageIcon(ImagePath + "src\\smallDot.png");
   ImageIcon bigDot = new ImageIcon(ImagePath + "src\\bigDot.png");
   ImageIcon wall = new ImageIcon(ImagePath + "src\\wall.png");
   ImageIcon enemy = new ImageIcon(ImagePath + "src\\enemy.png");
   ImageIcon pacman = new ImageIcon(ImagePath + "src\\pacman.png");
   ImageIcon empty = new ImageIcon(ImagePath + "src\\empty.png");
   ImageIcon success = new ImageIcon(ImagePath + "src\\success.jpg");
   int time;
   JLabel[][] f = new JLabel[14][14]; // 패널위에 올릴 라벨 배열 객체 생성
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
   private static Icon temp1;

   gamestart() {
      setTitle("PACMAN");
      contentPane = this.getContentPane();
      panel.setLayout(new GridLayout(14, 14)); // 패널의 레이아웃 설정
      setSize(690, 650);
      dialog.setSize(690, 650);
      dialog.setVisible(false);
      time = 0;
      for (int i = 0; i < 14; i++) {
         for (int j = 0; j < 14; j++) {
            f[i][j] = new JLabel(); // 라벨 생성
            panel.add(f[i][j]); // 패널에 라벨 추가 (패널-판, 라벨-컴포넌트)
         }
      }

      paint(f); // 게임 맵 그리기

      contentPane.add(panel, BorderLayout.CENTER); // contentpane에 패널 추가
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Runnable r = new enemy();
      Thread p = new Thread(r);
      p.start();
      Runnable r2 = new time();
      Thread p2 = new Thread(r2);
      p2.start();
      
      
      this.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent e) {
            if (numOfDot <= 0) {
               dialog.add(win);
               dialog.setVisible(true);
               numOfDot = 1;
               pacmanH = 100;

            }
            int key = e.getKeyCode();

            switch (key) {
            case KeyEvent.VK_UP:
               if ((f[pacmanH - 1][pacmanW].getIcon()).equals(smallDot)
                     || (f[pacmanH - 1][pacmanW].getIcon()).equals(empty)) {// 위에 이미지가 smallDot나 empty와 같으면
                  if ((f[pacmanH - 1][pacmanW].getIcon()).equals(smallDot)
                        && (f[pacmanH][pacmanW].getIcon()).equals(pacman)) // 위에 이미지가 smallDot이고 지금 이미지가 팩맨이면
                     numOfDot--; // 먹이 -1
                  f[pacmanH - 1][pacmanW].setIcon(pacman); // 위에 이미지를 팩맨으로 바꾸고
                  f[pacmanH][pacmanW].setIcon(empty); // 현재 이미지를 empty로 바꾼다
                  pacmanH--;
               }
               if ((f[pacmanH - 1][pacmanW].getIcon()).equals(enemy)) { // 초기보다 두칸위 이미지가 적이면
                  f[enemyH][enemyW].setIcon(enemy); // 현재 이미지를 enemy로 바꿈
                  dialog.add(button);
                  dialog.setVisible(true);
               }
               break;
            case KeyEvent.VK_DOWN:
               if ((f[pacmanH + 1][pacmanW].getIcon()).equals(smallDot)
                     || (f[pacmanH + 1][pacmanW].getIcon()).equals(empty)) {
                  if ((f[pacmanH + 1][pacmanW].getIcon()).equals(smallDot)
                        && (f[pacmanH][pacmanW].getIcon()).equals(pacman)) // 밑에 먹이, 현재 이미지 팩맨
                     numOfDot--;
                  f[pacmanH + 1][pacmanW].setIcon(pacman);
                  f[pacmanH][pacmanW].setIcon(empty);
                  pacmanH++;
               }
               if ((f[pacmanH + 1][pacmanW].getIcon()).equals(enemy)) { // 적 마주치는 경우
                  f[enemyH][enemyW].setIcon(enemy);
                  dialog.add(button);
                  dialog.setVisible(true);
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
               if ((f[pacmanH][pacmanW - 1].getIcon()).equals(enemy)) { // 적 마주치는 경우
                  f[enemyH][enemyW].setIcon(enemy);
                  dialog.add(button);
                  dialog.setVisible(true);
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
               if ((f[pacmanH][pacmanW + 1].getIcon()).equals(enemy)) { // 적 마주치는 경우
                  f[enemyH][enemyW].setIcon(enemy);
                  dialog.add(button);
                  dialog.setVisible(true);
               }
               break;
            }
            System.out.println(numOfDot); // 키 누를때마다 반복
         }

      });
      setVisible(true);

   }

   public void paint(JLabel[][] f) {
      for (int i = 0; i < 14; i++) {
         for (int j = 0; j < 14; j++) {
            f[i][j].setIcon(wall);
            panel.add(f[i][j]);
         }
      }
      f[1][1].setIcon(smallDot);
      f[2][1].setIcon(smallDot);
      f[3][1].setIcon(smallDot);
      f[4][1].setIcon(smallDot);
      f[5][1].setIcon(smallDot);
      f[5][2].setIcon(smallDot);
      f[5][3].setIcon(smallDot);
      f[1][3].setIcon(smallDot);
      f[2][3].setIcon(smallDot);
      f[2][2].setIcon(smallDot);
      f[2][5].setIcon(smallDot);
      f[3][3].setIcon(smallDot);
      f[4][3].setIcon(smallDot);
      f[1][4].setIcon(smallDot);
      f[1][5].setIcon(smallDot);
      f[1][6].setIcon(smallDot);
      f[1][7].setIcon(smallDot);
      f[1][8].setIcon(smallDot);
      f[1][9].setIcon(smallDot);
      f[1][10].setIcon(smallDot);
      f[1][11].setIcon(smallDot);
      f[1][12].setIcon(smallDot);
      f[2][9].setIcon(smallDot);
      f[2][12].setIcon(smallDot);
      f[3][12].setIcon(smallDot);
      f[4][12].setIcon(smallDot);
      f[5][12].setIcon(smallDot);
      f[3][4].setIcon(smallDot);
      f[3][5].setIcon(smallDot);
      f[3][10].setIcon(smallDot);
      f[3][11].setIcon(smallDot);
      f[4][5].setIcon(smallDot);
      f[4][6].setIcon(smallDot);
      f[4][7].setIcon(smallDot);
      f[4][8].setIcon(smallDot);
      f[4][9].setIcon(smallDot);
      f[4][10].setIcon(smallDot);
      f[2][7].setIcon(smallDot);
      f[3][7].setIcon(smallDot);
      f[4][10].setIcon(smallDot);
      f[4][11].setIcon(smallDot);
      f[5][11].setIcon(smallDot);
      f[6][11].setIcon(smallDot);
      f[7][11].setIcon(smallDot);
      f[7][12].setIcon(smallDot);
      f[8][12].setIcon(smallDot);
      f[9][12].setIcon(smallDot);
      f[10][12].setIcon(smallDot);
      f[11][12].setIcon(smallDot);
      f[5][7].setIcon(empty);
      f[6][2].setIcon(smallDot);
      f[7][1].setIcon(smallDot);
      f[9][11].setIcon(smallDot);
      f[11][1].setIcon(smallDot);
      f[11][3].setIcon(smallDot);
      f[11][11].setIcon(smallDot);
      f[7][2].setIcon(smallDot);
      f[7][3].setIcon(smallDot);
      f[8][1].setIcon(smallDot);
      f[9][1].setIcon(smallDot);
      f[9][2].setIcon(smallDot);
      f[9][3].setIcon(smallDot);
      f[9][4].setIcon(smallDot);
      f[9][5].setIcon(smallDot);
      f[9][6].setIcon(smallDot);
      f[12][1].setIcon(smallDot);
      f[12][2].setIcon(smallDot);
      f[12][3].setIcon(smallDot);
      f[12][4].setIcon(smallDot);
      f[12][5].setIcon(smallDot);
      f[12][9].setIcon(smallDot);
      f[12][10].setIcon(smallDot);
      f[12][11].setIcon(smallDot);
      f[10][5].setIcon(smallDot);
      f[11][5].setIcon(smallDot);
      f[10][6].setIcon(smallDot);
      f[10][7].setIcon(smallDot);
      f[10][8].setIcon(smallDot);
      f[10][9].setIcon(smallDot);
      f[11][7].setIcon(smallDot);
      f[12][7].setIcon(pacman); // 초기 플레이어 위치
      f[9][8].setIcon(smallDot);
      f[9][9].setIcon(smallDot);
      f[11][9].setIcon(smallDot);
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

   class TListener implements ActionListener { // timer클래스(적 움직임) ActionListener
      public void actionPerformed(ActionEvent event) {
         int iMap[] = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0,
               0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0,
               0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1,
               1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0,
               0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1,
               0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
               1, };
         int[][] iPath = new int[150][2];
         Node nodePath;
         AStar pfAStar;

         pfAStar = new AStar(iMap);
         int iCurrentPath = -1;

         if (pacmanW >= 0 && pacmanW <= 13 && pacmanH >= 0 && pacmanH <= 13 && iCurrentPath == -1
               && !(pacmanW == enemyW && pacmanH == enemyH) && iMap[pacmanH * 14 + pacmanW] == 0) {
            System.out.println("Enemy:(" + enemyW + "," + enemyH + ") , pacman:(" + pacmanW + "," + pacmanH + ")");

            nodePath = pfAStar.FindPath(enemyW, enemyH, pacmanW, pacmanH);
            iCurrentPath = 0;

            while (nodePath != null) {
               iPath[iCurrentPath][0] = nodePath.x;
               iPath[iCurrentPath][1] = nodePath.y;
               // System.out.println("xy" + nodePath.x + " " + nodePath.y);
               iCurrentPath++;
               nodePath = nodePath.prev;
            }
            // System.out.println(iCurrentPath);
            iCurrentPath--;
            temp1 = f[iPath[iCurrentPath - 1][1]][iPath[iCurrentPath - 1][0]].getIcon(); // temp1에 empty or smallDot
            // System.out.println("ddd" + iPath[iCurrentPath-1][1] + " " +
            // iPath[iCurrentPath-1][0]);
            f[enemyH][enemyW].setIcon(temp1); // 적위치의 이미지를 empty로 세팅 (temp 초기 empty)

            enemyW = iPath[iCurrentPath - 1][0];
            enemyH = iPath[iCurrentPath - 1][1];
            f[enemyH][enemyW].setIcon(enemy); // 적위의 이미지를 enemy으로 세팅
            iCurrentPath--;

         }
         if (numOfDot <= 0) {
            dialog.add(win);
            dialog.setVisible(true);
            numOfDot = 1;
         }
         if (enemyH == pacmanH && enemyW == pacmanW) { // 플레이어 위치랑 적 위치가 같을 때
            f[enemyH][enemyW].setIcon(enemy);
            dialog.add(button);
            dialog.setVisible(true);
            setVisible(false);

            pacmanH = 100;
         }
      }
   }

   class enemy implements Runnable { // timer클래스(적 움직임) ActionListener
      public void run() {
         TListener tListener = new TListener(); // timer 클래스, 적 이동 클래스
         Timer t = new Timer(300, tListener);
         if (pacmanH != 100) {
            t.start();
         } else {

         }
      }
   }
   class tiktok implements ActionListener
   {
      @Override
      public void actionPerformed(ActionEvent e) {
         // TODO Auto-generated method stub
         System.out.print(++time);
         System.out.print("tik tok\n");
      }
   }
   class time implements Runnable { 
      public void run() {
         tiktok tt = new tiktok();
         Timer ts = new Timer(500, tt);
         ts.start();
      }
   }
   
   class ti implements Runnable { // timer클래스(적 움직임) ActionListener
      public void run() {
         TListener tListener = new TListener(); // timer 클래스, 적 이동 클래스
         Timer t = new Timer(300, tListener);
         if (pacmanH != 100) {
            t.start();
         } else {

         }
      }
   }

}