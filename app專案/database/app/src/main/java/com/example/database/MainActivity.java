package com.example.database;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.*;//記得要import下面這兩行 一個是button 一個是view用的
import android.view.View;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    TextView textView; // 把視圖的元件宣告成全域變數
    Button button;
    String result; // 儲存資料用的字串
    EditText editText;
    static String idSearch ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 找到視圖的元件並連接
        button = findViewById(R.id.button);
        textView = findViewById(R.id.text_view);
        editText = findViewById(R.id.idSearch);

        // 宣告按鈕的監聽器監聽按鈕是否被按下
        // 跟上次在 View 設定的方式並不一樣
        // 我只是覺得好像應該也教一下這種寫法
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            // 按鈕事件
            public void onClick(View view) {
                // 按下之後會執行的程式碼
                // 宣告執行緒

                Thread thread = new Thread(mutiThread);
                thread.start(); // 開始執行
                idSearch = String.valueOf(editText.getText());
            }
        });
    }

    /* ======================================== */

    // 建立一個執行緒執行的事件取得網路資料
    // Android 有規定，連線網際網路的動作都不能再主線程做執行
    // 畢竟如果使用者連上網路結果等太久整個系統流程就卡死了
    private Runnable mutiThread = new Runnable(){
        public void run()
        {
            try {
                URL url = new URL("http://140.136.151.135/GetData.php");
                // 開始宣告 HTTP 連線需要的物件，這邊通常都是一綑的
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // 建立 Google 比較挺的 HttpURLConnection 物件
                connection.setRequestMethod("POST");
                // 設定連線方式為 POST
                connection.setDoOutput(true); // 允許輸出
                connection.setDoInput(true); // 允許讀入
                connection.setUseCaches(false); // 不使用快取
                connection.connect();
                 // 開始連線



                    //輸出

                    JSONObject json = new JSONObject();
                    json.put("idkey",idSearch);
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(json.toString().getBytes());
                    outputStream.flush();
                    outputStream.close();
                    //輸入
                    InputStream inputStream = connection.getInputStream();
                    // 取得輸入串流
                    BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                    // 讀取輸入串流的資料
                    String box = ""; // 宣告存放用字串
                    String line = null; // 宣告讀取用的字串
                        while((line = bufReader.readLine()) != null) {
                            box += line + "\n";
                            // 每當讀取出一列，就加到存放字串後面
                        }
                        inputStream.close(); // 關閉輸入串流
                        // 把存放用字串放到全域變數
                        //開始解析json
//                      JSONArray j = new JSONArray(box);\
                        //

                        JSONObject j = new JSONObject(box);
                        result =j.getString("str");


                //用list的方法轉換JSONArray到String
//                List<String> list = new ArrayList<String>();
//                for (int i = 0; i < j.length(); i++) {
//                    list.add(j.getJSONObject(i).getString("id"));
//                }
            } catch(Exception e) {
                result = e.toString(); // 如果出事，回傳錯誤訊息
            }
            // 當這個執行緒完全跑完後執行
            runOnUiThread(new Runnable() {
                public void run() {
                    textView.setText(result); // 更改顯示文字
                }
            });
        }
    };
}