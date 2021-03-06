package com.example.a2hands.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a2hands.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

public class EditPhoneActivity extends AppCompatActivity {

    private CountryCodePicker ccpCode;
    private EditText editTextCarrierNumberr;

    private Button savePhone;

    //firebase
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);

        savePhone= findViewById(R.id.saveNewPhone_btn);

        //code and phone
        ccpCode = findViewById(R.id.ccpCode_phoneEdit);
        editTextCarrierNumberr = findViewById(R.id.editText_carrierNumber_phoneEdit);
        ccpCode.registerCarrierNumberEditText(editTextCarrierNumberr);

        savePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editTextCarrierNumberr.getText().toString().trim())){
                    editTextCarrierNumberr.setError("Enter your Phone");
                }
                else if(! ccpCode.isValidFullNumber()) {
                    editTextCarrierNumberr.setError("Phone is not valid");
                }else {
                    Map<String, Object> newPhone = new HashMap<>();
                    newPhone.put("phone", ccpCode.getFullNumber());

                    db.collection("users").document(auth.getCurrentUser().getUid()).update(newPhone)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("saveNewPhone", "Done");
                                    startActivity(new Intent(EditPhoneActivity.this , SettingsActivity.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("saveNewPhone", e.toString());
                                    startActivity(new Intent(EditPhoneActivity.this , SettingsActivity.class));
                                    finish();
                                }
                            });
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
