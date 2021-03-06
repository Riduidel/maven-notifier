package com.github.jcgay.maven.notifier;

import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.MavenExecutionResult;

public interface Notifier {

    /**
     * Indicate if the notifier is an implementation for the provided parameter.
     *
     * @return {@code true} if the notifier should be used to notify the build status.
     */
    boolean shouldNotify();

    /**
     * Initializes the spy.
     *
     * @param context The event spy context, never {@code null}.
     */
    void init(EventSpy.Context context);


    /**
     * Notifies the notifier of build result.
     */
    void onEvent(MavenExecutionResult event);

    /**
     * Notifies the notifier of Maven's termination, allowing it to free any resources allocated by it.
     */
    void close();
}
