package software.addigma.carifilm;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import software.addigma.carifilm.Model.Film;
import software.addigma.carifilm.utils.FilmUtils;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    ListView listView;
    FilmAdapter mAdapter;
    EditText txtCari;
    Button btnCari;
    private ArrayList<Film> listItems = new ArrayList<>();
    private Disposable disposable;
    static final String TAG= "MainActivity";
    static final String EXTRAS_FILM = "EXTRAS_FILM";
    ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle("Search Movie");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //fetching init with rxjava2 and FAN
        AndroidNetworking.initialize(getApplicationContext());
        mAdapter = new FilmAdapter(this);
        mAdapter.notifyDataSetChanged();

        mProgress = new ProgressDialog(this);
        listView = (ListView)findViewById(R.id.listview);
        txtCari = (EditText)findViewById(R.id.edit_txt_film);
        btnCari = (Button) findViewById(R.id.btn_cari_film);
        btnCari.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Film flm = (Film) listView.getItemAtPosition(position);

                Intent i = new Intent(SearchActivity.this, DetailFilm.class);
                i.putExtra(DetailFilm.KEY_ITEM, flm);
                startActivityForResult(i, 0);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cari_film:
                getData();
                deleteData();

        }
    }



    @SuppressLint("CheckResult")
    public void getData(){
        mProgress.setMessage("Please Wait");
        mProgress.show();
        String cari = txtCari.getText().toString();
        Rx2AndroidNetworking.get("https://api.themoviedb.org/3/search/movie?api_key=876274721e9978d826977226dc1fb9ba&language=en-US&query={key}")
                .addPathParameter("key", cari)
                .build()
                .getStringObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(String s) {
                        try{
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray(FilmUtils.FILM.TAG_RESULT);
                            for(int i=0; i<array.length();i++){
                                JSONObject f = array.getJSONObject(i);

                                Film film = new Film(
                                    f.getString(FilmUtils.FILM.TAG_ID),
                                        f.getString(FilmUtils.FILM.TAG_TITLE),
                                        f.getString(FilmUtils.FILM.TAG_OVERVIEW),
                                        f.getString(FilmUtils.FILM.TAG_REALEASE),
                                        f.getString(FilmUtils.FILM.TAG_POS)
                                );

                                listItems.add(film);
                            }

                            mAdapter.setData(listItems);
                            listView.setAdapter(mAdapter);

                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ANError) {
                            ANError anError = (ANError) e;
                            if (anError.getErrorCode() != 0) {
                                // received ANError from server
                                // error.getErrorCode() - the ANError code from server
                                // error.getErrorBody() - the ANError body from server
                                // error.getErrorDetail() - just a ANError detail
                                Log.d(TAG, "onError errorCode : " + anError.getErrorCode());
                                Log.d(TAG, "onError errorBody : " + anError.getErrorBody());
                                Log.d(TAG, "onError errorDetail : " + anError.getErrorDetail());
                                // get parsed error object (If ApiError is your class)
                                //Film film = error.getErrorAsObject(Film.class);
                            } else {
                                // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                Log.d(TAG, "onError errorDetail : " + anError.getErrorDetail());
                            }
                        } else {
                            Log.d(TAG, "onError errorMessage : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        mProgress.dismiss();


                    }
                });
    }
    private void deleteData(){
        for(int i= listItems.size()-1; i>=0; i--){
           listItems.remove(listItems.get(i));
        }
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
