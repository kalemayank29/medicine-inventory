package com.example.kylehirschfelder.curationmed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class medicineListView extends AppCompatActivity {

    private static final int VERIFY = 0, DELETE = 1;

    ListView lv;
    Button btn;
    MedicineDBHandler db = new MedicineDBHandler(this);
    List<Medicine> medicineList = new ArrayList<Medicine>();
    List<Medicine> medicineListCur = new ArrayList<Medicine>();
    int longClickItemIndex = 0, longClickItemIndexCur = 0;
    ArrayAdapter<Medicine> medicineAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list_view);

        lv = (ListView) findViewById(R.id.medList);
        btn = (Button) findViewById(R.id.curBtn);
        List<Medicine> addableMed = db.getAllMedicine();


        int count = addableMed.size();

        for (int i = 0; i < count; i++) {
            medicineList.add(addableMed.get(i));
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), show_curated.class);
                startActivity(intent);
            }
        });

        populateList();
        registerForContextMenu(lv);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickItemIndex = position;
                return false;
            }
        });
    }

    public void populateList() {
        medicineAdapter = new medicineListAdapter(this.medicineList);
        lv.setAdapter(medicineAdapter);
        medicineAdapter.notifyDataSetChanged();
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info) {
        super.onCreateContextMenu(menu, view, info);
        menu.setHeaderIcon(R.drawable.edit_icon);
        menu.setHeaderTitle("Census Options");
        menu.add(Menu.NONE, VERIFY, menu.NONE, "Verify Medicine");
        menu.add(Menu.NONE, DELETE, menu.NONE, "Delete Medicine");

    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case VERIFY:
                List<NameValuePair> NameValuePairs = new ArrayList<NameValuePair>();


                    Medicine temp = medicineList.get(longClickItemIndex);
                    NameValuePairs.add(new BasicNameValuePair("name", temp.get_name()));
                    NameValuePairs.add(new BasicNameValuePair("tab", temp.get_mg()));
                    NameValuePairs.add(new BasicNameValuePair("exp_date", temp.get_expDate()));
                    NameValuePairs.add(new BasicNameValuePair("bott_date", temp.get_openDate()));
                    NameValuePairs.add(new BasicNameValuePair("no_tab", temp.get_noTabs()));
                    NameValuePairs.add(new BasicNameValuePair("patient_id", temp.get_patientId()));

                    db.createMedicineCur(temp);
                    db.deleteMed(medicineList.get(longClickItemIndex));
                    medicineList.remove(longClickItemIndex);
                    medicineAdapter.notifyDataSetChanged();

                break;

            case DELETE:
                Log.e("DELETE",String.valueOf(item.getItemId()));


                    db.deleteMed(medicineList.get(longClickItemIndex));
                    medicineList.remove(longClickItemIndex);
                    medicineAdapter.notifyDataSetChanged();

                    break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_medicine_list_view, menu);
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

    private class medicineListAdapter extends ArrayAdapter<Medicine> {

        List<Medicine> medList;
        public medicineListAdapter(List<Medicine> medList) {
            super(medicineListView.this, R.layout.medicine_item, medList);
            this.medList = medList;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.medicine_item, parent, false);
            }

            Medicine currentMedicine = medList.get(position);

            TextView medName = (TextView) view.findViewById(R.id.medName);
            medName.setText(currentMedicine.get_name());

            TextView medMg = (TextView) view.findViewById(R.id.medMg);
            medMg.setText(currentMedicine.get_mg());

            TextView medTab = (TextView) view.findViewById(R.id.medNoTab);
            medTab.setText(currentMedicine.get_noTabs());

            TextView medExp = (TextView) view.findViewById(R.id.medBotExp);
            medExp.setText(currentMedicine.get_expDate());

            TextView medOpen = (TextView) view.findViewById(R.id.medBotOpen);
            medOpen.setText(currentMedicine.get_openDate());

            TextView medPatId = (TextView) view.findViewById(R.id.medPatientId);
            medPatId.setText(currentMedicine.get_patientId());

            return view;
        }
    }
}
