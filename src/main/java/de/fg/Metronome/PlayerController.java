package de.fg.Metronome;

/**
 * Controls the Audio-Thread.
 */
public class PlayerController extends Thread {


    private boolean playing = false;
    private Player player = new Player();

    /**
     * Starts the Audio-Thread or shuts it down.
     * Invocations while the Audio-Thread is about to shut down but still alive are ignored to prevent two
     * Audio-Threads from running simultaneously.
     */
    public synchronized void play() {
        if (!playing && !player.isAlive()) {
            player = new Player();
            player.start();
            playing = true;

        } else {
            player.gracefullyTerminate();
            playing = false;
        }
    }
}
