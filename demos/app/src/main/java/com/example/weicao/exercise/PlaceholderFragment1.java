package com.example.weicao.exercise;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by weicao on 11/27/17.
 */

public class PlaceholderFragment1 extends Fragment {
    String[] names = {"Stock Symbol", "Last Price", "Change", "Timestamp", "Open", "Close", "Day's Range", "Volume"};
    String[] datas = {"aapl", "134.2", "1232", "1:0:0", "120", "130", "10(10%)", "21345654321"};
    String[] indicas = {"Price","SMA","EMA","STOCH", "RSI", "ADX", "CCI", "BBANDS", "MACD"};
    private RequestQueue requestQ;
    private View rootView;
    private List<String> stocks = Arrays.asList(new String[]{"Stock Symbol", "Last Price", "Change", "Timestamp", "Open", "Close", "Day's Range", "Volume"});
    public static String s1 = "";
    public static String s2 = "";
    public static String s3 = "";
    public static int flag = 0;
    public int buttonflag = 0;
    private TextView fail;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment1() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment1 newInstance(int sectionNumber) {
        PlaceholderFragment1 fragment = new PlaceholderFragment1();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.fragment_main2, null);
            //rootView = inflater.inflate(R.layout.fragment_main2, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            final ImageButton face = (ImageButton)rootView.findViewById(R.id.face);
            fail = (TextView)rootView.findViewById(R.id.failshow);
            fail.setVisibility(View.GONE);

            TextView inda = (TextView)rootView.findViewById(R.id.indica);
            final Button chan = (Button) rootView.findViewById(R.id.change);
            final ProgressBar spinner = (ProgressBar) rootView.findViewById(R.id.processBar1);
            final ProgressBar bar2 = (ProgressBar) rootView.findViewById(R.id.processBar2);
            bar2.setVisibility(View.GONE);

            final ListView listView = (ListView)rootView.findViewById(R.id.listView);
            listView.setVisibility(View.GONE);
            textView.setText("Stock Details");
            textView.setTextSize(25);
            textView.setTextColor(Color.rgb(0, 0, 0));
            face.setImageResource(R.drawable.facebook);


            final StocksAdapter stocksAdapter = new StocksAdapter();
            listView.setAdapter(stocksAdapter);

            inda.setText("Indicators");
            chan.setText("Change");

