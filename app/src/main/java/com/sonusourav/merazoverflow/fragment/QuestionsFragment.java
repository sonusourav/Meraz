package com.sonusourav.merazoverflow.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.sonusourav.merazoverflow.R;
import com.sonusourav.merazoverflow.adapter.PaginationListener;
import com.sonusourav.merazoverflow.adapter.QuestionAdapter;
import com.sonusourav.merazoverflow.app.ApiClient;
import com.sonusourav.merazoverflow.app.ApiInterface;
import com.sonusourav.merazoverflow.app.ApiResponse;
import com.sonusourav.merazoverflow.model.Question;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class QuestionsFragment extends Fragment {

  private List<Question> questionsList;
  private SwipeRefreshLayout swipeRefresh;
  private QuestionAdapter questionAdapter;
  private NestedScrollView nestedScrollView;
  private int pageNo = 1;

  public QuestionsFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_questions, container, false);

    RecyclerView questionRecyclerView = view.findViewById(R.id.questions_recycler_view);
    swipeRefresh = view.findViewById(R.id.swipeRefresh);
    questionsList = new ArrayList<>();
    questionAdapter = new QuestionAdapter(getActivity(), questionsList);

    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    questionRecyclerView.setLayoutManager(mLayoutManager);
    nestedScrollView = view.findViewById(R.id.nested_scroll_view);
    questionRecyclerView.setAdapter(questionAdapter);
    questionRecyclerView.setNestedScrollingEnabled(true);
    initScrollListener(mLayoutManager);
    //initScroll();
    questionAdapter.addLoading();
    swipeRefresh.setEnabled(false);
    fetchQuestion(1);
    swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        questionsList.clear();
        fetchQuestion(++pageNo);
      }
    });
    return view;
  }

  /*
  private void initScroll() {
    nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

      @Override
      public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
          int oldScrollY) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) questionRecyclerView.getLayoutManager();

        if (layoutManager != null) {
          int totalItemCount = layoutManager.getItemCount();
          int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

          if (!questionAdapter.isLoading()) {
            if (scrollY >= ( v.getChildAt(0).getMeasuredHeight()- v.getMeasuredHeight() -threshold )
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE) {
             Log.d("questions", "reaching loadMore");
              questionAdapter.addLoading();
              fetchQuestion(++pageNo);                  }

          }
        }
      }
    });
  }
  */


  private void fetchQuestion(final int pageNo) {
    ApiInterface apiService =
        ApiClient.getClient().create(ApiInterface.class);

    Call<ApiResponse> call = apiService.getQuestions(pageNo);
    call.enqueue(new Callback<ApiResponse>() {
      @Override
      public void onResponse(@NonNull Call<ApiResponse> call,
          @NonNull retrofit2.Response<ApiResponse> response) {

        if (response.body() != null) {
          questionAdapter.removeLoading();

          if (swipeRefresh.isRefreshing()) {
            questionAdapter.addItems(Arrays.asList(response.body().getQuestions()));
            swipeRefresh.setRefreshing(false);
          } else {
            questionsList.addAll(Arrays.asList(response.body().getQuestions()));
            questionAdapter.notifyDataSetChanged();
          }
          Log.d("Questions", response.body().getQuestions()[29].getTitle());
          swipeRefresh.setEnabled(true);

        }
      }

      @Override
      public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
        Log.e("Questions", t.toString());
        swipeRefresh.setEnabled(true);
        if (swipeRefresh.isRefreshing()) {
          swipeRefresh.setRefreshing(false);
        }
        questionAdapter.removeLoading();
      }
    });
  }

  private void initScrollListener(LinearLayoutManager layoutManager) {

    nestedScrollView.setOnScrollChangeListener(
        new PaginationListener(layoutManager) {
          @Override
          protected void loadMoreItems() {
            questionAdapter.addLoading();
            fetchQuestion(++pageNo);
          }

          @Override
          public boolean isLastPage() {
            return questionAdapter.isLastPage();
          }

          @Override
          public boolean isLoading() {
            return questionAdapter.isLoading();
          }
        });
  }
}

