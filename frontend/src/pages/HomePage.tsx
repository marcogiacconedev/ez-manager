import { useAuthStore } from "../store/useAuthStore";
import useTaskApi from "../hooks/useTaskApi";
import { useNavigate } from "react-router-dom";
import DropdownButton from "../components/DropdownButton";
import { useState } from "react";

export interface Task {
    id: string,
    name: string,
    priority: number,
    wholeDay: boolean,
    subtaskOf: string | null,
    description: string | null,
    date: Date,
    createdAt: Date,
    completedAt: Date | null,
}

const HomePage = (): React.ReactNode => {
    const username: string | null = useAuthStore.getState().username;
    const token: string | null = useAuthStore.getState().token;
    const logout = useAuthStore((state) => state.logout);
    const navigate = useNavigate(); 
    const { tasks } = useTaskApi(0, 5);
    const [isSectionsDropdownOpen, setIsSectionsDropdownOpen] = useState<boolean>(false);
    const [isAlertsDropdownOpen, setisAlertsDropdownOpen] = useState<boolean>(false);
    const handleLogout = (): void => {
        logout();
        navigate('/login');
    }

    if (!token) {
        navigate('/login');
        return
    }

    return (
        <>
            <div>
                <div className="header-container">
                    <h1 className="header">Hello,</h1>
                    <h3 className="header-subtitle">{username}</h3>
                    <h3 className="header-subtitle">Today: {new Date().toDateString()}</h3>
                </div>
                <div className="card-container">
                    <h2 className="header-2">Task</h2>
                    <div className="card">
                        {
                            tasks.map((task) => (
                            <div key={task.id} className="task-display-row" onClick={() => navigate(`/tasks/create/${task.id}`)}>
                                <p className="task-display-item task-date">▶ {new Date(task.date).toDateString()}</p>
                                <p className="task-display-item task-name">▻ {task.name}</p>
                                <p className="task-display-item task-description">▻ {task.description}</p>
                                <hr className="task-line"/>
                            </div>
                            ))
                        }
                    </div>
                </div>
                <div className="card-container">
                    <div className="card">
                        <DropdownButton 
                            header={'Sections'}
                            onOpen={() => {setIsSectionsDropdownOpen(!isSectionsDropdownOpen)}}
                            dropdownOpen={isSectionsDropdownOpen}
                        ></DropdownButton>
                        {isSectionsDropdownOpen && (
                            <>
                                <button className="home-button" onClick={() => navigate('/tasks')}>Task</button>
                                <button className="home-button" onClick={() => navigate('/shopping')}>Shopping List</button>
                                <button className="home-button">Metrics</button>                            
                            </>
                        )}
                    </div>
                </div>
                <div className="card-container">
                    <div className="card">
                        <DropdownButton
                            header={'Alerts'}
                            onOpen={() => setisAlertsDropdownOpen(!isAlertsDropdownOpen)}
                            dropdownOpen={isAlertsDropdownOpen}
                        ></DropdownButton>
                    </div>
                </div>
                <button onClick={handleLogout}>logout</button>
            </div>
        </>
    )
}

export default HomePage;