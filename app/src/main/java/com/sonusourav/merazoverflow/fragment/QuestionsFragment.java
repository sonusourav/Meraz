package com.sonusourav.merazoverflow.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.sonusourav.merazoverflow.R;
import com.sonusourav.merazoverflow.adapter.PaginationListener;
import com.sonusourav.merazoverflow.adapter.QuestionAdapter;
import com.sonusourav.merazoverflow.api.ApiClient;
import com.sonusourav.merazoverflow.api.ApiInterface;
import com.sonusourav.merazoverflow.api.ApiResponse;
import com.sonusourav.merazoverflow.model.Question;
import com.sonusourav.merazoverflow.model.sortByActivity;
import com.sonusourav.merazoverflow.model.sortByCreation;
import com.sonusourav.merazoverflow.model.sortByRelevance;
import com.sonusourav.merazoverflow.model.sortByVotes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;

public class QuestionsFragment extends Fragment {

  private List<Question> questionsList;
  private SwipeRefreshLayout swipeRefresh;
  private QuestionAdapter questionAdapter;
  private NestedScrollView nestedScrollView;
  private int pageNo = 1;
  private ApiInterface apiService;
  private androidx.appcompat.widget.SearchView searchView;
  private List<Question> tempQuestionList = null;
  private int[] sortOrder = new int[] { 1, 1, 1, 1 };
  private TextView sortActivity, sortRelevance, sortVotes, sortCreation;

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
    sortActivity = view.findViewById(R.id.sort_activity);
    sortRelevance = view.findViewById(R.id.sort_relevance);
    sortVotes = view.findViewById(R.id.sort_votes);
    sortCreation = view.findViewById(R.id.sort_creation);

    searchView = Objects.requireNonNull(getActivity()).findViewById(R.id.search_view);
    questionsList = new ArrayList<>();
    questionAdapter = new QuestionAdapter(getActivity(), questionsList);
    apiService = ApiClient.getClient().create(ApiInterface.class);

    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    questionRecyclerView.setLayoutManager(mLayoutManager);
    nestedScrollView = view.findViewById(R.id.nested_scroll_view);
    questionRecyclerView.setAdapter(questionAdapter);
    questionRecyclerView.setNestedScrollingEnabled(true);
    initScrollListener(mLayoutManager);
    questionAdapter.addLoading();
    swipeRefresh.setEnabled(false);
    initSortListener(sortActivity);
    initSortListener(sortCreation);
    initSortListener(sortVotes);
    initSortListener(sortRelevance);

