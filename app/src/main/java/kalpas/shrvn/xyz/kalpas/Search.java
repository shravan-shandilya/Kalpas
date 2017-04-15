package kalpas.shrvn.xyz.kalpas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Search extends AppCompatActivity {

    private Spinner spiKashayas,spiSearch;
    private MultiAutoCompleteTextView acKashaya,acSearch;
    private Button bSearch;
    private Toolbar toolbar;
    private RadioGroup rgOptions;

    private DatabaseHandler dbHandler;
    private Runnable run;

    private boolean indicationSelected = true;
    private boolean karmaSelected = false;
    private boolean spinnerRefreshed = false;
    private boolean spinnerKashayaTouched = false;

    private ArrayAdapter acAdapter = null;
    private ArrayAdapter spiAdapter = null;

    List<String> indications = null;
    List<String> indicationsSpinner = null;
    List<String> karmas = null;
    List<String> karmasSpinner = null;
    List<String> kashayas = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Linking widgets
        spiKashayas = (Spinner) findViewById(R.id.kashayas);
        //spiIndications = (Spinner) findViewById(R.id.indications);
        //spiKarmas = (Spinner) findViewById(R.id.karmas);
        rgOptions = (RadioGroup)findViewById(R.id.rgSearch);
        acKashaya = (MultiAutoCompleteTextView) findViewById(R.id.acKashaya);
        acSearch = (MultiAutoCompleteTextView) findViewById(R.id.acSearch);
        spiSearch = (Spinner) findViewById(R.id.spinner2);
        bSearch = (Button) findViewById(R.id.bSearch);


        //DB read
        dbHandler = new DatabaseHandler(this);
        indications = dbHandler.getIndications();
        karmas = dbHandler.getKarmas();
        kashayas = new ArrayList<>();
        kashayas.add(0,"Select");
        kashayas.addAll(dbHandler.getKashayas());


        if(dbHandler.status & (indications != null) & (karmas != null) & (kashayas != null)) {
            //Adapter things for Spinner and Autocomplete
            spiKashayas.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,kashayas));
            acAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>(getItemsForAutoComplete()));
            spiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,getItemsForSpinner());

            acSearch.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            acSearch.setAdapter(acAdapter);
            acKashaya.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            acKashaya.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>(kashayas)));
            spiSearch.setAdapter(spiAdapter);

            acKashaya.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getApplicationContext(),"You selected:"+parent.getAdapter().getItem(position),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Results.class);
                    intent.putExtra("formulation", parent.getAdapter().getItem(position).toString());
                    startActivity(intent);
                }
            });

            spiKashayas.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    spinnerKashayaTouched = true;
                    return false;
                }
            });
            spiKashayas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!spinnerKashayaTouched | position == 0)return;
                    Intent intent = new Intent(getApplicationContext(), Results.class);
                    intent.putExtra("formulation", kashayas.get(position));
                    startActivity(intent);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            //ActionListener for Spinner
            spiSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(spinnerRefreshed){
                        spinnerRefreshed = false;
                        position = 0;
                    }else if(!indicationsSpinner.get(position).equals("Select")) {
                        acSearch.setText(acSearch.getText() + getItemAtPosition(position) + ", ");
                    }else{
                        //do nothing
                    }
                    spiSearch.setSelection(position,false);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //do nothing
                    Toast.makeText(getApplicationContext(),"Nothing selected",Toast.LENGTH_SHORT).show();
                }
            });

            //Action Listerner for button
            bSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Hide keyboard
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(acSearch.getWindowToken(), 0);

                    //TODO: Sanitize query_item
                    String query_item = acSearch.getText().toString();
                    query_item = query_item.split(",")[0].toLowerCase().replace(" ", "");
                    Intent intent = new Intent(getApplicationContext(),Intermediate.class);
                    intent.putExtra("query_type", (indicationSelected)?"indications":"karmas");
                    intent.putExtra("query_string",query_item);
                    startActivity(intent);
                }
            });

            run = new Runnable() {
                public void run() {
                    acAdapter.clear();
                    spiAdapter.clear();
                    acAdapter.notifyDataSetInvalidated();
                    spiAdapter.notifyDataSetInvalidated();
                    acAdapter.addAll(getItemsForAutoComplete());
                    spiAdapter.addAll(getItemsForSpinner());
                    acAdapter.notifyDataSetChanged();
                    spiAdapter.notifyDataSetChanged();
                    acSearch.setHint(getItemHint());
                    acSearch.setText("");
                }
            };
            runOnUiThread(run);

            rgOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    if(checkedId == R.id.rbIndication){
                        indicationSelected = true;
                        karmaSelected = false;
                    }else if(checkedId == R.id.rbKarma){
                        indicationSelected = false;
                        karmaSelected = true;
                    }else{
                        //Do nothing
                    }
                    spinnerRefreshed = true;
                    runOnUiThread(run);
                }
            });
        }else{
            //DB read failure, try to recover(download latest db from remote) and restart
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<String> getItemsForAutoComplete(){
        if(indicationSelected & !karmaSelected)
            return indications;
        else if(!indicationSelected & karmaSelected)
            return karmas;
        else
            return null;
    }

    private List<String> getItemsForSpinner(){
        if(indicationSelected & !karmaSelected) {
            indicationsSpinner = new ArrayList<String>();
            indicationsSpinner.add(0, "Select");
            indicationsSpinner.addAll(1, indications);
            return indicationsSpinner;
        }else if(!indicationSelected & karmaSelected){
            karmasSpinner = new ArrayList<String>();
            karmasSpinner.add(0, "Select");
            karmasSpinner.addAll(1, karmas);
            return karmasSpinner;
        }else
            return null;
    }

    private String getItemAtPosition(int position){
        if(indicationSelected & !karmaSelected) {
            return indicationsSpinner.get(position);
        }else if(!indicationSelected & karmaSelected){
            return karmasSpinner.get(position);
        }else
            return null;
    }

    private CharSequence getItemHint(){
        if(indicationSelected & !karmaSelected) {
            return "Indication/s";
        }else if(!indicationSelected & karmaSelected){
            return "Karma/s";
        }else
            return "Something is wrong :(";
    }
}


