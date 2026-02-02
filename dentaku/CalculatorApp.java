import javax.swing.SwingUtilities;

public class CalculatorApp{
	public static void main(String [] args) {
		SwingUtilities.invokeLater(() -> {
			//GUI 画面を見て操作（ボタン・マウス）
			//CUI 文字だけ(consoleなど)
            CalculatorFrame view = new CalculatorFrame();    // GUIの初期化
            
            
        });
	}
}