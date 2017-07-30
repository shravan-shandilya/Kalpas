package kalpas.shrvn.xyz.kalpas;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Results extends AppCompatActivity {

    private ArrayList<String> results = null;
    private DatabaseHandler dbHandler;

    private TextView[] fields = new TextView[6];
    private TextView[] lables = new TextView[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        String formulation = (String) getIntent().getSerializableExtra("formulation");

        dbHandler = new DatabaseHandler(this);
        results = dbHandler.getFormulation(formulation);

        if(results != null) {
            fields[0] = (TextView) findViewById(R.id.field_name);
            fields[1] = (TextView) findViewById(R.id.field_ingredients);
            fields[2] = (TextView) findViewById(R.id.field_karmas);
            fields[3] = (TextView) findViewById(R.id.field_indications);
            fields[4] = (TextView) findViewById(R.id.field_anupana);
            fields[5] = (TextView) findViewById(R.id.field_adhikara);


            lables[0] = (TextView) findViewById(R.id.lable_name);
            lables[1] = (TextView) findViewById(R.id.lable_ingredients);
            lables[2] = (TextView) findViewById(R.id.lable_karmas);
            lables[3] = (TextView) findViewById(R.id.lable_indications);
            lables[4] = (TextView) findViewById(R.id.lable_anupana);
            lables[5] = (TextView) findViewById(R.id.lable_adhikara);


            for (int index = 0; index < results.toArray().length; index++) {
                if (!results.get(index).equals("") && !results.get(index).equals("__")) {
                    fields[index].setText(clean(results.get(index)));
                    fields[index].setTextSize(20);
                    fields[index].setTextColor(Color.BLUE);
                } else {
                    lables[index].setVisibility(View.GONE);
                    fields[index].setVisibility(View.GONE);
                }
            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private String clean(String str){
        String temp = "";
        int words_in_line = 0;
        for(String temp1:str.split("_")) {
            if(temp1.length() > 0) {
                temp += capitalize(temp1) + ",";
                if (words_in_line < 2) {
                    temp += "\n";
                    words_in_line = 0;
                }
                words_in_line++;
            }
        }
        return temp;
    }
    private String capitalize(String str){
        return Character.toUpperCase(str.charAt(0))+str.substring(1);
    }

    public void share(View view){
        Toast.makeText(getApplicationContext(),"Pressed",Toast.LENGTH_SHORT).show();
    }

}


