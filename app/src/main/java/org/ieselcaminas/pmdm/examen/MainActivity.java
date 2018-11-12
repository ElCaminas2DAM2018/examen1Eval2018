package org.ieselcaminas.pmdm.examen;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private static final int PICK_CONTACT_REQUEST = 1234;
    private static final int SEND_ACTIVITY = 1235;

    public static final String MESSAGE = "message";
    public static final String PHONE_OR_NAME = "PHONE_OR_NAME";


    private TextView phoneLabel;
    private TextView nameLabel;
    private EditText editText;
    private TextView messageLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneLabel = findViewById(R.id.PhoneLabel);
        nameLabel = findViewById(R.id.NameLabel);
        editText = findViewById(R.id.EditTextMessage);
        messageLabel = findViewById(R.id.messageSent);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString("name");
            String phone = savedInstanceState.getString("phone");

            if (name != null) {
                nameLabel.setText(name);
            }
            if (phone != null) {
                phoneLabel.setText(phone);
            }
        }


        Button selectButton = (Button) findViewById(R.id.buttonSelectContact);
        selectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        Uri.parse("content://contacts"));
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, PICK_CONTACT_REQUEST);
            }
        });

        TableLayout table = findViewById(R.id.table);
        for (int i=0; i<table.getChildCount() - 1; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (int j=0; j< row.getChildCount(); j++) {
                final Button b = (Button) row.getChildAt(j);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phoneLabel.setText(phoneLabel.getText().toString() +
                                            b.getText());
                        nameLabel.setText("");
                        messageLabel.setText("");
                    }
                });
            }
        }

        Button buttonDel = findViewById(R.id.buttonDel);
        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = phoneLabel.getText().toString();
                if (s != null && s.length()>0) {
                    s = s.substring(0, phoneLabel.length()-1);
                    phoneLabel.setText(s);
                    nameLabel.setText("");
                    messageLabel.setText("");
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                TextView textViewCounter = findViewById(R.id.TextViewCounter);
                textViewCounter.setText("" + editText.getText().toString().length());
                messageLabel.setText("");
            }
        });
        Button sendButton = findViewById(R.id.buttonSend);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageLabel.setText("");
                Intent intent = new Intent(getApplicationContext(), SendACtivity.class);
                intent.putExtra(MESSAGE, editText.getText().toString());
                String phoneOrName = nameLabel.getText().toString();
                if (phoneOrName == null || phoneOrName.length() == 0) {
                    phoneOrName = phoneLabel.getText().toString();
                }
                intent.putExtra(PHONE_OR_NAME, phoneOrName);
                startActivityForResult(intent, SEND_ACTIVITY);
            }
        });

        Button buttonDial = findViewById(R.id.buttonDial);
        buttonDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("org.ieselcaminas.pmdm.dialer.dialer");
                intent.putExtra("phone", phoneLabel.getText().toString());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = resultIntent.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);

                int columnName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(columnName);

                // Do something with the phone number...
                phoneLabel.setText(number);
                nameLabel.setText(name);

            }
        } else {
            if (requestCode == SEND_ACTIVITY) {
                if (resultCode == RESULT_OK) {
                    String s = resultIntent.getExtras().getString(SendACtivity.VIA);
                    messageLabel.setText("Message sent via " + s);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("name", nameLabel.getText().toString());
        bundle.putString("phone", phoneLabel.getText().toString());
        Toast.makeText(this, "Executing onSaveInstanceState",Toast.LENGTH_LONG).show();
    }
}
