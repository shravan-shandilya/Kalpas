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

public class FormulationResult extends AppCompatActivity {

    private DatabaseHandler dbHandler;
    private TextView tv_title,tv_row;
    private ListView lv_results;
    private ArrayList<String> results = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate);

        //Map componenets
        tv_title = (TextView) findViewById(R.id.tv_intermediate_title);
        lv_results = (ListView) findViewById(R.id.lv_intermediate_results);

        //Get information for query
        String formulation = (String) getIntent().getSerializableExtra("formulation");

        //Set title
        SpannableString title=  new SpannableString(Character.toUpperCase(formulation.charAt(0)) + formulation.substring(1));
        title.setSpan(new RelativeSizeSpan(2f), 0,formulation.length(), 0); // set size
        title.setSpan(new ForegroundColorSpan(Color.BLUE), 0, formulation.length(), 0);// set color
        tv_title.setText(title);
/*
        tv_row = (TextView) findViewById(R.id.row);
        if(formulation.equalsIgnoreCase("Asava & Arishta")){
            tv_row.setBackgroundColor(Color.MAGENTA);
        }else if(formulation.equalsIgnoreCase("Kashaya")){
            tv_row.setBackgroundColor(Color.GREEN);
        }else if(formulation.equalsIgnoreCase("Churna")){
            tv_row.setBackgroundColor(Color.BLUE);
        }else if(formulation.equalsIgnoreCase("Vati/Gutika/Rasa/Guggulu")){
            tv_row.setBackgroundColor(Color.);
        }else if(formulation.equalsIgnoreCase("Ghruta")){
            tv_row.setBackgroundColor(Color.MAGENTA);
        }else if(formulation.equalsIgnoreCase("Taila")){
            tv_row.setBackgroundColor(Color.MAGENTA);
        }else if(formulation.equalsIgnoreCase("Leha")){
            tv_row.setBackgroundColor(Color.MAGENTA);
        }else if(formulation.equalsIgnoreCase("Bhasma")){
            tv_row.setBackgroundColor(Color.MAGENTA);
        }else{
            tv_row.setBackgroundColor(Color.MAGENTA);
        }

*/
        //Query DB
        dbHandler = new DatabaseHandler(getApplicationContext());
        results.clear();
        if(formulation.equalsIgnoreCase("Asava & Arishta")){
            results.addAll(dbHandler.queryAllByType("Asava"));
            results.addAll(dbHandler.queryAllByType("Arishta"));
        }else if(formulation.equalsIgnoreCase("Vati/Gutika/Rasa/Guggulu")){
            results.addAll(dbHandler.queryAllByType("Vati"));
            results.addAll(dbHandler.queryAllByType("Gutika"));
            results.addAll(dbHandler.queryAllByType("Rasa"));
            results.addAll(dbHandler.queryAllByType("Guggulu"));
        }else{
            results.addAll(dbHandler.queryAllByType(formulation.toLowerCase()));
        }
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
