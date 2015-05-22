package com.paragonfervour.charactersheet.activity;

import android.support.v7.widget.Toolbar;

import roboguice.activity.RoboFragmentActivity;


public abstract class BaseToolbarActivity extends RoboFragmentActivity {

    public abstract Toolbar getToolbar();
}
