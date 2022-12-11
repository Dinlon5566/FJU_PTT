package com.example.database_ptt_1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ip_result_Fragment extends Fragment {

    static String account;
    static String result;
    private TextView textView4;
    private Runnable mutiThread= new Runnable(){
        public void run()
        {
            try {
                if(account.length()>50) {
                    result = "輸入過長，格式錯誤";
                }else {
                    String data = "ID=" + account;
                    URL url = new URL("http://140.136.151.135/functionPage/json_IDIP.php");
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
                    json.put("ID", account);

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(data.getBytes());
                    outputStream.flush();
                    outputStream.close();
                    //輸入
                    InputStream inputStream = connection.getInputStream();
                    // 取得輸入串流
                    BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                    // 讀取輸入串流的資料
                    String box = ""; // 宣告存放用字串
                    String line = null; // 宣告讀取用的字串
                    while ((line = bufReader.readLine()) != null) {
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
                    if(box.length()==24) {
                        result = "查無結果，請按返回鍵重新搜尋";
                    }else {
                        JSONArray j = new JSONArray(box);
                        String IP = "";
                        String userID = "";
                        String times = "";
                        for (int i = 0; i < j.length(); i++) {
                            JSONObject jj = j.getJSONObject(i);
                            IP = "IP:" + jj.getString("IP") + "\n";
                            userID = "使用者ID:" + jj.getString("userID") + "\n";
                            times = "發文次數:" + jj.getString("times") + "\n";
                            result +="\n"+IP + userID + times + "box的長度為:" + box.length();
                        }

                    }
                }
                //用list的方法轉換JSONArray到String
//                List<String> list = new ArrayList<String>();
//                for (int i = 0; i < j.length(); i++) {
//                    list.add(j.getJSONObject(i).getString("id"));
//                }
            } catch(Exception e) {
                result = e.toString(); // 如果出事，回傳錯誤訊息
            }
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    textView4.setText(result);
                    // 更改顯示文字
                }
            });
        }

    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ip_result_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView4 = view.findViewById(R.id.textView4);
        Bundle bundle = getArguments();
        if(bundle!=null){
            account = bundle.getString("account");
            Thread thread = new Thread(mutiThread);
            thread.start(); // 開始執行
        }
    }
}