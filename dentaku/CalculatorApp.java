import javax.swing.SwingUtilities;

public class CalculatorApp{
	/*関数ヘッダー：このプログラムのスタート地点を宣言している。
	詳細：swingのユティリティークラスの内のinvokeLaterメソッドを使い、
	GUIの処理を 「 正しいタイミングかつ、正しいスレッド 」で実行するための仕組み。
	※GUI 画面を見て操作（ボタン・マウス）
	※CUI 文字だけ(consoleなど)
	※UI 人が操作する「見える・触れる部分」ボタン 文字入力欄 メニュー 画面レイアウト クリック・タップできるところ。
	*/
	public static void main(String [] args) {
		SwingUtilities.invokeLater(() -> {
            CalculatorFrame view = new CalculatorFrame();    // GUIの初期化
            
            
        });
	}
}