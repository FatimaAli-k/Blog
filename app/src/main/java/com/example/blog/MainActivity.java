package com.example.blog;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.blog.controller.notification.FireBaseService;
//import com.example.blog.controller.notification.ForegroundService;
import com.example.blog.controller.notification.NotificationUtils;
import com.example.blog.controller.ui.category.CatDropDownFragment;
import com.example.blog.controller.ui.profile.ProfileActivity;
import com.example.blog.controller.WritePostActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements CatDropDownFragment.OnDataPass {

    private AppBarConfiguration mAppBarConfiguration;
    ImageView profilePic;
    TextView nameTextView,emailTextView;
    FloatingActionButton fab;
    String myId,myName;
    Boolean actLoggedIn=false;
    SharedPreferences prefs;

    private FragmentInterface fragmentInterfaceListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);


        //firebase service
        Intent serviceIntent = new Intent(this, FireBaseService.class);
        startService(serviceIntent);


//        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        mTitle.setText(getTitle());
        setSupportActionBar(toolbar);
        final boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

        prefs = getSharedPreferences("profile", Activity.MODE_PRIVATE);
        if(prefs.getString("user_id",null)!=null){
            actLoggedIn=true;
        }



       fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if(!loggedOut || actLoggedIn){
                Intent intent =new Intent(getApplicationContext(), WritePostActivity.class);
                startActivity(intent);
            }
                else {
                    Toast.makeText(getApplicationContext(),R.string.must_login_to_post,Toast.LENGTH_SHORT).show();
                }

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_bloggers,
                R.id.nav_categories, R.id.nav_register, R.id.nav_login, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//


        if (!loggedOut) {
//            Picasso.with(this).load(Profile.getCurrentProfile().getProfilePictureUri(200, 200)).into(imageView);
//            Log.d("TAG", "Username is: " + Profile.getCurrentProfile().getName());


            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            //Using Graph API
            try {

                getUserProfile(AccessToken.getCurrentAccessToken());
            }catch (Exception e){}


        }


//        NavGraph navGraph;
//        navGraph = navController.getGraph();
////        navGraph.setStartDestination(R.id.nav_gallery);
//        navController.setGraph(navGraph);
//        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
//            @Override
//            public void onDestinationChanged(@NonNull NavController controller,
//                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
//                if(destination.getId() == R.id.nav_logout) {
//
////                    LoginManager.getInstance().logOut();
////                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
////                    startActivity(intent);
//                    Toast.makeText(getApplicationContext(),"weeeeeeeeeeeeeeeeeeeeee",Toast.LENGTH_LONG).show();
//                }
//                else
//                if(destination.getId() == R.id.nav_categories) {
//
//                    Toast.makeText(getApplicationContext(),"hhhhhhommeeeeeeeeeeeeeeeeeeeeee",Toast.LENGTH_LONG).show();
//
//
//                }
//            }
//        });



        View headerView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        profilePic = headerView.findViewById(R.id.profilePic);
        nameTextView=headerView.findViewById(R.id.nameTextView);
        emailTextView=headerView.findViewById(R.id.emailTextView);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to profile
                if(!loggedOut) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("user_id", Profile.getCurrentProfile().getId());
                    startActivity(intent);
//                navController.navigate(R.id.nav_profile);
                }
                else if(actLoggedIn){
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("user_id",prefs.getString("user_id",null));
                    startActivity(intent);

                }

            }
        });


        if(actLoggedIn){
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);

            String img=prefs.getString("profile_pic",null);
            if(img != null)
            Picasso.with(MainActivity.this).load(img).into(profilePic);

            nameTextView.setText(prefs.getString("user_name","guest"));
        }
        else{
            nameTextView.setText("guest");
        }



//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                if(menuItem.getItemId() == R.id.nav_categories) {
////
////                    LoginManager.getInstance().logOut();
////                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
////                    startActivity(intent);
//
//
//                    Toast.makeText(getApplicationContext(),"weeeeeeeeeeeeeeeeeeeeee",Toast.LENGTH_LONG).show();
//                    return true;
//
//                }
//                return false;
//            }
//        });
//
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

//    public boolean onNavigationItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        Fragment fragment = null;
//        Bundle bundle = new Bundle();
//        if (id == R.id.nav_home) {
//            fragment = new HomeFragment();
//        } else if (id == R.id.nav_share) {
//            fragment = new ProfileFragment();
//        }
//
//            if (fragment != null) {
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.nav_host_fragment, fragment);
//                ft.commit();
//            }
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
//            return true;
//        }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", ""+response);
                        if(response.getError() == null)
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
//                            Toast.makeText(getApplicationContext(),first_name+" "+email+" , "+id,Toast.LENGTH_LONG).show();

                            nameTextView.setText(first_name +" "+last_name);
//                            emailTextView.setText(email);
//                            txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
//                            txtEmail.setText(email);
                            Picasso.with(MainActivity.this).load(image_url).into(profilePic);
//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();


    }

    public FloatingActionButton getFloatingActionButton() {
        return fab;
    }


    @Override
    public void onDataPass(int data) {

        fragmentInterfaceListener.sendData(data);
    }

    public interface FragmentInterface{
        void sendData(int data);
    }
    public void setOnDataListener(FragmentInterface fragmentInterface){
        fragmentInterfaceListener=fragmentInterface;
    }

}
