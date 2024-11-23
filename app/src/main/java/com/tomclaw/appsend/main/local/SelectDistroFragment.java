package com.tomclaw.appsend.main.local;

import android.Manifest;

import com.greysonparrelli.permiso.Permiso;
import com.tomclaw.appsend.R;
import com.tomclaw.appsend.main.item.ApkItem;

public class SelectDistroFragment extends DistroFragment {

    @Override
    public void loadAttempt() {
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {
                    invalidate();
                    loadFiles();
                } else {
                    errorText.setText(R.string.write_permission_select);
                    showError();
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                String title = getString(R.string.app_name);
                String message = getString(R.string.write_permission_select);
                Permiso.getInstance().showRationaleInDialog(title, message, null, callback);
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onClick(final ApkItem item) {
        CommonItemClickListener listener = null;
        if (getActivity() instanceof CommonItemClickListener) {
            listener = (CommonItemClickListener) getActivity();
        }
        if (listener != null) {
            listener.onClick(item);
        }
    }
}
