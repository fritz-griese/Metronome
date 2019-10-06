package de.fg.Metronome.deprecated;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class Player extends Thread {

    private int bpm = 80;
    private double frequencyOfTick = 500;
    private double frequencyOfFirstTick = 1000;
    private int sampleRate = 44100;
    private int bufferSize = 2000;

    private byte[] buffer;
    private int tickBufferSize;
    private double[] tickBuffer;
    private SourceDataLine line = null;
    private int tickLength = 4100;
    private boolean playing = true;

    Player() {
        tickBufferSize = (int) ((60 / (double) bpm) * (double) sampleRate) * 4;
    }

    @Override
    public void run() {
        createPlayer();
        generateTick();
        buffer = new byte[bufferSize];
        int b = 0;
        int t = 0;

        while (playing) {
            if (t >= tickBufferSize)
                t = 0;

            short maxSample = (short) ((tickBuffer[t++] * Short.MAX_VALUE));
            buffer[b++] = (byte) (maxSample & 0x00ff);
            buffer[b++] = (byte) ((maxSample & 0xff00) >>> 8);

            if (b >= bufferSize) {
                line.write(buffer, 0, buffer.length);
                b = 0;
            }
        }
        destroyPlayer();
        System.out.println("Audio Thread " + Thread.currentThread() + " has finished.");
    }

    private void generateTick() {
        tickBuffer = new double[tickBufferSize];
        int noTickLength = (tickBufferSize - (4 * tickLength)) / 4;
        int position = 0;

        for (int tick = 1; tick <= 4; ++tick) {
            if (tick == 1) {
                for (int sound = 0; sound < tickLength; sound++) {
                    tickBuffer[sound] = Math.sin(2 * Math.PI * sound / (sampleRate / frequencyOfFirstTick));
                }
            } else {
                for (int sound = position; sound < position + tickLength; sound++) {
                    tickBuffer[sound] = Math.sin(2 * Math.PI * sound / (sampleRate / frequencyOfTick));
                }
            }
            position += tickLength;
            for (int noSound = position; noSound < position + noTickLength; noSound++) {
                tickBuffer[noSound] = 0;
            }
            position += noTickLength;
        }
    }

    /*
    private void generateTick(){
        tickBuffer = new double[tickBufferSize];
        for (int i = 0; i < tickLength; i++)
            tickBuffer[i] = Math.sin(2 * Math.PI * i / (sampleRate/frequencyOfTick));
        for (int i = tickLength; i < tickBufferSize; i++)
            tickBuffer[i] = 0;
    }*/

    private void createPlayer() {
        AudioFormat af = new AudioFormat(sampleRate, 16, 1, true, false);
        try {
            line = AudioSystem.getSourceDataLine(af);
            line.open(af);
            line.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void destroyPlayer() {
        line.drain();
        line.close();
    }

    public void gracefullyTerminate() {
        playing = false;
    }
}
