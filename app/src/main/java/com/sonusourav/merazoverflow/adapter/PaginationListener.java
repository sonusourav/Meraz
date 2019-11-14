package com.sonusourav.merazoverflow.adapter;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationListener extends RecyclerView.OnScrollListener {

  public static final int PAGE_START = 1;
  private static final int PAGE_SIZE = 30;
  @NonNull
  private LinearLayoutManager layoutManager;
  private QuestionAdapter questionAdapter;

  protected PaginationListener(@NonNull LinearLayoutManager layoutManager,
      QuestionAdapter adapter) {
    this.layoutManager = layoutManager;
    this.questionAdapter = adapter;
  }

  @Override
  public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);

    int visibleItemCount = layoutManager.getChildCount();
    int totalItemCount = layoutManager.getItemCount();
    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
    int currentPos = layoutManager.findLastCompletelyVisibleItemPosition();
    Log.d("Pagination", visibleItemCount + "");
    Log.d("Pagination", totalItemCount + "");
    Log.d("Pagination", firstVisibleItemPosition + "");
    Log.d("Pagination", currentPos + "");

    if (!isLoading() && !isLastPage()) {
      if (questionAdapter.getCurrentPosition() >= totalItemCount - 1
          && firstVisibleItemPosition >= 0
          && totalItemCount >= PAGE_SIZE) {
        Log.d("Pagination", "reaching loadMore");
        loadMoreItems();
      }
    }
  }

  protected abstract void loadMoreItems();

  public abstract boolean isLastPage();

  public abstract boolean isLoading();
}
