import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import java.awt.*;
//import java.time.LocalDate;


public class CalculatorFrame extends JFrame {
	//GUI 画面を見て操作（ボタン・マウス）
	//CUI 文字だけ(consoleなど)
	
	//UI 人が操作する「見える・触れる部分」ボタン 文字入力欄 メニュー 画面レイアウト クリック・タップできるところ
	
	 //ディスプレイクラス（文字）インスタンス化
	private JLabel displayLabel;
	
	 //コントローラークラス　インスタンス化
    private CalculatorController controller;
    
	public CalculatorFrame() {
		
		controller = new CalculatorController(this);
		
		setDisplay();
		
		bindController();
	}
	
	public void setDisplay() {
		
		setTitle("Calculator");  // ウィンドウタイトル
        setSize(400, 300);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //ウィンドウの × ボタンを押したときに、プログラムを完全に終了させる設定
        
        setLocationRelativeTo(null); // 画面中央
        
        
        
        //表示欄（上）
        //初期値　0
        
        displayLabel = new JLabel("0");
        
        //右寄せ
        displayLabel.setHorizontalAlignment(JTextField.RIGHT);
        //大きめ・等幅フォント
        displayLabel.setFont(new Font("Arial", Font.BOLD, 40));
        
        //文字の余白の幅
        //表示欄（JLabel）の内側に余白（パディング）を作る
        //(上, 左, 下, 右)
        
        displayLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        //JFrame 全体の部品配置ルールを BorderLayout にする
        setLayout(new BorderLayout());

        //displayLabel を画面の上（NORTH）に配置する
        //画面の上側に表示する
        add(displayLabel, BorderLayout.NORTH);
        
        
		}
        
        
        
        
        public void bindController() {
        
        //ボタン(下)
        JPanel keypadPanel = new JPanel();
        keypadPanel.setLayout(new GridLayout(5,4,5,5)); 
        //(行, 列, 横の間隔, 縦の間隔)
       
        
        keypadPanel.setOpaque(true);
        keypadPanel.setBackground(java.awt.Color.WHITE);
        
       
        //// 表示
        
        
        String[] buttons = {
        	    "7","8","9","÷",
        	    "4","5","6","×",
        	    "1","2","3","-",
        	    "0",".","=","+",
        	    "C"
        	};

        
        	for (String text : buttons) {
        	    
        		JButton btn = new JButton(text);
        	    keypadPanel.add(btn);
        	    
        	    //ボタンのレイアウト
        	    btn.setOpaque(true);
        	    btn.setContentAreaFilled(true);
        	    btn.setBorderPainted(true);
        	    btn.setBackground(java.awt.Color.LIGHT_GRAY);
        	    btn.setFont(new Font("Arial", Font.BOLD, 18));
        	    
        	    
        	    
        	    //ボタン次第で、クラスインスタンス化したメソッドを呼び出す。
        	    
        	    
        	    
        	    btn.addActionListener(e->{
        	    	
        	    	//controllerのメソッドを呼び出す
        	    	controller.onButtonPressed(text);
        	    	
        	    });
        	    
        	    
        	    
        	}
        	
        	//ボタンの大きさを変えるために。
        
        	//表示
        	 add(keypadPanel);
        	 
        	 //フレームに追加
        	 //これがないと表示されない
             setVisible(true);
             //
        
        
        
	}
        
        // Controller から表示を更新するためのメソッド
        public void updateDisplay(String text) {
        	
            displayLabel.setText(text);
        }
        
	//getter
        
}