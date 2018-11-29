package utils;

public interface Bucket<T> {
  public T getBucket();
  public String getId();
  public int size();
}
