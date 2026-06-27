import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore";
import Calendar from "../components/Calendar";
import PriorityPicker from "../components/PriorityPicker";
import Header from "../components/Header";

export interface TaskRequestBody {
    name: string,
    description: string,
    date: Date,
    wholeDay: boolean | null,
    priority: number | null,
    subtaskOf: string | null,
    completedAt: Date | null
}

const TaskForm = (): React.ReactNode => {
    const { taskId } = useParams<{ taskId: string }>();
    const token = useAuthStore.getState().token
    const [completedAt, setCompletedAt] = useState<Date | null>(null);
    const [date, setDate] = useState<Date | null>(new Date());
    const [description, setDescription] = useState<string>("");
    const [taskName, setTaskName] = useState<string>("");
    const [priority, setPriority] = useState<number>(1);
    const [error, setError] = useState<string>("");
    const navigate = useNavigate();

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
                setCompletedAt(data.completedAt);
            })
        }      
    }, [])

    const onSelectDate = (date: Date | null): void => {
        if (date) {
            setDate(date);
        }
    }
        
    const submitForm = async (): Promise<void> => {
        const requestBody: TaskRequestBody = {
                name: taskName,
                description: description,
                date: date ? date : new Date(),
                wholeDay: true,
                priority: priority,
                subtaskOf: null,
                completedAt: completedAt
            }
        console.log(requestBody, 'request body');
        const requestUrl = `${import.meta.env.VITE_API_URL}/api/tasks`;
        const url = taskId ? `${requestUrl}/${taskId}` : requestUrl;
        const method: string = taskId ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method ,
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.json();

        if (data.id) {
            navigate('/tasks');
        } else {
            setError("Operazione non riuscita");
        }
    }

    return (
        <>
            <Header
                header="Task"
                username={null}
            ></Header>
            <div className="card">
                <input type="text" placeholder="name" className="form-input" value={taskName} onChange={e => {setTaskName(e.target.value)}}/>
                <textarea name="description" id="description" placeholder="description" className="form-textarea" value={description} onChange={e => {setDescription(e.target.value)}}></textarea>
                <hr className="task-line"/>
                <Calendar
                    selectedDate={date}
                    onSelectDate={onSelectDate}
                />
                <PriorityPicker
                    onSelect={(value) => setPriority(value)}
                    priority={priority}
                ></PriorityPicker>
                <div className="submit-form-container">
                    <button 
                        className={`mark-as-done-button ${completedAt ? 'done' : ''}`}
                        onClick={() => setCompletedAt(new Date())}>
                        {completedAt ? 'Task completed ✓' : 'Mark as completed'}
                    </button>
                    <button className="submit-form-button" onClick={submitForm}>{taskId ? 'Apply Changes' : 'Submit'}</button>
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

export default TaskForm;