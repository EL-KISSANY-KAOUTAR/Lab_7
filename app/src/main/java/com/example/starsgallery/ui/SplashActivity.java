package com.example.starsgallery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.starsgallery.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logooo);
        TextView title = findViewById(R.id.splash_title);
        TextView subtitle = findViewById(R.id.splash_subtitle);

        // Animation du Logo : Zoom + Rotation + Brillance
        logo.setScaleX(0f);
        logo.setScaleY(0f);
        logo.setAlpha(0f);

        logo.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .alpha(1f)
                .rotation(360f)
                .setDuration(1200)
                .withEndAction(() -> {
                    logo.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(300)
                            .start();
                    
                    // Apparition du titre
                    title.animate().alpha(1f).translationY(-20f).setDuration(800).start();
                    
                    // Apparition du sous-titre
                    subtitle.animate().alpha(1f).setDuration(1000).setStartDelay(400).start();
                })
                .start();

        // Redirection après 3.5 secondes
        logo.postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, ListActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 3500);
    }
}
