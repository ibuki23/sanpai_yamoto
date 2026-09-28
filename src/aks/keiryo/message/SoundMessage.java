package aks.keiryo.message;

import java.applet.*;

public class SoundMessage {

    AudioClip mAC;

    public SoundMessage() {
        mAC = null;
    }

    public void Play(String sound_name) {
        try {
            if (mAC != null) {
                mAC.stop();
            }

            mAC = java.applet.Applet.newAudioClip(getClass().getResource(sound_name));
            mAC.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
