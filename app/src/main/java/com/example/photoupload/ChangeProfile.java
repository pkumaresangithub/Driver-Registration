package com.example.photoupload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ChangeProfile extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 100;

    EditText fname,mname,lname,address, state,city,pin,mobileno;
    ImageView imageView;
    Button btnsave,Btnupload;
    String Mobileno;
    private static final int GalleryPick = 1;
    Uri ImageUri;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Dialog dialog, inputdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        fname = findViewById(R.id.firstname);
        mname = findViewById(R.id.midname);
        lname = findViewById(R.id.lastname);
        address = findViewById(R.id.PostalAddress);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        pin = findViewById(R.id.pincode);
        mobileno = findViewById(R.id.mobileno);
        imageView = findViewById(R.id.imageview);


        Btnupload = findViewById(R.id.Btnupload);

        imageView = findViewById(R.id.imageview);

        btnsave = findViewById(R.id.BtnSave);

        dialog = new Dialog(ChangeProfile.this);
        dialog.setContentView(R.layout.photo_dialog);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional

        ImageButton btncamera = dialog.findViewById(R.id.BtnCamera);
        ImageButton btnimage = dialog.findViewById(R.id.BtnGallery);
        ImageButton btncancel = dialog.findViewById(R.id.BtnCancel);


        getInputMobileNO();


        Btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

                btncamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkPermission()) {
                            //write your main code to execute, It will execute if the permission is already given.
                            openCameraMethod();
                        } else {
                            requestPermission();
                        }
                        dialog.dismiss();
                    }
                });

                btnimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        OpenGallery();
                    }
                });

                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });



        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadtofirebase();
            }
        });
    }

    private void getInputMobileNO() {

        inputdialog  = new Dialog(ChangeProfile.this);
        inputdialog.setContentView(R.layout.mobile_input);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            inputdialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        inputdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        inputdialog.setCancelable(true); //Optional

        inputdialog.show();
        Toast.makeText(getApplicationContext(),"Afet Dialog input",Toast.LENGTH_LONG).show();

        ImageButton btnretrieve = inputdialog.findViewById(R.id.BtnRetrieve);
        EditText edtmobile = inputdialog.findViewById(R.id.EdtMobile);
        btnretrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mobileno = edtmobile.getText().toString();
                getDriverDetails(Mobileno);
                inputdialog.dismiss();

            }
        });

    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    private void openCameraMethod() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);

    }


    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CAMERA_REQUEST_CODE && resultCode == RESULT_OK)
        {
            Log.e("error","Error in camera");
            OnCaptureImage(data);
        }

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            imageView.setImageURI(ImageUri);
        }
    }

    private void OnCaptureImage(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap bitmap  = (Bitmap)extras.get("data");
        imageView.setImageBitmap(bitmap);
        ImageUri =getImageUri(getApplicationContext(), bitmap);

//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG,90,bytes);
//        byte bb[] = bytes.toByteArray();
//        imageView.setImageBitmap(thumbnail);
    }

    private Uri getImageUri(Context applicationContext, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
        return Uri.parse(path);
    }

    private void uploadtofirebase() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();

        String Fname = fname.getText().toString();
        String Mname = mname.getText().toString();
        String Lname = lname.getText().toString();
        String Address = address.getText().toString();
        String State = state.getText().toString();
        String City = city.getText().toString();
        String Pin = pin.getText().toString();
        String Mobileno = mobileno.getText().toString();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference uploader = storage.getReference("DriverPhotos/" + Mobileno);

        uploader.putFile(ImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user");

                                DriverModelimage model = new DriverModelimage(Mobileno, Fname, Mname, Lname, Address, State, City, Pin, uri.toString());
                                databaseReference.child(Mobileno).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            dialog.dismiss();
//                                                datareset();
                                            Toast.makeText(getApplicationContext(), "User data Uploaded", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float percent = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded :" + (int) percent + " %");
                    }
                });

    }

    private void datareset() {
        fname.setText("");
        mname.setText("");
        lname.setText("");
        mobileno.setText("");
        address.setText("");
        state.setText("");
        city.setText("");
        pin.setText("");
        imageView.setImageResource(R.drawable.ic_launcher_background);

    }


// get Drivers details method for retrive data

    private void getDriverDetails(String mobile) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
   //    final StorageReference uploader = storage.getReference("DriverPhotos/" + mobile);

        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("user");
        driverRef.child(mobile).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                     DriverModelimage driverModelimage = dataSnapshot.getValue(DriverModelimage.class);
                    fname.setText(driverModelimage.getFname());
                    mname.setText(driverModelimage.getMname());
                    lname.setText(driverModelimage.getLname());
                    address.setText(driverModelimage.getAddress());
                    state.setText(driverModelimage.getState());
                    mobileno.setText(driverModelimage.getMobileno());
                    city.setText(driverModelimage.getCity());
                    pin.setText(driverModelimage.getPin());
//                    ImageUrl.setText(driverModelimage.getImageurl());
//                    imageView.setImageURI(driverModelimage.getImageurl());
                    Picasso.get().load(driverModelimage.getImageurl()).into(imageView);

//                    ImageUri =getImageUri(driverModelimage.getImageurl(),Bitmap);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    }
