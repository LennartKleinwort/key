/* This file has been generated by Stubmaker (de.uka.ilkd.stubmaker)
 * Date: Wed Nov 26 11:26:00 CET 2014
 */
package java.lang;

public final class Thread$State extends java.lang.Enum
{
   public final static java.lang.Thread$State NEW;
   public final static java.lang.Thread$State RUNNABLE;
   public final static java.lang.Thread$State BLOCKED;
   public final static java.lang.Thread$State WAITING;
   public final static java.lang.Thread$State TIMED_WAITING;
   public final static java.lang.Thread$State TERMINATED;


   /*@ requires true; ensures true; assignable \everything; */
   public static java.lang.Thread$State[] values();

   /*@ requires true; ensures true; assignable \everything; */
   public static java.lang.Thread$State valueOf(java.lang.String arg0);
}