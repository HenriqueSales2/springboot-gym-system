import React from 'react';
import { FiPlay, FiPause, FiSkipForward, FiSkipBack } from 'react-icons/fi';
import { useAudioPlayer } from '../../hooks/useAudioPlayer.js';

export default function MusicPlayer() {
    const {
        isPlaying,
        currentTime,
        duration,
        audioRef,
        currentTrack,
        toggleMusic,
        nextTrack,
        prevTrack,
        handleTimeUpdate,
        handleLoadedMetadata,
        handleSeek,
        formatTime
    } = useAudioPlayer();

    if (!currentTrack) return null;

    return (
        <>
            <audio 
                ref={audioRef} 
                src={currentTrack.src} 
                onEnded={nextTrack}
                onTimeUpdate={handleTimeUpdate}
                onLoadedMetadata={handleLoadedMetadata}
            />

            <div className="spotify-player">
                <div className="player-info">
                    <img src={currentTrack.cover} alt="Capa" className="cover-image" />
                    <div className="track-details">
                        <strong>{currentTrack.title}</strong>
                        <span>{currentTrack.artist}</span>
                    </div>
                </div>

                <div className="player-controls">
                    <div className="buttons-container">
                        <button type="button" className="icon-button" onClick={prevTrack}>
                            <FiSkipBack size={20} />
                        </button>
                        <button type="button" className="play-button" onClick={toggleMusic}>
                            {isPlaying ? <FiPause size={20} /> : <FiPlay size={20} style={{marginLeft: '2px'}} />}
                        </button>
                        <button type="button" className="icon-button" onClick={nextTrack}>
                            <FiSkipForward size={20} />
                        </button>
                    </div>

                    <div className="progress-container">
                        <span className="time">{formatTime(currentTime)}</span>
                        <input
                            type="range"
                            className="progress-bar"
                            min="0"
                            max={duration || 0}
                            value={currentTime}
                            onChange={handleSeek}
                        />
                        <span className="time">{formatTime(duration)}</span>
                    </div>
                </div>
            </div>
        </>
    );
}