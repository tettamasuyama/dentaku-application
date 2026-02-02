import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;


public class FormatterUtil {

    
    private static final int MAX_SIGNIFICANT_DIGITS = 8;

    public static String format(double value) {

        if (value == 0) return "0";

        // double誤差対策
        BigDecimal bd = BigDecimal.valueOf(value)
                .round(new MathContext(MAX_SIGNIFICANT_DIGITS, RoundingMode.HALF_UP))
                .stripTrailingZeros();

        String plain = bd.toPlainString();

        // 符号と小数点を除いた桁数
        int digitCount = plain
                .replace("-", "")
                .replace(".", "")
                .length();

        // 表示桁数が8を超えたら指数表記
        if (digitCount > MAX_SIGNIFICANT_DIGITS) {

            DecimalFormat df = new DecimalFormat("0.#######E0");
            df.setRoundingMode(RoundingMode.HALF_UP);

            
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

