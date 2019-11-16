package com.sonusourav.merazoverflow.api;

import com.google.gson.annotations.SerializedName;
import com.sonusourav.merazoverflow.model.Question;
import java.io.Serializable;

public class ApiResponse implements Serializable {

  @SerializedName("items")
  private Question[] questions;

  public Question[] getQuestions() {
    return questions;
  }

  public void setQuestions(Question[] questions) {
    this.questions = questions;
  }
}
