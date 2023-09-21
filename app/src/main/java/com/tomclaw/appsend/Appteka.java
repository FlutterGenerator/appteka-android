package com.tomclaw.appsend;

import static java.util.Collections.singletonList;

import android.app.Application;

import com.tomclaw.appsend.di.AppComponent;
import com.tomclaw.appsend.di.AppModule;
import com.tomclaw.appsend.di.DaggerAppComponent;
import com.tomclaw.appsend.net.Session;
import com.tomclaw.appsend.util.PackageIconLoader;
import com.tomclaw.appsend.util.PreferenceHelper;
import com.tomclaw.appsend.util.TimeHelper;
import com.tomclaw.appsend.util.states.StateHolder;
import com.tomclaw.cache.DiskLruCache;
import com.tomclaw.imageloader.SimpleImageLoader;
import com.tomclaw.imageloader.core.DiskCacheImpl;
import com.tomclaw.imageloader.core.FileProvider;
import com.tomclaw.imageloader.core.FileProviderImpl;
import com.tomclaw.imageloader.core.MainExecutorImpl;
import com.tomclaw.imageloader.core.MemoryCacheImpl;
import com.tomclaw.imageloader.util.BitmapDecoder;
import com.tomclaw.imageloader.util.loader.ContentLoader;
import com.tomclaw.imageloader.util.loader.FileLoader;
import com.tomclaw.imageloader.util.loader.UrlLoader;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * Created by ivsolkin on 21.03.17.
 */
@EApplication
public class Appteka extends Application {

    private static Appteka app;

    private static boolean wasRegistered = false;
    private static int lastRunBuildNumber = 0;

    private static AppComponent component;

    @Bean
    Session session;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        component = buildComponent();
        session.init();
        initImageLoader();
        actuateFlags();
        TimeHelper.init(this);
        StateHolder.init();
    }

    public static AppComponent getComponent() {
        return component;
    }

    private AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    private void initImageLoader() {
        try {
            FileProvider fileProvider = new FileProviderImpl(
                    getCacheDir(),
                    new DiskCacheImpl(DiskLruCache.create(getCacheDir(), 15728640L)),
                    new UrlLoader(),
                    new FileLoader(getAssets()),
                    new ContentLoader(getContentResolver()),
                    new PackageIconLoader(getPackageManager())
            );
            SimpleImageLoader.INSTANCE.initImageLoader(this, singletonList(new BitmapDecoder()),
                    fileProvider, new MemoryCacheImpl(), new MainExecutorImpl(),
                    Executors.newFixedThreadPool(5));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Appteka app() {
        return app;
    }

    private void actuateFlags() {
        wasRegistered = session.getUserData().isRegistered();
        lastRunBuildNumber = PreferenceHelper.getLastRunBuildNumber(this);
        PreferenceHelper.updateLastRunBuildNumber(this);
    }

    public static boolean wasRegistered() {
        return wasRegistered;
    }

    public static int getLastRunBuildNumber() {
        return lastRunBuildNumber;
    }

}
