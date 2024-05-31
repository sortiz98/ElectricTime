package com.example.electrictime;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    private static boolean distanceMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Button distanceButton = findViewById(R.id.distance);
        Button timeButton = findViewById(R.id.time);
        EditText input = findViewById((R.id.input));

        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<>();
        for (int i = 0; i < MyData.transportArray.length; i++) {
            data.add(new DataModel(
                    MyData.transportArray[i],
                    MyData.versionArray[i],
                    MyData.speedArray[i],
                    MyData.rangeArray[i],
                    MyData.id_[i]
            ));
        }

        removedItems = new ArrayList<>();
        adapter = new CustomAdapter(data, input, distanceButton, timeButton);
        recyclerView.setAdapter(adapter);
        setListeners();

        String message = "Click to Push to Top";
        Toast toast = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 700);
        toast.show();

    }

    @SuppressLint("ClickableViewAccessibility")
    public void setListeners() {
        final Button distanceButton = findViewById(R.id.distance);
        final Button timeButton = findViewById(R.id.time);
        final EditText input = findViewById((R.id.input));
        final TextView setup = findViewById((R.id.context));
        final TextView inputUnits = findViewById(R.id.units);

        distanceButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                distanceButton.setPressed(true);
                timeButton.setPressed(false);
                adapter.notifyDataSetChanged();
                setup.setText("I have to travel");
                inputUnits.setText("miles");
                distanceMode = true;
                return true;
            }
        });
        timeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                timeButton.setPressed(true);
                distanceButton.setPressed(false);
                adapter.notifyDataSetChanged();
                setup.setText("I have a time limit");
                inputUnits.setText("minutes");
                distanceMode = false;
                return true;
            }
        });
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    adapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    return false;
                }
                return false;
            }
        });

        distanceButton.setPressed(true);
        timeButton.setPressed(false);
        adapter.notifyDataSetChanged();
        setup.setText("I have to travel");
        inputUnits.setText("miles");
    }

    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            removeItem(v);
            addRemovedItemToList();
            layoutManager.scrollToPosition(0);
        }

        private void removeItem(View v) {
            int selectedItemPosition = recyclerView.getChildLayoutPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForLayoutPosition(selectedItemPosition);
            TextView textViewTransport
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewTransport);
            String selectedTransport = (String) textViewTransport.getText();
            int selectedItemId = -1;
            for (int i = 0; i < MyData.transportArray.length; i++) {
                if (selectedTransport.equals(MyData.transportArray[i])) {
                    selectedItemId = MyData.id_[i];
                }
            }
            removedItems.add(selectedItemId);
            data.remove(selectedItemPosition);
            adapter.notifyItemRemoved(selectedItemPosition);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public static void addRemovedItemToList() {
        int addItemAtListPosition = 0;
        data.add(addItemAtListPosition, new DataModel(
                MyData.transportArray[removedItems.get(0)],
                MyData.versionArray[removedItems.get(0)],
                MyData.speedArray[removedItems.get(0)],
                MyData.rangeArray[removedItems.get(0)],
                MyData.id_[removedItems.get(0)]
        ));
        removedItems.remove(0);
        adapter.notifyDataSetChanged();
    }

    public static boolean getDistanceMode() {
        return distanceMode;
    }
}