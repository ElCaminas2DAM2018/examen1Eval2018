package org.ieselcaminas.pmdm.examen;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SendACtivity extends AppCompatActivity {

    public static final String VIA = "VIA";

    private static final String[] items = {"SMS","WhatsApp"};
    private int optionSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_activity);

        TextView labelPhoneNumber = findViewById(R.id.textViewNameOrPhoneNumber);
        TextView labelMessage = findViewById(R.id.textViewMessage);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        labelPhoneNumber.setText(extras.getString(MainActivity.PHONE_OR_NAME));
        labelMessage.setText(extras.getString(MainActivity.MESSAGE));


        showDialog();

    }

    private void showDialog() {

        new AlertDialog.Builder(this)
                .setTitle("Select option")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(getBaseContext(),
                                        "OK clicked!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent();
                                intent.putExtra(VIA, items[optionSelected]);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(getBaseContext(),
                                        "Cancel clicked!", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_CANCELED);
                                finish();
                            }
                        }
                ).setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        optionSelected = which;
                    }
                }).create().show();

    }
}
