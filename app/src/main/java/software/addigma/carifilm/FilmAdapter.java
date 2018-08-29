package software.addigma.carifilm;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import software.addigma.carifilm.Model.Film;
import software.addigma.carifilm.R;

public class FilmAdapter extends BaseAdapter {

    private ArrayList<Film> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    public FilmAdapter(Context context){
        this.context = context;
        //this.mData = films;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setData(ArrayList<Film> items){
        mData = items;
        notifyDataSetChanged();
    }
    public void addItem(final Film film){
        mData.add(film);
        notifyDataSetChanged();
    }
    public void clearData(){
        mData.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        if(mData == null) return 0;
        return mData.size();
    }

    @Override
    public Film getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.film_item, null);
            holder.imgViewPosterFilm = (ImageView)convertView.findViewById(R.id.img_poster_film);
            holder.textViewNamaFilm = (TextView)convertView.findViewById(R.id.txt_nama_film);
            holder.textViewDeskripsiFilm = (TextView)convertView.findViewById(R.id.txt_deskripsi_film);
            holder.textViewTanggalFilm = (TextView)convertView.findViewById(R.id.txt_tanggal_film);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textViewNamaFilm.setText(mData.get(position).getTitle());
        holder.textViewDeskripsiFilm.setText(mData.get(position).getOverview());
        holder.textViewTanggalFilm.setText(mData.get(position).getRelease_data());
        Glide.with(context).load("http://image.tmdb.org/t/p/w185"+mData.get(position).getPoster_path())
                .into(holder.imgViewPosterFilm);
        return convertView;
    }
    private static class ViewHolder{
        ImageView imgViewPosterFilm;
        TextView textViewNamaFilm;
        TextView textViewDeskripsiFilm;
        TextView textViewTanggalFilm;
    }
}
