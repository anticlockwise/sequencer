package org.jaden.sequencer.executor

import akka.actor.{Actor, Props}
import org.jaden.sequencer.interpreter.ParallelAction
import org.jaden.sequencer.state.{Done, Started, State}

class ParallelActionActor() extends Actor {
  var actionStates = Map.empty[String, State]
  var currentAction: String = null;

  override def receive: Receive = {
    case ParallelAction(id, actions) =>
      currentAction = id
      println("Executing parallel actions")
      actions.foreach(action => {
        val executor = Executor.getActionActor(action)
        context.actorOf(executor) ! action
        actionStates += action.getId() -> Started(action.getId())
      })
    case Done(actionId) =>
      actionStates += actionId -> Done(actionId)
      val allDone = actionStates.values.forall(state => state.isInstanceOf[Done])
      if (allDone) {
        context.parent ! Done(currentAction)
        context.stop(self)
      }
  }
}
