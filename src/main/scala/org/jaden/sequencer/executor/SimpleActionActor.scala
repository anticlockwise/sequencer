package org.jaden.sequencer.executor

import akka.actor.ActorLogging
import akka.persistence.fsm.PersistentFSM
import org.jaden.sequencer.interpreter.{Action, NotifyDone, SimpleAction}
import org.jaden.sequencer.state._

import scala.concurrent.Future

class SimpleActionActor(actionId: String) extends PersistentFSM[State, Data, Action] with ActorLogging {

  import context.dispatcher

  startWith(Starting, Uninitialized)

  when(Starting) {
    case Event(SimpleAction(id, name, payload), Uninitialized) => {
      log.info(s"Action $actionId received starting event")
      Future {
        Thread.sleep(1000)
        self ! NotifyDone(actionId)
      }
      goto(Started)
    }
  }

  when(Started) {
    case Event(NotifyDone(id), Uninitialized) =>
      log.info(s"Action $actionId done")
      context.parent ! NotifyDone(actionId)
      stop()
  }
}
