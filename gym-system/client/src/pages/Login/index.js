import React, { useState, useRef, useEffect } from "react";
import './styles.css';

import logoImage from '../../assets/logo.png';

import StyleTaylorSwift from '../../assets/music/Style-TaylorSwift.mp3';
import BlankSpaceTaylorSwift from '../../assets/music/BlankSpace-TaylorSwift.mp3';
import AugustTaylorSwift from '../../assets/music/August-TaylorSwift.mp3';
import OhYeahSteveLacy from '../../assets/music/ohYeah-SteveLacy.mp3';
import CoolfortheSummerDemiLovato from '../../assets/music/CoolfortheSummer-DemiLovato.mp3';
import MirrorsRadioEditJustinTimberlake from '../../assets/music/MirrorsRadioEdit-JustinTimberlake.mp3';

export default function Login() {
 
    const playlist = [
        StyleTaylorSwift,
        BlankSpaceTaylorSwift,
        AugustTaylorSwift,
        OhYeahSteveLacy,
        CoolfortheSummerDemiLovato,
        MirrorsRadioEditJustinTimberlake
    ];

    const [isPlaying, setIsPlaying] = useState(false);
    
    const [currentSongIndex, setCurrentSongIndex] = useState(0);

    const audioRef = useRef(null);

    const toggleMusic = () => {
        if (isPlaying) {
            audioRef.current.pause();
            setIsPlaying(false);
        } else {
            audioRef.current.play();
            setIsPlaying(true);
        }
    };
 
    const nextTrack = () => {
        setCurrentSongIndex((indexAtual) => (indexAtual + 1) % playlist.length);
    };

    useEffect(() => {
        if (isPlaying) {
            audioRef.current.play();
        }
    }, [currentSongIndex]);

    return (
        <div className="login-container">

            <section className="form">
                <img src={logoImage} className="logo" alt="Logo Gym System"/>
                <form>
                        <div className="input-group">
                            <label htmlFor="username">Username</label>
                            <input type="text" id="username"/>
                        </div>
                        <div className="input-group">
                            <label htmlFor="password">Password</label>
                            <input type="password" id="password" />
                        </div>
                        <div className="button-group">
                            <button type="submit" className="button">Sign in</button>
                        </div>
                </form>
            </section>

            <audio 
                ref={audioRef} 
                src={playlist[currentSongIndex]} 
                onEnded={nextTrack} 
            />

            <div className="music-controls">
                <button 
                    className={`music-button ${!isPlaying ? 'paused' : ''}`} 
                    onClick={toggleMusic}
                >
                    {isPlaying ? "Pause" : "Play"}
                </button>

                <button className="music-button skip-button" onClick={nextTrack}>
                    Next
                </button>
            </div>
        </div>
    );
}