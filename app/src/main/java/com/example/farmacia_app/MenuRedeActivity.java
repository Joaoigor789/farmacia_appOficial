package com.example.farmacia_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MenuRedeActivity extends AppCompatActivity {

    private static final String URL_APK = "https://www.dropbox.com/scl/fi/mcfd7c4f27azn48p1kh98/music_apptech.apk?rlkey=xn6k7b6o5kaueq2gyk2ojsxws&st=0ghog6bs&dl=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu_rede);

        ImageView imageDownload = findViewById(R.id.imageDownload);

        imageDownload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_APK));
            startActivity(intent);
        });
    }
}
