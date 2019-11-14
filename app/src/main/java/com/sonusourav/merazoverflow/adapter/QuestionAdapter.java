package com.sonusourav.merazoverflow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.hootsuite.nachos.NachoTextView;
import com.sonusourav.merazoverflow.Question;
import com.sonusourav.merazoverflow.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final int VIEW_TYPE_ITEM = 0;
  private final int VIEW_TYPE_LOADING = 1;
  private Context context;
  private List<Question> questionList;
  private int mCurrentPosition;
  private boolean isLoading = false;

  public QuestionAdapter(Context context, List<Question> questionList) {
    this.context = context;
    this.questionList = questionList;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    switch (viewType) {
      case VIEW_TYPE_ITEM:
        return new MyViewHolder(
            LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_questions, parent, false));
      case VIEW_TYPE_LOADING:
        return new ProgressHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
      default:
        return null;
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

    if (getItemViewType(position) == VIEW_TYPE_LOADING) {
      mCurrentPosition = position;
      ProgressHolder loadingViewHolder = (ProgressHolder) holder;
      loadingViewHolder.progressBar.setIndeterminate(true);
    } else {
      MyViewHolder myViewHolder = (MyViewHolder) holder;
      final Question question = questionList.get(position);
      mCurrentPosition = position;
      myViewHolder.upVotes.setText(Integer.toString(question.getScore()));
      myViewHolder.views.setText(Integer.toString(question.getViewCount()));
      myViewHolder.answers.setText(Integer.toString(question.getAnswerCount()));
      myViewHolder.question.setText(question.getTitle());

      String pattern = "MM-dd-yyyy";
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
      String date = simpleDateFormat.format(question.getLastActivityDate());
      myViewHolder.lastActive.setText(date);

      ArrayAdapter<String> adapter =
          new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line,
              question.getTags());
      myViewHolder.nachoTextView.setAdapter(adapter);
    }
  }

  public void addItems(List<Question> questions) {
    questionList = new ArrayList<>(questions);
    notifyDataSetChanged();
  }

  public void addLoading() {
    isLoading = true;
    questionList.add(new Question());
    notifyItemInserted(questionList.size() - 1);
  }

  public void removeLoading() {
    int position = questionList.size() - 1;
    Question question = getItem(position);
    if (question != null) {
      questionList.remove(position);
      notifyItemRemoved(position);
    }
    isLoading = false;
  }

  public void clear() {
    questionList.clear();
    // notifyDataSetChanged();
  }

  private Question getItem(int position) {
    return questionList.get(position);
  }

  public int getCurrentPosition() {
    return mCurrentPosition;
  }

  @Override
  public int getItemViewType(int position) {
    if (isLoading) {
      return position == questionList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    } else {
      return VIEW_TYPE_ITEM;
    }
  }

  @Override
  public int getItemCount() {
    return questionList == null ? 0 : questionList.size();
  }

  public interface OnLoadMoreListener {
    void onLoadMore();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView upVotes, answers, views, question, lastActive;
    NachoTextView nachoTextView;
    SwipeRefreshLayout swipeRefreshLayout;

    MyViewHolder(View view) {
      super(view);
      upVotes = view.findViewById(R.id.upvotes_text);
      answers = view.findViewById(R.id.ans_text);
      views = view.findViewById(R.id.views_text);
      nachoTextView = view.findViewById(R.id.nacho_text_view);
      question = view.findViewById(R.id.question);
      lastActive = view.findViewById(R.id.last_activity);
      swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
    }
  }

  public class ProgressHolder extends RecyclerView.ViewHolder {

    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    ProgressHolder(View itemView) {
      super(itemView);
      progressBar = itemView.findViewById(R.id.progressBar);
    }
  }
}