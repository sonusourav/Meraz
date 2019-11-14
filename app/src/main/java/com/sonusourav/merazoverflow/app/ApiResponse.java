package com.sonusourav.merazoverflow.app;

import com.google.gson.annotations.SerializedName;
import com.sonusourav.merazoverflow.Question;

public class ApiResponse {

  @SerializedName("items")
  private Question[] questions;

  public Question[] getQuestions() {
    return questions;
  }

  public void setQuestions(Question[] questions) {
    this.questions = questions;
  }
}
