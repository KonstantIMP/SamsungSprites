package kimp.ksprites.org;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.material.slider.Slider;

public class MainActivity extends AppCompatActivity {
    private SpritesSurfaceView spritesSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Optimize activity view */
        getSupportActionBar().hide();

        /** Add sprites renderer */
        LinearLayout content_layout = (LinearLayout) findViewById(R.id.main_layout);
        spritesSurfaceView = new SpritesSurfaceView(this);
        content_layout.addView(spritesSurfaceView);

        Slider slider = (Slider)findViewById(R.id.speed_bar);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                spritesSurfaceView.getSprite().setSpeedMultiply(value);
            }
        });
        slider.setValue(0.5f);

        Spinner spinner = (Spinner) findViewById(R.id.hero_cb);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: spritesSurfaceView.changeTexture(R.drawable.cat_sprites); break;
                    case 1: spritesSurfaceView.changeTexture(R.drawable.bat_sprites); break;
                    case 2: spritesSurfaceView.changeTexture(R.drawable.dodo_sprites); break;
                    default: spritesSurfaceView.changeTexture(R.drawable.chicken_sprites);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }
}