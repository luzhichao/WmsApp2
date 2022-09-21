package com.linkingwin.education.studyonline.common.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by gsh on 2016/12/22.
 */

public class FileUtils {

    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
