package dam.pmdm.spyrothedragon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.animation.ValueAnimator;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class CanvasView extends View {

    private float animatedAlpha = 0f; // Opacidad inicial (transparente)
    private boolean soundPlayed = false;
    private Bitmap spyroBitmap;
    private Bitmap flameBitmap;
    private SoundPool soundPool; // Crear una instancia de SoundPool

    public CanvasView(Context context) {
        super(context);
        init(); // Inicializar imágenes y animación
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(); // Inicializar imágenes y animación
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(); // Inicializar imágenes y animación
    }

    private void init() {
        // Inicializar SoundPool solo una vez
        soundPool = new SoundPool.Builder().setMaxStreams(1).build();

        // Cargar las imágenes solo una vez
        spyroBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.spyro);
        flameBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flame);

        startAnimation(); // Iniciar la animación al crear la vista
    }

    private void startSound() {
        // Reproducir sonido inicio Guia solo una vez
        if (!soundPlayed) {
            int soundId = soundPool.load(getContext(), R.raw.fire, 1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    if (status == 0) {
                        soundPool.play(sampleId, 1.0f, 1.0f, 0, 0, 1.0f);
                        soundPlayed = true; // Marcar como reproducido
                    }
                }
            });
        }
    }

    // Método para iniciar la animación de fade-in
    private void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f); // De 0 (transparente) a 1 (totalmente visible)
        animator.setDuration(1500); // Duración de la animación en milisegundos
        animator.setInterpolator(new DecelerateInterpolator()); // Suavizar el inicio de la animación
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedAlpha = (float) animation.getAnimatedValue(); // Actualiza la opacidad
                invalidate(); // Redibuja la vista para aplicar el cambio de opacidad
            }
        });
        animator.start(); // Inicia la animación

        startSound(); // Reproducir sonido solo una vez
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Dibujar la imagen de Spyro
        if (spyroBitmap != null) {
            int newWidth = spyroBitmap.getWidth() / 2;
            int newHeight = spyroBitmap.getHeight() / 2;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(spyroBitmap, newWidth, newHeight, true);

            float x = (getWidth() - newWidth) / 2f;
            float y = (getHeight() - newHeight) / 2f;
            canvas.drawBitmap(scaledBitmap, x, y, null);
        }

        // Dibujar la imagen de la llama con efecto fade-in
        if (flameBitmap != null) {
            int newWidth = flameBitmap.getWidth() / 4;
            int newHeight = flameBitmap.getHeight() / 4;
            Bitmap scaledBitmap2 = Bitmap.createScaledBitmap(flameBitmap, newWidth, newHeight, true);

            // Crear un objeto Paint para controlar la opacidad
            Paint paint = new Paint();
            paint.setAlpha((int) (animatedAlpha * 255)); // Aplicar la opacidad animada

            // Dibujar la imagen escalada con la opacidad animada
            float x = (getWidth() - newWidth) / 2f;
            float y = (getHeight() - newHeight) / 2f + 650; // Posicionar la llama un poco más abajo
            canvas.drawBitmap(scaledBitmap2, x, y, paint);
        }

        // Crear un objeto Paint para el texto
        Paint paintText = new Paint();
        paintText.setColor(Color.MAGENTA);
        paintText.setTextSize(60);

        // Indicaciones para volver
        canvas.drawText("Toca la pantalla para volver", 100, 100, paintText);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Crear un Intent para volver a MainActivity
            Context context = getContext();
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            return true;
        }
        return super.onTouchEvent(event);
    }
}
