## Glide

- Glide是一个快速高效的Android图片加载库，注重平滑的滚动，Glide提供了易用的API，高性能、可扩展的图片解码管道，以及自动的资源池技术。

- Glide支持拉取，解码和展示视频快照，图片和GIF动画
- Glide的API灵活，开发者可以插入或者替换自己喜欢的网络栈，默认情况下，Glide使用的是一个定制化的HttpUrlConnection的栈，同时也提供了Volley和Okhttp快速集成的工具库

### 三级缓存

1. 活动资源

   ```java
   private EngineResource<?> loadFromMemory(
       EngineKey key, boolean isMemoryCacheable, long startTime) {
     if (!isMemoryCacheable) {
       return null;
     }
   
     EngineResource<?> active = loadFromActiveResources(key);
     // 从存活的资源中获取到图片
     // "存活的内存",存储的数据结构是hashMap
     // @VisibleForTesting final Map<Key, ResourceWeakReference> activeEngineResources = new HashMap<>();
     if (active != null) {
       if (VERBOSE_IS_LOGGABLE) {
         logWithTimeAndKey("Loaded resource from active resources", startTime, key);
       }
       return active;
     }
   ```
   
2. 内存缓存

   LruCache

   1. 存在一个LinkedHashMap存放数据，并且实现了LRU（最少使用算法）缓存策略

   2. Map<T, Y> map = new LinkedHashMap(100, 0.75f, true)
      第二个参数表示负载因子，容量达到75%的时候会把内存临时增加一倍（先把元素放进来，再去删掉前面的元素）

      第三个参数true表示按访问顺序排序，false表示按照插入顺序排序

   3. LruCache实现原理

      利用了LinkedHashMap排序方式的特点：使用访问顺序排序，进行get/put操作时元素会放在Map的最后面。所以当有一个新元素插入进来时，如果当前缓存数据大小超出了最大限制，那么会删除map前面的数据。

   ```java
     EngineResource<?> cached = loadFromCache(key);
     // 从内存缓存中获取到图片，把它从内存缓存中删掉，放到存活的资源中（activeResources）
     if (cached != null) {
       if (VERBOSE_IS_LOGGABLE) {
         logWithTimeAndKey("Loaded resource from cache", startTime, key);
       }
       return cached;
     }
   ```

3. 磁盘缓存

   DiskLruCache

   - 磁盘缓存策略
     1. DiskCacheStrategy.NONE：不缓存任何内容
     2. DiskCacheStrategy.DATA：只缓存原始图片
     3. DiskCacheStrategy.RESOURCE：只缓存转换过后的图片（命中内存缓存时，先找resource资源，再找原图）
     4. DiskCacheStrategy.ALL：既缓存原始图片，也缓存转换过后的图片
     5. DiskCacheStrategy.AUTOMATIC：让glide根据图片的资源智能地选择哪一种缓存策略（默认选项）

### Glide声明周期管理

- RequestManagerRetriever

  ```java
  private RequestManager supportFragmentGet(
      @NonNull Context context,
      @NonNull FragmentManager fm,
      @Nullable Fragment parentHint,
      boolean isParentVisible) {
      // 创建fragment
    SupportRequestManagerFragment current =
        getSupportRequestManagerFragment(fm, parentHint, isParentVisible);
    RequestManager requestManager = current.getRequestManager();
    if (requestManager == null) {
      Glide glide = Glide.get(context);
      requestManager =
          // current.getGlideLifecycle()，获得自定义生命周期fragment的lifecycle
          factory.build(
              glide, current.getGlideLifecycle(), current.getRequestManagerTreeNode(), context);
      current.setRequestManager(requestManager);
    }
    return requestManager;
  }
  
  private SupportRequestManagerFragment getSupportRequestManagerFragment(
        @NonNull final FragmentManager fm, @Nullable Fragment parentHint, boolean isParentVisible) {
      SupportRequestManagerFragment current =
          (SupportRequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
      if (current == null) {
        current = pendingSupportRequestManagerFragments.get(fm);
        if (current == null) {
          current = new SupportRequestManagerFragment();
          current.setParentFragmentHint(parentHint);
          if (isParentVisible) {
            current.getGlideLifecycle().onStart();
          }
          pendingSupportRequestManagerFragments.put(fm, current);
          // 添加一个自定义生命周期fragment到activity上
          fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
          handler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER, fm).sendToTarget();
        }
      }
      return current;
    }
  ```

  1. RequestManager实现了LifecycleListener，表示要监听声明周期
  2. RequestManagerRetriever中创建fragment，获取fragment的lifecycle
  3. RequestManager中lifecycle.addListener(this)，添加监听，完成RequestManager对页面声明周期监听

