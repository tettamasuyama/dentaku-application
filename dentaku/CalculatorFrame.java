import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 電卓アプリケーションのGUI（画面）を構築するクラス。
 *
 * <p>
 * JFrame を継承し、電卓の表示画面やボタン配置など
 * ユーザーインターフェースを管理する役割を持つ。
 * </p>
 *
 * <p>
 * このクラスは主に次の処理を行う。
 * </p>
 *
 * <ul>
 * <li>電卓の表示画面の作成</li>
 * <li>ボタンレイアウトの設定</li>
 * <li>ユーザー操作を Controller に通知</li>
 * <li>計算結果の表示更新</li>
 * </ul>
 *
 */
public class CalculatorFrame extends JFrame {

	/** 計算結果を表示するディスプレイラベル */
	private JLabel displayLabel;

	/** ボタン操作を処理するコントローラー */
	private CalculatorController controller;

	/**
	 * CalculatorFrame のコンストラクタ。
	 * <p>
	 * このコンストラクタでは以下の処理を行う。
	 * </p>
	 *
	 * <ul>
	 * <li>CalculatorController のインスタンスを作成</li>
	 * <li>画面レイアウトの初期設定</li>
	 * <li>ボタン操作とコントローラーの関連付け</li>
	 * </ul>
	 */
	public CalculatorFrame() {

		controller = new CalculatorController(this);

		setDisplay();

		bindController();
	}

	/**
	 * 電卓画面の表示設定を行うメソッド。
	 *
	 * <p>
	 * JFrame の基本設定と、
	 * 計算結果を表示するラベルの初期化を行う。
	 * </p>
	 */

	public void setDisplay() {

		// ウィンドウタイトル
		setTitle("Calculator");
		setSize(400, 300);
		// ウィンドウの × ボタンを押したときに、プログラムを完全に終了させる設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 画面中央
		setLocationRelativeTo(null);

		// 表示欄（上）
		// 初期値 0
		displayLabel = new JLabel("0");

		// 右寄せ
		displayLabel.setHorizontalAlignment(JTextField.RIGHT);
		// 大きめ・等幅フォント
		displayLabel.setFont(new Font("Arial", Font.BOLD, 40));

		// 文字の余白の幅
		// 表示欄（JLabel）の内側に余白（パディング）を作る
		// (上, 左, 下, 右)
		displayLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// JFrame 全体の部品配置ルールを BorderLayout にする
		setLayout(new BorderLayout());

		// displayLabel を画面の上（NORTH）に配置する
		// 画面の上側に表示する
		add(displayLabel, BorderLayout.NORTH);

	}

	/**
	 * 電卓ボタンのレイアウトを作成し、
	 * 各ボタンにイベントリスナーを設定するメソッド。
	 *
	 * <p>
	 * ボタンが押された場合、
	 * {@link CalculatorController#onButtonPressed(String)}
	 * メソッドを呼び出して処理を Controller に渡す。
	 * </p>
	 */

	public void bindController() {

		// ボタン下のパネル
		JPanel keypadPanel = new JPanel();
		// (行, 列, 横の間隔, 縦の間隔)
		keypadPanel.setLayout(new GridLayout(5, 4, 5, 5));
		keypadPanel.setOpaque(true);
		// パネルの背景色
		keypadPanel.setBackground(java.awt.Color.WHITE);

		//// 表示
		String[] buttons = {
				"7", "8", "9", "÷",
				"4", "5", "6", "×",
				"1", "2", "3", "-",
				"0", ".", "=", "+",
				"C"
		};

		for (String text : buttons) {

			JButton btn = new JButton(text);
			keypadPanel.add(btn);

			// ボタンのレイアウト
			btn.setOpaque(true);
			btn.setContentAreaFilled(true);
			btn.setBorderPainted(true);
			btn.setBackground(java.awt.Color.LIGHT_GRAY);
			btn.setFont(new Font("Arial", Font.BOLD, 18));

			/*
			 * 関数ヘッダー：ボタン次第で、CalculatorControllerクラスを
			 * インスタンス化したメソッドを "実行" ( イベントリスナー )
			 */

			btn.addActionListener(e -> {

				// controllerのメソッドを呼び出す
				controller.onButtonPressed(text);

			});

		}

		// 表示
		add(keypadPanel);

		// フレームに追加
		// これがないと表示されない
		setVisible(true);

	}

	/**
	 * ディスプレイの表示内容を更新するメソッド。
	 *
	 * <p>
	 * Controller クラスから呼び出され、
	 * 計算結果や入力値を画面に表示する。
	 * </p>
	 *
	 * @param text 表示する文字列
	 */
	public void updateDisplay(String text) {

		displayLabel.setText(text);
	}

}