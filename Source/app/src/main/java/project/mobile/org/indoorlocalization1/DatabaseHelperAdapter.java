package project.mobile.org.indoorlocalization1;

/**
 * Created by shardendu on 2/27/15.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;


public class DatabaseHelperAdapter {

    DatabaseHelper helper;
    private static String mDebug = DatabaseHelperAdapter.class.getName();
    private static DatabaseHelperAdapter mInstance = null;

    private DatabaseHelperAdapter(Context context) {

        helper = new DatabaseHelper(context);
        helper.getWritableDatabase();
    }

    public static DatabaseHelperAdapter getInstance(Context context){
        if(mInstance == null) {
            mInstance = new DatabaseHelperAdapter(context);
            Log.d(mDebug,"First time instance creation");
        }
        else
            Log.d(mDebug,"Using the same instance");
        return mInstance;
    }

    public String getLocation(String mFloorNum, String mSignText) {
        SQLiteDatabase db2 = helper.getWritableDatabase();
        String[] columns = {helper.LOCATION};
        Cursor cursor = db2.query(helper.TABLE_NAME, columns, helper.FLOOR+" = '"+mFloorNum+"'" + "and " + helper.CURR_SIGN + "= '"+ mSignText+"'", null, null, null, null);

        //StringBuffer buffer = new StringBuffer();
        String locate = "";
        int i=0;
        while(cursor.moveToNext()) {
            int u_id = cursor.getColumnIndex(helper.LOCATION);
            locate = cursor.getString(u_id);

        }

        return locate;
    }
    public Integer getFloorCount(String build_name) {
        SQLiteDatabase db2 = helper.getWritableDatabase();
        String[] columns = {helper.FLOOR};
        Cursor cursor = db2.query(true,helper.TABLE_NAME,columns,helper.BUILD_NAME+ " = '"+build_name+"'",null,null,null,null,null);
        int i=cursor.getCount();
        return i;
    }
    public String[] getSigns(String mFloorNum) {
        SQLiteDatabase db2 = helper.getWritableDatabase();
        String[] columns = {helper.CURR_SIGN};

        Cursor cursor = db2.query(true,helper.TABLE_NAME,columns,helper.FLOOR+ " = '"+mFloorNum+"'",null,null,null,null,null);
        String[] signs = new String[cursor.getCount()];
        int i=0;
        int count = 0;
        while(cursor.moveToNext()){
            int u_id = cursor.getColumnIndex(helper.CURR_SIGN);
            signs[i]=cursor.getString(u_id);
            i++;
            count++;
        }
        Log.d(mDebug, "The value of count inside getSigns is: "+ count);
        Log.d(mDebug, "The value of signs inside getSigns is: "+ signs);
        return signs;
    }

    public class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME= "ILSDatabase";
        private static final String TABLE_NAME="ILSTable";
        private static final int DATABASE_VERSION = 57;
        private static final String BUILD_NAME="build_name";
        private static final String FLOOR="floor";
        private static final String PREV_SIGN="prev_sign";
        private static final String CURR_SIGN="curr_sign";
        private static final String NEXT_SIGN="next_sign";
        private static final String LOCATION="_location";
        private static final String CREATE_TABLE="CREATE TABLE "+ TABLE_NAME +" ("+BUILD_NAME+" VARCHAR(50),"+FLOOR+" VARCHAR(50), "+PREV_SIGN+" VARCHAR(50), " +
                " "+CURR_SIGN+" VARCHAR(50), "+NEXT_SIGN+" VARCHAR(50), "+LOCATION+" VARCHAR(50));";
        private static final String DROP_TABLE="Drop table IF EXISTS "+ TABLE_NAME;
        private Context context;

        //constructor- mandatory
        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null,DATABASE_VERSION);//takes context, datbasename, custom cursor and database version
            this.context=context;
            Log.d("Constructor", "Constructor called");
        }

        //onCreate called only when database doesn't exist
        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
                ContentValues cv= new ContentValues();
                for(int i = 0; i < EnterSignsAndLocation.mList.size(); i++){
                    cv.put(DatabaseHelper.BUILD_NAME, "Jordan Hall");
                    cv.put(DatabaseHelper.FLOOR,"Floor "+String.valueOf(i+1));
                    int count = 0;
                    for(Map.Entry me : EnterSignsAndLocation.mList.get(i).entrySet()){
                        cv.put(DatabaseHelper.CURR_SIGN, String.valueOf(me.getKey()));
                        cv.put(DatabaseHelper.LOCATION,String.valueOf(me.getValue()));
                        insertValues(db, cv);
                        count++;
                    }
                    Log.d(mDebug, "The value of count is: "+ count);
                }

            }catch (Exception e){
                e.printStackTrace();
            }


        }
        public void insertValues(SQLiteDatabase db, ContentValues cv){
            try{
                long id=db.insert(DatabaseHelper.TABLE_NAME,null, cv);
            }catch (Exception e){
                e.printStackTrace();
            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e){
            }
        }
    }

}
/**
 * Created by shardendu on 2/26/15.
 */

