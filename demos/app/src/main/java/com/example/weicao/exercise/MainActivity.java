package com.example.weicao.exercise;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import static java.util.Collections.reverseOrder;

public class MainActivity extends AppCompatActivity {
    private Button button1;
    private Button button2;
    private AutoCompleteTextView auto;
    private InputMethodManager imm;
    public String info;
    public Spinner sortby;
    public Spinner orderby;
    public ArrayAdapter adapter1;
    public ArrayAdapter adapter2;
    private RequestQueue requestQueue;
    private int len;
    public static String tranname = "";
    private Switch swt;
    private ProgressBar pb;
    private ImageButton im;
    String stockname;
    String lastprice;
    String changeFinal;
    ListView listView;
    public MyAdapter adapter;
    String[] data1=new String[]{"Sort by","Default","Symbol","Price","Change"};
    String[] data2=new String[]{"Order by","Ascending","Descending"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        auto = (AutoCompleteTextView) findViewById(R.id.stockInput);
        //imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(edit.getWindowToken(),0);
        //edit.setInputType(0);
        auto.setInputType(InputType.TYPE_NULL);


        button1 = (Button)findViewById(R.id.btn1);
        button1.setOnClickListener(new ButtonListener());
        button2 = (Button)findViewById(R.id.btn2);
        button2.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                auto.setText("");
            }
        });
        pb = (ProgressBar)findViewById(R.id.pb);
        pb.setVisibility(View.GONE);
        swt = (Switch)findViewById(R.id.swt);
        swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pb.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {  //10秒后执行该方法
                            // handler自带方法实现定时器
                            try {
                                pb.setVisibility(View.GONE); //隐藏
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    };
                    handler.postDelayed(runnable, 3000); //10秒后执行runnable 的run方法
                }
            }
        });
        im = (ImageButton)findViewById(R.id.refre);
        im.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                pb.setVisibility(View.VISIBLE);
                Handler handler = new Handler();

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {  //10秒后执行该方法
                        // handler自带方法实现定时器
                        try {
                            pb.setVisibility(View.GONE); //隐藏
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                };
                handler.postDelayed(runnable, 3000); //10秒后执行runnable 的run方法

            }
        });




        sortby = (Spinner)findViewById(R.id.spSort);
        orderby = (Spinner)findViewById(R.id.spOrder);

        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data1){
            public boolean isEnabled(int position) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    return false;
                }
                return true;
            }
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                // TODO Auto-generated method stub
                View mView = super.getDropDownView(position, convertView, parent);
                TextView mTextView = (TextView) mView;
                if (position == 0) {
                    mTextView.setTextColor(Color.GRAY);
                } else {
                    mTextView.setTextColor(Color.BLACK);
                }
                return mView;
            }
        };

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortby.setAdapter(adapter1);

        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data2){
            public boolean isEnabled(int position) {
                // TODO Auto-generated method stub
                if (position == 0 ) {
                    return false;
                }
                return true;
            }
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                // TODO Auto-generated method stub
                View mView = super.getDropDownView(position, convertView, parent);
                TextView mTextView = (TextView) mView;
                if (position == 0) {
                    mTextView.setTextColor(Color.GRAY);
                } else {
                    mTextView.setTextColor(Color.BLACK);
                }
                return mView;
            }
        };

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderby.setAdapter(adapter2);
        sortby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 2){
                    Collections.sort(adapter.arr1);
                    adapter.notifyDataSetChanged();
                }if (position == 3){
                    Collections.sort(adapter.arr2);
                    adapter.notifyDataSetChanged();

                }if (position == 4){
                    Collections.sort(adapter.arr3);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });
        orderby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 2){
                    Collections.sort(adapter.arr1,reverseOrder());
                    Collections.sort(adapter.arr2,reverseOrder());
                    Collections.sort(adapter.arr3,reverseOrder());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                Log.d("msg",s.toString());
                String url = "http://hw9and.us-east-2.elasticbeanstalk.com/?symbol=" + s.toString();
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                ArrayList<String> stockinfos = new ArrayList<String>();
                                try {
                                    for(int i = 0; i < 5; i++){
                                        JSONObject jresponse = response.getJSONObject(i);
                                        String symbol = jresponse.getString("Symbol");
                                        String name = jresponse.getString("Name");
                                        String exchange = jresponse.getString("Exchange");
                                        String stock = symbol + " - " + name + " (" + exchange + ")";
                                        stockinfos.add(stock);
                                        Log.d("Name", name);
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.
                                            this, android.R.layout.simple_dropdown_item_1line, stockinfos);
                                    auto.setAdapter(adapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Nothing found!", Toast.LENGTH_SHORT);
                            }
                        });
                //add request to queue
                requestQueue.add(jsonArrayRequest);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        listView = (ListView) findViewById(R.id.mylist);
        adapter = new MyAdapter(this);
        listView.setAdapter(adapter);

    }
    protected void onStart(){
        super.onStart();
        auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                Log.d("msg",s.toString());
                String url = "http://hw9and.us-east-2.elasticbeanstalk.com/?symbol=" + s.toString();
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                ArrayList<String> stockinfos = new ArrayList<String>();
                                try {
                                    for(int i = 0; i < 5; i++){
                                        JSONObject jresponse = response.getJSONObject(i);
                                        String symbol = jresponse.getString("Symbol");
                                        String name = jresponse.getString("Name");
                                        String exchange = jresponse.getString("Exchange");
                                        String stock = symbol + " - " + name + " (" + exchange + ")";
                                        stockinfos.add(stock);
                                        Log.d("Name", name);
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.
                                            this, android.R.layout.simple_dropdown_item_1line, stockinfos);
                                    auto.setAdapter(adapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Nothing found!", Toast.LENGTH_SHORT);
                            }
                        });
                //add request to queue
                requestQueue.add(jsonArrayRequest);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                Log.d("msg",s.toString());
                String url = "http://hw9and.us-east-2.elasticbeanstalk.com/?symbol=" + s.toString();
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                ArrayList<String> stockinfos = new ArrayList<String>();
                                try {
                                    for(int i = 0; i < 5; i++){
                                        JSONObject jresponse = response.getJSONObject(i);
                                        String symbol = jresponse.getString("Symbol");
                                        String name = jresponse.getString("Name");
                                        String exchange = jresponse.getString("Exchange");
                                        String stock = symbol + " - " + name + " (" + exchange + ")";
                                        stockinfos.add(stock);
                                        Log.d("Name", name);
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.
                                            this, android.R.layout.simple_dropdown_item_1line, stockinfos);
                                    auto.setAdapter(adapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Nothing found!", Toast.LENGTH_SHORT);
                            }
                        });
                //add request to queue
                requestQueue.add(jsonArrayRequest);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        if(PlaceholderFragment1.flag == 1 ){
            stockname = PlaceholderFragment1.s1;
            lastprice = PlaceholderFragment1.s2;
            changeFinal = PlaceholderFragment1.s3;
            tranname = stockname;
            if (!adapter.arr1.contains(stockname)) {
                adapter.arr1.add(stockname);
                adapter.arr2.add(lastprice);
                adapter.arr3.add(changeFinal);
                Log.d("stockname", stockname);
                Log.d("lastprice", lastprice);
                Log.d("changeFinal", changeFinal);
                adapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        //Toast.makeText(getContext(), position, Toast.LENGTH_SHORT);
                        //info = auto.getText().toString();
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, ParentActivity.class);
                        intent.putExtra("com.example.weicao.exercise.Stock", stockname);
                        Log.d("debug", "intent in main");
                        startActivity(intent);
                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                   final int position, long id) {
                        //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Remove From Favourites?");

                        //添加AlertDialog.Builder对象的setPositiveButton()方法
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.arr1.remove(position);
                                adapter.arr2.remove(position);
                                adapter.arr3.remove(position);
                                tranname = "";

                                adapter.notifyDataSetChanged();
                            }
                        });

                        //添加AlertDialog.Builder对象的setNegativeButton()方法
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.create().show();
                        return false;
                    }
                });
            }
        }else if(PlaceholderFragment1.flag == 2){
            stockname = PlaceholderFragment1.s1;
            lastprice = PlaceholderFragment1.s2;
            changeFinal = PlaceholderFragment1.s3;
            adapter.arr1.remove(stockname);
            adapter.arr2.remove(lastprice);
            adapter.arr3.remove(changeFinal);
            adapter.notifyDataSetChanged();
        }

    }

    private class MyAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;
        public ArrayList<String> arr1;
        public ArrayList<String> arr2;
        public ArrayList<String> arr3;
        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arr1 = new ArrayList<String>();
            arr2 = new ArrayList<String>();
            arr3 = new ArrayList<String>();

        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arr1.size();
        }
        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }
        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }
        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if(view == null){
                view = inflater.inflate(R.layout.mainlist, null);
            }

            TextView textView_symbol = (TextView)view.findViewById(R.id.textView);
            TextView textView_last = (TextView)view.findViewById(R.id.textView2);
            TextView textView_change = (TextView)view.findViewById(R.id.textView4);

            textView_symbol.setText(arr1.get(position));
            textView_last.setText(arr2.get(position));
            textView_change.setText(arr3.get(position));
            if (arr3.get(position).substring(0,1).equals("-")){
                textView_change.setTextColor(Color.rgb(255, 0, 0));
            }else{
                textView_change.setTextColor(Color.rgb(0, 255, 0));
            }

            return view;
        }
    }


    class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if (auto.getText().toString().trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter a stock name or symbol", Toast.LENGTH_SHORT).show();
            }else {
                info = auto.getText().toString();
                if (info.indexOf(" ") > -1) {
                    info = info.substring(0, info.indexOf(" "));
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ParentActivity.class);
                    intent.putExtra("com.example.weicao.exercise.Stock", info);
                    Log.d("debug", "intent in main");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ParentActivity.class);
                    intent.putExtra("com.example.weicao.exercise.Stock", info);
                    Log.d("debug", "intent in main");
                    startActivity(intent);
                }
            }
        }
    }




}
