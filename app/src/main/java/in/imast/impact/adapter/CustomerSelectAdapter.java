package in.imast.impact.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.imast.impact.R;
import in.imast.impact.model.CustomerModel;

public class CustomerSelectAdapter extends RecyclerView.Adapter<CustomerSelectAdapter.RecylerviewViewholder> {
    Activity context;
    private ArrayList<CustomerModel.Customer> arrayList;

    public CustomerSelectAdapter(Activity mcontext, ArrayList<CustomerModel.Customer> arrayList) {

        context = mcontext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecylerviewViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_customer_select, parent, false);
        return new RecylerviewViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerviewViewholder holder, int position) {
        final RecylerviewViewholder recylerviewViewholder = (RecylerviewViewholder) holder;
        holder.tvTitle.setText(arrayList.get(position).getFirm_name());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (Integer)view.getTag();
                Intent intent = new Intent();
                intent.putExtra("firmName",arrayList.get(pos).getFirm_name());
                intent.putExtra("sellerId",arrayList.get(pos).getId()+"");
                intent.putExtra("address",arrayList.get(pos).getCustomer_address());
                intent.putExtra("mobile",arrayList.get(pos).getCustomer_mobile_number());
                intent.putExtra("email",arrayList.get(pos).getCustomer_email()+"");
                context.setResult(Activity.RESULT_OK,intent);
                context.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecylerviewViewholder extends RecyclerView.ViewHolder {
        TextView tvTitle;


        public RecylerviewViewholder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);


        }
    }

    public void filterList(ArrayList<CustomerModel.Customer> arrayListFilter) {
        arrayList = arrayListFilter;
        notifyDataSetChanged();
    }

}
