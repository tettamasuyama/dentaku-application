import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



/* 関数ヘッダー：　JFrame(ウィンドウ、画面そのものを作るクラス)を継承し、
画面(setDisplayメソッド)やボタン操作(bindControllerメソッド)の部分を
配置する処理を行うクラス（コンストラクターあり）

*/
public class CalculatorFrame extends JFrame {
	
	
	 /* ディスプレイクラス（文字）インスタンス化 */
	private JLabel displayLabel;
	
	 /* コントローラークラス　インスタンス化 */
    private CalculatorController controller;
    

		/* 関数ヘッダー：コンストラクターの処理

		詳細：下記の処理を実行

		controller変数：CalculatorControllerクラスのメソッドを呼び出すためや、
			　　　　　　　そのクラスのコンストラクターを実行するためのもの

		setDisplay()：画面のスタイルを設定するメソッド

		bindController()：ボタンやパネル(操作の部分)のスタイルを設定するメソッド

		*/
		public CalculatorFrame() {
			
			controller = new CalculatorController(this);
			
			setDisplay();
			
			bindController();
		}



		/* 関数ヘッダー：画面のスタイルを設定するメソッド

		詳細：JFrameクラスを継承しているためそのクラスのメソッドを
		使用し、ウィンドウスタイル、画面の位置などを整えていく。

		
		*/
	
		public void setDisplay() {

		// ウィンドウタイトル
		setTitle("Calculator");  
        setSize(400, 300);
        //ウィンドウの × ボタンを押したときに、プログラムを完全に終了させる設定
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 画面中央
        setLocationRelativeTo(null); 
        
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
        
        
        /* 関数ヘッダー：ボタンやパネル(操作の部分)のスタイルを設定するメソッド
		
		詳細：JPanelクラスのメソッドやJButtonクラスのメソッドを利用して、部分の
		スタイルを整えていく処理。
		また、コンストラクター処理内でCalculatorControllerをインスタンス化しているので、
		その変数を使って、そのクラスのメソッド"onButtonPressed(text)"を呼び出す。

		*/
        
        public void bindController() {
        
        //ボタン下のパネル
        JPanel keypadPanel = new JPanel();
		//(行, 列, 横の間隔, 縦の間隔)
        keypadPanel.setLayout(new GridLayout(5,4,5,5)); 
        keypadPanel.setOpaque(true);
		//パネルの背景色
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
        	    
        	    
        	    /* 　関数ヘッダー：ボタン次第で、CalculatorControllerクラスを
				　　　　　　　　　　インスタンス化したメソッドを　"実行"　( イベントリスナー )*/
				
        	    btn.addActionListener(e->{
        	    	
        	    	//controllerのメソッドを呼び出す
        	    	controller.onButtonPressed(text);
        	    	
        	    });
        	    
        	    
        	    
        	}
        	
        
        	//表示
        	 add(keypadPanel);
        	 
        	 //フレームに追加
        	 //これがないと表示されない
             setVisible(true);
             
        
        
        
	}
        /* 関数ヘッダー：displayLabelクラス（文字）から表示を更新するためのメソッド
		(CalculatorControllerクラスで利用する)*/

        public void updateDisplay(String text) {
        	
            displayLabel.setText(text);
        }
        
	
        
}