/*
 * Copyright (C) 2006 The Android Open Source Project
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

package com.xiaoguy.commonui.text.method;

import android.text.InputType;
import android.text.method.DigitsKeyListener;

import java.lang.reflect.Field;

public class FixedDigitsKeyListener extends DigitsKeyListener {

    private FixedDigitsKeyListener() {
        super();
    }

    public static FixedDigitsKeyListener getInstance(String accepted) {
        FixedDigitsKeyListener dim = new FixedDigitsKeyListener();

        try {
            Field f = DigitsKeyListener.class.getDeclaredField("mAccepted");
            f.setAccessible(true);
            char[] mAccepted = new char[accepted.length()];
            accepted.getChars(0, accepted.length(), mAccepted, 0);
            f.set(dim, mAccepted);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return dim;
    }

    @Override
    public int getInputType() {
        return InputType.TYPE_CLASS_TEXT;
    }
}
