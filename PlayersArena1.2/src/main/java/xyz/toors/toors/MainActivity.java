package xyz.toors.toors;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    SliderView sliderView;
    List<ImageSliderModel>imageSliderModelList;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageSliderModelList = new ArrayList<>();
        sliderView = findViewById(R.id.imageSlider);
        imageSliderModelList.add(new ImageSliderModel(R.drawable.sl1));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.sl2));
        imageSliderModelList.add(new ImageSliderModel(R.drawable.sl3));
        sliderView.setSliderAdapter(new ImageSliderAdapter( this,imageSliderModelList));


    }
    public void signup(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
    public void lgin(View view){
        Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent1);

        }
    }

