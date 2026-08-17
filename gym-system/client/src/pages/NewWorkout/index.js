import React, { useState, useRef, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { FiPlay, FiPause, FiSkipForward, FiSkipBack , FiArrowLeft } from 'react-icons/fi';
import playlist from "../../utils/playlist.js";
import api from '../../services/api';
import './styles.css';
import '../../player.css'; 

import logoImage from '../../assets/images/logo.png';

export default function NewWorkout() {
 
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

        const [id, setId] = useState(null);    
        const [exerciseName, setExerciseName] = useState('');
        const [muscleGroup, setMuscleGroup] = useState('');
        const [equipment, setEquipment] = useState('');
        const [difficulty, setDifficulty] = useState('');

        const username = localStorage.getItem('username');
        const accessToken = localStorage.getItem('accessToken');
        
                const navigate = useNavigate();
        
                async function createNewWorkout(e) {
                    e.preventDefault();

                    const data = {
                        exerciseName,
                        muscleGroup,
                        equipment,
                        difficulty,
                    };

                    const header = {
                        headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                };

                try {
                    await api.post('/api/workout/v1', data, header);
                    navigate('/workouts');
                } catch (error) {
                    alert('Error while recording Workout! Try again!');
                    }
                }



    return (
        <div className="new-workout-container">
            <div className="content">
                <section className = "form">
                    <img src ={logoImage} alt="GymLab"/>
                    <h1>Create New Workout</h1>
                    <p>Enter the details for your new workout and click on the "Create" button.</p>
                    <Link className="back-link" to="/workouts">
                        <FiArrowLeft size={16} color="#E02041"/>
                        Home
                    </Link>
                </section>
                <form onSubmit={createNewWorkout}>
                    <input
                        placeholder="Exercise Name"
                        value={exerciseName}
                        onChange={e => setExerciseName(e.target.value)} 
                        />
                    <input
                        placeholder="Muscle Group" 
                        value={muscleGroup}
                        onChange={e => setMuscleGroup(e.target.value)}
                        />
                    <input
                        placeholder="Equipment" 
                        value={equipment}
                        onChange={e => setEquipment(e.target.value)}/>
                    <input
                        placeholder="Difficulty" 
                        value={difficulty}
                        onChange={e => setDifficulty(e.target.value)}/>
                    <button className="button" type="submit">Create</button>
                </form>

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
        </div>
    );
}