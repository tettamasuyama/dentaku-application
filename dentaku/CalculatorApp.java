import javax.swing.SwingUtilities;

/**
 * 電卓アプリケーションのエントリーポイントとなるクラス。
 * <p>
 * このクラスはアプリケーションを起動し、GUI（グラフィカルユーザーインターフェース）
 * を表示する役割を持つ。
 * </p>
 */
public class CalculatorApp {
	/**
	 * プログラムの開始地点（エントリーポイント）。
	 * <p>
	 * SwingUtilities の invokeLater メソッドを使用して、
	 * GUI の処理を Event Dispatch Thread（EDT）上で実行する。
	 * これにより、Swingアプリケーションを正しいスレッドで安全に起動できる。
	 * </p>
	 *
	 * <p>
	 * GUI（Graphical User Interface）とは、
	 * ボタン・テキスト入力欄・メニューなどの
	 * ユーザーが画面上で操作できる要素を持つインターフェースを指す。
	 * </p>
	 *
	 * <p>
	 * 対して CUI（Character User Interface）は、
	 * コンソールなどで文字によって操作するインターフェースである。
	 * </p>
	 *
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			CalculatorFrame view = new CalculatorFrame(); // GUIの初期化

		});
	}
}