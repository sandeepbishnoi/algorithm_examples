package map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import utils.Bucket;
import utils.HashMapBucket;

/**
 * Sample implementation of Consistent Hashing Algorithm
 * For more details about consistent hashing algorithm, see the following link:
 * https://dl.acm.org/citation.cfm?id=258660
 * @param <K>
 * @param <V>
 */
public class ConsistentHashMap<K,V>
{
  /** Ring of buckets*/
  private final Ring<HashMap<K,V>> ring;

  /** List view of buckets which are situated in ring*/
  private List<Bucket<HashMap<K,V>>> bucketList;

  /**
   * Construct a Consistent Hashing Algorithm based HashMap with 1 bucket
   */
  public ConsistentHashMap()
  {
    this(1);
  }

  /**
   * Construct a Consistent Hashing Algorithm based HashMap with N buckets
   * @param numBuckets
   */
  public ConsistentHashMap(int numBuckets)
  {
    ring = new Ring<HashMap<K,V>>();
    bucketList = new ArrayList<Bucket<HashMap<K,V>>>();

    for(int i=0;i < numBuckets; i++)
    {
      Bucket next = new HashMapBucket<K,V>();
      ring.addBucket(next);
      bucketList.add(next);
    }
  }

  /**
   * Get an object corresponding to given key
   * @param key
   * @return value corresponding to the key, null if absent.
   */
  public V get(K key)
  {
    Bucket<HashMap<K,V>>bucket = ring.getBucket(key.hashCode());
    V out = bucket.getBucket().get(key);
    System.out.println("GET OPERATION: Key:"+key + " Value:"+ out + bucket.toString());
    return out;
  }

  /**
   * Put a (key,value) entry in the map.
   * @param key
   * @param value
   */
  public void put(K key, V value)
  {
    Bucket<HashMap<K,V>> bucket = ring.getBucket(key.hashCode());
    bucket.getBucket().put(key, value);
    System.out.println("PUT OPERATION: Key:"+key + " Value:" + value + bucket.toString());
  }

  /**
   * Add one more bucket in the map
   * @return
   */
  public boolean scaleOut()
  {
    Bucket added = new HashMapBucket<K,V>();
    Bucket<HashMap<K,V>> nextToAdded = ring.getNextNeighborBucket(added.getId().hashCode());
    ring.addBucket(added);
    bucketList.add(added);

    int count=0;
    HashMap<K,V> nextBucketMap = nextToAdded.getBucket();
    HashMap<K,V> nextBucketMapClone = new HashMap<K,V>(nextBucketMap);
    Set<K> nextBucketKeys = nextBucketMapClone.keySet();
    for(K key: nextBucketKeys) {
      put(key, nextBucketMap.remove(key));
      count++;
    }
    System.out.println("BUCKET ADDED: Rearranged " + count +" records from " + nextToAdded);

    return true;
  }

  /**
   * Reduce one bucket from the map
   * @return true if successful
   *         false if there is either zero or one bucket
   */
  public boolean scaleIn()
  {
    if(bucketList.isEmpty() || bucketList.size() == 1) {
      return false;
    }
    else
    {
      Bucket<HashMap<K,V>> removed = bucketList.remove(0);
      Bucket<HashMap<K,V>> nextToRemoved = ring.getNextNeighborBucket(removed.getId().hashCode());
      int count=0;
      for(Entry<K,V> entry: nextToRemoved.getBucket().entrySet()) {
        nextToRemoved.getBucket().put(entry.getKey(), entry.getValue());
        count++;
      }
      ring.removeBucket(removed);
      System.out.println("BUCKET REMOVED: Shifted " + count +" records from " + removed + " to neighbor " + nextToRemoved);
      return true;
    }
  }

  @Override
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    for(Bucket next: bucketList)
      sb.append(next.toString() + "\n");
    return sb.toString();
  }
}

/**
 * Internal Datastructure for the Consistent Hashing Algorithm implementation.
 * This is a ring of hashes.
 * On this ring, we have nodes present. Nodes can be map entry or bucket itself.
 * See the paper for details about the algorithm:
 * https://dl.acm.org/citation.cfm?id=258660
 * @param <T>
 */
class Ring<T>
{
  private final SortedMap<Integer,Bucket<T>> buckets;

  public Ring()
  {
    buckets = new TreeMap<Integer,Bucket<T>>();
  }

  public void addBucket(Bucket<T> bucket)
  {
    String bucketId = bucket.getId();
    System.out.println("ADD BUCKET:"+bucket.toString() + " RingHash:" + bucketId.hashCode());
    buckets.put(bucketId.hashCode(), bucket);
  }

  public void removeBucket(Bucket<T> bucket)
  {
    String bucketId = bucket.getId();
    System.out.println("REMOVE BUCKET:"+bucket.toString()+ " RingHash:" + bucketId.hashCode());
    buckets.remove(bucketId.hashCode());
  }

  public Bucket<T> getNextNeighborBucket(int pointOnRing)
  {
    SortedMap<Integer, Bucket<T>> tailMap = buckets.tailMap(pointOnRing);
    int nextBucketPointOnRing = tailMap.isEmpty() ? buckets.firstKey() : tailMap.firstKey();
    return buckets.get(nextBucketPointOnRing);
  }

  public Bucket<T> getBucket(int pointOnRing) {
    if (buckets.isEmpty())
      return null;

    if (!buckets.containsKey(pointOnRing))
    {
      SortedMap<Integer, Bucket<T>> tailMap = buckets.tailMap(pointOnRing);
      pointOnRing = tailMap.isEmpty() ? buckets.firstKey() : tailMap.firstKey();
    }
    return buckets.get(pointOnRing);
  }
}
