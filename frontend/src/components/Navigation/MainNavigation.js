import {NavLink} from "react-router-dom";
import "./MainNavigation.css";
import AuthContext from "../../context/auth-context";

const MainNavigation = (props) =>(
    <AuthContext.Consumer>
        {(context) =>{
            return (
                <header className="main-navigation">
                    <div className="main-navigation__logo">
                        <h1>EasyEvent</h1>
                    </div>
                    <nav className="main-navigation__items">
                        <ul>
                            {/** context没有token时可以看到authpage*/}
                            {!context.token && (
                                <li>
                                    <NavLink to ="/auth">Authentication</NavLink>
                                </li>
                            )}
                            <li>
                                <NavLink to ="/events">Events</NavLink>
                            </li>
                            {/**只有token表示登陆成功才可以booking */}
                            {context.token && (
                                <React.Fragment>
                                    <li>
                                        <NavLink to ="/bookings">Bookings</NavLink>
                                    </li>
                                    <li>
                                        <button onClick={context.logout}>Logout</button>
                                    </li>
                                </React.Fragment>
                                
                            )}
                        </ul>
                    </nav>
                </header>
            );
        }}
    </AuthContext.Consumer>
);
export default MainNavigation;