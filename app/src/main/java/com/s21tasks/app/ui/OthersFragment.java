package com.s21tasks.app.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.s21tasks.app.AppClass.LoginPreferences;
import com.s21tasks.app.BuildConfig;
import com.s21tasks.app.LoginActivity;
import com.s21tasks.app.ProfileActivity;
import com.s21tasks.app.R;
import com.s21tasks.app.SettingActivity;
import com.rabbil.toastsiliconlibrary.ToastSilicon;

public class OthersFragment extends Fragment {
    private LinearLayout logoutBtn , profileBtn ,settingBtn;
    private LoginPreferences login ;
    private TextView name , department ;
    private TextView versionName ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_other, container, false);
        login = new LoginPreferences(getContext());
        logoutBtn = root.findViewById(R.id.other_logout_btn);
        profileBtn = root.findViewById(R.id.other_profile_btn);
        name = root.findViewById(R.id.others_name);
        department = root.findViewById(R.id.other_department);
        settingBtn = root.findViewById(R.id.others_setting_btn);
        versionName = root.findViewById(R.id.others_version_name);
        versionName.setText("Version : "+BuildConfig.VERSION_CODE+"-"+BuildConfig.VERSION_NAME);

        name.setText(login.onNameGet());
        department.setText("Department : "+login.onDepartmentGet());


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutDialog();
            }
        });
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                startActivity(i);
            }
        });
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SettingActivity.class);
                startActivity(i);
            }
        });

        return root;

    }
    private void LogoutDialog(){
        new AlertDialog.Builder(getContext())
                .setIcon(getResources().getDrawable(R.drawable.ic_baseline_warning_24))
                .setTitle("Logout Alart")
                .setMessage("Are You Sure You Want To LogOut ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        login.onLoginSet("null","null","null");
                        ToastSilicon.toastPrimaryTwo(getContext(),"Logout Successful", Toast.LENGTH_SHORT);
                        Intent i = new Intent(getContext(), LoginActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
