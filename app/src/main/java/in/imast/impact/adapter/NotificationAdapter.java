package in.imast.impact.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import in.imast.impact.R;
import in.imast.impact.helper.StaticSharedpreference;
import in.imast.impact.model.NotificationModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.RecylerviewViewholder> {

    private Context context;
    private List<NotificationModel> models;

    public NotificationAdapter(Context context, List<NotificationModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public NotificationAdapter.RecylerviewViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_notification, parent, false);
        return new RecylerviewViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.RecylerviewViewholder holder, int position)
    {
        holder.timeTv.setText(models.get(position).getDate_time());
        holder.titleTv.setText(models.get(position).getTitle());
        holder.messageTv.setText(models.get(position).getDescription());

        if(!models.get(position).getImage().equalsIgnoreCase(""))
        {
            holder.img.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(models.get(position).getImage())
                    .placeholder(R.drawable.oops)
                    .into(holder.img);

        }
        else {
            holder.img.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class RecylerviewViewholder extends RecyclerView.ViewHolder
    {
        TextView timeTv, titleTv, messageTv;

        ImageView img;

        public RecylerviewViewholder(@NonNull View itemView) {
            super(itemView);
            timeTv = itemView.findViewById(R.id.timeTv);
            titleTv = itemView.findViewById(R.id.titleTv);
            messageTv = itemView.findViewById(R.id.messageTv);

            img = itemView.findViewById(R.id.img);
        }
    }


}
