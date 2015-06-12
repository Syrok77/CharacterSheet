package com.paragonfervour.charactersheet.activity;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import roboguice.activity.RoboActionBarActivity;


public abstract class BaseToolbarActivity extends RoboActionBarActivity {

    public abstract Toolbar getToolbar();
    public abstract TabLayout getTabLayout();

    // Navigation functionality
}
