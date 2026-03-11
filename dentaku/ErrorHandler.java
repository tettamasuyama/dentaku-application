/**
 * 電卓アプリケーション用のエラーハンドリングクラス。
 *
 * <p>
 * アプリケーション内で発生した例外を処理し、
 * エラーメッセージをコンソールに出力する役割を持つ。
 * </p>
 *
 * <p>
 * 開発モード（DEBUG = true）の場合は、
 * スタックトレースも表示してデバッグを容易にする。
 * </p>
 *
 * <p>
 * このクラスはユーティリティクラスのため
 * インスタンス化できないようにコンストラクタを private にしている。
 * </p>
 *
 * 
 */
public class ErrorHandler {

    /** デバッグモードの切り替えフラグ */
    private static final boolean DEBUG = false;

    /**
     * インスタンス生成を防ぐための private コンストラクタ。
     */
    private ErrorHandler() {
    }

    /**
     * 例外を処理し、エラー内容をコンソールに出力するメソッド。
     *
     * <p>
     * 例外のクラス名とメッセージを表示し、
     * ユーザーに再操作を促すメッセージを出力する。
     * </p>
     *
     * <p>
     * DEBUG モードが有効な場合は
     * スタックトレースも出力する。
     * </p>
     *
     * @param e 発生した例外
     */
    public static void handle(Exception e) {

        System.err.println("================ Calculator Error =================");
        System.err.println(e.getClass().getName());
        System.err.println("[Calculator Error] " + e.getMessage());
        System.err.println("Clearボタンを押して、別の計算処理を行ってください。");
        System.err.println("===================================================");
        //
        if (DEBUG) {
            // スタックトレースを出力（開発時のみ）
            e.printStackTrace();

        }

    }
}