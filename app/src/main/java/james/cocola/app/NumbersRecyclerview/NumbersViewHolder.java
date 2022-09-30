package james.cocola.app.NumbersRecyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cocola.app.R;

import org.jetbrains.annotations.NotNull;

public class NumbersViewHolder extends RecyclerView.ViewHolder {
    public TextView name , number ;
    public ImageView callImg ;
    public NumbersViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.number_name_id);
        number = itemView.findViewById(R.id.phone_no_id);
        callImg = itemView.findViewById(R.id.number_next_btn);
    }
}
