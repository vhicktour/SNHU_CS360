package com.example.victor_udeh_m3;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText nameEditText = findViewById(R.id.nameText);
        Button sayHelloButton = findViewById(R.id.buttonSayHello);

        sayHelloButton.setEnabled(false);

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sayHelloButton.setEnabled(!charSequence.toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    public void SayHello(View view) {
        EditText nameEditText = findViewById(R.id.nameText);
        TextView greetingTextView = findViewById(R.id.textGreeting);

        String name = nameEditText.getText().toString();
        if (!name.isEmpty()) {
            greetingTextView.setText("Hello " + name);
        } else {
            greetingTextView.setText("");
        }
    }
}
