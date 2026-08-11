import React, { useState, useRef, useEffect } from "react";
import { Link } from 'react-router-dom';
import { FiPower, FiEdit, FiTrash2, FiPlay, FiPause, FiSkipForward, FiSkipBack } from 'react-icons/fi';

import './styles.css';
import './player.css'; 

import logoImage from '../../assets/images/logo.png';
import taylorImage1989 from '../../assets/images/1989.avif';
import taylorImageFolklore from '../../assets/images/folklore.avif';
import RadioHead2more2equal5Image from '../../assets/images/2+2=5.jpg';
import AutoRetratoBadLuvImage from '../../assets/images/AUTORETRATO.jpg';
import AWolfAttheDoorImage from '../../assets/images/AWolfAttheDoor.jpg';
import BADLUVSINTOSUAFALTAImage from '../../assets/images/BADLUV-SINTOSUAFALTA.jpg';
import BornToDieLanaDelReyImage from '../../assets/images/BornToDie-LanaDelRey.jpg';
import FranklinHouseImage from '../../assets/images/Brenn-FranklinHouse.jpg';
import CICATRIZESImage from '../../assets/images/CICATRIZES.jpg';
import CicoBuffImage from '../../assets/images/CicoBuff.jpg';
import DeftonesEntombedImage from '../../assets/images/Deftones-Entombed.jpg';
import froufrouANewKindOfLoveImage from '../../assets/images/froufrou-ANewKindOfLove.jpg';
import GirlinRedWeFellInLoveInOctoberImage from '../../assets/images/GirlinRed-WeFellInLoveInOctober.jpg';
import ImpostorSyndromeImage from '../../assets/images/ImpostorSyndrome.jpg';
import JigsawFallingIntoPlaceRadioheadImage from '../../assets/images/JigsawFallingIntoPlace-Radiohead.jpg';
import KCigarettesAfterSexImage from '../../assets/images/K.-CigarettesAfterSex.jpg';
import LanaDelReyBluebirdImage from '../../assets/images/LanaDelRey-Bluebird.jpg';
import LanaDelReyRideImage from '../../assets/images/LanaDelRey-Ride.jpg';
import LetDownImage from '../../assets/images/LetDown.jpg';
import LomlImage from '../../assets/images/loml.jpg';
import LordHuronTheNightWeMetImage from '../../assets/images/LordHuron-TheNightWeMet.jpg';
import MEFAZTAOBEMImage from '../../assets/images/MEFAZTAOBEM.jpg';
import MitskiNobodyImage from '../../assets/images/Mitski-Nobody.jpg';
import No1PartyAnthemImage from '../../assets/images/No.1PartyAnthem.jpg';
import NotAllowedImage from '../../assets/images/NotAllowed.jpg';
import PinegroveNeed2Image from '../../assets/images/Pinegrove-Need2.jpg';
import RadioLanaDelReyImage from '../../assets/images/Radio-LanaDelRey.jpg';
import RadioheadAllINeedImage from '../../assets/images/Radiohead-AllINeed.jpg';
import RadioheadFakePlasticTreesImage from '../../assets/images/Radiohead-FakePlasticTrees.jpg';
import RadioheadManofWarImage from '../../assets/images/Radiohead-ManofWar.jpg';
import ScottStreetImage from '../../assets/images/ScottStreet.jpg';
import SeafretAtlantisImage from '../../assets/images/Seafret-Atlantis.jpg';
import SymphoniaIXImage from '../../assets/images/SymphoniaIX.jpg';
import TaylorSwiftExileImage from '../../assets/images/TaylorSwift-exile.jpg';
import TheMariasNobodyNewImage from '../../assets/images/TheMarias-NobodyNew.jpg';
import TheTouristRadioheadImage from '../../assets/images/TheTourist-Radiohead.jpg';
import ThisSideofParadiseCoyoteTheoryImage from '../../assets/images/ThisSideofParadise-CoyoteTheory.jpg';
import TRADUCAODESAUDADEImage from '../../assets/images/TRADUCAODESAUDADE.jpg';
import VacationsTelephonesImage from '../../assets/images/Vacations-Telephones.jpg';


