package com.vitin.mylaptop.invoiceactivity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.text.NumberFormat;



public class MainActivity extends AppCompatActivity implements OnEditorActionListener {
// define instance variable

    private EditText subtotalEditText;
    private TextView discountPercentLabel;
    private TextView discountAmountLabel;
    private TextView totalLabel;

    private String subtotalstring;
    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get reference in the widget

        subtotalEditText = (EditText) findViewById(R.id.subtotalEditText);
        discountPercentLabel = (TextView) findViewById(R.id.discountPercentLabel);
        discountAmountLabel = (TextView) findViewById(R.id.discountAmountLabel);
        totalLabel = (TextView) findViewById(R.id.totalLabel);

        //listener
        subtotalEditText.setOnEditorActionListener(this);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause() {
        Editor editor = savedValues.edit();
        editor.putString("subtotalString", subtotalstring);
        editor.commit();

        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        subtotalstring = savedValues.getString("subtotalstring","" );
        subtotalEditText.setText(subtotalstring);
        calculateAndDisplay();
    }

    private void calculateAndDisplay() {
        //get  sub total
        subtotalstring = subtotalEditText.getText().toString();
        float subtotal;
        if (subtotalstring.equals("")) {
            subtotal = 0;
        } else {
            subtotal = Float.parseFloat(subtotalstring);
        }
        // get discount percent
        float discountPercent = 0;
        if (subtotal >= 200) {
            discountPercent = .2f;
        } else if (subtotal >= 100) {
            discountPercent = .1f;
        } else {
            discountPercent = 0;
        }


        //calculate discount
        float discountAmount = subtotal * discountPercent;
        float total = subtotal - discountAmount;

// display data on the layout
        NumberFormat percent = NumberFormat.getPercentInstance();
        discountPercentLabel.setText(percent.format(discountPercent));

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        discountAmountLabel.setText(currency.format(discountAmount));
        totalLabel.setText(currency.format(total));


    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        calculateAndDisplay();
        // hide soft keyboard
        return false;



    }


}
