package com.example.wallpaperapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.wallpaperapplication.R;
import com.example.wallpaperapplication.models.Photo;
import com.example.wallpaperapplication.utils.Functions;
import com.example.wallpaperapplication.utils.GlideApp;
import com.example.wallpaperapplication.utils.RealmController;
import com.example.wallpaperapplication.webservices.ApiInterface;
import com.example.wallpaperapplication.webservices.ServiceGenerator;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullScreenPhotoActivity extends AppCompatActivity {
    @BindView(R.id.activity_fullscreen_photo)
    ImageView fullscreenphoto;
    @BindView(R.id.activity_fullscreen_user_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.activity_fullscreen_fab_menu)
    FloatingActionMenu fabMenu;
    @BindView(R.id.activity_fullscreen_fab_button_favorite)
    FloatingActionButton fabFavorite;
    @BindView(R.id.activity_fullscreen_fab_button_wallpaper)
    FloatingActionButton fabWallpaper;
    @BindView(R.id.activity_fullscreen_username)
    TextView username;
    @BindDrawable(R.drawable.ic_favorite_black_24dp)
    Drawable icFavorited;
    @BindDrawable(R.drawable.ic_favorite)
    Drawable icFavorite;

    private RealmController realmController;
    private Bitmap photoBitmap;
    private Photo photo = new Photo();
    private Unbinder unbinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_photo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        unbinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        String photoId = intent.getStringExtra("photoId");
        getPhoto(photoId);
        realmController = new RealmController();
        if (realmController.isPhotoExist(photoId)) {
            fabFavorite.setImageDrawable(icFavorited);
        }
    }
    private void getPhoto(String id) {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<Photo> call =apiInterface.getPhoto(id);
        call.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful()) {
                    photo = response.body();
                    updateUI(photo);
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {

            }
        });
    }

    private void updateUI(Photo photo) {
        try {
            username.setText(photo.getUser().getUsername());
            GlideApp
                    .with(FullScreenPhotoActivity.this)
                    .load(photo.getUser().getProfileImage().getSmall())
                    .into(userAvatar);
            GlideApp
                    .with(FullScreenPhotoActivity.this)
                    .asBitmap()
                    .load(photo.getUrl().getRegular())
                    .centerCrop()
                    .into(new CustomTarget<Bitmap>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            fullscreenphoto.setImageBitmap(resource);
                            photoBitmap =resource;
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        } catch (Exception e) {
        e.printStackTrace();}
    }

    @OnClick(R.id.activity_fullscreen_fab_button_favorite)
    public void setFabFavorite() {
        if (realmController.isPhotoExist(photo.getId())) {
            realmController.deletePhoto(photo);
            fabFavorite.setImageDrawable(icFavorite);
            Toast.makeText(FullScreenPhotoActivity.this,"Favorilerden kaldırıldı",Toast.LENGTH_LONG).show();
        } else {
            realmController.savePhoto(photo);
            fabFavorite.setImageDrawable(icFavorited);
            Toast.makeText(FullScreenPhotoActivity.this,"Favorilere eklendi",Toast.LENGTH_LONG).show();
        }

        fabMenu.close(true);
    }
    @OnClick(R.id.activity_fullscreen_fab_button_wallpaper)
    public void setFabWallpaper() {
        if (photoBitmap!=null) {
            if (Functions.setWallpaper(FullScreenPhotoActivity.this,photoBitmap)) {
                Toast.makeText(FullScreenPhotoActivity.this,"Duvarkağıdı başarıyla yüklendi",Toast.LENGTH_LONG).show();
            }
        }
        fabMenu.close(true);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
