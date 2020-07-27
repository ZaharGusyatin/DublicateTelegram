package com.example.duplicatetelegram.ui.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.view.*

import com.example.duplicatetelegram.R
import com.example.duplicatetelegram.activities.MainActivity
import com.example.duplicatetelegram.activities.RegisterActivity
import com.example.duplicatetelegram.utilits.*
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : BaseFragment(R.layout.fragment_settings) {


    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()

    }

    private fun initFields() {
        textViewAboutYouSettings.text = USER.bio
        textViewNumberSettings.text = USER.number
        settings_user_online.text = USER.status
        settings_full_name.text = USER.fullname
        textViewUsernameSettings.text = USER.username
        settings_button_change_userName.setOnClickListener {
            replaceFragment(ChangeUserNameFragment())
        }
        settings_button_change_aboutYou.setOnClickListener {
            replaceFragment(ChangeBioFragment())
        }
        settings_change_photo.setOnClickListener {
            changePhonoUser()
        }
        settings_user_photo.downloadAndSetImage(USER.photoUrl)

    }


    private fun changePhonoUser() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(activity as MainActivity, this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exitApp -> {
                AppStates.updateState(AppStates.OFFLINE)
                AUTH.signOut()
                (activity as MainActivity).replaceActivity(RegisterActivity())
            }
            R.id.settings_menu_change_name -> {
                replaceFragment(ChangeNameFragment())
            }
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK
            && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE).child(CURRENT_UID)
            putImageToStorage(uri, path) {
                getUrlFromStorage(path) {
                    putUrlDatabase(it) {
                        settings_user_photo.downloadAndSetImage(it)
                        showToast(getString(R.string.toast_data_update))
                        USER.photoUrl = it
                        APP_ACTIVITY.mAppDrawer.upDateHeader()
                    }
                }
            }


            /*  path.putFile(uri).addOnCompleteListener { task1 ->
                  if (task1.isSuccessful) {
                      path.downloadUrl.addOnCompleteListener { task2 ->
                          if (task2.isSuccessful) {
                              val photoUrl = task2.result.toString()
                              REF_DATA_BASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(
                                  CHILD_PHOTO_URL
                              ).setValue(photoUrl).addOnCompleteListener {
                                  if (it.isSuccessful) {
                                      settings_user_photo.downloadAndSetImage(photoUrl)
                                      showToast(getString(R.string.toast_data_update))
                                      USER.photoUrl = photoUrl

                                  }
                              }
                          }
                      }
                  }
              }*/
        }
    }


}
