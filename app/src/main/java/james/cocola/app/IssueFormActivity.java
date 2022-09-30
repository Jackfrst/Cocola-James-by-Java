package james.cocola.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.cocola.app.R;
import com.google.common.util.concurrent.ListenableFuture;
import james.cocola.app.AppClass.AppUrl;
import james.cocola.app.AppClass.LoginPreferences;
import james.cocola.app.AppClass.OnLocationSelect;
import james.cocola.app.LocationRecyclerView.LocationAdapter;
import james.cocola.app.LocationRecyclerView.LocationItems;
import com.rabbil.toastsiliconlibrary.ToastSilicon;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class IssueFormActivity extends AppCompatActivity implements OnLocationSelect,EasyPermissions.PermissionCallbacks {
    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_REQUEST = 102;
    public static final int GALLERY_REQUEST_CODE = 111;
    private LinearLayout captureBtn;
    private ImageView showImage;
    Bitmap imageBitmap = null;
    File imagefile = null;
    private boolean isPhotoSelected = false;
    //finding basic components
    private LinearLayout locationLayout;
    private EditText issueExitText;
    private EditText descEditText;
    private Button submitBtn;
    private TextView locationView;
    private boolean isLocationSelected = false;

    //intent strings
    private String issueCode, issueName;
    private LoginPreferences loginPreferences;

    //dialog components
    //dialog components
    private TextView dialogTitle;
    private String selectedLocation = "null";
    private String selectedLocationCode = "null";
    private Dialog selectLocationDialog;
    private ProgressBar dialogProgressBar;
    private RecyclerView locationRecyclerView;
    private List<LocationItems> locationList;
    private LocationAdapter locationAdapter;

    private LinearLayout BtnLayout, formImageLayout;

    //camerax setup
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    ImageCapture imageCapture;
    PreviewView previewView;
    private Uri outputFileUri;
    private static int TAKE_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_issue_form);
        loginPreferences = new LoginPreferences(this);

        //get intent data
        Intent i = getIntent();
        issueName = i.getStringExtra("nameKey");
        issueCode = i.getStringExtra("codeKey");
        setTitle(issueName);

        //finding basics components
        locationLayout = findViewById(R.id.form_select_location_layout);
        locationView = findViewById(R.id.form_location_text_view);
        issueExitText = findViewById(R.id.form_issue_edit_text);
        descEditText = findViewById(R.id.form_input_field);
        submitBtn = findViewById(R.id.form_submit_btn);
        BtnLayout = findViewById(R.id.form_btn_layout);
        formImageLayout = findViewById(R.id.form_image_layout);


        //finding
        captureBtn = findViewById(R.id.form_image_capture_btn);
        showImage = findViewById(R.id.form_image_show_view);

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MultiplePermissions();
                //OpenMediaChooser();
                //askPermissioni();
                //galleryIntent();
                chooseMediaType();
            }
        });

        //dialog setup
        //select type dialog
        selectLocationDialog = new Dialog(this);
        selectLocationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        selectLocationDialog.setCancelable(true);
        selectLocationDialog.setContentView(R.layout.select_location_dialog);
        Window window = selectLocationDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogProgressBar = selectLocationDialog.findViewById(R.id.dialog_progress_bar);
        dialogTitle = selectLocationDialog.findViewById(R.id.dialog_title_view);
        dialogTitle.setText("Select Location");
        //finding recyclerview
        locationRecyclerView = selectLocationDialog.findViewById(R.id.select_location_recyclerview);
        locationRecyclerView.setNestedScrollingEnabled(true);
        locationRecyclerView.setHasFixedSize(false);
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationList = new ArrayList<>();
        LocationItems myItems = new LocationItems(
                "Head Office",
                "demo_code"
        );
        LocationItems myItems2 = new LocationItems(
                "Factory",
                "demo_code"
        );
        locationList.add(myItems);
        locationList.add(myItems2);
        locationAdapter = new LocationAdapter(locationList, getApplicationContext(), IssueFormActivity.this);
        locationRecyclerView.setAdapter(locationAdapter);
        //onclick
        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLocationDialog.show();
            }
        });

        //sunbmit action
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });

    }
    //camerax
    /*
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
        imageCapture =
                new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(previewView.getDisplay().getRotation())
                        .build();

        cameraProvider.bindToLifecycle((LifecycleOwner)this,cameraSelector,preview,imageCapture);
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    void  captureImage(){
        File photoDir = new File("/mnt/sdcard/Pictures/CameraPhotos");
        if (photoDir.exists())
            photoDir.mkdir();

        Date date = new Date();
        String timeStamp = String.valueOf(date.getTime());
        String photoFilePath = photoDir.getAbsolutePath()+"/"+timeStamp+".jpg";
        File photoFile = new File(photoFilePath);

        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(photoFile).build(),
                getMainExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull @NotNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(IssueFormActivity.this, "Successfully Saved Imge", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(@NonNull @NotNull ImageCaptureException exception) {
                        Toast.makeText(IssueFormActivity.this, "Failed To save Image", Toast.LENGTH_SHORT).show();
                        Log.e("captueClicke",exception.toString());
                    }
                }
        );
    }
    */


    private void validation() {
        if (!isPhotoSelected) {
            ToastSilicon.toastDangerTwo(IssueFormActivity.this, "Please Select an Image!", Toast.LENGTH_SHORT);
        } else if (!isLocationSelected) {
            ToastSilicon.toastDangerTwo(IssueFormActivity.this, "Please select Location!", Toast.LENGTH_SHORT);
        } else if (issueExitText.getText().toString().isEmpty()) {
            ToastSilicon.toastDangerTwo(IssueFormActivity.this, "Please Insert an Issue!", Toast.LENGTH_SHORT);
        } else if (descEditText.getText().toString().isEmpty()) {
            ToastSilicon.toastDangerTwo(IssueFormActivity.this, "Please Insert Issue Details!", Toast.LENGTH_SHORT);
        } else {
            networkingMethod();
        }
    }
    private void networkingMethod() {
        String url = AppUrl.newIssueUrl();
        final ProgressDialog progressDialog = new ProgressDialog(IssueFormActivity.this);
        progressDialog.setMessage("Submitting...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (imagefile == null) {
            Log.e("nummImg", "imgNull");
        }

        AndroidNetworking.upload(url)
                .addMultipartParameter("emp_id", loginPreferences.onLoginIdGet())
                .addMultipartParameter("cat_code", issueCode)
                .addMultipartParameter("cat_name", issueName)
                .addMultipartParameter("location", selectedLocation)
                .addMultipartParameter("issue_title", issueExitText.getText().toString())
                .addMultipartParameter("issue_details", descEditText.getText().toString())
                .addMultipartFile("image", imagefile)
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
                Log.e("ressn", response);
                if (response.equals("1")) {
//                    imageBitmap = BitmapFactory.decodeFile(imagefile.getAbsolutePath());
                    ToastSilicon.toastPrimaryTwo(IssueFormActivity.this, "Submit Issue Success", Toast.LENGTH_SHORT);
                    onBackPressed();
                } else {
                    ToastSilicon.toastDangerTwo(IssueFormActivity.this, "Submit Issue Failed!", Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                progressDialog.dismiss();
                Log.e("ressn", anError.getErrorDetail());
                ToastSilicon.toastDangerTwo(IssueFormActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT);
            }
        });
    }

    //    public void MultiplePermissions(){
