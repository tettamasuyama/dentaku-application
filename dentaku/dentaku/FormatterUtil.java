import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;


public class FormatterUtil {

    
    private static final int MAX_SIGNIFICANT_DIGITS = 8;


    /*
    関数ヘッダー：format()はCalculatorModelクラスのequalsOp()のtryの部分で使用。

    詳細：doubleの数値をきれいな文字列に変換するメソッド
    
    */
    public static String format(double value) {

        if (value == 0) return "0";

        // double誤差対策
        BigDecimal bd = BigDecimal.valueOf(value)
                //文字列軽油で正確な値を作り、有効数字を8桁にまとめる。
                .round(new MathContext(MAX_SIGNIFICANT_DIGITS, RoundingMode.HALF_UP))
                 //小数点後の不要な0を削除する
                .stripTrailingZeros();
                
        String plain = bd.toPlainString();

        // 符号と小数点を除いた桁数
        int digitCount = plain
                .replace("-", "")
                .replace(".", "")
                .length();

        // 表示桁数が8を超えたら指数表記
        if (digitCount > MAX_SIGNIFICANT_DIGITS) {

            //四捨五入、5以上なら切り上げ
            //DecimalFomatを使って、数値を好きな形式に整えるためのクラス
            DecimalFormat df = new DecimalFormat("0.#######E0");
            df.setRoundingMode(RoundingMode.HALF_UP);

            //文字列変換（Eも含め）
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

