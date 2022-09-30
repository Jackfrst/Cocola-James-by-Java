package james.cocola.app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import james.cocola.app.AppClass.AppUrl;
import james.cocola.app.AppClass.DateTimeConversion;
import james.cocola.app.AppClass.ProgressDialogIndicatorColorChange;
import james.cocola.app.FollowUpRecycler.FollowUpAdapter;
import james.cocola.app.FollowUpRecycler.FollowUpItems;

import com.cocola.app.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.ortiz.touchview.TouchImageView;
import com.rabbil.toastsiliconlibrary.ToastSilicon;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class HistoryDetailsActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int CAMERA_REQUEST = 102;
    private static final int GALLERY_REQUEST_CODE = 111;
    //progress dialog components
    private Dialog statusDialog ;
    private TextView dialogInvoiceIdView , receiverName , receiverDepartment ;
    private TextView orderSendStatus , orderReceivedStatus , readyToPickupStatus , deliveredStatus ;
    private TextView orderSendDate , orderReceiveDate , readyToPickupDate , deliveredDate ;
    private ImageView orderSendCircle , orderReceivedCircle , readyToPickupCircle , deliveredCircle ;
    private LinearLayout orderSendLine , orderReceivedLine , readyToPickupLine , deliveredLine ;

    private RelativeLayout noDataLayout , noInternetLayout , progressLayout ;
    private ImageView detailsImage ;

    //general components
    private TextView issueNo , issueStatus , issueLocation , issueDate , issueTime , issueTitle , issueDesc ;
    private String getIssueNo , getStatue , getLocation , getDate , getTime , getTitle , getDesc ,getImg ;

    //general data
    private TextView categoryView , issueView , locationView , dateView , titleView , detailsView , statusView , acceptedView , planView , assignedView , requiredView , solved_atView , solvingStatView  ;


    //feedback dialog
    //dialog components
    private TextView dialogTitle ;
    private Dialog feedbackDialog;
    private Spinner solvingSpinner ;
    private Spinner qualitySpinner ;
    private String selectedQuality = "null";
    private EditText userComment ;
    private ImageView feedbackImage ;
    private Button feedbackSubmitBtn ;
    private String getSolvedType ;
    private String getSolvedCode = "1" ;
    //tack btn
    private ImageButton trackBtn ;
    private LinearLayout captureBtn ;
    private ImageView showImage ;
    Bitmap imageBitmap = null;
    File imagefile ;
    private boolean isPhotoSelected = false ;
    private String serviceQualityValue = "Not Found";

    //zoom
    private RelativeLayout zoomLayout ;
    private TouchImageView zoomImage ;
    private ImageButton zoomBtn ;
    private Button crossBtn ;
    private boolean isZoomLayoutOn = false ;

    //chat and confirm
    private ImageButton confirmBtn ;
    private ImageButton commentBtn ;
    private ImageButton followUpBtn ;

    //comment dialog
    private Dialog commentDialog ;
    private TextView commentDialogTitle ;
    private ImageView commentImage;
    private TextView commentStatus , commentDate , commentDetails;
    private TextView serviceQualityView ;

    //followup dialog
    private Dialog followupDialog ;
    private TextView followUpDialogTitle ;
    private RecyclerView followUpRecyclerview ;
    private List<FollowUpItems> followUpItems ;
    private FollowUpAdapter followUpAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_history_details);


        //intent data get
        Intent i = getIntent();
        getIssueNo = i.getStringExtra("issueKey");
        getStatue = i.getStringExtra("statusKey");
        getLocation = i.getStringExtra("locatoinKey");
        getDate = i.getStringExtra("dateKey").split(" ")[0];
        getTime = i.getStringExtra("dateKey").split(" ")[1];
        getTitle = i.getStringExtra("titleKey");
        getDesc = i.getStringExtra("descKey");
        getImg = i.getStringExtra("imageKey");
        setTitle("Task Details");

        //finding
        categoryView = findViewById(R.id.details_category);
        issueView = findViewById(R.id.details_issue_no);
        locationView = findViewById(R.id.details_location);
        dateView = findViewById(R.id.details_date_time);
        titleView = findViewById(R.id.details_title);
        detailsView= findViewById(R.id.details_desc);
        statusView = findViewById(R.id.details_status);
        acceptedView = findViewById(R.id.details_accepted_at);
        planView = findViewById(R.id.details_plan_of_action);
        assignedView = findViewById(R.id.details_assigned_person);
        requiredView = findViewById(R.id.details_Required_time);
        solved_atView = findViewById(R.id.details_solved_at);
        solvingStatView = findViewById(R.id.details_solving_status);
        confirmBtn = findViewById(R.id.details_confirm_btn);
        commentBtn = findViewById(R.id.details_comment_btn);
        trackBtn = findViewById(R.id.details_track_btn);
        followUpBtn = findViewById(R.id.details_followup_btn);


        //finding zoom
        zoomLayout = findViewById(R.id.zoom_layout);
        zoomBtn = findViewById(R.id.zoom_btn);
        zoomImage = findViewById(R.id.zoom_image_view);
        crossBtn = findViewById(R.id.zoom_cross_btn);

        zoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().hide();
                zoomLayout.setVisibility(View.VISIBLE);
                isZoomLayoutOn = true ;
            }
        });
        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportActionBar().show();
                zoomLayout.setVisibility(View.GONE);
                isZoomLayoutOn = false;
            }
        });
        detailsImage = findViewById(R.id.details_image_id);
        Picasso.get().load(getImg).placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_image_24).into(detailsImage);
        Picasso.get().load(getImg).placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_image_24).into(zoomImage);





        // status dialog setup
        statusDialog = new Dialog(this);
        statusDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        statusDialog.setCancelable(true);
        statusDialog.setContentView(R.layout.order_progress_dialog);
        Window window = statusDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //finding dialog components
        dialogInvoiceIdView = statusDialog.findViewById(R.id.progress_dialog_inv_code_id);
        receiverName = statusDialog.findViewById(R.id.progress_dialog_receiver_name);
        receiverDepartment = statusDialog.findViewById(R.id.progress_dialog_receiver_department);
        //line finding
        orderSendLine = statusDialog.findViewById(R.id.send_order_line);
        orderReceivedLine = statusDialog.findViewById(R.id.order_recive_line);
        readyToPickupLine = statusDialog.findViewById(R.id.ready_to_pickup_line);
        //circle finding
        orderSendCircle = statusDialog.findViewById(R.id.order_send_circle);
        orderReceivedCircle = statusDialog.findViewById(R.id.order_recived_circle);
        readyToPickupCircle = statusDialog.findViewById(R.id.ready_to_pickup_circle);
        deliveredCircle = statusDialog.findViewById(R.id.delivered_circle);
        //status text finding
        orderSendStatus = statusDialog.findViewById(R.id.order_send_text);
        orderReceivedStatus = statusDialog.findViewById(R.id.order_receive_text);
        readyToPickupStatus = statusDialog.findViewById(R.id.ready_to_pickup_text);
        deliveredStatus = statusDialog.findViewById(R.id.delivered_text);
        orderReceivedStatus.setText("ACCEPTED & PROCESSING");
        //date finding
        orderSendDate = statusDialog.findViewById(R.id.order_send_date_text);
        orderReceiveDate = statusDialog.findViewById(R.id.order_received_date_text);
        readyToPickupDate = statusDialog.findViewById(R.id.ready_to_pickup_date_text);
        deliveredDate = statusDialog.findViewById(R.id.delivered_date_text);
        loadHistoryData(getIssueNo);



        //Feedback Dialog Setup
        feedbackDialog = new Dialog(this);
        feedbackDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        feedbackDialog.setCancelable(true);
        feedbackDialog.setContentView(R.layout.feedback_dialog);
        Window feedbackWindow = feedbackDialog.getWindow();
        feedbackWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialogTitle = feedbackDialog.findViewById(R.id.dialog_title_view);
        solvingSpinner = feedbackDialog.findViewById(R.id.feedback_dialog_solving_stat_spinner);
        userComment = feedbackDialog.findViewById(R.id.feedback_dialog_comment_edittext);
        feedbackImage = feedbackDialog.findViewById(R.id.feedback_dialog_image_id);
        feedbackSubmitBtn = feedbackDialog.findViewById(R.id.feedback_dialog_submit_btn);
        qualitySpinner = feedbackDialog.findViewById(R.id.feedback_dialog_service_quality_spinner);
        dialogTitle.setText("Please Give Feedback");


        //Solving spinner setup
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.spinner_solving_array)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_dropdown_item_1line);
        solvingSpinner.setAdapter(spinnerArrayAdapter);
        solvingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getSolvedType = adapterView.getItemAtPosition(i).toString().trim();
                if (getSolvedType.equals("Yes")){
                    getSolvedCode = "1";
                }else if(getSolvedType.equals("Partially Solved")){
                    getSolvedCode = "2";
                }else if(getSolvedType.equals("Not Solved")){
                    getSolvedCode = "3";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> qualityAdapter = new ArrayAdapter<String>
                (this, R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.spinner_quality)); //selected item will look like a spinner set from XML
        qualityAdapter.setDropDownViewResource(android.R.layout
                .simple_dropdown_item_1line);
        qualitySpinner.setAdapter(qualityAdapter);
        qualitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedQuality = adapterView.getItemAtPosition(i).toString().trim();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //comment dialog setup
        commentDialog = new Dialog(this);
        commentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        commentDialog.setCancelable(true);
        commentDialog.setContentView(R.layout.comment_dialog);
        Window commentWindow = commentDialog.getWindow();
        commentWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        commentDialogTitle = commentDialog.findViewById(R.id.dialog_title_view);
        commentImage = commentDialog.findViewById(R.id.comment_dialog_image);
        commentStatus = commentDialog.findViewById(R.id.comment_dialog_status);
        commentDate = commentDialog.findViewById(R.id.comment_dialog_date_time);
        commentDetails = commentDialog.findViewById(R.id.comment_dialog_feedback);
        serviceQualityView = commentDialog.findViewById(R.id.comment_service_quality);
        commentDialogTitle.setText("Users Feedback");
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentDialog.show();
            }
        });

        //followup Dialog Setup
        followupDialog = new Dialog(this);
        followupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        followupDialog.setCancelable(true);
        followupDialog.setContentView(R.layout.followup_list_dialog);
        Window followUpWindow = followupDialog.getWindow();
        followUpWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        followUpDialogTitle = followupDialog.findViewById(R.id.follow_up_dialog_title);
        followUpDialogTitle.setText("FollowUp List");
        followUpRecyclerview = followupDialog.findViewById(R.id.follow_up_dialog_recyclerview);
        followUpRecyclerview.setNestedScrollingEnabled(true);
        followUpRecyclerview.setHasFixedSize(false);
        followUpRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        followUpItems = new ArrayList<>();
        //onclick
        followUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFollowUpList();
            }
        });

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentDialog.show();
            }
        });

        //onclick
        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressData(getIssueNo);
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackDialog.show();
            }
        });

        feedbackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OpenMediaChooser();
                //MultiplePermissions();
                chooseMediaType();
            }
        });
        feedbackSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });



    }
    private void loadHistoryData(String getIssueParams){
        resetProgressBar();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        ProgressDialogIndicatorColorChange.setProgressColor(progressDialog,this);
        progressDialog.show();
        String urlString = AppUrl.getIssueDetailsurl();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                              JSONArray jsonArray = new JSONArray(response);

                            //data
                            JSONObject dataObj = jsonArray.getJSONObject(0);

                            categoryView.setText(HtmlCompat.fromHtml("<b>Category :</b> "+dataObj.getString("cat_name"),0));
                            issueView.setText(HtmlCompat.fromHtml("<b>Task No :</b> "+dataObj.getString("issue_no"),0));
                            locationView.setText(HtmlCompat.fromHtml("<b>Location :</b> "+dataObj.getString("location"),0));
                            dateView.setText(HtmlCompat.fromHtml("<b>Task Date :</b> "+ DateTimeConversion.convertDatetime(dataObj.getString("created_at").trim()),0));
                            titleView.setText(HtmlCompat.fromHtml("<b>Task Title :</b> "+dataObj.getString("issue_title"),0));
                            detailsView.setText(HtmlCompat.fromHtml("<b>Task Details :</b> "+dataObj.getString("issue_details"),0));

                            serviceQualityValue = dataObj.getString("service_quality");
                            serviceQualityView.setText(HtmlCompat.fromHtml("<b>Service Quality : </b>"+dataObj.getString("service_quality"),0));;
                            if (dataObj.getString("status").equals("0")){
                                statusView.setText(HtmlCompat.fromHtml("<b>Task Status :</b> Request Sent",0));
                            }else if(dataObj.getString("status").equals("1")){
                                statusView.setText(HtmlCompat.fromHtml("<b>Task Status :</b> Accepted & Processing",0));
                            }else if(dataObj.getString("status").equals("2")){
                                statusView.setText(HtmlCompat.fromHtml("<b>Task Status :</b> Rejected",0));
                            }else if(dataObj.getString("status").equals("3")){
                                statusView.setText(HtmlCompat.fromHtml("<b>Task Status :</b> Solved",0));
                            }else if(dataObj.getString("status").equals("4")){
                                statusView.setText(HtmlCompat.fromHtml("<b>Task Status :</b> User Confirmed",0));
                            }

                            if (!dataObj.getString("accepted_at").equals("null")){
                                if (dataObj.getString("status").equals("2")){
                                    acceptedView.setText(HtmlCompat.fromHtml("<b>Rejected At :</b> "+dataObj.getString("accepted_at"),0));

                                }else{
                                    acceptedView.setText(HtmlCompat.fromHtml("<b>Accepted At :</b> "+dataObj.getString("accepted_at"),0));

                                }
                            }
                            if(!dataObj.getString("plan_of_action").equals("null")){
                                planView.setText(HtmlCompat.fromHtml("<b>Plan Of Action :</b> "+dataObj.getString("plan_of_action"),0));
                            }
                            if(!dataObj.getString("assigned_person").equals("null")){
                                assignedView.setText(HtmlCompat.fromHtml("<b>Assigned Person :</b> "+dataObj.getString("assigned_person"),0));
                            }
                            if(!dataObj.getString("required_time").equals("null")){
                                requiredView.setText(HtmlCompat.fromHtml("<b>Required Time :</b> "+dataObj.getString("required_time"),0));
                            }
                            if(!dataObj.getString("solved_at").equals("null")){
                                solved_atView.setText(HtmlCompat.fromHtml("<b>Solved At :</b> "+dataObj.getString("solved_at"),0));
                            }


                            if(dataObj.getString("solving_status").equals("0")){
                                solvingStatView.setText(HtmlCompat.fromHtml("<b>Solving Status :</b> Not Confirmed By User",0));
                                commentStatus.setText(HtmlCompat.fromHtml("Not Confirmed By User",0));
//                                confirmBtn.setVisibility(View.VISIBLE);
//                                commentBtn.setVisibility(View.GONE);

                            }else if(!dataObj.getString("solving_status").equals("1")){
                                solvingStatView.setText(HtmlCompat.fromHtml("<b>Solving Status :</b> Confirmed & Solved",0));
                                commentStatus.setText(HtmlCompat.fromHtml("Confirmed & Solved",0));
//                                confirmBtn.setVisibility(View.GONE);
//                                commentBtn.setVisibility(View.VISIBLE);

                            }else if(!dataObj.getString("solving_status").equals("2")){
                                solvingStatView.setText(HtmlCompat.fromHtml("<b>Solving Status :</b> Confirmed & Partially Solved",0));
                                commentStatus.setText(HtmlCompat.fromHtml("Confirmed & Partially Solved",0));
//                                confirmBtn.setVisibility(View.GONE);
//                                commentBtn.setVisibility(View.VISIBLE);

                            }else if(!dataObj.getString("solving_status").equals("3")){
                                solvingStatView.setText(HtmlCompat.fromHtml("<b>Solving Status :</b> Confirmed & Not Solved",0));
                                commentStatus.setText(HtmlCompat.fromHtml("<b>Confirmed & Not Solved",0));
//                                confirmBtn.setVisibility(View.GONE);
//                                commentBtn.setVisibility(View.VISIBLE);

                            }

                            if (dataObj.getString("status").equals("3")&&dataObj.getString("solving_status").equals("0")){
                                confirmBtn.setVisibility(View.VISIBLE);
                            }else{
                                confirmBtn.setVisibility(View.GONE);
                            }

                            if (Integer.valueOf(dataObj.getString("status"))>=3&&Integer.valueOf(dataObj.getString("solving_status"))>0){
                                commentBtn.setVisibility(View.VISIBLE);
                            }else{
                                commentBtn.setVisibility(View.GONE);
                            }

                            //comment setup
                            commentDate.setText(HtmlCompat.fromHtml("<b>Feedback Date : </b>"+DateTimeConversion.convertDatetime(dataObj.getString("feedbacked_at").trim()),0));
                            commentDetails.setText(HtmlCompat.fromHtml("<b>User  Feedback : </b>"+dataObj.getString("user_comments"),0));
                            Picasso.get().load(dataObj.getString("user_feedback_photo_url")).placeholder(R.drawable.ic_baseline_image_24)
                                    .error(R.drawable.ic_baseline_image_24).into(commentImage);
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            ToastSilicon.toastDangerTwo(HistoryDetailsActivity.this,"Something Went Wrong !", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        ToastSilicon.toastDangerTwo(HistoryDetailsActivity.this,"Please Check Your Internet Connection !",Toast.LENGTH_SHORT);
                    }
                }
        ){
            protected Map<String , String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("issue_no",getIssueParams);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
    private void progressData(String getIssueParams){
        resetProgressBar();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        ProgressDialogIndicatorColorChange.setProgressColor(progressDialog,this);
        progressDialog.show();
        String urlString = AppUrl.getProgressDataUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("progressData",response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            //data
                            JSONObject dataObj = jsonArray.getJSONObject(0);
                            int statusIntValue = Integer.valueOf(dataObj.getString("status"));
                            dialogInvoiceIdView.setText("TASK NO : "+dataObj.getString("issue_no"));
                            switch (statusIntValue){
                                case 4:
                                    //delivered Operation
                                    readyToPickupLine.setBackgroundColor(getResources().getColor(R.color.completed_line_color));
                                    deliveredCircle.setImageResource(R.drawable.ic_round_check_circle_24);
                                    deliveredStatus.setTextColor(getResources().getColor(R.color.completed_line_color));
                                    deliveredDate.setTextColor(getResources().getColor(R.color.gray_text_color));
                                    //setting dats
                                    deliveredDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("feedbacked_at").trim()));
                                    readyToPickupDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("solved_at").trim()));
                                    orderReceiveDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("accepted_at").trim()));
                                    orderSendDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("created_at").trim()));
                                    //setting more info
