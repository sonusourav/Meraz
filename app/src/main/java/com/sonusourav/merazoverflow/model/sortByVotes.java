package com.sonusourav.merazoverflow.model;

import java.util.Comparator;

public class sortByVotes implements Comparator<Question> {
  public int compare(Question a, Question b) {
    return a.getScore() - b.getScore();
  }
}
