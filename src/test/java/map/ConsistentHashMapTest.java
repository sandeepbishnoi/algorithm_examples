package map;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import utils.RandomGenerator;

/**
 * Unit test for ConsistentHashMap Algorithm
 */
public class ConsistentHashMapTest {

  @Test
  public void testGetAndPut1()
  {
    ConsistentHashMap<String,String> map = new ConsistentHashMap<String,String>(5);
    map.put("key1", RandomGenerator.getAlphaNumericString(5));
    map.get("key1");
    map.put("asdfasdfdasf", RandomGenerator.getAlphaNumericString(5));
    map.get("asdfasdfdasf");
    map.put("key1", RandomGenerator.getAlphaNumericString(5));
    map.get("key1");
    map.get("key1");
    map.put("akey2", RandomGenerator.getAlphaNumericString(5));
    map.put("bkey3", RandomGenerator.getAlphaNumericString(5));
    map.put("ckey4", RandomGenerator.getAlphaNumericString(5));
    map.put("dkey5", RandomGenerator.getAlphaNumericString(5));
  }

  @Test
  public void testScaleOut()
  {
    ConsistentHashMap<String,String> map = new ConsistentHashMap<String,String>(5);

    // Generate 100 key-value pairs
    String[] keys = new String[100];
    for(int i=0; i < keys.length; i++)
      map.put(RandomGenerator.getAlphaNumericString(5),RandomGenerator.getAlphaNumericString(10));

    map.scaleOut();
    System.out.println("Bucket Stats:"+map.toString());
  }
}
