package com.goodthinking.younglod.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EventInformationActivity_Firebase extends AppCompatActivity {
    private TextView ViewEventHeadline, ViewEventdate, ViewEventtime,
            ViewEventSynopsys, ViewEventInfo, ViewEventParticipatorsno, ViewEventHostName, RegistrationIsClosed;
    private DatabaseReference Eventdatabase,  MyEventdatabase ;
    private int position;
    Button Register;
    String key;

    private DatabaseReference root;
    private FirebaseAuth mAuth;
    private String keyUserId;
    private Boolean EventIsClosed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_information_activity__firebase);

        root = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        Register=(Button)findViewById(R.id.fbviewregtoeventtbtn);
        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            key = intent.getStringExtra("Eventkey");
            position = intent.getIntExtra("position", 0);
            //Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_LONG).show();

            ViewEventHeadline = (TextView) findViewById(R.id.fbviewEventHeadlineview);
            ViewEventdate = (TextView) findViewById(R.id.fbviewEventdateview);
            ViewEventtime = (TextView) findViewById(R.id.fbviewEventtimeview);
            ViewEventSynopsys = (TextView) findViewById(R.id.fbviewEventSynopsysview);
            ViewEventInfo = (TextView) findViewById(R.id.fbviewEventInfoview);
            ViewEventParticipatorsno = (TextView) findViewById(R.id.fbviewEventParticipatorsno);
            ViewEventHostName = (TextView) findViewById(R.id.fbviewEventhostname);
            RegistrationIsClosed = (TextView) findViewById(R.id.fbRegistrationIsClosed);


//             Event event = (Event) intent.getSerializableExtra("Event");
//            if (event != null) {
//
//
//            ViewEventHeadline.setText(event.getEventName());
//            ViewEventdate.setText(event.getEventDate());
//            ViewEventtime.setText(event.getEventTime());
//            ViewEventSynopsys.setText(event.getEventSynopsys());
//            ViewEventInfo.setText(event.getEventInformation());
//            ViewEventParticipatorsno.setText("" +event.getEventParticipatorsno());
//            ViewEventHostName.setText(event.getEventHost());
//             }


            ViewEventHeadline.setText(EventArraydata.getInstance().getEvents().get(position).getEventName());
            ViewEventdate.setText(EventArraydata.getInstance().getEvents().get(position).getEventDate());
            ViewEventtime.setText(EventArraydata.getInstance().getEvents().get(position).getEventTime());
            ViewEventSynopsys.setText(EventArraydata.getInstance().getEvents().get(position).getEventSynopsys());
            ViewEventInfo.setText(EventArraydata.getInstance().getEvents().get(position).getEventInformation());
            ViewEventParticipatorsno.setText(String.valueOf(EventArraydata.getInstance().getEvents().get(position).getEventParticipatorsno()));
            ViewEventHostName.setText(EventArraydata.getInstance().getEvents().get(position).getEventHost());
            EventIsClosed = EventArraydata.getInstance().getEvents().get(position).getEventIsClosed();
            if (EventIsClosed == true){
                Register.setEnabled(false);
                RegistrationIsClosed.setVisibility(View.VISIBLE);
            }
        }

        checkRegistration();
    }

    private void checkRegistration() {
        if (mAuth.getCurrentUser() != null) {
            keyUserId = mAuth.getCurrentUser().getUid();
            Query qrefUserRegister = root.child("Tables").child("Events").child(key).child("Applicants").child(keyUserId);
            qrefUserRegister.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot == null || snapshot.getValue() == null) {
                        Register.setText(R.string.register_event_btn);
                        //  Toast.makeText(EventInformationActivity_Firebase.this, "No record found",
                        //          Toast.LENGTH_SHORT).show();
                    } else {
                        Register.setText(R.string.update_register_event_btn);
                        // Toast.makeText(EventInformationActivity_Firebase.this, snapshot.getValue().toString(),
                        //         Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Download failed" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Register.setText(R.string.register_event_btn);
        }
    }

    public void CancelAddEventbtn(View view) {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void RegisterEventbtn(View view) {
        Intent intent = new Intent(getApplicationContext(), EventRegisterationActivity.class);
        intent_putExtra(intent);
      //  finish();
    }


    private void intent_putExtra(Intent intent) {
        intent.putExtra("Eventkey", EventArraydata.getInstance().getEvents().get(position).getKey());
        intent.putExtra("position",position);
        startActivity(intent);
        finish();
    }
}
