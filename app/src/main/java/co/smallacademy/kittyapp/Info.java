package co.smallacademy.kittyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class Info extends AppCompatActivity {
    ImageView catImage;
    TextView catName,catOrigin,catDesc,catTemp;
    Button wikiInfo,moreInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent data = getIntent();

        catImage = findViewById(R.id.catImage);
        catName = findViewById(R.id.catName);
        catOrigin = findViewById(R.id.catOrigin);
        catDesc = findViewById(R.id.catDescription);
        catTemp = findViewById(R.id.catTemperament);

        wikiInfo = findViewById(R.id.wikiBtn);
        moreInfo = findViewById(R.id.moreInfoBtn);

        // set the data to ui

        catName.setText(data.getStringExtra("name"));
        catOrigin.setText(data.getStringExtra("origin"));
        catDesc.setText(data.getStringExtra("desc"));
        catTemp.setText(data.getStringExtra("temp"));

        Picasso.get().load(data.getStringExtra("imageUrl")).into(catImage);

        wikiInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri catUri = Uri.parse(data.getStringExtra("wikiUrl"));
                Intent browser = new Intent(Intent.ACTION_VIEW,catUri);
                startActivity(browser);
            }
        });

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri catUri = Uri.parse(data.getStringExtra("moreLink"));
                Intent browser = new Intent(Intent.ACTION_VIEW,catUri);
                startActivity(browser);
            }
        });

    }
}