import StyleTaylorSwift from '../../assets/music/Style-TaylorSwift.mp3';
import BlankSpaceTaylorSwift from '../../assets/music/BlankSpace-TaylorSwift.mp3';
import AugustTaylorSwift from '../../assets/music/August-TaylorSwift.mp3';
import RadioHead2more2equal5 from '../../assets/music/2+2=5.mp3';
import AutoRetratoBadLuv from '../../assets/music/AUTORETRATO.mp3';
import AWolfAttheDoor from '../../assets/music/AWolfAttheDoor.mp3';
import BADLUVSINTOSUAFALTA from '../../assets/music/BADLUV-SINTOSUAFALTA.mp3';
import BornToDieLanaDelRey from '../../assets/music/BornToDie-LanaDelRey.mp3';
import FranklinHouse from '../../assets/music/Brenn-FranklinHouse.mp3';
import CICATRIZES from '../../assets/music/CICATRIZES.mp3';
import CicoBuff from '../../assets/music/CicoBuff.mp3';
import DeftonesEntombed from '../../assets/music/Deftones-Entombed.mp3';
import froufrouANewKindOfLove from '../../assets/music/froufrou-ANewKindOfLove.mp3';
import GirlinRedWeFellInLoveInOctober from '../../assets/music/GirlinRed-WeFellInLoveInOctober.mp3';
import ImpostorSyndrome from '../../assets/music/ImpostorSyndrome.mp3';
import JigsawFallingIntoPlaceRadiohead from '../../assets/music/JigsawFallingIntoPlace-Radiohead.mp3';
import KCigarettesAfterSex from '../../assets/music/K.-CigarettesAfterSex.mp3';
import LanaDelReyBluebird from '../../assets/music/LanaDelRey-Bluebird.mp3';
import LanaDelReyRide from '../../assets/music/LanaDelRey-Ride.mp3';
import LetDown from '../../assets/music/LetDown.mp3';
import Loml from '../../assets/music/loml.mp3';
import LordHuronTheNightWeMet from '../../assets/music/LordHuron-TheNightWeMet.mp3';
import MEFAZTAOBEM from '../../assets/music/MEFAZTAOBEM.mp3';
import MitskiNobody from '../../assets/music/Mitski-Nobody.mp3';
import No1PartyAnthem from '../../assets/music/No.1PartyAnthem.mp3';
import NotAllowed from '../../assets/music/NotAllowed.mp3';
import PinegroveNeed2 from '../../assets/music/Pinegrove-Need2.mp3';
import RadioLanaDelRey from '../../assets/music/Radio-LanaDelRey.mp3';
import RadioheadAllINeed from '../../assets/music/Radiohead-AllINeed.mp3';
import RadioheadFakePlasticTrees from '../../assets/music/Radiohead-FakePlasticTrees.mp3';
import RadioheadManofWar from '../../assets/music/Radiohead-ManofWar.mp3';
import ScottStreet from '../../assets/music/ScottStreet.mp3';
import SeafretAtlantis from '../../assets/music/Seafret-Atlantis.mp3';
import SymphoniaIX from '../../assets/music/SymphoniaIX.mp3';
import TaylorSwiftExile from '../../assets/music/TaylorSwift-exile.mp3';
import TheMariasNobodyNew from '../../assets/music/TheMarias-NobodyNew.mp3';
import TheTouristRadiohead from '../../assets/music/TheTourist-Radiohead.mp3';
import ThisSideofParadiseCoyoteTheory from '../../assets/music/ThisSideofParadise-CoyoteTheory.mp3';
import TRADUCAODESAUDADE from '../../assets/music/TRADUCAODESAUDADE.mp3';
import VacationsTelephones from '../../assets/music/Vacations-Telephones.mp3';




