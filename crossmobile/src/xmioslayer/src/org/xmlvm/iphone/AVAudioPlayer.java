/* Copyright (c) 2011 by crossmobile.org
 *
 * CrossMobile is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2.
 *
 * CrossMobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CrossMobile; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.xmlvm.iphone;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import java.util.HashMap;
import org.crossmobile.ios2a.ImplementationError;
import org.crossmobile.ios2a.MainActivity;

/**
 *
 * @author teras
 */
public class AVAudioPlayer {

    private static final int MAXSTREAMS = 10;
    //
    private static final AudioManager manager = (AudioManager) MainActivity.current.getSystemService(Context.AUDIO_SERVICE);
    private static final SoundPool soundPool = new SoundPool(MAXSTREAMS, AudioManager.STREAM_MUSIC, 0);
    private static final HashMap<String, Integer> poolindex = new HashMap<String, Integer>();
    //
    private MediaPlayer player;
    private AVAudioPlayerDelegate delegate;
    private float volume = 1;
    private NSURL url;

    public static AVAudioPlayer audioPlayerWithContentsOfURL(NSURL url, NSErrorHolder error) {
        AVAudioPlayer result;
        try {
            error.error = null;
            return new AVAudioPlayer(url);
        } catch (Exception ex) {
            if (error != null) {
                HashMap info = new HashMap();
                info.put(NSError.Key.NSUnderlyingError, ex.getClass().getName());
                info.put(NSError.Key.NSLocalizedDescription, ex.getLocalizedMessage());
                info.put(NSError.Key.NSLocalizedFailureReason, ex.getCause() != null ? ex.getCause().getLocalizedMessage() : ex.getLocalizedMessage());
                error.error = new NSError(NSError.Domain.NSURL, NSError.ErrorCode.NSURL.Unknown, info);
            }
            return null;
        }
    }

    public static AVAudioPlayer audioPlayerWithData(NSData nsData, NSErrorHolder error) {
        throw new ImplementationError();
    }

    private AVAudioPlayer(NSURL url) throws Exception {
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDataSource(url.absoluteString());
        player.prepareAsync();
        this.url = url;
    }

    public boolean play() {
        try {
            if (!player.isPlaying())
                player.start();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean playAtTime(double time) {
        try {

            if (!player.isPlaying())
                player.start();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void stop() {
        try {
            if (player.isPlaying())
                player.stop();
        } catch (Exception e) {
        }
    }

    public void pause() {
        if (player.isPlaying())
            player.pause();
    }

    public void prepareToPlay() {
        throw new ImplementationError();
    }

    public int getNumberOfLoops() {
        throw new ImplementationError();
    }

    public void setNumberOfLoops(int numberOfLoops) {
        throw new ImplementationError();
    }

    public AVAudioPlayerDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(AVAudioPlayerDelegate delegate) {
        this.delegate = delegate;
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public void setCurrentTime(double currentTime) {
        player.seekTo((int) (currentTime + 0.5));
    }

    public double getCurrentTime() {
        return player.getCurrentPosition();
    }

    public void setVolume(float volume) {
        this.volume = volume;
        player.setVolume(volume, volume);
    }

    public float getVolume() {
        return volume;
    }

    public int getNumberOfChannels() {
        NSLog.log("AVAudioPlayer.getNumberOfChannels: always return 2");
        return 2;
    }

    public double getDuration() {
        return player.getDuration();
    }

    public NSURL getURL() {
        return url;
    }

    public NSData getData() {
        throw new ImplementationError();
    }
}
