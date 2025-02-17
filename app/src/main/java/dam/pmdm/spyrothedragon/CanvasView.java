package dam.pmdm.spyrothedragon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;


public class CanvasView extends View {

    public CanvasView(Context context) {
        super(context);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Cargar la imagen desde @drawable
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.spyro);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.flame);

        if (bitmap != null) {
            // Escalar la imagen a un 50% del tamaño original
            int newWidth = bitmap.getWidth() / 2;
            int newHeight = bitmap.getHeight() / 2;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

            // Dibujar la imagen escalada en el centro de la pantalla
            float x = (getWidth() - newWidth) / 2f;
            float y = (getHeight() - newHeight) / 2f;
            canvas.drawBitmap(scaledBitmap, x, y, null);
        }

        if (bitmap2 != null) {
            Log.d("CanvasView", "Dibujando imagen");
            // Escalar la imagen a un 50% del tamaño original
            int newWidth = bitmap2.getWidth() / 4;
            int newHeight = bitmap2.getHeight() / 4;
            Bitmap scaledBitmap2 = Bitmap.createScaledBitmap(bitmap2, newWidth, newHeight, true);

            // Dibujar la imagen escalada en el centro de la pantalla
            float x = (getWidth() - newWidth) / 2f;
            float y = (getHeight() - newHeight) / 2f + 650;
            canvas.drawBitmap(scaledBitmap2, x, y, null);
        }

        // Crear un objeto Paint para el texto
        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);
        paint.setTextSize(60);

        // Indicaciones para volver
        canvas.drawText("Toca la pantalla para volver", 100, 100, paint);
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