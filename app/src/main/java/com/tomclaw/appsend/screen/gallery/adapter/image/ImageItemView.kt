package com.tomclaw.appsend.screen.gallery.adapter.image

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.avito.konveyor.adapter.BaseViewHolder
import com.avito.konveyor.blueprint.ItemView
import com.tomclaw.appsend.R
import com.tomclaw.appsend.view.ZoomImageView
import com.tomclaw.imageloader.util.fetch

interface ImageItemView : ItemView {

    fun setUri(uri: Uri)

}

class ImageItemViewHolder(view: View) : BaseViewHolder(view), ImageItemView {

    private val image: ZoomImageView = view.findViewById(R.id.gallery_image)

    init {
        image.disallowPagingWhenZoomed = true
    }

    override fun setUri(uri: Uri) {
        image.setImageResource(R.drawable.ic_cloud)
        image.scaleType = ImageView.ScaleType.CENTER_INSIDE
        image.fetch(uri) {
            placeholder = {
                with(it.get()) {
                    image.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    image.setImageResource(R.drawable.ic_cloud)
                }
            }
            success = { viewHolder, result ->
                with(viewHolder.get()) {
                    image.scaleType = ImageView.ScaleType.MATRIX
                    setImageDrawable(result.getDrawable())
                }
            }
            error = {
                with(it.get()) {
                    image.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    image.setImageResource(R.drawable.ic_error)
                }
            }
        }
    }

}
