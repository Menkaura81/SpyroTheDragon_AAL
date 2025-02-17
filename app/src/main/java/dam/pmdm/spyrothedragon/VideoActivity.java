package dam.pmdm.spyrothedragon;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // La actividad ocupa toda la pantalla
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Cargar layout
        setContentView(R.layout.activity_video);
        // Reproducir en apaisado
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // Evita que la pantalla se apague mientras se reproduce el video
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        videoView = findViewById(R.id.videoView);

        // Ruta del video
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video; // Cambia 'video_name' al nombre de tu video
        videoView.setVideoPath(videoPath);

        // Configura el MediaController
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);


        videoView.start();

        // Listener para saber cuándo termina el video
        videoView.setOnCompletionListener(mp -> {

            NavController navController = Navigation.findNavController(VideoActivity.this, R.id.navHostFragment);
            // Navegar hacia el fragmento "Coleccionables"
            navController.navigate(R.id.navigation_collectibles);
            finish();
        });
    }
}
