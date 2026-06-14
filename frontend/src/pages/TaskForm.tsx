import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore";
import Calendar from "../components/Calendar";
import PriorityPicker from "../components/PriorityPicker";

const TaskForm = (): React.ReactNode => {
    const { taskId } = useParams<{ taskId: string }>();
    const token = useAuthStore.getState().token
    const [completedAt, setCompletedAt] = useState<Date | null>(null);
    const [date, setDate] = useState<Date | null>(null);
    const [description, setDescription] = useState<string>("");
    const [taskName, setTaskName] = useState<string>("");
    const [priority, setPriority] = useState<number>(0);
    const [wholeDay, setWholeDay] = useState<boolean>(false);


    useEffect(() => {
        if (taskId) {
            fetch(`${import.meta.env.VITE_API_URL}/api/tasks/${taskId}`, {
            headers: { "Authorization": `Bearer ${token}`}
            })
            .then(res => res.json())
            .then(data => {
                setCompletedAt(data.completedAt);
                setDate(data.date);
                setDescription(data.description);
                setTaskName(data.name);
                setPriority(data.priority);
                setWholeDay(data.wholeDay);
            })
        }      
    }, [])
    return (
        <>
            <div>
                <div className="header-container">
                    <h1 className="header">Task</h1>
                    <h3 className="header-subtitle">Oggi: {new Date().toDateString()}</h3>
                </div>
            </div>
            <div className="card">
                <input type="text" placeholder="name" className="form-input" value={taskName} onChange={e => {setTaskName(e.target.value)}}/>
                <textarea name="description" id="description" placeholder="description" className="form-textarea" value={description} onChange={e => {setDescription(e.target.value)}}></textarea>
                <hr className="task-line"/>
                <Calendar></Calendar>
                <PriorityPicker
                    onSelect={(value) => setPriority(value)}
                    priority={priority}
                ></PriorityPicker>
            </div>
        </>
    )
}

export default TaskForm;