package xyz.valnet.engine.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassToInstanceSet<B> {
  private Map<Class<? extends B>, Set<B>> map = new HashMap<>();
  
  private final <T extends B> void ensure(Class<T> clazz) {
    if(!map.containsKey(clazz)) {
      map.put(clazz, new HashSet<>());
    }
  }

  public <T extends B> void add(Class<T> clazz, T obj) {
    ensure(clazz);
    var set = map.get(clazz);
    set.add(obj);
  }

  @SuppressWarnings("unchecked") // it IS checked, by way of the add method.
  public final <T extends B> Set<T> get(Class<T> clazz) {
    return (Set<T>) map.get(clazz);
  }
}
