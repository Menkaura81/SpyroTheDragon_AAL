package dam.pmdm.spyrothedragon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding;
import dam.pmdm.spyrothedragon.databinding.GuideBinding;
import dam.pmdm.spyrothedragon.databinding.GuideColeccionablesBinding;
import dam.pmdm.spyrothedragon.databinding.GuideFinalBinding;
import dam.pmdm.spyrothedragon.databinding.GuideInformacionBinding;
import dam.pmdm.spyrothedragon.databinding.GuideMundosBinding;
import dam.pmdm.spyrothedragon.databinding.GuidePersonajesBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private GuideBinding guideBinding;
    private GuidePersonajesBinding personajesBinding;
    private GuideMundosBinding mundosBinding;
    private GuideColeccionablesBinding coleccionablesBinding;
    private GuideInformacionBinding informacionBinding;
    private GuideFinalBinding finalBinding;
    NavController navController = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        guideBinding = binding.includeLayout;
        personajesBinding = binding.includeLayoutPersonajes;
        mundosBinding = binding.includeLayoutMundos;
        coleccionablesBinding = binding.includeLayoutColeccionables;
        informacionBinding = binding.includeLayoutInformacion;
        finalBinding = binding.includeLayoutFinal;
        setContentView(binding.getRoot());

        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.navView, navController);
            NavigationUI.setupActionBarWithNavController(this, navController);
        }

        binding.navView.setOnItemSelectedListener(this::selectedBottomMenu);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_characters ||
                    destination.getId() == R.id.navigation_worlds ||
                    destination.getId() == R.id.navigation_collectibles) {
                // Para las pantallas de los tabs, no queremos que aparezca la flecha de atrás
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            else {
                // Si se navega a una pantalla donde se desea mostrar la flecha de atrás, habilítala
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });

        initializeGuide();


    }

    private void initializeGuide() {
        // Comprobamos si la guía ya ha sido mostrada anteriormente
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean guideShown = sharedPreferences.getBoolean("guide_shown", false);


        guideBinding.botonComenzar.setOnClickListener(this::comenzarGuia);
        if (!guideShown){
            guideBinding.guideLayout.setVisibility(View.VISIBLE);

            // Reproducir sonido inicio Guia
            SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
            int soundId = soundPool.load(this, R.raw.up, 1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    if (status == 0) {
                        soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
                    }
                }
            });
        }
    }


    private void comenzarGuia(View view) {
        personajesBinding.botonSaltar.setOnClickListener(this::saltarGuide);
        personajesBinding.botonSiguiente.setOnClickListener(this::mundosGuide);

        // Reproducir sonido cambio de pantalla
        SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        int soundId = soundPool.load(this, R.raw.cambio, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
        });

        // Colocar circulo en el centro de la pestaña correspondiente
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        ImageView circulo = findViewById(R.id.circulo);
        circulo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                circulo.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int tabWidth = screenWidth / 3;
                int centerX = tabWidth/2 ;

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) circulo.getLayoutParams();
                params.setMarginStart(centerX - (circulo.getWidth() / 2));
                circulo.setLayoutParams(params);
            }
        });

        // Animación de salida deslizándose hacia la izquierda
        ObjectAnimator slideOut = ObjectAnimator.ofFloat(guideBinding.guideLayout, "translationX", 0f, -guideBinding.guideLayout.getWidth());
        slideOut.setDuration(500);
        // Animación de entrada con fade-in
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(personajesBinding.bocadilloPersonajes, "alpha", 0f, 1f);
        fadeIn.setDuration(500);
        // Asegurar el cambio de layout antes de la animacion
        slideOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                guideBinding.guideLayout.setVisibility(View.GONE);
                personajesBinding.guideLayoutPersonajes.setVisibility(View.VISIBLE);
            }
        });
        // Configurar y ejecutar las animaciones en secuencia
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(slideOut, fadeIn);
        animatorSet.start();
    }

    private void mundosGuide(View view){
        navController.navigate(R.id.navigation_worlds);
        mundosBinding.botonSaltar.setOnClickListener(this::saltarGuide);
        mundosBinding.botonSiguiente.setOnClickListener(this::coleccionablesGuide);

        // Reproducir sonido cambio de pantalla
        SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        int soundId = soundPool.load(this, R.raw.cambio, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
        });


        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(personajesBinding.bocadilloPersonajes, "alpha", 1f, 0f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(mundosBinding.bocadilloMundos, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(fadeOut, fadeIn);
        animatorSet.start();

       fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                personajesBinding.guideLayoutPersonajes.setVisibility(View.GONE);
                mundosBinding.guideLayoutMundos.setVisibility(View.VISIBLE);
            }
        });
    }

    private void coleccionablesGuide(View view) {
        navController.navigate(R.id.navigation_collectibles);
        coleccionablesBinding.botonSaltar.setOnClickListener(this::saltarGuide);
        coleccionablesBinding.botonSiguiente.setOnClickListener(this::informacionGuide);

        // Reproducir sonido cambio de pantalla
        SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        int soundId = soundPool.load(this, R.raw.cambio, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
        });

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        ImageView circulo = findViewById(R.id.circulo);
        circulo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                circulo.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int tabWidth = screenWidth / 3;
                int rightX = (tabWidth*2) + (tabWidth/2) ;
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) circulo.getLayoutParams();
                params.setMarginStart(rightX - (circulo.getWidth() / 2));
                circulo.setLayoutParams(params);
            }
        });

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(mundosBinding.bocadilloMundos, "alpha", 1f, 0f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(coleccionablesBinding.bocadilloColeccionables, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(fadeOut, fadeIn);
        animatorSet.start();

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mundosBinding.guideLayoutMundos.setVisibility(View.GONE);
                coleccionablesBinding.guideLayoutColeccionables.setVisibility(View.VISIBLE);
            }
        });


    }

    private void informacionGuide(View view) {
        informacionBinding.botonSaltar.setOnClickListener(this::saltarGuide);
        informacionBinding.botonSiguiente.setOnClickListener(this::lastGuide);

        // Reproducir sonido cambio de pantalla
        SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        int soundId = soundPool.load(this, R.raw.cambio, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
        });

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(coleccionablesBinding.bocadilloColeccionables, "alpha", 1f, 0f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(informacionBinding.bocadilloInformacion, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(fadeOut, fadeIn);
        animatorSet.start();

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                coleccionablesBinding.guideLayoutColeccionables.setVisibility(View.GONE);
                informacionBinding.guideLayoutInformacion.setVisibility(View.VISIBLE);
            }
        });

    }

    private void lastGuide(View view) {
        navController.navigate(R.id.navigation_characters);
        finalBinding.botonTerminar.setOnClickListener(this::saltarGuide);

        // Reproducir sonido cambio de pantalla
        SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        int soundId = soundPool.load(this, R.raw.cambio, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
        });

        // Mantener guideLayoutFinal invisible inicialmente
        finalBinding.guideLayoutFinal.setVisibility(View.INVISIBLE);

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(informacionBinding.bocadilloInformacion, "alpha", 1f, 0f);
        fadeOut.setDuration(500);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                informacionBinding.guideLayoutInformacion.setVisibility(View.GONE);

                // Usamos post() para asegurarnos de que getWidth() tiene un valor correcto
                finalBinding.guideLayoutFinal.post(() -> {
                    // Colocar guideLayoutFinal fuera de la pantalla antes de hacerlo visible
                    finalBinding.guideLayoutFinal.setTranslationX(-finalBinding.guideLayoutFinal.getWidth());
                    finalBinding.guideLayoutFinal.setVisibility(View.VISIBLE);

                    // Animación de entrada deslizándose desde la izquierda
                    ObjectAnimator slideIn = ObjectAnimator.ofFloat(finalBinding.guideLayoutFinal, "translationX", 0f);
                    slideIn.setDuration(500);
                    slideIn.start();
                });
            }
        });
        // Ejecutar la animación de fadeOut
        fadeOut.start();
    }

    private void saltarGuide(View view) {
        personajesBinding.guideLayoutPersonajes.setVisibility(View.GONE);
        mundosBinding.guideLayoutMundos.setVisibility(View.GONE);
        coleccionablesBinding.guideLayoutColeccionables.setVisibility(View.GONE);
        informacionBinding.guideLayoutInformacion.setVisibility(View.GONE);
        finalBinding.guideLayoutFinal.setVisibility(View.GONE);
        // Reproducir sonido final de guia
        SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        int soundId = soundPool.load(this, R.raw.finalizar, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
        });

        // Actualizamos el valor en SharedPreferences para indicar que la guía ha sido completada
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("guide_shown", true);  // Marcamos que la guía ya se mostró
        editor.apply();
    }

    private boolean selectedBottomMenu(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_characters)
            navController.navigate(R.id.navigation_characters);
        else
        if (menuItem.getItemId() == R.id.nav_worlds)
            navController.navigate(R.id.navigation_worlds);
        else
            navController.navigate(R.id.navigation_collectibles);
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestiona el clic en el ítem de información
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();  // Muestra el diálogo
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        // Crear un diálogo de información
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_about)
                .setMessage(R.string.text_about)
                .setPositiveButton(R.string.accept, null)
                .show();
    }
}