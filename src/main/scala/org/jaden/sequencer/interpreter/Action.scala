package org.jaden.sequencer.interpreter

abstract class Action(id: String, name: String) {
  def getId(): String = id
}

case class SimpleAction(id: String, name: String, payload: String) extends Action(id, name)
case class ParallelAction(id: String, actions: Seq[Action]) extends Action(id, "Parallel")
case class SequentialAction(id: String, actions: Seq[Action]) extends Action(id, "Sequential")

final case class NotifyDone(actionId: String)
