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
import in.imast.impact.model.DemoModal;


public class PaginationAdapterDemo extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity context;
    private ArrayList<DemoModal.CustomerInfo> movieList;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;

    public PaginationAdapterDemo(Activity context, ArrayList<DemoModal.CustomerInfo> movieList) {
        this.context = context;
        this.movieList = new ArrayList<>();
    }

    public void setMovieList(ArrayList<DemoModal.CustomerInfo> movieList) {
        this.movieList = movieList;
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
                movieViewHolder.movieTitle.setText(movieList.get(position).getFirmName());

                movieViewHolder.cardMain.setOnClickListener(view ->{
                    if(InvoicesFragment.productId.contains(movieList.get(position).getId()+"")){

                        Toast.makeText(context, "Product Already Added", Toast.LENGTH_SHORT).show();

                    }else {
                        Intent intent = new Intent();
                        intent.putExtra("name", movieList.get(position).getFirmName());
                        intent.putExtra("qty", movieList.get(position).getProductQuanty() + "");
                        intent.putExtra("code", movieList.get(position).getCode());
                        intent.putExtra("unit", movieList.get(position).getUnit() + "");
                        intent.putExtra("id", movieList.get(position).getId() + "");
                        intent.putExtra("value", movieList.get(position).getValue() + "");
                        context.setResult(Activity.RESULT_OK, intent);
                        context.finish();
                    }
                });

                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                Log.e("size>>", "" + movieList.size());
                if (movieList.size() == 1) {

                    loadingViewHolder.progressBar.setVisibility(View.GONE);
                } else {
                    loadingViewHolder.progressBar.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new DemoModal.CustomerInfo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieList.size() - 1;
        DemoModal.CustomerInfo result = getItem(position);

        if (result != null) {
            movieList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(DemoModal.CustomerInfo movie) {
        movieList.add(movie);
        notifyItemInserted(movieList.size() - 1);
    }

    public void addAll(List<DemoModal.CustomerInfo> moveResults) {
        for (DemoModal.CustomerInfo result : moveResults) {
            add(result);
        }
    }

    public void remove(DemoModal.CustomerInfo city) {
        int position = movieList.indexOf(city);
        if (position > -1) {
            movieList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public DemoModal.CustomerInfo getItem(int position) {
        return movieList.get(position);
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView movieTitle;
        private LinearLayout cardMain;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieTitle = (TextView) itemView.findViewById(R.id.tvTitle);
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
