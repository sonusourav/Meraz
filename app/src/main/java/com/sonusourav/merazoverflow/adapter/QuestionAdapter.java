package com.sonusourav.merazoverflow.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sonusourav.merazoverflow.QuestionDetails;
import com.sonusourav.merazoverflow.R;
import com.sonusourav.merazoverflow.model.Question;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final int VIEW_TYPE_ITEM = 0;
  private final int VIEW_TYPE_LOADING = 1;
  private Context context;
  private List<Question> questionList;
  private int mCurrentPosition;
  private boolean isLoading = false;
  private boolean isLastPage = false;
  private boolean isSearchEnabled = true;

  public QuestionAdapter(Context context, List<Question> questionList) {
    this.context = context;
    this.questionList = questionList;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    switch (viewType) {
      case VIEW_TYPE_ITEM:
        return new MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.card_questions, parent, false));
      case VIEW_TYPE_LOADING:
        return new ProgressHolder(
            LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false));
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
      myViewHolder.userName.setText(question.getOwner().getName());
      myViewHolder.userName.setTextColor(context.getResources().getColor(R.color.sky_blue));

      if (question.isIsAnswered()) {
        myViewHolder.leftLayout.setBackgroundColor(
            context.getResources().getColor(R.color.color_answered));
      } else {
        myViewHolder.leftLayout.setBackgroundColor(
            context.getResources().getColor(R.color.color_unanswered));
      }

      myViewHolder.question.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          Intent intent = new Intent(context, QuestionDetails.class);
          intent.putExtra("url", question.getLink());
          context.startActivity(intent);
        }
      });

      myViewHolder.userName.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          Intent intent = new Intent(context, QuestionDetails.class);
          intent.putExtra("url", question.getOwner().getUserIdlink());
          context.startActivity(intent);
        }
      });

      for (int i = 0; i < 2; i++) {
        Chip tag = new Chip(context);
        tag.setText(question.getTags()[i]);
        myViewHolder.tagView.addView(tag);
      }
    }
  }

  public void addItems(List<Question> questions) {
    clear();
    questionList.addAll(questions);
    notifyDataSetChanged();
  }

  public void addLoading() {
    isLoading = true;
    questionList.add(new Question());
    notifyItemInserted(questionList.size() - 1);
  }

  public boolean isLoading() {
    return isLoading;
  }

  public boolean isSearchEnabled() {
    return isSearchEnabled;
  }

  public void setSearchEnabled(boolean searchEnabled) {
    isSearchEnabled = searchEnabled;
  }

  public boolean isLastPage() {
    return isLastPage;
  }

  public void removeLoading() {
    int position = getCurrentPosition();
    if (getItemViewType(position) == VIEW_TYPE_LOADING) {
      questionList.remove(position);
      notifyItemRemoved(position);
    }
    isLoading = false;
  }

  public void clear() {
    questionList.clear();
    notifyDataSetChanged();
  }

  private Question getItem(int position) {
    return questionList.get(position);
  }

  private int getCurrentPosition() {
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

  public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView upVotes, answers, views, question, userName;
    ChipGroup tagView;
    SearchView searchView;
    LinearLayout leftLayout;
    CardView cardView;

    MyViewHolder(View view) {
      super(view);
      upVotes = view.findViewById(R.id.upvotes_text);
      answers = view.findViewById(R.id.ans_text);
      views = view.findViewById(R.id.views_text);
      tagView = view.findViewById(R.id.tag_view);
      question = view.findViewById(R.id.question);
      userName = view.findViewById(R.id.user_name);
      searchView = view.findViewById(R.id.search_view);
      leftLayout = view.findViewById(R.id.left_layout);
      cardView = view.findViewById(R.id.card_view);
    }
  }

  public class ProgressHolder extends RecyclerView.ViewHolder {

    ProgressBar progressBar;

    ProgressHolder(View itemView) {
      super(itemView);
      progressBar = itemView.findViewById(R.id.progressBar);
    }
  }
}