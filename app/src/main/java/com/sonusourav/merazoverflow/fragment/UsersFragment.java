package com.sonusourav.merazoverflow.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.sonusourav.merazoverflow.R;
import com.sonusourav.merazoverflow.model.Question;
import java.util.List;

public class UsersFragment extends Fragment {

  private static final String TAG = UsersFragment.class.getSimpleName();

  // url to fetch shopping items
  private static final String URL = "https://api.androidhive.info/json/movies_2017.json";

  private RecyclerView recyclerView;
  private List<Question> usersList;

  public UsersFragment() {
    // Required empty public constructor
  }

  public static UsersFragment newInstance() {
    UsersFragment fragment = new UsersFragment();
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
    View view = inflater.inflate(R.layout.fragment_users, container, false);

    recyclerView = view.findViewById(R.id.users_recycler_view);

    return view;
  }
}
