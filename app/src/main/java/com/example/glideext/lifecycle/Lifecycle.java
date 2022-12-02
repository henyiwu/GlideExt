package com.example.glideext.lifecycle;

public interface Lifecycle {

    void addListener(LifecycleListener lifecycleListener);

    void removeListener(LifecycleListener lifecycleListener);
}
