package jp.ac.u_tokyo.t.utdroid_multithread;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Handler handler;
    private Thread safeThread;
    private Thread unsafeThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* クリックした時の動作を指定する。findViewByIdの結果をそのまま渡しても良い */
        findViewById(R.id.threadOnly).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unsafeThread.start();
            }
        });

        /* クリックした時の動作を指定する。findViewByIdの結果をそのまま渡しても良い */
        findViewById(R.id.threadHandler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safeThread.start();
            }
        });

        /* Handlerの初期化 */
        handler = new Handler();

        /* 安全なスレッドの準備 */
        safeThread = new Thread() {
            public void run() {
                for (int i = 0; i < 5; i++) {
                    /* 表示するテキストを生成 */
                    final String text = (i * 3) + "秒経過しました。";

                    /* Handler経由でメインスレッド（UIスレッド）にタスクを投げる */
                    handler.post(new Runnable(){
                        /**
                         * このメソッドがメインスレッド（UIスレッド）で実行される
                         */
                        @Override
                        public void run() {
                            /* UIを操作する命令 */
                            Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                        }
                    });

                    try {
                        /* 3秒間だけ待つ */
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        /* エラーが起きたらログに出力 */
                        e.printStackTrace();
                    }
                }
            }
        };

        /* 安全でないスレッドの準備 */
        unsafeThread = new Thread() {
            public void run() {
                for (int i = 0; i < 5; i++) {
                    /* 表示するテキストを生成 */
                    final String text = (i * 3) + "秒経過しました。";

                    /* UIを操作する命令。メインスレッド以外で行ってはならない（強制終了する） */
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();

                    try {
                        /* 3秒間だけ待つ */
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        /* エラーが起きたらログに出力 */
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
