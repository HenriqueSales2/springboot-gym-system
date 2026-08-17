import React, { useState, useRef, useEffect } from "react";
import { Link } from 'react-router-dom';
import { FiPower, FiEdit, FiTrash2, FiPlay, FiPause, FiSkipForward, FiSkipBack } from 'react-icons/fi';
import playlist from "../../utils/playlist.js";

import './styles.css';
import '../../player.css'; 

import logoImage from '../../assets/images/logo.png';

export default function Workout() {

    const [isPlaying, setIsPlaying] = useState(false);
    const [currentSongIndex, setCurrentSongIndex] = useState(0);
    const [currentTime, setCurrentTime] = useState(0);
    const [duration, setDuration] = useState(0);

    const audioRef = useRef(null);

    const toggleMusic = () => {
        if (isPlaying) {
            audioRef.current.pause();
        } else {
            audioRef.current.play();
        }
        setIsPlaying(!isPlaying);
    };

    // next music
    const nextTrack = () => {
        setCurrentSongIndex((indexAtual) => (indexAtual + 1) % playlist.length);
    };

    // back music
    const prevTrack = () => {
        setCurrentSongIndex((indexAtual) => (indexAtual - 1 + playlist.length) % playlist.length);
    };

    useEffect(() => {
        if (isPlaying) {
            audioRef.current.play();
        }
    }, [currentSongIndex, isPlaying]);

    const handleTimeUpdate = () => setCurrentTime(audioRef.current.currentTime);
    const handleLoadedMetadata = () => setDuration(audioRef.current.duration);
    
    const handleSeek = (e) => {
        const time = Number(e.target.value);
        audioRef.current.currentTime = time;
        setCurrentTime(time);
    };

    const formatTime = (time) => {
        if (time && !isNaN(time)) {
            const minutes = Math.floor(time / 60);
            const seconds = Math.floor(time % 60);
            return `${minutes < 10 ? '0' : ''}${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
        }
        return '00:00';
    };

    const currentTrack = playlist[currentSongIndex];

    return (
        <div className="workout-container">
            <header>
                <img src={logoImage} alt="GymLab"/>
                <span>Welcome the GymLab, <strong>Name</strong>!</span>
                <Link className="button" to="/workouts/new">Add New Workout</Link>
                <button type="button">
                    <FiPower size={18} color="#e4544b"/>    
                </button>
            </header>

            <h1>Registered Workouts</h1>
            <ul>
                <li>
                    <strong>Exercise Name:</strong>
                    <p>Alternating Dumbbell Curl</p>
                    <strong>Muscle Group:</strong>
                    <p>Biceps</p>
                    <strong>Equipment:</strong>
                    <p>Dumbbells</p>
                    <strong>Difficulty:</strong>
                    <p>Beginner</p>
                        <button type="button" className="edit">
                            <FiEdit size={20} color="#4053bd" />
                        </button>
                        <button type="button" className="delete">
                            <FiTrash2 size={20} color="#e4544b" />
                        </button>
                </li>

                <li>
                    <strong>Exercise Name:</strong>
                    <p>Alternating Dumbbell Curl</p>
                    <strong>Muscle Group:</strong>
                    <p>Biceps</p>
                    <strong>Equipment:</strong>
                    <p>Dumbbells</p>
                    <strong>Difficulty:</strong>
                    <p>Beginner</p>
                        <button type="button" className="edit">
                            <FiEdit size={20} color="#4053bd" />
                        </button>
                        <button type="button" className="delete">
                            <FiTrash2 size={20} color="#e4544b" />
                        </button>
                </li>

                <li>
                    <strong>Exercise Name:</strong>
                    <p>Alternating Dumbbell Curl</p>
                    <strong>Muscle Group:</strong>
                    <p>Biceps</p>
                    <strong>Equipment:</strong>
                    <p>Dumbbells</p>
                    <strong>Difficulty:</strong>
                    <p>Beginner</p>
                        <button type="button" className="edit">
                            <FiEdit size={20} color="#4053bd" />
                        </button>
                        <button type="button" className="delete">
                            <FiTrash2 size={20} color="#e4544b" />
                        </button>
                </li>

                <li>
                    <strong>Exercise Name:</strong>
                    <p>Alternating Dumbbell Curl</p>
                    <strong>Muscle Group:</strong>
                    <p>Biceps</p>
                    <strong>Equipment:</strong>
                    <p>Dumbbells</p>
                    <strong>Difficulty:</strong>
                    <p>Beginner</p>
                        <button type="button" className="edit">
                            <FiEdit size={20} color="#4053bd" />
                        </button>
                        <button type="button" className="delete">
                            <FiTrash2 size={20} color="#e4544b" />
                        </button>
                </li>

                <li>
                    <strong>Exercise Name:</strong>
                    <p>Alternating Dumbbell Curl</p>
                    <strong>Muscle Group:</strong>
                    <p>Biceps</p>
                    <strong>Equipment:</strong>
                    <p>Dumbbells</p>
                    <strong>Difficulty:</strong>
                    <p>Beginner</p>
                        <button type="button" className="edit">
                            <FiEdit size={20} color="#4053bd" />
                        </button>
                        <button type="button" className="delete">
                            <FiTrash2 size={20} color="#e4544b" />
                        </button>
                </li>

                <li>
                    <strong>Exercise Name:</strong>
                    <p>Alternating Dumbbell Curl</p>
                    <strong>Muscle Group:</strong>
                    <p>Biceps</p>
                    <strong>Equipment:</strong>
                    <p>Dumbbells</p>
                    <strong>Difficulty:</strong>
                    <p>Beginner</p>
                        <button type="button" className="edit">
                            <FiEdit size={20} color="#4053bd" />
                        </button>
                        <button type="button" className="delete">
                            <FiTrash2 size={20} color="#e4544b" />
                        </button>
                </li>

                <li>
                    <strong>Exercise Name:</strong>
                    <p>Alternating Dumbbell Curl</p>
                    <strong>Muscle Group:</strong>
                    <p>Biceps</p>
                    <strong>Equipment:</strong>
                    <p>Dumbbells</p>
                    <strong>Difficulty:</strong>
                    <p>Beginner</p>
                        <button type="button" className="edit">
                            <FiEdit size={20} color="#4053bd" />
                        </button>
                        <button type="button" className="delete">
                            <FiTrash2 size={20} color="#e4544b" />
                        </button>
                </li>

                <li>
                    <strong>Exercise Name:</strong>
                    <p>Alternating Dumbbell Curl</p>
                    <strong>Muscle Group:</strong>
                    <p>Biceps</p>
                    <strong>Equipment:</strong>
                    <p>Dumbbells</p>
                    <strong>Difficulty:</strong>
                    <p>Beginner</p>
                        <button type="button" className="edit">
                            <FiEdit size={20} color="#4053bd" />
                        </button>
                        <button type="button" className="delete">
                            <FiTrash2 size={20} color="#e4544b" />
                        </button>
                </li>

                <li>
                    <strong>Exercise Name:</strong>
                    <p>Alternating Dumbbell Curl</p>
                    <strong>Muscle Group:</strong>
                    <p>Biceps</p>
                    <strong>Equipment:</strong>
                    <p>Dumbbells</p>
                    <strong>Difficulty:</strong>
                    <p>Beginner</p>
                        <button type="button" className="edit">
                            <FiEdit size={20} color="#4053bd" />
                        </button>
                        <button type="button" className="delete">
                            <FiTrash2 size={20} color="#e4544b" />
                        </button>
                </li>

                <li>
                    <strong>Exercise Name:</strong>
                    <p>Alternating Dumbbell Curl</p>
                    <strong>Muscle Group:</strong>
                    <p>Biceps</p>
                    <strong>Equipment:</strong>
                    <p>Dumbbells</p>
                    <strong>Difficulty:</strong>
                    <p>Beginner</p>
                        <button type="button" className="edit">
                            <FiEdit size={20} color="#4053bd" />
                        </button>
                        <button type="button" className="delete">
                            <FiTrash2 size={20} color="#e4544b" />
                        </button>
                </li>

                <li>
                    <strong>Exercise Name:</strong>
                    <p>Alternating Dumbbell Curl</p>
                    <strong>Muscle Group:</strong>
                    <p>Biceps</p>
                    <strong>Equipment:</strong>
                    <p>Dumbbells</p>
                    <strong>Difficulty:</strong>
                    <p>Beginner</p>
                        <button type="button" className="edit">
                            <FiEdit size={20} color="#4053bd" />
                        </button>
                        <button type="button" className="delete">
                            <FiTrash2 size={20} color="#e4544b" />
                        </button>
                </li>

                <li>
                    <strong>Exercise Name:</strong>
                    <p>Alternating Dumbbell Curl</p>
                    <strong>Muscle Group:</strong>
                    <p>Biceps</p>
                    <strong>Equipment:</strong>
                    <p>Dumbbells</p>
                    <strong>Difficulty:</strong>
                    <p>Beginner</p>
                        <button type="button" className="edit">
                            <FiEdit size={20} color="#4053bd" />
                        </button>
                        <button type="button" className="delete">
                            <FiTrash2 size={20} color="#e4544b" />
                        </button>
                </li>
            </ul>
            
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
                        <button className="icon-button" onClick={prevTrack}>
                            <FiSkipBack size={20} />
                        </button>
                        <button className="play-button" onClick={toggleMusic}>
                            {isPlaying ? <FiPause size={20} /> : <FiPlay size={20} style={{marginLeft: '2px'}} />}
                        </button>
                        <button className="icon-button" onClick={nextTrack}>
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
        </div>
    );
}