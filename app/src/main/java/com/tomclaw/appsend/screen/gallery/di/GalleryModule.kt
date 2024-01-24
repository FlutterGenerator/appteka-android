package com.tomclaw.appsend.screen.gallery.di

import android.content.Context
import android.os.Bundle
import com.avito.konveyor.ItemBinder
import com.avito.konveyor.adapter.AdapterPresenter
import com.avito.konveyor.adapter.SimpleAdapterPresenter
import com.avito.konveyor.blueprint.ItemBlueprint
import com.tomclaw.appsend.screen.gallery.GalleryInteractor
import com.tomclaw.appsend.screen.gallery.GalleryInteractorImpl
import com.tomclaw.appsend.screen.gallery.GalleryItem
import com.tomclaw.appsend.screen.gallery.GalleryPresenter
import com.tomclaw.appsend.screen.gallery.GalleryPresenterImpl
import com.tomclaw.appsend.screen.gallery.GalleryResourceProvider
import com.tomclaw.appsend.screen.gallery.GalleryResourceProviderImpl
import com.tomclaw.appsend.screen.gallery.StreamsProvider
import com.tomclaw.appsend.screen.gallery.StreamsProviderImpl
import com.tomclaw.appsend.screen.gallery.adapter.image.ImageItemBlueprint
import com.tomclaw.appsend.screen.gallery.adapter.image.ImageItemPresenter
import com.tomclaw.appsend.util.PerActivity
import com.tomclaw.appsend.util.SchedulersFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import okhttp3.OkHttpClient
import java.util.Locale

@Module
class GalleryModule(
    private val context: Context,
    private val items: List<GalleryItem>,
    private val startIndex: Int,
    private val state: Bundle?
) {

    @Provides
    @PerActivity
    internal fun providePresenter(
        resourceProvider: GalleryResourceProvider,
        adapterPresenter: Lazy<AdapterPresenter>,
        interactor: GalleryInteractor,
        schedulers: SchedulersFactory
    ): GalleryPresenter = GalleryPresenterImpl(
        items,
        startIndex,
        resourceProvider,
        adapterPresenter,
        interactor,
        schedulers,
        state
    )

    @Provides
    @PerActivity
    internal fun provideGalleryInteractor(
        client: OkHttpClient,
        streamsProvider: StreamsProvider,
        schedulers: SchedulersFactory,
    ): GalleryInteractor = GalleryInteractorImpl(client, streamsProvider, schedulers)

    @Provides
    @PerActivity
    internal fun provideStreamsProvider(): StreamsProvider = StreamsProviderImpl(context)

    @Provides
    @PerActivity
    internal fun provideAdapterPresenter(binder: ItemBinder): AdapterPresenter {
        return SimpleAdapterPresenter(binder, binder)
    }

    @Provides
    @PerActivity
    internal fun provideItemBinder(
        blueprintSet: Set<@JvmSuppressWildcards ItemBlueprint<*, *>>
    ): ItemBinder {
        return ItemBinder.Builder().apply {
            blueprintSet.forEach { registerItem(it) }
        }.build()
    }

    @Provides
    @IntoSet
    @PerActivity
    internal fun provideImageItemBlueprint(
        presenter: ImageItemPresenter
    ): ItemBlueprint<*, *> = ImageItemBlueprint(presenter)

    @Provides
    @PerActivity
    internal fun provideImageItemPresenter() = ImageItemPresenter()

    @Provides
    @PerActivity
    internal fun provideGalleryResourceProvider(locale: Locale): GalleryResourceProvider =
        GalleryResourceProviderImpl(context.resources, locale)

}
