package in.imast.impact.helper;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageHelper {

    private static final String GENERAL_STORAGE = "GENERAL_STORAGE";
    private static final String KEY_USER_LANGUAGE = "KEY_USER_LANGUAGE";

    /**
     * Update the app language
     *
     * @param language Language to switch to.
     */
    public static void updateLanguage(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }*/

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }



    /**
     * Store the language selected by the user.
     * /!\ SHOULD BE CALLED WHEN THE USER CHOOSE THE LANGUAGE
     */
    public static void storeUserLanguage(Context context, String language) {
        StaticSharedpreference.saveInfo("app_language",language, context);
    }

    /**
     * @return The stored user language or null if not found.
     */
    public static String getUserLanguage(Context context) {
        return StaticSharedpreference.getInfo("app_language",context);
    }
}
