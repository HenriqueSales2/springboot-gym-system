import  { useState, useEffect } from "react";
import { useNavigate, Link, useParams } from "react-router-dom";
import { FiArrowLeft } from 'react-icons/fi';
import api from '../../services/api';
import './styles.css';
import '../../player.css'; 

import logoImage from '../../assets/images/logo.png';
import MusicPlayer from "../../components/MusicPlayer/index.js";

export default function NewWorkout() {

        const [id, setId] = useState(null);    
        const [exerciseName, setExerciseName] = useState('');
        const [muscleGroup, setMuscleGroup] = useState('');
        const [equipment, setEquipment] = useState('');
        const [difficulty, setDifficulty] = useState('');

        const {workoutId} = useParams();

        const username = localStorage.getItem('username');
        const accessToken = localStorage.getItem('accessToken');
        
                const navigate = useNavigate();

                const header = {
                        headers: {
                        Authorization: `Bearer ${accessToken}`
                    }
                };

                async function loadWorkout() {
                    try {
                        const response = await api.get(`/api/workout/v1/${workoutId}`, header);
                        
                        setId(response.data.id);
                        setExerciseName(response.data.exerciseName);
                        setMuscleGroup(response.data.muscleGroup);
                        setEquipment(response.data.equipment);
                        setDifficulty(response.data.difficulty)

                    } catch (error) {
                        alert('Error recovering Workout! Try again!');
                        navigate('/workouts');
                    }
                }

                useEffect(() => {
                    if (workoutId === '0') return;
                    else loadWorkout();
                }, [workoutId])
        
                async function saveOrUpdate(e) {
                    e.preventDefault();

                    const data = {
                        exerciseName,
                        muscleGroup,
                        equipment,
                        difficulty,
                    };

                try {
                    if (workoutId === '0') await api.post('/api/workout/v1', data, header);
                    else {
                        data.id = id;
                        await api.put('/api/workout/v1', data, header);
                    }
                    
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
                    <h1>{workoutId === '0' ? 'Create New' : 'Edit'} Workout</h1>
                    <p>Enter the details for your new workout and click on the {workoutId === '0' ? "'Create'" : "'Edit'"} button.</p>
                    <Link className="back-link" to="/workouts">
                        <FiArrowLeft size={16} color="#E02041"/>
                        Back
                    </Link>
                </section>
                <form onSubmit={saveOrUpdate}>
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
                    <button className="button" type="submit">{workoutId === '0' ? 'Create' : 'Edit'}</button>
                </form>

                <MusicPlayer/>
                </div>
        </div>
    );
}