/**
 * 電卓アプリケーションのコントローラークラス。
 *
 * <p>
 * MVCアーキテクチャにおいて Controller の役割を持ち、
 * ユーザーが押したボタン入力を受け取り、
 * 適切な Model の処理を呼び出す。
 * </p>
 *
 * <p>
 * また、処理結果を View に渡して画面表示を更新する。
 * </p>
 *
 */
public class CalculatorController {

    /** 画面表示を担当する View */
    private CalculatorFrame view;

    /** 計算処理や状態管理を担当する Model */
    private CalculatorModel model;

    /**
     * CalculatorController のコンストラクタ。
     *
     * <p>
     * View を受け取り、Model を生成する。
     * </p>
     *
     * @param view 電卓画面（CalculatorFrame）
     */
    public CalculatorController(CalculatorFrame view) {
        this.view = view;
        this.model = new CalculatorModel();
    }

    /**
     * ボタン入力を判定し、対応する処理メソッドへ振り分ける。
     *
     * <p>
     * 入力されたキーの種類によって、
     * 数字入力・小数点・演算子・イコール・クリアなどの
     * 処理を実行する。
     * </p>
     *
     * <p>
     * 最後に Model から表示用文字列を取得し、
     * View の表示を更新する。
     * </p>
     *
     * @param key 押されたボタンの文字列
     */
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

        // 表示更新はここで一元管理
        view.updateDisplay(model.getDisplayText());
    }

    // 各入力処理

    /**
     * 数字ボタンが押された場合の処理。
     *
     * @param key 押された数字
     */
    private void onDigit(String key) {
        model.appendDigit(key.charAt(0));
    }

    /**
     * 小数点ボタンが押された場合の処理。
     */
    private void onDot() {
        model.appendDot();
    }

    /**
     * イコールボタンが押された場合の処理。
     */
    private void onEquals() {
        model.equalsOp();
    }

    /**
     * クリアボタンが押された場合の処理。
     */
    private void onClear() {
        model.clearAll();
    }

    /**
     * 演算子ボタンが押された場合の処理。
     *
     * @param key 押された演算子（+, -, ×, ÷ など）
     */
    private void onOperator(String key) {
        model.inputOperator(key);
    }
}