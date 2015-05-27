package com.paragonfervour.charactersheet.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.drawer.NavTarget;
import com.paragonfervour.charactersheet.drawer.NavigationDrawerFragment;
import com.paragonfervour.charactersheet.fragment.BioFragment;
import com.paragonfervour.charactersheet.fragment.DefenseFragment;
import com.paragonfervour.charactersheet.fragment.OffenseFragment;

import roboguice.inject.InjectView;

public class CharacterActivity extends BaseToolbarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    @InjectView(R.id.activity_toolbar)
    private Toolbar mToolbar;

    @InjectView(R.id.navigation_drawer)
    private View mNavigationDrawer;

    @InjectView(R.id.drawer_layout)
    private DrawerLayout mDrawerLayout;

    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private boolean mUserLearnedDrawer;

    private NavTarget mCurrentNavTarget = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);

        setUpDrawer(savedInstanceState);

    }

    @Override
    public void onNavigationOptionSelected(NavTarget navTarget) {
        mCurrentNavTarget = navTarget;
        mDrawerLayout.closeDrawers();
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.offense, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    private void setUpDrawer(Bundle savedInstanceState) {
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        final ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close)  /* "close drawer" description for accessibility */
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()

                if (mCurrentNavTarget != null) {
                    // update the main content by replacing fragments
                    Fragment fragment;
                    switch(mCurrentNavTarget) {
                        case OFFENSE:
                            fragment = OffenseFragment.newInstance();
                            break;
                        case DEFENSE:
                            fragment = DefenseFragment.newInstance();
                            break;
                        case BIOGRAPHY:
                            fragment = BioFragment.newInstance();
                            break;
                        default:
                            // All cases should have already been covered.
                            return;
                    }


                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();

                    mCurrentNavTarget = null;
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(CharacterActivity.this);
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                mDrawerLayout, mDrawerToggle);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && savedInstanceState == null) {
            mDrawerLayout.openDrawer(mNavigationDrawer);
        }

        onNavigationOptionSelected(NavTarget.OFFENSE);
    }

}
