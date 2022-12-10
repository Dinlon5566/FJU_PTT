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
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 找到視圖的元件並連接
        button = findViewById(R.id.button);
        textView = findViewById(R.id.text_view);
        editText = findViewById(R.id.idSearch);
        handler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String[] split = result.split(",");
                ListView listview = (ListView) findViewById(R.id.listview);
                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,split);
                listview.setAdapter(adapter);
            }
        };
        //ListView 要顯示的內容

        //android.R.layout.simple_list_item_1 為內建樣式，還有其他樣式可自行研究

        // 宣告按鈕的監聽器監聽按鈕是否被按下
        // 跟上次在 View 設定的方式並不一樣
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

                String data = "IP="+idSearch;
                URL url = new URL("http://140.136.151.135/functionPage/json_IP.php");
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
                    json.put("ID",idSearch);

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(data.getBytes());
                    outputStream.flush();
                    outputStream.close();
                    //輸入
                    InputStream inputStream = connection.getInputStream();
                    // 取得輸入串流
                    BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                    // 讀取輸入串流的資料
                    String box=""; // 宣告存放用字串
                    String line = null; // 宣告讀取用的字串
                        while((line = bufReader.readLine()) != null) {
                            box += line + "\n";
                            // 每當讀取出一列，就加到存放字串後面
                        }
                        inputStream.close(); // 關閉輸入串流
                        // 把存放用字串放到全域變數

                        //開始解析json
                        //由於回傳是放在$result[] array字串陣列中，因此要先轉為JSONArray
                        //但是又由於我要用jetString的方始取得json 的key再顯示出來，因此又要轉成JSONObject，因為key是String
                        //之後用一個temp字串暫存結果(不用也可以)，因為我是用","在分割字串成array字串的，至於為什麼要分割成array字串
                        //因為我使用的功能listView是只接受array字串的，他將每個字串分別分行列出來
                        //box+="box的長度為:"+box.length();
                //經上面函數檢查box當街收到為搜尋到的json空值傳回時，長度為11不知道為什麼，不過先用此長度做防呆。
                if(box.length()==11){
                    box="沒有資料";
                    result=box;
                }else {
                    JSONArray j = new JSONArray(box);
                    String temp = "";
                    for (int i = 0; i < j.length(); i++) {
                        JSONObject jj = j.getJSONObject(i);
                        temp += jj.getString("userID") + ",";
                    }
                    result = temp;
                }
                        //result=box;
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
                    textView.setText(result);
                    handler.sendEmptyMessage(0);
                    // 更改顯示文字
                }
            });
        }
    };
}