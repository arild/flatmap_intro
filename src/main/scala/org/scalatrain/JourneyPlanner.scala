package org.scalatrain

case class JourneyPlanner(trains: Set[Train]) {
	val stations: Set[Station] = { trains flatMap( _.stations ) }	
	
	// Returns all trains at provided station
	def trainsAt(station: Station): Set[Train] = {
	  trains.filter(_.stations.contains(station))
	}
	
	// Returns time and train for trains that contain provided Station in schedule
	def stopsAt(station: Station): Set[(Time, Train)] = {
	  for {
	    train <- trains
	    scheduleElem <- train.schedule if scheduleElem._2 == station
	  } yield (scheduleElem._1, train)
	}
	
	// Returns true if start station is no more than 2 hops
	// away from destination station
	def isShortTrip(from: Station, to: Station): Boolean = 
	  trains exists { train =>
	    // Drop stations until we find start station (can't pattern match on this with _*)
	  	train.stations dropWhile(_ != from) match {
	  	  case Seq(_, `to`, _*) => true
	  	  case Seq(_, _, `to`, _*) => true
	  	  case _ => false
	  	}
	}
}
