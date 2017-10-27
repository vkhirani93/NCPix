package in.netcore.ncpix.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import in.netcore.ncpix.R;
import in.netcore.ncpix.fragment.GalleryFragment;
import in.netcore.ncpix.fragment.UploadFragment;
import in.netcore.ncpix.miscellaneous.Constant;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private CoordinatorLayout coordinatorLayout;
    private FrameLayout frameLayout;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private UploadFragment uploadFragment;
    private GalleryFragment galleryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main_coordinator_layout);
        frameLayout = (FrameLayout) findViewById(R.id.activity_main_frame_layout);
        toolbar = (Toolbar) findViewById(R.id.layout_toolbar_tb);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.activity_main_navigation_view);

        setUpToolbar();
        setUpDrawerToggle();
        setUpFragments();
        setUpInitFragment();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_nav_drawer_upload:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.activity_main_frame_layout, uploadFragment, Constant.FRAG_UPLOAD);
                fragmentTransaction.commit();
                break;

            case R.id.menu_nav_drawer_gallery:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.activity_main_frame_layout, galleryFragment, Constant.FRAG_GALLERY);
                fragmentTransaction.commit();
                break;
        }

        item.setChecked(true);
        drawerLayout.closeDrawers();

        return false;
    }

    private void setUpToolbar(){
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpDrawerToggle(){
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void setUpFragments(){
        fragmentManager = getSupportFragmentManager();

        uploadFragment = new UploadFragment();
        galleryFragment = new GalleryFragment();
    }

    private void setUpInitFragment(){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.activity_main_frame_layout, uploadFragment, Constant.FRAG_UPLOAD);
        fragmentTransaction.commit();

        navigationView.setCheckedItem(R.id.menu_nav_drawer_upload);
    }
}
