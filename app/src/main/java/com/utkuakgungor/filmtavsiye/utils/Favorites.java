package com.utkuakgungor.filmtavsiye.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.utkuakgungor.filmtavsiye.models.Model;
import com.utkuakgungor.filmtavsiye.models.Movie;
import com.utkuakgungor.filmtavsiye.models.MovieFirebase;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Favoriler";
    private static final String TABLE_NAME = "filmler";
    private static String FILM_ID = "film_id";
    private static String FILM_TUR = "film_tur";
    private static String FILM_TUR_ENG = "film_tur_eng";
    private static String FILM_SINIF="film_sinif";

    public Favorites(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void deleteData (String ad){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "film_id = ?", new String[]{ad});
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DB_ID = "db_id";
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + DB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FILM_ID + " TEXT,"
                + FILM_TUR + " TEXT,"
                + FILM_TUR_ENG + " TEXT,"
                + FILM_SINIF + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    public void ekle(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FILM_ID, movie.getFilm_id());
        values.put(FILM_TUR,movie.getFilm_tur());
        values.put(FILM_TUR_ENG,movie.getFilm_tur_eng());
        values.put(FILM_SINIF,movie.getFilm_sinif());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Model> getDataFromDB(){
        List<Model> modelList = new ArrayList<>();
        String query = "select * from "+TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                Model model = new Model();
                model.setFilm_id(cursor.getString(1));
                model.setFilm_tur(cursor.getString(12));
                model.setFilm_tur_eng(cursor.getString(13));
                model.setFilm_sinif(cursor.getString(15));
                modelList.add(model);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return modelList;
    }

    public boolean Kontrol(String filmid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        String sql ="SELECT * FROM filmler WHERE film_id='"+filmid+"'";
        cursor= db.rawQuery(sql,null);

        if(cursor.getCount()>0){
            cursor.close();
            return false;
        }else{
            cursor.close();
            return true;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

    }

    public void ekleModel(Model model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FILM_ID, model.getFilm_id());;
        values.put(FILM_TUR,model.getFilm_tur());
        values.put(FILM_TUR_ENG,model.getFilm_tur_eng());
        values.put(FILM_SINIF,model.getFilm_sinif());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void ekleFirebase(MovieFirebase movieFirebase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FILM_ID, movieFirebase.getFilm_id());;
        values.put(FILM_TUR,movieFirebase.getFilm_tur());
        values.put(FILM_TUR_ENG,movieFirebase.getFilm_tur_eng());
        values.put(FILM_SINIF,movieFirebase.getFilm_sinif());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
}
