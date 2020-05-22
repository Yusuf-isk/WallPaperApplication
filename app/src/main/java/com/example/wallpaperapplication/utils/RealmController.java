package com.example.wallpaperapplication.utils;

import com.example.wallpaperapplication.models.Photo;

import java.util.List;

import io.realm.Realm;

public class RealmController {
    private final Realm realm;
    public RealmController() {
        realm=Realm.getDefaultInstance();
    }
    public void savePhoto(Photo photo) {
        realm.beginTransaction();
        realm.copyToRealm(photo);
        realm.commitTransaction();
    }
    public void deletePhoto(Photo photo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Photo resultphoto = realm.where(Photo.class).equalTo("id",photo.getId()).findFirst();
                resultphoto.deleteFromRealm();
            }
        });
    }
    public boolean isPhotoExist(String photoId) {
        Photo resultphoto = realm.where(Photo.class).equalTo("id",photoId).findFirst();
        if (resultphoto ==null) {
            return false;
        }return true;
    }
    public List<Photo> getPhotos() {
        return realm.where(Photo.class).findAll();
    }
}
