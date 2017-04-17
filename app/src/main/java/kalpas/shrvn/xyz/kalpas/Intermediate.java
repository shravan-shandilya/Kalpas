package kalpas.shrvn.xyz.kalpas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Intermediate extends AppCompatActivity {

    private DatabaseHandler dbHandler;
    private TextView tv_title;
    private ListView lv_results;
    private ArrayList<String> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate);

        //Map componenets
        tv_title = (TextView) findViewById(R.id.tv_intermediate_title);
        lv_results = (ListView) findViewById(R.id.lv_intermediate_results);

        //Get information for query
        String query_string = (String) getIntent().getSerializableExtra("query_string");
        String query_type = (String) getIntent().getSerializableExtra("query_type");

        //Set title
        SpannableString title=  new SpannableString(Character.toUpperCase(query_type.charAt(0)) + query_type.substring(1)+"  ("+Character.toUpperCase(query_string.charAt(0)) + query_string.substring(1)+")");
        title.setSpan(new RelativeSizeSpan(2f), 0,query_type.length(), 0); // set size
        title.setSpan(new ForegroundColorSpan(Color.BLUE), 0, query_type.length(), 0);// set color
        tv_title.setText(title);

        //Query DB
        dbHandler = new DatabaseHandler(getApplicationContext());
        results = dbHandler.queryItem(query_type,query_string);
        for (int index =0 ;index <results.size();index++) {
            results.set(index,capitalize(results.get(index)));
        }

        //Show
        if(results != null) {
            lv_results.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.result_row, results));

            //Handle selection
            lv_results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (results != null) {
                        Intent intent = new Intent(getApplicationContext(), Results.class);
                        intent.putExtra("formulation", results.get(position).toLowerCase());
                        startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private String capitalize(String str){
        return Character.toUpperCase(str.charAt(0))+str.substring(1);
    }
}
