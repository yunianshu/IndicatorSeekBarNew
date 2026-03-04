package com.yunianshu.indicatorseekbarnew;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public final class LocaleHelper {

    private static final String PREFERENCE_NAME = "locale_pref";
    private static final String KEY_LANGUAGE_TAG = "language_tag";

    private LocaleHelper() {
    }

    public static Context wrap(Context context) {
        String languageTag = getSavedLanguageTag(context);
        if (languageTag.isEmpty()) {
            return context;
        }
        return updateResources(context, languageTag);
    }

    public static void setSavedLanguageTag(Context context, String languageTag) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(KEY_LANGUAGE_TAG, normalizeLanguageTag(languageTag)).apply();
    }

    public static String getSavedLanguageTag(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return normalizeLanguageTag(preferences.getString(KEY_LANGUAGE_TAG, ""));
    }

    public static String getCurrentLanguageTag(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = configuration.getLocales().get(0);
        } else {
            locale = configuration.locale;
        }
        if (locale == null) {
            return "";
        }
        return normalizeLanguageTag(locale.toLanguageTag());
    }

    public static boolean isSameLanguage(String firstLanguageTag, String secondLanguageTag) {
        return normalizeLanguageTag(firstLanguageTag).equals(normalizeLanguageTag(secondLanguageTag));
    }

    private static Context updateResources(Context context, String languageTag) {
        String normalizedTag = normalizeLanguageTag(languageTag);
        if (normalizedTag.isEmpty()) {
            return context;
        }
        Locale locale = Locale.forLanguageTag(normalizedTag);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            return context;
        }
    }

    private static String normalizeLanguageTag(String languageTag) {
        if (languageTag == null) {
            return "";
        }
        String trimmedTag = languageTag.trim();
        if (trimmedTag.isEmpty()) {
            return "";
        }
        String normalizedTag = trimmedTag.replace('_', '-');
        Locale locale = Locale.forLanguageTag(normalizedTag);
        String language = locale.getLanguage();
        if (language == null || language.trim().isEmpty()) {
            return "";
        }
        return language.toLowerCase(Locale.US);
    }
}
