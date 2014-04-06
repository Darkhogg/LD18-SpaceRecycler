package es.darkhogg.ld18;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum Sound {
    SHOOT("/es/darkhogg/snd/shoot.wav"), HIT("/es/darkhogg/snd/hit.wav"), EXPLOSION("/es/darkhogg/snd/explosion.wav"),
    DEATH("/es/darkhogg/snd/death.wav"), GRAB("/es/darkhogg/snd/grab.wav"), RELEASE("/es/darkhogg/snd/release.wav"),
    HURT("/es/darkhogg/snd/hurt.wav");

    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(new ThreadFactory() {
        @Override
        public Thread newThread (Runnable task) {
            Thread t = new Thread(task);
            t.setDaemon(true);
            t.setName("sound-thread-" + t.hashCode());
            return t;
        }
    });

    private final byte[] data;

    private Sound (URL url) {
        System.out.printf("Loading Sound.%s from '%s'...%n", this, url);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (InputStream in = url.openStream()) {
            // Copy input to output
            byte[] buf = new byte[4096];
            int r;
            while ((r = in.read(buf)) != -1) {
                out.write(buf, 0, r);
            }

        } catch (IOException exc) {
            exc.printStackTrace();
        } finally {
            data = out.toByteArray();
        }
    }

    private Sound (String str) {
        this(Sound.class.getResource(str));
    }

    private class SoundPlayer implements Runnable {
        @Override
        public void run () {
            try {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
                AudioFormat audioFormat = audioInput.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

                SourceDataLine audioLine;

                audioLine = (SourceDataLine) AudioSystem.getLine(info);

                audioLine.open(audioFormat);

                audioLine.start();

                int bytesRead = 0;
                byte[] audioData = new byte[1024 * 128]; // 128 KiB

                try {
                    while (bytesRead != -1) {
                        bytesRead = audioInput.read(audioData);

                        if (bytesRead >= 0) {
                            audioLine.write(audioData, 0, bytesRead);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    audioLine.drain();
                    audioLine.close();
                }
            } catch (LineUnavailableException e1) {
                e1.printStackTrace();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void play () {
        EXECUTOR.submit(new SoundPlayer());
    }
}
