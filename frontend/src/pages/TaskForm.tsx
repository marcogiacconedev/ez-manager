import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore";
import Calendar from "../components/Calendar";
import PriorityPicker from "../components/PriorityPicker";

export interface TaskRequestBody {
    name: string,
    description: string,
    date: Date,
    wholeDay: boolean | null,
    priority: number | null,
    subtaskOf: string | null
}

const TaskForm = (): React.ReactNode => {
    const { taskId } = useParams<{ taskId: string }>();
    const token = useAuthStore.getState().token
    const [completedAt, setCompletedAt] = useState<Date | null>(null);
    const [date, setDate] = useState<Date | null>(new Date());
    const [description, setDescription] = useState<string>("");
    const [taskName, setTaskName] = useState<string>("");
    const [priority, setPriority] = useState<number>(0);
    const [wholeDay, setWholeDay] = useState<boolean>(false);
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
                setWholeDay(data.wholeDay);
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
                subtaskOf: null
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
            <div>
                <div className="header-container">
                    <h1 className="header">Task</h1>
                    <h3 className="header-subtitle">Today: {new Date().toDateString()}</h3>
                </div>
            </div>
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
                <button className="submit-form-button" onClick={submitForm}>{taskId ? 'Apply Changes' : 'Submit'}</button>
                	{ error && 
						<div className="error-message-container">
							<p className="error-message">{error}</p>
						</div>
					}
            </div>
        </>
    )
}

export default TaskForm;