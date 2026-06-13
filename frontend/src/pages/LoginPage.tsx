import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore";

export interface LoginRequest {
	username: string,
	password: string
}

const LoginPage = (): React.ReactNode => {
	const navigate = useNavigate();
  	const setAuth = useAuthStore((state) => state.setAuth)
	const [error, setError] = useState<string>("");
	const [username, setUsername] = useState<string>("");
	const [password, setPassword] = useState<string>("");

	const login = async (): Promise<void> => {
		const baseUrl: string = import.meta.env.VITE_API_URL;
		try {
			const requestBody: LoginRequest = {
				username: username,
				password: password
			} 
			const response = await fetch(`${baseUrl}/auth/login`, {
				method: "POST",
				headers : {
					"Content-Type" : "application/json"
				},
				body: JSON.stringify(requestBody)
			})

			if (response.status !== 200) {
				setError("Errore nel Login");
				return
			} 

			setError("");
			const data = await response.json();
      		setAuth(data.username, data.token);

			navigate('/home');
			localStorage.setItem('ez-manager-jwt', data.token);

		} catch (error) {
			console.log(error);
		}
	}

	return (
		<>
			<div>
				<div className="header-container">
					<h1 className="header">Login</h1>
				</div>
				<div className="card-container">
					<div className="card">
						<input className="login-input" type="text" name="username" id="username" placeholder="username" onChange={e => {setUsername(e.target.value)}} />
						<input className="login-input" type="password" name="password" id="password" placeholder="password" onChange={e => {setPassword(e.target.value)}} />
						<button className="submit-button" onClick={login}>Login</button>
					</div>
					{ error && 
						<div className="error-message-container">
							<p className="error-message">{error}</p>
						</div>
					}
				</div>
			</div>
		</>
	)
}

export default LoginPage;