//                                    receiverName.setText("Received By : "+deliveredObj.getString("receiver_name"));
//                                    receiverDepartment.setText(deliveredObj.getString("receiver_designation"));
                                case 3:
                                    if (dataObj.getString("solving_status").equals("0")){
                                        orderReceivedLine.setBackgroundColor(getResources().getColor(R.color.completed_line_color));
                                        readyToPickupCircle.setImageResource(R.drawable.ic_round_check_circle_24);
                                        readyToPickupStatus.setTextColor(getResources().getColor(R.color.completed_line_color));
                                        readyToPickupDate.setTextColor(getResources().getColor(R.color.gray_text_color));
//                                    //setting dats
                                        readyToPickupDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("solved_at").trim()));
                                        orderReceiveDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("accepted_at").trim()));
                                        orderSendDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("created_at").trim()));
                                    }else{
                                        orderReceivedLine.setBackgroundColor(getResources().getColor(R.color.completed_line_color));
                                        readyToPickupCircle.setImageResource(R.drawable.ic_round_check_circle_24);
                                        readyToPickupStatus.setTextColor(getResources().getColor(R.color.completed_line_color));
                                        readyToPickupDate.setTextColor(getResources().getColor(R.color.gray_text_color));
//                                    //setting dats
                                        readyToPickupDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("solved_at").trim()));
                                        orderReceiveDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("accepted_at").trim()));
                                        orderSendDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("created_at").trim()));
                                        //delivered Operation
                                        readyToPickupLine.setBackgroundColor(getResources().getColor(R.color.completed_line_color));
                                        deliveredCircle.setImageResource(R.drawable.ic_round_check_circle_24);
                                        deliveredStatus.setTextColor(getResources().getColor(R.color.completed_line_color));
                                        deliveredDate.setTextColor(getResources().getColor(R.color.gray_text_color));
                                        //setting dats
                                        deliveredDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("feedbacked_at").trim()));
                                        readyToPickupDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("solved_at").trim()));
                                        orderReceiveDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("accepted_at").trim()));
                                        orderSendDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("created_at").trim()));
                                    }

                                case 2:
                                    if (statusIntValue<3) {
                                        orderSendLine.setBackgroundColor(getResources().getColor(R.color.completed_line_color));
                                        orderReceivedCircle.setImageResource(R.drawable.ic_tick_red);
                                        orderReceivedStatus.setTextColor(getResources().getColor(R.color.red_text_color));
                                        orderReceivedStatus.setText("REJECTED");
                                        orderReceiveDate.setTextColor(getResources().getColor(R.color.gray_text_color));
                                        //off bottom
                                        orderSendStatus.setTextColor(getResources().getColor(R.color.completed_line_color));
                                        orderSendDate.setTextColor(getResources().getColor(R.color.gray_text_color));
                                        orderSendCircle.setImageResource(R.drawable.ic_round_check_circle_24);
                                        //setting dats
                                        orderReceiveDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("accepted_at").trim()));
                                        orderSendDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("created_at").trim()));
                                        break;
                                    }


                                case 1:
                                    orderSendLine.setBackgroundColor(getResources().getColor(R.color.completed_line_color));
                                    orderReceivedCircle.setImageResource(R.drawable.ic_round_check_circle_24);
                                    orderReceivedStatus.setTextColor(getResources().getColor(R.color.completed_line_color));
                                    orderReceivedStatus.setText("ACCEPTED & PROCESSING");
                                    orderReceiveDate.setTextColor(getResources().getColor(R.color.gray_text_color));
                                    //setting dats
                                    orderReceiveDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("accepted_at").trim()));
                                    orderSendDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("created_at").trim()));
                                default:
                                    //send Order Operation
                                    orderSendStatus.setTextColor(getResources().getColor(R.color.completed_line_color));
                                    orderSendDate.setTextColor(getResources().getColor(R.color.gray_text_color));
                                    orderSendCircle.setImageResource(R.drawable.ic_round_check_circle_24);
                                    //setting Value
                                    orderSendDate.setText(DateTimeConversion.convertDatetime(dataObj.getString("created_at").trim()));
                            }

                            statusDialog.show();
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Log.e("progressData",e.toString());
                            ToastSilicon.toastDangerTwo(HistoryDetailsActivity.this,"Something Went Wrong !", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        ToastSilicon.toastDangerTwo(HistoryDetailsActivity.this,"Please Check Your Internet Connection !",Toast.LENGTH_SHORT);
                    }
                }
        ){
            protected Map<String , String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("issue_no",getIssueParams);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
    private void resetProgressBar(){
        //send Order Operation
        orderSendStatus.setTextColor(getResources().getColor(R.color.uncompleted_line_color));
        orderSendDate.setTextColor(getResources().getColor(R.color.uncompleted_line_color));
        orderSendCircle.setImageResource(R.drawable.ic_round_check_uncomplete_circle_24);
        //order Received Operation
        orderSendLine.setBackgroundColor(getResources().getColor(R.color.uncompleted_line_color));
        orderReceivedCircle.setImageResource(R.drawable.ic_round_check_uncomplete_circle_24);
        orderReceivedStatus.setTextColor(getResources().getColor(R.color.uncompleted_line_color));
        orderReceiveDate.setTextColor(getResources().getColor(R.color.uncompleted_line_color));
        //ready to pickup Operation
        orderReceivedLine.setBackgroundColor(getResources().getColor(R.color.uncompleted_line_color));
        readyToPickupCircle.setImageResource(R.drawable.ic_round_check_uncomplete_circle_24);
        readyToPickupStatus.setTextColor(getResources().getColor(R.color.uncompleted_line_color));
        readyToPickupDate.setTextColor(getResources().getColor(R.color.uncompleted_line_color));
        //delivered Operation
        readyToPickupLine.setBackgroundColor(getResources().getColor(R.color.uncompleted_line_color));
        deliveredCircle.setImageResource(R.drawable.ic_round_check_uncomplete_circle_24);
        deliveredStatus.setTextColor(getResources().getColor(R.color.uncompleted_line_color));
        deliveredDate.setTextColor(getResources().getColor(R.color.uncompleted_line_color));

        //reset receever view
        receiverName.setText("Received by : ----");
        receiverDepartment.setText("-------------");
        //setting dats
        deliveredDate.setText("");
        readyToPickupDate.setText("");
        orderReceiveDate.setText("");
        orderSendDate.setText("");
        //setting more info
    }
    @Override
    public void onBackPressed() {
        if (isZoomLayoutOn){
            zoomLayout.setVisibility(View.GONE);
            getSupportActionBar().show();
            isZoomLayoutOn = false ;
        }else{
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                    onBackPressed();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void validation(){
        if (selectedQuality.equals("null")){
            ToastSilicon.toastDangerTwo(HistoryDetailsActivity.this,"Please Select a Quality!",Toast.LENGTH_SHORT);
        }else if (userComment.getText().toString().isEmpty()){
            ToastSilicon.toastDangerTwo(HistoryDetailsActivity.this,"Please Insert Your Feedback!",Toast.LENGTH_SHORT);
        }else if (!isPhotoSelected){
            ToastSilicon.toastDangerTwo(HistoryDetailsActivity.this,"Please Insert an Image!",Toast.LENGTH_SHORT);
        }else{
            networkingMethod();
        }
    }

    //image section
    private void networkingMethod(){
        String url = AppUrl.feedbackSubmitUrl();
        final ProgressDialog progressDialog = new ProgressDialog(HistoryDetailsActivity.this);
        progressDialog.setMessage("Submitting...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.upload(url)
                .addMultipartParameter("issue_no",getIssueNo)
                .addMultipartParameter("solving_status",getSolvedCode)
                .addMultipartParameter("service_quality",selectedQuality)
                .addMultipartParameter("user_comments",userComment.getText().toString())
                .addMultipartFile("image",imagefile)
                .setPriority(Priority.HIGH)
                .build().setUploadProgressListener(new UploadProgressListener() {
            @Override
            public void onProgress(long bytesUploaded, long totalBytes) {
                progressDialog.show();
            }
        }).getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("ressn",response);
                if (response.equals("1")){
//                    imageBitmap = BitmapFactory.decodeFile(imagefile.getAbsolutePath());
                    ToastSilicon.toastPrimaryTwo(HistoryDetailsActivity.this,"Submit Feedback Success", Toast.LENGTH_SHORT);
                    loadHistoryData(getIssueNo);
                    isPhotoSelected = false;
                    solvingSpinner.setSelection(0);
                    userComment.setText("");
                    feedbackImage.setImageResource(R.drawable.ic_baseline_blue_camera_alt_24);
                    feedbackDialog.dismiss();
                }else{
                    ToastSilicon.toastDangerTwo(HistoryDetailsActivity.this,"Submit Feedback Failed!",Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                progressDialog.dismiss();
                Log.e("ressn",anError.getErrorDetail());
                ToastSilicon.toastDangerTwo(HistoryDetailsActivity.this,"Please Check Your Internet Connection",Toast.LENGTH_SHORT);
            }
        });
    }
    public void MultiplePermissions(){

        Dexter.withContext(HistoryDetailsActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(HistoryDetailsActivity.this);
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        if(permissionDeniedResponse.isPermanentlyDenied()){
                            AlertDialog.Builder builder=new AlertDialog.Builder(HistoryDetailsActivity.this);
                            builder.setTitle("App Permission").setMessage("Please Confirm Permission!")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.Q)
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent=new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package",getOpPackageName(),null));
                                            startActivityForResult(intent,51);
                                        }
                                    }).setNegativeButton("cancle",null).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }

                }).check();
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                final Uri resultUri = result.getUri();
//                imagefile = new File(resultUri.getPath());
//                isPhotoSelected = true ;
//
//                imageBitmap = BitmapFactory.decodeFile(imagefile.getAbsolutePath());
//                feedbackImage.setImageBitmap(imageBitmap);
//
//                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                bmOptions.inSampleSize = 8 ;
//
//
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//
//            }
//        }
//    }
    private void getFollowUpList(){
        followUpItems.clear();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String urlString = AppUrl.followUpUrl();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("typeresponse",response);
                        progressDialog.dismiss();
                        followupDialog.show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length()>=1){
                                for(int i = 0 ;i<jsonArray.length();i++){
                                    JSONObject dataObj = jsonArray.getJSONObject(i);
                                    FollowUpItems myItems = new FollowUpItems(
                                            dataObj.getString("issue_no"),
                                            dataObj.getString("comments"),
                                            dataObj.getString("created_at")
                                    );
                                    followUpItems.add(myItems);
                                }
                                followUpAdapter = new FollowUpAdapter(followUpItems,getApplicationContext());
                                followUpRecyclerview.setAdapter(followUpAdapter);

                            }else{
                                ToastSilicon.toastDangerTwo(HistoryDetailsActivity.this,"No FollowUp Found !", Toast.LENGTH_SHORT);
                            }

                        } catch (JSONException e) {
                            ToastSilicon.toastDangerTwo(HistoryDetailsActivity.this,"Something Went Wrong !", Toast.LENGTH_SHORT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        ToastSilicon.toastDangerTwo(HistoryDetailsActivity.this,"Please Check Your Internet Connection !",Toast.LENGTH_SHORT);
                    }
                }
        ){
            protected Map<String , String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("issue_no",getIssueNo);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode == CAMERA_REQUEST) {
                try {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    feedbackImage.setImageBitmap(bitmap);
                    isPhotoSelected = true;
                    //create a file to write bitmap data
                    imagefile = new File(getCacheDir(), "issue_form_image");
                    try {
                        imagefile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Convert bitmap to byte array
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    //write the bytes in file
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(imagefile);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.e("file",imagefile.getAbsolutePath());
                } catch (Exception e) {

                }
            }

            if (requestCode == GALLERY_REQUEST_CODE) {
                try {
                    Uri selectedImageUri = data.getData();
                    final String path = getRealPathFromURI(HistoryDetailsActivity.this, selectedImageUri);

                    //imagefile = new File(RealPathUtil.getRealPath(IssueFormActivity.this, selectedImageUri));
                    //imagefile = new File(path);
                    //selectedImageUri = Uri.fromFile(imagefile);
                    Log.e("path", "pathNotNull");

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImageUri);



                    imagefile = new File(getCacheDir(),"gallery_image_file");
                    imagefile.createNewFile();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();


                    bitmap.compress(Bitmap.CompressFormat.JPEG,50/*ignored for PNG*/,bos);
                    feedbackImage.setImageBitmap(bitmap);

                    byte[] bitmapdata = bos.toByteArray();
                    FileOutputStream fos = new FileOutputStream(imagefile);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();

                    //showImage.setImageURI(selectedImageUri);
                    isPhotoSelected = true;
                } catch (Exception e) {
                    Log.e("galleryE", e.toString());
                }

            }


            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                final CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    final Uri resultUri = result.getUri();
                    imagefile = new File(resultUri.getPath());
                    isPhotoSelected = true;

                    Log.e("Onresult", "onresult");
                    imageBitmap = BitmapFactory.decodeFile(imagefile.getAbsolutePath());
                    showImage.setImageBitmap(imageBitmap);


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();

                }
            }
        }else{

        }
    }
    public void askPermissioni() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        chooseMediaType();
    }
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull @NotNull List<String> perms) {
        ToastSilicon.toastDangerTwo(HistoryDetailsActivity.this, "Permission Denied!", Toast.LENGTH_SHORT);
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

//        if (requestCode == CAMERA_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openCamera();
//            }
//        } else {
//            ToastSilicon.toastDangerTwo(IssueFormActivity.this, "Permission Denied!", Toast.LENGTH_SHORT);
//        }
    }
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }
    private void galleryIntent() {

        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
            galleryIntent.setType("image/*");
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GALLERY_REQUEST_CODE);
            ;
        } else {
            EasyPermissions.requestPermissions(this, "Need Permission", 123, Manifest.permission.READ_EXTERNAL_STORAGE);
        }


    }
    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private void chooseMediaType() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            new AlertDialog.Builder(this)
                    .setIcon(getResources().getDrawable(R.drawable.ic_baseline_warning_24))
                    .setTitle("Please Choose Option")
                    .setMessage("How You want to Input Image ?")
                    .setPositiveButton("CAMERA", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openCamera();
                        }
                    })
                    .setNegativeButton("GALLERY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            galleryIntent();
                        }
                    })
                    .show();
        }else {
            EasyPermissions.requestPermissions(this, "Need Permission", 123, perms);
        }

    }

}