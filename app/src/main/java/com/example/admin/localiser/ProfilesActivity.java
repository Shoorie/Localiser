package com.example.admin.localiser;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import static com.example.admin.localiser.ValuesInApp.Values.*;


public class ProfilesActivity extends ActionBarActivity{
    ListView listView;
    ArrayAdapter<String> adapter = null;
    private EditText filterText = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        // Get ListView object from xml
        filterText = (EditText) findViewById(R.id.search_box);
        filterText.addTextChangedListener(filterTextWatcher);
        listView = (ListView) findViewById(R.id.list);
        String[] values = new String[] {ADD_PROFILE, DELETE_PROFILE, DISPLAY_PROFILE,
                EDIT_PROFILE };
        adapter=new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_checked, values){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);
                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(Color.rgb(202, 202, 202));
                // ListView Clicked item index
               // int itemPosition = position;
                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                if (itemValue.matches(ADD_PROFILE)) {
                    addProf(view);
                } else if (itemValue.matches(DELETE_PROFILE)) {
                    delProf(view);
                } else if (itemValue.matches(DISPLAY_PROFILE)) {
                    dispProf(view);
                }
                else if(itemValue.matches(EDIT_PROFILE)){
                    editProf(view);
                }
            }
    });
    }

    public void addProf(View view){
        Intent intent = new Intent(this, AddProfileActivity.class);
        startActivity(intent);
    }
    public void delProf(View view){
        Intent intent = new Intent(this, DeleteProfileActivity.class);
        startActivity(intent);
    }
    public void dispProf(View view){
        Intent intent = new Intent(this, DisplayProfileActivity.class);
        startActivity(intent);
    }
    public void editProf(View view){
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            adapter.getFilter().filter(s);
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterText.removeTextChangedListener(filterTextWatcher);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profiles, menu);
        /*MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); */

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
