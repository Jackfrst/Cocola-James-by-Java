package james.cocola.app.NotificationRecyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cocola.app.R;

import org.jetbrains.annotations.NotNull;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    public TextView notiTitle , notiDesc , notiDate ;
    public NotificationViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        notiTitle = itemView.findViewById(R.id.notification_title_child);
        notiDesc = itemView.findViewById(R.id.notification_desc_child);
        notiDate = itemView.findViewById(R.id.notification_date_child);
    }
}