            Spinner indicators = (Spinner)rootView.findViewById(R.id.spin);
            final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, indicas);
            //ArrayAdapter<String> listArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.list_content)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            indicators.setAdapter(adapter);


            final WebView viewWeb = (WebView)rootView.findViewById(R.id.webview);
            viewWeb.getSettings().setJavaScriptEnabled(true);
            viewWeb.setVisibility(View.GONE);
            indicators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                //"Price","SMA","EMA","STOCH", "RSI", "ADX", "CCI", "BBANDS", "MACD"
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    buttonflag = 1;
                    if (position == 0){
                        chan.setTextColor(Color.BLACK);
                        chan.setEnabled(true);
                        chan.setOnClickListener(new Button.OnClickListener(){
                            public void onClick(View view){
                                if (buttonflag == 1) {
                                    bar2.setVisibility(View.VISIBLE);
                                    viewWeb.setVisibility(View.GONE);
                                    ParentActivity activity = (ParentActivity) getActivity();
                                    String stockname = activity.getMyData();
                                    String url = "http://hw9price.us-east-2.elasticbeanstalk.com/?symbol=" + stockname;
                                    viewWeb.loadUrl(url);
                                    bar2.setVisibility(View.GONE);
                                    viewWeb.setVisibility(View.VISIBLE);
                                    buttonflag = 2;
                                    chan.setTextColor(Color.GRAY);
                                    chan.setEnabled(false);
                                }
                            }
                        });
                    }if (position == 1){
                        chan.setTextColor(Color.BLACK);
                        chan.setEnabled(true);
                        chan.setOnClickListener(new Button.OnClickListener(){
                            public void onClick(View view) {
                                if (buttonflag == 1) {
                                    bar2.setVisibility(View.VISIBLE);
                                    viewWeb.setVisibility(View.GONE);
                                    ParentActivity activity = (ParentActivity) getActivity();
                                    String stockname = activity.getMyData();
                                    String url = "http://10.0.2.2/sma.php?symbol=" + stockname;
                                    viewWeb.loadUrl(url);
                                    bar2.setVisibility(View.GONE);
                                    viewWeb.setVisibility(View.VISIBLE);
                                    buttonflag = 2;
                                    chan.setTextColor(Color.GRAY);
                                    chan.setEnabled(false);
                                }
                            }
                        });
                    }if (position == 2){
                        chan.setTextColor(Color.BLACK);
                        chan.setEnabled(true);
                        chan.setOnClickListener(new Button.OnClickListener(){
                            public void onClick(View view){
                                bar2.setVisibility(View.VISIBLE);
                                viewWeb.setVisibility(View.GONE);
                                ParentActivity activity = (ParentActivity) getActivity();
                                String stockname = activity.getMyData();
                                String url = "http://10.0.2.2/ema.php?symbol=" + stockname;
                                viewWeb.loadUrl(url);
                                bar2.setVisibility(View.GONE);
                                viewWeb.setVisibility(View.VISIBLE);
                                buttonflag = 2;
                                chan.setTextColor(Color.GRAY);
                                chan.setEnabled(false);
                            }
                        });
                    }if (position == 3){
                        chan.setTextColor(Color.BLACK);
                        chan.setEnabled(true);
                        chan.setOnClickListener(new Button.OnClickListener(){
                            public void onClick(View view){
                                bar2.setVisibility(View.VISIBLE);
                                viewWeb.setVisibility(View.GONE);
                                ParentActivity activity = (ParentActivity) getActivity();
                                String stockname = activity.getMyData();
                                String url = "http://10.0.2.2/stoch.php?symbol=" + stockname;
                                viewWeb.loadUrl(url);
                                bar2.setVisibility(View.GONE);
                                viewWeb.setVisibility(View.VISIBLE);
                                buttonflag = 2;
                                chan.setTextColor(Color.GRAY);
                                chan.setEnabled(false);
                            }
                        });
                    }if (position == 4){
                        chan.setTextColor(Color.BLACK);
                        chan.setEnabled(true);
                        chan.setOnClickListener(new Button.OnClickListener(){
                            public void onClick(View view){
                                bar2.setVisibility(View.VISIBLE);
                                viewWeb.setVisibility(View.GONE);
                                ParentActivity activity = (ParentActivity) getActivity();
                                String stockname = activity.getMyData();
                                String url = "http://10.0.2.2/rsi.php?symbol=" + stockname;
                                viewWeb.loadUrl(url);
                                bar2.setVisibility(View.GONE);
                                viewWeb.setVisibility(View.VISIBLE);
                                buttonflag = 2;
                                chan.setTextColor(Color.GRAY);
                                chan.setEnabled(false);
                            }
                        });
                    }if (position == 5){
                        chan.setTextColor(Color.BLACK);
                        chan.setEnabled(true);
                        chan.setOnClickListener(new Button.OnClickListener(){
                            public void onClick(View view){
                                bar2.setVisibility(View.VISIBLE);
                                viewWeb.setVisibility(View.GONE);
                                ParentActivity activity = (ParentActivity) getActivity();
                                String stockname = activity.getMyData();
                                String url = "http://10.0.2.2/adx.php?symbol=" + stockname;
                                viewWeb.loadUrl(url);
                                bar2.setVisibility(View.GONE);
                                viewWeb.setVisibility(View.VISIBLE);
                                buttonflag = 2;
                                chan.setTextColor(Color.GRAY);
                                chan.setEnabled(false);
                            }
                        });
                    }if (position == 6){
                        chan.setTextColor(Color.BLACK);
                        chan.setEnabled(true);
                        chan.setOnClickListener(new Button.OnClickListener(){
                            public void onClick(View view){
                                bar2.setVisibility(View.VISIBLE);
                                viewWeb.setVisibility(View.GONE);
                                ParentActivity activity = (ParentActivity) getActivity();
                                String stockname = activity.getMyData();
                                String url = "http://10.0.2.2/cci.php?symbol=" + stockname;
                                viewWeb.loadUrl(url);
                                bar2.setVisibility(View.GONE);
                                viewWeb.setVisibility(View.VISIBLE);
                                buttonflag = 2;
                                chan.setTextColor(Color.GRAY);
                                chan.setEnabled(false);
                            }
                        });
                    }if (position == 7){
                        chan.setTextColor(Color.BLACK);
                        chan.setEnabled(true);
                        chan.setOnClickListener(new Button.OnClickListener(){
                            public void onClick(View view){
                                bar2.setVisibility(View.VISIBLE);
                                viewWeb.setVisibility(View.GONE);
                                ParentActivity activity = (ParentActivity) getActivity();
                                String stockname = activity.getMyData();
                                String url = "http://10.0.2.2/bbands.php?symbol=" + stockname;
                                viewWeb.loadUrl(url);
                                bar2.setVisibility(View.GONE);
                                viewWeb.setVisibility(View.VISIBLE);
                                buttonflag = 2;
                                chan.setTextColor(Color.GRAY);
                                chan.setEnabled(false);
                            }
                        });
                    }if (position == 8){
                        chan.setTextColor(Color.BLACK);
                        chan.setEnabled(true);
                        chan.setOnClickListener(new Button.OnClickListener(){
                            public void onClick(View view){
                                bar2.setVisibility(View.VISIBLE);
                                viewWeb.setVisibility(View.GONE);
                                ParentActivity activity = (ParentActivity) getActivity();
                                String stockname = activity.getMyData();
                                String url = "http://10.0.2.2/macd.php?symbol=" + stockname;
                                viewWeb.loadUrl(url);
                                bar2.setVisibility(View.GONE);
                                viewWeb.setVisibility(View.VISIBLE);
                                buttonflag = 2;
                                chan.setTextColor(Color.GRAY);
                                chan.setEnabled(false);
                            }
                        });
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    return;
                }
            });

        /*
        String st = getArguments().getString("stockt");
        String url = "http://hw9chart.us-east-2.elasticbeanstalk.com/?symbol="+ st +"&count=1";*/
            ParentActivity activity = (ParentActivity) getActivity();
            String stockname = activity.getMyData();
            String url = "http://hw9chart.us-east-2.elasticbeanstalk.com/?symbol="+ stockname +"&count=1";
            Log.d("stockname",url);

            final ImageButton star = (ImageButton)rootView.findViewById(R.id.star);
            if (MainActivity.tranname.equals(stockname)){
                star.setImageResource(R.drawable.filled);
            }else{
                star.setImageResource(R.drawable.empty);
            }


            requestQ = Volley.newRequestQueue(getContext());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                final String stockname = response.getString("symbolf");
                                String timestamp = response.getString("timestamp");
                                final String lastprice = response.getString("lastprice");
                                String change = response.getString("change");
                                String changeper = response.getString("changeper");
                                String open = response.getString("open");
                                String preclose = response.getString("preclose");
                                String range = response.getString("range");
                                String volume = response.getString("volume");
                                final String changeFinal = change + "(" + changeper + ")";
                                stocks.set(0, stockname);
                                stocks.set(1, lastprice);
                                stocks.set(2, changeFinal);
                                stocks.set(3, timestamp);
                                stocks.set(4, open);
                                stocks.set(5, preclose);
                                stocks.set(6, range);
                                stocks.set(7, volume);
                                if (stockname == "null"){
                                    fail.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                    spinner.setVisibility(View.GONE);
                                }else {
                                    spinner.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    //adapter.clear();
                                    //stocks.clear();
                                    //setListAdapter(adapter);
                                    stocksAdapter.notifyDataSetChanged();

                                    star.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (star.getDrawable().getCurrent().getConstantState().equals(ContextCompat.getDrawable(getContext(), R.drawable.empty).getConstantState())) {
                                                star.setImageResource(R.drawable.filled);
                                                s1 = stockname;
                                                s2 = lastprice;
                                                s3 = changeFinal;
                                                flag = 1;
                                            } else {
                                                star.setImageResource(R.drawable.empty);
                                                s1 = stockname;
                                                s2 = lastprice;
                                                s3 = changeFinal;
                                                flag = 2;
                                            }

                                        }
                                    });


                                }

                                Log.d("TAG", stocks.toString());

                            } catch (JSONException e) {
                                spinner.setVisibility(View.GONE);
                                fail.setVisibility(View.VISIBLE);

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", error.getMessage(), error);
                }
            });
            //add request to queue
            requestQ.add(jsonObjectRequest);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }



        return rootView;
    }
    class StocksAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.stocklayout, null);
            TextView textView_name = (TextView)view.findViewById(R.id.textView);
            TextView textView_data = (TextView)view.findViewById(R.id.textView2);
            ImageView imgup = (ImageView)view.findViewById(R.id.img1);

            textView_name.setText(names[i]);
            textView_data.setText(stocks.get(i));
            if(i==2) {
                Log.d("first", stocks.get(2).substring(0, 1));
                if (stocks.get(2).substring(0, 1).equals("-")) {
                    imgup.setImageResource(R.drawable.down);
                } else {
                    imgup.setImageResource(R.drawable.up);
                }
            }
            return view;
        }
    }
}