package com.paragonfervour.charactersheet.injection;


import android.util.SparseArray;

import java.util.LinkedList;

public class ScopeStack<Scope, Component> {

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private final SparseArray<Component> mActivityComponents = new SparseArray<>();
    private final LinkedList<Integer> mScopeIdStack = new LinkedList<>();

    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------

    /**
     * Get the most component for the most recently seen scope instance.
     *
     * @return the component.
     */
    public Component getTop() {
        if (!mScopeIdStack.isEmpty()) {
            Integer id = mScopeIdStack.getFirst();
            return mActivityComponents.get(id);
        }
        return null;
    }

    /**
     * Get a component for a given scope instance.
     *
     * @param scopeInstance the scope instance.
     * @return the component.
     */
    public Component getComponent(Scope scopeInstance) {
        int id = System.identityHashCode(scopeInstance);
        setScopeTop(scopeInstance);
        return mActivityComponents.get(id);
    }

    /**
     * Create a component for a specified scope instance.
     *
     * @param scopeInstance the scope instance.
     * @param factory       the component factory.
     * @return the created component.
     */
    public Component createComponentForScope(Scope scopeInstance, ComponentFactory<Component> factory) {
        int id = System.identityHashCode(scopeInstance);
        setScopeTop(scopeInstance);
        Component component = getComponent(scopeInstance);
        if (component == null) {
            component = factory.createComponent();
            mActivityComponents.put(id, component);
        }
        return component;
    }

    /**
     * Remove the component for a given scope instance.
     *
     * @param scopeInstance the scope instance.
     */
    public void releaseComponent(Scope scopeInstance) {
        Integer id = System.identityHashCode(scopeInstance);
        mActivityComponents.remove(id);
        mScopeIdStack.remove(id);
    }

    //endregion

    //region PRIVATE METHODS -----------------------------------------------------------------------

    /**
     * Set the most recently seen scope as the Top.
     *
     * @param scopeInstance the scope instance.
     */
    private void setScopeTop(Scope scopeInstance) {
        Integer id = System.identityHashCode(scopeInstance);
        mScopeIdStack.remove(id);
        mScopeIdStack.addFirst(id);
    }

    //endregion

    //region OBSERVERS -----------------------------------------------------------------------------
    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------

    public interface ComponentFactory<T> {
        T createComponent();
    }

    //endregion
}
