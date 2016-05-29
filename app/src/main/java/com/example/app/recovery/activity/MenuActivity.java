package com.example.app.recovery.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.app.recovery.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by app on 16/4/10.
 */
public class MenuActivity extends Activity {

    private GridView mGridView;
    private String[] itemMenu = {"残值单录入", "残值查询"};
    private Class[] childPanels = {PaperEnterActivity.class, PaperSearchActivity.class};
    private int[] imageRes = {R.drawable.entry_btn, R.drawable.search_btn};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        GridView mGridView = (GridView) findViewById(R.id.grid_view);

        class GridViewItemOnClick implements AdapterView.OnItemClickListener {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
//                Toast.makeText(getApplicationContext(), position + "",
//                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MenuActivity.this, childPanels[position]);
                startActivity(intent);
            }
        }

        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        int length = itemMenu.length;
        for (int i = 0; i < length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImageView", imageRes[i]);
            map.put("ItemTextView", itemMenu[i]);
            data.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(MenuActivity.this,
                data, R.layout.menu_item, new String[]{"ItemImageView", "ItemTextView"},
                new int[]{R.id.ItemImageView, R.id.ItemTextView});
        mGridView.setAdapter(simpleAdapter);
        mGridView.setOnItemClickListener(new GridViewItemOnClick());

        Button btn_back = (Button) findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
