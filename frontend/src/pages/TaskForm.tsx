import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore";
import Calendar from "../components/Calendar";
import Header from "../components/Header";
import DropdownButton from "../components/DropdownButton";

export interface TaskRequest {
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
    const [isPriorityDropdownOpen, setIsPriorityDropdownOpen] = useState<boolean>(false);
    const [isCalendarOpen, setIsCalendarOpen] = useState<boolean>(true);
    const [isNotesOpen, setIsNotesOpen] = useState<boolean>(false);

    const [error, setError] = useState<string>("");
    const navigate = useNavigate();

    const onSelectDate = (date: Date | null): void => {
        if (date) {
            console.log(date);
            setDate(date);
        }
    }

    useEffect(() => {
        if (taskId) {
            fetch(`${import.meta.env.VITE_API_URL}/api/tasks/${taskId}`, {
                headers: { "Authorization": `Bearer ${token}` }
            })
            .then(res => res.json())
            .then(data => {
                setCompletedAt(data.completedAt);
                setDate(data.date ? new Date(data.date) : new Date());
                setDescription(data.description);
                setTaskName(data.name);
                setPriority(data.priority);
            })
        }
    }, [taskId, token])
        
    const submitForm = async (): Promise<void> => {
        const requestBody: TaskRequest = {
                name: taskName,
                description: description,
                date: date ? date : new Date(),
                wholeDay: true,
                priority: priority,
                subtaskOf: null,
                completedAt: completedAt
            }
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
                isNavigationButtonVisible={true}
            ></Header>
            <div className="card">
                <input 
                    type="text" 
                    placeholder="name" 
                    className="form-input" 
                    value={taskName} 
                    onChange={e => {setTaskName(e.target.value)}}
                    disabled={completedAt ? true : false}/>
                <DropdownButton
                    header={'Date'}
                    onOpen={() => setIsCalendarOpen(!isCalendarOpen)}
                    dropdownOpen={isCalendarOpen}
                    marginTop="1.5rem"
                    marginBottom="0"                    
                ></DropdownButton>
                { isCalendarOpen && (
                    <Calendar
                        selectedDate={date}
                        onSelectDate={onSelectDate}
                    />
                )}
                <DropdownButton
                    header={'Notes'}
                    onOpen={() => setIsNotesOpen(!isNotesOpen)}
                    dropdownOpen={isNotesOpen}
                    marginTop="1.5rem"
                    marginBottom="0"
                ></DropdownButton>
                { isNotesOpen && (
                    <textarea 
                        name="description" 
                        id="description" 
                        placeholder="description" 
                        className="form-textarea" 
                        value={description} 
                        onChange={e => {setDescription(e.target.value)}}
                        disabled={completedAt ? true : false}    
                    ></textarea>                
                )}
                <DropdownButton
                    header={'Priority'}
                    onOpen={() => setIsPriorityDropdownOpen(!isPriorityDropdownOpen)}
                    dropdownOpen={isPriorityDropdownOpen}
                    marginTop="1.5rem"
                    marginBottom={isPriorityDropdownOpen ? "0rem" : "1.5rem"}                    
                ></DropdownButton>
                {isPriorityDropdownOpen && (
                    <>
                    <div className="prio-row-container">
                        <button className="home-button" onClick={() => setPriority(1)}>1</button>
                        {priority === 1 && (<span className="prio-row-tick">✓</span>)}                
                    </div>
                    <div className="prio-row-container">
                        <button className="home-button" onClick={() => setPriority(2)}>2</button>
                        {priority === 2 && (<span className="prio-row-tick">✓</span>)}
                    </div>
                    <div className="prio-row-container">
                        <button className="home-button" onClick={() => setPriority(3)}>3</button>                            
                        {priority === 3 && (<span className="prio-row-tick">✓</span>)}
                    </div>
                    </>
                )}
                <div className="submit-form-container">
                    <button 
                        className={`mark-as-done-button ${completedAt ? 'done' : ''}`}
                        onClick={() => setCompletedAt(new Date())}>
                        {completedAt ? 'Task completed ✓' : 'Mark as completed'}
                    </button>
                    <button 
                        className="submit-form-button" 
                        onClick={submitForm}
                        disabled={completedAt ? true : false}    
                    >{taskId ? 'Apply Changes' : 'Submit'}</button>
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