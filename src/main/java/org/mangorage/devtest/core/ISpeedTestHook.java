package org.mangorage.devtest.core;

import fr.bmartel.speedtest.model.SpeedTestError;

public interface ISpeedTestHook {
    void onCompleted(long bits);
    void onProgress(long bits, float progressPercent);
    void onError(SpeedTestError error, String errorMessage);
}
