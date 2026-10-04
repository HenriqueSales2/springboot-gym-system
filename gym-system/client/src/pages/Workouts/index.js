import { useState, useEffect } from "react";
import { Link , useNavigate } from 'react-router-dom';
import { FiPower, FiEdit, FiTrash2 } from 'react-icons/fi';
import MusicPlayer from "../../components/MusicPlayer/index.js";

import api from '../../services/api';

import './styles.css';
import '../../player.css';

import logoImage from '../../assets/images/logo.png';

export default function Workout() {

    const [workout, setWorkouts] = useState([]);
    const [page, setPage] = useState([0]);

    const username = localStorage.getItem('username');
    const accessToken = localStorage.getItem('accessToken');
            
    const navigate = useNavigate();

    async function logout() {
        localStorage.clear();
        navigate('/');
    }

    async function updateWorkout(id) {
        try {
            navigate(`/workouts/new/${id}`)
        } catch (error) {
            alert('Edit failed! Try again!')
        }
    }

    async function deleteWorkout(id) {
        try {
            await api.delete(`/api/workout/v1/${id}`, {
                headers: {
                Authorization: `Bearer ${accessToken}`
                }
            })

            setWorkouts(workout.filter(workout => workout.id !== id));
        } catch (error) {
            alert('Delete failed! Try again!')
        }
    }

    const header = {
        headers: {
        Authorization: `Bearer ${accessToken}`
        }
    };

    async function fetchMoreWorkouts() {
        try {
            const response = await api.get(`/api/workout/v1?page=${page}&size=40&direction=asc`, header);
            if(!response.data._embedded) return;
            setWorkouts([ ...workout, ...response.data._embedded.workouts]);
            setPage(page + 1);
        } catch (error) {
            console.error("Error loading workouts:", error);
        }
    }

    useEffect(() => {
    fetchMoreWorkouts();
    }, []); 

    return (
        <div className="workout-container">
            <header>
                <img src={logoImage} alt="GymLab"/>
                <span>Welcome the GymLab, <strong>{username.charAt(0).toUpperCase() + username.slice(1).toLowerCase()}</strong>!</span>
                <Link className="button" to="/workouts/new/0">Add New Workout</Link>
                <button onClick={() => logout()} type="button">
                    <FiPower size={18} color="#e4544b"/>    
                </button>
            </header>

            <h1>Registered Workouts</h1>
            <ul>
                {workout.map(workout => (
                    <li key={workout.id}>
                    <strong>Exercise Name:</strong>
                    <p>{workout.exerciseName}</p>
                    <strong>Muscle Group:</strong>
                    <p>{workout.muscleGroup}</p>
                    <strong>Equipment:</strong>
                    <p>{workout.equipment}</p>
                    <strong>Difficulty:</strong>
                    <p>{workout.difficulty}</p>
                        <button onClick={() => updateWorkout(workout.id)} type="button" className="edit">
                            <FiEdit size={20} color="#4053bd" />
                        </button>
                        <button onClick={() => deleteWorkout(workout.id)} type="button" className="delete">
                            <FiTrash2 size={20} color="#e4544b" />
                        </button>
                </li>
                ))}
            </ul>
            <button className="button" onClick={fetchMoreWorkouts} type="button">Load More</button>
            <MusicPlayer/>
        </div>
    );
}