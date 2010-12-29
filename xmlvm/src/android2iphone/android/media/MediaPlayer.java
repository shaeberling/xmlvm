/*
 * Copyright (c) 2004-2009 XMLVM --- An XML-based Programming Language
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA.
 * 
 * For more information, visit the XMLVM Home Page at http://www.xmlvm.org
 */

package android.media;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.xmlvm.iphone.AVAudioPlayer;
import org.xmlvm.iphone.AVAudioPlayerDelegate;
import org.xmlvm.iphone.NSData;
import org.xmlvm.iphone.NSError;
import org.xmlvm.iphone.NSErrorHolder;
import org.xmlvm.iphone.NSMutableData;

public class MediaPlayer {

    class AudioPlayerDelegate implements AVAudioPlayerDelegate {

        private WeakReference<MediaPlayer> mediaPlayer;


        public AudioPlayerDelegate(MediaPlayer mediaPlayer) {
            this.mediaPlayer = new WeakReference<MediaPlayer>(mediaPlayer);
        }

        @Override
        public void audioPlayerBeginInterruption(AVAudioPlayer player) {
        }

        @Override
        public void audioPlayerDecodeErrorDidOccur(AVAudioPlayer player, NSError error) {
        }

        @Override
        public void audioPlayerDidFinishPlaying(AVAudioPlayer player, boolean successfully) {
            if (onCompletionListener != null) {
                onCompletionListener.onCompletion(mediaPlayer.get());
            }
        }

        @Override
        public void audioPlayerEndInterruption(AVAudioPlayer player) {
        }

    }


    public static interface OnCompletionListener {
        abstract void onCompletion(MediaPlayer mp);
    }


    private static final int     BUF_SIZE             = 32767;

    private AVAudioPlayer        player               = null;
    private boolean              looping              = false;
    private NSData               data                 = null;
    private AudioPlayerDelegate  delegate             = null;
    private double               currentPosition      = 0.0d;
    private OnCompletionListener onCompletionListener = null;


    public MediaPlayer() {
        delegate = new AudioPlayerDelegate(this);
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public void setDataSource(FileDescriptor fd, long offset, long length) throws IOException {
        FileInputStream fis = new FileInputStream(fd);
        data = readData(fis, offset, length);
        fis.close();
        createPlayer();
    }

    public void setAudioStreamType(int streamtype) {
        // There's nothing appropriate on the iPhone so this implementation
        // will remain empty
    }

    public void prepare() {
        if (player == null) {
            throw new IllegalStateException("Player not initialized");
        }
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
        if (player != null) {
            player.setNumberOfLoops(looping ? -1 : 0);
        }
    }

    public void setVolume(float leftVolume, float rightVolume) {
        if (player != null) {
            player.setVolume((leftVolume + rightVolume) / 2.0f);
        } else {
            throw new IllegalStateException("Player not initialized");
        }
    }

    public void start() throws IllegalStateException {
        if (player != null) {
            if (currentPosition != 0.0d) {
                player.setCurrentTime(currentPosition);
            }
            
            player.play();
        } else {
            throw new IllegalStateException("Player not initialized");
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
            currentPosition = 0.0d;

            // Create new player
            try {
                createPlayer();
            } catch (IOException exc) {
                // Should not happen since all data has already been loaded
            }
        } else {
            throw new IllegalStateException("Player not initialized");
        }
    }

    public void pause() {
        if (player != null) {
            player.stop();
            currentPosition = player.getCurrentTime();

            // Create new player
            try {
                createPlayer();
            } catch (IOException exc) {
                // Should not happen since all data has already been loaded
            }
        } else {
            throw new IllegalStateException("Player not initialized");
        }
    }

    public void seekTo(int msec) {
        if (player != null) {
            currentPosition = Math.round(msec / 1000);

            if (player.isPlaying()) {
                player.setCurrentTime(currentPosition);
            }
        } else {
            throw new IllegalStateException("Player not initialized");
        }
    }

    public void release() {
        if (player != null) {
            player.stop();
            player.setDelegate(null);
            player = null;
        }
        
        data = null;
        delegate = null;
        onCompletionListener = null;
    }

    /**
     * @param onCompletionListener
     */
    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    private void createPlayer() throws IOException {
        NSErrorHolder error = new NSErrorHolder();
        player = AVAudioPlayer.audioPlayerWithData(data, error);
        if (player == null) {
            throw new IOException(error.description());
        }

        player.setNumberOfLoops(looping ? -1 : 0);
        player.setDelegate(delegate);
    }

    private NSData readData(InputStream is, long offset, long length) {
        long remaining = length;
        NSMutableData nsData = new NSMutableData();
        try {

            if (offset > 0) {
                is.skip(offset);
            }

            int bytesRead;
            do {
                int toRead = remaining == -1 ? BUF_SIZE : (int) Math.min(BUF_SIZE, remaining);
                byte[] b = new byte[toRead];
                bytesRead = is.read(b, 0, toRead);

                if (bytesRead > 0) {
                    nsData.appendBytes(b);
                }

                if (remaining > 0) {
                    remaining -= bytesRead;
                }
            } while (bytesRead > 0);
        } catch (IOException e) {
            // Do nothing, just return what we've read so far
        }

        return nsData;
    }

}
