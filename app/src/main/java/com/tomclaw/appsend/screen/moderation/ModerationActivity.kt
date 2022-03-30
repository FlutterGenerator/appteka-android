package com.tomclaw.appsend.screen.moderation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.avito.konveyor.ItemBinder
import com.avito.konveyor.adapter.AdapterPresenter
import com.avito.konveyor.adapter.SimpleRecyclerAdapter
import com.tomclaw.appsend.Appteka
import com.tomclaw.appsend.R
import com.tomclaw.appsend.main.download.DownloadActivity.createAppActivityIntent
import com.tomclaw.appsend.screen.moderation.di.ModerationModule
import com.tomclaw.appsend.util.ThemeHelper
import javax.inject.Inject

class ModerationActivity : AppCompatActivity(), ModerationPresenter.ModerationRouter {

    @Inject
    lateinit var presenter: ModerationPresenter

    @Inject
    lateinit var adapterPresenter: AdapterPresenter

    @Inject
    lateinit var binder: ItemBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        val presenterState = savedInstanceState?.getBundle(KEY_PRESENTER_STATE)
        Appteka.getComponent()
            .moderationComponent(ModerationModule(this, presenterState))
            .inject(activity = this)
        ThemeHelper.updateTheme(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.moderation_activity)

        val adapter = SimpleRecyclerAdapter(adapterPresenter, binder)
        val view = ModerationViewImpl(window.decorView, adapter)

        presenter.attachView(view)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        presenter.attachRouter(this)
    }

    override fun onStop() {
        presenter.detachRouter()
        super.onStop()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(KEY_PRESENTER_STATE, presenter.saveState())
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_UPDATE_META) {
            if (resultCode == RESULT_OK) {
                presenter.invalidateApps()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun openAppModerationScreen(appId: String, title: String) {
        val intent = createAppActivityIntent(this, appId, title, true, true)
        startActivityForResult(intent, REQUEST_UPDATE_META)
    }

    override fun leaveScreen() {
        finish()
    }

}

fun createModerationActivityIntent(
    context: Context,
): Intent = Intent(context, ModerationActivity::class.java)

private const val KEY_PRESENTER_STATE = "presenter_state"
private const val REQUEST_UPDATE_META = 1
