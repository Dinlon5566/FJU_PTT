package com.example.database_ptt_1;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;



public class article_contentFragment extends Fragment {

    static String account;
    static String searchingResult="搜尋中...\n由於資料庫龐大請稍候...";
    public String result=searchingResult;
    private ListView id_listview;
    private TextView textView5;
    static String[] resultTolistview;
    static int listviewCount;
    boolean WhileSearchError=false;//設定參數當此數值為真 表示正在搜尋不可按返回
    ProgressBar pgSpinner;//轉圈圈美觀搜尋
    private Runnable mutiThread= new Runnable(){
        public void run()
        {       result=searchingResult;
            try {
                WhileSearchError=true;
                pgSpinner.setVisibility(View.VISIBLE);//顯示轉圈圈
                String data = "ID="+account;
                URL url = new URL("http://140.136.151.135/functionPage/json_article.php");
                // 開始宣告 HTTP 連線需要的物件，這邊通常都是一綑的
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(30000);//連線超過30秒會顯示無結果，因為實際上若>30秒php會報錯
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
                json.put("ID",account);
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
                connection.disconnect();
                // 把存放用字串放到全域變數
                //開始解析json
                //由於回傳是放在$result[] array字串陣列中，因此要先轉為JSONArray
                //但是又由於我要用jetString的方始取得json 的key再顯示出來，因此又要轉成JSONObject，因為key是String
                //之後用一個temp字串暫存結果(不用也可以)，因為我是用","在分割字串成array字串的，至於為什麼要分割成array字串
                //因為我使用的功能listView是只接受array字串的，他將每個字串分別分行列出來
                //box+="box的長度為:"+box.length();
                result = "";
                JSONArray j = new JSONArray(box);
                String board = "";
                String idArticles = "";
                String writer = "";
                String title = "";
                String time = "";
                String IP = "";
                String body="";//文章內文
                String error = "";
                listviewCount = j.length();
                resultTolistview= new String[listviewCount];
                for (int i = 0; i < j.length(); i++) {
                    JSONObject jj = j.getJSONObject(i);
                    error = jj.optString("error");
                    if(error!="") {
                        textView5.setVisibility(View.VISIBLE);
                        result = "查無結果，請按返回鍵重新搜尋";
                        resultTolistview=null;
                    }
                    else{
                        board = "看板:"+ jj.getString("board") + "\n";
                        idArticles = "文章代號:"+ jj.getString("idArticles") + "\n";
                        writer = "作者:"+ jj.getString("writer") + "\n";
                        title = "標題:"+ jj.getString("title") + "\n";
                        time = "發文時間:" + jj.getString("time") +"\n" ;
                        IP = "IP:" + jj.getString("IP") + "\n";
                        body = "文章內容:\n\n" + jj.getString("body")+ "\n" ;
                        resultTolistview[i]= board + idArticles + writer + title + time+ IP + body +"\n";
                        result = searchingResult;
                    }
                }
                //用list的方法轉換JSONArray到String
//                List<String> list = new ArrayList<String>();
//                for (int i = 0; i < j.length(); i++) {
//                    list.add(j.getJSONObject(i).getString("id"));
//                }
//                result = body;
            } catch (SocketTimeoutException t) {
                // 如果出事，回傳錯誤訊息
                result = "查無結果，請按返回鍵重新搜尋";
            }catch (SocketException i){
                result = "網路連線意外中斷，請檢查網路設置";
            }catch (Exception e){
                result = "網路連線意外中斷，請檢查網路設置";
            }
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    pgSpinner.setVisibility(View.INVISIBLE);//轉圈圈消失
                    if(resultTolistview!=null) {
                        textView5.setVisibility(View.INVISIBLE);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, resultTolistview);
                        id_listview.setAdapter(adapter);
                        //搜尋到了就隱藏文字
                        // 更改顯示文字
                    }else {
                        textView5.setVisibility(View.VISIBLE);
                        textView5.setText(result);
                    }
                    WhileSearchError=false;
                }
            });
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView5 = view.findViewById(R.id.textView5);
        id_listview = view.findViewById(R.id.id_listview);
        pgSpinner = view.findViewById(R.id.progressBar_Spinner);//轉圈圈美觀功能
        Bundle bundle = getArguments();
        pgSpinner.setVisibility(View.INVISIBLE);//轉圈圈一開始是不出現的
        textView5.setText(result);
        if(bundle!=null){
            account = bundle.getString("send");
            Thread thread = new Thread(mutiThread);
            thread.start(); // 開始執行
        }
    }
    //按下返回按鈕事件
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //按下返回键时想要实现的方法
                if(WhileSearchError==false){
                    NavHostFragment.findNavController(article_contentFragment.this)
                            .popBackStack();
                    result = searchingResult;}
                else{
                    Toast.makeText(getActivity(),"正在搜尋中請勿中斷返回...",Toast.LENGTH_SHORT).show();
                }
            }
        };
        //把回调函数添加到Activity中
        requireActivity().getOnBackPressedDispatcher().addCallback(
                this, // LifecycleOwner
                callback);
    }
    @Override
    public void onDestroy() {
        result=searchingResult;
        id_listview.setVisibility(View.INVISIBLE);
        super.onDestroy();
    }

}