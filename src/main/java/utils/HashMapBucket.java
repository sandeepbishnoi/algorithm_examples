package utils;
import java.util.HashMap;

public class HashMapBucket<K,V> implements Bucket<HashMap<K,V>>
{
  private final String id;
  private final HashMap<K,V> bucket;

  public HashMapBucket()
  {
    this.bucket = new HashMap<K,V>();
    this.id = "Bucket-" + IDGenerator.getNextId() + "-" + RandomGenerator.getAlphaNumericString(10);

  }

  @Override
  public HashMap getBucket() {
    return bucket;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public int size() {
    return bucket.size();
  }

  @Override
  public String toString()
  {
    return "HashMapBucket[id="+id + " size="+ bucket.size() +"]";
  }
}
