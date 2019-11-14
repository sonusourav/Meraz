package com.sonusourav.merazoverflow.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.sonusourav.merazoverflow.R;

public class TagsFragment extends Fragment {

  public TagsFragment() {
    // Required empty public constructor
  }

  public static TagsFragment newInstance(String param1, String param2) {
    TagsFragment fragment = new TagsFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_tags, container, false);
  }
}