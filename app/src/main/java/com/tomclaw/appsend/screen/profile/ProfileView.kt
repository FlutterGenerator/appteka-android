package com.tomclaw.appsend.screen.profile

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.avito.konveyor.adapter.SimpleRecyclerAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxrelay3.PublishRelay
import com.tomclaw.appsend.R
import com.tomclaw.appsend.util.hide
import com.tomclaw.appsend.util.hideWithAlphaAnimation
import com.tomclaw.appsend.util.show
import com.tomclaw.appsend.util.showWithAlphaAnimation
import io.reactivex.rxjava3.core.Observable

interface ProfileView {

    fun showProgress()

    fun showContent()

    fun showMenu(canEliminate: Boolean)

    fun hideMenu()

    fun showToolbar()

    fun hideToolbar()

    fun showError()

    fun hideError()

    fun showEditNameDialog(name: String, nameRegex: String?)

    fun showEliminationDialog()

    fun showEliminationDone(filesCount: Int, messagesCount: Int, ratingsCount: Int)

    fun showEliminationFailed()

    fun contentUpdated()

    fun contentUpdated(position: Int)

    fun showEditNameError()

    fun showSubscriptionError()

    fun showUnauthorizedError()

    fun navigationClicks(): Observable<Unit>

    fun swipeRefresh(): Observable<Unit>

    fun shareClicks(): Observable<Unit>

    fun eliminateClicks(): Observable<Boolean>

    fun retryClicks(): Observable<Unit>

    fun loginClicks(): Observable<Unit>

    fun nameEditedClicks(): Observable<String>

}

class ProfileViewImpl(
    view: View,
    private val adapter: SimpleRecyclerAdapter
) : ProfileView {

    private val context = view.context
    private val toolbar: Toolbar = view.findViewById(R.id.toolbar)
    private val swipeRefresh: SwipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
    private val recycler: RecyclerView = view.findViewById(R.id.recycler)
    private val error: View = view.findViewById(R.id.error)
    private val blockingProgress: View = view.findViewById(R.id.blocking_progress)
    private val retryButton: View = view.findViewById(R.id.retry_button)

    private val navigationRelay = PublishRelay.create<Unit>()
    private val refreshRelay = PublishRelay.create<Unit>()
    private val shareRelay = PublishRelay.create<Unit>()
    private val eliminateRelay = PublishRelay.create<Boolean>()
    private val retryRelay = PublishRelay.create<Unit>()
    private val loginRelay = PublishRelay.create<Unit>()
    private val nameEditedRelay = PublishRelay.create<String>()

    private val layoutManager: LinearLayoutManager

    init {
        toolbar.setNavigationOnClickListener { navigationRelay.accept(Unit) }
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.share -> shareRelay.accept(Unit)
                R.id.eliminate -> eliminateRelay.accept(false)
            }
            true
        }

        swipeRefresh.setOnRefreshListener {
            refreshRelay.accept(Unit)
        }

        retryButton.setOnClickListener { retryRelay.accept(Unit) }

        val orientation = RecyclerView.VERTICAL
        layoutManager = LinearLayoutManager(view.context, orientation, false)
        adapter.setHasStableIds(true)
        recycler.adapter = adapter
        recycler.layoutManager = layoutManager
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.itemAnimator?.changeDuration = DURATION_MEDIUM
    }

    override fun showProgress() {
        blockingProgress.showWithAlphaAnimation(animateFully = true)
    }

    override fun showContent() {
        blockingProgress.hideWithAlphaAnimation(animateFully = false)
        swipeRefresh.isRefreshing = false
    }

    override fun showMenu(
        canEliminate: Boolean,
    ) {
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.profile_menu)
        if (!canEliminate) {
            toolbar.menu.removeItem(R.id.eliminate)
        }
        toolbar.invalidateMenu()
    }

    override fun hideMenu() {
        toolbar.menu.clear()
        toolbar.invalidateMenu()
    }

    override fun showToolbar() {
        toolbar.show()
    }

    override fun hideToolbar() {
        toolbar.hide()
    }

    override fun showError() {
        error.show()
    }

    override fun hideError() {
        error.hide()
    }

    override fun showEditNameDialog(name: String, nameRegex: String?) {
        var editUserName: TextInputEditText? = null
        val dialog = AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.edit_name_title))
            .setView(R.layout.profile_edit_name_dialog)
            .setPositiveButton(R.string.ok) { _, _ ->
                val editedName = editUserName?.text?.toString() ?: ""
                nameEditedRelay.accept(editedName)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()

        editUserName = dialog
            .findViewById<TextInputEditText>(R.id.user_name)
            ?.apply {
                this.setText(name)
            }

        if (nameRegex != null) {
            editUserName?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, st: Int, cn: Int, af: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    dialog
                        .getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(s.toString().matches(nameRegex.toRegex()))
                }
            })
        }
    }

    override fun showEliminationDialog() {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.eliminate_user_title))
            .setMessage(context.getString(R.string.eliminate_user_message))
            .setNegativeButton(R.string.yes) { _, _ ->
                eliminateRelay.accept(true)
            }
            .setPositiveButton(R.string.no, null)
            .show()
    }

    override fun showEliminationDone(filesCount: Int, messagesCount: Int, ratingsCount: Int) {
        val message: String = context.getString(
            R.string.eliminate_user_success,
            filesCount,
            messagesCount,
            ratingsCount
        )
        Snackbar.make(recycler, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showEliminationFailed() {
        Toast.makeText(context, R.string.eliminate_user_failed, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun contentUpdated() {
        adapter.notifyDataSetChanged()
    }

    override fun contentUpdated(position: Int) {
        adapter.notifyItemChanged(position)
    }

    override fun showEditNameError() {
        Snackbar
            .make(recycler, R.string.unable_to_change_name, Snackbar.LENGTH_LONG)
            .show()
    }

    override fun showSubscriptionError() {
        Snackbar
            .make(recycler, R.string.unable_to_change_subscription_state, Snackbar.LENGTH_LONG)
            .show()
    }

    override fun showUnauthorizedError() {
        Snackbar
            .make(recycler, R.string.authorization_required_message, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.login_button) {
                loginRelay.accept(Unit)
            }
            .show()
    }

    override fun navigationClicks(): Observable<Unit> = navigationRelay

    override fun swipeRefresh(): Observable<Unit> = refreshRelay

    override fun shareClicks(): Observable<Unit> = shareRelay

    override fun eliminateClicks(): Observable<Boolean> = eliminateRelay

    override fun retryClicks(): Observable<Unit> = retryRelay

    override fun loginClicks(): Observable<Unit> = loginRelay

    override fun nameEditedClicks(): Observable<String> = nameEditedRelay

}

private const val DURATION_MEDIUM = 300L
