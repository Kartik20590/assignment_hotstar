package creativex.com.myapplication.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import creativex.com.myapplication.Adapter.CatagoryAdapter;
import creativex.com.myapplication.R;

/**
 * Created by karthik on 7/3/16.
 */
public class CatagoryActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {


    private ListView listView;
    private CatagoryAdapter mCatagoryAdapter;
    public static String[] catagoryList = {"food", "gym", "school", "hospital", "spa", "restaurant"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        mToolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolBar);

        listView = (ListView) findViewById(R.id.lv_catagory);
        mCatagoryAdapter = new CatagoryAdapter(this, catagoryList);
        listView.setAdapter(mCatagoryAdapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String catagory = "";
        switch (position) {
            case 0:
                catagory = "food";
                break;
            case 1:
                catagory = "gym";
                break;
            case 2:
                catagory = "school";
                break;
            case 3:
                catagory = "hospital";
                break;
            case 4:
                catagory = "spa";
                break;
            case 5:
                catagory = "restaurant";
                break;
        }

        Intent i = new Intent(CatagoryActivity.this, MainActivity.class);
        i.putExtra("CATAGORY", catagory);
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
}
