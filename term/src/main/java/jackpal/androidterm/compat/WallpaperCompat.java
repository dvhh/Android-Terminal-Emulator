package jackpal.androidterm.compat;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.drawable.Drawable;

public class WallpaperCompat {

    @TargetApi(5)
    private static class Api5OrLater {
        public static Drawable getDrawable(Context context) {
            return WallpaperManager.getInstance(context).getDrawable();
        }

        public static Drawable getFastDrawable(Context context) {
            return WallpaperManager.getInstance(context).getFastDrawable();
        }
    }

    public static Drawable getDrawable(Context context) {
        Drawable result=null;
        if(AndroidCompat.SDK >= 5) {
            result=Api5OrLater.getDrawable(context);
        }
        return result;
    }

    public static Drawable getFastDrawable(Context context) {
        Drawable result=null;
        if(AndroidCompat.SDK >= 5) {
            result=Api5OrLater.getFastDrawable(context);
        }
        return result;

    }

}
