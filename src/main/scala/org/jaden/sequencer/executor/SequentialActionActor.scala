package org.jaden.sequencer.executor

import akka.actor.FSM
import org.jaden.sequencer.interpreter.{NotifyDone, SequentialAction}
import org.jaden.sequencer.state._

class SequentialActionActor(actionId: String) extends FSM[State, Data] {
  startWith(Starting, Uninitialized)

  when(Starting) {
    case Event(SequentialAction(id, actions), Uninitialized) =>
      log.info(s"Action $actionId received starting event")
      val currentAction = actions.head
      val executor = Executor.getActionActor(currentAction)
      context.actorOf(executor, executor.actorClass().getName() + currentAction.getId()) ! currentAction
      goto(Started) using StartedActions(actions, Map.empty)
  }

  when(Started) {
    case Event(NotifyDone(id), StartedActions(actions, states)) =>
      val newStates = states + (id -> Done)
      val doneIndex = actions.indexWhere(action => action.getId() == id)
      if (doneIndex >= actions.length - 1 || doneIndex < 0) {
        log.info(s"Action $actionId done")
        context.parent ! NotifyDone(actionId)
        stop()
      } else {
        val nextAction = actions(doneIndex + 1)
        val executorProps = Executor.getActionActor(nextAction)
        context.actorOf(executorProps) ! nextAction
        stay() using StartedActions(actions, newStates)
      }
  }

  initialize()
}
