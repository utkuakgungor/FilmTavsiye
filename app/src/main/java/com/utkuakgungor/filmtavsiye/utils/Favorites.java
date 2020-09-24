package com.utkuakgungor.filmtavsiye.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "Favoriler";
    private static final String TABLE_NAME = "filmler";
    private static String FILM_ADI = "film_adi";
    private static String FILM_YILI = "film_yili";
    private static String FILM_PUAN = "film_puan";
    private static String FILM_IMAGE = "film_image";
    private static String FILM_YOUTUBE = "film_youtube";
    private static String FILM_OZET = "film_ozet";
    private static String FILM_SURE = "film_sure";
    private static String FILM_OZET_ENG = "film_ozet_eng";
    private static String FILM_SURE_ENG = "film_sure_eng";
    private static String TEXT_COLOR= "text_color";
    private static String FILM_OYUNCULAR = "film_oyuncular";
    private static String FILM_TUR = "film_tur";
    private static String FILM_TUR_ENG = "film_tur_eng";
    private static String FILM_YONETMEN = "film_yonetmen";
    private static String FILM_SINIF="film_sinif";
    private static String FILM_RESIMLER="film_resimler";

    public Favorites(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void deleteData (String ad){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "film_adi = ?", new String[]{ad});
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String FILM_ID = "id";
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + FILM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FILM_ADI + " TEXT,"
                + FILM_YILI + " TEXT,"
                + FILM_PUAN + " TEXT,"
                + FILM_IMAGE + " TEXT,"
                + FILM_YOUTUBE + " TEXT,"
                + FILM_OZET + " TEXT,"
                + FILM_SURE + " TEXT,"
                + FILM_OZET_ENG + " TEXT,"
                + FILM_SURE_ENG + " TEXT,"
                + TEXT_COLOR + " TEXT,"
                + FILM_OYUNCULAR + " TEXT,"
                + FILM_TUR + " TEXT,"
                + FILM_TUR_ENG + " TEXT,"
                + FILM_YONETMEN + " TEXT,"
                + FILM_SINIF + " TEXT,"
                + FILM_RESIMLER + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    public void ekle(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FILM_ADI, movie.getFilm_adi());
        values.put(FILM_YILI, movie.getFilm_yili());
        values.put(FILM_PUAN, movie.getFilm_puani());
        values.put(FILM_IMAGE, movie.getImage());
        values.put(FILM_YOUTUBE,movie.getYoutube());
        values.put(FILM_OZET,movie.getOzet());
        values.put(FILM_SURE,movie.getSure());
        values.put(FILM_OZET_ENG,movie.getFilm_ozet_eng());
        values.put(FILM_SURE_ENG,movie.getFilm_sure_eng());
        values.put(TEXT_COLOR,movie.getText_color());
        values.put(FILM_OYUNCULAR,movie.getFilm_oyuncular());
        values.put(FILM_TUR,movie.getFilm_tur());
        values.put(FILM_TUR_ENG,movie.getFilm_tur_eng());
        values.put(FILM_YONETMEN,movie.getFilm_yonetmen());
        values.put(FILM_SINIF,movie.getFilm_sinif());
        values.put(FILM_RESIMLER,movie.getFilm_resimler());

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
                model.setFilm_adi(cursor.getString(1));
                model.setFilm_yili(cursor.getString(2));
                model.setFilm_puan(cursor.getString(3));
                model.setFilm_image(cursor.getString(4));
                model.setFilm_youtube(cursor.getString(5));
                model.setFilm_ozet(cursor.getString(6));
                model.setFilm_sure(cursor.getString(7));
                model.setFilm_ozet_eng(cursor.getString(8));
                model.setFilm_sure_eng(cursor.getString(9));
                model.setText_color(cursor.getString(10));
                model.setFilm_oyuncular(cursor.getString(11));
                model.setFilm_tur(cursor.getString(12));
                model.setFilm_tur_eng(cursor.getString(13));
                model.setFilm_yonetmen(cursor.getString(14));
                model.setFilm_sinif(cursor.getString(15));
                model.setFilm_resimler(cursor.getString(16));
                modelList.add(model);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return modelList;
    }

    public boolean Kontrol(String filmadi){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        String sql ="SELECT * FROM filmler WHERE film_adi='"+filmadi+"'";
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
        if(arg1<2){
            db.execSQL("ALTER TABLE "+TABLE_NAME+" ADD COLUMN "+FILM_SINIF+" TEXT;");
        }
        if(arg1<3){
            db.execSQL("ALTER TABLE "+TABLE_NAME+" ADD COLUMN "+FILM_RESIMLER+" TEXT;");
        }
    }

    public void ekleModel(Model model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FILM_ADI, model.getFilm_adi());
        values.put(FILM_YILI, model.getFilm_yili());
        values.put(FILM_PUAN, model.getFilm_puani());
        values.put(FILM_IMAGE, model.getImage());
        values.put(FILM_YOUTUBE,model.getYoutube());
        values.put(FILM_OZET,model.getOzet());
        values.put(FILM_SURE,model.getSure());
        values.put(FILM_OZET_ENG,model.getFilm_ozet_eng());
        values.put(FILM_SURE_ENG,model.getFilm_sure_eng());
        values.put(TEXT_COLOR,model.getText_color());
        values.put(FILM_OYUNCULAR,model.getFilm_oyuncular());
        values.put(FILM_TUR,model.getFilm_tur());
        values.put(FILM_TUR_ENG,model.getFilm_tur_eng());
        values.put(FILM_YONETMEN,model.getFilm_yonetmen());
        values.put(FILM_SINIF,model.getFilm_sinif());
        values.put(FILM_RESIMLER,model.getFilm_resimler());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
}
