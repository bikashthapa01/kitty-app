package co.smallacademy.kittyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    FloatingActionButton infoBtn,downloadBtn,refreshBtn;
    ImageView catImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoBtn = findViewById(R.id.infoBtn);
        downloadBtn = findViewById(R.id.downloadBtn);
        refreshBtn = findViewById(R.id.refreshBtn);

        catImageView = findViewById(R.id.kittyImage);

        getImage(getResources().getString(R.string.api_url));

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(getResources().getString(R.string.api_url));
            }
        });
        

        


    }

    public void getImage(String url){
        // extract the json data

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ///Log.d(TAG, "onResponse: " + response.toString());
                try {

                    JSONObject kittyData = response.getJSONObject(0);
                    String catUrl = kittyData.getString("url");
                    Log.d(TAG, "onResponse: " + catUrl);
                    Picasso.get().load(catUrl).into(catImageView);

                    downloadBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri catUri = Uri.parse(catUrl);
                            Intent browser = new Intent(Intent.ACTION_VIEW,catUri);
                            startActivity(browser);
                        }
                    });

                    infoBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                JSONArray breedsInfo = kittyData.getJSONArray("breeds");
                                if(breedsInfo.isNull(0)){
                                    Toast.makeText(MainActivity.this, "Data Not Found.", Toast.LENGTH_SHORT).show();
                                }else {
                                    JSONObject breedsData = breedsInfo.getJSONObject(0);
                                    Intent i = new Intent(getApplicationContext(),Info.class);
                                    i.putExtra("name",breedsData.getString("name"));
                                    i.putExtra("origin",breedsData.getString("origin"));
                                    i.putExtra("desc",breedsData.getString("description"));
                                    i.putExtra("temp",breedsData.getString("temperament"));
                                    i.putExtra("wikiUrl",breedsData.getString("wikipedia_url"));
                                    i.putExtra("moreLink",breedsData.getString("vcahospitals_url"));
                                    i.putExtra("imageUrl",catUrl);
                                    startActivity(i);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(arrayRequest);

    }
}