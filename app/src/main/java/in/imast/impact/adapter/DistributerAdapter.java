package in.imast.impact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import in.imast.impact.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import in.imast.impact.Connection.InvoiceInterface;
import in.imast.impact.model.DistributerModal;

public class DistributerAdapter extends RecyclerView.Adapter<DistributerAdapter.RecylerviewViewholder> {
    private final InvoiceInterface buttonListener;
    Context context;
    private ArrayList<DistributerModal> arrayList;
    private String get_idd;
    private String get_name;

    public DistributerAdapter(Context mcontext, ArrayList<DistributerModal> arrayList, InvoiceInterface buttonListener) {
        context = mcontext;
        this.arrayList = arrayList;
        this.buttonListener = buttonListener;
    }

    @NonNull
    @Override
    public RecylerviewViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.spinner_adapter, parent, false);
        return new RecylerviewViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerviewViewholder holder, int position) {
        final RecylerviewViewholder recylerviewViewholder = (RecylerviewViewholder) holder;
        holder.tvTime.setText(arrayList.get(position).getName());

        holder.text_view1.setOnClickListener(v -> {
            get_idd = arrayList.get(position).getId();
            get_name = arrayList.get(position).getName();

            buttonListener.foo(get_idd, get_name);
            //dialog.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        if(!arrayList.isEmpty())
        {
            return arrayList.size();
        }
        else {
            return 0;
        }

    }

    public static class RecylerviewViewholder extends RecyclerView.ViewHolder {
        RelativeLayout text_view1;
        TextView tvTime;


        public RecylerviewViewholder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.text_view);
            text_view1 = itemView.findViewById(R.id.text_view1);

        }
    }
}
