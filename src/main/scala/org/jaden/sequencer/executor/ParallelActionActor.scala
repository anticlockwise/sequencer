package org.jaden.sequencer.executor

import akka.actor.FSM
import akka.persistence.fsm.PersistentFSM
import org.jaden.sequencer.interpreter.{Action, NotifyDone, ParallelAction}
import org.jaden.sequencer.state._

import scala.reflect.ClassTag

class ParallelActionActor(actionId: String) extends PersistentFSM[State, Data, Action] {
  startWith(Starting, Uninitialized)

  when(Starting) {
    case Event(ParallelAction(id, actions), Uninitialized) =>
      log.info(s"Action $actionId received starting event")
      actions.foreach(action => {
        val executor = Executor.getActionActor(action)
        context.actorOf(executor, executor.actorClass().getName() + action.getId()) ! action
      })
      goto(Started) replying StartedActions(actions, Map.empty)
  }

  when(Started) {
    case Event(NotifyDone(id), StartedActions(actions, states)) =>
      val newStates = states + (id -> Done)
      val allDone = states.values.forall(s => s == Done) && (newStates.size == actions.size)
      if (allDone) {
        log.info(s"Action $actionId done")
        context.parent ! NotifyDone(actionId)
        stop()
      } else {
        stay() replying StartedActions(actions, newStates)
      }
  }

  override def applyEvent(domainEvent: Action, currentData: Data): Data = ???

  override def persistenceId: String = ???
}
