import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import { FiPower, FiEdit, FiTrash2, FiPlay, FiPause, FiSkipForward, FiSkipBack } from 'react-icons/fi';
import playlist from "../../utils/playlist.js";
import './styles.css';
import '../../player.css'; 

import api from '../../services/api.js';

import logoImage from '../../assets/images/logo.png';

export default function Login() {


    
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


        const [username, setUsername] = useState('');
        const [password, setPassword] = useState('');

        const navigate = useNavigate();

        async function login(e) {
            e.preventDefault();

            const data = {
                username, 
                password,
            };

            try {
                const response = await api.post('/auth/signin', data);

                localStorage.setItem('username', username);
                localStorage.setItem('accessToken', response.data.accessToken);

                navigate('/workouts');
                
            } catch (error) {
                alert('Login failed! Try again!');
            }
        };

    return (
        <div className="login-container">

            <section className="form">
                <img src={logoImage} className="logo" alt="GymLab"/>
                <form onSubmit={login}>
                        <div className="input-group">
                            <label htmlFor="username">Username</label>
                            <input 
                            type="text" id="username"
                            value= {username}
                            onChange={e => setUsername(e.target.value)}
                            />
                        </div>
                        <div className="input-group">
                            <label htmlFor="password">Password</label>
                            <input 
                            type="password" id="password" 
                            value= {password}
                            onChange={e => setPassword(e.target.value)}
                            />
                        </div>
                        <div className="button-group">
                            <button type="submit" className="button">Sign in</button>
                        </div>
                </form>
            </section>

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