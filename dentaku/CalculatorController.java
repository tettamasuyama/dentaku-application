public class CalculatorController {

    // View と Model
    private CalculatorFrame view;
    private CalculatorModel model;

    

    /* 関数ヘッダー：コンストラクターとして下記を処理

    private CalculatorFrame view・・・setterの役割
    private CalculatorModel model・・・modelクラスのインスタンス化

    ※ 引数 view　は " CalculatorFrameクラス "そのもの。
    ※ CMVモデルの場合はviewクラスを引数として渡す時もある。

    */
    public CalculatorController(CalculatorFrame view) {
        this.view = view;
        this.model = new CalculatorModel();
    }

    
    /* 関数ヘッダー：ボタン入力の振り分け
        詳細：CalculatorFrameクラスにてonButtonPressedメソッドを実行。
        内容として、押されたボタン値の"引数"から条件分岐でおのおのの処理を行う。

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

        
        /*
        表示更新は必ずここで一元管理
        
        CalculatorModelクラスのgetDisplayTextメソッドを引数に、
        CalculatorFrameの下記のメソッドを使い、表示を更新 */
        view.updateDisplay(model.getDisplayText());
    }

    
    // 各入力処理
    
    /* 関数ヘッダー：onButtonPressedの引数が"0～9"の場合行われる
    　　　　　　　　　CalclatorModelクラスのappendDight()メソッド。
    */
    private void onDigit(String key) {
        model.appendDigit(key.charAt(0));
    }
  
    /* 関数ヘッダー：onButtonPressedの引数が"."の場合行われる
    　　　　　　　　　CalclatorModelクラスのappendDot()メソッド。
    */
    private void onDot() {
        model.appendDot();
    }

    /* 関数ヘッダー：onButtonPressedの引数が"="の場合行われる
    　　　　　　　　　CalclatorModelクラスのequalsOp()メソッド。
    */
    // イコール
    private void onEquals() {
        model.equalsOp();
    }
    /* 関数ヘッダー：onButtonPressedの引数が"C"の場合行われる
    　　　　　　　　　CalclatorModelクラスのonCleart()メソッド。
    */
    private void onClear() {
        model.clearAll();
    }

    /* 関数ヘッダー：onButtonPressedの引数が"演算記号"の場合行われる
    　　　　　　　　　CalclatorModelクラスのinputOperator()メソッド。
    */
    private void onOperator(String key) {
        model.inputOperator(key);
    }
}