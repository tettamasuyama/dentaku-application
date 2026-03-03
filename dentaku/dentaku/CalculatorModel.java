import java.util.ArrayList;
import java.util.List;




public class CalculatorModel {
    //文字数制限の変数
    private static final int MAX_DIGITS = 8;
    //デフォルト値
    private String current = "0";
    //計算後判定の変数
    private boolean calculated = false;
    //エラー表記の後に文字を数字を入力できないようにしたい。
    private boolean errorState = false;

    
       //入力系メソッド
    
/* 関数ヘッダー：数字の出力設定（引数：入力値）メソッド、CalculatorControllerのonDigit()で使用。

    詳細：主に、引数（char digit）を使って、デフォルト値(String current)に
    複合代入演算子( += )を使って" 数字 "を追加。

    それ以外の処理として、

    桁数（8文字）や、最後の数字（演算記号を基準に、以降、substring()メソッドで抜き取った数字）と
    その数字より前の数字と演算記号（演算記号を基準に、以前、substring()メソッドで抜き取った数字）を
    説明変数として宣言を行い、
    それを基準に、
    桁数制限、"0"を出力する際、"000"と連続させない様にすること、値が"0"の場合"1~9"を出力すると
    0が1～9の数字に書き換えることなどのルールを設定をする。

   ※ isOperation(char c):引数が（入力された文字1文字）が演算記号であれば結果としてその値をtrueで返すメソッド。
   
   ※ char c の値は繰り返し処理で順番として最後から得た"1文字分"の値である。
    例）123　→　3,2,1　の順に値を取得する。

*/
    public boolean appendDigit(char digit) {

        // Error中は入力不可
        if (errorState) return false;

        if (!Character.isDigit(digit)) return false;

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

    /* 関数ヘッダー：演算記号の出力設定（引数；入力値）メソッド、CalculatorControllerのonOperator()で使用。

    詳細： 詳細：主に、引数（String digit）を使って、デフォルト値(String current)に
    複合代入演算子( += )を使って" 演算記号 "を追加。

    それ以外の処理として、
    
    条件分岐にてisAfterOperatorSign()を使い、"× or ÷"の後に"-"の入力を可能や、
    演算子の置き換え、負号単体入力可、小数点直後に"-"を書き換え、先頭の数字が"0"単体の場合負号を書き換える
    などのルールを設定。

    ※isAfterOperatorSign():演算記号"×,÷"の後のみ、"-"の入力を可能にするためのメソッド（戻り値はtrueとして返す）。

*/


    
    public void inputOperator(String operatior) {
    	
    	  if (errorState) return;

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
    	    if (current.equals("0") && characterOp == '-') {
    	        current = "-";
    	        return;
    	    }

    	    //  通常の演算子処理 
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

    /* 関数ヘッダー：演算記号"×,÷"の後のみ、"-"の入力を可能にするためのメソッド（戻り値はtrueとして返す）
    メソッド、このクラスのinputOperator()で使用。


    詳細：
    char prev：current(出力されている値)の中で最後から2番目の値
    char last：current(出力されている値)の中で最後の値

    つまるところ、isOparation()でprevの値の演算子の結果かつ、
    その後、（last:最後の文字）で" - "を結果として返してあげる。


*/

    //判定系
    private boolean isAfterOperatorSign() {
        if (current.length() < 2) return false;

        char prev = current.charAt(current.length() - 2);
        char last = current.charAt(current.length() - 1);

        return isOperation(prev) && last == '-';
    }
    

    /* 関数ヘッダー："="の出力設定メソッド、CalculatorControllerのonEquals()で使用。

    詳細：char last　最後の文字が演算子の場合、計算は行われず、それ以外であれば
    evaluate()を通して、計算結果を出力する。
    また、calculated = true;　や　errorState = true;　のように計算結果後やError出力時に
    入力可能な設定を行うことだけでなく。
    catchにてエラーであれば、ErrorHandlerクラスのコーソール出力を行うことも処理として含まれている。

*/

    
    public void equalsOp() {
        //エラー出力
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

/* 関数ヘッダー：" . "の出力設定メソッド、CalculatorControllerのonDot()で使用。

　　詳細：主に、currentに小数点がすでに含まれているのであれば、何もせず、
　　　　　含まれていなければ、複合代入演算子（ += ）を使って小数点を追加する。
　　　　　また、この処理には小数点後に入力可能を判定するもの（calculated）も含まれており、
         canAddDot()を使って最後の値に小数点が無ければ、小数点を入力できないようにすることや、
         最後の文字が演算記号である場合、小数点を入力できる処理も含まれている。


    ※　canAddDot()：最後の文字に小数点がなければ、trueを返す。
*/

 
    
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

    /* 関数ヘッダー：" C "の出力設定メソッド、CalculatorControllerのonClea()で使用。

　　　詳細：初期値を0に更新と入力可否判定。

*/
    
    
    public void clearAll() {
        current = "0";
        calculated = false;
        errorState = false;
    }




    /* 関数ヘッダー：更新結果を文字列型にして結果を返す（ 表示取得 ）メソッド、
    CalculatorControllerのonButtonPressed()で使用。

    詳細：CalculatorControllerクラスにて、updateDisplay()メソッドの引数として、
　　　こちらの関数を利用する。

*/
    public String getDisplayText() {
        return current;
    }

    


    /* 関数ヘッダー：内部計算処理（引数：入力値）メソッド、
    このクラスのequalsOp()で使用。


         詳細：主に、文字列数字のものを、繰り返し処理にて1文字ずつ数字に変換しながら、リストに追加、
        リストは数字のものと( List<Double> nums　と　List<Character> ops ）で二つに分かれており、
        各配列の何番目かを特定し、特定した値を使って計算処理を行う。※1．


        構成は主に3つに分かれており、下記の通りである。
        
        1. 負の数を正しく読み取る処理
        2. 数字、小数点、eまたはEであれば、数字の始まりだと判断する処理
        3. 四則演算の優先順位を守って計算する処理

        ※1.数字は演算子で区切る

*/
     
    

    private double evaluate(String expr) {

        //数式を計算できる記号に入れ替える　「 ×　→　* 」、「 ÷　→　/ 」
        expr = expr.replace("×", "*").replace("÷", "/");
        //数字を格納する配列
        List<Double> nums = new ArrayList<>();
        //演算記号を格納する配列
        List<Character> ops = new ArrayList<>();

        // 1. 負の数を正しく読み取る処理

        int i = 0;
        //繰り返し処理を行い　1 文字ずつ取り出していく
        while (i < expr.length()) {
            char character = expr.charAt(i);
            //" - "かつ、一番はじめの文字か直前が演算子か
            if (character  == '-' && (i == 0 || "+*/".indexOf(expr.charAt(i - 1)) >= 0)) {

                //桁数内かつ、数字、小数点が次の桁に存在するのであればtrueを返して、変数jに直接上書き。
                int j = i + 1;
                while (j < expr.length() &&
                        (Character.isDigit(expr.charAt(j)) || expr.charAt(j) == '.')) {
                    j++;
                }
                //いま読み込んでいるiの値から次の値ｊを抜き取り数字変換させてリストに追加
                nums.add(Double.parseDouble(expr.substring(i, j)));
                //次の数字を読み込むことが可能である。
                i = j;
            }

        //指数表記
            //数字か、小数点か、eまたはEのとき
			else if (Character.isDigit(character) || character == '.' || character == 'e' || character == 'E') {
			
			    int j = i;
                //繰り返し処理を行う上で、
			    while (j < expr.length()) {

			        char Jcharacter = expr.charAt(j);
                    //数字か小数点を読み取った場合、ループは進み、
			        if (Character.isDigit(Jcharacter) || Jcharacter == '.') {
			            j++;
			        }
                    //指数部を含めた文字数の指定と'e'または'E'を許可
			        else if ((Jcharacter == 'e' || Jcharacter == 'E') && j + 1 < expr.length()) {
			            j++;
			            // 指数部の符号を許可
			            if (expr.charAt(j) == '+' || expr.charAt(j) == '-') {
			                j++;
			            }
			        }
			        else {
                        //その他はループ抜ける処理が必要
			            break;
			        }
			    }
			            
                //いま読み込んでいるiの値から次の値ｊを抜き取り数字変換させてリストに追加
                //Double.parseDouble()の変換機能は指数表記をも理解ができる。
                // そのため、'e'も「数字の一部」として読み込まないといけない。
                nums.add(Double.parseDouble(expr.substring(i, j)));

                //次の数字へ移動
                i = j;
            }
            else {
                //負号、数字、小数点、eまたはE以外、つまるところ、演算記号は
                //演算記号専用のリストに追加。
                ops.add(character);
                i++;
            }
        }

        // × ÷
        //×、÷　を優先するため先に書く。

        //演算記号を格納する配列の数分繰り返し処理を行う
        for (int idx = 0; idx < ops.size(); idx++) {
            if (ops.get(idx) == '*' || ops.get(idx) == '/') {

                //getメソッド意味・・・指定した値(index)を取り出す。
                double left = nums.get(idx);
                double right = nums.get(idx + 1);

                //0で割った場合のエラー表記
                if (ops.get(idx) == '/' && right == 0) {
                    throw new ArithmeticException("0 division");
                }

                //三項演算子
                // 条件式、？、：　の3つの構成で成り立つ
                //条件式を通して、true であれば、左をの結果を返して、falseであれば右の結果を返す。
                double result = ops.get(idx) == '*' ? left * right : left / right;

                //古い数を計算結果で書き換える
                nums.set(idx, result);
                //計算結果の右の数が残っているので消す
                nums.remove(idx + 1);
                //使い終わった演算子も削除
                ops.remove(idx);
                //位置調整
                idx--;
            }
        }

        // ＋ −
        //プリミティブ型の変数resultは書き換えた結果を返す。
        double result = nums.get(0);
        //
        for (int idx = 0; idx < ops.size(); idx++) {
        
            if (ops.get(idx) == '+') {
                //複合代入演算で変数resultの数に足す。
                result += nums.get(idx + 1);
            } else {
                //複合代入演算で変数resultの数に引く。
                result -= nums.get(idx + 1);
            }
        }
        //結果を返す
        return result;
    }


    
//判定系


    
/* 関数ヘッダー：引数が（入力された文字1文字）が演算記号であれば結果としてその値をtrueで返すメソッド。
このクラス内で使われている。
*/
    private boolean isOperation(char characterOperator) {
        return characterOperator == '+' || characterOperator == '-' 
        || characterOperator == '*' || characterOperator == '/' ||
         characterOperator == '×' || characterOperator == '÷';
    }


    /* 関数ヘッダー：最後の値に小数点を含んでいなければtrueを返すメソッド。

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
/* 関数ヘッダー：小数点を桁数のカウントとして含めないためのメソッド。
このクラス内で使われている。
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
