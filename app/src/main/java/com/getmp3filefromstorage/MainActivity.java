package com.getmp3filefromstorage;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int MY_PERMISSION_REQUEST = 1;

    @BindView(R.id.header)
    CustomHeader header;
    @BindView(R.id.listView)
    RecyclerView listView;
    SoundAdapter soundAdapter = null;
    public ArrayList<SoundModel> objList = null;


    private MediaPlayer player = null;
    private int lastPos1 = -1;
    Player playerAsync =null;
    int lastPos = 0;
    int previousPos = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        header.txtCenterTitle.setText(getResources().getString(R.string.name));
        header.txtCenterTitle.setVisibility(View.VISIBLE);
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager
                .PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }else {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }

        }else {
            doStuff();

        }


    }

    public void doStuff(){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        soundAdapter = new SoundAdapter(MainActivity.this);
        listView.setAdapter(soundAdapter);
        getMusic();


    }
    public void getMusic(){
        objList = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.d("uriiiiiiiiiiiiiiiiiiii",songUri+"");
        Cursor songCursor = contentResolver.query(songUri,null,null,null,null);
        if (songCursor!=null && songCursor.moveToFirst()){
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLoc = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);


            do{
                SoundModel soundModel = new SoundModel();
                soundModel.title = songCursor.getString(songTitle);
                soundModel.artist = songCursor.getString(songArtist);
                soundModel.location = songCursor.getString(songLoc);
                objList.add(soundModel);

            }while (songCursor.moveToNext());
            soundAdapter.addData(objList);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
            switch (requestCode){
                case MY_PERMISSION_REQUEST:{
                    if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"permission granted!",Toast.LENGTH_SHORT).show();

                            doStuff();
                        }
                    }else {
                        Toast.makeText(this,"permission Denied!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    return;
                }
            }
        }

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            // TODO Auto-generated method stub
            soundAdapter.objList.get(lastPos).isPlaying = false;
            if (player != null) {
                player.release();
                player = null;
            }
            soundAdapter.notifyDataSetChanged();

        }
    };
    @OnClick({})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPlay:
                lastPos = Integer.parseInt(view.getTag(R.string.app_name).toString());



                if (lastPos != previousPos && previousPos != -1) {

                    soundAdapter.objList.get(previousPos).isPlaying = false;

                    soundAdapter.notifyDataSetChanged();
                    if (player != null ) {
                        player.release();
                        player = null;

                    }
                }

                previousPos = lastPos;

                if (soundAdapter.objList.get(lastPos).isPlaying) {


                    soundAdapter.objList.get(lastPos).isPlaying = false;

                    if (player != null) {
                        player.release();
                        player = null;
                    }
                    soundAdapter.notifyDataSetChanged();

                } else {


                    soundAdapter.objList.get(lastPos).isPlaying = true;
                    soundAdapter.notifyDataSetChanged();



                    if (playerAsync != null && playerAsync.getStatus() != AsyncTask.Status.FINISHED)
                        playerAsync.cancel(true);
                    playerAsync = new Player();

                    playerAsync.execute(soundAdapter.objList.get(lastPos).location);

                }




                break;



        }

    }



    //try to media


    class Player extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub


            /*if (player != null) {
                Utils.print("==================PLAER RELESE >> " );
                player.release();

            }*/
            if (isCancelled()) {

                return null;
            }

            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {





                    player.setDataSource(params[0]); //soundAdapter.objList.get(lastPos).Uri

                player.setOnCompletionListener(onCompletionListener);
                player.prepare();

                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        soundAdapter.notifyDataSetChanged();
                        player.start();
                    }
                });
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {


                        soundAdapter.objList.get(previousPos).isPlaying = false;

                        if (player != null) {
                            player.stop();
                            player.release();
                        }
                        soundAdapter.notifyDataSetChanged();
                    }
                });

            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block


                Log.d("IllegarArgument", e.getMessage());
                e.printStackTrace();
            } catch (SecurityException e) {


                e.printStackTrace();
            } catch (IllegalStateException e) {


                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }




            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
           /* if (progress.isShowing()) {
                progress.cancel();
            }*/


            Log.d("Prepared", "//" + result);

        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();




//            this.progress.setMessage("Buffering...");
//            this.progress.show();

        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (player != null)
        {
            try {
                player.release();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
}
