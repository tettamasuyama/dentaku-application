public class CalculatorController {

    // View と Model
    private CalculatorFrame view;
    private CalculatorModel model;

    public CalculatorController(CalculatorFrame view) {
        this.view = view;
        this.model = new CalculatorModel();
    }

    // ボタン入力の振り分け
  
    public void onButtonPressed(String key) {

        if (key.matches("[0-9]")) {
            onDigit(key);

        } else if (key.equals(".")) {
            onDot();

        } else if (key.equals("=")) {
            onEquals();

        } else if (key.equals("C")) {
            onClear();

        } else {
            onOperator(key);
        }

        // 表示更新は必ずここで一元管理
        view.updateDisplay(model.getDisplayText());
    }

    
    // 各入力処理
    

    // 数字
    private void onDigit(String key) {
        model.appendDigit(key.charAt(0));
    }

    // 演算子
    private void onOperator(String key) {
        model.inputOperator(key);
    }

    // ドット
    private void onDot() {
        model.appendDot();
    }

    // イコール
    private void onEquals() {
        model.equalsOp();
    }

    // クリア
    private void onClear() {
        model.clearAll();
    }
}