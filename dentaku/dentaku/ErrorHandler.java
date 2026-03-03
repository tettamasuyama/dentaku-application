public class ErrorHandler {
	
	//開発用
	private static final boolean DEBUG = false;

    private ErrorHandler() {}

    /*関数ヘッダー
    
    詳細：エラーの内容をコンソール表記で実行するメソッド
    
    */

    public static void handle(Exception e) {
    	

        
        
        System.err.println("================ Calculator Error =================");
        System.err.println(e.getClass().getName());
        System.err.println("[Calculator Error] " + e.getMessage());
        System.err.println("Clearボタンを押して、別の計算処理を行ってください。");
        System.err.println("===================================================");
        //
        if (DEBUG) {
            e.printStackTrace();
            //スタックトレース
        }

    }
}