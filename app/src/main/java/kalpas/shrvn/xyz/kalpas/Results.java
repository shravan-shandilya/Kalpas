package kalpas.shrvn.xyz.kalpas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
                    fields[index].setText(results.get(index));
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

}
