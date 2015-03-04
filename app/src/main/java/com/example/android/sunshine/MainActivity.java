package com.example.android.sunshine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            String[] values = new String[]{
                                "Today - Sunny - 83/63"
                                ,"Tomorrow - Sunny - 83/63"
                                ,"Weds - Sunny - 83/63"
                                ,"Weds - Sunny - 83/63"
                                ,"Thurs - Sunny - 83/63"
                                ,"Fri - Sunny - 83/63"
                                ,"Sat - Sunny - 83/63"
                                ,"Sun - Sunny - 83/63"};

            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < values.length; ++i) {
                list.add(values[i]);
            }

            ArrayAdapter  myAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast, list);
            final ListView listview = (ListView) this.getView().findViewById(R.id.listView_forecast);
            //ListView myListView = (ListView) this.getView().findViewById(R.id.listView_forecast);
            //myListView.setAdapter(myAdapter);
            return rootView;
        }
    }
}
