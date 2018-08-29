package software.addigma.carifilm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import software.addigma.carifilm.Model.UpComing;
import software.addigma.carifilm.utils.CustomOnItemClickListener;

public class UpComingAdapter extends RecyclerView.Adapter<UpComingAdapter.UpComingViewHolder>{
    private ArrayList<UpComing> listUpComing;
    private Context context;

    UpComingAdapter(Context context){
        this.context = context;
    }
    private ArrayList<UpComing> getListUpComing(){
        return listUpComing;
    }
    void setListUpComing(ArrayList<UpComing> listUpComing){
        this.listUpComing = listUpComing;
    }

    @NonNull
    @Override
    public UpComingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_playing_now, parent, false);
        return new UpComingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpComingViewHolder holder, int position) {
        UpComing uc = getListUpComing().get(position);
        Glide.with(context)
                .load("http://image.tmdb.org/t/p/w185"+getListUpComing().get(position).getPoster_path())
                .into(holder.imgPhoto);
        holder.tvName.setText(getListUpComing().get(position).getTitle());
        holder.tvDesc.setText(getListUpComing().get(position).getOverview());
        holder.tvDate.setText(getListUpComing().get(position).getRelease_data());

        holder.btnFavorite.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Toast.makeText(context, "Detail "+getListUpComing().get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        }));

        holder.btnShare.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Toast.makeText(context, "Share "+getListUpComing().get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        }));

    }

    @Override
    public int getItemCount() {
        return getListUpComing().size();
    }
    class UpComingViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPhoto;
        TextView tvName,tvDesc,tvDate;
        Button btnFavorite, btnShare;
        UpComingViewHolder(View itemView){
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo_playingnow);
            tvName = itemView.findViewById(R.id.tv_item_name_playingnow);
            tvDesc = itemView.findViewById(R.id.tv_item_description_playingnow);
            tvDate = itemView.findViewById(R.id.tv_item_date_playingnow);
            btnFavorite = itemView.findViewById(R.id.btn_set_favorite_playingnow);
            btnShare = itemView.findViewById(R.id.btn_set_share_playingnow);
        }
    }
}
