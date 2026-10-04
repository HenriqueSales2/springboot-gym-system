import { useState, useRef, useEffect } from "react";
import playlist from "../utils/playlist.js";

export function useAudioPlayer() {
    const [isPlaying, setIsPlaying] = useState(false);
    const [currentSongIndex, setCurrentSongIndex] = useState(0);
    const [currentTime, setCurrentTime] = useState(0);
    const [duration, setDuration] = useState(0);

    const audioRef = useRef(null);

    const toggleMusic = () => {
        if (isPlaying) {
            audioRef.current?.pause();
        } else {
            audioRef.current?.play();
        }
        setIsPlaying(!isPlaying);
    };

    const nextTrack = () => {
        setCurrentSongIndex((indexAtual) => (indexAtual + 1) % playlist.length);
    };

    const prevTrack = () => {
        setCurrentSongIndex((indexAtual) => (indexAtual - 1 + playlist.length) % playlist.length);
    };

    useEffect(() => {
        if (isPlaying) {
            audioRef.current?.play();
        }
    }, [currentSongIndex, isPlaying]);

    const handleTimeUpdate = () => setCurrentTime(audioRef.current?.currentTime || 0);
    const handleLoadedMetadata = () => setDuration(audioRef.current?.duration || 0);

    const handleSeek = (e) => {
        const time = Number(e.target.value);
        if (audioRef.current) {
            audioRef.current.currentTime = time;
        }
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

    return {
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
    };
}