package jackpal.androidterm.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class DpiUtils {
    public static  int dpToPixel(Context context,int input) {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                input,
                r.getDisplayMetrics()
        );
        return px;
    }
}
