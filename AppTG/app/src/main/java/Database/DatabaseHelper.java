package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import item.BaoThuc;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final  String DB_NAME = "QuanLyThoiGian.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_ALARM = "Alarm";

    public DatabaseHelper(Context context){
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_ALARM + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "h INTEGER NOT NULL, " +
                "m INTEGER NOT NULL, " +
                "t2 INTEGER DEFAULT 0, " +
                "t3 INTEGER DEFAULT 0, " +
                "t4 INTEGER DEFAULT 0, " +
                "t5 INTEGER DEFAULT 0, " +
                "t6 INTEGER DEFAULT 0, " +
                "t7 INTEGER DEFAULT 0, " +
                "cn INTEGER DEFAULT 0, " +
                "bat INTEGER DEFAULT 0)";

        db.execSQL(createTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        onCreate(db);
    }


    public long insertBaoThuc(BaoThuc baoThuc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("h", baoThuc.getH());
        values.put("m", baoThuc.getM());
        values.put("t2", baoThuc.getRepeatString().contains("T2") ? 1 : 0);
        values.put("t3", baoThuc.getRepeatString().contains("T3") ? 1 : 0);
        values.put("t4", baoThuc.getRepeatString().contains("T4") ? 1 : 0);
        values.put("t5", baoThuc.getRepeatString().contains("T5") ? 1 : 0);
        values.put("t6", baoThuc.getRepeatString().contains("T6") ? 1 : 0);
        values.put("t7", baoThuc.getRepeatString().contains("T7") ? 1 : 0);
        values.put("cn", baoThuc.getRepeatString().contains("CN") ? 1 : 0);
        values.put("bat", baoThuc.getBat());
        long id = db.insert(TABLE_ALARM, null, values);
        db.close();
        return id;
    }
    // Lấy tất cả báo thức
    public List<BaoThuc> getAllBaoThuc() {
        List<BaoThuc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARM, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int h = cursor.getInt(cursor.getColumnIndexOrThrow("h"));
                int m = cursor.getInt(cursor.getColumnIndexOrThrow("m"));
                int t2 = cursor.getInt(cursor.getColumnIndexOrThrow("t2"));
                int t3 = cursor.getInt(cursor.getColumnIndexOrThrow("t3"));
                int t4 = cursor.getInt(cursor.getColumnIndexOrThrow("t4"));
                int t5 = cursor.getInt(cursor.getColumnIndexOrThrow("t5"));
                int t6 = cursor.getInt(cursor.getColumnIndexOrThrow("t6"));
                int t7 = cursor.getInt(cursor.getColumnIndexOrThrow("t7"));
                int cn = cursor.getInt(cursor.getColumnIndexOrThrow("cn"));
                int bat = cursor.getInt(cursor.getColumnIndexOrThrow("bat"));

                list.add(new BaoThuc(id, h, m, t2, t3, t4, t5, t6, t7, cn, bat));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
    public void updateBaoThuc(BaoThuc b){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("h", b.getH());
        values.put("m", b.getM());
        values.put("t2", b.getT2());
        values.put("t3", b.getT3());
        values.put("t4", b.getT4());
        values.put("t5", b.getT5());
        values.put("t6", b.getT6());
        values.put("t7", b.getT7());
        values.put("cn", b.getCn());
        values.put("bat", b.getBat());
        db.update(TABLE_ALARM, values, "id=?", new String[]{String.valueOf(b.getId())});
        db.close();
    }

    public void deleteBaoThuc(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARM, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }
    // Lấy 1 báo thức theo id
    public BaoThuc getBaoThucById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARM + " WHERE id = ?", new String[]{String.valueOf(id)});

        BaoThuc baoThuc = null;
        if (cursor.moveToFirst()) {
            int h = cursor.getInt(cursor.getColumnIndexOrThrow("h"));
            int m = cursor.getInt(cursor.getColumnIndexOrThrow("m"));
            int t2 = cursor.getInt(cursor.getColumnIndexOrThrow("t2"));
            int t3 = cursor.getInt(cursor.getColumnIndexOrThrow("t3"));
            int t4 = cursor.getInt(cursor.getColumnIndexOrThrow("t4"));
            int t5 = cursor.getInt(cursor.getColumnIndexOrThrow("t5"));
            int t6 = cursor.getInt(cursor.getColumnIndexOrThrow("t6"));
            int t7 = cursor.getInt(cursor.getColumnIndexOrThrow("t7"));
            int cn = cursor.getInt(cursor.getColumnIndexOrThrow("cn"));
            int bat = cursor.getInt(cursor.getColumnIndexOrThrow("bat"));

            baoThuc = new BaoThuc(id, h, m, t2, t3, t4, t5, t6, t7, cn, bat);
        }

        cursor.close();
        db.close();
        return baoThuc;
    }


}
