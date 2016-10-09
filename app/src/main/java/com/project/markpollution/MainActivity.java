package com.project.markpollution;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.markpollution.CustomAdapter.ViewPagerAdapter;
import com.project.markpollution.Fragments.MapsFragment;
import com.project.markpollution.Fragments.NewsFeedFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    private TabLayout tabs;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();

        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);
//        setupTabIcons();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        fab.setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewsFeedFragment(), "NEWS FEED");
        adapter.addFragment(new MapsFragment(), "MAPS");
        viewPager.setAdapter(adapter);
        //====================================================== TODO: ====================
        viewPager.requestTransparentRegion(viewPager);
    }

//    private void setupTabIcons() {
//        int[] tabIcons = {
//                R.mipmap.ic_launcher,
//                R.mipmap.ic_launcher,
//        };
//
//        tabs.getTabAt(0).setIcon(tabIcons[0]);
//        tabs.getTabAt(1).setIcon(tabIcons[1]);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        drawerLayout.closeDrawer(GravityCompat.START);

        switch (id) {
            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.reportmanagement:
                Toast.makeText(getApplicationContext(), "Reportmanagement Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.seriousreport:
                Toast.makeText(getApplicationContext(), "Seriousreport Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.rencentlyreport:
                Toast.makeText(getApplicationContext(), "Rencentlyreport Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nearbyreport:
                Toast.makeText(getApplicationContext(), "Nearbyreport Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.allreport:
                Toast.makeText(getApplicationContext(), "Allreport Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_share:
                Toast.makeText(getApplicationContext(), "Share Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_logout:
                Toast.makeText(getApplicationContext(), "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
