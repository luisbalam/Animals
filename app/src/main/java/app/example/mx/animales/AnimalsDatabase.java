package app.example.mx.animales;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

/**
 * Created by richux on 29/10/16.
 */

public class AnimalsDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "animales.db";
    private static final String TABLE_NAME = "animals";
    public static final String COL_NAME = "nombre";
    public static final String COL_DESCRIPTION = "descripcion";

    public AnimalsDatabase(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createQuery =
                "CREATE TABLE " + TABLE_NAME +
                        " (_id INTEGER PRIMARY KEY, "
                + COL_NAME + " TEXT NOT NULL COLLATE UNICODE, "
                + COL_DESCRIPTION + " TEXT NOT NULL)";

        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String upgradeQuery = "DROP TABLE IF EXIST "+ TABLE_NAME;
        db.execSQL(upgradeQuery);
    }

    public static long insertaAnimal (Context context, String nombre, String descripcion){

        SQLiteOpenHelper dbOpenHelper = new AnimalsDatabase(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();

        ContentValues valorAnimal = new ContentValues();
        valorAnimal.put(COL_NAME, nombre);
        valorAnimal.put(COL_DESCRIPTION, descripcion);

        long result = -1L;
        try {
            result = database.insert(TABLE_NAME, null, valorAnimal);
            if (result != -1L){

                LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
                Intent intentFilter = new Intent(AnimalsLoader.ACTION_RELOAD_TABLE);
                broadcastManager.sendBroadcast(intentFilter);
            }
        } finally {
            dbOpenHelper.close();
        }
        return result;
    }

    public static Cursor devuelveTodos (Context context){
        SQLiteOpenHelper dbOpenHelper = new AnimalsDatabase(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();

        return  database.query(
                TABLE_NAME,
                new String[]{COL_NAME, COL_DESCRIPTION,BaseColumns._ID},
                null, null, null, null,
                COL_NAME+" ASC");
    }

    public static Cursor devuelveConId(Context context, long identificador){
        SQLiteOpenHelper dbOpenHelper = new AnimalsDatabase(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();

        return database.query(
                TABLE_NAME,
                new String[]{COL_NAME, COL_DESCRIPTION, BaseColumns._ID},
                BaseColumns._ID + " = ?",
                new String[]{String.valueOf(identificador)},
                null,
                null,
                COL_NAME+" ASC");
    }

    public static int eliminaConId(Context context, long animalId){

        SQLiteOpenHelper dbOpenHelper = new AnimalsDatabase(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();

        int resultado = database.delete(
                TABLE_NAME,
                BaseColumns._ID + " =?",
                new String[]{String.valueOf(animalId)});

        if (resultado != 0){

            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
            Intent intentFilter = new Intent(AnimalsLoader.ACTION_RELOAD_TABLE);
            broadcastManager.sendBroadcast(intentFilter);
        }

        dbOpenHelper.close();
        return resultado;

    }

    public static int actualizaAnimal (Context context, String nombre, String descripcion, long animalId){

        SQLiteOpenHelper dbOpenHelper = new AnimalsDatabase(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();

        ContentValues valorAnimal = new ContentValues();
        valorAnimal.put(COL_NAME, nombre);
        valorAnimal.put(COL_DESCRIPTION, descripcion);

        int result = database.update(
                TABLE_NAME,
                valorAnimal,
                BaseColumns._ID + " =?",
                new String[]{String.valueOf(animalId)});

        if (result != 0){

            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
            Intent intentFilter = new Intent(AnimalsLoader.ACTION_RELOAD_TABLE);
            broadcastManager.sendBroadcast(intentFilter);
        }

        dbOpenHelper.close();

        return result;
    }


















}