//
//        Dexter.withContext(IssueFormActivity.this)
//                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
////                        CropImage.activity()
////                                .setGuidelines(CropImageView.Guidelines.ON)
////                                .start(IssueFormActivity.this);
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                        outputFileUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//                        startActivityForResult(intent, TAKE_PICTURE);
//                    }
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                        if(permissionDeniedResponse.isPermanentlyDenied()){
//                            AlertDialog.Builder builder=new AlertDialog.Builder(IssueFormActivity.this);
//                            builder.setTitle("App Permission").setMessage("Please Confirm Permission!")
//                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                        @RequiresApi(api = Build.VERSION_CODES.Q)
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            Intent intent=new Intent();
//                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                            intent.setData(Uri.fromParts("package",getOpPackageName(),null));
//                                            startActivityForResult(intent,51);
//                                        }
//                                    }).setNegativeButton("cancle",null).show();
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                        permissionToken.continuePermissionRequest();
//                    }
//
//                }).check();
//    }


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
    @Override
    public void onLocationSelectedListener(String typeName, String typeCode) {
        isLocationSelected = true;
        selectedLocation = typeName;
        selectLocationDialog.dismiss();
        locationView.setText(selectedLocation);
        selectedLocationCode = typeCode;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){



            Log.e("OnResult", "onResult");

            if (requestCode == CAMERA_REQUEST) {
                try {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    showImage.setImageBitmap(bitmap);
                    isPhotoSelected = true;
                    BtnLayout.setVisibility(View.GONE);
                    formImageLayout.setVisibility(View.VISIBLE);
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
                    final String path = getRealPathFromURI(IssueFormActivity.this, selectedImageUri);

                    //imagefile = new File(RealPathUtil.getRealPath(IssueFormActivity.this, selectedImageUri));
                    //imagefile = new File(path);
                    //selectedImageUri = Uri.fromFile(imagefile);
                    Log.e("path", "pathNotNull");

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImageUri);



                    imagefile = new File(getCacheDir(),"gallery_image_file");
                    imagefile.createNewFile();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();


                    bitmap.compress(Bitmap.CompressFormat.JPEG,50/*ignored for PNG*/,bos);
                    showImage.setImageBitmap(bitmap);

                    byte[] bitmapdata = bos.toByteArray();
                    FileOutputStream fos = new FileOutputStream(imagefile);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();

                    //showImage.setImageURI(selectedImageUri);
                    isPhotoSelected = true;
                    BtnLayout.setVisibility(View.GONE);
                    try{
                        formImageLayout.setVisibility(View.VISIBLE);
                    }catch (Exception e){
                        Log.e("exception",e.toString());
                    }


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
                    BtnLayout.setVisibility(View.GONE);
                    formImageLayout.setVisibility(View.VISIBLE);

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
        ToastSilicon.toastDangerTwo(IssueFormActivity.this, "Permission Denied!", Toast.LENGTH_SHORT);
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
    public String getRealPathFromURI(Context context, Uri contentUri) {
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
    public void chooseMediaType() {
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