export default function Workout() {

    const playlist = [
        { title: "Style", artist: "Taylor Swift", src: StyleTaylorSwift, cover: taylorImage1989 },
        { title: "Blank Space", artist: "Taylor Swift", src: BlankSpaceTaylorSwift, cover: taylorImage1989 },
        { title: "August", artist: "Taylor Swift", src: AugustTaylorSwift, cover: taylorImageFolklore },
        { title: "2 + 2 = 5", artist: "Radiohead", src: RadioHead2more2equal5, cover: RadioHead2more2equal5Image },
        { title: "AUTO RETRATO", artist: "BADLUV", src: AutoRetratoBadLuv, cover: AutoRetratoBadLuvImage },
        { title: "A Wolf At the Door", artist: "Radiohead", src: AWolfAttheDoor, cover: AWolfAttheDoorImage },
        { title: "SINTO SUA FALTA", artist: "BADLUV", src: BADLUVSINTOSUAFALTA, cover: BADLUVSINTOSUAFALTAImage },
        { title: "Born To Die", artist: "Lana Del Rey", src: BornToDieLanaDelRey, cover: BornToDieLanaDelReyImage },
        { title: "Franklin House", artist: "Brenn!", src: FranklinHouse, cover: FranklinHouseImage },
        { title: "CICATRIZES", artist: "BADLUV", src: CICATRIZES, cover: CICATRIZESImage },
        { title: "Cico Buff", artist: "Cocteau Twins", src: CicoBuff, cover: CicoBuffImage },
        { title: "Entombed", artist: "Deftones", src: DeftonesEntombed, cover: DeftonesEntombedImage },
        { title: "A New Kind of Love", artist: "Frou Frou", src: froufrouANewKindOfLove, cover: froufrouANewKindOfLoveImage },
        { title: "We Fell In Love In October", artist: "Girl in Red", src: GirlinRedWeFellInLoveInOctober, cover: GirlinRedWeFellInLoveInOctoberImage },
        { title: "Impostor Syndrome", artist: "Sidney Gish", src: ImpostorSyndrome, cover: ImpostorSyndromeImage },
        { title: "Jigsaw Falling Into Place", artist: "Radiohead", src: JigsawFallingIntoPlaceRadiohead, cover: JigsawFallingIntoPlaceRadioheadImage },
        { title: "K.", artist: "Cigarettes After Sex", src: KCigarettesAfterSex, cover: KCigarettesAfterSexImage },
        { title: "Bluebird", artist: "Lana Del Rey", src: LanaDelReyBluebird, cover: LanaDelReyBluebirdImage },
        { title: "Ride", artist: "Lana Del Rey", src: LanaDelReyRide, cover: LanaDelReyRideImage },
        { title: "Let Down", artist: "Radiohead", src: LetDown, cover: LetDownImage },
        { title: "loml", artist: "Taylor Swift", src: Loml, cover: LomlImage },
        { title: "The Night We Met", artist: "Lord Huron", src: LordHuronTheNightWeMet, cover: LordHuronTheNightWeMetImage },
        { title: "ME FAZ TÃO BEM", artist: "BADLUV", src: MEFAZTAOBEM, cover: MEFAZTAOBEMImage },
        { title: "Nobody", artist: "Mitski", src: MitskiNobody, cover: MitskiNobodyImage },
        { title: "No. 1 Party Anthem", artist: "Arctic Monkeys", src: No1PartyAnthem, cover: No1PartyAnthemImage },
        { title: "Not Allowed", artist: "TV Girl", src: NotAllowed, cover: NotAllowedImage },
        { title: "Need 2", artist: "Pinegrove", src: PinegroveNeed2, cover: PinegroveNeed2Image },
        { title: "Radio", artist: "Lana Del Rey", src: RadioLanaDelRey, cover: RadioLanaDelReyImage },
        { title: "All I Need", artist: "Radiohead", src: RadioheadAllINeed, cover: RadioheadAllINeedImage },
        { title: "Fake Plastic Trees", artist: "Radiohead", src: RadioheadFakePlasticTrees, cover: RadioheadFakePlasticTreesImage },
        { title: "Man of War", artist: "Radiohead", src: RadioheadManofWar, cover: RadioheadManofWarImage },
        { title: "Scott Street", artist: "Phoebe Bridgers", src: ScottStreet, cover: ScottStreetImage },
        { title: "Atlantis", artist: "Seafret", src: SeafretAtlantis, cover: SeafretAtlantisImage },
        { title: "Symphonia IX", artist: "Grimes", src: SymphoniaIX, cover: SymphoniaIXImage },
        { title: "exile", artist: "Taylor Swift", src: TaylorSwiftExile, cover: TaylorSwiftExileImage },
        { title: "Nobody New", artist: "The Marías", src: TheMariasNobodyNew, cover: TheMariasNobodyNewImage },
        { title: "The Tourist", artist: "Radiohead", src: TheTouristRadiohead, cover: TheTouristRadioheadImage },
        { title: "This Side of Paradise", artist: "Coyote Theory", src: ThisSideofParadiseCoyoteTheory, cover: ThisSideofParadiseCoyoteTheoryImage },
        { title: "TRADUÇÃO DE SAUDADE", artist: "BADLUV", src: TRADUCAODESAUDADE, cover: TRADUCAODESAUDADEImage },
        { title: "Telephones", artist: "Vacations", src: VacationsTelephones, cover: VacationsTelephonesImage }
    ];

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