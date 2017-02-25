package kalpas.shrvn.xyz.kalpas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by shts on 06/02/17.
 */

public class DatabaseHandler extends SQLiteAssetHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "kalpas.db";
    private static final String TABLE = "kalpas";
    private static SQLiteDatabase db = null;

    private static final String REMOTE_DB_API = "http://192.168.0.6:5000/api/v1/version";
    //private static final String REMOTE_DB_API = "http://api.coindesk.com/v1/bpi/currentprice.json";
    private Context thisContext = null;

    public boolean status = false;

    public DatabaseHandler(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        this.thisContext = context;
        if(!checkDatabase()){
            this.status = false;
            return;
        }
        this.status = true;
    }

    public boolean checkDatabase(){
        //Check local Database
        try{
            db = getReadableDatabase();
        }catch(SQLiteException e){
            e.printStackTrace();
        }

        //Check if the local DB is of latest version.
        //Fetch the latest version
        RequestQueue queue = Volley.newRequestQueue(thisContext);
        StringRequest versionRequest = new StringRequest(Request.Method.GET, REMOTE_DB_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(thisContext,response,Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(thisContext,"Something went wrong :( ",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(versionRequest);
        return db != null;
    }

    public ArrayList<String> getIndications(){
        if(status) {
            HashSet<String> indications = new HashSet<String>();
            Cursor cursorIndications = db.rawQuery("select indications from " + TABLE, null);
            cursorIndications.moveToFirst();
            do {
                String temp = cursorIndications.getString(cursorIndications.getColumnIndex("indications"));
                if(!temp.equals("__")) {
                    for(String temp_split:temp.split("_")){
                        if(temp_split.length() > 1 ){
                            indications.add(Character.toUpperCase(temp_split.charAt(0))+temp_split.substring(1));
                        }
                    }
                }
            } while (cursorIndications.moveToNext());
            return new ArrayList<String>(indications);
        }
        return null;

    }

    public ArrayList<String> getKarmas(){
        if(status){
            HashSet<String> karmas = new HashSet<String>();
            Cursor cursorIndications = db.rawQuery("select karmas from " + TABLE, null);
            cursorIndications.moveToFirst();
            do {
                String temp = cursorIndications.getString(cursorIndications.getColumnIndex("karmas"));
                if(!temp.equals("__")) {
                    for(String temp_split:temp.split("_")){
                        if((!karmas.contains(temp_split)) && (temp_split.length() > 1 )){
                            karmas.add(Character.toUpperCase(temp_split.charAt(0))+temp_split.substring(1));
                        }
                    }
                }
            } while (cursorIndications.moveToNext());
            return new ArrayList<String>(karmas);
        }
        return null;
    }

    public ArrayList<String> queryIndication(String indication){
        if(status){
            ArrayList<String> result = new ArrayList<String>();
            String query = "select * from "+TABLE+" where indications glob "+"'*_"+indication.toLowerCase()+"_*'";
            Cursor queryIndication = db.rawQuery(query,null);
            if(queryIndication.moveToFirst()) {
                do {
                    result.add(queryIndication.getString(queryIndication.getColumnIndex("name")));
                } while (queryIndication.moveToNext());
            }
            queryIndication.close();
            return result;
        }
        return null;
    }

    public ArrayList<String> queryKarma(String karma){
        if(status){
            ArrayList<String> result = new ArrayList<String>();
            String query = "select * from "+TABLE+" where karmas glob "+"'*_"+karma.toLowerCase()+"_*'";
            Cursor queryKarma = db.rawQuery(query,null);
            if(queryKarma.moveToFirst()) {
                ;
                do {
                    result.add(queryKarma.getString(queryKarma.getColumnIndex("name")));
                } while (queryKarma.moveToNext());
            }
            queryKarma.close();
            return result;
        }
        return null;
    }


    public ArrayList<String> getFormulation(String formula){
        if(status) {
            ArrayList<String> result = new ArrayList<String>();
            String query = "select * from " + TABLE + " where name = '" + formula.toLowerCase() +"'";
            System.out.println("Query:  "+ query);
            Cursor queryName = db.rawQuery(query,null);
            if(queryName.moveToFirst()) {
                if (queryName.getCount() == 1) {
                    result.add(queryName.getString(queryName.getColumnIndex("ingredients")));
                    result.add(queryName.getString(queryName.getColumnIndex("karmas")));
                    result.add(queryName.getString(queryName.getColumnIndex("indications")));
                    result.add(queryName.getString(queryName.getColumnIndex("anupana")));
                    result.add(queryName.getString(queryName.getColumnIndex("adhikara")));
                }
            }
            queryName.close();
            return result;
        }
        return null;
    }
}
