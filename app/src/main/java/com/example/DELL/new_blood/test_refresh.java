package com.example.DELL.new_blood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;

public class test_refresh extends AppCompatActivity {

    ListView listView;
    static final String[] FRUITS = new String[] { "Apple", "Avocado", "Banana",
            "Blueberry", "Coconut", "Durian", "Guava", "Kiwifruit",
            "Jackfruit", "Mango", "Olive", "Pear", "Sugar-apple" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh);

        listView = (ListView) findViewById(R.id.list);

       listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,FRUITS));

        PullRefreshLayout layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setRefreshStyle(PullRefreshLayout.STYLE_CIRCLES);
// listen refresh event
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(test_refresh.this,"Refresh",Toast.LENGTH_LONG).show();
            }
        });


// refresh complete
        layout.setRefreshing(false);
    }
}
