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

        // Hacer que la actividad ocupe toda la pantalla
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video); // Asegúrate de tener un layout para esta actividad

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // Evita que la pantalla se apague mientras se reproduce el video
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        // Inicializa el VideoView
        videoView = findViewById(R.id.videoView);

        // Ruta del video
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video; // Cambia 'video_name' al nombre de tu video
        videoView.setVideoPath(videoPath);

        // Configura el MediaController
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Inicia la reproducción
        videoView.start();

        // Listener para saber cuándo termina el video
        videoView.setOnCompletionListener(mp -> {
            // Obtener el NavController asociado a la actividad
            NavController navController = Navigation.findNavController(VideoActivity.this, R.id.navHostFragment);

            // Navegar hacia el fragmento "Coleccionables"
            navController.navigate(R.id.navigation_collectibles);
            finish(); // Cierra la actividad de video
        });
    }
}
