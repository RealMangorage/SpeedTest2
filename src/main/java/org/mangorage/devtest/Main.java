package org.mangorage.devtest;

import fr.bmartel.speedtest.model.SpeedTestError;
import org.mangorage.devtest.core.BitType;
import org.mangorage.devtest.core.ISpeedTestHook;
import org.mangorage.devtest.core.SpeedTestManager;
import org.mangorage.devtest.gui.SpeedTestGUI;


public class Main implements ISpeedTestHook {
    // Function to convert bytes to megabytes

    public static void main(String[] args) {
        if (args.length == 0) {
            SpeedTestGUI.create();
        } else {
            SpeedTestManager.getInstance().hook(new Main());
            SpeedTestManager.getInstance().startDownload(args[0], 0);
        }
    }

    @Override
    public void onCompleted(long bits) {
        BitType.Result result = BitType.getLargestSize(bits, BitType.Type.BIT);
        String resultString = result.getRoundedString();
        System.out.println("(Final) Download: %sps".replace("%s", resultString));
    }

    @Override
    public void onProgress(long bits, float progressPercent) {
        BitType.Result result = BitType.getLargestSize(bits, BitType.Type.BIT);
        String resultString = result.getRoundedString();
        System.out.println("Download: %sps".replace("%s", resultString));
    }

    @Override
    public void onError(SpeedTestError error, String errorMessage) {
        System.out.println(error.name() + " -> " + errorMessage);
    }
}
