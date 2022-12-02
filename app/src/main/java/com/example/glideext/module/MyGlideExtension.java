package com.example.glideext.module;

import android.graphics.Bitmap;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.annotation.GlideType;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.glideext.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

@GlideExtension
public class MyGlideExtension {

    private MyGlideExtension() {

    }

    @GlideOption
    public static BaseRequestOptions<?> applyAvatar(BaseRequestOptions<?> options, int size) {

        BlurTransformation transformation = new BlurTransformation(25, 1);

        return options.placeholder(R.mipmap.empty_placeholder) // 加载中的占位图
                .error(R.mipmap.error_placeholder) // 出错时的图片
                .override(size)
                .centerCrop()
                .transform(new MultiTransformation<>(transformation, new CircleCrop()));
    }

//    private static final RequestOptions DECODE_TYPE_GIF =
//            GlideOptions.decodeTypeOf(GifDrawable.class).lock();
//
//    @GlideType(GifDrawable.class)
//    public static RequestBuilder<GifDrawable> asGif(RequestBuilder<GifDrawable> requestBuilder) {
//        return requestBuilder.transition(new DrawableTransitionOptions())
//                .apply(DECODE_TYPE_GIF);
//    }

    /**
     * 封装淡入效果
     */
    @GlideType(Bitmap.class)
    public static RequestBuilder<Bitmap> fade(RequestBuilder<Bitmap> requestBuilder) {
        return requestBuilder.transition(BitmapTransitionOptions.withCrossFade());
    }
}
