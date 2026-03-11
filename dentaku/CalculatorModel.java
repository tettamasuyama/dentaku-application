import java.util.ArrayList;
import java.util.List;

/** 
 * 電卓アプリケーションの Model クラス。 
 *
 * <p> 
 * MVCアーキテクチャにおいて Model の役割を担当する。 
 * このクラスは次の処理を管理する。 
 * </p> 
 * <ul> 
 * <li>数値や演算子の入力状態の管理</li> 
 * <li>式の文字列管理</li> 
 * <li>計算処理</li> 
 * <li>表示用文字列の提供</li> 
 * </ul> 
 * 
 * <p> 
 * Controller から呼び出され、入力に応じて内部状態を更新する。 
 * </p> 
 * 
 */

public class CalculatorModel {
    /** 入力できる最大桁数（小数点を除く） */
    private static final int MAX_DIGITS = 8;
    /** 現在の表示文字列 */
    private String current = "0";
    /** 計算直後かどうかの判定 */
    private boolean calculated = false;
    /** エラー状態かどうか */
    private boolean errorState = false;

    // 入力系メソッド

    /** 
    * 数字入力を処理するメソッド。 
    * 
    * <p> 
    * 入力された数字を現在の式に追加する。 
    * </p> 
    * 
    * <p> 
    * 主な処理： 
    * </p> 
    * <ul> 
    * <li>桁数制限のチェック</li> 
    * <li>先頭ゼロ（000など）の防止</li> 
    * <li>現在の数値部分の取得</li> 
    * </ul> 
    * 
    * @param digit 入力された数字 
    * @return 入力が成功した場合 true 
    */

    public boolean appendDigit(char digit) {

        // Error中は入力不可
        if (errorState)
            return false;

        if (!Character.isDigit(digit))
            return false;

        // 桁数制限（小数点除外）
        if (getDigitCount() >= MAX_DIGITS) {
            return false;
        }

        // 最後の数値部分を取得
        int lastOpIndex = -1;
        for (int i = current.length() - 1; i >= 0; i--) {
            if (isOperation(current.charAt(i))) {
                lastOpIndex = i;
                break;
            }
        }

        String before = current.substring(0, lastOpIndex + 1);
        String number = current.substring(lastOpIndex + 1);

        // 整数部が 0 だけ（00, 000 を含む）かつ小数点なし
        boolean isZeroInteger = !number.contains(".") &&
                number.length() == 1 &&
                number.charAt(0) == '0';

        // 「0」のとき
        if (isZeroInteger) {

            if (digit == '0') {
                // 000防止
                return false;

            } else {
                // 0 → 1〜9 に置き換え
                current = before + digit;
                return true;
            }
        }

        current += digit;
        return true;
    }

    /** 
    * 演算子入力を処理するメソッド。 
    * 
    * <p> 
    * 現在の数式に演算子を追加する。 
    * </p> 
    * 
    * <p> 
    * 以下のルールを考慮する： 
    * </p> 
    * 
    * <ul> 
    * <li>演算子の連続入力の調整</li> 
    * <li>負号の入力処理</li> 
    * <li>小数点直後の演算子の修正</li> 
    * </ul> 
    * 
    * @param operator 入力された演算子 
    */

    public void inputOperator(String operatior) {

        if (errorState)
            return;

        char characterOp = operatior.charAt(0);

        // 演算子 + 負号」の状態
        if (isAfterOperatorSign()) {

            // - を押した場合は維持
            if (characterOp == '-') {
                return;
            }

            // それ以外は「演算子 + 負号」を丸ごと置き換え
            current = current.substring(0, current.length() - 2) + characterOp;
            calculated = false;
            return;
        }

        // 負号単体
        if (current.equals("-")) {
            return;
        }

        char last = current.charAt(current.length() - 1);

        // 小数点直後に演算子 → 小数点を削除
        if (last == '.') {
            current = current.substring(0, current.length() - 1);
            // ※ return しない！
        }

        // 先頭の負号
        if (current.equals("0") && characterOp == '-') {
            current = "-";
            return;
        }

        // 通常の演算子処理
        if (isOperation(last)) {

            // ＋ の直後の - は「演算子置き換え」
            if (last == '+' && characterOp == '-') {
                current = current.substring(0, current.length() - 1) + '-';
                return;
            }

            // × ÷ の直後の - は「負号」
            if ((last == '×' || last == '÷' || last == '*' || last == '/')
                    && characterOp == '-') {
                current += '-';
                return;
            }

            // 演算子置き換え
            current = current.substring(0, current.length() - 1) + characterOp;

        } else {
            current += characterOp;
        }

        calculated = false;

    }

