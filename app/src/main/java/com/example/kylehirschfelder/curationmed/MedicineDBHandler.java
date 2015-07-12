package com.example.kylehirschfelder.curationmed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MedicineDBHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "formManager",
            TABLE_MEDICINE = "medicineTable",
            KEY_ID = "id",
            KEY_NAME = "name",
            KEY_MG = "mg",
            KEY_EXP = "exp_date",
            KEY_BOTT = "bott_date",
            KEY_TABS = "no_tab",
            KEY_PATIENT = "patient_id";

   private static final String TABLE_MEDICINECUR = "medicineCur",
            KEY_IDCUR = "id",
            KEY_NAMECUR = "name",
            KEY_MGCUR = "mg",
            KEY_EXPCUR = "exp_date",
            KEY_BOTTCUR = "bott_date",
            KEY_TABSCUR = "no_tab",
            KEY_PATIENTCUR = "patient_id";


    public MedicineDBHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

       db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_MEDICINE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " VARCHAR, "
                + KEY_MG + " VARCHAR, "
                + KEY_EXP + " VARCHAR, "
                + KEY_BOTT + " VARCHAR, "
                + KEY_TABS + " VARCHAR, "
                + KEY_PATIENT + " VARCHAR);");

        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_MEDICINECUR + "("
                + KEY_IDCUR + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAMECUR + " VARCHAR, "
                + KEY_MGCUR + " VARCHAR, "
                + KEY_EXPCUR + " VARCHAR, "
                + KEY_BOTTCUR + " VARCHAR, "
                + KEY_TABSCUR + " VARCHAR, "
                + KEY_PATIENTCUR + " VARCHAR);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINECUR);
        onCreate(db);
    }

    public void createMedicine(Medicine medicine){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID, medicine.get_id());
        values.put(KEY_NAME, medicine.get_name());
        values.put(KEY_MG, medicine.get_mg());
        values.put(KEY_EXP, medicine.get_expDate());
        values.put(KEY_BOTT, medicine.get_openDate());
        values.put(KEY_TABS, medicine.get_noTabs());
        values.put(KEY_PATIENT, medicine.get_patientId());

        db.insert(TABLE_MEDICINE, null, values);
        db.close();
    }

   public void createMedicineCur(Medicine medicine){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_IDCUR, medicine.get_id());
        values.put(KEY_NAMECUR, medicine.get_name());
        values.put(KEY_MGCUR, medicine.get_mg());
        values.put(KEY_EXPCUR, medicine.get_expDate());
        values.put(KEY_BOTTCUR, medicine.get_openDate());
        values.put(KEY_TABSCUR, medicine.get_noTabs());
        values.put(KEY_PATIENTCUR, medicine.get_patientId());

        db.insert(TABLE_MEDICINECUR, null, values);
        db.close();
    }

    public int getMedicineCount(){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEDICINE, null);
        int c = cursor.getCount();
        db.close();
        cursor.close();
        return c;
    }

    public List<Medicine> getAllMedicine(){
        List<Medicine> medicineList = new ArrayList<Medicine>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEDICINE, null);

        if(cursor.moveToFirst()){
            do{
                Medicine element = new Medicine(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),
                        cursor.getString(6));
                medicineList.add(element);
            }
            while(cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return medicineList;
    }

    public int getMedicineCurCount(){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEDICINECUR, null);
        int c = cursor.getCount();
        db.close();
        cursor.close();
        return c;
    }

    public List<Medicine> getAllMedicineCur(){
        List<Medicine> medicineList = new ArrayList<Medicine>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MEDICINECUR, null);

        if(cursor.moveToFirst()){
            do{
                Medicine element = new Medicine(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),
                        cursor.getString(6));
                medicineList.add(element);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return medicineList;
    }

    public void deleteRow(long row,String key_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDICINECUR, KEY_ID + "=" + row + " and " + KEY_NAME + "=" + key_name, null);

    }

    public void deleteMed(Medicine medicine){

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MEDICINE, KEY_ID + "=?", new String[] {String.valueOf(medicine.get_id())});
        db.close();
    }

    public void deleteMedCur(Medicine medicine){

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MEDICINECUR, KEY_ID + "=?", new String[] {String.valueOf(medicine.get_id())});
        db.close();
    }

    public void deleteAllTables(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MEDICINE);
        db.execSQL("DELETE FROM " + TABLE_MEDICINECUR);
        db.close();
    }
}
