import React, {Component} from 'react';
import './Auth.css';
import AuthContext from '../context/auth-context';


class AuthPage extends Component{
    state = {
        isLogin: true,
    };
    //how to touch login/logout function
    //import static authContext 
    static contextType = AuthContext;

    constructor(props){
        super(props);
        this.emailEl = React.createRef();
        this.passwordEl = React.createRef();

    }
    switchModeHandler = () =>{
        this.setState((prevState) =>{
            return {isLogin: !prevState.isLogin};
        });
    };
    submitHandle =(event) =>{
        event.preventDafault();
        const email = this.emailEl.current.value;
        const password = this.passwordEl.current.value;

        if(email.trim().length === 0 || password.trim().length === 0){
            return;
        }

        let requestBody = {
            query:`
                query {
                    login(loginInput : {
                        email: "${email}",
                        password: "${password}"
                    }){
                        useId,
                        token,
                        tokenExpination
                    
                }
                `,
                variables: {
                    email: email,
                    password: password,
                },
        };
        //not login use this method,register statusa,new user
        if(!this.state.isLogin){
            requestBody ={
                query: `
                    mutation CreateUser($email: String!,$password : String!){
                        createUser(userInput : {
                            email: "$email",
                            password: "$password"
                        }){
                            id
                            email
                        }
                    }
                `,
                variables: {
                    email: email,
                    password: password,
                },
    
            };
        }
        
        console.log(email, password);
        //backend request
        //GraphQL optimization
        fetch("http://localhost:8080/graphql",{
            method: "POST",
            body: JSON.stringify(requestBody),
            headers:{
                "Content-Type": "application/json",
            },
        }).then((res) =>{
            if(res.status !== 200 && res.status !== 201){
                throw new Error("Failed!");
            }
            return res.json();
        })
        .then((resData) => {
            console.log(resData);
            if(resData.data.login.token){
                this.context.login(
                    resData.data.login.token,
                    resData.data.login.userId,
                    resData.data.login.tokenExpiration
                );
            }
            
        })
        .catch((err) =>{
            console.log(err);
        });
    };

    render() {
        return (
            <form className="auth-form" onSubmit = {this.submitHandle}>
                <div className="form-control">
                    <label htmlFor="email">E-mail</label>
                    <input type="email" id="email" ref ={this.emailEl}/>
                </div>
                <div className="form-control">
                    <label htmlFor="password">Password</label>
                    <input type="password" id="password" ref = {this.passwordEl}/>
                </div>
                <div className="form-actions">
                    <button type="submit">Submit</button>
                    <button type= "button" onClick= {this.switchModeHandler}>
                        Switch to {this.state.isLogin ? "sighup" :"login"}
                    </button>
                </div>
            </form>
        );
    }

}
export default AuthPage;