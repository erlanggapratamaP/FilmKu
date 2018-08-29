package software.addigma.carifilm;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import software.addigma.carifilm.Model.Film;

public class DetailFilm extends AppCompatActivity {
    public static String KEY_ITEM = "item";
    private TextView txtJudul, txtSinopsis, txtTanggal;
    private ImageView imgPoster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        Film film = getIntent().getParcelableExtra(KEY_ITEM);
        getSupportActionBar().setTitle("Detail Film");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtJudul = (TextView) findViewById(R.id.txt_detail_judul);
        txtSinopsis = (TextView)findViewById(R.id.txt_detail_sinopsis);
        txtTanggal = (TextView) findViewById(R.id.txt_detail_tanggal);
        imgPoster = (ImageView) findViewById(R.id.img_detail_poster);

        txtJudul.setText(film.getTitle());
        txtSinopsis.setText(film.getOverview());
        txtTanggal.setText(film.getRelease_data());
        Glide.with(getApplicationContext())
                .load("http://image.tmdb.org/t/p/w185"+film.getPoster_path())
                .into(imgPoster);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
