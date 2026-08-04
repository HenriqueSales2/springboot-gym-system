import React, { useState, useRef, useEffect } from "react";
import { Link } from 'react-router-dom';
import {FiPower, FiEdit, FiTrash2 } from 'react-icons/fi';

import './styles.css';

import logoImage from '../../assets/logo.png';

import StyleTaylorSwift from '../../assets/music/Style-TaylorSwift.mp3';
import BlankSpaceTaylorSwift from '../../assets/music/BlankSpace-TaylorSwift.mp3';
import AugustTaylorSwift from '../../assets/music/August-TaylorSwift.mp3';
import OhYeahSteveLacy from '../../assets/music/ohYeah-SteveLacy.mp3';
import CoolfortheSummerDemiLovato from '../../assets/music/CoolfortheSummer-DemiLovato.mp3';
import MirrorsRadioEditJustinTimberlake from '../../assets/music/MirrorsRadioEdit-JustinTimberlake.mp3';
import ANSIEDADECefa from '../../assets/music/ANSIEDADECefa.mp3';
import LABIRINTOCefa from '../../assets/music/LABIRINTOCefa.mp3';

export default function Workout() {

     const playlist = [
            StyleTaylorSwift,
            BlankSpaceTaylorSwift,
            AugustTaylorSwift,
            OhYeahSteveLacy,
            CoolfortheSummerDemiLovato,
            MirrorsRadioEditJustinTimberlake, 
            ANSIEDADECefa, 
            LABIRINTOCefa
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
        <div className="workout-container">
            <header>
                <img src={logoImage} alt="GymLab"/>
                <span>Welcome the GymLab, <strong>Name</strong>!</span>
                <Link className="button" to="workout/new">Add New Workout</Link>
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