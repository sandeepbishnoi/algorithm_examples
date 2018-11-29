package utils;

public class IDGenerator
{
  private static long id = 0;

  private IDGenerator()
  {}

  public static long getNextId()
  {
    return id++;
  }
}
