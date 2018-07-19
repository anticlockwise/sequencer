package org.jaden.sequencer.state

import akka.persistence.fsm.PersistentFSM.FSMState
import org.jaden.sequencer.interpreter.Action

abstract class State extends FSMState

case object Starting extends State {
  override def identifier: String = "Starting"
}
case object Started extends State {
  override def identifier: String = "Started"
}
case object Done extends State {
  override def identifier: String = "Done"
}

sealed trait Data

case object Uninitialized extends Data
final case class StartedActions(actions: Seq[Action], states: Map[String, State]) extends Data
