import React from "react";
import {BrowserRouter, Route, Routes} from 'react-router-dom';

import Login from './pages/Login';
import Workout from './pages/Workout';
import NewWorkout from './pages/NewWorkout';

export default function AppRoutes() {
    return (
        <BrowserRouter> 
            <Routes>
                <Route path="/" exact element={<Login/>}></Route>
                <Route path="/workout" element={<Workout/>}></Route>
                <Route path="/workout/new/:workoutId" element={<NewWorkout/>}></Route>
            </Routes>
        </BrowserRouter>
    );
}