package com.sonusourav.merazoverflow.adapter;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;

public abstract class PaginationListener
    implements NestedScrollView.OnScrollChangeListener {

  private static final int PAGE_SIZE = 30;
  private LinearLayoutManager layoutManager;
  private static final int threshold = 3500;

  protected PaginationListener(@NonNull LinearLayoutManager layoutManager) {
    this.layoutManager = layoutManager;
  }

  protected abstract void loadMoreItems();

  public abstract boolean isLastPage();

  public abstract boolean isLoading();

  @Override public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
      int oldScrollY) {

    if (layoutManager != null) {
      int totalItemCount = layoutManager.getItemCount();
      int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

      if (!isLoading() && !isLastPage()) {
        if (scrollY >= (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() - threshold)
            && firstVisibleItemPosition >= 0
            && totalItemCount >= PAGE_SIZE) {
          loadMoreItems();
        }
      }
    }
  }
}
