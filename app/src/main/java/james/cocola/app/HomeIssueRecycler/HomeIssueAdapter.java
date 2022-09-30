package james.cocola.app.HomeIssueRecycler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import james.cocola.app.IssueFormActivity;
import com.cocola.app.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeIssueAdapter extends RecyclerView.Adapter<HomeIssueViewHolder> {
    private Context context ;
    private List<HomeIssueItems> homeIssueItems ;

    public HomeIssueAdapter(Context context, List<HomeIssueItems> homeIssueItems) {
        this.context = context;
        this.homeIssueItems = homeIssueItems;
    }

    @NonNull
    @NotNull
    @Override
    public HomeIssueViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.issue_child_layout,parent,false);
        HomeIssueViewHolder homeIssueViewHolder = new HomeIssueViewHolder(v);
        return homeIssueViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeIssueViewHolder holder, int position) {
        HomeIssueItems issueItems = homeIssueItems.get(position);
        Picasso.get().load(issueItems.getIssueImage()).into(holder.issueImage);
        holder.issueName.setText(issueItems.getIssueName());


        holder.issueLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, IssueFormActivity.class);
                i.putExtra("codeKey",issueItems.getIssueCode());
                i.putExtra("nameKey",issueItems.getIssueName());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeIssueItems.size();
    }
}
