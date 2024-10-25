package vn.tdc.edu.fooddelivery.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public abstract class AbstractFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public <T> T setFragment(Class<T> tClass, int layout, boolean addCurrentFragmentToBackStack) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        AbstractFragment fragment = (AbstractFragment) fragmentManager.findFragmentByTag(tClass.getSimpleName() + "");
        Log.d("fragment-manager", fragmentManager.getFragments().size() + "");
        try {
            if (fragment != null) {
                transaction.remove(fragment).commit();
            }

            fragment = (AbstractFragment) tClass.newInstance();

            transaction.replace(layout, fragment, tClass.getSimpleName() + "");

            if (fragmentManager.findFragmentByTag(tClass.getSimpleName() + "") == null) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (java.lang.InstantiationException e) {
            throw new RuntimeException(e);
        }
        return (T) fragment;
    }
}
