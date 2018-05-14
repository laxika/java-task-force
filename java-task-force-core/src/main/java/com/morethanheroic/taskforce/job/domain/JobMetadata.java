package com.morethanheroic.taskforce.job.domain;

import java.util.HashMap;
import java.util.Map;

public class JobMetadata {

  private final Map<String, Object> metadataHolder = new HashMap<>();

  public void setMetadata(final String name, final Object value) {
    metadataHolder.put(name, value);
  }

  @SuppressWarnings("unchecked")
  public <T> T getMetadata(final String name, final Class<T> clazz) {
    return (T) metadataHolder.get(name);
  }
}
