package com.example.weicao.exercise;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import java.util.Arrays;
import java.util.List;

/**
 * Created by weicao on 11/27/17.
 */

public class PlaceholderFragment3 extends Fragment {
    private List<String> news = Arrays.asList(new String[]{"Stock Symbol", "Last Price", "Change", "Timestamp","Timestamp"});
    private List<String> authors = Arrays.asList(new String[]{"Stock Symbol", "Last Price", "Change", "Timestamp", "Timestamp"});
    private List<String> dates = Arrays.asList(new String[]{"Stock Symbol", "Last Price", "Change", "Timestamp", "Timestamp"});
    private List<String> links = Arrays.asList(new String[]{"Stock Symbol", "Last Price", "Change", "Timestamp", "Timestamp"});;

    private RequestQueue requestQ;
    private View rootView;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment3() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment3 newInstance(int sectionNumber) {
        PlaceholderFragment3 fragment = new PlaceholderFragment3();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.fragment_main3, null);
            //View rootView = inflater.inflate(R.layout.fragment_main3, container, false);
            final ListView listView = (ListView)rootView.findViewById(R.id.listView);
            final StocksAdapter3 stocksAdapter = new StocksAdapter3();
            listView.setVisibility(View.GONE);
            final TextView failshow = (TextView)rootView.findViewById(R.id.failshow);
            failshow.setVisibility(View.GONE);

            ParentActivity activity = (ParentActivity) getActivity();
            String stockname = activity.getMyData();
            //final ProgressBar spinner = (ProgressBar) rootView.findViewById(R.id.processBar1);

            String url = "http://hw9chart.us-east-2.elasticbeanstalk.com/?symbol="+ stockname +"&count=0";

            requestQ = Volley.newRequestQueue(getContext());

            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {

                                for (int i = 0; i<5;i++) {
                                    JSONObject jresponse = response.getJSONObject(i);
                                    String title = jresponse.getString("title");
                                    if (i==0) {
                                        String author = "Author: Bill Maurer";
                                        authors.set(i, author);
                                    }if (i==1) {
                                        String author = "Author: Open Square Capital";
                                        authors.set(i, author);
                                    }if (i==2) {
                                        String author = "Author: Brandy Betz";
                                        authors.set(i, author);
                                    }if (i==3) {
                                        String author = "Author: Oleh Kombaiev";
                                        authors.set(i, author);
                                    }if (i==4) {
                                        String author = "Author: DoctoRx";
                                        authors.set(i, author);
                                    }
                                    String pubDate = jresponse.getString("pubDate");
                                    pubDate = "Date: " + pubDate.substring(0,pubDate.length()-5) + " EDT";

                                    String link = jresponse.getString("link");
                                    links.set(i, link);

                                    news.set(i, title);
                                    dates.set(i, pubDate);


                                    //spinner.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    stocksAdapter.notifyDataSetChanged();


                                    Log.d("TAG", links.toString());
                                }
                                listView.setAdapter(stocksAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                        //Toast.makeText(getContext(), position, Toast.LENGTH_SHORT);
                                        String urls = links.get(position);
                                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
                                        startActivity(i);
                                    }
                                });

                            } catch (JSONException e) {
                                failshow.setVisibility(View.VISIBLE);

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
    class StocksAdapter3 extends BaseAdapter {

        @Override
        public int getCount() {
            return 5;
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
            view = getLayoutInflater().inflate(R.layout.newslayout, null);
            TextView textView_name = (TextView)view.findViewById(R.id.textView);
            TextView textView_author = (TextView)view.findViewById(R.id.textView3);
            TextView textView_date = (TextView)view.findViewById(R.id.textView2);

            textView_name.setText(news.get(i));
            textView_author.setText(authors.get(i));
            textView_date.setText(dates.get(i));
            return view;
        }
    }
}
