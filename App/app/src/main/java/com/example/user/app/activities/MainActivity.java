package com.example.user.app.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.user.app.R;
import com.example.user.app.databinding.ActivityMainBinding;
import com.example.user.app.fragments.Favourites;
import com.example.user.app.fragments.History;
import com.example.user.app.fragments.Settings;
import com.example.user.app.fragments.TranslatePage;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(activityMainBinding.toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, activityMainBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView version=(TextView) navigationView.getHeaderView(0).findViewById(R.id.version);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version.setText(packageInfo.versionName +" build "+ packageInfo.versionCode);       //вывод текущей версии и сборки

        }
        catch (PackageManager.NameNotFoundException e){
            version.setText("древность"); //если весию каким-то образом не получили, то уходим в давние времена
        }
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame, new TranslatePage()) // замена фрагментов
                .commit();
        setTitle(getString(R.string.text_translate));
    }

    /**
     * Обрабатываем кнопку Back, для того, что первое её нажатие закрывало боковую панель, если та была открыта
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty, menu);
        return true;
    }


    /**
     * Обработка нажатия на пункты меню в боковой панели
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_translate) {
          getSupportFragmentManager()
                  .beginTransaction()
                  .replace(R.id.frame, new TranslatePage())
                  .commit();
            setTitle(getString(R.string.text_translate));

        } else if (id == R.id.nav_favourites) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, new Favourites())
                    .commit();
            setTitle(getString(R.string.favourites));

        } else if (id == R.id.nav_history) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, new History())
                    .commit();
            setTitle(getString(R.string.history));

        } else if (id == R.id.nav_tools) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, new Settings())
                    .commit();
            setTitle(getString(R.string.settings));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);        //закрываем боковую панель
        return true;
    }
}
