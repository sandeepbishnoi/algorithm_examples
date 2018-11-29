package utils;

import java.util.Random;

public class RandomGenerator
{
  private static RandomGenerator instance;
  private final Random randomizer = new Random();
  private char[] alphaNumeric = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

  public static RandomGenerator getInstance()
  {
    if(instance == null)
      instance = new RandomGenerator();
    return instance;
  }

  private RandomGenerator(){
  }

  public static String getAlphaNumericString(int len)
  {
    char[] arr = new char[len];
    RandomGenerator randomGenerator = RandomGenerator.getInstance();
    Random randomizer = randomGenerator.randomizer;
    for(int i=0; i < len; i++)
    {
      int randomIdx = randomizer.nextInt(randomGenerator.alphaNumeric.length);
      arr[i] = randomGenerator.alphaNumeric[randomIdx];
    }
    return String.valueOf(arr);
  }

  public static int getInt(int bound)
  {
    return getInstance().randomizer.nextInt(bound);
  }
}
