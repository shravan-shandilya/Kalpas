package kalpas.shrvn.xyz.kalpas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

public class Results extends AppCompatActivity {

    private ArrayList<String> result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        result = (ArrayList<String>) getIntent().getSerializableExtra("formulation");
        String temp = new String();
        for(String temp1:result){
            temp += temp1+"\n";
        }
        Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_LONG).show();


    }

}