    /** 
    * 現在の数式が「演算子 + 負号」の状態かどうかを判定するメソッド。 
    * 
    * <p> 
    * 例： 
    * </p> 
    * <ul> 
    * <li>「5×-」 → true</li> 
    * <li>「8÷-」 → true</li> 
    * <li>「5+-」 → true</li> 
    * </ul> 
    * 
    * <p> 
    * 直前の文字（prev）が演算子であり、 
    * 最後の文字（last）が負号「-」である場合に true を返す。 
    * </p> 
    * 
    * <p> 
    * この判定は {@link #inputOperator(String)} メソッドで使用され、 
    * 「演算子 + 負号」の状態で新しい演算子が入力された場合の 
    * 置き換え処理に利用される。 
    * </p> 
    * 
    * @return 「演算子 + 負号」の状態であれば true 
    */

    // 判定系
    private boolean isAfterOperatorSign() {
        if (current.length() < 2)
            return false;

        char prev = current.charAt(current.length() - 2);
        char last = current.charAt(current.length() - 1);

        return isOperation(prev) && last == '-';
    }

    /** 
    * イコール入力時の処理を行うメソッド。 
    * 
    * <p> 
    * 現在の式を計算し、結果を表示用文字列として保存する。 
    * </p> 
    * 
    * <p> 
    * 計算中にエラーが発生した場合は 
    * "Error" を表示しエラー状態にする。 
    * </p> 
    */

    public void equalsOp() {
        // エラー出力
        if (errorState)
            return;

        char last = current.charAt(current.length() - 1);
        if (isOperation(last))
            return;

        try {
            double result = evaluate(current);
            current = FormatterUtil.format(result);
            calculated = true;

        } catch (Exception e) {
            ErrorHandler.handle(e); // ログだけ
            current = "Error";
            errorState = true;

        }
    }

    /**
    * 小数点入力を処理するメソッド。 
    * 
    * <p> 
    * 現在の数値部分に小数点を追加する。 
    * ただし以下の条件を満たす場合のみ追加される。 
    * </p> 
    * 
    * <ul> 
    * <li>既に小数点が存在しない</li> 
    * <li>演算子直後の場合は "0." を補完する</li> 
    * </ul> 
    * 
    * @return 小数点が追加された場合 true 
    */

    public boolean appendDot() {

        // Error中は入力不可
        if (errorState)
            return false;

        // 計算直後の . は「結果に追加」
        if (calculated) {
             if (current.contains("e") || current.contains("E")) {
                return false;
            }
            // すでに . があれば何もしない
            if (current.contains(".")) {
                return false;
            }
            calculated = false;
            current += ".";
            return true;
        }

        if (!canAddDot())
            return false;

        char last = current.charAt(current.length() - 1);

        // 演算子直後 → 0. を補完
        if (isOperation(last)) {
            current += "0.";
            return true;
        }

        current += ".";
        return true;
    }

    /**
    * 電卓の状態を初期状態にリセットするメソッド。
    *
    * <p>
    * このメソッドは現在の入力内容や計算状態をすべて初期化する。
    * </p>
    *
    * <ul>
    * <li>表示文字列を "0" に戻す</li>
    * <li>計算直後フラグ（calculated）を false にする</li>
    * <li>エラー状態フラグ（errorState）を false にする</li>
    * </ul>
    *
    * <p>
    * 主に C ボタンが押されたときに Controller から呼び出される。
    * </p>
    */

    public void clearAll() {
        current = "0";
        calculated = false;
        errorState = false;
    }

    /**
    * 表示用文字列を取得するメソッド。 
    * 
    * <p> 
    * Controller が View の表示更新に使用する。 
    * </p> 
    * 
    * @return 現在の表示文字列 
    */
    
    public String getDisplayText() {
        return current;
    }

    /** 
    * 数式文字列を解析して計算するメソッド。 
    * 
    * <p> 
    * 次の手順で計算を行う。 
    * </p> 
    * 
    * <ol> 
    * <li>数式を解析して数値と演算子に分割</li> 
    * <li>負の数を正しく解釈</li> 
    * <li>指数表記（e/E）を処理</li> 
    * <li>乗算・除算を先に計算</li> 
    * <li>加算・減算を計算</li> 
    * </ol> 
    * 
    * @param expr 計算対象の数式 
    * @return 計算結果 
    * @throws ArithmeticException 0除算が発生した場合 
    */

