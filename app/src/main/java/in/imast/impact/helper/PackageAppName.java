package in.imast.impact.helper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PackageAppName {

    private Context context;
    private PackageManager manager;

    public PackageAppName(Context context) {
        this.context = context;
    }

    public String getVersionName() {
        manager = context.getPackageManager();

        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public int getVersion() {
        manager = context.getPackageManager();

        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String getApplicationName() {
        try {
            manager = context.getPackageManager();
            String packageName = context.getPackageName();
            ApplicationInfo applicationInfo = manager.getApplicationInfo(packageName, 0);

            return applicationInfo.packageName;

        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
