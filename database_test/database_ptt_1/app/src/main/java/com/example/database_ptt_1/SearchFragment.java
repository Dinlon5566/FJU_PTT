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
import android.widget.EditText;


public class SearchFragment extends Fragment {

    private Activity activity;
    private EditText et_search;
    private Button bt_searchip,bt_searchaccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et_search =view.findViewById(R.id.et_search);
        bt_searchip = view.findViewById(R.id.bt_searchip);
        bt_searchip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String send = et_search.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("send",send);
                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_ip_Fragment,bundle);
            }
        });
        bt_searchaccount = view.findViewById(R.id.bt_searchaccount);
        bt_searchaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String send = bt_searchaccount.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("send",send);
                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_accountFragment,bundle);
            }
        });
    }
}

