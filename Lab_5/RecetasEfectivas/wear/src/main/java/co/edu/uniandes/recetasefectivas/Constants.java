/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.edu.uniandes.recetasefectivas;

/**
 * Constants that are used in both the Application and the Wearable modules.
 */
public final class Constants {

    private Constants() {};

    public static final int WATCH_ONLY_ID = 2;
    public static final int PHONE_ONLY_ID = 3;
    public static final int BOTH_ID = 4;

    public static final String BOTH_PATH = "/both";
    public static final String PATH = "/recetasEfectivas";
    public static final String KEY_NOTIFICATION_ID = "notification-id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";

    public static final String ACTION_BUY_NOW = "comprar ahora";
    public static final String ACTION_DISMISS
            = "com.example.android.wearable.synchronizednotifications.DISMISS";
    public static final String ACTION_PROCESSED_PRODUCT = "processed product";
    public static final String ACTION_EMPTY_LIST = "empty list";
    public static final String ACTION_PROCESS_PRODUCT = "process product";

    public static final String ARGUMENTS = "arguments";
    private static final String
            SEND_INGRLIST_CAPABILITY_NAME = "send_ingrLis";


    public static final String STATUS_BUY = "bought";
    public static final String STATUS_SKIP = "skipped";
    public static final String STATUS_MISS = "missed";
}
