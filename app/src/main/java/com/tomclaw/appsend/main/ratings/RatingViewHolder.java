package com.tomclaw.appsend.main.ratings;

import static com.tomclaw.appsend.main.ratings.RatingsHelper.tintRatingIndicator;
import static com.tomclaw.appsend.main.ratings.RatingsListener.STATE_FAILED;
import static com.tomclaw.appsend.main.ratings.RatingsListener.STATE_LOADED;
import static com.tomclaw.appsend.main.ratings.RatingsListener.STATE_LOADING;
import static com.tomclaw.appsend.util.TimeHelper.timeHelper;
import static java.util.concurrent.TimeUnit.SECONDS;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.tomclaw.appsend.R;
import com.tomclaw.appsend.main.dto.RatingItem;
import com.tomclaw.appsend.view.UserIconView;
import com.tomclaw.appsend.view.UserIconViewImpl;

/**
 * Created by solkin on 03.08.17.
 */
class RatingViewHolder extends RecyclerView.ViewHolder {

    private final View itemView;
    private final UserIconView memberImageView;
    private final AppCompatRatingBar ratingView;
    private final TextView dateView;
    private final TextView commentView;
    private final View progressView;
    private final View errorView;
    private final View retryButtonView;

    RatingViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        memberImageView = new UserIconViewImpl(itemView.findViewById(R.id.member_icon));
        ratingView = itemView.findViewById(R.id.rating_view);
        dateView = itemView.findViewById(R.id.date_view);
        commentView = itemView.findViewById(R.id.comment_view);
        progressView = itemView.findViewById(R.id.item_progress);
        errorView = itemView.findViewById(R.id.error_view);
        retryButtonView = itemView.findViewById(R.id.button_retry);
    }

    void bind(final RatingItem item, boolean isLast, final RatingsListener listener) {
        tintRatingIndicator(itemView.getContext(), ratingView);
        itemView.setOnClickListener(v -> listener.onClick(item));
        memberImageView.bind(item.getUserIcon());
        ratingView.setRating(item.getScore());
        dateView.setText(timeHelper().getFormattedDate(SECONDS.toMillis(item.getTime())));
        String text = item.getText();
        commentView.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        commentView.setText(text);
        boolean isProgress = false;
        boolean isError = false;
        if (isLast) {
            int result = listener.onNextPage();
            switch (result) {
                case STATE_LOADING:
                    isProgress = true;
                    isError = false;
                    break;
                case STATE_FAILED:
                    isProgress = false;
                    isError = true;
                    break;
                case STATE_LOADED:
                    isProgress = false;
                    isError = false;
                    break;
            }
            progressView.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
        }
        progressView.setVisibility(isProgress ? View.VISIBLE : View.GONE);
        errorView.setVisibility(isError ? View.VISIBLE : View.GONE);
        if (isError) {
            retryButtonView.setOnClickListener(v -> listener.onRetry());
        } else {
            retryButtonView.setOnClickListener(null);
        }
    }
}
