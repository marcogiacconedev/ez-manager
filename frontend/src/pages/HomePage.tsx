import { useAuthStore } from "../store/useAuthStore";
import useTaskApi from "../hooks/useTaskApi";
import { useNavigate } from "react-router-dom";
import DropdownButton from "../components/DropdownButton";
import { useState } from "react";
import Header from "../components/Header";
import EmptyListRow from "../components/EmptyListRow";

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
                <Header
                    header="Hello,"
                    username={username}
                    isNavigationButtonVisible={false}
                ></Header>
                <div className="card-container">
                    <h2 className="header-2">Task</h2>
                    <div className="card">
                        <EmptyListRow
                            isRowVisible={tasks.length < 1}
                            text={'No tasks found ♫ ♪​'}
                        ></EmptyListRow>
                        { tasks.map((task) => (
                            <div key={task.id} className="task-display-row" onClick={() => navigate(`/tasks/create/${task.id}`)}>
                                <div className={`task-completed-led ${task.completedAt ? 'completed' : ''}`}></div>
                                <p className="task-display-item task-date">▶ {new Date(task.date).toDateString()}</p>
                                <p className="task-display-item task-name">▻ {task.name}</p>
                                {task.description !== '' && <p className="task-display-item task-description">▻ {task.description}</p>}
                                <hr className="task-line"/>
                            </div>
                        ))}
                    </div>
                </div>
                <div className="card-container">
                    <div className="card">
                        <DropdownButton 
                            header={'Sections'}
                            onOpen={() => {setIsSectionsDropdownOpen(!isSectionsDropdownOpen)}}
                            dropdownOpen={isSectionsDropdownOpen}
                            marginTop="0"
                            marginBottom="0"                            
                        ></DropdownButton>
                        {isSectionsDropdownOpen && (
                            <>
                                <button className="home-button" onClick={() => navigate('/tasks')}>Task</button>
                                <button className="home-button" onClick={() => navigate('/shopping')}>Shopping List</button>
                                <button className="home-button" onClick={() => navigate('/items')}>Shopping Items</button>
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
                            marginTop="0"
                            marginBottom="0"                            
                        ></DropdownButton>
                    </div>
                </div>
                <button 
                    className="logout-button" 
                    onClick={handleLogout}>                    
                ⏏</button>
            </div>
        </>
    )
}

export default HomePage;