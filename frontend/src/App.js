import {Component} from 'react';
import {BrowserRouter,Routes,Route,Navigate} from 'react-router-dom';

import './App.css';
import AuthPage from './pages/auth';
import EventsPage from './pages/Events';
import BookingsPage from './pages/Bookings';
import MainNavigation from './components/Navigation/MainNavigation';


class App extends Component{
    render(){
        return (
            <BrowserRouter>
            <MainNavigation />
            <main className='main-content'>
            <Routes>
                <Route path="/" element = {<Navigate to="/auth"/>}/>
                <Route path="/auth" element = {<AuthPage/>}/>
                <Route path="/events" element = {<EventsPage/>}/>
                <Route path="/bookings" element = {<BookingsPage/>}/>
            </Routes>
            </main>
            </BrowserRouter>
        )
    }
}

export default App;