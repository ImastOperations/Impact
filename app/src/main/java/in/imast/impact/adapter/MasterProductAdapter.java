package in.imast.impact.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.imast.impact.R;

public class MasterProductAdapter extends RecyclerView.Adapter<MasterProductAdapter.RecylerviewViewholder> {
    Activity context;
 //   private List<GetProducts.Product> arrayList;
/*

    public MasterProductAdapter(Activity mcontext, List<GetProducts.Product> arrayList) {
        context = mcontext;
        this.arrayList = arrayList;
    }
*/

    @NonNull
    @Override
    public RecylerviewViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_customer_select, parent, false);
        return new RecylerviewViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerviewViewholder holder, int position) {
        final RecylerviewViewholder recylerviewViewholder = (RecylerviewViewholder) holder;
       // holder.tvTitle.setText(arrayList.get(position).getProductName());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = (Integer)view.getTag();

               /* if(InvoicesFragment.productId.contains(arrayList.get(pos).getProductId()+"")){

                    Toast.makeText(context, "Product Already Added", Toast.LENGTH_SHORT).show();

                }else {
                    Intent intent = new Intent();
                    intent.putExtra("name", arrayList.get(pos).getProductName());
                    intent.putExtra("qty", arrayList.get(pos).getQuantity() + "");
                    intent.putExtra("code", arrayList.get(pos).getProduct_code());
                    intent.putExtra("unit", arrayList.get(pos).getProduct_unit() + "");
                    intent.putExtra("id", arrayList.get(pos).getProductId() + "");
                    intent.putExtra("value", arrayList.get(pos).getProduct_value() + "");
                    context.setResult(Activity.RESULT_OK, intent);
                    context.finish();
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return 9;
     //   return arrayList.size();
    }

    public static class RecylerviewViewholder extends RecyclerView.ViewHolder {
        TextView tvTitle;


        public RecylerviewViewholder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);


        }
    }

}
