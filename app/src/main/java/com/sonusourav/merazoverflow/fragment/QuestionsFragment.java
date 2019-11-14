package com.sonusourav.merazoverflow.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.sonusourav.merazoverflow.Question;
import com.sonusourav.merazoverflow.R;
import com.sonusourav.merazoverflow.adapter.PaginationListener;
import com.sonusourav.merazoverflow.adapter.QuestionAdapter;
import com.sonusourav.merazoverflow.app.ApiClient;
import com.sonusourav.merazoverflow.app.ApiInterface;
import com.sonusourav.merazoverflow.app.ApiResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class QuestionsFragment extends Fragment {

  private static final String TAG = UsersFragment.class.getSimpleName();
  private RecyclerView questionRecyclerView;
  private List<Question> questionsList;
  private SwipeRefreshLayout swipeRefresh;
  private QuestionAdapter questionAdapter;
  private boolean isLoading = false;
  private boolean isLastPage = false;
  private int pageNo = 1;

  public QuestionsFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_questions, container, false);

    questionRecyclerView = view.findViewById(R.id.questions_recycler_view);
    swipeRefresh = view.findViewById(R.id.swipeRefresh);
    questionsList = new ArrayList<>();
    questionAdapter = new QuestionAdapter(getActivity(), questionsList);

    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    questionRecyclerView.setLayoutManager(mLayoutManager);
    questionRecyclerView.setAdapter(questionAdapter);

    fetchQuestion(1);
    initScrollListener(mLayoutManager);
    swipeRefresh.setEnabled(false);
    questionAdapter.addLoading();
    swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        questionsList.clear();
        fetchQuestion(++pageNo);
      }
    });

    return view;
  }

  private void fetchQuestion(final int pageNo) {
    ApiInterface apiService =
        ApiClient.getClient().create(ApiInterface.class);

    Call<ApiResponse> call = apiService.getQuestions(pageNo);
    call.enqueue(new Callback<ApiResponse>() {
      @Override
      public void onResponse(@NonNull Call<ApiResponse> call,
          @NonNull retrofit2.Response<ApiResponse> response) {

        if (response.body() != null) {
          questionAdapter.addItems(Arrays.asList(response.body().getQuestions()));
          Log.d("Questions", response.body().getQuestions()[29].getTitle());
          swipeRefresh.setEnabled(true);
          if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
          }
          questionAdapter.removeLoading();
        }
      }

      @Override
      public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
        // Log error here since request failed
        Log.e("Questions", t.toString());
        swipeRefresh.setEnabled(true);
        if (swipeRefresh.isRefreshing()) {
          swipeRefresh.setRefreshing(false);
        }
        questionAdapter.removeLoading();
      }
    });
  }

  private void initScrollListener(final LinearLayoutManager layoutManager) {

    questionRecyclerView.addOnScrollListener(
        new PaginationListener(layoutManager, questionAdapter) {
          @Override
          protected void loadMoreItems() {
            questionAdapter.addLoading();
            fetchQuestion(++pageNo);
          }

          @Override
          public boolean isLastPage() {
            return isLastPage;
          }

          @Override
          public boolean isLoading() {
            return isLoading;
          }
        });
  }
}

