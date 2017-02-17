package com.assistne.kotlintodoapp.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager


/**
 * Created by assistne on 17/2/17.
 */
class ActivityUtils {
    companion object {
        fun addFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment, frameId: Int) {
            fragmentManager.beginTransaction().add(frameId, fragment).commit()
        }
    }
}