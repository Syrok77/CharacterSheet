package com.paragonfervour.charactersheet.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paragonfervour.charactersheet.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that contains a list of Components and forwards in lifecycle events.
 */
public class ComponentBaseFragment extends Fragment {

    private final List<Component> mComponents = new ArrayList<>();

    protected void add(Component component) {
        mComponents.add(component);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        for (Component c : mComponents) {
            c.onAttach();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (Component c : mComponents) {
            c.onCreate();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        for (Component c : mComponents) {
            c.onCreateView();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (Component c : mComponents) {
            c.onViewCreated();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (Component c : mComponents) {
            c.onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Component c : mComponents) {
            c.onDestroy();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        for (Component c : mComponents) {
            c.onDetached();
        }
    }
}
