package com.example.glideext.lifecycle;

import java.util.ArrayList;
import java.util.List;

public class FragmentLifecycle implements Lifecycle {

    private List<LifecycleListener> lifecycleListeners = new ArrayList<>();

    @Override
    public void addListener(LifecycleListener lifecycleListener) {
        lifecycleListeners.add(lifecycleListener);
    }

    @Override
    public void removeListener(LifecycleListener lifecycleListener) {
        lifecycleListeners.remove(lifecycleListener);
    }

    public void onStart() {
        for (LifecycleListener lifecycleListener : lifecycleListeners) {
            lifecycleListener.onStart();
        }
    }

    public void onDestroy() {
        for (LifecycleListener lifecycleListener : lifecycleListeners) {
            lifecycleListener.onDestroy();
        }
    }
}