    private double evaluate(String expr) {

        // 数式を計算できる記号に入れ替える 「 × → * 」、「 ÷ → / 」
        expr = expr.replace("×", "*").replace("÷", "/");
        // 数字を格納する配列
        List<Double> nums = new ArrayList<>();
        // 演算記号を格納する配列
        List<Character> ops = new ArrayList<>();

        // 1. 負の数を正しく読み取る処理

        int i = 0;
        // 繰り返し処理を行い 1 文字ずつ取り出していく
        while (i < expr.length()) {
            char character = expr.charAt(i);
            // " - "かつ、一番はじめの文字か直前が演算子か
            if (character == '-' && (i == 0 || "+*/".indexOf(expr.charAt(i - 1)) >= 0)) {

                // 桁数内かつ、数字、小数点が次の桁に存在するのであればtrueを返して、変数jに直接上書き。
                int j = i + 1;
                while (j < expr.length() &&
                        (Character.isDigit(expr.charAt(j)) || expr.charAt(j) == '.')) {
                    j++;
                }
                // いま読み込んでいるiの値から次の値ｊを抜き取り数字変換させてリストに追加
                nums.add(Double.parseDouble(expr.substring(i, j)));
                // 次の数字を読み込むことが可能である。
                i = j;
            }

            // 指数表記
            // 数字か、小数点か、eまたはEのとき
            else if (Character.isDigit(character) || character == '.' || character == 'e' || character == 'E') {

                int j = i;
                // 繰り返し処理を行う上で、
                while (j < expr.length()) {

                    char Jcharacter = expr.charAt(j);
                    // 数字か小数点を読み取った場合、ループは進み、
                    if (Character.isDigit(Jcharacter) || Jcharacter == '.') {
                        j++;
                    }
                    // 指数部を含めた文字数の指定と'e'または'E'を許可
                    else if ((Jcharacter == 'e' || Jcharacter == 'E') && j + 1 < expr.length()) {
                        j++;
                        // 指数部の符号を許可
                        if (expr.charAt(j) == '+' || expr.charAt(j) == '-') {
                            j++;
                        }
                    } else {
                        // その他はループ抜ける処理が必要
                        break;
                    }
                }

                // いま読み込んでいるiの値から次の値ｊを抜き取り数字変換させてリストに追加
                // Double.parseDouble()の変換機能は指数表記をも理解ができる。
                // そのため、'e'も「数字の一部」として読み込まないといけない。
                nums.add(Double.parseDouble(expr.substring(i, j)));

                // 次の数字へ移動
                i = j;
            } else {
                // 負号、数字、小数点、eまたはE以外、つまるところ、演算記号は
                // 演算記号専用のリストに追加。
                ops.add(character);
                i++;
            }
        }

        // × ÷
        // ×、÷ を優先するため先に書く。

        // 演算記号を格納する配列の数分繰り返し処理を行う
        for (int idx = 0; idx < ops.size(); idx++) {
            if (ops.get(idx) == '*' || ops.get(idx) == '/') {

                // getメソッド意味・・・指定した値(index)を取り出す。
                double left = nums.get(idx);
                double right = nums.get(idx + 1);

                // 0で割った場合のエラー表記
                if (ops.get(idx) == '/' && right == 0) {
                    throw new ArithmeticException("0 division");
                }

                // 三項演算子
                // 条件式、？、： の3つの構成で成り立つ
                // 条件式を通して、true であれば、左をの結果を返して、falseであれば右の結果を返す。
                double result = ops.get(idx) == '*' ? left * right : left / right;

                // 古い数を計算結果で書き換える
                nums.set(idx, result);
                // 計算結果の右の数が残っているので消す
                nums.remove(idx + 1);
                // 使い終わった演算子も削除
                ops.remove(idx);
                // 位置調整
                idx--;
            }
        }

        // ＋ −
        // プリミティブ型の変数resultは書き換えた結果を返す。
        double result = nums.get(0);
        //
        for (int idx = 0; idx < ops.size(); idx++) {

            if (ops.get(idx) == '+') {
                // 複合代入演算で変数resultの数に足す。
                result += nums.get(idx + 1);
            } else {
                // 複合代入演算で変数resultの数に引く。
                result -= nums.get(idx + 1);
            }
        }
        // 結果を返す
        return result;
    }

    // 判定系

    /** 
    * 指定された文字が演算子かどうかを判定する。 
    * 
    * @param characterOperator 判定対象の文字 
    * @return 演算子であれば true 
    */

    private boolean isOperation(char characterOperator) {
        return characterOperator == '+' || characterOperator == '-'
                || characterOperator == '*' || characterOperator == '/' ||
                characterOperator == '×' || characterOperator == '÷';
    }

    /** 
    * 現在の数値部分に小数点を追加できるか判定する。 
    * 
    * @return 小数点を追加可能な場合 true 
    */

    private boolean canAddDot() {
        int lastOpIndex = -1;
        for (int i = current.length() - 1; i >= 0; i--) {
            if (isOperation(current.charAt(i))) {
                lastOpIndex = i;
                break;
            }
        }
        String lastNumber = current.substring(lastOpIndex + 1);
        return !lastNumber.contains(".");
    }

    /** 
    * 現在の数値部分の桁数を取得する。 
    * 
    * <p> 
    * 小数点は桁数に含めない。 
    * </p> 
    * 
    * @return 桁数 
    */

    private int getDigitCount() {
        int lastOpIndex = -1;
        for (int i = current.length() - 1; i >= 0; i--) {
            if (isOperation(current.charAt(i))) {
                lastOpIndex = i;
                break;
            }
        }
        String lastNumber = current.substring(lastOpIndex + 1);
        return lastNumber.replace(".", "").length();
    }
}
