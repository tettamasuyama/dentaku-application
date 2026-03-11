import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 数値を電卓表示用の文字列に変換するユーティリティクラス。
 *
 * <p>
 * 計算結果の double 値を整形し、電卓表示に適した形式の
 * 文字列へ変換する。
 * </p>
 *
 * <p>
 * 主な処理：
 * </p>
 * <ul>
 * <li>double の誤差を BigDecimal で補正</li>
 * <li>有効数字を最大 8 桁に制限</li>
 * <li>不要な末尾の 0 を削除</li>
 * <li>桁数が多い場合は指数表記に変換</li>
 * </ul>
 *
 * <p>
 * CalculatorModel の {@link CalculatorModel#equalsOp()} メソッドから
 * 呼び出され、計算結果の表示整形を担当する。
 * </p>
 *
 */

public class FormatterUtil {

    private static final int MAX_SIGNIFICANT_DIGITS = 8;

    /**
     * 数値を電卓表示用の文字列に整形するメソッド。
     *
     * <p>
     * double 値を BigDecimal に変換し、有効数字を制限して
     * 表示用文字列を生成する。
     * </p>
     *
     * <p>
     * 桁数が上限を超える場合は指数表記（e 表記）で返す。
     * </p>
     *
     * @param value 整形する数値
     * @return 整形された文字列
     */
    public static String format(double value) {

        if (value == 0)
            return "0";

        // double誤差対策
        BigDecimal bd = BigDecimal.valueOf(value)
                // 文字列軽油で正確な値を作り、有効数字を8桁にまとめる。
                .round(new MathContext(MAX_SIGNIFICANT_DIGITS, RoundingMode.HALF_UP))
                // 小数点後の不要な0を削除する
                .stripTrailingZeros();

        String plain = bd.toPlainString();

        // 符号と小数点を除いた桁数
        int digitCount = plain
                .replace("-", "")
                .replace(".", "")
                .length();

        // 表示桁数が8を超えたら指数表記
        if (digitCount > MAX_SIGNIFICANT_DIGITS) {

            // 四捨五入、5以上なら切り上げ
            // DecimalFomatを使って、数値を好きな形式に整えるためのクラス
            DecimalFormat df = new DecimalFormat("0.#######E0");
            df.setRoundingMode(RoundingMode.HALF_UP);

            // 文字列変換（Eも含め）
            String exp = df.format(bd).toLowerCase();

            // 正の指数に + を付ける
            if (exp.matches(".*e\\d+$")) {
                exp = exp.replace("e", "e+");
            }

            return exp;
        }

        return plain;
    }

}
