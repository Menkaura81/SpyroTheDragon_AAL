package dam.pmdm.spyrothedragon.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.VideoActivity;
import dam.pmdm.spyrothedragon.models.Collectible;

public class CollectiblesAdapter extends RecyclerView.Adapter<CollectiblesAdapter.CollectiblesViewHolder> {

    private List<Collectible> list;
    private Context context;
    private int clickCount = 0;
    private Handler handler = new Handler();
    private Runnable resetClickCountRunnable;

    public CollectiblesAdapter(List<Collectible> collectibleList) {

        this.list = collectibleList;
        this.context = context;

        // Runnable para resetear el contador de clics si no se llega a 4 en un periodo determinado
        resetClickCountRunnable = new Runnable() {
            @Override
            public void run() {
                clickCount = 0;
            }
        };
    }

    @Override
    public CollectiblesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CollectiblesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectiblesViewHolder holder, int position) {
        Collectible collectible = list.get(position);
        holder.nameTextView.setText(collectible.getName());

        // Cargar la imagen (simulado con un recurso drawable)
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(collectible.getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);

        // Verificar si el item tiene el nombre "Gemas"
        if (collectible.getName().equals("Gemas")) {
            holder.itemView.setOnClickListener(view -> {
                clickCount++;

                // Si el número de clics alcanza 4
                if (clickCount == 4) {
                    Intent intent = new Intent(holder.itemView.getContext(), VideoActivity.class);
                    holder.itemView.getContext().startActivity(intent);

                    // Resetear el contador
                    clickCount = 0;
                }

                // Resetear el contador
                handler.removeCallbacks(resetClickCountRunnable);
                handler.postDelayed(resetClickCountRunnable, 1000); // Resetear después de 1 segundo
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CollectiblesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CollectiblesViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }
}
