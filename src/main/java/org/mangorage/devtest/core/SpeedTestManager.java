package org.mangorage.devtest.core;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import fr.bmartel.speedtest.utils.SpeedTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class SpeedTestManager implements ISpeedTestListener {
    private static final SpeedTestManager manager = new SpeedTestManager();

    public static SpeedTestManager getInstance() {
        return manager;
    }

    private final SpeedTestSocket socket = new SpeedTestSocket();
    private final List<ISpeedTestHook> hooks = new CopyOnWriteArrayList<>();

    private SpeedTestManager() {
        socket.addSpeedTestListener(this);
    }

    public void hook(ISpeedTestHook hook) {
        hooks.add(hook);
    }

    public void startDownload(String url, int time) {
        if (time <= 0) {
            socket.startDownload(url);
        } else {
            socket.startFixedDownload(url, time * 1000);
        }
    }

    public void startUpload(String url, int time) {
        String fileName = SpeedTestUtils.generateFileName() + ".txt";
        if (time <= 0) {
            socket.startUpload(url + fileName, 1_048_576 * 100);
        } else {
            socket.startFixedUpload(url + fileName, 1_048_576 * 100, time * 1000);
        }
    }

    public void stop() {
        socket.forceStopTask();
    }


    @Override
    public void onCompletion(SpeedTestReport report) {
        hooks.forEach(hook -> hook.onCompleted(report.getTransferRateBit().longValue()));
    }

    @Override
    public void onProgress(float percent, SpeedTestReport report) {
        hooks.forEach(hook -> hook.onProgress(report.getTransferRateBit().longValue(), report.getProgressPercent()));
    }

    @Override
    public void onError(SpeedTestError speedTestError, String errorMessage) {
        hooks.forEach(hook -> hook.onError(speedTestError, errorMessage));
    }

}
