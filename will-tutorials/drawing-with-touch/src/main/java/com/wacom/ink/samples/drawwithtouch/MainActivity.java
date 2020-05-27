package com.wacom.ink.samples.drawwithtouch;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {

    private static final Class<?>[] activities = new Class<?>[]{
            DrawWithTouchPart01.class,
            DrawWithTouchPart02.class,
            DrawWithTouchPart03.class,
            DrawWithTouchPart04.class,
            DrawWithTouchPart05.class,
            DrawWithTouchPart06.class,
            DrawWithTouchPart07.class
    };

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        List<String> names = new ArrayList<String>();

        String[] activityTitles = getResources().getStringArray(R.array.activity_titles);

        for (int i = 0; i < activityTitles.length; i++) {
            names.add(activityTitles[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.textView, names);

        listView.setAdapter(adapter);

        listView.setDividerHeight(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(MainActivity.this, activities[position]);

                startActivity(intent);

            }

        });
    }

}
