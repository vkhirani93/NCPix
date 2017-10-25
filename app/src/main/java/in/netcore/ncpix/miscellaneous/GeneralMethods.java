package in.netcore.ncpix.miscellaneous;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by vrajesh on 10/25/17.
 */

public class GeneralMethods {
    String permissionType;

    public String getPermissionType(int permissionRequestCode) {
        switch (permissionRequestCode) {
            case Constant.PERMISSION_RECORD_AUDIO:
                permissionType = Manifest.permission.RECORD_AUDIO;
                break;

            case Constant.PERMISSION_WRITE_STORAGE:
                permissionType = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;

            default:
                permissionType = "";
                break;
        }

        return permissionType;
    }

    public boolean hasRequestedPermission(Context context, int permissionRequestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionType = getPermissionType(permissionRequestCode);

            if (context.checkCallingOrSelfPermission(permissionType) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}
