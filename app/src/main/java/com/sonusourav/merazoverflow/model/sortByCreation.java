package com.sonusourav.merazoverflow.model;

import java.util.Comparator;

public class sortByCreation implements Comparator<Question> {
  public int compare(Question a, Question b) {
    return (int) (a.getCreationDate() - b.getCreationDate());
  }
}
