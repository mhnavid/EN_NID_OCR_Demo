package nvd.hasan.ennidocrdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button oldNidBtn, newNidBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oldNidBtn = findViewById(R.id.oldNIDBtn);
        newNidBtn = findViewById(R.id.newNIDBtn);

        oldNidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, OldNidProcess.class);
                startActivity(in);
            }
        });

        newNidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, NewNidProcess.class);
                startActivity(in);
            }
        });
    }
}
