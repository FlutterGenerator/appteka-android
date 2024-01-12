package com.tomclaw.appsend.main.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.tomclaw.appsend.R;
import com.tomclaw.appsend.main.adapter.files.FileViewHolder;
import com.tomclaw.appsend.main.adapter.files.FileViewHolderCreator;
import com.tomclaw.appsend.main.item.StoreItem;

public class StoreFileViewHolderCreator implements FileViewHolderCreator<StoreItem> {

    private final LayoutInflater inflater;

    public StoreFileViewHolderCreator(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public FileViewHolder<StoreItem> create(@NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.store_item, parent, false);
        return new StoreFileViewHolder(view);
    }

}
