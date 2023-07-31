// Generated by view binder compiler. Do not edit!
package com.android.mobicam.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.android.mobicam.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentProfileBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final TextView emailID;

  @NonNull
  public final CircleImageView imageProfile;

  @NonNull
  public final Button logoutBtn;

  @NonNull
  public final TextView phoneNumber;

  @NonNull
  public final TextView userName;

  private FragmentProfileBinding(@NonNull FrameLayout rootView, @NonNull TextView emailID,
      @NonNull CircleImageView imageProfile, @NonNull Button logoutBtn,
      @NonNull TextView phoneNumber, @NonNull TextView userName) {
    this.rootView = rootView;
    this.emailID = emailID;
    this.imageProfile = imageProfile;
    this.logoutBtn = logoutBtn;
    this.phoneNumber = phoneNumber;
    this.userName = userName;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.emailID;
      TextView emailID = ViewBindings.findChildViewById(rootView, id);
      if (emailID == null) {
        break missingId;
      }

      id = R.id.image_profile;
      CircleImageView imageProfile = ViewBindings.findChildViewById(rootView, id);
      if (imageProfile == null) {
        break missingId;
      }

      id = R.id.logout_btn;
      Button logoutBtn = ViewBindings.findChildViewById(rootView, id);
      if (logoutBtn == null) {
        break missingId;
      }

      id = R.id.phoneNumber;
      TextView phoneNumber = ViewBindings.findChildViewById(rootView, id);
      if (phoneNumber == null) {
        break missingId;
      }

      id = R.id.userName;
      TextView userName = ViewBindings.findChildViewById(rootView, id);
      if (userName == null) {
        break missingId;
      }

      return new FragmentProfileBinding((FrameLayout) rootView, emailID, imageProfile, logoutBtn,
          phoneNumber, userName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}