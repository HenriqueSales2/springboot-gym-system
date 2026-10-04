import React, { useState } from "react";
import { useNavigate } from 'react-router-dom';
import MusicPlayer from '../../components/MusicPlayer';

import './styles.css';
import '../../player.css'; 

import api from '../../services/api.js';
import logoImage from '../../assets/images/logo.png';

export default function Login() {

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

            <MusicPlayer/>
        </div>
    );
}