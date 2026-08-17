import React from "react";
import {BrowserRouter, Route, Routes} from 'react-router-dom';

import Login from './pages/Login';
import Workout from './pages/Workouts';
import NewWorkout from './pages/NewWorkout';

export default function AppRoutes() {
    return (
        <BrowserRouter> 
            <Routes>
                <Route path="/" exact element={<Login/>}></Route>
                <Route path="/workouts" element={<Workout/>}></Route>
                <Route path="/workouts/new" element={<NewWorkout/>}></Route>
            </Routes>

        </BrowserRouter>
    );
}