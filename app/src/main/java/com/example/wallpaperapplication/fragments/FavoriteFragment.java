package com.example.wallpaperapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaperapplication.R;
import com.example.wallpaperapplication.adapters.PhotosAdapter;
import com.example.wallpaperapplication.models.Photo;
import com.example.wallpaperapplication.utils.RealmController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoriteFragment extends Fragment {
    @BindView(R.id.fragment_favorite_recylerview)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_favorite_text)
    TextView textView;

    private PhotosAdapter photosAdapter;
    private List<Photo> photos = new ArrayList<>();
    private Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite,container,false);
        unbinder = ButterKnife.bind(this,view);

        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        photosAdapter = new PhotosAdapter(getActivity(),photos);
        recyclerView.setAdapter(photosAdapter);

        getPhotos();
        return view;

    }

    private void getPhotos() {
        RealmController realmController = new RealmController();
        photos.addAll(realmController.getPhotos());
        if (photos.size() == 0) {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            photosAdapter.notifyDataSetChanged();
           /* textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);*/
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
