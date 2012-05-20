package org.scalatrain;

case class Station(name: String)

case class Train(schedule: Seq[(Time, Station)]) {
	require(schedule.size >= 2, "Requires minimum two stations")	
	val stations: Seq[Station] = { schedule map(_._2) }	
}

