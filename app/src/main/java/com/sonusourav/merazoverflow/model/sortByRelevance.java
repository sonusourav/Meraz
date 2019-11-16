package com.sonusourav.merazoverflow.model;

import java.util.Comparator;

public class sortByRelevance implements Comparator<Question> {
  public int compare(Question a, Question b) {
    return a.getViewCount() - b.getViewCount();
  }
}
