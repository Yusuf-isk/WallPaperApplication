package com.example.wallpaperapplication.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wallpaperapplication.R;
import com.example.wallpaperapplication.adapters.PhotosAdapter;
import com.example.wallpaperapplication.models.Photo;
import com.example.wallpaperapplication.webservices.ApiInterface;
import com.example.wallpaperapplication.webservices.ServiceGenerator;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotosFragment extends Fragment {
    private final String TAG = PhotosFragment.class.getSimpleName();
    @BindView(R.id.fragment_photos_processbar)
    ProgressBar progressBar;
    @BindView(R.id.fragment_photos_recyclerview)
    RecyclerView recyclerView;

    private PhotosAdapter photosAdapter;
    private List<Photo> photos = new ArrayList<>();
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos,container,false);

        unbinder = ButterKnife.bind(this,view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        photosAdapter = new PhotosAdapter(getActivity(),photos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(photosAdapter);
        photosAdapter.notifyDataSetChanged();
        showProgressBar(true);
        getPhotos();
        return view;
    }
    private void getPhotos() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<List<Photo>> call = apiInterface.getPhotos();
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    photos.addAll(response.body());
                    photosAdapter.notifyDataSetChanged();

                } else {
                    Log.e(TAG,response.message());
                }
                showProgressBar(false);
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Log.e(TAG,t.getMessage());
                showProgressBar(false);
            }
        });
    }
    private void showProgressBar(boolean isShow) {
        if (isShow) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
