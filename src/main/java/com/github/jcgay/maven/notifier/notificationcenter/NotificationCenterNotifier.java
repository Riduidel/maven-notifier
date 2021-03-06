package com.github.jcgay.maven.notifier.notificationcenter;

import com.github.jcgay.maven.notifier.AbstractCustomEventSpy;
import com.github.jcgay.maven.notifier.Configuration;
import com.github.jcgay.maven.notifier.Notifier;
import com.github.jcgay.maven.notifier.Status;
import com.github.jcgay.maven.notifier.executor.Executor;
import com.github.jcgay.maven.notifier.executor.RuntimeExecutor;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import org.apache.maven.execution.MavenExecutionResult;
import org.codehaus.plexus.component.annotations.Component;

@Component(role = Notifier.class, hint = "notification-center")
public class NotificationCenterNotifier extends AbstractCustomEventSpy {

    private static final String CMD_MESSAGE = "-message";
    private static final String CMD_TITLE = "-title";
    private static final String CMD_SUBTITLE = "-subtitle";
    private static final String CMD_GROUP = "-group";
    private static final String CMD_ACTIVATE = "-activate";
    private static final String CMD_SOUND = "-sound";
    private static final String CMD_CONTENT_IMAGE = "-contentImage";
    private static final String GROUP = "maven";

    private final Executor executor;

    public NotificationCenterNotifier() {
        this.executor = new RuntimeExecutor();
    }

    @VisibleForTesting
    NotificationCenterNotifier(Executor executor, Configuration configuration) {
        this.executor = executor;
        this.configuration = configuration;
    }

    @Override
    public void onEvent(MavenExecutionResult event) {
        super.onEvent(event);
        executor.exec(buildCommand(event));
    }

    @Override
    protected String buildNotificationMessage(MavenExecutionResult result) {
        return buildShortDescription(result);
    }

    private String[] buildCommand(MavenExecutionResult result) {
        Status status = getBuildStatus(result);

        String[] commands = new String[configuration.getNotificationCenterSound() == null ? 13 : 15];
        commands[0] = configuration.getNotificationCenterPath();
        commands[1] = CMD_TITLE;
        commands[2] = result.getProject().getName();
        commands[3] = CMD_SUBTITLE;
        commands[4] = status.message();
        commands[5] = CMD_MESSAGE;
        commands[6] = buildNotificationMessage(result);
        commands[7] = CMD_GROUP;
        commands[8] = GROUP;
        commands[9] = CMD_ACTIVATE;
        commands[10] = configuration.getNotificationCenterActivate();
        commands[11] = CMD_CONTENT_IMAGE;
        commands[12] = status.asPath();
        if (configuration.getNotificationCenterSound() != null) {
            commands[13] = CMD_SOUND;
            commands[14] = configuration.getNotificationCenterSound();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Will execute command line: " + Joiner.on(" ").skipNulls().join(commands));
        }

        return commands;
    }
}
