package org.jaden.sequencer.executor

import akka.actor.Props
import org.jaden.sequencer.interpreter.{Action, ParallelAction, SequentialAction, SimpleAction}

object Executor {
  def getActionActor(action: Action): Props = {
    action match {
      case SimpleAction(_, _, _) =>
        Props(new SimpleActionActor)
      case ParallelAction(_, _) =>
        Props(new ParallelActionActor)
      case SequentialAction(_, _) =>
        Props(new SequentialActionActor)
      case _ =>
        throw new RuntimeException(s"Action not recognized: $action")
    }
  }
}
