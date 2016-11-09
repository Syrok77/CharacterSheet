package com.paragonfervour.charactersheet.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.paragonfervour.charactersheet.component.Component;

import java.util.ArrayList;
import java.util.List;


public abstract class ComponentBaseActivity extends ActionBarActivity {

    private final List<Component> mComponents = new ArrayList<>();

    protected void add(Component component) {
        mComponents.add(component);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (Component c : mComponents) {
            c.onCreate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Component c : mComponents) {
            c.onDestroy();
        }
    }

}
