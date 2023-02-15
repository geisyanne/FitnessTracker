package co.tiagoaguiar.codelab.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListCalcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calc);

        Bundle extras = getIntent().getExtras();

        RecyclerView recyclerView = findViewById(R.id.rv_list);


        if (extras != null) {
            String type = extras.getString("type");

            new Thread(() -> {
                List<Register> registers = SqlHelper.getInstance(this).getRegisterBy(type);

                runOnUiThread(() -> {
                    Log.d("Teste", registers.toString());
                    ListCalcAdapter adapter = new ListCalcAdapter(registers);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(adapter);
                });
            }).start();
        }
    }

    private class ListCalcAdapter extends RecyclerView.Adapter<ListCalcAdapter.ListCalcViewHolder> {

        private List<Register> datasList;

        public ListCalcAdapter(List<Register> datasList) {
            this.datasList = datasList;
        }

        @NonNull
        @Override
        public ListCalcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ListCalcViewHolder(getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ListCalcViewHolder holder, int position) {
            Register registerCurrent = datasList.get(position);
            holder.bind(registerCurrent);
        }

        @Override
        public int getItemCount() {
            return datasList.size();
        }

        private class ListCalcViewHolder extends RecyclerView.ViewHolder {
            public ListCalcViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void bind(Register dado) {
                String formatted = "";
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "BR"));
                    Date dateSaved = sdf.parse(dado.createdDate);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", new Locale("pt", "BR"));
                    formatted = dateFormat.format(dateSaved);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ((TextView) itemView).setText(getString(R.string.list_reponse, dado.response, formatted));
            }
        }
    }
}