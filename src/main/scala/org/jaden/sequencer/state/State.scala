package org.jaden.sequencer.state

abstract class State(actionId: String, name: String)

case class Started(actionId: String) extends State(actionId, "Started")
case class Done(actionId: String) extends State(actionId, "Done")
case class Failed(actionId: String, message: String) extends State(actionId, "Failed")
case class Interrupted(actionId: String) extends State(actionId, "Interrupted")
