package in.imast.impact.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import in.imast.impact.R;
import in.imast.impact.fragment.InvoicesFragment;
import in.imast.impact.model.ProductModal;


public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<ProductModal> product_list;
    private Activity context;

    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;
    private String Day_Closed_;
    private String View_Status;
    ProductModal productModal;

    public PaginationAdapter(Activity context, ArrayList<ProductModal> product_list) {
        this.context = context;
        this.product_list = product_list;
        productModal = new ProductModal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.adapter_customer_select, parent, false);
                viewHolder = new MovieViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
                movieViewHolder.tvTitle.setText(product_list.get(position).getProductName());


                movieViewHolder.cardMain.setOnClickListener(view ->{
                    if(InvoicesFragment.productId.contains(product_list.get(position).getProductId()+"")){

                        Toast.makeText(context, "Product Already Added", Toast.LENGTH_SHORT).show();

                    }else {
                        Intent intent = new Intent();
                        intent.putExtra("name", product_list.get(position).getProductName());
                        intent.putExtra("qty", product_list.get(position).getProductQuantity() + "");
                        intent.putExtra("code", product_list.get(position).getProductCode());
                        intent.putExtra("unit", product_list.get(position).getProductUnit() + "");
                        intent.putExtra("id", product_list.get(position).getProductId() + "");
                        intent.putExtra("value", product_list.get(position).getProductValue() + "");
                        context.setResult(Activity.RESULT_OK, intent);
                        context.finish();
                    }
                });



                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                Log.e("size>>", "" + product_list.size());
                if (product_list.size() == 1) {
                    loadingViewHolder.progressBar.setVisibility(View.GONE);
                } else {

                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return product_list == null ? 0 : product_list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == product_list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(productModal);
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = product_list.size() - 1;
        ProductModal result = getItem(position);

        if (result != null) {
            product_list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(ProductModal movie) {
        product_list.add(movie);
        notifyItemInserted(product_list.size() - 1);
    }

    public void addAll(List<ProductModal> moveResults) {
        for (ProductModal result : moveResults) {
            add(result);
        }
    }

    public void remove(ProductModal city) {
        int position = product_list.indexOf(city);
        if (position > -1) {
            product_list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ProductModal getItem(int position) {
        return product_list.get(position);
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private LinearLayout cardMain;

        public MovieViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            cardMain = (LinearLayout) itemView.findViewById(R.id.cardMain);

        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);

        }
    }
}
