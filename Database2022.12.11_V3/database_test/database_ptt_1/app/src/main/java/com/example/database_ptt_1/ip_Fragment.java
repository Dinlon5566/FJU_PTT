package com.example.database_ptt_1;

import android.content.Context;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
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

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ip_Fragment extends Fragment implements AdapterView.OnItemClickListener{

    static String ip;
    static String searchingResult="搜尋中...\n由於資料庫龐大請稍後...";
    public String result=searchingResult;
    TextView textView;
    private ListView ip_listview;
    static String[] resultTolistview;
    static String[] IPss;
    static int listviewCount;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ip_, container, false);

    }

    private Runnable mutiThread = new Runnable() {
        public void run() {
            try {
                    if(ip.length()<=3){
                        //輸入IP太短
                        result = "IP過短，格式錯誤";
                    }else if(ip.length()>15) {
                        result = "IP過長，格式錯誤";
                    }else{
                    String data = "IP=" + ip;
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
                         connection.disconnect();
                    // 把存放用字串放到全域變數
                    //開始解析json
                    //由於回傳是放在$result[] array字串陣列中，因此要先轉為JSONArray
                    //但是又由於我要用jetString的方始取得json 的key再顯示出來，因此又要轉成JSONObject，因為key是String
                    //之後用一個temp字串暫存結果(不用也可以)，因為我是用","在分割字串成array字串的，至於為什麼要分割成array字串
                    //因為我使用的功能listView是只接受array字串的，他將每個字串分別分行列出來
//                     box+=box.length();
//                     result = box;
                    //經上面函數檢查box當街收到為搜尋到的json空值傳回時，長度為11不知道為什麼，不過先用此長度做防呆

                        result ="";
                        JSONArray j = new JSONArray(box);
                        String IP = "";
                        String IPs ="";
                        String userID = "";
                        String times = "";
                        String error = "";
                        listviewCount = j.length();
                        resultTolistview= new String[listviewCount];
                        IPss = new String[listviewCount];
                        for (int i = 0; i < j.length(); i++) {
                            JSONObject jj = j.getJSONObject(i);
                            error = jj.optString("error");
                            if(error!="") {
                                textView.setVisibility(View.VISIBLE);
                                result = "查無結果，請按返回鍵重新搜尋";
                                resultTolistview=null;
                            }
                            else{
                                IP = "IP:" + jj.getString("IP")+ "\n"  ;
                                userID = "使用者ID:" + jj.getString("userID")+ "\n";
                                IPs = jj.getString("userID");
                                times = "發文次數:" + jj.getString("times");
                                resultTolistview[i]=userID;
                                IPss[i]=IPs;
                                result += IP + userID + times + "\n";
                            }
                        }
                    }
                        //result= box;


                //用list的方法轉換JSONArray到String
//                List<String> list = new ArrayList<String>();
//                for (int i = 0; i < j.length(); i++) {
//                    list.add(j.getJSONObject(i).getString("id"));
//                }
            } catch (Exception e) {
                result = e.toString(); // 如果出事，回傳錯誤訊息
            }
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if(resultTolistview!=null) {
                        textView.setText(result);
                        textView.setVisibility(View.INVISIBLE);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, resultTolistview);
                        ip_listview.setAdapter(adapter);
                        //搜尋到了就隱藏文字
                        // 更改顯示文字
                    }else {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(result);
                    }
                }
            });
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
            ip_listview = view.findViewById(R.id.ip_listview);
            textView = view.findViewById(R.id.textView);
            Bundle bundle = getArguments();
            textView.setText(result);
            if (bundle != null) {
                ip = bundle.getString("send");
                Thread thread = new Thread(mutiThread);
                thread.start(); // 開始執行
                ip_listview.setOnItemClickListener(this);
            }
        }catch (Exception e1){
        }
    }

@Override
public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {

            //按下返回键时想要实现的方法
            NavHostFragment.findNavController(ip_Fragment.this)
                    .popBackStack();
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
        ip_listview.setVisibility(View.INVISIBLE);
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String send = IPss[i];
        Bundle bundle = new Bundle();
        bundle.putString("send",send);
        Navigation.findNavController(view).navigate(R.id.action_ip_Fragment_to_accountFragment,bundle);

    }
}