    fetchQuestion(1);
    swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        questionsList.clear();
        fetchQuestion(++pageNo);
      }
    });

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String query) {
        if (questionAdapter.isSearchEnabled()) {
          questionAdapter.setSearchEnabled(false);
          if (questionsList.size() > 0) {
            tempQuestionList = questionsList.subList(0, questionsList.size() - 1);
          }
          questionAdapter.clear();
          questionAdapter.addLoading();
          searchQuestion(query);
        }
        return false;
      }

      @Override public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

    return view;
  }

  private void fetchQuestion(final int pageNo) {

    Call<ApiResponse> call = apiService.getQuestions(pageNo);
    call.enqueue(new Callback<ApiResponse>() {
      @Override
      public void onResponse(@NonNull Call<ApiResponse> call,
          @NonNull retrofit2.Response<ApiResponse> response) {
        questionAdapter.setSearchEnabled(true);
        if (response.body() != null) {
          questionAdapter.removeLoading();
          updateSortingBar(sortRelevance, 0);
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
        questionAdapter.setSearchEnabled(true);
        if (swipeRefresh.isRefreshing()) {
          swipeRefresh.setRefreshing(false);
        }
        questionAdapter.removeLoading();
        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void searchQuestion(String question) {
    Call<ApiResponse> call = apiService.searchQuestions(question);
    call.enqueue(new Callback<ApiResponse>() {
      @Override
      public void onResponse(@NonNull Call<ApiResponse> call,
          @NonNull retrofit2.Response<ApiResponse> response) {

        if (swipeRefresh.isRefreshing()) {
          swipeRefresh.setRefreshing(false);
        }
        swipeRefresh.setEnabled(true);
        questionAdapter.removeLoading();
        questionAdapter.setSearchEnabled(true);

        if (response.body() != null && response.body().getQuestions().length > 0) {
          questionAdapter.addItems(Arrays.asList(response.body().getQuestions()));
        } else {
          Toast.makeText(getActivity(), "No answers found", Toast.LENGTH_SHORT).show();
          questionAdapter.addItems(tempQuestionList);
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
        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void initSortListener(TextView textView) {

    textView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

        if (!questionsList.isEmpty()) {
          switch (v.getId()) {
            case R.id.sort_relevance:
              Log.d("Question", "relevance");
              if (sortOrder[0] == 1) {
                Collections.sort(questionsList, new sortByRelevance());
                sortOrder[0] = 0;
              } else {
                Collections.sort(questionsList, new sortByRelevance());
                Collections.reverse(questionsList);
                sortOrder[0] = 1;
              }
              updateSortingBar(sortRelevance, 0);
              break;
            case R.id.sort_activity:
              Log.d("Question", "activity");
              if (sortOrder[1] == 1) {
                Collections.sort(questionsList, new sortByActivity());
                sortOrder[1] = 0;
              } else {
                Collections.sort(questionsList, new sortByActivity());
                Collections.reverse(questionsList);
                sortOrder[1] = 1;
              }
              updateSortingBar(sortActivity, 1);
              break;
            case R.id.sort_votes:
              Log.d("Question", "votes");
              if (sortOrder[2] == 1) {
                Log.d("Question", "votes0001");
                Collections.sort(questionsList, new sortByVotes());
                sortOrder[2] = 0;
              } else {
                Log.d("Question", "votes0000");
                Collections.sort(questionsList, new sortByVotes());
                Collections.reverse(questionsList);
                sortOrder[2] = 1;
              }
              updateSortingBar(sortVotes, 2);
              break;
            case R.id.sort_creation:
              Log.d("Question", "creation");
              if (sortOrder[3] == 1) {
                Collections.sort(questionsList, new sortByCreation());
                sortOrder[3] = 0;
              } else {
                Collections.sort(questionsList, new sortByCreation());
                Collections.reverse(questionsList);
                sortOrder[3] = 1;
              }
              updateSortingBar(sortCreation, 3);
              break;
          }
          questionAdapter.notifyDataSetChanged();
        }
      }
    });
  }

  private void updateSortingBar(TextView textView, int number) {
    Log.d("Questions", number + "");
    sortRelevance.setTextColor(getActivity().getResources().getColor(R.color.light_text));
    sortActivity.setTextColor(getActivity().getResources().getColor(R.color.light_text));
    sortVotes.setTextColor(getActivity().getResources().getColor(R.color.light_text));
    sortCreation.setTextColor(getActivity().getResources().getColor(R.color.light_text));
    sortRelevance.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    sortActivity.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    sortVotes.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    sortCreation.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

    textView.setTextColor(getActivity().getResources().getColor(R.color.sky_blue));
    Drawable drawable = null;
    if (sortOrder[number] == 0) {
      drawable = ContextCompat.getDrawable(getActivity(), R.drawable.icon_down_arrow);
    } else {
      drawable = ContextCompat.getDrawable(getActivity(), R.drawable.icon_up_arrow);
    }
    int pixelDrawableSize = (int) Math.round(textView.getLineHeight() * 0.7);
    if (drawable != null) {
      drawable.setBounds(0, 0, 0, pixelDrawableSize);
      Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
      DrawableCompat.setTint(wrappedDrawable,
          getActivity().getResources().getColor(R.color.sky_blue));
    }
    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
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

