package org.scalatrain;

case class Time(hours: Int = 0, minutes: Int = 0) {
  require(hours >= 0 && hours < 24, "illegal hours parameter")
  require(minutes >= 0 && minutes < 60, "illegal minutes parameter")
  
  // Evaluated when actually used
  lazy val asMinutes = hours * 60 + minutes
  
  def minus(that: Time): Int = this.asMinutes - (that.hours * 60 + that.minutes)
  
  // Alias for minus()
  def -(that: Time): Int = minus(that)
}

// Companion object ~ static methods in Java
object Time {
  def fromMinutes(numMinutes: Int): Time = {
	new Time(hours = numMinutes / 60, minutes = numMinutes % 60)
  }
}