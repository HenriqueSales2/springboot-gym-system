import React from "react";
import './styles.css';

import padlock from '../../assets/padlock.png';
import logoImage from '../../assets/logo.png';

export default function Login() {
    return (
        <header>
            <div className="login-container"></div>
            <section className="form">
                <img src={logoImage} alt="Logo Gym System"/>
                <form>
                    <h1>Access your Account</h1>
                    <input placeholder="Username"/>
                    <input type="password" placeholder="Password"/>

                    <button type="submit">Login</button>
                </form>

            </section>

            <img src={padlock} alt="Login" />
        </header>
    );
}