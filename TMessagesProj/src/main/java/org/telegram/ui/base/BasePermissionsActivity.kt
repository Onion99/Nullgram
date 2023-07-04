/*
 * Copyright (C) 2019-2023 qwq233 <qwq233@qwq2333.top>
 * https://github.com/qwq233/Nullgram
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this software.
 *  If not, see
 * <https://www.gnu.org/licenses/>
 */

package org.telegram.ui.base

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.annotation.RawRes
import org.telegram.messenger.AndroidUtilities
import org.telegram.messenger.ApplicationLoader
import org.telegram.messenger.ContactsController
import org.telegram.messenger.FileLog
import org.telegram.messenger.ImageLoader
import org.telegram.messenger.LocaleController
import org.telegram.messenger.NotificationCenter
import org.telegram.messenger.SharedConfig
import org.telegram.messenger.camera.CameraController
import org.telegram.ui.ActionBar.AlertDialog
import org.telegram.ui.ActionBar.Theme
import org.telegram.ui.Components.AlertsCreator
import org.telegram.ui.GroupCallActivity
import org.telegram.messenger.R


const val REQUEST_CODE_GEOLOCATION = 2
const val REQUEST_CODE_EXTERNAL_STORAGE = 4
const val REQUEST_CODE_ATTACH_CONTACT = 5
const val REQUEST_CODE_CALLS = 7
const val REQUEST_CODE_OPEN_CAMERA = 20
const val REQUEST_CODE_VIDEO_MESSAGE = 150
const val REQUEST_CODE_EXTERNAL_STORAGE_FOR_AVATAR = 151
const val REQUEST_CODE_SIGN_IN_WITH_GOOGLE = 200
const val REQUEST_CODE_PAYMENT_FORM = 210
open class BasePermissionsActivity : Activity() {

    protected var currentAccount = -1
    protected fun checkPermissionsResult(requestCode: Int, permissions: Array<String?>?, grantResults: IntArray?): Boolean {
        var tempPermissions = permissions
        var tempGrantResults = grantResults
        if (tempGrantResults == null) {
            tempGrantResults = IntArray(0)
        }
        if (tempPermissions == null) {
            tempPermissions = arrayOfNulls(0)
        }
        val granted = tempGrantResults.isNotEmpty() && tempGrantResults[0] == PackageManager.PERMISSION_GRANTED
        if (requestCode == 104) {
            if (granted) {
                if (GroupCallActivity.groupCallInstance != null) {
                    GroupCallActivity.groupCallInstance.enableCamera()
                }
            } else {
                showPermissionErrorAlert(R.raw.permission_request_camera, LocaleController.getString("VoipNeedCameraPermission", R.string.VoipNeedCameraPermission))
            }
        } else if (requestCode == REQUEST_CODE_EXTERNAL_STORAGE || requestCode == REQUEST_CODE_EXTERNAL_STORAGE_FOR_AVATAR) {
            if (!granted) {
                showPermissionErrorAlert(
                    R.raw.permission_request_folder,
                    if (requestCode == REQUEST_CODE_EXTERNAL_STORAGE_FOR_AVATAR) LocaleController.getString(
                        "PermissionNoStorageAvatar",
                        R.string.PermissionNoStorageAvatar
                    ) else LocaleController.getString("PermissionStorageWithHint", R.string.PermissionStorageWithHint)
                )
            } else {
                ImageLoader.getInstance().checkMediaPaths()
            }
        } else if (requestCode == REQUEST_CODE_ATTACH_CONTACT) {
            if (!granted) {
                showPermissionErrorAlert(R.raw.permission_request_contacts, LocaleController.getString("PermissionNoContactsSharing", R.string.PermissionNoContactsSharing))
                return false
            } else {
                ContactsController.getInstance(currentAccount).forceImportContacts()
            }
        } else if (requestCode == 3 || requestCode == REQUEST_CODE_VIDEO_MESSAGE) {
            var audioGranted = true
            var cameraGranted = true
            var i = 0
            val size = Math.min(tempPermissions.size, tempGrantResults.size)
            while (i < size) {
                if (Manifest.permission.RECORD_AUDIO == tempPermissions[i]) {
                    audioGranted = tempGrantResults[i] == PackageManager.PERMISSION_GRANTED
                } else if (Manifest.permission.CAMERA == tempPermissions[i]) {
                    cameraGranted = tempGrantResults[i] == PackageManager.PERMISSION_GRANTED
                }
                i++
            }
            if (requestCode == REQUEST_CODE_VIDEO_MESSAGE && (!audioGranted || !cameraGranted)) {
                showPermissionErrorAlert(R.raw.permission_request_camera, LocaleController.getString("PermissionNoCameraMicVideo", R.string.PermissionNoCameraMicVideo))
            } else if (!audioGranted) {
                showPermissionErrorAlert(R.raw.permission_request_microphone, LocaleController.getString("PermissionNoAudioWithHint", R.string.PermissionNoAudioWithHint))
            } else if (!cameraGranted) {
                showPermissionErrorAlert(R.raw.permission_request_camera, LocaleController.getString("PermissionNoCameraWithHint", R.string.PermissionNoCameraWithHint))
            } else {
                if (SharedConfig.inappCamera) {
                    CameraController.getInstance().initCamera(null)
                }
                return false
            }
        } else if (requestCode == 18 || requestCode == 19 || requestCode == REQUEST_CODE_OPEN_CAMERA || requestCode == 22) {
            if (!granted) {
                showPermissionErrorAlert(R.raw.permission_request_camera, LocaleController.getString("PermissionNoCameraWithHint", R.string.PermissionNoCameraWithHint))
            }
        } else if (requestCode == REQUEST_CODE_GEOLOCATION) {
            NotificationCenter.getGlobalInstance().postNotificationName(if (granted) NotificationCenter.locationPermissionGranted else NotificationCenter.locationPermissionDenied)
        }
        return true
    }

    protected fun createPermissionErrorAlert(@RawRes animationId: Int, message: String?): AlertDialog {
        return AlertDialog.Builder(this)
            .setTopAnimation(animationId, AlertsCreator.PERMISSIONS_REQUEST_TOP_ICON_SIZE, false, Theme.getColor(Theme.key_dialogTopBackground))
            .setMessage(AndroidUtilities.replaceTags(message))
            .setPositiveButton(
                LocaleController.getString("PermissionOpenSettings", R.string.PermissionOpenSettings)
            ) { dialogInterface: DialogInterface?, i: Int ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.packageName))
                    startActivity(intent)
                } catch (e: Exception) {
                    FileLog.e(e)
                }
            }
            .setNegativeButton(LocaleController.getString("ContactsPermissionAlertNotNow", R.string.ContactsPermissionAlertNotNow), null)
            .create()
    }

    private fun showPermissionErrorAlert(@RawRes animationId: Int, message: String) {
        createPermissionErrorAlert(animationId, message).show()
    }

    companion object {

    }
}
