import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MyMap{
  int hight,width;
  int timeCount=0;
  boolean isStart=false;
  int[] dx={1,1,0,-1,-1,-1,0,1};
  int[] dy={0,1,1,1,0,-1,-1,-1};
  int[][] mapdata;
  JButton[][] buttons;
  JLabel countText;
  final int chipSize=10;

  JFrame jframe;
  JPanel jpanel = new JPanel();

  public MyMap(int hight,int width){
    //init
    this.hight=hight;
    this.width=width;
    mapdata = new int[hight][width];

    for(int i=0;i<hight;i++){
      for(int j=0;j<width;j++){
        mapdata[i][j]=0;
      }
    }

    initWindow();
    print();
  }

  void initWindow(){
    jframe = new JFrame("LifeGame");
    jframe.getContentPane().setBackground(Color.BLACK);

    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jframe.setSize(chipSize * hight,chipSize * width+100);

    JButton startButton = new JButton("START");
    startButton.setBounds(20,20, 100, 20);
    startButton.addActionListener(new StartAction());
    jpanel.add(startButton);

    JButton stopButton = new JButton("STOP");
    stopButton.setBounds(20,50, 100, 20);
    stopButton.addActionListener(new StopAction());
    jpanel.add(stopButton);

    JButton resetButton = new JButton("RESET");
    resetButton.setBounds(150,20, 100, 20);
    resetButton.addActionListener(new ResetAction());
    jpanel.add(resetButton);

    countText = new JLabel("Time : "+ timeCount);
    countText.setBounds(280,20, 100, 20);
    jpanel.add(countText);

    try{
      UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
    }catch(Exception e) {
      e.printStackTrace();
    }

    buttons= new JButton[hight][width];
    jpanel.setLayout(null);
    //button
    for(int i=0;i<hight;i++){
      for(int j=0;j<width;j++){
        buttons[i][j] = new JButton();
        buttons[i][j].setBounds(i*chipSize, 100+j*chipSize, chipSize, chipSize);
        buttons[i][j].setBackground(Color.RED);
        buttons[i][j].addActionListener(new ClickAction(i,j));
        jpanel.add(buttons[i][j]);
      }
    }

    jframe.getContentPane().add(jpanel,BorderLayout.CENTER);
    jframe.setVisible(true);
  }

  class StartAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
      isStart=true;
		}
  }
  class StopAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
      isStart=false;
		}
  }
  class ResetAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
      for(int i=0;i<hight;i++){
        for(int j=0;j<width;j++){
          mapdata[i][j]=0;
        }
      }
      timeCount=0;
      print();
    }
  }
  class ClickAction implements ActionListener {
    int i,j;
    public ClickAction(int x,int y){
      i=x;j=y;
    }

		@Override
		public void actionPerformed(ActionEvent e) {
      System.out.println("button" + i + j);
      if(mapdata[i][j]==1)mapdata[i][j]=0;
      else mapdata[i][j]=1;
      print();
		}
	}

  void set(int i,int j,int x){
    mapdata[i][j]=x;
  }

  void print(){
    countText.setText("Time : "+timeCount);
    for(int i=0;i<hight;i++){
      for(int j=0;j<width;j++){
        System.out.print(mapdata[i][j]);
        if(mapdata[i][j]==1){
          buttons[i][j].setBackground(Color.RED);
        }else{
          buttons[i][j].setBackground(Color.BLACK);
        }
      }
      System.out.println();
    }
    jpanel.repaint();
  }

  void cal(){
    int[][] copymap =new int[hight][width];
    //copy
    for(int i=0;i<hight;i++){
      for(int j=0;j<width;j++){
        copymap[i][j]=mapdata[i][j];
      }
    }

    for(int i=0;i<hight;i++){
      for(int j=0;j<width;j++){
        int num=0;
        for(int k=0;k<8;k++){
          int nextX=i+dx[k];
          int nextY=j+dy[k];
          if(nextX<0 || nextX>=hight || nextY<0 || nextY>=width)continue;
          if(copymap[nextX][nextY]==1)num++;
        }

        if(copymap[i][j]==0 && num==3)mapdata[i][j]=1;
        else if(copymap[i][j]==1 && (num==2 || num==3))mapdata[i][j]=1;
        else if(num<=1)mapdata[i][j]=0;
        else if(num>=1)mapdata[i][j]=0;
        else mapdata[i][j]=0;
      }
    }
  }
}

public class LifeGame{
  public static void main(String args[])throws InterruptedException,IOException, InterruptedException{
    int hight=60;
    int width=60;
    
    MyMap map = new MyMap(hight, width);

    
    // 8角形のやつ
    // map.set(2,0,1);map.set(5,0,1);
    // map.set(2,1,1);map.set(5,1,1);
    // map.set(0,2,1);map.set(1,2,1);map.set(3,2,1);map.set(4,2,1);map.set(6,2,1);map.set(7,2,1);
    // map.set(2,3,1);map.set(5,3,1);
    // map.set(2,4,1);map.set(5,4,1);
    // map.set(0,5,1);map.set(1,5,1);map.set(3,5,1);map.set(4,5,1);map.set(6,5,1);map.set(7,5,1);
    // map.set(2,6,1);map.set(5,6,1);
    // map.set(2,7,1);map.set(5,7,1);

    // map.set(7,7,1);map.set(7,6,1);map.set(7,5,1);
    // map.set(6,7,1);
    // map.set(5,6,1);

    // map.print();

    while(true){
      if(map.isStart){
        map.cal();
        map.print();
        map.timeCount++;
      }
      Thread.sleep(100);
    }
  }
}