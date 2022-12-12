package com.example.database_ptt_1;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class accountFragment extends Fragment {
    static String account;
    private Button bt_article,bt_message,bt_ip;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //----------------------以下為bundle值的接收---------------------
        Bundle bundle = getArguments();
        if(bundle!=null){
            account = bundle.getString("send");
        }
        //-----------------------以下為三個按鈕-----------------------------
        bt_article = view.findViewById(R.id.bt_article);
        bt_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("account",account);
                Navigation.findNavController(view).navigate(R.id.action_accountFragment_to_articleFragment,bundle);
            }
        });
        bt_message = view.findViewById(R.id.bt_message);
        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("account",account);
                Navigation.findNavController(view).navigate(R.id.action_accountFragment_to_near_accountFragment,bundle);
            }
        });
        bt_ip = view.findViewById(R.id.bt_ip);
        bt_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("account",account);
                Navigation.findNavController(view).navigate(R.id.action_accountFragment_to_ip_result_Fragment,bundle);
            }
        });
    }
}