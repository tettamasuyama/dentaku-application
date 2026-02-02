import java.util.ArrayList;
import java.util.List;

public class CalculatorModel {

    private static final int MAX_DIGITS = 8;

    private String current = "0";
    private boolean calculated = false;
    
    //エラー表記の後に文字を数字を入力できないようにしたい。
    private boolean errorState = false;

    
       //入力系メソッド
    

    // 数字追加
    public boolean appendDigit(char digit) {

        // Error中は入力不可
        if (errorState) return false;

        if (!Character.isDigit(digit)) return false;

        // 計算直後ならリセット
        if (calculated) {
            calculated = false;
        }

        // 桁数制限（小数点除外）
        if (getDigitCount() >= MAX_DIGITS) {
            return false;
        }

        //a
        
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
        boolean isZeroInteger =
        		!number.contains(".") &&
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

    // 演算子
    
    public void inputOperator(String op) {
    	
    	  if (errorState) return;

    	    char c = op.charAt(0);

    	    // 演算子 + 負号」の状態 
    	    if (isAfterOperatorSign()) {

    	        // - を押した場合は維持
    	        if (c == '-') {
    	            return;
    	        }

    	        // それ以外は「演算子 + 負号」を丸ごと置き換え
    	        current = current.substring(0, current.length() - 2) + c;
    	        calculated = false;
    	        return;
    	    }

    	    //  負号単体 
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
    	    if (current.equals("0") && c == '-') {
    	        current = "-";
    	        return;
    	    }

    	    //  通常の演算子処理 
    	    if (isOperation(last)) {
    	    	
    	    	// ＋ の直後の - は「演算子置き換え」
    	    	if (last == '+' && c == '-') {
    	    	    current = current.substring(0, current.length() - 1) + '-';
    	    	    return;
    	    	}

    	    	// × ÷ の直後の - は「負号」
    	    	if ((last == '×' || last == '÷' || last == '*' || last == '/')
    	    	        && c == '-') {
    	    	    current += '-';
    	    	    return;
    	    	}

    	        // 演算子置き換え
    	        current = current.substring(0, current.length() - 1) + c;

    	    } else {
    	        current += c;
    	    }

    	    calculated = false;

    }

    //判定系
    private boolean isAfterOperatorSign() {
        if (current.length() < 2) return false;

        char prev = current.charAt(current.length() - 2);
        char last = current.charAt(current.length() - 1);

        return isOperation(prev) && last == '-';
    }
    
    // イコール

    
    public void equalsOp() {
        if (errorState) return;


        char last = current.charAt(current.length() - 1);
        if (isOperation(last)) return;

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

 // 小数点
    
    public boolean appendDot() {

        // Error中は入力不可
        if (errorState) return false;
        
        // 計算直後の . は「結果に追加」
        if (calculated) {
            // すでに . があれば何もしない
            if (current.contains(".")) {
                return false;
            }
            calculated = false;
            current += ".";
            return true;
        }

        
        //

        if (!canAddDot()) return false;

        char last = current.charAt(current.length() - 1);

        // 演算子直後 → 0. を補完
        if (isOperation(last)) {
            current += "0.";
            return true;
        }
        
        
        current += ".";
        return true;
    }
    
    // クリア
    public void clearAll() {
        current = "0";
        calculated = false;
        //
        errorState = false;
    }

    // 表示取得
    public String getDisplayText() {
        return current;
    }

    
     // 内部計算処理
    

    private double evaluate(String expr) {
        expr = expr.replace("×", "*").replace("÷", "/");

        List<Double> nums = new ArrayList<>();
        List<Character> ops = new ArrayList<>();

        int i = 0;
        while (i < expr.length()) {
            char c = expr.charAt(i);

            if (c == '-' && (i == 0 || "+*/".indexOf(expr.charAt(i - 1)) >= 0)) {
                int j = i + 1;
                while (j < expr.length() &&
                        (Character.isDigit(expr.charAt(j)) || expr.charAt(j) == '.')) {
                    j++;
                }
                nums.add(Double.parseDouble(expr.substring(i, j)));
                i = j;
            }


			else if (Character.isDigit(c) || c == '.' || c == 'e' || c == 'E') {
			
			    int j = i;
			
			    while (j < expr.length()) {
			        char cj = expr.charAt(j);
			
			        if (Character.isDigit(cj) || cj == '.') {
			            j++;
			        }
			        else if ((cj == 'e' || cj == 'E') && j + 1 < expr.length()) {
			            j++;
			            // 指数部の符号を許可
			            if (expr.charAt(j) == '+' || expr.charAt(j) == '-') {
			                j++;
			            }
			        }
			        else {
			            break;
			        }
			    }
			            
            
                nums.add(Double.parseDouble(expr.substring(i, j)));
                i = j;
            }
            else {
                ops.add(c);
                i++;
            }
        }

        // × ÷
        for (int idx = 0; idx < ops.size(); idx++) {
            if (ops.get(idx) == '*' || ops.get(idx) == '/') {
                double a = nums.get(idx);
                double b = nums.get(idx + 1);
                if (ops.get(idx) == '/' && b == 0) {
                    throw new ArithmeticException("0 division");
                }
                double r = ops.get(idx) == '*' ? a * b : a / b;

                nums.set(idx, r);
                nums.remove(idx + 1);
                ops.remove(idx);
                idx--;
            }
        }

        // ＋ −
        double result = nums.get(0);
        for (int idx = 0; idx < ops.size(); idx++) {
            if (ops.get(idx) == '+') {
                result += nums.get(idx + 1);
            } else {
                result -= nums.get(idx + 1);
            }
        }

        return result;
    }

    
       //判定系
    

    private boolean isOperation(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '×' || c == '÷';
    }

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
