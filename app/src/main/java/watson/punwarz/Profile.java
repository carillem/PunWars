package watson.punwarz;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import watson.punwarz.ImageView.RoundedImageView;
import watson.punwarz.ListView.CustomAdapter;
import watson.punwarz.ListView.ListModel;
import watson.punwarz.ListView.PunAdapter;
import watson.punwarz.ListView.PunModel;

import com.facebook.FacebookSdk;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Author: Carille
 * Created: 2016-03-08
 * Desc: Using inheritance, takes after page and will display user, the user's points, rank, themes created, and puns submitted.
 */
public class Profile extends Page
{
    private TextView name;
    private TextView points;
    private TextView rankText;
    private RoundedImageView profilePic;
    private com.facebook.Profile profile;
    private ParseApplication parse;
    private int userPoints;
    private String userID;

    ListView themeList;
    ListView punList;
    CustomAdapter themeAdapter;
    PunAdapter punAdapter;

    public Profile CustomListView = null;
    public ArrayList<ListModel> CustomListViewValuesArrTheme = new ArrayList<ListModel>();
    public ArrayList<PunModel> CustomListViewValuesArrPun = new ArrayList<PunModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);

        //Set textViews here
        name = (TextView)findViewById(R.id.name);
        points = (TextView)findViewById(R.id.points);
        rankText = (TextView)findViewById(R.id.rank);
        profilePic = (RoundedImageView)findViewById(R.id.profilePic);


        setSupportActionBar(toolbar);
        profile = com.facebook.Profile.getCurrentProfile();
        userID = profile.getId();
        parse = new ParseApplication();

        CustomListView = this;

        setName();
        setPoints();
        setRank();
        setProfilePic();

        setThemeList();

        setPunList();

        Resources res = getResources();
        themeList = ( ListView )findViewById( R.id.user_themes_list );
        punList = ( ListView )findViewById( R.id.user_puns_list );

        themeAdapter = new CustomAdapter( CustomListView, CustomListViewValuesArrTheme, res);
        themeList.setAdapter( themeAdapter );

        punAdapter = new PunAdapter( CustomListView, CustomListViewValuesArrPun, res);
        punList.setAdapter( punAdapter );
    }

    private void setName(){
        name.setText(profile.getName());
    }

    private void setPoints(){
        userPoints = parse.getUserPoints(profile.getId());
        points.setText("Points: " + Integer.toString(userPoints));
    }

    private void setRank(){
        String rank;

        if (userPoints < 30){
            rank = "Punching Bag";
        }
        else if (userPoints < 60){
            rank = "Pun'kd";
        }
        else if (userPoints < 100){
            rank = "Depundable";
        }
        else if (userPoints < 140){
            rank = "Pungent";
        }
        else if (userPoints < 190){
            rank = "Puntagon";
        }
        else if (userPoints < 240){
            rank = "Punny";
        }
        else if (userPoints < 300){
            rank = "Punctual";
        }
        else if (userPoints < 360){
            rank = "Puntastic";
        }
        else if (userPoints < 430){
            rank = "Punchier";
        }
        else if (userPoints < 500){
            rank = "Punderful";
        }
        else if (userPoints < 580){
            rank = "Cyberpun";
        }
        else {
            rank = "Punisher";
        }

        rankText.setText("Rank: " + rank);
    }

    private void setProfilePic(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = profile.getProfilePictureUri(150, 150);

                try{
                    URL facebookProfileURL = new URL(uri.toString());
                    final Bitmap bitmap = BitmapFactory.decodeStream(facebookProfileURL.openConnection().getInputStream());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            profilePic.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setThemeList(){
        ArrayList<ArrayList<String>> themes = parse.getUserThemes(userID);

        for (int i = 0; i < themes.size(); i++) {
            ArrayList<String> current = themes.get(i);
            final ListModel sched = new ListModel();

                sched.setLobbyTitle(current.get(0));
                sched.setExpireDate(current.get(1));
                sched.setLobbyDes(current.get(2));
                sched.setTopPun(current.get(3));

            CustomListViewValuesArrTheme.add( sched );
        }
    }

    private void setPunList(){
        ArrayList<ArrayList<String>> puns = parse.getUserPuns(userID);

        for (int i = 0; i < puns.size(); i++) {
            ArrayList<String> current = puns.get(i);
            final PunModel sched = new PunModel();

                sched.setPun(current.get(0));
                sched.setPunVotes(current.get(1));
                sched.setThemeID(current.get(2));

            CustomListViewValuesArrPun.add( sched );
        }
    }

}
