package org.jaden.sequencer.executor

import akka.actor.Actor
import org.jaden.sequencer.interpreter.{Action, SequentialAction}
import org.jaden.sequencer.state.Done

class SequentialActionActor() extends Actor {
  var currentAction: String = null
  var actionSequence: Seq[Action] = Seq.empty[Action]

  override def receive: Receive = {
    case SequentialAction(id, actions) =>
      currentAction = id
      println("Executing sequential actions")
      actionSequence = actions
      val firstAction: Action = actionSequence.head
      val executorProps = Executor.getActionActor(firstAction)
      context.actorOf(executorProps) ! firstAction
    case Done(actionId) =>
      val doneIndex = actionSequence.indexWhere(action => action.getId() == actionId)
      println(s"Done index: $doneIndex")
      if (doneIndex >= actionSequence.length - 1 || doneIndex < 0) {
        context.parent ! Done(currentAction)
        context.stop(self)
      } else {
        val nextAction = actionSequence(doneIndex + 1)
        val executorProps = Executor.getActionActor(nextAction)
        context.actorOf(executorProps) ! nextAction
      }
  }
}
