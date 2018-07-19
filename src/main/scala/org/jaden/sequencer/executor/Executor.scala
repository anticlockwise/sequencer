package org.jaden.sequencer.executor

import akka.actor.Props
import org.jaden.sequencer.interpreter.{Action, ParallelAction, SequentialAction, SimpleAction}

object Executor {
  def getActionActor(action: Action): Props = {
    action match {
      case SimpleAction(id, _, _) =>
        Props(new SimpleActionActor(id))
      case ParallelAction(id, _) =>
        Props(new ParallelActionActor(id))
      case SequentialAction(id, _) =>
        Props(new SequentialActionActor(id))
      case _ =>
        throw new RuntimeException(s"Action not recognized: $action")
    }
  }
}
