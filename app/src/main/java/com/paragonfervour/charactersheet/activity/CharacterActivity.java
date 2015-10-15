package com.paragonfervour.charactersheet.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;
import com.paragonfervour.charactersheet.R;
import com.paragonfervour.charactersheet.character.dao.CharacterDAO;
import com.paragonfervour.charactersheet.character.helper.CharacterHelper;
import com.paragonfervour.charactersheet.character.model.CharacterInfo;
import com.paragonfervour.charactersheet.character.model.GameCharacter;
import com.paragonfervour.charactersheet.fragment.CharacterPagerFragment;
import com.paragonfervour.charactersheet.fragment.EquipmentFragment;
import com.paragonfervour.charactersheet.fragment.SpellsFragment;

import roboguice.inject.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class CharacterActivity extends BaseToolbarActivity {

    @Inject
    private CharacterDAO mCharacterDAO;

    @InjectView(R.id.activity_toolbar)
    private Toolbar mToolbar;

    @InjectView(R.id.activity_character_tab)
    private TabLayout mTabLayout;

    @InjectView(R.id.drawer_layout)
    private DrawerLayout mDrawerLayout;

    @InjectView(R.id.navigation_view)
    private NavigationView mNavigationView;

    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private CharSequence mTitle;
    private boolean mUserLearnedDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        mTitle = getTitle();

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);

        setUpDrawer(savedInstanceState);
        navigateToTarget(R.id.navigation_character_info);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }

    private class DrawerListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            navigateToTarget(menuItem.getItemId());
            mDrawerLayout.closeDrawers();
            menuItem.setChecked(true);
            return false;
        }
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mDrawerLayout.isDrawerOpen(mNavigationView)) {
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
        mDrawerToggle.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    private void setUpDrawer(Bundle savedInstanceState) {
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open,  // "open drawer" description for accessibility
                R.string.navigation_drawer_close)  // "close drawer" description for accessibility
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
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

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && savedInstanceState == null) {
            mDrawerLayout.openDrawer(mNavigationView);
        }

        mCompositeSubscription.add(mCharacterDAO.getActiveCharacter()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GameCharacter>() {
                    @Override
                    public void call(GameCharacter gameCharacter) {
                        View headerView = LayoutInflater.from(CharacterActivity.this).inflate(R.layout.drawer_header_view, mNavigationView, false);
                        CharacterInfo info = gameCharacter.getInfo();
                        TextView description = (TextView) headerView.findViewById(R.id.drawer_header_class_desc);
                        TextView characterName = (TextView) headerView.findViewById(R.id.drawer_header_character_name);
                        description.setText(String.format(headerView.getContext().getString(R.string.character_info_class_format), info.getLevel(), info.getCharacterClass()));
                        characterName.setText(info.getName());

                        mNavigationView.addHeaderView(headerView);

                        mToolbar.setTitle(CharacterHelper.getToolbarTitle(CharacterActivity.this, gameCharacter));
                    }
                }));

        mNavigationView.setNavigationItemSelectedListener(new DrawerListener());
    }

    private void navigateToTarget(int navTarget) {
        // update the main content by replacing fragments
        Fragment fragment;
        switch (navTarget) {
            case R.id.navigation_character_info:
                fragment = CharacterPagerFragment.newInstance();
                break;
            case R.id.navigation_spells:
                fragment = SpellsFragment.newInstance();
                break;
            case R.id.navigation_equipment:
                fragment = EquipmentFragment.newInstance();
                break;
            default:
                // All cases should have already been covered.
                return;
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

}
