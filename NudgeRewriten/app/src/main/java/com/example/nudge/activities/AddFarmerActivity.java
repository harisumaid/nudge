package com.example.nudge.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nudge.R;
import com.example.nudge.fragments.FarmerListFragment;
import com.example.nudge.models.FarmerModel;
import com.example.nudge.utils.SharedPrefUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.model.value.GeoPointValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;


public class AddFarmerActivity extends AppCompatActivity {

    ImageView cameraBtn, farmerImg, backBtn2;
    EditText farmerName, farmerNo1, farmerNo2, farmerAdd,farmSize;
    Toolbar toolbar;
    Uri img_uri;
    ProgressBar geoPb,uploadPb;
    int flag=0;
    SharedPrefUtils sharedPrefUtils;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference mStorageRef;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    boolean gotLocation = false;

    double longitude = 0.0;
    double latitude = 0.0;

    LocationManager locationManager;

    public boolean validLatLng(double lat, double lng) {
        if (lat != 0.0 && lng != 0.0) {
            this.gotLocation = true;
            return true;
        } else return false;
    }

    public boolean haveLocation() {
        return this.gotLocation;
    }

    // CHECK PERMISSION FOR LOCATION

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("The App wants your location.")
                        .setMessage("Turn on GPS!")
                        .setPositiveButton("Give Permission.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(AddFarmerActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_farmer);

        cameraBtn = findViewById(R.id.camera_btn);
        farmerImg = findViewById(R.id.farmer_img);

        farmerName = findViewById(R.id.farmer_name);
        farmerAdd = findViewById(R.id.farmer_add);
        farmerNo1 = findViewById(R.id.farmer_no_1);
        farmerNo2 = findViewById(R.id.farmer_no_2);
        backBtn2 = findViewById(R.id.back_btn2);
        uploadPb = findViewById(R.id.upload_pb);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(!isLocationEnabled(this) || !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
        }

        mStorageRef = FirebaseStorage.getInstance().getReference();

        farmSize = findViewById(R.id.farm_size);


        geoPb = findViewById(R.id.geo_pb);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkLocationPermission();

        sharedPrefUtils = new SharedPrefUtils(this);

        String id = getIntent().getStringExtra("FarmerId");
        if(id!=null) {
            db.collection("agents").document(sharedPrefUtils.readAgentId()).collection("farmers").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    FarmerModel farmer = documentSnapshot.toObject(FarmerModel.class);

                    farmerName.setText(farmer.getName());
                    farmerAdd.setText(farmer.getAddress());
                    farmerNo1.setText(farmer.getPrimary_contact_number());
                    farmerNo2.setText(farmer.getSecondary_contact_number());
                    Glide.with(getApplicationContext()).load(farmer.getImage()).into(farmerImg);
                    farmSize.setText(farmer.getFarm_size());
                }
            });
        }


        toolbar = findViewById(R.id.toolbar_farmer_add);

        toolbar.inflateMenu(R.menu.farmer_add_menu);
        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.actions_add) {

                    if(checkLocationPermission())
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,0,mLocationListener);

                    if(flag==0)
                        Toast.makeText(AddFarmerActivity.this, "Please Choose an Image.", Toast.LENGTH_SHORT).show();
                    else if(farmerName.getText().toString().length()==0) farmerName.setError("Name cannot be empty");
                    else if(farmerAdd.getText().toString().length()==0) farmerAdd.setError("Address cannot be empty");
                    else if(farmerNo1.getText().toString().length()==0 || (!validate(farmerNo1.getText().toString()))) farmerNo1.setError("Contact Number cannot be empty");
                    else if(farmerNo2.getText().toString().length()==0 || (!validate(farmerNo2.getText().toString()))) farmerNo2.setError("Provide an alternative Number");
                    else if(farmSize.getText().toString().length()==0) farmSize.setError("Farm Size cant be empty");
                    else {

                        Toast.makeText(AddFarmerActivity.this, "Adding farmer.", Toast.LENGTH_SHORT).show();

                        uploadPb.setVisibility(View.VISIBLE);
                        String name = farmerName.getText().toString();
                        String primary_contact_number = farmerNo1.getText().toString();
                        String secondary_contact_number = farmerNo2.getText().toString();
                        String address = farmerAdd.getText().toString();
                        String size = farmSize.getText().toString();
                        final String[] id = new String[1];

                            uploadPb.setVisibility(View.VISIBLE);
                            FarmerModel farmer = new FarmerModel(name,primary_contact_number,secondary_contact_number,"",address,size,"",new GeoPoint(latitude,longitude));

                            Bitmap bitmap = ((BitmapDrawable) farmerImg.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                            final byte[] data = baos.toByteArray();

                            Toast.makeText(AddFarmerActivity.this, "Farmer is being added to your account. Pls Wait.", Toast.LENGTH_SHORT).show();

                            db.collection("agents")
                                    .document(sharedPrefUtils.readAgentId())
                                    .collection("farmers").add(farmer)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            id[0] = documentReference.getId();
                                            final StorageReference farmerImgRef = mStorageRef.child("farmerImgs/"+id[0]+".jpg");

                                            UploadTask uploadTask = farmerImgRef.putBytes(data);

                                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    // Handle unsuccessful uploads
                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                                    // ...
                                                    farmerImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {

                                                            db.collection("agents")
                                                                    .document(sharedPrefUtils.readAgentId())
                                                                    .collection("farmers")
                                                                    .document(id[0]).update(
                                                                    "id",id[0],
                                                                    "image",uri.toString()
                                                            );

                                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                            intent.putExtra("ID","update");
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                    };
                    return true;
                } else return false;
            }
        });

        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.farmer_add_menu, menu);
        return true;
    }

    public void captureImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(AddFarmerActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });

        builder.show();

    }

    public boolean validate(String num) {

        if(num.length()!=10) return false;

        int fl=0;
        for(int i=0;i<num.length();i++) {
            if((num.charAt(i)=='+' && i==0) || (num.charAt(i)>=48 && num.charAt(i)<=57));
            else fl=1;
        }
        if(fl==1) return false;
        else return true;
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                farmerImg.setImageBitmap(photo);
                flag=1;
            } else if (requestCode == 2) {
                img_uri = data.getData();
                farmerImg.setImageURI(img_uri);
                flag=1;
            }

            // Create a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Get the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                checkLocationPermission();
                return;
            }
            Toast toast = Toast.makeText(this, "Geo-tagging the image.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            locationManager.requestLocationUpdates(provider, 1000, 0.0f, mLocationListener); // (String) provider, time in milliseconds when to check for an update, distance to change in coordinates to request an update, LocationListener.
        }
    }

    LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged (Location location){
            Log.i("Success","Somewhat.");
            if (!haveLocation() && validLatLng(location.getLatitude(), location.getLongitude())) {
                // Stops the new update requests.
                locationManager.removeUpdates(mLocationListener);
                longitude = location.getLongitude();
                latitude = location.getLatitude();



                Toast toast = Toast.makeText(getApplicationContext(), "Image Successfully Geo-tagged.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        }

        public void onStatusChanged(java.lang.String s, int i, android.os.Bundle bundle) {
        }

        public void onProviderEnabled(java.lang.String s){
        }

        public void onProviderDisabled(java.lang.String s){
            Log.i("Success","Problem");
        }

    };

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

}
