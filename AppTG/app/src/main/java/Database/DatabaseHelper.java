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
    private static final String DB_NAME = "QuanLyThoiGian.db";
    private static final int DB_VERSION = 2; // Tăng version lên 2

    public static final String TABLE_ALARM = "Alarm";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
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
                "bat INTEGER DEFAULT 0, " +
                "ringtoneUri TEXT" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nếu cũ chưa có cột ringtoneUri thì thêm cột
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_ALARM + " ADD COLUMN ringtoneUri TEXT");
        }
        // Không xóa bảng cũ nữa để tránh mất dữ liệu
    }

    // Insert
    public long insertBaoThuc(BaoThuc baoThuc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("h", baoThuc.getH());
        values.put("m", baoThuc.getM());
        values.put("t2", baoThuc.getT2());
        values.put("t3", baoThuc.getT3());
        values.put("t4", baoThuc.getT4());
        values.put("t5", baoThuc.getT5());
        values.put("t6", baoThuc.getT6());
        values.put("t7", baoThuc.getT7());
        values.put("cn", baoThuc.getCn());
        values.put("bat", baoThuc.getBat());
        values.put("ringtoneUri", baoThuc.getRingtoneUri());
        long id = db.insert(TABLE_ALARM, null, values);
        db.close();
        return id;
    }

    // Get all
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
                String ringtoneUri = cursor.getString(cursor.getColumnIndexOrThrow("ringtoneUri"));

                BaoThuc bt = new BaoThuc(id, h, m, t2, t3, t4, t5, t6, t7, cn, bat);
                bt.setRingtoneUri(ringtoneUri);
                list.add(bt);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // Update
    public void updateBaoThuc(BaoThuc b) {
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
        values.put("ringtoneUri", b.getRingtoneUri());
        db.update(TABLE_ALARM, values, "id=?", new String[]{String.valueOf(b.getId())});
        db.close();
    }

    // Delete
    public void deleteBaoThuc(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARM, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Get by id
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
            String ringtoneUri = cursor.getString(cursor.getColumnIndexOrThrow("ringtoneUri"));

            baoThuc = new BaoThuc(id, h, m, t2, t3, t4, t5, t6, t7, cn, bat);
            baoThuc.setRingtoneUri(ringtoneUri);
        }
        cursor.close();
        db.close();
        return baoThuc;
    }
}
