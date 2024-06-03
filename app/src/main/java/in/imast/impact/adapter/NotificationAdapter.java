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

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.RecylerviewViewholder> {
    Context context;
    private ArrayList<NotificationModel> arrayList;
    public NotificationAdapter(Context mcontext, ArrayList<NotificationModel> arrayList){

        context = mcontext;
        this.arrayList = arrayList;
}

    @NonNull
    @Override
    public RecylerviewViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_notification, parent, false);
        return new RecylerviewViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerviewViewholder holder, int position) {
       final RecylerviewViewholder recylerviewViewholder = (RecylerviewViewholder)holder;
        holder.tvMessage.setText( arrayList.get(position).getMessage());
        holder.tvTime.setText( arrayList.get(position).getTime());
        holder.tvTitle.setText( arrayList.get(position).getTitle());

        if( !arrayList.get(position).getBitmap().equals("")) {

            holder.img.setVisibility(View.VISIBLE);
            Drawable mDefaultBackground = context.getResources().getDrawable(R.drawable.oops);
            Glide
                    .with(context)
                    .load(arrayList.get(position).getBitmap()
                    ).error(mDefaultBackground).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    /* myViewHolder.gif.setVisibility(View.INVISIBLE);*/
                    return false;
                }
            })
                    .into(holder.img);
        }else{
            holder.img.setVisibility(View.GONE);
        }


        holder.imgDelete.setTag(position);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos  = (Integer) view.getTag();

                alertDialogMsg(context,"Do you want to delete notification?","Delete",pos);
            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecylerviewViewholder extends RecyclerView.ViewHolder {
       TextView tvMessage,tvTime,tvTitle;
       ImageView imgDelete,img;


        public RecylerviewViewholder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            img = itemView.findViewById(R.id.img);

        }
    }

    public void alertDialogMsg(final Context context, String message, String title,final int pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        arrayList.remove(pos);

                        String json2 = StaticSharedpreference.getInfo("notification", context);
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<NotificationModel>>() {
                        }.getType();
                        ArrayList<NotificationModel> arrayList = gson.fromJson(json2, type);

                        arrayList.remove(pos);
                        Gson gson1 =new Gson();
                        String specialization = gson1.toJson(arrayList);

                        StaticSharedpreference.saveInfo("notification", specialization, context);

                        notifyDataSetChanged();

                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(title);
        alert.show();

    }


}
