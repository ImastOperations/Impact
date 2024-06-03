package in.imast.impact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import in.imast.impact.R;
import in.imast.impact.fragment.InvoicesFragment;
import in.imast.impact.model.ProductSelectedModel;

public class MasterSelectedProductAdapter extends RecyclerView.Adapter<MasterSelectedProductAdapter.RecylerviewViewholder> {
    Context context;
    private List<ProductSelectedModel> arrayList;

    public MasterSelectedProductAdapter(Context mcontext, List<ProductSelectedModel> arrayList, EventListener listener) {
        context = mcontext;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    EventListener listener;

    public interface EventListener {
        void onEvent(int data);
    }

    @NonNull
    @Override
    public RecylerviewViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_product_select, parent, false);
        return new RecylerviewViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerviewViewholder holder, int position) {
        final RecylerviewViewholder recylerviewViewholder = (RecylerviewViewholder) holder;
        holder.tvTitle.setText(arrayList.get(position).getName());
        holder.tvRate.setText(Integer.parseInt(arrayList.get(position).getRate())*
                Integer.parseInt(arrayList.get(position).getQty())+"");


        holder.tvRateDetail.setText(arrayList.get(position).getQty()+" x "+arrayList.get(position).getRate());

        holder.cardMain.setTag(position);

        holder.cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (Integer)view.getTag();

                listener.onEvent(pos);

            }
        });
        holder.relativeDelete.setTag(position);
        holder.relativeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (Integer)view.getTag();

                InvoicesFragment.productId.remove(pos);
                arrayList.remove(pos);
                notifyDataSetChanged();

                int rate = 0, qty = 0;
                for (int i = 0; i < arrayList.size(); i++) {

                    rate = rate + (Integer.parseInt(arrayList.get(i).getQty()) *
                            Integer.parseInt(arrayList.get(i).getRate()));

                    qty = qty + Integer.parseInt(arrayList.get(i).getQty());

                }

                InvoicesFragment.tvTotalQty.setText(qty + "");
                InvoicesFragment.tvTotalRate.setText(rate + "");

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecylerviewViewholder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvRate,tvRateDetail;
        RelativeLayout relativeDelete;
        LinearLayout cardMain;
        public RecylerviewViewholder(@NonNull View itemView) {
            super(itemView);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvRateDetail = itemView.findViewById(R.id.tvRateDetail);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            relativeDelete = itemView.findViewById(R.id.relativeDelete);
            cardMain = itemView.findViewById(R.id.cardMain);


        }
    }

}
