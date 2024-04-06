import {Component} from 'react';
import {BrowserRouter,Routes,Route,Navigate} from 'react-router-dom';

import './App.css';
import AuthPage from './pages/Auth';
import EventsPage from './pages/Events';
import BookingsPage from './pages/Bookings';
import MainNavigation from './components/Navigation/MainNavigation';
import AuthContext from './context/auth-context';


class App extends Component{
    state = {
        token: null,
        userId: null,

    };
    login = (token, userId,tokenExpiration) =>{
        this.setState({token : token, userId : userId});
    };
    logout = () =>{
        this.setState({token: null, userId: null});
    };

    render(){
        return (
            <BrowserRouter>
                <AuthContext.Provider value={{
                    token: this.state.token,
                    userId: this.state.userId,
                    login: this.login,
                    logout: this.logout,
                }}>
                <MainNavigation />
                <main className='main-content'>
                <Routes>
                    {/**unlongin status return auth page */}
                    {/**Discuss on a case-by-case basis */}
                    {/**1.如果没登录，导航到登录页面 */}
                    {!this.status.token && (
                    <Route path="/" element = {<Navigate to="/auth"/>}/>
                    )}
                    {/** 2.如果登陆了，导航到events页面 */}
                    {this.status.token && (
                        <Route path="/" element = {<Navigate to="/events"/>}/>
                    )}
                    {/**3.如果导航到auth路径，有token，直接导航到events页面  */}
                    {this.status.token && (
                        <Route path="/auth" element = {<Navigate to="/events"/>}/>
                    )}
                    {/**4.如果没有token,访问auth,直接导航到authpage */}
                    {!this.status.token && (
                    <Route path="/auth" element = {<AuthPage />}/>
                    )}
                    <Route path="/events" element = {<EventsPage/>}/>
                    {this.status.token && (
                        <Route path="/bookings" element = {<BookingsPage/>}/>
                    )}
                    
                </Routes>
                </main>
                </AuthContext.Provider>
            </BrowserRouter>
        )
    }
}

export default App;