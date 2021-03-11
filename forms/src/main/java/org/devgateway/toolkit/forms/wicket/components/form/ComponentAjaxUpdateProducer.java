package org.devgateway.toolkit.forms.wicket.components.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEventSink;
import org.apache.wicket.event.IEventSource;
import org.devgateway.toolkit.forms.wicket.events.ComponentAjaxUpdateEvent;
import org.springframework.util.Assert;

/**
 * Interface that implements sending of {@link ComponentAjaxUpdateEvent}
 *
 * @author mpostelnicu
 */
public interface ComponentAjaxUpdateProducer extends IEventSource {

    Broadcast BROADCAST_TYPE = Broadcast.BREADTH;

    IEventSink getBroadcastUpdateSink();

    void setBroadcastUpdateSink(IEventSink sink);

    /**
     * Invoke to enable sending, defining a sink for the events
     *
     * @param sink
     */
    default void broadcastUpdate(IEventSink sink) {
        Assert.notNull(sink, "Sink cannot be null!");
        setBroadcastUpdateSink(sink);
    }

    default Component getSourceComponent() {
        return (Component) this;
    }

    default void sendBroadcastUpdate(AjaxRequestTarget target) {
        if (getBroadcastUpdateSink() != null) {
            send(getBroadcastUpdateSink(), BROADCAST_TYPE,
                    new ComponentAjaxUpdateEvent(target, getSourceComponent()));
        }
    }
}
