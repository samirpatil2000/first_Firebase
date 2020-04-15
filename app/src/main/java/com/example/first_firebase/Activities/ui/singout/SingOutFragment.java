package com.example.first_firebase.Activities.ui.singout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.first_firebase.R;

public class SingOutFragment extends Fragment {

    private SingOutViewModel singOutViewModel;

    @SuppressLint("FragmentLiveDataObserve")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        singOutViewModel =
                ViewModelProviders.of(this).get(SingOutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_singout, container, false);
        final TextView textView = root.findViewById(R.id.text_share);
        singOutViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}