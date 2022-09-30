package james.cocola.app.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cocola.app.R;
import james.cocola.app.TabLayoutHelper.MyAdapter;

import com.cocola.app.databinding.FragmentNotificationsBinding;
import com.google.android.material.tabs.TabLayout;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        tabLayout=(TabLayout)root.findViewById(R.id.tabLayout);
        viewPager=(ViewPager)root.findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Total"));
        tabLayout.addTab(tabLayout.newTab().setText("Solved"));
        tabLayout.addTab(tabLayout.newTab().setText("In progress"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

//        tabLayout.getTabAt(0).select();
        Log.e("onHistory","history");

        final MyAdapter adapter = new MyAdapter(getContext(),getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return root;
    }

}