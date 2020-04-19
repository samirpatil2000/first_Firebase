package com.example.first_firebase.Activities.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.first_firebase.Adapters.PostAdapter;
import com.example.first_firebase.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;

    private HomeViewModel homeViewModel;

    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();

    @SuppressLint("FragmentLiveDataObserve")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

         recyclerView=root.findViewById(R.id.recyclerView);

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        initImageBimaps();
        return root;

    }

    private void initImageBimaps() {
        imageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        name.add("Havasu Falls");

        imageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        name.add("Trondheim");

        imageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        name.add("Portugal");

        imageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        name.add("Rocky Mountain National Park");


        imageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        name.add("Mahahual");

        imageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        name.add("Frozen Lake");


        imageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        name.add("White Sands Desert");

        imageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        name.add("Austrailia");

        imageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        name.add("Washington");

        initRecyclerView();


    }

    private void initRecyclerView(){

        PostAdapter adapter = new PostAdapter(name, imageUrls,getActivity());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}