package org.jaden.sequencer.executor

import akka.actor.Actor
import akka.pattern.pipe
import org.jaden.sequencer.interpreter.{Action, SimpleAction}
import org.jaden.sequencer.state.Done

import scala.concurrent.Future

class SimpleActionActor() extends Actor {
  import context.dispatcher

  override def receive: Receive = {
    case SimpleAction(id, name, payload) =>
      val future: Future[Done] = Future {
        Thread.sleep(1000)
        Done(id)
      }

      future pipeTo self

      println(s"Sender: $sender")
    case Done(actionId) =>
      println(s"Action completed for $actionId, sending back to $sender")
      context.parent forward Done(actionId)
      context.stop(self)
  }
}
