package com.example.kylehirschfelder.curationmed;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MedicineDataBase extends AppCompatActivity {

    Button submit, localBtn, lvBtn;
    EditText editMed, editNoTabs, editMg, editExp, editOpen, editPatientId;
    MedicineDBHandler db;

    List<Medicine> medicineList = new ArrayList<Medicine>();
    List<Medicine> medicineCurList = new ArrayList<Medicine>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
        setContentView(R.layout.activity_medicine_data_base);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Declaration of all buttons and text fields
        db = new MedicineDBHandler(getApplicationContext());
        localBtn = (Button) findViewById(R.id.localBtn);
        submit = (Button) findViewById(R.id.submit);
        lvBtn = (Button) findViewById(R.id.lvBtn);
        editMed = (EditText) findViewById(R.id.medicine_name);
        editNoTabs = (EditText) findViewById(R.id.no_tab);
        editMg = (EditText) findViewById(R.id.mg_tab);
        editExp = (EditText) findViewById(R.id.expiry);
        editOpen = (EditText) findViewById(R.id.opened);
        editPatientId = (EditText) findViewById(R.id.patient_id);

        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.medicine_name);

        // Get the string array
        final String[] medicine = getResources().getStringArray(R.array.medicine_array);

        // Create the adapter and set it to the AutoCompleteTextView
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, medicine);

        textView.setAdapter(adapter);

        //This button submits the user-entered data to two local databases
        localBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Sets string variables to the user entered text fields
                String name = "" + editMed.getText().toString();
                String tab = "" + editMg.getText().toString();
                String exp_date = "" + editExp.getText().toString();
                String bott_date = "" + editOpen.getText().toString();
                String no_tab = "" + editNoTabs.getText().toString();
                String patient_id = "" + editPatientId.getText().toString();

                editExp.setText("");
                editMed.setText("");
                editNoTabs.setText("");
                editMg.setText("");
                editOpen.setText("");
                editPatientId.setText("");

                //Case handling if user has failed to enter every field
                if(name.equals("") || tab.equals("") || exp_date.equals("") || bott_date.equals("") || no_tab.equals("") || patient_id.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_LONG).show();
                    return;

                }else {
                    if(!name.equals("")) {
                        String msg = name + " has been added to the local database";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        //Creates two new objects to store in both local databases. One is
                        //for the medicine table, and the other is for the curated medicine table
                        Medicine medicineObj = new Medicine(db.getMedicineCount(), name,
                                tab, exp_date, bott_date, no_tab, patient_id);
                        Medicine medicineCurObj = new Medicine(db.getMedicineCurCount(), name,
                                tab, exp_date, bott_date, no_tab, patient_id);

                        //Creates a medicine objects and populates the local database with the contents
                        //that were obtained from the text fields above
                        db.createMedicine(medicineObj);
                        medicineList.add(medicineObj);
                        //db.createMedicineCur(medicineCurObj);
                        //medicineCurList.add(medicineCurObj);
                    }
                }
            }
        });

        //Launches the list view to view the medical inventory
        lvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), medicineListView.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_medicine_data_base, menu);
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
}