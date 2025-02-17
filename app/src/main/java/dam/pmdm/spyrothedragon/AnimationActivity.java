package dam.pmdm.spyrothedragon;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import dam.pmdm.spyrothedragon.databinding.ActivityAnimationBinding;

public class AnimationActivity extends AppCompatActivity {


    private ActivityAnimationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAnimationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}



