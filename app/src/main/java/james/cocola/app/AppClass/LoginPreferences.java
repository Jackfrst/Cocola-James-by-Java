package james.cocola.app.AppClass;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginPreferences {
    private Context CONTEXT;

    public LoginPreferences(Context context) {
        this.CONTEXT = context;
    }
    public void onLoginSet(String number , String name , String department ){
        SharedPreferences sharedPreferences=CONTEXT.getSharedPreferences("loginData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("loginCode",number);
        editor.putString("nameKey",name);
        editor.putString("deptKey",department);
        editor.commit();
    }
    public String onLoginIdGet(){
        SharedPreferences sharedPreferences=CONTEXT.getSharedPreferences("loginData", Context.MODE_PRIVATE);
        String loginCode=sharedPreferences.getString("loginCode","null");
        return loginCode ;
    }
    public String onNameGet(){
        SharedPreferences sharedPreferences=CONTEXT.getSharedPreferences("loginData", Context.MODE_PRIVATE);
        String loginCode=sharedPreferences.getString("nameKey","null");
        return loginCode ;
    }
    public String onDepartmentGet(){
        SharedPreferences sharedPreferences=CONTEXT.getSharedPreferences("loginData", Context.MODE_PRIVATE);
        String loginCode=sharedPreferences.getString("deptKey","null");
        return loginCode ;
    }
}
