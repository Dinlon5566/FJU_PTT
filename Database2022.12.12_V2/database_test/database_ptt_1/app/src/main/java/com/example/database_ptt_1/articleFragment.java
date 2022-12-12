package com.example.database_ptt_1;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class articleFragment extends Fragment implements AdapterView.OnItemClickListener{

    static String account;
    static String searchingResult="搜尋中...\n由於資料庫龐大請稍後...";
    public String result=searchingResult;
    TextView textView2;
    TextView textmessenge;
    private ListView id_listview;
    static String[] resultTolistview=null;
    static String[] IPss;
    static int listviewCount;
    boolean WhileSearchError=false;
    private Runnable mutiThread= new Runnable(){
        public void run()
        {
            try {if(account.length()>50) {
                result = "輸入過長，格式錯誤";
                resultTolistview=null;
                }else{
                WhileSearchError=true;
                String data = "ID="+account;
                URL url = new URL("http://140.136.151.135/functionPage/json_articleLog.php");
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
                //經上面函數檢查box當街收到為搜尋到的json空值傳回時，長度為11不知道為什麼，不過先用此長度做防呆。
                result = "";
                JSONArray j = new JSONArray(box);
                String board = "";
                String idArticles = "";
                String title = "";
                String time = "";
                String error = "";
                String IP = "";
                String IPs ="";
                listviewCount = j.length();
                resultTolistview= new String[listviewCount];
                IPss= new String[listviewCount];
                for (int i = 0; i < j.length(); i++) {
                    JSONObject jj = j.getJSONObject(i);
                    error = jj.optString("error");
                    if(error!="") {
                        textView2.setVisibility(View.VISIBLE);
                        result = "查無結果，請按返回鍵重新搜尋";
                        resultTolistview=null;
                    }
                    else{
                        board = "看板:" + jj.getString("board") + "\n";
                        idArticles = "代號:" + jj.getString("idArticles") + "\n";
                        title = "標題:" + jj.getString("title") + "\n";
                        time = "時間:" + jj.getString("time") + "\n";
                        IP = "IP:" + jj.getString("IP") + "\n";
                        IPs = jj.getString("idArticles");
                        resultTolistview[i]=board + idArticles + title + time + IP;
                        IPss[i]=IPs;
                        //result += board + idArticles + title + time + IP + "\n";
                    }
                }
                //用list的方法轉換JSONArray到String
//                List<String> list = new ArrayList<String>();
//                for (int i = 0; i < j.length(); i++) {
//                    list.add(j.getJSONObject(i).getString("id"));}
                }
            }
            catch(Exception e) {
                result = "網路連線意外中斷，請檢查網路設置"; // 如果出事，回傳錯誤訊息
            }
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if(resultTolistview!=null) {
                        textView2.setVisibility(View.INVISIBLE);
                        textmessenge.setVisibility(View.VISIBLE);
                        textmessenge.setText("共搜尋到了"+String.valueOf(listviewCount)+"筆資料:");
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, resultTolistview);
                        id_listview.setAdapter(adapter);
                        //搜尋到了就隱藏文字
                        // 更改顯示文字
                    }else {
                        textmessenge.setVisibility(View.INVISIBLE);
                        textView2.setVisibility(View.VISIBLE);
                        textView2.setText(result);
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
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView2 = view.findViewById(R.id.textView2);
        id_listview = view.findViewById(R.id.id_listview);
        textmessenge = view.findViewById(R.id.textmessenge);
        Bundle bundle = getArguments();
        textmessenge.setVisibility(View.INVISIBLE);
        textView2.setText(result);//作用於搜尋時顯示文字
        if(bundle!=null){
            account = bundle.getString("account");
            Thread thread = new Thread(mutiThread);
            thread.start(); // 開始執行
            id_listview.setOnItemClickListener(this);
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //按下返回键时想要实现的方法
                if(WhileSearchError==false){
                    NavHostFragment.findNavController(articleFragment.this)
                            .popBackStack();}
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
        super.onDestroy();
        result=searchingResult;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String send = IPss[i];
        Bundle bundle = new Bundle();
        bundle.putString("send",send);
        Navigation.findNavController(view).navigate(R.id.action_articleFragment_to_article_contentFragment,bundle);
    }
}