package james.cocola.app.HomeIssueRecycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cocola.app.R;

import org.jetbrains.annotations.NotNull;

public class HomeIssueViewHolder extends RecyclerView.ViewHolder {
    public TextView issueName ;
    public ImageView issueImage ;
    public LinearLayout issueLayout ;
    public HomeIssueViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        issueName = itemView.findViewById(R.id.issue_child_text);
        issueImage = itemView.findViewById(R.id.issue_child_image);
        issueLayout = itemView.findViewById(R.id.issue_child_layout);


    }
}
