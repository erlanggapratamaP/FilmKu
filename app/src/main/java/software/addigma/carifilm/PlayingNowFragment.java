package software.addigma.carifilm;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import software.addigma.carifilm.Model.PlayingNow;
import software.addigma.carifilm.utils.FilmUtils;


public class PlayingNowFragment extends Fragment {
    private ArrayList<PlayingNow> listItems = new ArrayList<>();
    private Disposable disposable;
    static final String TAG= "PlayingNowFragment";
    RecyclerView mRecyclerView;
    PlayingNowAdapter mAdapter;


    public PlayingNowFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(getActivity());



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playing_now, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.playing_now_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getData();
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    //FETCH DATA DARI API
    @SuppressLint("CheckResult")
    public void getData(){

        Rx2AndroidNetworking.get("https://api.themoviedb.org/3/movie/now_playing?api_key=876274721e9978d826977226dc1fb9ba&language=en-US")
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

                                PlayingNow pn = new PlayingNow(
                                        f.getString(FilmUtils.FILM.TAG_ID),
                                        f.getString(FilmUtils.FILM.TAG_TITLE),
                                        f.getString(FilmUtils.FILM.TAG_OVERVIEW),
                                        f.getString(FilmUtils.FILM.TAG_REALEASE),
                                        f.getString(FilmUtils.FILM.TAG_POS)
                                );

                                listItems.add(pn);
                            }

                            mAdapter = new PlayingNowAdapter(getActivity());
                            mAdapter.setListPlayingNow(listItems);
                            mRecyclerView.setAdapter(mAdapter);

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
                                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "onError errorMessage : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {



                    }
                });
    }






}
