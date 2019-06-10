package com.example.nudge.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nudge.R;
import com.example.nudge.models.AgentModel;
import com.example.nudge.utils.SharedPrefUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText licenseCode;
    TextView submit;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    SharedPreferences agentId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String android_id;

    SharedPrefUtils sharedPrefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        agentId=this.getSharedPreferences("com.example.nudge", Context.MODE_PRIVATE);

        sharedPrefUtils = new SharedPrefUtils(this);

        licenseCode = findViewById(R.id.edit_text_license_code);
        submit = findViewById(R.id.submit);
        progressBar = findViewById(R.id.progress_login);

        mAuth = FirebaseAuth.getInstance();

        android_id = android.os.Build.MODEL + " " + Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        progressBar.setVisibility(View.VISIBLE);

        if(mAuth!=null && agentId.getString("agentId","Not found").compareTo("Not found")!=0) {

            progressBar.setVisibility(View.INVISIBLE);
            startActivity(new Intent(this,MainActivity.class));

        } else {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Problem", "signInAnonymously:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            // ...
                        }
                    });
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                checkId(licenseCode.getText().toString());
            }
        });

    }

    public void checkId(final String id) {

        Toast.makeText(this, "Checking Licence code. Please Wait...", Toast.LENGTH_SHORT).show();

        db.collection("agents").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    int flag=0;
                    for(DocumentSnapshot d: list) {

                        AgentModel agent = d.toObject(AgentModel.class);

                        if(agent.getId().equals(id)) {

                            db.collection("agents").document(agent.getId()).update(
                                    "isLoggedIn",true,
                                    "device", android_id
                            );

                            Toast.makeText(LoginActivity.this, "Welcome "+agent.getName(), Toast.LENGTH_SHORT).show();
                            agentId.edit().putString("agentId",agent.getId()).apply();
                            agentId.edit().putString("agentName",agent.getName()).apply();
                            agentId.edit().putString("agentImg",agent.getImage()).apply();
                            agentId.edit().putString("agentFarmersCnt",agent.getFarmers_count()).apply();
                            agentId.edit().putString("agentLevel",agent.getLevel()).apply();
                            agentId.edit().putInt("agentPts",agent.getPoints()).apply();

                            sharedPrefUtils.writeAgentId(agent.getId());

                            flag = 1;
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            break;
                        }
                    }
                    if(flag==0) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "No agents found with this Code. Please Try Again.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, "Searching Failed. Check Your internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}