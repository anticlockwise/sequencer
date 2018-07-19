package org.jaden.sequencer

import akka.actor.ActorSystem
import org.jaden.sequencer.executor.Executor
import org.jaden.sequencer.interpreter.{ParallelAction, SequentialAction, SimpleAction}

object Sequencer {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("Sequencer")

    val actions = List(
      SequentialAction("3", List(SimpleAction("1", "SimpleAction1", "SimplePayload2"),
        SimpleAction("2", "SimpleAction2", "SimplePayload2"))),
      SimpleAction("4", "SimpleAction3", "SimplePayload3"))
    val action = ParallelAction("0", actions)
    val actor = system.actorOf(Executor.getActionActor(action), "sequencer-actor")

    actor ! action
  }
}
